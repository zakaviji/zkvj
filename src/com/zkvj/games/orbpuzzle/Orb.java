/******************************************************************************
 * @file Orb.java
 *
 * @brief Class which represents an individual orb.
 *
 * @author glasscock
 *
 * @created Jun 28, 2013
 *****************************************************************************/

/* Package declaration */
package com.zkvj.games.orbpuzzle;

import java.awt.Color;

public class Orb
{
   private static final Color[] kColors = new Color[] {Color.MAGENTA,
                                                       Color.YELLOW,
                                                       Color.RED,
                                                       Color.GREEN,
                                                       Color.BLUE};
   
   private static final int kNUM_COLORS = kColors.length;

   public Orb(int aColumn, int aRow)
   {
      _col = aColumn;
      _row = aRow;
   }

   public void randomize()
   {
      _color = kColors[(int)Math.floor(kNUM_COLORS * Math.random())];
   }

   public Color getColor()
   {
      return _color;
   }

   public void setColor(Color aColor)
   {
      this._color = aColor;
   }

   public int getCol()
   {
      return _col;
   }

   public void setCol(int aCol)
   {
      this._col = aCol;
   }

   public int getRow()
   {
      return _row;
   }

   public void setRow(int aRow)
   {
      this._row = aRow;
   }

   public boolean isVisible()
   {
      return _visible;
   }

   public void setVisible(boolean aVisible)
   {
      this._visible = aVisible;
   }

   public boolean equals(Orb aOrb)
   {
      return (_col == aOrb.getCol() &&
              _row == aOrb.getRow());
   }

   private boolean _visible = true;
   private int _col, _row;
   private Color _color = Color.BLACK;
}