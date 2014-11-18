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
//      Matrix tA = new Matrix(3, 3);
//
//      for(int i = 1; i <= tA.getRows(); i++)
//      {
//         for(int j = 1; j <= tA.getColumns(); j++)
//         {
//            tA.setValue(i, j, Math.round(Math.random()*10));
//         }
//      }
//
//      System.out.println(tA.toString() + "\n");
//
//      Matrix tB = new Matrix(3, 3);
//
//      for(int i = 1; i <= tB.getRows(); i++)
//      {
//         for(int j = 1; j <= tB.getColumns(); j++)
//         {
//            tB.setValue(i, j, Math.round(Math.random()*10));
//         }
//      }
//
//      System.out.println(tB.toString() + "\n");
//
//      System.out.println("Sum:\n" + tA.addMatrix(tB).toString());
//      System.out.println("Product:\n" + tA.multiplyByMatrix(tB).toString());
      
      
//      Matrix A = new Matrix(3,3, new double[][] {{1,2,3},
//                                                 {4,5,6},
//                                                 {7,8,9}});
//      
//      Matrix B = new Matrix(3,3, new double[][] {{9,8,7},
//                                                 {6,5,4},
//                                                 {3,2,1}});
//      
//      System.out.println(B.multiplyByMatrix(A));
      
      
      
      Vector3D a = Vector3D.normalize(new Vector3D(0, 0, 1));
      Vector3D b = Vector3D.normalize(new Vector3D(1, 0, 0));
      Vector3D v = Vector3D.crossProduct(a, b);
      
      double s = Vector3D.magnitude(v);
      if(s==0)
      {
         System.out.println("a and b are parallel");
      }
      
      double c = Vector3D.dotProduct(a, b);
      
      Matrix v_x = new Matrix(3, 3, new double[][] {{   0,-v.z, v.y},
                                                    { v.z,   0,-v.x},
                                                    {-v.y, v.x,   0}});
      Matrix v_x2 = v_x.multiplyByMatrix(v_x);
      
      
      Matrix R = Matrix.getIdentityMatrix(3);
      R.addMatrix(v_x);
      R.addMatrix(v_x2.multiplyByScaler((1-c)/(s*s)));
      
      Vector3D result = new Vector3D(R.multiplyByVector(a.toArray()));
      
      System.out.println("result = " + result);
   }
}