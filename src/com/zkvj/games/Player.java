package com.zkvj.games;

import java.io.Serializable;

/**
 * Class representing a player in a game.
 */
public class Player implements Serializable
{
   private static final long serialVersionUID = 5416852658856583389L;
   
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