/******************************************************************************
 * @file Point3D.java
 *
 * @brief Represents a point in XYZ-coordinate space
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.math;

/*---- non-JDK imports ------------------------------------------------------*/

/*---- JDK imports ----------------------------------------------------------*/

/**
 * Represents a point in XYZ-coordinate space
 */
public class Point3D
{
   /**
    * Default constructor sets this point equal to the origin (0,0,0).
    */
   public Point3D()
   {
      this.x = 0;
      this.y = 0;
      this.z = 0;
   }

   /**
    * Constructs a new Point3D from given coordinates
    *
    * @param x - the x-coordinate
    * @param y - the y-coordinate
    * @param z - the z-coordinate
    */
   public Point3D(double x, double y, double z)
   {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   /**
    * @return string representation of this object
    */
   @Override
   public String toString()
   {
      return "(" + x + ", " + y + ", " + z + ")";
   }

   /** XYZ coordinates */
   public double x, y, z;
}