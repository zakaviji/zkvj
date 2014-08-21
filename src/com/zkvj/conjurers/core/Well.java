package com.zkvj.conjurers.core;

/**
 * An individual hexagonal space on the Conjurers game board.
 */
public class Well
{
   private Element _elementType = Element.eNEUTRAL;
   private Conjurer _controller = null;
   
   /**
    * Constructor
    */
   public Well()
   {
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