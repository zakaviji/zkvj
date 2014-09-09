package com.zkvj.conjurers.core;

import java.util.HashMap;
import java.util.Map;

/**
 * This class maintains a database of all cards available in Conjurers.
 */
public final class CardDB
{
   /**
    * database of all cards
    */
   private static final Map<Integer, Card> kDB = new HashMap<Integer, Card>();
   
   /**
    * Returns the Card with the given ID, if any, else returns null.
    * @param aID - card ID number
    * @return Card
    */
   public static Card getCard(Integer aID)
   {
      return kDB.get(aID);
   }
   
   /**
    * Loads default cards (for testing purposes).
    */
   public static void loadDefaultCards()
   {
      kDB.clear();
      
      kDB.put(new Integer(1),
            Card.createMinionCard(1,"The King's Pawn",
                                  2,Rarity.eBRONZE,
                                  "",
                                  Element.eNEUTRAL,2,1));
      kDB.put(new Integer(2),
            Card.createMinionCard(2,"The King's Rook",
                                  5,Rarity.eSILVER,
                                  "Can move up to 2 spaces (in a line) when moving laterally or backwards.",
                                  Element.eNEUTRAL,2,3));
      kDB.put(new Integer(3),
            Card.createMinionCard(3,"The King's Knight",
                                  6,Rarity.eSILVER,
                                  "Can move up to 2 spaces (in any direction).",
                                  Element.eNEUTRAL,3,3));
      kDB.put(new Integer(4),
            Card.createMinionCard(4,"The King's Bishop",
                                  7,Rarity.eSILVER,
                                  "Can move up to 2 spaces (in a line). Cannot move laterally.",
                                  Element.eNEUTRAL,4,4));
      kDB.put(new Integer(5),
            Card.createMinionCard(5,"The King's Queen",
                                  9,Rarity.eGOLD,
                                  "Can move up to 3 spaces (in a line).",
                                  Element.eNEUTRAL,6,5));
      kDB.put(new Integer(6),
            Card.createMinionCard(6,"Reinhart King of Thrylia",
                                  10,Rarity.ePLATINUM,
                                  "Double Attack (able to declare two moves per turn).",
                                  Element.eNEUTRAL,7,7));
   }
}