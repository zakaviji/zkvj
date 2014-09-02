package com.zkvj.conjurers.client.game;

import java.util.ArrayList;
import java.util.List;

import com.zkvj.conjurers.core.Card;

public class FocusCardMgr
{
   /** the focus card */
   private static Card sFocusCard = null;
   
   /**
    * Class which defines a listener for focus card changes
    */
   public static abstract class FocusCardListener
   {
      public abstract void focusCardChanged();
   }
   
   /** list of listeners for focus card changes */
   private static final List<FocusCardListener> _listeners = 
      new ArrayList<FocusCardListener>();
   
   /**
    * Add listener for when focus card changes
    */
   public static void addListener(FocusCardListener aListener)
   {
      synchronized (_listeners)
      {
         _listeners.add(aListener);
      }
   }
   
   /**
    * Calls all focus card change handlers to tell them focus card has changed.
    */
   private static void fireFocusCardChanged()
   {
      //hold lock while creating temp copy of list
      List<FocusCardListener> tCopy;
      synchronized (_listeners)
      {
         tCopy = new ArrayList<FocusCardListener>(_listeners);
      }

      for(FocusCardListener tListener : tCopy)
      {
         tListener.focusCardChanged();
      }
   }
   
   /**
    * @return the focus card
    */
   public static final Card getFocusCard()
   {
      return sFocusCard;
   }
   
   /**
    * @param aCard the focus card
    */
   public static final void setFocusCard(Card aCard)
   {
      sFocusCard = aCard;
      
      fireFocusCardChanged();
   }
}
