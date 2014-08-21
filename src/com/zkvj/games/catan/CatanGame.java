/******************************************************************************
 * @file CatanGame.java
 *
 * @brief A Java implementation of the board game "Settlers of Catan" (TM).
 *
 * @author glasscock
 *
 * @created Aug 9, 2013
 *****************************************************************************/

package com.zkvj.games.catan;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.zkvj.utils.FrameUtil;

public class CatanGame extends JPanel
{
   /** serial version UID */
   private static final long serialVersionUID = 1L;

   /** Constructor */
   public CatanGame()
   {
      _image = new BufferedImage(Constants.kWIDTH,
                                 Constants.kHEIGHT,
                                 BufferedImage.TYPE_INT_RGB);
      _imageGraphics = (Graphics2D)_image.getGraphics();
      _imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);

      this.setPreferredSize(new Dimension(Constants.kWIDTH,
                                          Constants.kHEIGHT));
      this.setFocusable(true);
      this.addMouseListener(_mouseListener);
      this.addMouseMotionListener(_mouseMotionListener);
      this.addKeyListener(_keyListener);

      _board.initializeBoard();

      updateBufferedImage();
   }

   /**
    * Generates the image to be drawn based on the game state.
    */
   private void updateBufferedImage()
   {
      clear();

      _board.draw(_imageGraphics);
   }

   /**
    * Paints the buffered image to the screen.
    *
    * @param aG - screen graphics
    */
   @Override
   public void paintComponent(Graphics aG)
   {
      super.paintComponent(aG);
      aG.drawImage(_image, 0, 0, null);
   }

   /**
    * Clears the image graphics
    */
   private void clear()
   {
      _imageGraphics.setColor(Constants.kOCEAN_COLOR);
      _imageGraphics.fillRect(0,0,Constants.kWIDTH,Constants.kHEIGHT);
   }

   /**
    * Main
    * @param aArgs
    */
   public static void main(String[] aArgs)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            JFrame tFrame = FrameUtil.openInJFrame(new CatanGame(),
                                                   "Catan");
            tFrame.setLocation(200, 200);
            tFrame.setVisible(true);
         }
      });
   }

   /** Mouse listener */
   private final MouseListener _mouseListener = new MouseListener()
   {
      @Override
      public void mouseClicked(MouseEvent aEvent)
      {
         updateBufferedImage();
         repaint();
      }

      @Override
      public void mousePressed(MouseEvent aEvent) {}

      @Override
      public void mouseReleased(MouseEvent aEvent) {}

      @Override
      public void mouseEntered(MouseEvent aEvent) {}

      @Override
      public void mouseExited(MouseEvent aEvent) {}
   };

   /** Mouse listener */
   private final MouseMotionListener _mouseMotionListener =
      new MouseMotionListener()
   {
      @Override
      public void mouseDragged(MouseEvent aEvent){}

      @Override
      public void mouseMoved(MouseEvent aEvent){}
   };

   /** Key listener */
   private final KeyListener _keyListener = new KeyListener()
   {
      @Override
      public void keyPressed(KeyEvent aEvent)
      {
         switch(aEvent.getKeyCode())
         {
            case KeyEvent.VK_R:
               _board.initializeBoard();
               break;
            default:
               break;
         }

         updateBufferedImage();
         repaint();
      }

      @Override
      public void keyTyped(KeyEvent aEvent){}

      @Override
      public void keyReleased(KeyEvent aEvent){}
   };

   /** the image which is drawn to the screen */
   private final BufferedImage _image;

   /** the graphics object for our image */
   private final Graphics2D _imageGraphics;

   /** the game board */
   private final CatanBoard _board = new CatanBoard();
}