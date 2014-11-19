/******************************************************************************
 * @file ProjectionTest.java
 *
 * @brief Test application for 3D -> 2D projections
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.projection.test;

/*---- non-JDK imports ------------------------------------------------------*/
import com.zkvj.math.Matrix;
import com.zkvj.math.Point3D;
import com.zkvj.math.Vector3D;
import com.zkvj.projection.Camera3D;
import com.zkvj.projection.Edge3D;
import com.zkvj.projection.Object3D;
import com.zkvj.projection.Projection;
import com.zkvj.utils.FrameUtil;

/*---- JDK imports ----------------------------------------------------------*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Test application for 3D -> 2D projections
 */
public class ProjectionTest extends JPanel
{
   /** serial version UID */
   private static final long serialVersionUID = 1L;
	   
   /** Panel dimensions 4:3 */
   private static final int kWIDTH = 800;
   private static final int kHEIGHT = 800;

   /** Movement speeds for camera */
   private static final double kTILT_SPEED = Math.PI/180;
   private static final double kMOVE_SPEED = 0.5;
   
   /** The projection matrix */
   private static final Matrix kPROJECTION =
      Matrix.getPerspectiveProjection(90.0,             //FoV degrees
                                      (kWIDTH/kHEIGHT), //aspect ratio
                                      0.1,              //near
                                      100.0);           //far

   /** the camera object */
   private Camera3D _camera = new Camera3D(new Point3D(0, 0, 20), //position
                                           new Vector3D(0, 0,-1), //forward
                                           new Vector3D(0, 1, 0), //up
                                           new Vector3D(1, 0, 0));//right

   /** a 3D cube */
   private final Object3D _cube = generateCube();

   /** Used to keep track of cursor position */
   private Point2D.Double _cursor = new Point2D.Double();

   /** Image to be drawn */
   private final BufferedImage _image;

   /** Image graphics */
   private final Graphics2D _imageGraphics;

   /** Key listener */
   private final KeyListener _keyListener = new KeyListener()
   {
      /**
       * Called when a key is pressed. Calls repaint.
       *
       * @param aEvent - the key event
       */
      @Override
      public void keyPressed(KeyEvent aEvent)
      {
         switch(aEvent.getKeyCode())
         {
            case KeyEvent.VK_W:
               _camera.getPosition().z -= kMOVE_SPEED;
//               _cube.move(new Vector3D(0, 0, -kMOVE_SPEED));
               break;
            case KeyEvent.VK_S:
               _camera.getPosition().z += kMOVE_SPEED;
//               _cube.move(new Vector3D(0, 0, kMOVE_SPEED));
               break;
            case KeyEvent.VK_D:
               _camera.getPosition().x += kMOVE_SPEED;
//               _cube.move(new Vector3D(kMOVE_SPEED, 0, 0));
               break;
            case KeyEvent.VK_A:
               _camera.getPosition().x -= kMOVE_SPEED;
//               _cube.move(new Vector3D(-kMOVE_SPEED, 0, 0));
               break;
            case KeyEvent.VK_SPACE:
               _camera.getPosition().y += kMOVE_SPEED;
//               _cube.move(new Vector3D(0, kMOVE_SPEED, 0));
               break;
            case KeyEvent.VK_X:
               _camera.getPosition().y -= kMOVE_SPEED;
//               _cube.move(new Vector3D(0, -kMOVE_SPEED, 0));
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

   /** Mouse listener */
   private final MouseMotionListener _mouseListener = new MouseMotionListener()
   {
      /**
       * Called when mouse is dragged within screen
       *
       * @param aEvent - the mouse event
       */
      @Override
      public void mouseDragged(MouseEvent aEvent)
      {
         int tX = aEvent.getX();
         int tY = aEvent.getY();

         if(tY > _cursor.y)
         {
            _camera.tilt(Camera3D.TiltDirection.eUP, kTILT_SPEED);
         }
         if(tY < _cursor.y)
         {
            _camera.tilt(Camera3D.TiltDirection.eDOWN, kTILT_SPEED);
         }
         if(tX > _cursor.x)
         {
            _camera.tilt(Camera3D.TiltDirection.eRIGHT, kTILT_SPEED);
         }
         if(tX < _cursor.x)
         {
            _camera.tilt(Camera3D.TiltDirection.eLEFT, kTILT_SPEED);
         }

         _cursor.x = tX;
         _cursor.y = tY;

         updateBufferedImage();
         repaint();
      }

      @Override
      public void mouseMoved(MouseEvent aEvent){}
   };

   /**
    * Constructor
    */
   public ProjectionTest()
   {
      _image = new BufferedImage(kWIDTH, kHEIGHT, BufferedImage.TYPE_INT_RGB);
      _imageGraphics = (Graphics2D)_image.getGraphics();
      _imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);
      
      this.setPreferredSize(new Dimension(kWIDTH, kHEIGHT));
      this.setFocusable(true);
      this.addKeyListener(_keyListener);
      this.addMouseMotionListener(_mouseListener);
      
      updateBufferedImage();
   }

   /**
    * Creates a 3D cube
    * 
    * @return a cube
    */
   public static Object3D generateCube()
   {
      Set<Edge3D> tEdges = new HashSet<Edge3D>();

      Point3D p1 = new Point3D( 1,-1, 1);
      Point3D p2 = new Point3D( 1, 1, 1);
      Point3D p3 = new Point3D( 1,-1,-1);
      Point3D p4 = new Point3D( 1, 1,-1);
      Point3D p5 = new Point3D(-1,-1, 1);
      Point3D p6 = new Point3D(-1, 1, 1);
      Point3D p7 = new Point3D(-1,-1,-1);
      Point3D p8 = new Point3D(-1, 1,-1);
      
      tEdges.add(new Edge3D(p1,p2));
      tEdges.add(new Edge3D(p3,p4));
      tEdges.add(new Edge3D(p5,p6));
      tEdges.add(new Edge3D(p7,p8));
      tEdges.add(new Edge3D(p1,p3));
      tEdges.add(new Edge3D(p1,p5));
      tEdges.add(new Edge3D(p7,p3));
      tEdges.add(new Edge3D(p7,p5));
      tEdges.add(new Edge3D(p2,p4));
      tEdges.add(new Edge3D(p2,p6));
      tEdges.add(new Edge3D(p8,p4));
      tEdges.add(new Edge3D(p8,p6));

      return new Object3D(tEdges);
   }

   /**
    * Recreates the buffered image
    */
   private void updateBufferedImage()
   {
      clear(_imageGraphics);
      Dimension tImageSize = new Dimension(kWIDTH, kHEIGHT);
      
      //Matrix tWVP = _camera.getViewMatrix().multiplyByMatrix(kPROJECTION);
      Matrix tWVP = kPROJECTION.multiplyByMatrix(_camera.getViewMatrix());
      //System.out.println("W*V*P matrix : \n" + tWVP.toString());

      _imageGraphics.setColor(Color.black);
      for(Edge3D tEdge : _cube.getEdges())
      {
         _imageGraphics.draw(new Line2D.Double(
                     Projection.projectPoint(tEdge.a, tWVP, tImageSize),
                     Projection.projectPoint(tEdge.b, tWVP, tImageSize)));
      }
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
    * Clears the graphics by drawing a white rectangle
    * 
    * @param aG - graphics
    */
   private void clear(Graphics aG)
   {
      Color tOldColor = aG.getColor();
      aG.setColor(Color.white);
      aG.fillRect(0, 0, kWIDTH, kHEIGHT);
      aG.setColor(tOldColor);
   }

   /**
    * Main
    * 
    * @param aArgs
    */
   public static void main(String[] aArgs)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            JFrame tFrame = FrameUtil.openInJFrame(new ProjectionTest(),
                                                   "Projection");
            tFrame.setLocation(1400, 800);
            tFrame.setVisible(true);
         }
      });
   }
}