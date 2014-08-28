package com.zkvj.conjurers.core;

import java.io.Serializable;

/**
 * An individual hexagonal space on the Conjurers game board.
 */
public class Well implements Serializable
{
   private static final long serialVersionUID = -3082920850583791539L;
   
   private Element _elementType;
   private Conjurer _controller;
   
   /**
    * Constructor
    */
   public Well()
   {
      _elementType = Element.eNEUTRAL;
      _controller = null;
   }
   
   /**
    * Constructor
    * @param aElement
    */
   public Well(Element aElement)
   {
      _elementType = aElement;
      _controller = null;
   }

   /**
    * Returns the element type for this hex.
    * @return Element
    */
   public Element getElementType()
   {
      return _elementType;
   }
   
   /**
    * Sets the element type for this hex.
    * @param aElementType
    */
   public void setElementType(Element aElementType)
   {
      _elementType = aElementType;
   }
   
   /**
    * Returns the conjurer currently in control of this well.
    * @return Conjurer
    */
   public Conjurer getController()
   {
      return _controller;
   }
   
   /**
    * Sets the given conjurer as being in control of this well.
    * @param aNewController
    */
   public void setController(Conjurer aNewController)
   {
      _controller = aNewController;
   }
   
   /**
    * Returns a string representation for this hex.
    */
   @Override
   public String toString()
   {
      return _elementType.toString();
   }
}