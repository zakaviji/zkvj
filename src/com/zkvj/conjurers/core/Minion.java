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
   private int _controllerID;
   private Element _element;
   //private Effect _effect;
   
   /**
    * Constructor
    * @param aCard
    * @param aControllerID
    */
   public Minion(Card aCard, int aControllerID)
   {
      _card = aCard;
      _controllerID = aControllerID;
      _power = _card.getPower();
      _health = _card.getHealth();
      _name = _card.getName();
      _element = _card.getElement();
      //_effect = _card.getEffect();
   }
   
   /**
    * Copy constructor
    * @param aMinion
    */
   public Minion(Minion aMinion)
   {
      _card = aMinion._card;
      _controllerID = aMinion._controllerID;
      _power = aMinion._power;
      _health = aMinion._health;
      _name = aMinion._name;
      _element = aMinion._element;
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
    * Returns the ID of the controller of this minion.
    * @return int
    */
   public int getController()
   {
      return _controllerID;
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
    * @param aControllerID
    */
   public void setController(int aControllerID)
   {
      _controllerID = aControllerID;
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
