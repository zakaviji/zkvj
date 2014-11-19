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
public final class Vector3D
{
   /** components */
   public final double x, y, z;
   
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
    * Contructs a new Vector3D from the given array of values.
    * Array length must be greater than or equal to 3.
    * @param aValues
    * @throws IllegalArgumentException
    */
   public Vector3D(double[] aValues)
   {
      if(aValues.length < 3)
      {
         throw new IllegalArgumentException("given array not of proper size");
      }
      
      this.x = aValues[0];
      this.y = aValues[1];
      this.z = aValues[2];
   }

   /**
    * @param aV1
    * @param aV2
    * @return cross product aV1 x aV2
    */
   public static Vector3D crossProduct(Vector3D aV1, Vector3D aV2)
   {
      return new Vector3D((aV1.y * aV2.z) - (aV1.z * aV2.y),
                        -((aV1.x * aV2.z) - (aV1.z * aV2.x)),
                          (aV1.x * aV2.y) - (aV1.y * aV2.x));
   }

   /**
    * @param aV1
    * @param aV2
    * @return dot product aV1 * aV2
    */
   public static double dotProduct(Vector3D aV1, Vector3D aV2)
   {
      return (aV1.x * aV2.x + aV2.y * aV2.y + aV1.z * aV2.z);
   }
   
   /**
    * Returns the magnitude of this vector.
    * @return double
    */
   public double magnitude()
   {
      return Math.sqrt(Math.pow(x,2) +
                       Math.pow(y,2) +
                       Math.pow(z,2));
   }

   /**
    * @param aV
    * @return normalization of aV
    */
   public static Vector3D normalize(Vector3D aV)
   {
      double tMagnitude = aV.magnitude();

      return new Vector3D(aV.x/tMagnitude, 
                          aV.y/tMagnitude,
                          aV.z/tMagnitude);
   }
   
   /**
    * @return array representation equal to {x,y,z}
    */
   public double[] toArray()
   {
      return new double[] {x,y,z};
   }
   
   /**
    * @return array representation equal to {x,y,z,1}
    */
   public double[] toArray4()
   {
      return new double[] {x,y,z,1};
   }

   /**
    * @return string representation of this object
    */
   @Override
   public String toString()
   {
      return "<" + x + ", " + y + ", " + z + ">";
   }
}