package com.zkvj.conjurers.core;

import java.io.Serializable;

/**
 * Message sent between the client and server
 */
public class Message implements Serializable
{
   private static final long serialVersionUID = -8815722793230284217L;

   /**
    * Different types of messages
    */
   public enum Type
   {
      eLOGIN_REQUEST,
      eLOGIN_ACCEPTED,
      eLOGIN_REJECTED,
      eLOGOUT,
   }
   
   public Type _type;
   public String _userName;
   public String _password;
   
   @Override
   public String toString()
   {
      StringBuilder tBuild = new StringBuilder();
      
      tBuild.append("{")
            .append("type=").append(_type)
            .append(",username=").append(_userName)
            .append(",password=").append(_password)
            .append("}");
      
      return tBuild.toString();
   }
}
