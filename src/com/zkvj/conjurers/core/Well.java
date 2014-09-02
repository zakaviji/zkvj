package com.zkvj.conjurers.core;

import java.io.Serializable;

/**
 * An individual hexagonal space on the Conjurers game board. Immutable.
 */
public final class Well implements Serializable
{
   private static final long serialVersionUID = -3082920850583791539L;
   
   public final Element elementType;
   public final int controllerID;
   
   /**
    * Default Constructor
    */
   public Well()
   {
      elementType = Element.eNEUTRAL;
      controllerID = Conjurer.kNONE;
   }
   
   /**
    * Constructor
    * @param aElement
    * @param aControllerID
    */
   public Well(Element aElement, int aControllerID)
   {
      elementType = aElement;
      controllerID = aControllerID;
   }
   
   /**
    * Copy constructor.
    * @param aWell
    */
   public Well(Well aWell)
   {
      elementType = aWell.elementType;
      controllerID = aWell.controllerID;
   }

   /**
    * Returns a string representation for this hex.
    */
   @Override
   public String toString()
   {
      return elementType.toString();
   }
}