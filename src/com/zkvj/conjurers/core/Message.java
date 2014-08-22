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
      eDESKTOP_CHAT,
      eGAME_CHAT,
   }
   
   public Type _type;
   public int _clientID;
   public String _userName;
   public String _password;
   public String _chatMessage;
   
   @Override
   public String toString()
   {
      StringBuilder tBuild = new StringBuilder();
      
      tBuild.append("{")
            .append("type=").append(_type)
            .append(",clientID=").append(_clientID)
            .append(",username=").append(_userName);
      
      if(null != _password && _password.length() > 0)
      {
         tBuild.append(",password=").append(_password);
      }
      
      if(null != _chatMessage && _chatMessage.length() > 0)
      {
         tBuild.append(",chatMessage=").append(_chatMessage);
      }
      
      tBuild.append("}");
      
      return tBuild.toString();
   }
}
