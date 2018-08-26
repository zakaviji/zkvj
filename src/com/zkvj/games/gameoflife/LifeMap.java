package com.zkvj.games.gameoflife;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.HashMap;

public class LifeMap
{
   /**
    * Initialize the map
    */
   public void initialize()
   {
      //
   }
   
   /**
    * Renders the cells in this map to the given graphics object.
    * @param aG
    */
   public void draw(Graphics2D aG)
   {
      drawGrid(aG);
      
      //draw filled cells
   }
   
   /**
    * Draw the grid lines for all cells.
    * @param aG
    */
   public void drawGrid(Graphics2D aG)
   {
      aG.setColor(Color.BLACK);
      
      double tSlantX = Constants.kHEIGHT / Math.sqrt(3);
      
      double tDx = Constants.kCELL_SIZE;
      double tDy = Constants.kCELL_SIZE * Math.sqrt(3)/2;
      
      Path2D.Double tPath = new Path2D.Double();
      for(int i = 0; i < (Constants.kWIDTH + tSlantX)/Constants.kCELL_SIZE; i++)
      {
         tPath.moveTo(i*Constants.kCELL_SIZE - tSlantX, 0);
         tPath.lineTo(i*Constants.kCELL_SIZE, Constants.kHEIGHT);
      }
      for(int i = 0; i < (Constants.kWIDTH + tSlantX)/Constants.kCELL_SIZE; i++)
      {
         tPath.moveTo(i*Constants.kCELL_SIZE, 0);
         tPath.lineTo(i*Constants.kCELL_SIZE - tSlantX, Constants.kHEIGHT);
      }
      for(int i = 0; i < (Constants.kHEIGHT/tDy); i++)
      {
         tPath.moveTo(0, i*tDy);
         tPath.lineTo(Constants.kWIDTH, i*tDy);
      }
      aG.draw(tPath);
   }

   /**
    * Set the mouseover cell based on the given coordinates.
    * @param aX
    * @param aY
    */
   public void setMouseover(int aX, int aY)
   {
      _map.clear();
      _map.put(new Point(aX/Constants.kCELL_SIZE,
                         aY/Constants.kCELL_SIZE), true);
   }
   
   private final HashMap<Point, Boolean> _map = 
         new HashMap<Point, Boolean>();
}
