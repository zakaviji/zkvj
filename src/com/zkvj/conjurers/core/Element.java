package com.zkvj.conjurers.core;

import java.awt.Color;

/**
 * Enumeration defining the elements available in Conjurers.
 */
public enum Element
{
   eFIRE("Fire", Constants.kFIRE_COLOR),
   eAIR("Air", Constants.kAIR_COLOR),
   eWATER("Water", Constants.kWATER_COLOR),
   eEARTH("Earth", Constants.kEARTH_COLOR),
   eNEUTRAL("Neutral", Constants.kNEUTRAL_COLOR);
   
   private static final Element[] kValues = Element.values();
   public static final int kNUM_VALUES = kValues.length;

   private final String _name;
   private final Color _color;

   /**
    * Constructor
    * @param aName
    * @param aColor
    */
   private Element(String aName, Color aColor)
   {
      this._name = aName;
      this._color = aColor;
   }
   
   /**
    * Returns the color for this element type.
    * @return Color
    */
   public Color getColor()
   {
      return _color;
   }

   /**
    * Returns a random Element type.
    * @return Element
    */
   public static Element getRandom()
   {
      return kValues[(int)Math.floor(kNUM_VALUES * Math.random())];
   }
   
   /**
    * Returns array of all Element values.
    * @return Element[]
    */
   public static Element[] getValues()
   {
      return kValues;
   }

   /**
    * Returns a String representation for this type.
    * @return String
    */
   @Override
   public String toString()
   {
      return _name;
   }
}