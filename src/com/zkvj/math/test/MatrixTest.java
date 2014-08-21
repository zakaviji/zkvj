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
      Matrix tA = new Matrix(3, 3);

      for(int i = 1; i <= tA.getRows(); i++)
      {
         for(int j = 1; j <= tA.getColumns(); j++)
         {
            tA.setValue(i, j, Math.round(Math.random()*10));
         }
      }

      System.out.println(tA.toString() + "\n");

      Matrix tB = new Matrix(3, 3);

      for(int i = 1; i <= tB.getRows(); i++)
      {
         for(int j = 1; j <= tB.getColumns(); j++)
         {
            tB.setValue(i, j, Math.round(Math.random()*10));
         }
      }

      System.out.println(tB.toString() + "\n");

      System.out.println("Sum:\n" + tA.addMatrix(tB).toString());
      System.out.println("Product:\n" + tA.multiplyByMatrix(tB).toString());
   }
}