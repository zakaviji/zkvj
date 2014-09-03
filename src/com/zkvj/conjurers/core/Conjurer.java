package com.zkvj.conjurers.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zkvj.games.Player;

/**
 * Class used to keep track of everything related to the player.
 */
public class Conjurer extends Player implements Serializable
{
   private static final long serialVersionUID = 6886979304549117881L;
   
   /** Constants used to identify which player is which */
   public static final int kNONE = 0;
   public static final int kPLAYER_A = 1;
   public static final int kPLAYER_B = 2;
   
   private final int _playerID;
   private Deck _deck;
   private List<Card> _hand;
   private int _health;
   private int _energy;
   
   /**
    * Constructor
    * 
    * @param aName
    */
   public Conjurer(String aName, int aPlayerID, Deck aDeck)
   {
      super(aName);
      
      _playerID = aPlayerID;
      _deck = aDeck;
      _hand = new ArrayList<Card>();
      _health = Constants.kINITAL_PLAYER_HEALTH;
      _energy = Constants.kINITAL_PLAYER_ENERGY;
   }
   
   /**
    * Copy constructor
    * @param aPlayerA
    */
   public Conjurer(Conjurer aConjurer)
   {
      super(aConjurer.getName());
      
      _playerID = aConjurer._playerID;
      _deck = new Deck(aConjurer._deck);
      _hand = new ArrayList<Card>(aConjurer.getHand());
      _health = aConjurer._health;
      _energy = aConjurer._energy;
   }
    
   /**
    * Removes the top card from the deck and adds it to the hand.
    */
   public final void drawCard()
   {
      _hand.add(_deck.draw());
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
   public int getHealth()
   {
      return _health;
   }
   
   /**
    * Sets the health for this conjurer.
    * @param aHealth
    */
   public void setHealth(int aHealth)
   {
      _health = aHealth;
   }

   /**
    * @return the playerID
    */
   public int getPlayerID()
   {
      return _playerID;
   }

   /**
    * Updates this Conjurer object with data from the given object.
    * @param aConjurer
    */
   public void updateFrom(Conjurer aConjurer)
   {
      _health = aConjurer._health;
      _energy = aConjurer._energy;
      _deck = aConjurer._deck;
      _hand = aConjurer._hand;
   }
}
