/******************************************************************************
 * @file Object3D.java
 *
 * @brief 3D object consisting of a set of edges.
 *
 * @author glasscock
 *
 * @created Oct 6, 2011
 *****************************************************************************/

/* Package declaration */
package com.zkvj.projection;

/*---- non-JDK imports ------------------------------------------------------*/

/*---- JDK imports ----------------------------------------------------------*/
import com.zkvj.math.Vector3D;

import java.util.Set;

/**
 * 3D object consisting of a set of edges.
 */
public class Object3D
{
   /**
    * Constructor
    *
    * @param aEdges - set of edges to define this object
    */
   public Object3D(Set<Edge3D> aEdges)
   {
      this._edges = aEdges;
   }

   /**
    * @return the set of edges which defines this object
    */
   public Set<Edge3D> getEdges()
   {
      return _edges;
   }

   /**
    * Move this object according to the given vector.
    * @param aV - movement vector
    */
   public void move(Vector3D aV)
   {
      for(Edge3D tEdge : _edges)
      {
         tEdge.a.x += aV.x;
         tEdge.a.y += aV.y;
         tEdge.a.z += aV.z;
         tEdge.b.x += aV.x;
         tEdge.b.y += aV.y;
         tEdge.b.z += aV.z;
      }
   }

   /**
    * @return string representation of this object
    */
   @Override
   public String toString()
   {
      StringBuilder tReturn = new StringBuilder();

      for(Edge3D tEdge : _edges)
      {
         tReturn.append("Edge: ").append(tEdge.toString()).append("\n");
      }

      return tReturn.toString();
   }

   /** The set of edges which defines this object */
   private Set<Edge3D> _edges;
}