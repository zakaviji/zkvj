package com.zkvj.conjurers.core;

/**
 * Class representing a minion card in Conjurers.
 */
public class MinionCard extends Card
{
   private static final long serialVersionUID = 3827470256463249083L;
   
   private final Element _element;
   private final int _power;
   private final int _health;
   private final Effect _effect;
   
   /**
    * Constructor
    * @param aName
    * @param aEneryCost
    * @param aElement
    * @param aPower
    * @param aHealth
    * @param aEffect
    */
   public MinionCard(int aID,
                     String aName,
                     int aEneryCost,
                     Rarity aRarity,
                     String aText,
                     Element aElement,
                     int aPower,
                     int aHealth,
                     Effect aEffect)
   {
      super(aID, aName, aEneryCost, aRarity, aText);
      
      this._element = aElement;
      this._power = aPower;
      this._health = aHealth;
      this._effect = aEffect;
   }
   
   /**
    * Returns the effect for thsi card.
    * @return Effect
    */
   public Effect getEffect()
   {
      return _effect;
   }
   
   /**
    * Returns the element for this card.
    * @return Element
    */
   public Element getElement()
   {
      return _element;
   }
   
   /**
    * Returns the heath for this card.
    * @return int
    */
   public int getHealth()
   {
      return _health;
   }
   
   /**
    * Returns the power for this card.
    * @return int
    */
   public int getPower()
   {
      return _power;
   }
}
