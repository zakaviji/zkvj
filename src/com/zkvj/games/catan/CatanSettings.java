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

public class CatanSettings
{
   /** Dimensions */
   public static int kWIDTH = 800;
   public static int kHEIGHT = 600;
   
   /** Numbers of rings in the hexagonal Catan game board */
   public static int kHEX_RINGS = 3;
   
   /** Colors */
   public static Color kOCEAN_COLOR = new Color(0,205,205);
   public static Color kMOUNTAIN_COLOR = new Color(100,100,100);
   public static Color kFOREST_COLOR = new Color(34,139,34);
   public static Color kHILLS_COLOR = new Color(199,97,20);
   public static Color kFIELD_COLOR = new Color(238,201,0);
   public static Color kPASTURE_COLOR = new Color(124,252,0);
   public static Color kDESERT_COLOR = new Color(238,197,145);
}
