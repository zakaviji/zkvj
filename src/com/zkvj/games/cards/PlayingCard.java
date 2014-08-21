package com.zkvj.games.cards;

/**
 * This class represents a standard playing card.
 */
public class PlayingCard implements Card
{
   /**
    * Constructor
    * @param aNum
    * @param aSuit
    */
   public PlayingCard(CardNumber aNum, Suit aSuit)
   {
      _num = aNum;
      _suit = aSuit;
   }
   
   /**
    * Returns the CardNumber of this Card
    * @return CardNumber
    */
   public CardNumber getNum()
   {
      return _num;
   }
   
   /**
    * Returns the Suit of this Card
    * @return Suit
    */
   public Suit getSuit()
   {
      return _suit;
   }

   /**
    * Returns a string representation of this object.
    */
   @Override
   public String toString()
   {
      return _num.toString() + " of " + _suit.toString();
   }
   
   private final Suit _suit;
   private final CardNumber _num;
}
