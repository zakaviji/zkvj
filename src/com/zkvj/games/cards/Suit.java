/******************************************************************************
 * @file Suit.java
 *
 * @brief Class representing the suit of a playing card.
 *
 * @author glasscock
 *
 * @created Aug 20, 2013
 *****************************************************************************/

package com.zkvj.games.cards;

public enum Suit
{
   eHEARTS("Hearts"),
   eDIAMONDS("Diamonds"),
   eSPADES("Spades"),
   eCLUBS("Clubs");

   /**
    * Constructor
    * 
    * @param aName
    */
   private Suit(String aName)
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
