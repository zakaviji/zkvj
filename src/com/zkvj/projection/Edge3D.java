/******************************************************************************
 * @file Edge3D.java
 *
 * @brief An edge is defined by its two end-points A and B.
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.projection;

import com.zkvj.math.Point3D;

/*---- non-JDK imports ------------------------------------------------------*/

/*---- JDK imports ----------------------------------------------------------*/

/**
 * An edge is defined by its two end-points A and B.
 */
public class Edge3D
{
   /**
    * Constructor
    *
    * @param a - one end-point of the edge
    * @param b - the other end-point of the edge
    */
   public Edge3D(Point3D a, Point3D b)
   {
      this.a = a;
      this.b = b;
   }

   /**
    * @return string representation of this object
    */
   @Override
   public String toString()
   {
      return "A: " + a.toString() + "B: " + b.toString();
   }

   /** The end-points which define this edge */
   public Point3D a, b;
}