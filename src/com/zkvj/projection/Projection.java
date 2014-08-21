/******************************************************************************
 * @file Projection.java
 *
 * @brief This class is used to create 2D projections of 3D objects
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.projection;

/*---- non-JDK imports ------------------------------------------------------*/

/*---- JDK imports ----------------------------------------------------------*/
import com.zkvj.math.Matrix;
import com.zkvj.math.Point3D;

import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * This class is used to create 2D projections of 3D objects
 */
public class Projection
{
   /**
    * Creates a matrix based on the position and orientation of a given
    * Camera3D that can be used to perform a perspective projection.
    *
    * @param aCam - the camera
    * @return projection matrix
    */
   public static Matrix getProjectionMatrix(Camera3D aCam)
   {
      Matrix tResult = Matrix.getTranslationMatrix(aCam.getPosition().x,
                                                   aCam.getPosition().y,
                                                   aCam.getPosition().z);
      tResult = tResult.multiplyByMatrix(
         Matrix.getRotationMatrixX(aCam.getDirection().x));
      tResult = tResult.multiplyByMatrix(
         Matrix.getRotationMatrixY(aCam.getDirection().y));
      tResult = tResult.multiplyByMatrix(
         Matrix.getRotationMatrixZ(aCam.getDirection().z));

      tResult = Matrix.getProjectionMatrix(50, 3, 1, 0.75).
                                                     multiplyByMatrix(tResult);
      return tResult;
   }

   /**
    * Projects given Point3D to a Point2D by applying the given
    * projection matrix and screen dimensions.
    *
    * @param aPoint - the point to be projected
    * @param aProjection - the projection matrix
    * @param aSize - dimension of the screen/graphics area
    * @return point which is the 2D projection of a
    */
   public static Point2D projectPoint(Point3D aPoint,
                                      Matrix aProjection,
                                      Dimension aSize)
   {
      //System.out.println("projectPoint " + aPoint.toString());

      double[] tVector = new double[] {aPoint.x, aPoint.y, aPoint.z, 1.0};

      tVector = aProjection.multiplyByVector(tVector);

      double tX = tVector[0];
      double tY = tVector[1];
      double tW = tVector[3];

      //Perspective divide, which puts x and y into the range [-1,1]
      //if the point fits on the screen
      tX = tX / tW;
      tY = tY / tW;

      //Convert to actually screen coordinates
      Point2D.Double b = new Point2D.Double(
         (tX + 1) * 0.5 * aSize.width,
         (1 - (tY + 1) * 0.5) * aSize.height);

      //System.out.println("result " + b.toString());

      return b;
   }
}