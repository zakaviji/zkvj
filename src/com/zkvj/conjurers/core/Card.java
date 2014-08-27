package com.zkvj.conjurers.core;

import java.io.Serializable;

/**
 * Abstract class representing a card used in Conjurers.
 */
public abstract class Card implements com.zkvj.games.cards.Card, Serializable
{
   private static final long serialVersionUID = 2038116492183179005L;
   
   private final int _id;
   private final String _name;
   private final int _energyCost;
   private final Rarity _rarity;
   private final String _text;
   
   /**
    * Constructor
    * @param aName
    * @param aEneryCost
    */
   public Card(int aID, String aName, int aEneryCost, Rarity aRarity, String aText)
   {
      this._id = aID;
      this._name = aName;
      this._energyCost = aEneryCost;
      this._rarity = aRarity;
      this._text = aText;
   }
   
   /**
    * @return the id
    */
   public int getID()
   {
      return _id;
   }

   /**
    * Returns the energy cost of this card.
    * @return int
    */
   public int getEnergyCost()
   {
      return _energyCost;
   }
   
   /**
    * Returns the name of this card.
    * @return String
    */
   public String getName()
   {
      return _name;
   }
   
   public Rarity getRarity()
   {
      return _rarity;
   }

   /**
    * @return the text
    */
   public String getText()
   {
      return _text;
   }
}
