/******************************************************************************
 * @file OrbPuzzle.java
 *
 * @brief A simple game where the player rearranges orbs to form combinations
 *        and score points.
 *
 * @author glasscock
 *
 * @created Jun 28, 2013
 *****************************************************************************/

package com.zkvj.games.orbpuzzle;

import com.zkvj.utils.FrameUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A simple game where the player rearranges orbs to form combinations
 * and score points.
 */
public class OrbPuzzle extends JPanel
{
   /** serial version UID */
   private static final long serialVersionUID = 1L;

   /** Background color */
   private static final Color kBACKGROUND_COLOR = Color.DARK_GRAY;

   /** Number of orbs in the playing area */
   private static final int kORB_COLUMNS = 6;
   private static final int kORB_ROWS = 5;

   /** Size constants */
   private static final int kORB_SIZE = 50;
   private static final int kORB_RADIUS = kORB_SIZE/2;
   private static final int kORB_BUFFER_SIZE = 5;

   /** Panel dimensions derived from above constants */
   private static final int kWIDTH = kORB_COLUMNS * kORB_SIZE +
                                     kORB_BUFFER_SIZE * (kORB_COLUMNS + 1);
   private static final int kHEIGHT = kORB_ROWS * kORB_SIZE +
                                     kORB_BUFFER_SIZE * (kORB_ROWS + 1);
   
   public OrbPuzzle()
   {
      _image = new BufferedImage(kWIDTH, kHEIGHT, BufferedImage.TYPE_INT_RGB);
      _imageGraphics = (Graphics2D)_image.getGraphics();
      _imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);

      this.setPreferredSize(new Dimension(kWIDTH, kHEIGHT));
      this.setFocusable(true);
      this.addMouseListener(_mouseListener);
      this.addMouseMotionListener(_mouseMotionListener);
      this.addKeyListener(_keyListener);

      initializeOrbs();
      updateBufferedImage();
   }

   private void initializeOrbs()
   {
      for(int i = 0; i < kORB_COLUMNS; i++)
      {
         for(int j = 0; j < kORB_ROWS; j++)
         {
            Orb tOrb = new Orb(i, j);
            tOrb.randomize();
            _orbs.add(tOrb);
         }
      }
   }

   /**
    * Resets and randomizes all orbs.
    */
   private void randomizeOrbs()
   {
      for(Orb tOrb : _orbs)
      {
         tOrb.randomize();
      }
   }

   /**
    * Swaps the positions of the given orbs.
    * @param aOrb1
    * @param aOrb2
    */
   private static void swap(Orb aOrb1, Orb aOrb2)
   {
      int tCol = aOrb1.getCol();
      int tRow = aOrb1.getRow();

      aOrb1.setCol(aOrb2.getCol());
      aOrb1.setRow(aOrb2.getRow());

      aOrb2.setCol(tCol);
      aOrb2.setRow(tRow);
   }

   /**
    * Draws an individual orb at the given reference point (top, left corner)
    * and of the given color.
    * @param aOrb 
    */
   private void drawOrb(Point2D.Double aRefPoint, Color aColor)
   {
      Ellipse2D tOrb = new Ellipse2D.Double(aRefPoint.x, aRefPoint.y,
                                            kORB_SIZE, kORB_SIZE);
      _imageGraphics.setColor(aColor);
      _imageGraphics.fill(tOrb);
   }

   /**
    * Generates the image to be drawn based on the game state.
    */
   private void updateBufferedImage()
   {
      clear();

      for(Orb tOrb : _orbs)
      {
         if(inBounds(tOrb) && tOrb.isVisible())
         {
            drawOrb(getRefPoint(tOrb), tOrb.getColor());
         }
      }

      if(null != _dragOrb &&
         null != _mouse)
      {
         Point2D.Double tDragOrbRef = new Point2D.Double(_mouse.x - kORB_RADIUS,
                                                         _mouse.y - kORB_RADIUS);
         drawOrb(tDragOrbRef, _dragOrb.getColor());
      }
   }

   /**
    * Returns true iff the given orb is within the bounds of the playing area.
    * @param aOrb
    * @return true or false
    */
   private static boolean inBounds(Orb aOrb)
   {
      return (aOrb.getCol() >= 0 &&
         aOrb.getCol() < kORB_COLUMNS &&
         aOrb.getRow() >= 0 &&
         aOrb.getRow() < kORB_ROWS);
   }

   /**
    * Returns the screen XY point which is the center of the
    * given orb.
    * @param aOrb
    * @return screen XY point
    */
   private static Point2D.Double getCenterPoint(Orb aOrb)
   {
      return new Point2D.Double(kORB_BUFFER_SIZE * (aOrb.getCol() + 1) +
                                   kORB_SIZE * aOrb.getCol() + kORB_RADIUS,
                                kORB_BUFFER_SIZE * (aOrb.getRow() + 1) +
                                   kORB_SIZE * aOrb.getRow() + kORB_RADIUS);
   }

   /**
    * Returns the screen XY point which is the upper-left corner of the
    * given orb.
    * @param aOrb
    * @return screen XY point
    */
   private static Point2D.Double getRefPoint(Orb aOrb)
   {
      return new Point2D.Double(kORB_BUFFER_SIZE * (aOrb.getCol() + 1) +
                                   kORB_SIZE * aOrb.getCol(),
                                kORB_BUFFER_SIZE * (aOrb.getRow() + 1) +
                                   kORB_SIZE * aOrb.getRow());
   }

   /**
    * Returns the orb which is under the current mouse position. Returns null
    * if the mouse is not pressed/dragging, or if the mouse is not over an orb.
    * @return orb
    */
   private Orb findOrbUnderMouse()
   {
      Orb tReturn = null;
      
      if(null != _mouse)
      {
         for(Orb tOrb : _orbs)
         {
            Point2D.Double tOrbCenter = getCenterPoint(tOrb);
            if(_mouse.distance(tOrbCenter) < kORB_RADIUS)
            {
               tReturn = tOrb;
               break;
            }
         }
      }

      return tReturn;
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
      _imageGraphics.setColor(kBACKGROUND_COLOR);
      _imageGraphics.fillRect(0, 0, kWIDTH, kHEIGHT);
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
            JFrame tFrame = FrameUtil.openInJFrame(new OrbPuzzle(),
                                                   "Orb Match");
            tFrame.setLocation(200, 200);
            tFrame.setVisible(true);
         }
      });
   }

   private final MouseListener _mouseListener = new MouseListener()
   {
      @Override
      public void mouseClicked(MouseEvent aEvent) {}

      @Override
      public void mousePressed(MouseEvent aEvent)
      {
         _mouse = new Point2D.Double(aEvent.getX(), aEvent.getY());

         Orb tOrb = findOrbUnderMouse();
         if(null != tOrb)
         {
            tOrb.setVisible(false);
            _dragOrb = tOrb;
         }

         updateBufferedImage();
         repaint();
      }

      @Override
      public void mouseReleased(MouseEvent aEvent)
      {
         if(null != _dragOrb)
         {
            _dragOrb.setVisible(true);
         }

         _mouse = null;
         _dragOrb = null;

         updateBufferedImage();
         repaint();
      }

      @Override
      public void mouseEntered(MouseEvent aEvent) {}

      @Override
      public void mouseExited(MouseEvent aEvent) {}
   };

   /** Mouse listener */
   private final MouseMotionListener _mouseMotionListener = new MouseMotionListener()
   {
      @Override
      public void mouseDragged(MouseEvent aEvent)
      {
         _mouse = new Point2D.Double(aEvent.getX(), aEvent.getY());

         Orb tOrb = findOrbUnderMouse();
         if(null != tOrb &&
            null != _dragOrb &&
            !tOrb.equals(_dragOrb))
         {
            swap(tOrb, _dragOrb);
         }

         updateBufferedImage();
         repaint();
      }

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
               randomizeOrbs();
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

   private Point2D.Double _mouse = null;
   private Orb _dragOrb = null;

   private final ArrayList<Orb> _orbs = new ArrayList<Orb>();

   private final BufferedImage _image;
   private final Graphics2D _imageGraphics;
}