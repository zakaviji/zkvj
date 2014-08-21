package com.zkvj.conjurers.core;

/**
 * Class representing a Spell card in Conjurers.
 */
public class SpellCard extends Card
{
   private final Effect _effect;
   
   /**
    * Constructor
    * @param aName
    * @param aEneryCost
    * @param aEffect
    */
   public SpellCard(String aName,
                    int aEneryCost,
                    Rarity aRarity,
                    Effect aEffect)
   {
      super(aName, aEneryCost, aRarity);
      
      this._effect = aEffect;
   }
   
   /**
    * Returns the Effect of this card.
    * @return Effect
    */
   public Effect getEffect()
   {
      return _effect;
   }
}
