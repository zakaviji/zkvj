/******************************************************************************
 * @file RotatingCubeExample.java
 *
 * @brief 
 *
 * @author glasscock
 *
 * @created Jun 18, 2013
 *****************************************************************************/

package com.zkvj.examples.projection;

import com.zkvj.math.Point3D;
import com.zkvj.projection.Edge3D;
import com.zkvj.projection.Object3D;
import com.zkvj.projection.test.ProjectionTest;
import com.zkvj.utils.FrameUtil;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RotatingCubeExample extends JPanel
{
   /** serial version UID */
   private static final long serialVersionUID = 1L;
   
   private static final int kWIDTH = 400;
   private static final int kHEIGHT = 400;//300;

   private static final int kSCALE_FACTOR = kWIDTH / 4;

   // distance from eye to near plane
   private static final double kNEAR = 4;

   // distance from near plane to center of object
   private static final double kNEAR_TO_OBJ = 1.5f;

   public RotatingCubeExample()
   {
      _image = new BufferedImage(kWIDTH, kHEIGHT, BufferedImage.TYPE_INT_RGB);
      _imageGraphics = (Graphics2D)_image.getGraphics();
      _imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);
      _imageGraphics.setColor(Color.black);
      drawWireframe();

      this.setPreferredSize(new Dimension(kWIDTH, kHEIGHT));
      this.setFocusable(true);
      this.addMouseMotionListener(_mouseListener);
   }

   private void drawWireframe()
   {
      clear(_imageGraphics);

      for(Edge3D tEdge : _cube.getEdges())
      {
         _imageGraphics.draw(new Line2D.Double(projectPoint(tEdge.a),
                                               projectPoint(tEdge.b)));
      }
   }

   private Point2D projectPoint(Point3D aPoint)
   {
      // compute coefficients for the projection
      double tTheta = _azimuth * Math.PI / 180.0;
      double tPhi = _elevation * Math.PI / 180.0;
      float tCosT = (float) Math.cos(tTheta);
      float tSinT = (float) Math.sin(tTheta);
      float tCosP = (float) Math.cos(tPhi);
      float tSinP = (float) Math.sin(tPhi);

      double tX = aPoint.x;
      double tY = aPoint.y;
      double tZ = aPoint.z;

      // compute an orthographic projection
      double tX_proj = tCosT * tX + tSinT * tZ;
      double tY_proj = -(tSinT * tSinP) * tX + tCosP * tY + tCosT * tSinP * tZ;

      // now adjust things to get a perspective projection
      double tZ_proj = tCosT * tCosP * tZ - tSinT * tCosP * tX - tSinP * tY;
      tX_proj = tX_proj * kNEAR / (tZ_proj + kNEAR + kNEAR_TO_OBJ);
      tY_proj = tY_proj * kNEAR / (tZ_proj + kNEAR + kNEAR_TO_OBJ);

      // the 0.5 is to round off when converting to int
      return new Point2D.Double(kWIDTH/2 + kSCALE_FACTOR * tX_proj,
                                kHEIGHT/2 - kSCALE_FACTOR * tY_proj);
   }

   private void clear(Graphics aG)
   {
      Color tOldColor = aG.getColor();
      aG.setColor(Color.white);
      aG.fillRect(0, 0, kWIDTH, kHEIGHT);
      aG.setColor(tOldColor);
   }
   
   @Override
   protected void paintComponent(Graphics aG)
   {
      super.paintComponent(aG);
      aG.drawImage(_image, 0, 0, null);
   }

   public static void main(String[] aArgs)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            JFrame tFrame = FrameUtil.openInJFrame(new RotatingCubeExample(),
                                                   "WireCubeExample");
            tFrame.setLocation(1400, 800);
            tFrame.setVisible(true);
         }
      });
   }

   private final MouseMotionListener _mouseListener = new MouseMotionListener()
   {
      @Override
      public void mouseDragged(MouseEvent aEvent)
      {
         // get the latest mouse position
         int new_mx = aEvent.getX();
         int new_my = aEvent.getY();

         // adjust angles according to the distance travelled by the mouse
         // since the last event
         _azimuth += new_mx - _mouseX;
         _elevation += new_my - _mouseY;

         // update the _image
         drawWireframe();

         // update our data
         _mouseX = new_mx;
         _mouseY = new_my;

         repaint();
         aEvent.consume();
      }

      @Override
      public void mouseMoved(MouseEvent aEvent){}
   };
   
   private final BufferedImage _image;
   private final Graphics2D _imageGraphics;

   private final Object3D _cube = ProjectionTest.generateCube();
   
   private int _mouseX, _mouseY;// the most recently recorded mouse coordinates

   private int _azimuth = 35, _elevation = 30;
}
