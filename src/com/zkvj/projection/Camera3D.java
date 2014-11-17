/******************************************************************************
 * @file Camera3D.java
 *
 * @brief Representation of a camera which is used to create a
 *        2D projection of a 3D object.
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.projection;

import com.zkvj.math.Matrix;
import com.zkvj.math.Point3D;
import com.zkvj.math.Vector3D;

/*---- non-JDK imports ------------------------------------------------------*/

/*---- JDK imports ----------------------------------------------------------*/

/**
 * Representation of a camera which is used to create a
 * 2D projection of a 3D object.
 */
public class Camera3D
{
   /**
    * Enumeration representing the directions the camera can tilt
    */
   public enum TiltDirection
   {
      eUP,
      eDOWN,
      eLEFT,
      eRIGHT,
      eROLL_LEFT,
      eROLL_RIGHT,
   }

   /**
    * Default constructor.
    */
   public Camera3D()
   {
      this._position = new Point3D(0, 0, 0);
      this._direction = new Vector3D(0, 0, 1);
      this._up = new Vector3D(0, 1, 0);
   }

   /**
    * Constructor
    *
    * @param aPos - position of the camera in XYZ coordinates
    * @param aDir - vector representing the direction of the camera
    * @param aUp - vector representing which direction the camera sees as "up"
    */
   public Camera3D(Point3D aPos, Vector3D aDir, Vector3D aUp)
   {
      this._position = aPos;
      this._direction = aDir;
      this._up = aUp;
   }

   /**
    * @return vector representing the direction of the camera
    */
   public Vector3D getDirection()
   {
      return _direction;
   }

   /**
    * @return the camera's position in XYZ coordinates
    */
   public Point3D getPosition()
   {
      return _position;
   }

   /**
    * @return vector representing which direction the camera sees as "up"
    */
   public Vector3D getUp()
   {
      return _up;
   }

   /**
    * @return the view matrix based on this camera's current
    * position and orientation
    */
   public Matrix getViewMatrix()
   {
      Vector3D tVz = Vector3D.normalize(_direction);
      Vector3D tVx = Vector3D.normalize(Vector3D.crossProduct(_up, tVz));
      Vector3D tVy = Vector3D.crossProduct(tVz, tVx);
      
      //inverse view matrix = camera rotation matrix times camera translation matrix
      
      //Matrix.getTranslationMatrix(_position.x, _position.y, _position.z);

//      Matrix tInverseView = new Matrix(4, 4,
//         new double [][] {{      tVx.x,      tVx.y,      tVx.z, 0},   //col 1
//                          {      tVy.x,      tVy.y,      tVy.z, 0},   //col 2
//                          {      tVz.x,      tVz.y,      tVz.z, 0},   //col 3
//                          {_position.x,_position.y,_position.z, 1}}); //col 4
      
      Matrix tInverseView = new Matrix(4, 4,
         new double [][] {{tVx.x, tVy.x, tVz.x, _position.x},  //col 1
                          {tVx.y, tVy.y, tVz.y, _position.y},  //col 2
                          {tVx.z, tVy.z, tVz.z, _position.z},  //col 3
                          {    0,     0,     0,           1}});//col 4

      return Matrix.getInverseMatrix(tInverseView);
   }

   /**
    * Sets the camera's direction as an XYZ vector
    *
    * @param aDir - the camera's direction as a XYZ vector
    */
   public final void setDirection(Vector3D aDir)
   {
      this._direction = aDir;
   }

   /**
    * Sets the camera's position
    *
    * @param aPos - camera's position in XYZ coordinates
    */
   public void setPosition(Point3D aPos)
   {
      this._position = aPos;
   }

   /**
    * Sets which direction the camera sees as "up"
    *
    * @param aUp - "up" direction vector
    */
   public void setUp(Vector3D aUp)
   {
      this._up = aUp;
   }

   /**
    * Method to change the orientation of the camera. It ensures that
    * _direction.z, _direction.x, and _direction.y stay with the range [-PI/2, PI/2]
    *
    * @param aDir - the direction in which to tilt
    * @param aRadians - the amount to tilt
    */
   public void tilt(TiltDirection aDir, double aRadians)
   {
      switch (aDir)
      {
         case eDOWN :
         {
            Matrix tRotateX = Matrix.getRotationMatrixX(aRadians);
            _direction = new Vector3D(tRotateX.multiplyByVector(_direction.toArray4()));
            _up = new Vector3D(tRotateX.multiplyByVector(_up.toArray4()));
            break;
         }
         case eUP :
            Matrix tRotateX = Matrix.getRotationMatrixX(-aRadians);
            _direction = new Vector3D(tRotateX.multiplyByVector(_direction.toArray4()));
            _up = new Vector3D(tRotateX.multiplyByVector(_up.toArray4()));
            break;
         case eLEFT :
            //_direction.y unchanged
//            _direction.x += aRadians;
//            _direction.x = Math.min(_direction.x, Math.PI / 2);
            break;
         case eRIGHT :
            //_direction.y unchanged
//            _direction.x -= aRadians;
//            _direction.x = Math.max(_direction.x, -Math.PI / 2);
            break;
         case eROLL_RIGHT :
            //_direction.z unchanged
//            _direction.y += aRadians;
//            _direction.y = Math.min(_direction.y, Math.PI / 2);
            break;
         case eROLL_LEFT :
            //_direction.z unchanged
//            _direction.y -= aRadians;
//            _direction.y = Math.max(_direction.y, -Math.PI / 2);
            break;
         default :
            break;
      }
   }

   /** Vector representing the direction the camera is facing */
   private Vector3D _direction;

   /** Vector representing which direction is "up" for the camera */
   private Vector3D _up;

   /** The camera's position */
   private Point3D _position;
}