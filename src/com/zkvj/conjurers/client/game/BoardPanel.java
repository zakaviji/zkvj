package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zkvj.conjurers.core.Board;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameModel;
import com.zkvj.conjurers.core.Well;
import com.zkvj.conjurers.core.GameModel.GameModelListener;
import com.zkvj.utils.BufferedImageComponent;

/**
 * Class representing the board in Conjurers.
 */
public class BoardPanel extends BufferedImageComponent
{
   private static final long serialVersionUID = -1194634150725552094L;
   
   /** the game model */
   private final GameModel _model;
   
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
    */
   public BoardPanel(GameModel aModel)
   {
      _model = aModel;
      _model.addListener(_modelListener);
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
         
         double tDx = tHexWidth;
         double tDy = .75 * tHexHeight;
         
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
            aG.setColor(tEntry.getValue().getElementType().getColor());
            aG.fill(tHex);
            
            //shading denotes which player has control of this space
//            aG.setColor(Constants.kPLAYER_ONE_SHADE);
//            if(0 == (int)Math.floor(2*Math.random()))
//            {
//               aG.setColor(Constants.kPLAYER_TWO_SHADE);
//            }
//            aG.fill(tHex);
         }
         
         //Draw the conjurer spaces
         List<Point> tConjurerSpaces = new ArrayList<Point>();
         tConjurerSpaces.add(getBoard().getPlayerPosition());
         tConjurerSpaces.add(getBoard().getOpponentPosition());
         
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
               
               double tScreenX = tHexCenterX + (tHexRadius-4) * Math.cos(tAngle);
               double tScreenY = tHexCenterY + (tHexRadius-4) * Math.sin(tAngle);
               
               tDiamond.lineTo(tScreenX, tScreenY);
            }
            tDiamond.closePath();
            
            aG.setColor(Constants.kPLAYER_ONE_COLOR);
            if(tJndex > 0)
            {
               aG.setColor(Constants.kPLAYER_TWO_COLOR);
            }
            aG.fill(tDiamond);
         }
      }
      else
      {
         System.out.println("BoardPanel.draw(): WARNING: game data was null");
      }
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
    * Enables/disables this panel based on whether or not it is our turn
    */
   @Override
   public void setEnabled(boolean aEnabled)
   {
      super.setEnabled(aEnabled);
      
      //todo?
   }
}
