package com.zkvj.conjurers.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * Class containing the constants for Conjurers.
 */
public final class Constants
{
   /** Dimensions of the screen */
   public static final Dimension kSCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
   
   /** Dimensions */
   public static final int kWIDTH = 1600;
   public static final int kHEIGHT = 900;
//   public static final int kWIDTH = kSCREEN_SIZE.width - 50;
//   public static final int kHEIGHT = kSCREEN_SIZE.height - 50;
   
   /** Numbers of rings in the hexagonal game board */
   public static final int kHEX_RINGS = 4;
   
   /** Colors */
   public static final Color kFIRE_COLOR = new Color(210,0,0);
   public static final Color kWATER_COLOR = new Color(39,64,175);
   public static final Color kAIR_COLOR = new Color(190,226,240);
   public static final Color kEARTH_COLOR = new Color(34,139,34);
   public static final Color kNEUTRAL_COLOR = Color.GRAY;
   public static final Color kBACKGROUND_COLOR = Color.GRAY;
   public static final Color kPLAYER_COLOR = Color.WHITE;
   public static final Color kOPPONENT_COLOR = Color.BLACK;
   public static final Color kPLAYER_SHADE = new Color(255,255,255,100);
   public static final Color kOPPONENT_SHADE = new Color(0,0,0,100);
   public static final Color kTRANSPARENT_COLOR = new Color(0,0,0,0);
   public static final Color kUI_BKGD_COLOR = Color.DARK_GRAY;//new Color(0,0,0,128);
   
   /** Layers */
   public static final Integer kBACKGROUND_LAYER = new Integer(0);
   public static final Integer kBOARD_LAYER = new Integer(1);
   public static final Integer kUI_LAYER = new Integer(2);
   
   /** Number of players */
   public static final int kNUM_PLAYERS = 2;
   
   /** Initial life of a player */
   public static final int kINITAL_PLAYER_HEALTH = 20;
   
   /** Bounds on player health */
   public static final int kMAX_PLAYER_HEALTH = 99;
   public static final int kMIN_PLAYER_HEALTH = 0; 
   
   /** Initial energy of a player */
   public static final int kINITAL_PLAYER_ENERGY = 0;
   
   /** Initial deck size */
   public static final int kDECK_SIZE = 40;
   
   /** Starting hand size */
   public static final int kINITIAL_HAND_SIZE = 5;
   
   /** Maximum hand size */
   public static final int kMAX_HAND_SIZE = 7;
   
   /** starting conjurer positions */
   public static final Point kDEFAULT_PLAYER_A_POS = new Point(-2,4);
   public static final Point kDEFAULT_PLAYER_B_POS = new Point(2,-4);
   
   /** server port number */
   public static final String kHOST_NAME = "localhost";
   public static final int kPORT_NUMBER = 11077;
   
   /** dimensions for login panel */
   public static final int kLOGIN_WIDTH = 500;
   public static final int kLOGIN_HEIGHT = 100;
   
   /** padding between UI elements */
   public static final int kUI_PADDING = 10;

   /** set to false in order to restrict logins */
   public static final boolean kALLOW_ALL_USERS = true;
}