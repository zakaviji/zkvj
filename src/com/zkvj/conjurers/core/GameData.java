package com.zkvj.conjurers.core;

public class GameData
{
   private Conjurer _player = new Conjurer("Default Player");
   private Conjurer _opponent = new Conjurer("Default Opponent");
   private Board _board = new Board();
   private Card _focusCard = null;
   
   /**
    * Constructor. Initializes game data to default start game state.
    */
   public GameData()
   {
      _board.initializeBoard();
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
