package com.zkvj.conjurers.core;

import java.util.HashMap;
import java.util.Map;

import com.zkvj.games.cards.Deck;

/**
 * This class is responsible for managing card data.
 */
public final class CardManager
{
   /**
    * database of all cards
    */
   private static final Map<Integer, Card> kCARD_DB = new HashMap<Integer, Card>();
   
   /**
    * Builds a default deck that can be used for testing purposes.
    * @return Deck
    */
   public static Deck getDefaultDeck()
   {
      Deck tReturn = new Deck();
      
      tReturn.addTop(kCARD_DB.get(1));
      tReturn.addTop(kCARD_DB.get(1));
      tReturn.addTop(kCARD_DB.get(2));
      tReturn.addTop(kCARD_DB.get(2));
      tReturn.addTop(kCARD_DB.get(3));
      tReturn.addTop(kCARD_DB.get(3));
      tReturn.addTop(kCARD_DB.get(4));
      tReturn.addTop(kCARD_DB.get(4));
      tReturn.addTop(kCARD_DB.get(5));
      tReturn.addTop(kCARD_DB.get(5));
      tReturn.addTop(kCARD_DB.get(6));
      tReturn.addTop(kCARD_DB.get(6));
      
      return tReturn;
   }
   
   /**
    * Loads default cards (for testing purposes).
    */
   public static void loadDefaultCards()
   {
      kCARD_DB.clear();
      
      kCARD_DB.put(new Integer(1),
            new MinionCard(1,"The King's Pawn",
                           2,Rarity.eBRONZE,
                           "",
                           Element.eNEUTRAL,
                           2,1,null));
      kCARD_DB.put(new Integer(2),
            new MinionCard(2,"The King's Rook",
                           5,Rarity.eSILVER,
                           "Can move up to 2 spaces (in a line) when moving laterally or backwards.",
                           Element.eNEUTRAL,
                           2,3,null));
      kCARD_DB.put(new Integer(3),
            new MinionCard(3,"The King's Knight",
                           6,Rarity.eSILVER,
                           "Can move up to 2 spaces (in any direction).",
                           Element.eNEUTRAL,
                           3,3,null));
      kCARD_DB.put(new Integer(4),
            new MinionCard(4,"The King's Bishop",
                           7,Rarity.eSILVER,
                           "Can move up to 3 spaces (in a line). Cannot move laterally.",
                           Element.eNEUTRAL,
                           4,4,null));
      kCARD_DB.put(new Integer(5),
            new MinionCard(5,"The King's Queen",
                           9,Rarity.eGOLD,
                           "Can move up to 3 spaces (in a line).",
                           Element.eNEUTRAL,
                           6,5,null));
      kCARD_DB.put(new Integer(6),
            new MinionCard(6,"Reinhart King of Thrylia",
                           10,Rarity.eBRONZE,
                           "Double Attack (able to declare two moves per turn).",
                           Element.eNEUTRAL,
                           7,7,null));
   }
}