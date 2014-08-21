package com.zkvj.games.othello;

/**
 * This class represents an individual piece in the game Othello.
 * 
 * @author zakaviji
 */
class Piece
{
   public boolean _isWhite; // true = white; false = black
   public boolean _isReal; // true = actually there; false = only a possibility

   /**
    * Constructor
    * @param aIsWhite
    * @param aIsReal
    */
   public Piece(boolean aIsWhite, boolean aIsReal)
   {
      _isWhite = aIsWhite;
      _isReal = aIsReal;
   }

   /**
    * Changes this pieces from white to black, or vice versa.
    */
   public void flip()
   {
      _isWhite = !_isWhite;
   }
}