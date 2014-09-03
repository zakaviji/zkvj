package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.core.Board;
import com.zkvj.conjurers.core.Card;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameData;
import com.zkvj.conjurers.core.GameModel;
import com.zkvj.conjurers.core.GameModel.GameModelListener;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;
import com.zkvj.conjurers.core.Minion;
import com.zkvj.conjurers.core.Well;
import com.zkvj.utils.BufferedImageComponent;

/**
 * Class representing the board in Conjurers.
 */
public class BoardPanel extends BufferedImageComponent
{
   private static final long serialVersionUID = -1194634150725552094L;
   
   private final Client _client;
   private final GameModel _model;
   
   /** the player represented as WHITE */
   private int _playerID;
   
   /** the player represented as BLACK */
   private int _opponentID;
   
   /** panel only allow certain features while it is our turn */
   private boolean _myTurn;
   
   /** used for moving minions by dragging */
   private boolean _dragging;
   private Point _dragOrigin;
   
   /**
    * Perform actions based on mouse events:
    * - With a card selected, click on an empty space on the board to play that card
    * - Right click to toggle control of a space.
    * - Click and drag a minion to move it (drag off the board to get rid of the minion).
    * - Mouse wheel to change health of a minion.
    */
   private final MouseAdapter _mouseAdaptor = new MouseAdapter()
   {
      @Override
      public void mouseClicked(MouseEvent aEvent)
      {
         if(_myTurn)
         {
            Point tBoardPos = screenToBoard(aEvent.getPoint());
            
            if(null != tBoardPos)
            {
               boolean tChanged = false;
               Card tCard = SelectionMgr.getSelectedCard();
               
               //left mouse button with card selected
               if(MouseEvent.BUTTON1 == aEvent.getButton() &&
                  null != tCard &&
                  _model.getGameData().getPlayer(_playerID).getEnergy() >= tCard.getEnergyCost())
               {
                  Minion tMinion = getBoard().getMinion(tBoardPos);
                  
                  //if there is not a minion here, play it
                  if(null == tMinion)
                  {
                     tMinion = _model.getGameData().playCardFromHand(_playerID, tCard);
                     tChanged = true;
                     
                     //if we got a minion, put it on the board
                     if(null != tMinion)
                     {
                        getBoard().setMinion(tBoardPos, tMinion);
                     }
                  }
                  
                  if(tChanged)
                  {
                     SelectionMgr.playSelectedCard();
                  }
               }
               //right mouse button: cycle through controller IDs
               else if(MouseEvent.BUTTON3 == aEvent.getButton())
               {
                  Well tWell = getBoard().getWell(tBoardPos);
                  
                  int tController = (tWell.controllerID == Conjurer.kNONE)?
                        _playerID : Conjurer.kNONE;
                  
                  getBoard().setWell(tBoardPos, new Well(tWell.elementType, tController));
                  
                  tChanged = true;
               }
               
               if(tChanged)
               {
                  updateFromModel();
                  
                  Message tGameDataMsg = new Message(Type.eGAME_DATA);
                  tGameDataMsg.gameData = new GameData(_model.getGameData());
                  _client.sendMessage(tGameDataMsg);
               }
            }
         }
      }
      
      @Override
      public void mousePressed(MouseEvent aEvent)
      {
         if(_myTurn &&
            !_dragging &&
            MouseEvent.BUTTON1 == aEvent.getButton())
         {
            Point tBoardPos = screenToBoard(aEvent.getPoint());
            Minion tMinion = getBoard().getMinion(tBoardPos);
            
            //if mouse pressed on a minion
            if(null != tBoardPos &&
               null != tMinion)
            {
               _dragging = true;
               _dragOrigin = tBoardPos;
            }
         }
      }
      
      @Override
      public void mouseReleased(MouseEvent aEvent)
      {
         if(_myTurn &&
            _dragging &&
            MouseEvent.BUTTON1 == aEvent.getButton())
         {
            _dragging = false;
            boolean tChanged = false;
            
            Point tBoardPos = screenToBoard(aEvent.getPoint());
            Minion tMinion = getBoard().getMinion(tBoardPos);
            
            //mouse released on a different, valid space with no minion
            if(null != tBoardPos &&
               null == tMinion &&
               tBoardPos != _dragOrigin)
            {
               tMinion = getBoard().getMinion(_dragOrigin);
               
               getBoard().setMinion(_dragOrigin, null);
               getBoard().setMinion(tBoardPos, tMinion);
               tChanged = true;
            }
            //mouse released off the board, remove minion
            else if(null == tBoardPos)
            {
               getBoard().setMinion(_dragOrigin, null);
               tChanged = true;
            }
            
            if(tChanged)
            {
               updateFromModel();
               
               Message tGameDataMsg = new Message(Type.eGAME_DATA);
               tGameDataMsg.gameData = new GameData(_model.getGameData());
               _client.sendMessage(tGameDataMsg);
            }
         }
      }
      
      @Override
      public void mouseWheelMoved(MouseWheelEvent aEvent)
      {
         if(_myTurn)
         {
            Point tBoardPos = screenToBoard(aEvent.getPoint());
            Minion tMinion = getBoard().getMinion(tBoardPos);
            
            //if mouse wheeled on a minion
            if(null != tBoardPos &&
               null != tMinion)
            {
               int tNewHealth = Math.max(0, tMinion.getHealth() - aEvent.getWheelRotation());
               
               if(tNewHealth != tMinion.getHealth())
               {
                  //create a copy so object stream will see a change
                  tMinion = new Minion(tMinion);
                  tMinion.setHealth(tNewHealth);
                  getBoard().setMinion(tBoardPos, tMinion);
                  
                  updateFromModel();
                  
                  Message tGameDataMsg = new Message(Type.eGAME_DATA);
                  tGameDataMsg.gameData = new GameData(_model.getGameData());
                  _client.sendMessage(tGameDataMsg);
               }
            }
         }
      }

      @Override
      public void mouseMoved(MouseEvent aEvent)
      {
         Point tBoardPos = screenToBoard(aEvent.getPoint());
         Minion tMinion = getBoard().getMinion(tBoardPos);

         if(null != tBoardPos &&
            null != tMinion)
         {
            SelectionMgr.setFocusCard(tMinion.getCard());
         }
      }
   };
   
   /** repaint upon game data changes */
   private final GameModelListener _modelListener = new GameModelListener()
   {
      @Override
      public void gameDataChanged()
      {
         updateFromModel();
      }
   };
   
   /**
    * Constructor
    * @param aClient - the client
    * @param aModel - the game data model
    * @param aPlayerID - which player should be represented as white
    */
   public BoardPanel(Client aClient, GameModel aModel, int aPlayerID)
   {
      _client = aClient;
      _model = aModel;
      _model.addListener(_modelListener);
      
      _playerID = aPlayerID;
      
      _opponentID = Conjurer.kPLAYER_A;
      if(Conjurer.kPLAYER_A == _playerID)
      {
         _opponentID = Conjurer.kPLAYER_B;
      }
      
      this.addMouseListener(_mouseAdaptor);
      this.addMouseMotionListener(_mouseAdaptor);
      this.addMouseWheelListener(_mouseAdaptor);
   }
   
   /**
    * Draws this display component.
    * @param aG
    */
   //@Override
   public void draw(Graphics2D aG)
   {
      if(null != getBoard())
      {
         //may need to draw the board upside down, if we are player B
         int tPlayerDir = (Conjurer.kPLAYER_A == _playerID)? 1 : -1;
         
         //System.out.println(_data.getBoard().toString());
         
         int tWidth = getWidth();
         int tHeight = getHeight();
         
         //magic formula for finding ideal hex radius given component size and number of rings
         double tHexRadius = Math.min(
            tHeight/(3*Constants.kHEX_RINGS + 2),
            tWidth/(1 + 2*Math.sqrt(3.0)*Constants.kHEX_RINGS));
         
         //then make it 20% bigger
         tHexRadius *= 1.2;
         
         double tHexWidth = tHexRadius * Math.sqrt(3.0);
         double tHexHeight = tHexRadius * 2;
         
         double tDx = tPlayerDir * tHexWidth;
         double tDy = tPlayerDir * .75 * tHexHeight;
         
         double tBoardCenterX = tWidth / 2;
         double tBoardCenterY = tHeight / 2;
         
         //Draw the hexagonal spaces
         for(Map.Entry<Point, Well> tEntry : getBoard().getWells())
         {
            Point tBoardPos = tEntry.getKey();
            
            Path2D.Double tOuterHex = new Path2D.Double();
            Path2D.Double tHex = new Path2D.Double();
            Path2D.Double tInnerHex = new Path2D.Double();
            
            double tHexCenterX = tBoardCenterX + tBoardPos.x * tDx + tBoardPos.y * tDx / 2;
            double tHexCenterY = tBoardCenterY + tBoardPos.y * tDy;
            
            for(int tIndex = 0; tIndex < 7; tIndex++)
            {
               double tAngle = (Math.PI / 6) + tIndex * (Math.PI / 3);
               
                double tScreenXOuter = tHexCenterX + (tHexRadius+2) * Math.cos(tAngle);
                double tScreenYOuter = tHexCenterY + (tHexRadius+2) * Math.sin(tAngle);
                
                double tScreenX = tHexCenterX + (tHexRadius-1) * Math.cos(tAngle);
                double tScreenY = tHexCenterY + (tHexRadius-1) * Math.sin(tAngle);
                
                double tInnerScreenX = tHexCenterX + (tHexRadius-10) * Math.cos(tAngle);
                double tInnerScreenY = tHexCenterY + (tHexRadius-10) * Math.sin(tAngle);
               
               if(tIndex == 0)
               {
                  tOuterHex.moveTo(tScreenXOuter, tScreenYOuter);
                  tHex.moveTo(tScreenX, tScreenY);
                  tInnerHex.moveTo(tInnerScreenX, tInnerScreenY);
               }
               else
               {
                  tOuterHex.lineTo(tScreenXOuter, tScreenYOuter);
                  tHex.lineTo(tScreenX, tScreenY);
                  tInnerHex.lineTo(tInnerScreenX, tInnerScreenY);
               }
            }
            
            //outline
            aG.setColor(Color.BLACK);
            aG.fill(tOuterHex);
            
            //color denotes the element of the hex
            aG.setColor(tEntry.getValue().elementType.getColor());
            aG.fill(tHex);
            
            //shading denotes which player has control of this space, if any
            if(tEntry.getValue().controllerID != Conjurer.kNONE)
            {
               Color tShade = (tEntry.getValue().controllerID == _playerID)?
                     Constants.kPLAYER_SHADE : Constants.kOPPONENT_SHADE;
               
               aG.setColor(tShade);
               aG.fill(tHex);
            }
            
            //if there is a minion here, draw inner hex and text
            Minion tMinion = getBoard().getMinion(tBoardPos);
            if(null != tMinion)
            {
               Color tMinionBkgd = (tMinion.getController() == _playerID)?
                     Constants.kPLAYER_COLOR : Constants.kOPPONENT_COLOR;
               aG.setColor(tMinionBkgd);
               aG.fill(tInnerHex);
               
               Color tTextColor = (tMinion.getController() == _playerID)?
                     Constants.kOPPONENT_COLOR : Constants.kPLAYER_COLOR;
               aG.setColor(tTextColor);
               
               int tCenterX = Math.round((float)tHexCenterX);
               int tCenterY = Math.round((float)tHexCenterY);
               
               String tName = tMinion.getName();
               String tPH = tMinion.getPower() + " / " + tMinion.getHealth();
               int tFontSize = aG.getFont().getSize();
               
               int tNameX = tCenterX - tName.length() * tFontSize/4;
               int tNameY = tCenterY;
               
               int tPHX = tCenterX - tPH.length() * tFontSize/4;
               int tPHY = tCenterY + tFontSize;
               
               aG.drawString(tName, tNameX, tNameY);
               aG.drawString(tPH, tPHX, tPHY);
            }
         }
         
         //Draw the conjurer spaces
         List<Point> tConjurerSpaces = new ArrayList<Point>();
         tConjurerSpaces.add(getBoard().getPlayerPosition(_playerID));
         tConjurerSpaces.add(getBoard().getPlayerPosition(_opponentID));
         
         for(int tJndex = 0; tJndex < tConjurerSpaces.size(); tJndex++)
         {
            Point tSpace = tConjurerSpaces.get(tJndex);
            
            Path2D.Double tDiamond = new Path2D.Double();
   
            double tHexCenterX = tBoardCenterX + tSpace.x * tDx + tSpace.y * tDx / 2;
            double tHexCenterY = tBoardCenterY + tSpace.y * tDy;
            
            tDiamond.moveTo(tHexCenterX, tHexCenterY);
            
            for(int tIndex = 0; tIndex < 3; tIndex++)
            {
               double tAngle = (Math.PI * 7 / 6) + tIndex * (Math.PI / 3);
               if(tSpace.y < 0)
               {
                  tAngle = (Math.PI / 6) + tIndex * (Math.PI / 3);
               }
               
               double tScreenX = tHexCenterX + (tHexRadius-4) * Math.cos(tAngle) * tPlayerDir;
               double tScreenY = tHexCenterY + (tHexRadius-4) * Math.sin(tAngle) * tPlayerDir;
               
               tDiamond.lineTo(tScreenX, tScreenY);
            }
            tDiamond.closePath();
            
            aG.setColor(Constants.kPLAYER_COLOR);
            if(tJndex > 0)
            {
               aG.setColor(Constants.kOPPONENT_COLOR);
            }
            aG.fill(tDiamond);
         }
      }
//      else
//      {
//         System.out.println("BoardPanel.draw(): WARNING: game data was null");
//      }
   }
   
   /**
    * Convenience method for getting the board from the game model.
    * @return Board
    */
   private Board getBoard()
   {
      Board tReturn = null;
      
      if(null != _model &&
         null != _model.getGameData())
      {
         tReturn = _model.getGameData().getBoard();
      }
      
      return tReturn;
   }

   /**
    * Enables/disables certain features of this panel based on
    * whether or not it is our turn
    */
   public void setIsMyTurn(boolean aMyTurn)
   {
      _myTurn = aMyTurn;
   }
   
   /**
    * Converts QR hexagonal board position to the XY screen position which represents
    * the center of that hex (with double precision).
    * @param aBoard
    * @return Point2D
    */
   private Point2D.Double boardToScreen(Point aBoard)
   {
      Point2D.Double tReturn = null;
      
      if(null != aBoard)
      {
         //may need to flip things upside down, if we are player B
         int tPlayerDir = (Conjurer.kPLAYER_A == _playerID)? 1 : -1;
         
         int tWidth = getWidth();
         int tHeight = getHeight();
         
         //magic formula for finding ideal hex radius given component size and number of rings
         double tHexRadius = Math.min(
            tHeight/(3*Constants.kHEX_RINGS + 2),
            tWidth/(1 + 2*Math.sqrt(3.0)*Constants.kHEX_RINGS));
         
         //then make it 20% bigger
         tHexRadius *= 1.2;
         
         double tHexWidth = tHexRadius * Math.sqrt(3.0);
         double tHexHeight = tHexRadius * 2;
         
         double tDx = tPlayerDir * tHexWidth;
         double tDy = tPlayerDir * .75 * tHexHeight;
         
         double tBoardCenterX = tWidth / 2;
         double tBoardCenterY = tHeight / 2;
         
         double tHexCenterX = tBoardCenterX + aBoard.x * tDx + aBoard.y * tDx / 2;
         double tHexCenterY = tBoardCenterY + aBoard.y * tDy;
         
         tReturn = new Point2D.Double(tHexCenterX, tHexCenterY);
      }
      
      return tReturn;
   }
   
   /**
    * Converts an XY screen position to the QR hexagonal board position of the hex
    * in which that screen position lies. If screen position is off the board, returns null.
    * @param aScreen
    * @return Point
    */
   public Point screenToBoard(Point aScreen)
   {
      Point tReturn = null;
      
      if(null != aScreen)
      {
         for(Point tBoardPos : getBoard().getPositions())
         {
            //convert from board coords to screen coords
            Point2D.Double tHexCenter = boardToScreen(tBoardPos);
            
            if(null != tHexCenter)
            {
               double tDistance = Point2D.distance(aScreen.x, aScreen.y, tHexCenter.x, tHexCenter.y);
               
               //magic formula for finding ideal hex radius given component size and number of rings
               double tHexRadius = Math.min(
                     getHeight()/(3*Constants.kHEX_RINGS + 2),
                     getWidth()/(1 + 2*Math.sqrt(3.0)*Constants.kHEX_RINGS));
               
               double tInnerRadius = tHexRadius * Math.sqrt(3.0)/2;
               
               //found it!
               if(tDistance < tInnerRadius)
               {
                  tReturn = tBoardPos;
                  break;
               }
            }
         }
      }
            
      return tReturn;
   }
   
   /**
    * Updates this panel with latest data from model.
    */
   private void updateFromModel()
   {
      updateBufferedImage();
      repaint();
   }
}
