/******************************************************************************
 * @file MatrixProjectionExample.java
 *
 * @brief Example of using matrix transformations to calculate a projection
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.examples.projection;

/*---- non-JDK imports ------------------------------------------------------*/
import com.zkvj.math.Matrix;

/*---- JDK imports ----------------------------------------------------------*/
import javax.swing.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

/**
 * Example of using matrix transformations to calculate a projection
 */
public class MatrixProjectionExample
{
   /**
    * Main
    * 
    * @param aArgs
    */
   public static void main(String[] aArgs)
   {
      final BufferedImage backbuffer =
                        new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
      final JFrame frame = new JFrame();

      final Graphics g = backbuffer.getGraphics();
      g.setColor(Color.white);
      g.fillRect(0, 0, 400, 300);

      try
      {
         SwingUtilities.invokeAndWait(new Runnable()
         {
            @Override
            public void run()
            {
               frame.setSize(400, 300);
               frame.setVisible(true);
               frame.setIgnoreRepaint(true);
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
         });
      }
      catch(Exception e)
      {
         e.printStackTrace();
         System.exit(0);
      }

      Thread renderThread = new Thread()
      {
         @Override
         public void run()
         {
            /* Initialize transform matrix to the identity */
            Matrix transform = Matrix.getIdentity(4);

            /* Add a translation to the overall transformation */
            transform = transform.multiplyByMatrix(
                                        Matrix.getTranslation(0, 0, 35));

            /* The projection matrix */
            Matrix projection = Matrix.getProjection(50, 3, 1, 0.75);

            /* Vertices of the cube */
            double[][] vertices = {{-1,-1,-1, 1},
                                   { 1,-1,-1, 1},
                                   { 1,-1, 1, 1},
                                   {-1,-1, 1, 1},
                                   {-1, 1,-1, 1},
                                   { 1, 1,-1, 1},
                                   { 1, 1, 1, 1},
                                   {-1, 1, 1, 1}};

            /* Indices which link vertices together to keep track of edges */
            int[][] indices ={{0, 1},
                              {1, 2},
                              {2, 3},
                              {3, 0},
                              {0, 4},
                              {1, 5},
                              {2, 6},
                              {3, 7},
                              {4, 5},
                              {5, 6},
                              {6, 7},
                              {7, 4}};

            while(true)
            {
               g.setColor(Color.white);
               g.fillRect(0, 0, 400, 300);

               transform = transform.multiplyByMatrix(
                              Matrix.getRotationX(Math.toRadians(1)));
               transform = transform.multiplyByMatrix(
                              Matrix.getRotationY(Math.toRadians(1)));
               transform = transform.multiplyByMatrix(
                              Matrix.getRotationZ(Math.toRadians(1)));

               List<Point> points = new ArrayList<Point>();

/**
 * P (TV) = (PT) V
 * Where P and T are the Projection and Transform matrices and V is the vertex 
 * vector. We do this so we can cut the matrix multiplications nearly in half
 * by pre-multiplying the two matrices.
 *
 * A matrix-matrix multiplication is a lot more work than a matrix-vector
 * multiplication. But the matrix-matrix multiplication is only done once,
 * before the geometry processing, while the matrix-vector multiplications
 * are done on a per vertex basis.
 *
 * This cuts the number of matrix-vector multiplications in half.
 * As long as we're doing at least a dozen or so vertices per flattened
 * matrix, this is a speed boost.
 */

               Matrix flattenedMatrix = projection.multiplyByMatrix(transform);

               for(int i = 0; i < vertices.length; i++)
               {
                  /*
                   * double[] result = transform.multiplyByVector(vertices[i]);
                   * result = projection.multiplyByVector(result);
                   * */
                  double[] result = flattenedMatrix.multiplyByVector(vertices[i]);
                  for(int j = 0; j < 4; j++)
                  {
                     result[j] = result[j] / result[3];
                  }
                  points.add(pointFromVertex(result));
               }

               Graphics2D g2d = (Graphics2D) g;
//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g.setColor(Color.black);
               for(int i = 0; i < indices.length; i++)
               {
                  Point p1, p2;
                  p1 = points.get(indices[i][0]);
                  p2 = points.get(indices[i][1]);
                  g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
               }

               try
               {
                  Thread.sleep(25);
               }
               catch(InterruptedException aEx)
               {
                  System.out.println(aEx.toString());
               }

               try
               {
                  SwingUtilities.invokeAndWait(new Runnable()
                  {
                     @Override
                     public void run()
                     {
                        frame.getGraphics().drawImage(backbuffer, 0, 0, null);
                     }
                  });
               }
               catch(Exception aEx)
               {
                  System.out.println(aEx.toString());
               }
            }
         }
      };

      renderThread.start();

   }//end main

   /**
    * Converts a projected vertex into a point representing actual
    * screen coordinates.
    * 
    * @param vertex
    * @return
    */
   public static Point pointFromVertex(double[] vertex)
   {
      Point p = new Point();
      p.translate((int) Math.round(vertex[0] * 400) + 200,
                  (int) Math.round(vertex[1] * 300) + 150);
      return p;
   }
}
