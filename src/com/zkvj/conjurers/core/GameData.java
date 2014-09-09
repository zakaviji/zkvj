package com.zkvj.conjurers.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.Map;

public final class GameData implements Serializable
{
   private static final long serialVersionUID = 2399765568244517550L;
   
   private final Conjurer playerA;
   private final Conjurer playerB;
   private final Board board;
   private int turnPlayerID;

   /**
    * Constructor. Initializes game data to default start game state.
    * @param aPlayerA
    * @param aPlayerB
    */
   public GameData(Conjurer aPlayerA, Conjurer aPlayerB)
   {
      playerA = aPlayerA;
      playerB = aPlayerB;
      board = new Board();
      
      //assign initial energy to both players
//      updateEnergy(Conjurer.kPLAYER_A);
//      updateEnergy(Conjurer.kPLAYER_B);
      
      //randomly choose who goes first
      turnPlayerID = (Math.random() < .5)? Conjurer.kPLAYER_A : Conjurer.kPLAYER_B;
      
      playerA.getDeck().shuffle();
      playerB.getDeck().shuffle();
      
      //draw opening hands
      for(int i=0; i<Constants.kINITIAL_HAND_SIZE; i++)
      {
         playerA.getHand().add(playerA.getDeck().draw());
         playerB.getHand().add(playerB.getDeck().draw());
      }
   }
   
   /**
    * Copy constructor
    * @param aGameData
    */
   public GameData(GameData aGameData)
   {
      playerA = new Conjurer(aGameData.playerA);
      playerB = new Conjurer(aGameData.playerB);
      board = new Board(aGameData.board);
      turnPlayerID = aGameData.turnPlayerID;
   }

   /**
    * @return the _board
    */
   public final Board getBoard()
   {
      return board;
   }
   
   /**
    * @param aPlayerID - player A or player B (see enum Conjurer.ID)
    * @return Conjurer
    */
   public final Conjurer getPlayer(int aPlayerID)
   {
      Conjurer tReturn = null;
      
      if(Conjurer.kPLAYER_A == aPlayerID)
      {
         tReturn = playerA;
      }
      else if(Conjurer.kPLAYER_B == aPlayerID)
      {
         tReturn = playerB;
      }
      
      return tReturn;
   }
   
   /**
    * @return the turnPlayerID
    */
   public final int getTurnPlayerID()
   {
      return turnPlayerID;
   }
   
   /**
    * Changes turn from one player to the next.
    */
   public final void endTurn()
   {
      if(Conjurer.kPLAYER_A == turnPlayerID)
      {
         turnPlayerID = Conjurer.kPLAYER_B;
      }
      else
      {
         turnPlayerID = Conjurer.kPLAYER_A;
      }
      
      //updateEnergy(turnPlayerID);
      drawCard(turnPlayerID);
   }
   
   /**
    * Causes the given player to draw a card (remove from top of deck, add to hand).
    * @param aPlayerID
    */
   public final void drawCard(int aPlayerID)
   {
      getPlayer(aPlayerID).drawCard();
   }
   
   /**
    * Calculate and assign appropriate energy to given player
    * @param aPlayerID
    */
   public final void updateEnergy(int aPlayerID)
   {
      int tEnergy = 0;
      for(Map.Entry<Point, Well> tEntry : board.getWells())
      {
         if(null != tEntry.getValue() &&
            tEntry.getValue().controllerID == aPlayerID)
         {
            tEnergy++;
         }
      }
      getPlayer(aPlayerID).setEnergy(tEnergy);
   }

   /**
    * Plays the given card if it exists in the given player's hand and if the
    * player has enough energy. If the card is a minion card, this method
    * creates and returns a new Minion.
    * @param aPlayerID
    * @param aCard
    * @return Minion or null
    */
   public Minion playCardFromHand(int aPlayerID, Card aCard)
   {
      Minion tReturn = null;
      
      int tPlayerEnergy = getPlayer(aPlayerID).getEnergy();
      
      //try to play card from hand
      if(null != aCard &&
         tPlayerEnergy >= aCard.getEnergyCost() &&
         getPlayer(aPlayerID).getHand().remove(aCard))
      {
         getPlayer(aPlayerID).setEnergy(tPlayerEnergy - aCard.getEnergyCost());
       
         if(aCard.isMinion())
         {
            tReturn = new Minion(aCard, aPlayerID);
         }
      }
            
      return tReturn;
   }
}
