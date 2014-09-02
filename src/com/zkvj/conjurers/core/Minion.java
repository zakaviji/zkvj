package com.zkvj.conjurers.core;

import java.io.Serializable;

/**
 * Class representing a minion entity that is on the board.
 */
public class Minion implements Serializable
{
   private static final long serialVersionUID = -6964740931116576091L;
   
   private final Card _card;
   private int _power;
   private int _health;
   private String _name;
   private Conjurer _controller;
   private Element _element;
   //private Effect _effect;
   
   /**
    * Constructor
    * @param aCard
    * @param aController
    */
   public Minion(Card aCard, Conjurer aController)
   {
      _card = aCard;
      _controller = aController;
      _power = _card.getPower();
      _health = _card.getHealth();
      _name = _card.getName();
      _element = _card.getElement();
      //_effect = _card.getEffect();
   }
   
   /**
    * Returns the card object for this minion.
    * @return MinionCard
    */
   public Card getCard()
   {
      return _card;
   }
   
   /**
    * Returns the controller of this minion.
    * @return Conjurer
    */
   public Conjurer getController()
   {
      return _controller;
   }
   
   /**
    * Returns the element of this minion.
    * @return Element
    */
   public Element getElement()
   {
      return _element;
   }
   
   /**
    * Returns the name of this minion.
    * @return String
    */
   public String getName()
   {
      return _name;
   }
   
   /**
    * Returns the power for this minion.
    * @return int
    */
   public int getPower()
   {
      return _power;
   }

   /**
    * Returns the health of this minion.
    * @return int
    */
   public int getHealth()
   {
      return _health;
   }
   
   /**
    * Sets the controller of this minion
    * @param aController
    */
   public void setController(Conjurer aController)
   {
      _controller = aController;
   }
   
   /**
    * Sets the element of this minion
    * @param aElement
    */
   public void setElement(Element aElement)
   {
      _element = aElement;
   }
   
   /**
    * Sets the name of this minion.
    * @param aName
    */
   public void setName(String aName)
   {
      _name = aName;
   }
   
   /**
    * Sets the power of this minion.
    * @param aPower
    */
   public void setPower(int aPower)
   {
      _power = aPower;
   }
   
   /**
    * Sets the current health of this minion.
    * @param aHealth
    */
   public void setHealth(int aHealth)
   {
      _health = aHealth;
   }
}
