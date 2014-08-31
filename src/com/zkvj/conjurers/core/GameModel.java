package com.zkvj.conjurers.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameModel implements Serializable
{
   private static final long serialVersionUID = 6438259023082620415L;
   
   /** the data managed by this model */
   private GameData _data;

   /**
    * Class which defines a listener for game data changes
    */
   public static abstract class GameModelListener
   {
      public abstract void gameDataChanged();
   }
   
   /** list of listeners for game data changes */
   private final transient List<GameModelListener> _listeners = 
      new ArrayList<GameModelListener>();
   
   /**
    * Constructor
    * @param aData - the game data
    */
   public GameModel(GameData aData)
   {
      _data = aData;
   }
   
   /**
    * Add listener for when game data changes
    */
   public void addListener(GameModelListener aListener)
   {
      synchronized (_listeners)
      {
         _listeners.add(aListener);
      }
   }
   
   /**
    * Calls all game data change handlers to tell them game data has changed.
    */
   private void fireGameDataChanged()
   {
      //hold lock while creating temp copy of list
      List<GameModelListener> tCopy;
      synchronized (_listeners)
      {
         tCopy = new ArrayList<GameModelListener>(_listeners);
      }

      for(GameModelListener tListener : tCopy)
      {
         tListener.gameDataChanged();
      }
   }
   
   /**
    * Set the data managed by this model.
    * @param aData - the game data
    */
   public void setGameData(GameData aData)
   {
      _data = aData;
      
      fireGameDataChanged();
   }

   /**
    * @return the game data
    */
   public GameData getGameData()
   {
      return _data;
   }
}
