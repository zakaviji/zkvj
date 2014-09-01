package com.zkvj.conjurers.core;

import java.io.Serializable;

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
   public Board getBoard()
   {
      return board;
   }
   
   /**
    * @param aPlayerID - player A or player B (see enum Conjurer.ID)
    * @return Conjurer
    */
   public Conjurer getPlayer(int aPlayerID)
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
   public int getTurnPlayerID()
   {
      return turnPlayerID;
   }
   
   /**
    * Toggles the turn player ID.
    */
   public void endTurn()
   {
      if(Conjurer.kPLAYER_A == turnPlayerID)
      {
         turnPlayerID = Conjurer.kPLAYER_B;
      }
      else
      {
         turnPlayerID = Conjurer.kPLAYER_A;
      }
   }
}
