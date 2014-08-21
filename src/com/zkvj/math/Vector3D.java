/******************************************************************************
 * @file Vector3D.java
 *
 * @brief Represents a vector in XYZ-coordinate space
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
 * Represents a vector in XYZ-coordinate space
 */
public class Vector3D
{
   /**
    * Default constructor returns a zero vector
    */
   public Vector3D()
   {
      this.x = 0;
      this.y = 0;
      this.z = 0;
   }

   /**
    * Constructs a new Vector3D from the given components
    *
    * @param x - the x-component
    * @param y - the y-component
    * @param z - the z-component
    */
   public Vector3D(double x, double y, double z)
   {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   /**
    * @param aV1
    * @param aV2
    * @return cross product of aV1 x aV2
    */
   public static Vector3D crossProduct(Vector3D aV1, Vector3D aV2)
   {
      return new Vector3D((aV1.y * aV2.z) - (aV1.z * aV2.y),
                        -((aV1.x * aV2.z) - (aV1.z * aV2.x)),
                          (aV1.x * aV2.y) - (aV1.y * aV2.x));
   }

   /**
    * @param aV
    * @return normalization of aV
    */
   public static Vector3D normalize(Vector3D aV)
   {
      double tMag = Math.sqrt(Math.pow(aV.x,2) +
                              Math.pow(aV.y,2) +
                              Math.pow(aV.z,2));

      return new Vector3D(aV.x/tMag, 
                          aV.y/tMag,
                          aV.z/tMag);
   }

   /**
    * @return string representation of this object
    */
   @Override
   public String toString()
   {
      return "<" + x + ", " + y + ", " + z + ">";
   }

   /** XYZ vector components */
   public double x, y, z;
}