package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zkvj.conjurers.core.Board;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameModel;
import com.zkvj.conjurers.core.GameModel.GameModelListener;
import com.zkvj.conjurers.core.Minion;
import com.zkvj.conjurers.core.Well;
import com.zkvj.utils.BufferedImageComponent;

/**
 * Class representing the board in Conjurers.
 */
public class BoardPanel extends BufferedImageComponent
{
   private static final long serialVersionUID = -1194634150725552094L;
   
   /** the game model */
   private final GameModel _model;
   
   /** the player represented as WHITE */
   private int _playerID;
   
   /** the player represented as BLACK */
   private int _opponentID;
   
   /** panel only allow certain features while it is our turn */
   private boolean _myTurn;
   
   private final MouseMotionListener _mouseMotionListener = new MouseMotionListener()
   {
      @Override
      public void mouseMoved(MouseEvent aEvent)
      {
         // convert from screen coords to board coords
         Point tBoardPos = screenToBoard(aEvent.getPoint());

         if(null != tBoardPos && null != getBoard())
         {
            // for testing only
//            FocusCardMgr.setFocusCard(Card.createSpellCard(-1,
//                  tBoardPos.toString(), 0, Rarity.ePLATINUM, ""));

            Minion tMinion = getBoard().getMinion(tBoardPos);

            if(null != tMinion)
            {
               FocusCardMgr.setFocusCard(tMinion.getCard());
            }
         }
      }
      
      @Override
      public void mouseDragged(MouseEvent aEvent)
      {}
   };
   
   private final GameModelListener _modelListener = new GameModelListener()
   {
      @Override
      public void gameDataChanged()
      {
         updateBufferedImage();
         repaint();
      }
   };
   
   /**
    * Constructor
    * @param aModel - the game data model
    * @param aPlayerID - which player should be represented as white
    */
   public BoardPanel(GameModel aModel, int aPlayerID)
   {
      _model = aModel;
      _model.addListener(_modelListener);
      
      _playerID = aPlayerID;
      
      _opponentID = Conjurer.kPLAYER_A;
      if(Conjurer.kPLAYER_A == _playerID)
      {
         _opponentID = Conjurer.kPLAYER_B;
      }
      
      this.addMouseMotionListener(_mouseMotionListener);
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
            Path2D.Double tOuterHex = new Path2D.Double();
            Path2D.Double tHex = new Path2D.Double();
            
            double tHexCenterX = tBoardCenterX + tEntry.getKey().x * tDx + tEntry.getKey().y * tDx / 2;
            double tHexCenterY = tBoardCenterY + tEntry.getKey().y * tDy;
            
            for(int tIndex = 0; tIndex < 7; tIndex++)
            {
               double tAngle = (Math.PI / 6) + tIndex * (Math.PI / 3);
               
                double tScreenXOuter = tHexCenterX + (tHexRadius+2) * Math.cos(tAngle);
                double tScreenYOuter = tHexCenterY + (tHexRadius+2) * Math.sin(tAngle);
                
                double tScreenX = tHexCenterX + (tHexRadius-1) * Math.cos(tAngle);
                double tScreenY = tHexCenterY + (tHexRadius-1) * Math.sin(tAngle);
               
               if(tIndex == 0)
               {
                  tOuterHex.moveTo(tScreenXOuter, tScreenYOuter);
                  tHex.moveTo(tScreenX, tScreenY);
               }
               else
               {
                  tOuterHex.lineTo(tScreenXOuter, tScreenYOuter);
                  tHex.lineTo(tScreenX, tScreenY);
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
}
