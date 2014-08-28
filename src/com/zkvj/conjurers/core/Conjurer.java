package com.zkvj.conjurers.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zkvj.games.Player;

/**
 * Class used to keep track of everything related to the player.
 */
public class Conjurer extends Player implements Entity, Serializable
{
   private static final long serialVersionUID = 6886979304549117881L;
   
   private Deck _deck;
   private List<Card> _hand;
   private int _health;
   private int _energy;
   
   /**
    * Constructor
    * 
    * @param aName
    */
   public Conjurer(String aName, Deck aDeck)
   {
      super(aName);
      
      _deck = aDeck;
      _hand = new ArrayList<Card>();
      _health = Constants.kINITAL_PLAYER_HEALTH;
      _energy = Constants.kINITAL_PLAYER_ENERGY;
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
