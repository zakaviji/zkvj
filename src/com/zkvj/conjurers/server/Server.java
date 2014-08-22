package com.zkvj.conjurers.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.zkvj.conjurers.core.ClientState;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;

/**
 * Server process for Conjurers
 */
public class Server
{
   /** keep track of the next available client ID */
   private static int sNextUniqueID = 0;
   
   /** the port number */
   private int _port;
   
   /** while true, keep listening for clients */
   private boolean _keepServing;
   
   /** list of client threads */
   private ArrayList<ServerThread> _threads;
   
   /**
    * Constructor
    * @param aPort
    */
   public Server(int aPort)
   {
      _port = aPort;
      _threads = new ArrayList<ServerThread>();
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
            
            ServerThread tThread = new ServerThread(tSocket);
            _threads.add(tThread);
            tThread.start();
         }
         
         //was told to stop
         try
         {
            tServerSocket.close();
            
            for(ServerThread tThread : _threads)
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
      
      Server tServer = new Server(tPortNumber);
      tServer.start();
   }
   
   /**
    * Remove a client who has logged off.
    * @param aID - client ID
    */
   synchronized private void remove(int aID)
   {
      for(ServerThread tThread : _threads)
      {
         if(tThread._clientID == aID)
         {
            _threads.remove(tThread);
            return;
         }
      }
   }
   
   /**
    * In case GUI needs to stop the server
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
    * Broadcast a message to all clients
    */
   private synchronized void broadcastMessage(Message aMsg)
   {
      // loop in reverse order in case we have to remove a client
      for(int i = _threads.size(); --i >= 0;)
      {
         ServerThread tThread = _threads.get(i);
         
         // if cannot send to client, remove it from the list
         if(!tThread.sendMessage(aMsg))
         {
            _threads.remove(i);
            System.out.println("Server: disconnected client "
                  + tThread._userName);
         }
      }
   }
   
   /**
    * One instance of this thread will run for each client
    */
   class ServerThread extends Thread
   {
      /** the socket where to listen/talk */
      Socket _socket;
      ObjectInputStream _inStream;
      ObjectOutputStream _outStream;
      
      /** unique ID for this thread */
      int _clientID;
      
      /** user name of the client */
      String _userName;
      
      /** state of the client */
      ClientState _state;

      /**
       * Constructor
       * @param aSocket
       */
      ServerThread(Socket aSocket)
      {
         _userName = "<unknown>";
         _clientID = ++sNextUniqueID;
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
            
            if(null != tMsg)
            {
               System.out.println("ServerThread: received message: " + tMsg.toString());
               
               //handle logout message regardless of client state
               if(Type.eLOGOUT == tMsg._type)
               {
                  System.out.println("ServerThread: user " + _userName + " has logged out");
                  break;
               }
               
               switch(_state)
               {
                  case eLOGIN:
                  {
                     if(Type.eLOGIN_REQUEST == tMsg._type)
                     {
                        Message tReply = new Message();
                        if(authenticateLogin(tMsg._userName, tMsg._password))
                        {
                           _userName = tMsg._userName;
                           _state = ClientState.eDESKTOP;
                           
                           tReply._type = Type.eLOGIN_ACCEPTED;
                           System.out.println("ServerThread: user " + _userName + " logged in successfully");
                        }
                        else
                        {
                           tReply._type = Type.eLOGIN_REJECTED;
                           System.out.println("ServerThread: login was rejected");
                        }
                        sendMessage(tReply);
                     }
                     else
                     {
                        System.err.println("ServerThread: error: received message type " + 
                           tMsg._type + " while in state " + _state);
                     }
                     break;
                  }
                  case eDESKTOP:
                  {
                     if(Type.eDESKTOP_CHAT == tMsg._type)
                     {
                        broadcastMessage(tMsg);
                     }
                     else
                     {
                        System.err.println("ServerThread: error: received message type " + 
                           tMsg._type + " while in state " + _state);
                     }
                     break;
                  }
                  case eGAME:
                  {
                     break;
                  }
                  default:
                  {
                     System.err.println("ServerThread: warning: state " + _state + "is not handled");
                     break;
                  }
               }
            }
         }//end while
         
         //we have disconnected, so remove this thread from list and close
         remove(_clientID);
         close();
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
       * Send message to the client
       */
      private boolean sendMessage(Message aMsg)
      {
         if(!_socket.isConnected())
         {
            close();
            return false;
         }
         
         aMsg._clientID = _clientID;
         aMsg._userName = _userName;
         
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
