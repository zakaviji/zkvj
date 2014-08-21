package com.zkvj.games;

/**
 * Class representing a player in a game.
 */
public class Player
{
   private final String _name;
   
   /**
    * Constructor
    * @param aName
    */
   public Player(String aName)
   {
      this._name = aName;
   }
   
   /**
    * Returns the name of the player.
    * @return String
    */
   public String getName()
   {
      return _name;
   }
}