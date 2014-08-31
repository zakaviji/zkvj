package com.zkvj.conjurers.core;

import java.io.Serializable;
import java.util.List;

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
      /**
       * Messages from the client
       */
      eLOGIN_REQUEST,
      eLOGOUT,
      eGAME_ACCEPT,
      
      /**
       * Messages from the server
       */
      eLOGIN_ACCEPTED,
      eLOGIN_REJECTED,
      eUSER_LIST,
      eGAME_START,
      
      /**
       * Two-way messages
       */
      eDESKTOP_CHAT,
      eGAME_CHAT,
      eGAME_REQUEST,
      eGAME_DATA,
   }
   
   /**
    * Message type; must be specified in constructor
    */
   public final Type type;
   
   /**
    * The client ID and user name fields always correspond to client who 
    * is sending/receiving the message
    */
   public int clientID;
   public String userName;
   
   /**
    * used for eLOGIN_REQUEST messages
    */
   public String password;
   
   /**
    * used for eDESKTOP_CHAT and eGAME_CHAT messages
    */
   public String chatMessage;
   
   /**
    * used for eUSER_LIST messages
    */
   public List<String> userList;
   
   /**
    * used for eGAME_REQUEST and eGAME_START messages
    */
   public String opponent;
   
   /**
    * user for eGAME_START and eGAME_DATA messages
    */
   public GameData gameData;
   
   /**
    * Constructor
    * @param aType - message type
    */
   public Message(Type aType)
   {
      type = aType;
   }
   
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
      
      if(null != userList && userList.size() > 0)
      {
         tBuild.append(",userList=[").append(userList.get(0));
         
         for(int i=1; i<userList.size(); i++)
         {
            tBuild.append(",").append(userList.get(i));
         }
         
         tBuild.append("]");
      }
      
      if(null != opponent && opponent.length() > 0)
      {
         tBuild.append(",opponent=").append(opponent);
      }
      
      if(null != gameData)
      {
         tBuild.append(",gameData=yes");
      }
      
      tBuild.append("}");
      
      return tBuild.toString();
   }
}
