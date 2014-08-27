package com.zkvj.conjurers.core;

/**
 * Class representing a Spell card in Conjurers.
 */
public class SpellCard extends Card
{
   private static final long serialVersionUID = -617073088682063706L;
   
   private final Effect _effect;
   
   /**
    * Constructor
    * @param aName
    * @param aEneryCost
    * @param aEffect
    */
   public SpellCard(int aID,
                    String aName,
                    int aEneryCost,
                    Rarity aRarity,
                    String aText,
                    Effect aEffect)
   {
      super(aID, aName, aEneryCost, aRarity, aText);
      
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
