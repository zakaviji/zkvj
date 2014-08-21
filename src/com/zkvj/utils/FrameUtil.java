/******************************************************************************
 * @file FrameUtil.java
 *
 * @brief Constructs JFrames for general use.
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.utils;

import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;

/**
 * Constructs JFrames for general use.
 */
public class FrameUtil
{
   /** 
    * A simplified way to see a JPanel or other Container.
    * Pops up a JFrame with specified Container as the content pane.
    * 
    * @param aContent
    * @param aTitle
    * @param aBgColor
    * @return new JFrame
    */
   public static JFrame openInJFrame(Container aContent,
                                     String aTitle,
                                     Color aBgColor)
   {
      JFrame tFrame = new JFrame(aTitle);
      tFrame.setBackground(aBgColor);
      tFrame.setContentPane(aContent);
      tFrame.setResizable(false);
      tFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      tFrame.pack();
      return tFrame;
   }

   /** 
    * Uses Color.white as the background color.
    * 
    * @param aContent
    * @param aTitle
    * @return new JFrame
    */
   public static JFrame openInJFrame(Container aContent,
                                     String aTitle)
   {
      return openInJFrame(aContent,
                          aTitle,
                          Color.white);
   }

   /** 
    * Uses Color.white as the background color, and the
    * name of the containers class as the JFrame title.
    * 
    * @param aContent
    * @return new JFrame
    */
   public static JFrame openInJFrame(Container aContent)
   {
      return openInJFrame(aContent,
                          aContent.getClass().getName(),
                          Color.white);
   }
}
