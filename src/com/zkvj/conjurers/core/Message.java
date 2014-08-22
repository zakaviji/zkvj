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
      eUSER_LIST,
   }
   
   public Type type;
   public int clientID;
   public String userName;
   public String password;
   public String chatMessage;
   public String[] userList;
   
   @Override
   public String toString()
   {
      StringBuilder tBuild = new StringBuilder();
      
      tBuild.append("{")
            .append("type=").append(type)
            .append(",clientID=").append(clientID)
            .append(",username=").append(userName);
      
      if(null != password && password.length() > 0)
      {
         tBuild.append(",password=").append(password);
      }
      
      if(null != chatMessage && chatMessage.length() > 0)
      {
         tBuild.append(",chatMessage=").append(chatMessage);
      }
      
      if(null != userList && userList.length > 0)
      {
         tBuild.append(",userList=[").append(userList[0]);
         
         for(int i=1; i<userList.length; i++)
         {
            tBuild.append(",").append(userList[i]);
         }
         
         tBuild.append("]");
      }
      
      tBuild.append("}");
      
      return tBuild.toString();
   }
}
