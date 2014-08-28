package com.zkvj.conjurers.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a player's deck in a game of Conjurers
 */
public class Deck implements Serializable
{
   private static final long serialVersionUID = 3513429075485536015L;
   
   /**
    * The list of cards in the deck.
    * Index 0 represents the bottom of the deck.
    * Index (size - 1) represents the top of the deck.
    * */
   private List<Card> _cards;
   
   public Deck()
   {
      _cards = new ArrayList<Card>();
   }
   
   /**
    * Constructor. Creates a new deck with the given cards.
    * @param aCards
    */
   public Deck(List<Card> aCards)
   {
      _cards = aCards;
   }
   
   /**
    * @return the cards
    */
   public List<Card> getCards()
   {
      return _cards;
   }

   /**
    * @param aCards the cards to set
    */
   public void setCards(List<Card> aCards)
   {
      _cards = aCards;
   }

   /**
    * Removes the top card from the deck and returns it. Null if the deck is empty.
    * @return Card
    */
   public Card draw()
   {
      Card tReturn = null;
      
      if(!_cards.isEmpty())
      {
         tReturn = _cards.remove(_cards.size()-1);
      }
      
      return tReturn;
   }
   
   /**
    * Randomizes the order of the cards in the deck.
    */
   public void shuffle()
   {
      Collections.shuffle(_cards);
   }

   /**
    * @return the number of cards in the deck
    */
   public int size()
   {
      return _cards.size();
   }
}
