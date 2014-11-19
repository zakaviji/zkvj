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
   
   /** Default unit vectors which define the default camera view */
   public static final Vector3D kDefaultForward = new Vector3D(0,0,-1);
   public static final Vector3D kDefaultUp = new Vector3D(0,1,0);
   public static final Vector3D kDefaultRight = new Vector3D(1,0,0);
   
   /** Default camera position */
   public static final Point3D kDefaultPosition = new Point3D(0,0,0);

   /** Vectors which keep track of the orientation of the camera */
   private Vector3D _forward;
   private Vector3D _up;
   private Vector3D _right;

   /** The camera's position */
   private Point3D _position;

   /**
    * Default constructor.
    */
   public Camera3D()
   {
      this._position = kDefaultPosition;
      this._forward = kDefaultForward;
      this._up = kDefaultUp;
      this.setRight(kDefaultRight);
   }

   /**
    * Constructor
    *
    * @param aPos - position of the camera in XYZ world coordinates
    * @param aForward - vector representing which direction the camera sees as "forward"
    * @param aUp - vector representing which direction the camera sees as "up"
    * @param aRight - vector representing which direction the camera sees as "right"
    */
   public Camera3D(Point3D aPos, Vector3D aForward, Vector3D aUp, Vector3D aRight)
   {
      this._position = aPos;
      this._forward = aForward;
      this._up = aUp;
      this.setRight(aRight);
   }

   /**
    * @return vector representing which direction the camera sees as "forward"
    */
   public Vector3D getForward()
   {
      return _forward;
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
    * @return vector representing which direction the camera sees as "right"
    */
   public Vector3D getRight()
   {
      return _right;
   }

   /**
    * @return the view matrix based on this camera's current
    * position and orientation
    */
   public Matrix getViewMatrix()
   {
      Vector3D tBack = Vector3D.normalize(new Vector3D(-_forward.x, -_forward.y, -_forward.z));
      Vector3D tRight = Vector3D.normalize(_right);
      Vector3D tUp = Vector3D.normalize(_up);
      
      //inverse view matrix = camera rotation matrix * camera translation matrix
//      Matrix tInverseView = Matrix.getRotation(kDefaultDirection, tVz).multiplyByMatrix(
//            Matrix.getTranslationMatrix(_position.x, _position.y, _position.z));

      Matrix tCamRotation = new Matrix(3, 3,
            new double [][] {{tRight.x, tRight.y, tRight.z},
                             {   tUp.x,    tUp.y,    tUp.z},
                             { tBack.x,  tBack.y,  tBack.z}});
      
      Matrix tViewRotation = Matrix.getTranspose(tCamRotation);
      
      double[][] tVR = tViewRotation.getValues();
      
      double[] tCamPosition = new double [] {_position.x, _position.y, _position.z};

      double[] tVT = tViewRotation.multiplyByScaler(-1).multiplyByVector(tCamPosition);
      
      Matrix tViewMatrix = new Matrix(4, 4,
         new double[][] {{tVR[0][0],tVR[0][1],tVR[0][2],tVT[0]},
                         {tVR[1][0],tVR[1][1],tVR[1][2],tVT[1]},
                         {tVR[2][0],tVR[2][1],tVR[2][2],tVT[2]},
                         {0,0,0,1}});
      
      return tViewMatrix;
      
//      Matrix tInverseView = new Matrix(4, 4,
//         new double [][] {{right.x, right.y, right.z, _position.x},
//                          {   up.x,    up.y,    up.z, _position.y},
//                          { back.x,  back.y,  back.z, _position.z},
//                          {      0,       0,       0,           1}});
//
//      return Matrix.getInverseMatrix(tInverseView);
   }

   /**
    * Sets which direction the camera sees as "forward"
    *
    * @param aForward - Vector3D
    */
   public final void setForward(Vector3D aForward)
   {
      this._forward = aForward;
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
    * @param aUp - Vector3D
    */
   public void setUp(Vector3D aUp)
   {
      this._up = aUp;
   }

   /**
    * Sets which direction the camera sees as "right"
    * 
    * @param aRight - Vector3D
    */
   public void setRight(Vector3D aRight)
   {
      _right = aRight;
   }

   /**
    * Convenience method for changing the orientation of the camera.
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
            Matrix tRotation = Matrix.getRotation(_right, aRadians);
            _forward = new Vector3D(tRotation.multiplyByVector(_forward.toArray4()));
            _up = new Vector3D(tRotation.multiplyByVector(_up.toArray4()));
            break;
         }
//         case eUP :
//         {
//            Matrix tRotation = Matrix.getRotation(_right, -aRadians);
//            _forward = new Vector3D(tRotation.multiplyByVector(_forward.toArray4()));
//            _up = new Vector3D(tRotation.multiplyByVector(_up.toArray4()));
//            break;
//         }
//         case eRIGHT :
//         {
//            Matrix tRotation = Matrix.getRotation(_up, aRadians);
//            _forward = new Vector3D(tRotation.multiplyByVector(_forward.toArray4()));
//            _right = new Vector3D(tRotation.multiplyByVector(_right.toArray4()));
//            break;
//         }
//         case eLEFT :
//         {
//            Matrix tRotation = Matrix.getRotation(_up, -aRadians);
//            _forward = new Vector3D(tRotation.multiplyByVector(_forward.toArray4()));
//            _right = new Vector3D(tRotation.multiplyByVector(_right.toArray4()));
//            break;
//         }
//         case eROLL_RIGHT :
//         {
//            Matrix tRotation = Matrix.getRotation(_forward, aRadians);
//            _up = new Vector3D(tRotation.multiplyByVector(_up.toArray4()));
//            _right = new Vector3D(tRotation.multiplyByVector(_right.toArray4()));
//            break;
//         }
//         case eROLL_LEFT :
//         {
//            Matrix tRotation = Matrix.getRotation(_forward, -aRadians);
//            _up = new Vector3D(tRotation.multiplyByVector(_up.toArray4()));
//            _right = new Vector3D(tRotation.multiplyByVector(_right.toArray4()));
//            break;
//         }
         default :
         {
            break;
         }
      }
      
      _forward = Vector3D.normalize(_forward);
      _up = Vector3D.normalize(_up);
      _right = Vector3D.normalize(_right);
      
      System.out.println(this.toString() + "\n");
   }
   
   /**
    * Returns a string representation of the Camera3D object.
    * @return String
    */
   @Override
   public String toString()
   {
      StringBuilder tOutput = new StringBuilder();

      tOutput.append("position = ").append(_position).
              append("\nforward = ").append(_forward).
              append("\nup = ").append(_up).
              append("\nright = ").append(_right);
      
      return tOutput.toString();
   }
}