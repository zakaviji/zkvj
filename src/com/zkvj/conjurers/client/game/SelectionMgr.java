package com.zkvj.conjurers.client.game;

import java.util.ArrayList;
import java.util.List;

import com.zkvj.conjurers.core.Card;

public final class SelectionMgr
{
   /**
    * The focus card is determined by hovering the mouse over a card.
    * Details on the card are shown in the CardDetailsArea.
    */
   private static Card sFocusCard = null;
   
   /**
    * Class which defines a listener for focus card changes
    */
   public static abstract class FocusCardListener
   {
      public abstract void focusCardChanged();
   }
   
   /** list of listeners for focus card changes */
   private static final List<FocusCardListener> _focusCardListeners = 
      new ArrayList<FocusCardListener>();
   
   /**
    * The selected card is determined by actually clicking on a card (in the hand).
    * Cards are selected in order to be played.
    */
   private static Card sSelectedCard = null;
   
   /**
    * Class which defines a listener for when the selected card is played
    */
   public static abstract class PlayedCardListener
   {
      public abstract void selectedCardPlayed();
   }
   
   /** list of listeners for played cards */
   private static final List<PlayedCardListener> _playedCardListeners = 
      new ArrayList<PlayedCardListener>();
   
   /**
    * Add listener for when focus card changes
    */
   public static final void addFocusCardListener(FocusCardListener aListener)
   {
      synchronized (_focusCardListeners)
      {
         _focusCardListeners.add(aListener);
      }
   }

   /**
    * Add listener for when focus card changes
    */
   public static final void addPlayedCardListener(PlayedCardListener aListener)
   {
      synchronized (_playedCardListeners)
      {
         _playedCardListeners.add(aListener);
      }
   }
   
   /**
    * Calls all focus card change handlers to tell them focus card has changed.
    */
   private static void fireFocusCardChanged()
   {
      //hold lock while creating temp copy of list
      List<FocusCardListener> tCopy;
      synchronized (_focusCardListeners)
      {
         tCopy = new ArrayList<FocusCardListener>(_focusCardListeners);
      }

      for(FocusCardListener tListener : tCopy)
      {
         tListener.focusCardChanged();
      }
   }
   
   /**
    * Calls all focus card change handlers to tell them focus card has changed.
    */
   private static void fireSelectedCardPlayed()
   {
      //hold lock while creating temp copy of list
      List<PlayedCardListener> tCopy;
      synchronized (_playedCardListeners)
      {
         tCopy = new ArrayList<PlayedCardListener>(_playedCardListeners);
      }

      for(PlayedCardListener tListener : tCopy)
      {
         tListener.selectedCardPlayed();
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

   /**
    * @return the selectedCard
    */
   public static final Card getSelectedCard()
   {
      return sSelectedCard;
   }

   /**
    * Plays the selected card and informs listeners. After calling this method,
    * the selected card is set back to null. 
    * @return the selectedCard
    */
   public static final Card playSelectedCard()
   {
      fireSelectedCardPlayed();
      
      Card tReturn = sSelectedCard;
      sSelectedCard = null;
      
      return tReturn;
   }

   /**
    * @param aSelectedCard the selectedCard to set
    */
   public static final void setSelectedCard(Card aSelectedCard)
   {
      sSelectedCard = aSelectedCard;
   }
}
