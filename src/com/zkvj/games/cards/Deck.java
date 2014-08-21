package com.zkvj.games.cards;

import java.util.ArrayList;

/**
 * Class representing a deck of cards.
 */
public class Deck
{
   /** array list of the cards in this deck */
   private final ArrayList<PlayingCard> _deck = new ArrayList<PlayingCard>();
   
   /**
    * Constructor
    */
   public Deck()
   {
   }
   
   /**
    * Adds the given card to the bottom of the deck.
    * @param aCard
    */
   public void addBottom(PlayingCard aCard)
   {
      _deck.add(aCard);
   }
   
   /**
    * Adds the given card to the deck at random.
    * @param aCard
    */
   public void addRandom(PlayingCard aCard)
   {
      int tIndex = (int)Math.floor(size() * Math.random());
      _deck.add(tIndex, aCard);
   }
   
   /**
    * Adds the given card to the top of the deck.
    * @param aCard
    */
   public void addTop(PlayingCard aCard)
   {
      _deck.add(0, aCard);
   }
   
   /**
    * Removes the bottom card from the deck and returns it to the caller.
    * Returns null if the deck is empty.
    * @return Card or null
    */
   public PlayingCard drawBottom()
   {
      PlayingCard tReturn = null;
      
      if(size() > 0)
      {
         tReturn = _deck.remove(size() - 1);
      }
      
      return tReturn;
   }
   
   /**
    * Removes a random card from the deck and returns it to the caller.
    * Returns null if the deck is empty.
    * @return Card or null
    */
   public PlayingCard drawRandom()
   {
      PlayingCard tReturn = null;
      
      if(size() > 0)
      {
         int tIndex = (int)Math.floor(size() * Math.random());
         tReturn = _deck.remove(tIndex);
      }
      
      return tReturn;
   }
   
   /**
    * Removes the top card from the deck and returns it to the caller.
    * Returns null if the deck is empty.
    * @return Card or null
    */
   public PlayingCard drawTop()
   {
      PlayingCard tReturn = null;
      
      if(size() > 0)
      {
         tReturn = _deck.remove(0);
      }
      
      return tReturn;
   }
   
   /**
    * Returns the current size of this deck.
    * @return int
    */
   public int size()
   {
      return _deck.size();
   }
   
   /**
    * Puts the cards in this deck in random order
    */
   public void shuffle()
   {
      //TODO
   }
}
