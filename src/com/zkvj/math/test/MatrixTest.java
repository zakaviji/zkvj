/******************************************************************************
 * @file MatrixTest.java
 *
 * @brief Small application to test the Matrix class
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.math.test;

/*---- non-JDK imports ------------------------------------------------------*/
import com.zkvj.math.Matrix;
import com.zkvj.math.Vector3D;

/*---- JDK imports ----------------------------------------------------------*/

/**
 * Small application to test the Matrix class
 */
public class MatrixTest
{
   /**
    * Main
    * 
    * @param aArgs
    */
   public static void main(String[] aArgs)
   {  
//      Matrix A = new Matrix(3,3, new double[][] {{1,2,3},
//                                                 {4,5,6},
//                                                 {7,8,9}});
//      
//      Matrix B = new Matrix(3,3, new double[][] {{9,8,7},
//                                                 {6,5,4},
//                                                 {3,2,1}});
//      
//      System.out.println(B.multiplyByMatrix(A));
      
//      Vector3D a = Vector3D.normalize(new Vector3D(1, 1, 1));
//      Vector3D b = Vector3D.normalize(new Vector3D(1, 2, 3));
//      
//      System.out.println("a = " + a);
//      System.out.println("b = " + b);
//      
//      Vector3D v = Vector3D.crossProduct(a, b);
//      
//      double s = v.magnitude();
//      if(s==0)
//      {
//         System.out.println("a and b are parallel");
//      }
//      
//      double c = Vector3D.dotProduct(a, b);
//      
//      boolean tUse4DMatrix = false;
//      if(!tUse4DMatrix)
//      {
//         Matrix v_x = new Matrix(3, 3, new double[][] {{   0,-v.z, v.y},
//                                                       { v.z,   0,-v.x},
//                                                       {-v.y, v.x,   0}});
//         Matrix v_x2 = v_x.multiplyByMatrix(v_x);
//         
//         Matrix R = Matrix.getIdentityMatrix(3);
//         R = R.addMatrix(v_x);
//         R = R.addMatrix(v_x2.multiplyByScaler((1-c)/(s*s)));
//         
//         System.out.println(R);
//         Vector3D result = new Vector3D(R.multiplyByVector(a.toArray()));
//         System.out.println("R * a = " + result);
//      }
//      else
//      {
//         Matrix v_x = new Matrix(4, 4, new double[][] {{   0,-v.z, v.y,   0},
//                                                       { v.z,   0,-v.x,   0},
//                                                       {-v.y, v.x,   0,   0},
//                                                       {   0,   0,   0,   0}});
//         Matrix v_x2 = v_x.multiplyByMatrix(v_x);
//         
//         Matrix R = Matrix.getIdentityMatrix(4);
//         R = R.addMatrix(v_x);
//         R = R.addMatrix(v_x2.multiplyByScaler((1-c)/(s*s)));
//         
//         System.out.println(R);
//         Vector3D result = new Vector3D(R.multiplyByVector(a.toArray4()));
//         System.out.println("R * a = " + result);
//      }
      
      Vector3D tAxis = new Vector3D(1,2,3);
      double tRadians = Math.PI / 4;
      
      
      double tCos = Math.cos(tRadians);
      double tSin = Math.sin(tRadians);
      
      
   }
}