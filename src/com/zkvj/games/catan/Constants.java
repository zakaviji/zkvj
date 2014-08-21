/******************************************************************************
 * @file CatanSettings.java
 *
 * @brief Class responsible for keeping track of the settings for Catan.
 *
 * @author glasscock
 *
 * @created Aug 13, 2013
 *****************************************************************************/

package com.zkvj.games.catan;

import java.awt.Color;

/**
 * 
 * @author Andrew
 *
 */
public final class Constants
{
   /** Dimensions */
   public static final int kWIDTH = 800;
   public static final int kHEIGHT = 600;
   
   /** Numbers of rings in the hexagonal game board */
   public static final int kHEX_RINGS = 4;
   
   /** Colors */
   public static final Color kOCEAN_COLOR = new Color(0,205,205);
   public static final Color kMOUNTAIN_COLOR = new Color(100,100,100);
   public static final Color kFOREST_COLOR = new Color(34,139,34);
   public static final Color kHILLS_COLOR = new Color(199,97,20);
   public static final Color kFIELD_COLOR = new Color(238,201,0);
   public static final Color kPASTURE_COLOR = new Color(124,252,0);
   public static final Color kDESERT_COLOR = new Color(238,197,145);
}
