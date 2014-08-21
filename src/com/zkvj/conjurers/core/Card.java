package com.zkvj.conjurers.core;

/**
 * Abstract class representing a card used in Conjurers.
 */
public abstract class Card implements com.zkvj.games.cards.Card
{
   private final String _name;
   private final int _energyCost;
   private final Rarity _rarity;
   
   /**
    * Constructor
    * @param aName
    * @param aEneryCost
    */
   public Card(String aName, int aEneryCost, Rarity aRarity)
   {
      this._name = aName;
      this._energyCost = aEneryCost;
      this._rarity = aRarity;
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
}
