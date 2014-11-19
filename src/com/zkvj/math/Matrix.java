/******************************************************************************
 * @file Matrix.java
 *
 * @brief General purpose Matrix class
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.math;

/*---- non-JDK imports ------------------------------------------------------*/
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;

/*---- JDK imports ----------------------------------------------------------*/

/**
 * A general purpose Matrix class.
 * 
 * Matrices are stored as 2D arrays. The first parameter determines the row,
 * and the second parameter determines the column. For example, the matrix:
 * 
 *  |  1  2  3  |
 *  |  4  5  6  |
 * 
 * can be initialized using the following 2D array:
 * 
 *  double[][] tValues = new double[][] {{1,2,3},
 *                                       {4,5,6}};
 */
public class Matrix
{
   /**
    * Constructor which takes a 2D array of values
    *
    * @param aRows
    * @param aColumns
    * @param aValues
    */
	public Matrix(int aRows, int aColumns, double[][] aValues)
	{
		this._rows = aRows;
		this._columns = aColumns;		
		this._values = aValues;
		
		setFromArray2D(aValues);
	}

	/**
    * Constructor that takes only size parameters.
    * Values are defaulted to zero.
    * 
    * @param aRows
    * @param aColumns
    */
   public Matrix(int aRows, int aColumns)
	{
		this._rows = aRows;
		this._columns = aColumns;
		this._values = new double[aRows][aColumns];
	}

   /**
    * Returns the result of adding this matrix to the given matrix.
    * Returns null if the two matrices are not the same size.
    *
    * @param aM
    * @return sum Matrix, or null
    */
	public Matrix addMatrix(Matrix aM)
	{
		Matrix tSum = null;

      if(null != aM &&
         aM.getRows() == _rows &&
         aM.getColumns() == _columns)
      {
         tSum = new Matrix(_rows, _columns);

         for(int tRow = 1; tRow <= _rows; tRow++)
         {
            for(int tCol = 1; tCol <= _columns; tCol++)
            {
               tSum.setValue(tRow, tCol, 
                  this.getValue(tRow, tCol) + aM.getValue(tRow, tCol));
            }
         }
      }
      else
      {
         System.out.println("Unable to add matrices: " +
                            "not the same size: returning null");
      }

      return tSum;
	}

   /**
    * Returns a Matrix which is the result of adding a scalar value
    * to this matrix
    *
    * @param aScalar - the scalar value to be added to this matrix
    * @return sum Matrix
    */
	public Matrix addScaler(double aScalar)
	{
		double tSumValues[][] = new double[_rows][_columns];

      for(int tRow = 0; tRow < _rows; tRow++)
      {
         for(int tCol = 0; tCol < _columns; tCol++)
         {
            tSumValues[tRow][tCol] = _values[tRow][tCol] + aScalar;
         }
      }

		return new Matrix(_rows, _columns, tSumValues);
	}

   /**
    * Method to access the number of columns in this matrix
    *
    * @return int
    */
	public int getColumns()
	{
		return this._columns;
	}

   /**
    * Returns the identity matrix of the given order
    *
    * @param aOrder
    * @return Matrix
    */
	public static Matrix getIdentityMatrix(int aOrder)
	{
		Matrix tIdentity = new Matrix(aOrder, aOrder);
		
		for(int tIndex = 1; tIndex <= aOrder; tIndex++)
		{
			tIdentity.setValue(tIndex, tIndex, 1f);
		}
		
		return tIdentity;
	}

   /**
    * Returns the inverse of the given matrix. If this matrix is not invertible,
    * this method returns null.
    *
    * @param aM - the matrix for which to find the inverse
    * @return Matrix, or null if aMatrix is not invertible
    */
   public static Matrix getInverseMatrix(Matrix aM)
   {
      Matrix tReturn = null;

      RealMatrix tA = new Array2DRowRealMatrix(aM.getValues());
      DecompositionSolver tSolver = new LUDecomposition(tA).getSolver();
      
      if(tSolver.isNonSingular())
      {
         RealMatrix tInverse = tSolver.getInverse();
         tReturn = new Matrix(tInverse.getRowDimension(),
                              tInverse.getColumnDimension(),
                              tInverse.getData());
      }
      
      return tReturn;
   }

   /**
    * Returns a perspective projection matrix according to the given
    * parameters.
    *
    * @param aFoV_deg - field of view in degrees
    * @param aAspectRatio - aspect ratio (i.e. 4:3 would be 4/3)
    * @param aNear - near limit
    * @param aFar - far limit
    * @return Matrix
    */
   public static Matrix getPerspectiveProjectionMatrix(double aFoV_deg,
                                                       double aAspectRatio,
                                                       double aNear,
                                                       double aFar)
   {
//      double tSx = (aAspectRatio > 1) ? (1/aAspectRatio) : 1;
//      double tSy = (aAspectRatio > 1) ? 1 : aAspectRatio;
//      double tCotA2 = 1/Math.tan(Math.toRadians(aFoV_deg/2));
//      double tScaleX = tCotA2 * tSx;
//      double tScaleY = tCotA2 * tSy;
//      double tDepth = aFar - aNear;
//
//      double tProj[][] = {{tScaleX, 0, 0, 0},
//                          {0, tScaleY, 0, 0},
//                          {0, 0, (aFar/tDepth), (-(aFar + aNear)/tDepth)},
//                          {0, 0, 1, 0}};

//      double tF = 1/Math.tan(Math.toRadians(aFoV_deg/2));
//
//      double tProj[][] = {{tF * aAspectRatio, 0, 0, 0},
//                          {0, tF, 0, 0},
//                          {0, 0, (aFar+aNear)/(aFar-aNear), 1},
//                          {0, 0, (2*aNear*aFar)/(aNear-aFar), 0}};


      double[][] tProj = new double[4][4];

      double tScale = 1.0 / Math.tan(Math.toRadians(aFoV_deg * 0.5));
      tProj[0][0] = tScale;
      tProj[1][1] = tScale;
      tProj[2][2] = (-aFar / (aFar - aNear));
      tProj[3][2] = (-aFar * aNear) / (aFar - aNear);
      tProj[2][3] = -1;

      return new Matrix(4, 4, tProj);
   }

   /**
    * Returns a projection transformation matrix
    *
    * @param aFocus
    * @param aNear
    * @param aWidth
    * @param aHeight
    * @return Matrix
    */
   public static Matrix getProjectionMatrix(double aFocus,
                                            double aNear,
                                            double aWidth,
                                            double aHeight)
   {
      double matrix[][] =
               {{(2 * aNear) / aWidth, 0, 0, 0},
                {0, (2 * aNear) / aHeight, 0, 0},
                {0, 0, aFocus / (aFocus - aNear), -(aFocus / (aFocus - aNear))},
                {0, 0, 1, 0}};

      return new Matrix(4, 4, matrix);
   }

   /**
    * Returns a transformation matrix which performs a rotation which
    * transforms unit vector A into unit vector B.
    * 
    * @param aA - unit vector
    * @param aB - unit vector
    * @return Matrix
    */
   public static Matrix getRotation(Vector3D aA, Vector3D aB)
   {
      Matrix tReturn = null;
      
      aA = Vector3D.normalize(aA);
      aB = Vector3D.normalize(aB);
      
      Vector3D v = Vector3D.crossProduct(aA, aB);
      
      double s = v.magnitude();
      if(s == 0)
      {
         //TODO handle parallel case
         
         System.out.println("error: given vectors are parallel");
      }
      else
      {
         double c = Vector3D.dotProduct(aA, aB);
         
         Matrix v_x = new Matrix(4, 4, new double[][] {{   0,-v.z, v.y,   0},
                                                       { v.z,   0,-v.x,   0},
                                                       {-v.y, v.x,   0,   0},
                                                       {   0,   0,   0,   0}});
         Matrix v_x2 = v_x.multiplyByMatrix(v_x);
         
         tReturn = Matrix.getIdentityMatrix(4)
                         .addMatrix(v_x)
                         .addMatrix(v_x2.multiplyByScaler((1-c)/(s*s)));
      }
      
      return tReturn;
   }

   /**
    * Returns a transformation matrix which performs a rotation of the given
    * amount (in Radians) about the given axis.
    *
    * @param aAxis - axis of rotation
    * @param aRadians - angle of rotation in Radians
    * @return Matrix
    */
   public static Matrix getRotation(Vector3D aAxis, double aRadians)
   {
      Matrix tReturn = null;

      
      
      return tReturn;
   }

   /**
    * Returns a transformation matrix which performs a rotation about the
    * X-axis
    *
    * @param aRadians
    * @return Matrix
    */
   public static Matrix getRotationMatrixX(double aRadians)
   {
      double[][] rotationMatrixArray =
         {{1.0, 0, 0, 0},
          {0, Math.cos(aRadians), -Math.sin(aRadians), 0},
          {0, Math.sin(aRadians), Math.cos(aRadians), 0},
          {0, 0, 0, 1.0}};

      return new Matrix(4, 4, rotationMatrixArray);
   }

   /**
    * Returns a transformation matrix which performs a rotation about the
    * Y-axis
    *
    * @param aRadians
    * @return Matrix
    */
   public static Matrix getRotationMatrixY(double aRadians)
   {
      double[][] rotationMatrixArray =
         {{Math.cos(aRadians), 0, Math.sin(aRadians), 0},
          {0, 1, 0, 0},
          {-Math.sin(aRadians), 0, Math.cos(aRadians), 0},
          {0, 0, 0, 1.0}};

      return new Matrix(4, 4, rotationMatrixArray);
   }

   /**
    * Returns a transformation matrix which performs a rotation about the
    * Z-axis
    *
    * @param aRadians
    * @return Matrix
    */
   public static Matrix getRotationMatrixZ(double aRadians)
   {
      double[][] rotationMatrixArray =
         {{Math.cos(aRadians), -Math.sin(aRadians), 0, 0},
          {Math.sin(aRadians), Math.cos(aRadians), 0, 0},
          {0, 0, 1.0, 0},
          {0, 0, 0, 1.0}};

      return new Matrix(4, 4, rotationMatrixArray);
   }

   /**
    * Method to access the number of rows in this matrix
    *
    * @return int
    */
	public int getRows()
	{
		return this._rows;
	}

   /**
    * Returns a transformation matrix which is a scale transformation
    *
    * @param aScaleX
    * @param aScaleY
    * @param aScaleZ
    * @return Matrix
    */
   public static Matrix getScaleMatrix(double aScaleX,
                                       double aScaleY,
                                       double aScaleZ)
   {
      double[][] scaleMatrixArray = {{aScaleX, 0, 0, 0},
                                     {0, aScaleY, 0, 0},
                                     {0, 0, aScaleZ, 0},
                                     {0, 0, 0, 1.0}};

      return new Matrix(4, 4, scaleMatrixArray);
   }

   /**
    * Returns a translation transformation matrix
    *
    * @param aDx
    * @param aDy
    * @param aDz
    * @return Matrix
    */
   public static Matrix getTranslationMatrix(double aDx, double aDy, double aDz)
   {
      double[][] translationMatrixArray = {{1.0, 0  , 0  , aDx},
                                           {0  , 1.0, 0  , aDy},
                                           {0  , 0  , 1.0, aDz},
                                           {0  , 0  , 0  , 1.0}};

      return new Matrix(4, 4, translationMatrixArray);
   }

   /**
    * Method to get a specific value from this matrix
    *
    * @param aRow
    * @param aColumn
    * @return double
    */
	public double getValue(int aRow, int aColumn)
	{
      double tReturn = 0;
      
      if(aRow >= 1 &&
         aRow <= _rows &&
         aColumn >= 1 &&
         aColumn <= _columns)
      {
		   tReturn = this._values[aRow - 1][aColumn - 1];
      }
      else
      {
         System.out.println("Unable to set value: " +
                            "row/column index out of bounds");
      }
      return tReturn;
	}

   /**
    * Method to access the values of this matrix as a 2D array
    *
    * @return double[][]
    */
	public double[][] getValues()
	{
		return this._values;
	}

   /**
    * Returns true if this matrix is invertible.
    *
    * @return boolean
    */
   public boolean isInvertible()
   {
      boolean tReturn = false;

      //must be square
      if(this._columns == this._rows)
      {
         //TODO
         if(true)
         {
            tReturn = true;
         }
      }

      return tReturn;
   }

   /**
    * Returns the result of multiplying this matrix by the given matrix
    *
    * @param aM - the matrix to multiply with this matrix
    * @return product Matrix
    */
	public Matrix multiplyByMatrix(Matrix aM)
	{
		Matrix tProduct = null;

      if(null != aM &&
         aM.getRows() == _columns &&
         aM.getColumns() == _rows)
      {
         int tProdRows = _rows;
         int tProdCols = aM.getColumns();
         int tComponentsPerValue = _columns;
         double tValue;

         tProduct = new Matrix(tProdRows, tProdCols);

         for(int tRow = 1; tRow <= tProdRows; tRow++)
         {
            for(int tCol = 1; tCol <= tProdCols; tCol++)
            {
               tValue = 0;
               for(int tIndex = 1; tIndex <= tComponentsPerValue; tIndex++)
               {
                  tValue +=
                     this.getValue(tRow, tIndex) * aM.getValue(tIndex, tCol);
               }

               tProduct.setValue(tRow, tCol, tValue);
            }
         }
      }
      else
      {
         System.out.println("Unable to multiply matrices: " +
                            "not the proper size: returning null");
      }
		
		return tProduct;
	}

   /**
    * Returns the result of multiplying this matrix by a scalar value
    *
    * @param aScalar - the scalar value to multiply against this matrix
    * @return product Matrix
    */
	public Matrix multiplyByScaler(double aScalar)
	{
		double tProdValues[][] = new double[_rows][_columns];

      for(int tRow = 0; tRow < _rows; tRow++)
      {
         for(int tCol = 0; tCol < _columns; tCol++)
         {
            tProdValues[tRow][tCol] = _values[tRow][tCol] * aScalar;
         }
      }

		return new Matrix(_rows, _columns, tProdValues);
	}

   /**
    * Returns the vector result of multiplying this matrix by a given vector
    *
    * @param aVector
    * @return double[]
    */
	public double[] multiplyByVector(double[] aVector)
	{
      double[] tResult = null;
      double tValue;
      
      if(aVector.length == _rows &&
         aVector.length == _columns)
      {
         tResult = new double[aVector.length];

         for(int tRow = 0; tRow < _rows; tRow++)
         {
            tValue = 0;
            for(int tCol = 0; tCol < _columns; tCol++)
            {
               tValue += this._values[tRow][tCol] * aVector[tCol];
            }

            tResult[tRow] = tValue;
         }
      }
      else
      {
         System.out.println("Unable to multiply matrix by vector: " +
                            "vector is not the proper size: returning null");
      }
		
		return tResult;
	}

   /**
    * Sets the values of this matrix from the values given by a 2D array
    *
    * @param array
    */
	private void setFromArray2D(double[][] aValues)
	{
		if(aValues.length == _rows &&
         aValues.length > 0 &&
         aValues[0].length == _columns)
      {
         this._values = aValues;
      }
      else
      {
         System.out.println("Unable to set matrix values: " +
                            "arrays not the same size");
      }
	}

   /**
    * Sets a particular value in this matrix
    *
    * @param aRow
    * @param aColumn
    * @param aValue
    */
	public void setValue(int aRow, int aColumn, double aValue)
	{
      if(aRow >= 1 &&
         aRow <= _rows &&
         aColumn >= 1 &&
         aColumn <= _columns)
      {
         this._values[aRow - 1][aColumn - 1] = aValue;
      }
      else
      {
         System.out.println("Unable to set value: " +
                            "row/column index out of bounds");
      }
	}

   /**
    * Returns a string representation of this matrix
    *
    * @return String
    */
   @Override
	public String toString()
	{
		StringBuilder tOutput = new StringBuilder();
		
		for(int tRow = 0; tRow < _rows; tRow++)
		{
			tOutput.append("[\t");
			
			for(int tCol = 0; tCol < _columns; tCol++)
			{
				tOutput.append(String.valueOf(this._values[tRow][tCol]));
				tOutput.append("\t");
			}
			
			tOutput.append("]\n");
		}
		
		return tOutput.toString();
	}

   /** The values contained in this matrix */
   private double[][] _values;

   /** The number of rows and columns in this matrix */
	private int _rows, _columns;
}