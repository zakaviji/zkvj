/******************************************************************************
 * @file CardNumber.java
 *
 * @brief Class representing the card number of a playing card.
 *
 * @author glasscock
 *
 * @created Aug 20, 2013
 *****************************************************************************/

package com.zkvj.games.cards;

public enum CardNumber
{
   eACE("Ace"),
   eONE("One"),
   eTWO("Two"),
   eTHREE("Three"),
   eFOUR("Four"),
   eFIVE("Five"),
   eSIX("Six"),
   eSEVEN("Seven"),
   eEIGHT("Eight"),
   eNINE("Nine"),
   eTEN("Ten"),
   eJACK("Jack"),
   eQUEEN("Queen"),
   eKING("King");

   /**
    * Constructor
    * 
    * @param aName
    */
   private CardNumber(String aName)
   {
      _name = aName;
   }

   /**
    * Returns a string representation of this card number.
    */
   @Override
   public String toString()
   {
      return _name;
   }

   private final String _name;
}
