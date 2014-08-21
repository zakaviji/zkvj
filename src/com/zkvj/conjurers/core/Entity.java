package com.zkvj.conjurers.core;

/**
 * Interface representing an entity on the game board which has health.
 */
public interface Entity
{
   public abstract int getHealth();  
   public abstract void setHealth(int aHealth);
}
