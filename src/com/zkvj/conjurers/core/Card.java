package com.zkvj.conjurers.core;

import java.io.Serializable;

/**
 * Class defining a card used in Conjurers.
 */
public final class Card implements Serializable
{
   private static final long serialVersionUID = 2038116492183179005L;
   
   /** fields common to all cards */
   private final int _id;
   private final String _name;
   private final int _energyCost;
   private final Rarity _rarity;
   private final String _text;

   /** true if this card is a minion card */
   private final boolean _isMinion;
   
   /** fields only applicable to minion cards */
   private final Element _element;
   private final int _power;
   private final int _health;
   
   /**
    * Constructor for non-minion cards.
    * @param aID
    * @param aName
    * @param aEneryCost
    * @param aRarity
    * @param aText
    */
   private Card(int aID, String aName, int aEneryCost, Rarity aRarity, String aText)
   {
      this(aID, aName, aEneryCost, aRarity, aText, Element.eNEUTRAL, 0, 0);
   }
   
   /**
    * Constructor for minion cards
    * @param aID
    * @param aName
    * @param aEneryCost
    * @param aRarity
    * @param aText
    * @param aElement
    * @param aPower
    * @param aHealth
    */
   private Card(int aID,
                String aName,
                int aEneryCost,
                Rarity aRarity,
                String aText,
                Element aElement,
                int aPower,
                int aHealth)
   {
      this._id = aID;
      this._name = aName;
      this._energyCost = aEneryCost;
      this._rarity = aRarity;
      this._text = aText;
      this._element = aElement;
      this._power = aPower;
      this._health = aHealth;
      
      //if power or health > 0, this is minion card
      this._isMinion = (aPower > 0 || aHealth > 0);
   }
   
   /**
    * Factory method for creating minion cards.
    * @param aID
    * @param aName
    * @param aEneryCost
    * @param aRarity
    * @param aText
    * @param aElement
    * @param aPower
    * @param aHealth
    * @return Card
    */
   public static Card createMinionCard(int aID,
                                       String aName,
                                       int aEneryCost,
                                       Rarity aRarity,
                                       String aText,
                                       Element aElement,
                                       int aPower,
                                       int aHealth)
   {
      return new Card(aID, aName, aEneryCost, aRarity, aText, aElement, aPower, aHealth);
   }
   
   /**
    * Factory method for creating spell cards.
    * @param aID
    * @param aName
    * @param aEneryCost
    * @param aRarity
    * @param aText
    * @return Card
    */
   public static Card createSpellCard(int aID, String aName, int aEneryCost, Rarity aRarity, String aText)
   {
      return new Card(aID, aName, aEneryCost, aRarity, aText);
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

   /**
    * @return the isMinion
    */
   public boolean isMinion()
   {
      return _isMinion;
   }

   /**
    * @return the element
    */
   public Element getElement()
   {
      return _element;
   }

   /**
    * @return the power
    */
   public int getPower()
   {
      return _power;
   }

   /**
    * @return the health
    */
   public int getHealth()
   {
      return _health;
   }
}
