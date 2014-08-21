package com.zkvj.conjurers.core;

import java.util.ArrayList;
import java.util.List;

import com.zkvj.games.Player;
import com.zkvj.games.cards.Deck;

/**
 * Class used to keep track of everything related to the player.
 */
public class Conjurer extends Player implements Entity
{
   private Deck _deck = new Deck();
   private List<Card> _hand = new ArrayList<Card>();
   private int _health = Constants.kINITAL_PLAYER_HEALTH;
   private int _energy = Constants.kINITAL_PLAYER_ENERGY;
   
   /**
    * Constructor
    * 
    * @param aName
    */
   public Conjurer(String aName)
   {
      super(aName);
   }
   
   /**
    * Returns the deck object for this conjurer.
    * 
    * @return Deck
    */
   public Deck getDeck()
   {
      return _deck;
   }
   
   /**
    * Returns the list of cards which represent this conjurer's hand.
    * 
    * @return List<Card>
    */
   public List<Card> getHand()
   {
      return _hand;
   }
   
   /**
    * Returns this conjurer's energy.
    * 
    * @return int
    */
   public int getEnergy()
   {
      return _energy;
   }
   
   /**
    * Sets the energy for this conjurer.
    * @param aEnergy
    */
   public void setEnergy(int aEnergy)
   {
      _energy = aEnergy;
   }

   /**
    * Returns this conjurer's life.
    * 
    * @return int
    */
   @Override
   public int getHealth()
   {
      return _health;
   }
   
   /**
    * Sets the health for this conjurer.
    * @param aHealth
    */
   @Override
   public void setHealth(int aHealth)
   {
      _health = aHealth;
   }
}
