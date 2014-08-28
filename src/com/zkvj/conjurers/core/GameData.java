package com.zkvj.conjurers.core;

import java.io.Serializable;

public class GameData implements Serializable
{
   private static final long serialVersionUID = 2399765568244517550L;
   
   private Conjurer _player;
   private Conjurer _opponent;
   private Board _board;
   private Card _focusCard;
   
   /**
    * Constructor. Initializes game data to default start game state.
    */
   public GameData(Conjurer aPlayer, Conjurer aOpponent)
   {
      _player = aPlayer;
      _opponent = aOpponent;
      _board = new Board();
      _focusCard = null;
   }
   
   /**
    * @return the _board
    */
   public Board getBoard()
   {
      return _board;
   }
   /**
    * @param aBoard the _board to set
    */
   public void setBoard(Board aBoard)
   {
      this._board = aBoard;
   }
   /**
    * @return the _opponent
    */
   public Conjurer getOpponent()
   {
      return _opponent;
   }
   /**
    * @param aOpponent the _opponent to set
    */
   public void setOpponent(Conjurer aOpponent)
   {
      this._opponent = aOpponent;
   }
   /**
    * @return the _player
    */
   public Conjurer getPlayer()
   {
      return _player;
   }
   /**
    * @param aPlayer the _player to set
    */
   public void setPlayer(Conjurer aPlayer)
   {
      this._player = aPlayer;
   }
   /**
    * @return the _focusCard
    */
   public Card getFocusCard()
   {
      return _focusCard;
   }
   /**
    * @param aFocusCard the _focusCard to set
    */
   public void setFocusCard(Card aFocusCard)
   {
      this._focusCard = aFocusCard;
   }
}
