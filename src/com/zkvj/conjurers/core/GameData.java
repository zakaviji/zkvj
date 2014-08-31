package com.zkvj.conjurers.core;

import java.io.Serializable;

public final class GameData implements Serializable
{
   private static final long serialVersionUID = 2399765568244517550L;
   
   private final Conjurer playerA;
   private final Conjurer playerB;
   private final Board board;
   
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
}
