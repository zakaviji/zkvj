/******************************************************************************
 * @file CatanBoard.java
 *
 * @brief Class representing the board in Catan.
 *
 * @author glasscock
 *
 * @created Aug 13, 2013
 *****************************************************************************/

package com.zkvj.games.catan;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;

public class CatanBoard
{  
   /** array of pre-calculated offsets for neighbor hexes */
   private static final Point[] kNeighbors = {new Point( 1, 0),
                                              new Point( 1,-1),
                                              new Point( 0,-1),
                                              new Point(-1, 0),
                                              new Point(-1, 1),
                                              new Point( 0, 1)};

   /**
    * Sets up the game board with random terrain hexes and dice numbers.
    */
   public void initializeBoard()
   {
      for(int tX = 1-Constants.kHEX_RINGS; tX < Constants.kHEX_RINGS; tX++)
      {
         for(int tZ = 1-Constants.kHEX_RINGS; tZ < Constants.kHEX_RINGS; tZ++)
         {
            if(Constants.kHEX_RINGS >
                  (Math.abs(tX) + Math.abs(tZ) + Math.abs(tX+tZ))/2)
            {
               setHex(tX, tZ, new TerrainHex(Terrain.getRandom()));
            }
         }
      }
   }
      
   /**
    * Renders the hexes in this map to the given graphics object.
    * @param aG
    */
   public void draw(Graphics2D aG)
   {
      double tHexRadius = Math.min(
         Constants.kHEIGHT/(3*Constants.kHEX_RINGS + 1),
         Constants.kWIDTH/(2*Math.sqrt(3.0)*Constants.kHEX_RINGS));
      
      double tHexWidth = tHexRadius * Math.sqrt(3.0);
      double tHexHeight = tHexRadius * 2;
      
      double tDx = tHexWidth;
      double tDy = .75 * tHexHeight;
      
      for(Map.Entry<Point, TerrainHex> tEntry : _hexMap.entrySet())
      {
         Path2D.Double tPath = new Path2D.Double();
         
         double tHexCenterX = Constants.kWIDTH / 2 +
                              tEntry.getKey().x * tDx +
                              tEntry.getKey().y * tDx / 2;
         double tHexCenterY = Constants.kHEIGHT / 2 +
                              tEntry.getKey().y * tDy;
         
         for(int tIndex = 0; tIndex < 7; tIndex++)
         {
            double tAngle = (Math.PI / 6) + tIndex * (Math.PI / 3);
            
            double tScreenX = tHexCenterX + tHexRadius * Math.cos(tAngle);
            double tScreenY = tHexCenterY + tHexRadius * Math.sin(tAngle);
            
            if(tIndex == 0)
            {
               tPath.moveTo(tScreenX, tScreenY);
            }
            else
            {
               tPath.lineTo(tScreenX, tScreenY);
            }
         }
         
         aG.setColor(tEntry.getValue().getTerrainType().getColor());
         aG.fill(tPath);
         aG.setColor(Color.WHITE);
         aG.setStroke(new BasicStroke(4));
         aG.draw(tPath);
//         aG.setColor(Color.BLACK);
//         aG.setStroke(new BasicStroke(2));
//         aG.draw(tPath);
      }
   }
   
   /**
    * Returns the TerrainHex at the given coordinates
    * @param q
    * @param r
    * @return TerrainHex
    */
   public TerrainHex getHex(int q, int r)
   {
      return _hexMap.get(new Point(q,r));
   }
   
   /**
    * Sets the given TerrainHex at the given coordinates
    * @param q
    * @param r
    * @param aHex
    */
   public void setHex(int q, int r, TerrainHex aHex)
   {
      _hexMap.put(new Point(q,r),aHex);
   }
   
   /**
    * Returns the hex which neighbors the hex specified by (q,r) in the
    * direction specified by dir
    * @param q
    * @param r
    * @param dir
    * @return TerrainHex
    */
   public TerrainHex getNeighbor(int q, int r, int dir)
   {
      TerrainHex tReturn = null;
      
      if(dir >= 0 && dir < kNeighbors.length)
      {
         tReturn = _hexMap.get(new Point(q + kNeighbors[dir].x,
                                         r + kNeighbors[dir].y));
      }
      
      return tReturn;
   }
   
   /**
    * Returns a string representation of this hex map
    * @return String
    */
   @Override
   public String toString()
   {
      StringBuilder tReturn = new StringBuilder();
      
      for(Map.Entry<Point, TerrainHex> tEntry : _hexMap.entrySet())
      {
         tReturn.append("(").
                 append(tEntry.getKey().x).
                 append(",").
                 append(tEntry.getKey().y).
                 append(") = ").
                 append(tEntry.getValue().toString()).
                 append("\n");
      }
      
      return tReturn.toString();
   }
   
   private final HashMap<Point, TerrainHex> _hexMap = 
         new HashMap<Point, TerrainHex>();
}
