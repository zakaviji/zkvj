package com.zkvj.conjurers.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zkvj.conjurers.core.Card;
import com.zkvj.conjurers.core.CardDB;
import com.zkvj.conjurers.core.ClientState;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Deck;
import com.zkvj.conjurers.core.GameData;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;

/**
 * Server process for Conjurers
 */
public class Server
{
   /** keep track of the next available client ID */
   private static int sNextUniqueID = 1;
   
   /** the port number */
   private int _port;
   
   /** while true, keep listening for clients */
   private boolean _keepServing;
   
   /** map of client threads by client ID */
   private Map<Integer, ServerThread> _threadMap = new HashMap<Integer, ServerThread>();
   
   /** map of client ID's by user name */
   private Map<String, Integer> _userNameMap = new HashMap<String, Integer>();
   
   /**
    * Constructor
    * @param aPort
    */
   public Server(int aPort)
   {
      _port = aPort;
   }
   
   /**
    * Initializes the server and starts listening for clients
    */
   public void start()
   {
      _keepServing = true;
      
      // create server socket and wait for connection requests
      try
      {
         ServerSocket tServerSocket = new ServerSocket(_port);

         System.out.println("Server: waiting for clients on port " + _port);
         while(_keepServing)
         {
            Socket tSocket = tServerSocket.accept();
            System.out.println("Server: accepted client on port " + _port);
            
            //was told to stop
            if(!_keepServing)
            {
               break;
            }
            
            Integer tClientID = new Integer(sNextUniqueID);
            sNextUniqueID++;
            
            ServerThread tThread = new ServerThread(tSocket, tClientID);
            synchronized (_threadMap)
            {
               _threadMap.put(tClientID, tThread);
            }
            tThread.start();
         }
         
         //was told to stop
         try
         {
            tServerSocket.close();
            
            synchronized (_threadMap)
            {
               for(ServerThread tThread : _threadMap.values())
               {
                  try
                  {
                     tThread._inStream.close();
                     tThread._outStream.close();
                     tThread._socket.close();
                  }
                  catch(IOException aEx)
                  {
                     System.err.println("Server: Exception closing client thread: " + aEx);
                  }
               }
            }
         }
         catch(Exception aEx)
         {
            System.err.println("Server: Exception closing the server and clients: " + aEx);
         }
      }
      catch(IOException aEx)
      {
         System.err.println("Server: Exception on new ServerSocket: " + aEx);
      }
   }
   
   /**
    * main
    * @param args
    * @throws IOException
    */
   public static void main(String[] args) throws IOException
   {
      int tPortNumber = Constants.kPORT_NUMBER;
      
      CardDB.loadDefaultCards();
      
      Server tServer = new Server(tPortNumber);
      tServer.start();
   }
   
   /**
    * Remove a client who has logged off.
    * @param aID - client ID
    */
   private void removeThread(Integer aID)
   {
      synchronized (_threadMap)
      {
         ServerThread tThread = _threadMap.get(aID);
         
         if(null != tThread)
         {
            _userNameMap.remove(tThread._userName);
            _threadMap.remove(aID);
         }
      }
   }
   
   /**
    * In case something outside this class needs to stop the server
    */
   protected void stop()
   {
      _keepServing = false;
      
      // connect to myself as Client to exit while loop
      try
      {
         new Socket("localhost", _port);
      }
      catch(Exception aEx)
      {
         System.err.println("Server: exception trying to stop the server: " + aEx);
      }
   }
   
   /**
    * Returns true if the login request is allowed.
    * @param aUserName
    * @param aPassword
    * @return boolean
    */
   private boolean authenticateLogin(String aUserName, String aPassword)
   {
      boolean tReturn = false;
      
      if(Constants.kALLOW_ALL_USERS)
      {
         tReturn = true;
      }
      else if("zakaviji".equals(aUserName))
      {
         tReturn = true;
      }
      else
      {
         System.out.println("Server: unrecognized user: " + aUserName);
      }
      
      return tReturn;
   }
   
   /**
    * Sent list of all users to all clients
    */
   private void broadcastUserList()
   {
      Message tMsg = new Message(Type.eUSER_LIST);
      
      synchronized (_threadMap)
      {
         tMsg.userList = new ArrayList<String>(_userNameMap.keySet());
      }
      
      broadcastMessage(tMsg);
   }
   
   /**
    * Broadcast a message to all clients who are logged in
    */
   private void broadcastMessage(Message aMsg)
   {
      synchronized (_threadMap)
      {
         for(Map.Entry<Integer, ServerThread> tEntry : _threadMap.entrySet())
         {
            ServerThread tThread = tEntry.getValue();
            
            //only broadcast to clients who are logged in
            if(ClientState.eLOGIN != tThread._state)
            {
               if(!tThread.sendMessage(aMsg))
               {
                  _userNameMap.remove(tThread._userName);
                  _threadMap.remove(tEntry.getKey());
                  tThread.close();
                  System.out.println("Server: disconnected client " + tThread._userName);
               }
            }
         }
      }
   }
   
   /**
    * Ends the game between the two given clients
    * @param aClientA
    * @param aClientB
    */
   private void endGame(Integer aClientA, Integer aClientB)
   {
      if(null != aClientA || null != aClientB)
      {
         ServerThread tThreadA = null, tThreadB = null;
         
         synchronized (_threadMap)
         {
            tThreadA = _threadMap.get(aClientA);
            tThreadB = _threadMap.get(aClientB);
         }
  
         Message tGameQuitMsg = new Message(Type.eGAME_QUIT);
         
         if(null != tThreadA)
         {
            tThreadA._state = ClientState.eDESKTOP;
            tThreadA._opponentID = null;
            sendMessageToClient(aClientA, tGameQuitMsg);
         }
         
         if(null != tThreadB)
         {
            tThreadB._state = ClientState.eDESKTOP;
            tThreadB._opponentID = null;
            sendMessageToClient(aClientB, tGameQuitMsg);
         }
      }
   }
   
   /**
    * Constructs and returns a default deck for testing purposes.
    * @return Deck
    */
   private Deck getDefaultDeck()
   {
      List<Card> tList = new ArrayList<Card>();
      tList.add(CardDB.getCard(1));
      tList.add(CardDB.getCard(1));
      tList.add(CardDB.getCard(1));
      tList.add(CardDB.getCard(2));
      tList.add(CardDB.getCard(2));
      tList.add(CardDB.getCard(2));
      tList.add(CardDB.getCard(3));
      tList.add(CardDB.getCard(3));
      tList.add(CardDB.getCard(3));
      tList.add(CardDB.getCard(4));
      tList.add(CardDB.getCard(4));
      tList.add(CardDB.getCard(4));
      tList.add(CardDB.getCard(5));
      tList.add(CardDB.getCard(5));
      tList.add(CardDB.getCard(5));
      tList.add(CardDB.getCard(6));
      tList.add(CardDB.getCard(6));
      tList.add(CardDB.getCard(6));
      
      return new Deck(tList);
   }
   
   /**
    * Sends the given message to the client with the given ID, if any.
    * @param aClientID
    * @param aMsg
    */
   private void sendMessageToClient(Integer aClientID, Message aMsg)
   {
      if(null == aMsg ||
         null == aClientID)
      {
         System.err.println("Server: sendMessageToClient: given client id or msg was null");
         return;
      }
      
      synchronized (_threadMap)
      {
         ServerThread tThread = _threadMap.get(aClientID);
         
         if(null != tThread)
         {
            if(!tThread.sendMessage(aMsg))
            {
               _userNameMap.remove(tThread._userName);
               _threadMap.remove(aClientID);
               tThread.close();
               System.out.println("Server: disconnected client " + tThread._userName);
            }
         }
      }
   }
   
   /**
    * Sends the given message to the client with the given username, if any.
    * @param aUserName
    * @param aMsg
    */
   private void sendMessageToUser(String aUserName, Message aMsg)
   {
      Integer tClientID = null;
      synchronized (_threadMap)
      {
         tClientID = _userNameMap.get(aUserName);
      }
      
      if(null != tClientID)
      {
         sendMessageToClient(tClientID, aMsg);
      }
   }
   
   /**
    * Starts a new game between user A and user B.
    * @param aUserA
    * @param aUserB
    */
   private void startGame(String aUserA, String aUserB)
   {
      Integer tClientA = null, tClientB = null;
      
      synchronized (_threadMap)
      {
         tClientA = _userNameMap.get(aUserA);
         tClientB = _userNameMap.get(aUserB);
      }
      
      if(null != tClientA || null != tClientB)
      {
         ServerThread tThreadA = null, tThreadB = null;
         
         synchronized (_threadMap)
         {
            tThreadA = _threadMap.get(tClientA);
            tThreadB = _threadMap.get(tClientB);
         }
  
         if(null == tThreadA || ClientState.eDESKTOP != tThreadA._state ||
            null == tThreadB || ClientState.eDESKTOP != tThreadB._state)
         {
            System.err.println("Server:startGame: unable to start game");
            return;
         }
         
         tThreadA._state = ClientState.eGAME;
         tThreadA._opponentID = tClientB;
         
         tThreadB._state = ClientState.eGAME;
         tThreadB._opponentID = tClientA;
         
         Message tGameStartMsg = new Message(Type.eGAME_START);
         tGameStartMsg.gameData = new GameData(
                  new Conjurer(aUserA, Conjurer.kPLAYER_A, getDefaultDeck()),
                  new Conjurer(aUserB, Conjurer.kPLAYER_B, getDefaultDeck()));
         
         sendMessageToClient(tClientA, tGameStartMsg);
         sendMessageToClient(tClientB, tGameStartMsg);
      }
   }
   
   /**
    * One instance of this thread will run for each client
    */
   private class ServerThread extends Thread
   {
      /** the socket where to listen/talk */
      private final Socket _socket;
      private ObjectInputStream _inStream;
      private ObjectOutputStream _outStream;
      
      /** unique ID for this thread */
      private final Integer _clientID;
      
      /** ID for our opponent (when in game) */
      private Integer _opponentID = null;
      
      /** user name of the client */
      private String _userName;
      
      /** state of the client */
      private ClientState _state;

      /**
       * Constructor
       * @param aSocket
       */
      public ServerThread(Socket aSocket, Integer aClientID)
      {
         _userName = "<unknown>";
         _clientID = aClientID;
         _state = ClientState.eLOGIN;
         
         this._socket = aSocket;
         
         try
         {
            _outStream = new ObjectOutputStream(aSocket.getOutputStream());
            _inStream = new ObjectInputStream(aSocket.getInputStream());
         }
         catch(IOException aEx)
         {
            System.err.println("ServerThread: Exception creating new Input/output Streams: " + aEx);
            return;
         }
      }

      /**
       * main loop for this thread
       */
      @Override
      public void run()
      {
         while(true)
         {
            Message tMsg = null;
            
            try
            {
               tMsg = (Message)_inStream.readObject();
            }
            catch(IOException aEx)
            {
               System.err.println("ServerThread: Exception reading from stream: " + aEx);
               break;
            }
            catch(ClassNotFoundException aEx)
            {
               System.err.println("ServerThread: Exception reading object from stream: " + aEx);
               break;
            }
            
            //if logout, break out of while loop
            if(Type.eLOGOUT == tMsg.type)
            {
               System.out.println("ServerThread: user " + _userName + " has logged out");
               
               if(ClientState.eGAME == _state)
               {
                  endGame(_clientID, _opponentID);
               }
               break;
            }
            
            processMessage(tMsg);
         }//end while
         
         //we have disconnected, so remove this thread from map and close
         removeThread(_clientID);
         close();
         
         broadcastUserList();
      }

      /**
       * Cleanup and close everything
       */
      private void close()
      {
         try
         {
            if(_outStream != null) _outStream.close();
         }
         catch(Exception aEx)
         {
            System.err.println("ServerThread: exception while trying to close output stream: " + aEx);
         }
         
         try
         {
            if(_inStream != null) _inStream.close();
         }
         catch(Exception aEx)
         {
            System.err.println("ServerThread: exception while trying to close input stream: " + aEx);
         }

         try
         {
            if(_socket != null) _socket.close();
         }
         catch(Exception aEx)
         {
            System.err.println("ServerThread: exception while trying to close socket: " + aEx);
         }
      }
      
      /**
       * Process a message from the client
       * @param aMsg
       */
      private void processMessage(Message aMsg)
      {
         if(null != aMsg)
         {
            System.out.println("ServerThread: received message: " + aMsg.toString());
            
            switch(_state)
            {
               case eLOGIN:
               {
                  if(Type.eLOGIN_REQUEST == aMsg.type)
                  {
                     boolean tSendUserList = false;
                     
                     Message tReplyMsg;
                     if(authenticateLogin(aMsg.userName, aMsg.password))
                     {
                        _userName = aMsg.userName;
                        _state = ClientState.eDESKTOP;
                        tSendUserList = true;
                        
                        synchronized (_threadMap)
                        {
                           _userNameMap.put(_userName, _clientID);
                        }

                        tReplyMsg = new Message(Type.eLOGIN_ACCEPTED);
                        System.out.println("ServerThread: user " + _userName + " logged in successfully");
                     }
                     else
                     {
                        tReplyMsg = new Message(Type.eLOGIN_REJECTED);
                        System.out.println("ServerThread: login was rejected");
                     }
                     
                     sendMessage(tReplyMsg);
                     
                     if(tSendUserList)
                     {
                        broadcastUserList();
                     }
                  }
                  else
                  {
                     System.err.println("ServerThread: error: received message type " + 
                        aMsg.type + " while in state " + _state);
                  }
                  break;
               }
               case eDESKTOP:
               {
                  if(Type.eDESKTOP_CHAT == aMsg.type)
                  {
                     broadcastMessage(aMsg);
                  }
                  //forward game request to appropriate client
                  else if(Type.eGAME_REQUEST == aMsg.type)
                  {
                     String tRecipient = aMsg.opponent;
                     
                     Message tRequestMsg = new Message(Type.eGAME_REQUEST);
                     tRequestMsg.opponent = aMsg.userName;
                     
                     sendMessageToUser(tRecipient, tRequestMsg);
                  }
                  else if(Type.eGAME_ACCEPT == aMsg.type)
                  {
                     startGame(_userName, aMsg.opponent);
                  }
                  else
                  {
                     System.err.println("ServerThread: error: received message type " + 
                        aMsg.type + " while in state " + _state);
                  }
                  break;
               }
               case eGAME:
               {
                  if(Type.eGAME_CHAT == aMsg.type)
                  {
                     sendMessage(aMsg);//echo back to self
                     sendMessageToClient(_opponentID, aMsg);//send to opponent
                  }
                  else if(Type.eGAME_DATA == aMsg.type)
                  {
                     //sendMessage(aMsg);//echo back to self
                     sendMessageToClient(_opponentID, aMsg);//send to opponent
                  }
                  else if(Type.eGAME_QUIT == aMsg.type)
                  {
                     endGame(_clientID, _opponentID);
                  }
                  else
                  {
                     System.err.println("ServerThread: error: received message type " + 
                        aMsg.type + " while in state " + _state);
                  }
                  break;
               }
               default:
               {
                  System.err.println("ServerThread: warning: state " + _state + "is not handled");
                  break;
               }
            }
         }
      }

      /**
       * Send message to the client
       */
      private boolean sendMessage(Message aMsg)
      {
         if(!_socket.isConnected())
         {
            close();
            return false;
         }
         
         aMsg.clientID = _clientID.intValue();
         aMsg.userName = _userName;
         
         System.out.println("ServerThread: sending message: " + aMsg);
         
         try
         {
            _outStream.writeObject(aMsg);
         }
         catch(IOException aEx)
         {
            System.err.println("ServerThread: Error sending message to " + _userName + ": " + aEx);
         }
         
         return true;
      }
   }
}
