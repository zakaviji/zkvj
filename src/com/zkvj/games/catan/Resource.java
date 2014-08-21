/******************************************************************************
 * @file Resource.java
 *
 * @brief Enumeration defining the various resources available in Catan.
 *
 * @author glasscock
 *
 * @created Aug 9, 2013
 *****************************************************************************/

package com.zkvj.games.catan;

public enum Resource
{
   eWOOD("Wood"),
   eBRICK("Brick"),
   eSHEEP("Sheep"),
   eWHEAT("Wheat"),
   eROCK("Rock");

   private Resource(String aName)
   {
      this._name = aName;
   }

   /**
    * Returns a String representation for this resource type.
    * @return String
    */
   @Override
   public String toString()
   {
      return _name;
   }

   private final String _name;
}