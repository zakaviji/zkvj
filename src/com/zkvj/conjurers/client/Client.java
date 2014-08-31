package com.zkvj.conjurers.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.zkvj.conjurers.core.ClientState;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;

public class Client
{
   /**
    * Listener for chat messages
    */
   public static abstract class ClientMessageHandler
   {
      public abstract void handleMessage(Message aMsg);
   }
   
   /** list of listeners for desktop chat messages */
   private final List<ClientMessageHandler> _messageHandlers = 
      new ArrayList<ClientMessageHandler>();
   
   /** for I/O */
   private ObjectInputStream _inStream;      // to read from the socket
   private ObjectOutputStream _outStream;    // to write on the socket
   private Socket _socket;
   
   /** the host, the port */
   private String _hostName;
   private int _port;
   
   /** client ID and user name */
   private int _clientID;
   private String _userName;
   
   /** thread on which we listen to server */
   private ClientThread _thread;
   
   /** current state of this client */
   private ClientState _state = ClientState.eLOGIN;
   
   /**
    * Constructor
    * @param aHostName
    * @param aPort
    */
   public Client(String aHostName, int aPort)
   {
      _hostName = aHostName;
      _port = aPort;
   }
   
   /**
    * Add listener for desktop chat messages from server
    */
   public void addMessageHandler(ClientMessageHandler aHandler)
   {
      synchronized (_messageHandlers)
      {
         _messageHandlers.add(aHandler);
      }
   }
   
   /**
    * Call all message handlers to inform them a message has been received.
    * @param aMsg
    */
   private void fireMessageReceived(Message aMsg)
   {
      //hold lock while creating temp copy of list
      List<ClientMessageHandler> tCopy;
      synchronized (_messageHandlers)
      {
         tCopy = new ArrayList<ClientMessageHandler>(_messageHandlers);
      }

      for(ClientMessageHandler tHandler : tCopy)
      {
         tHandler.handleMessage(aMsg);
      }
   }
   
   /**
    * @return the userName
    */
   public String getUserName()
   {
      return _userName;
   }
   
   /**
    * @param aUserName the userName to set
    */
   public void setUserName(String aUserName)
   {
      _userName = aUserName;
   }

   /**
    * Initialize connection with the server
    */
   public boolean start()
   {
      try
      {
         _socket = new Socket(_hostName, _port);
      }
      catch(Exception aEx)
      {
         System.err.println("Client: error connecting to server: " + aEx);
         return false;
      }

      System.out.println("Client: Connection accepted at " + _socket.getInetAddress() + ":"
            + _socket.getPort());

      try
      {
         _inStream = new ObjectInputStream(_socket.getInputStream());
         _outStream = new ObjectOutputStream(_socket.getOutputStream());
      }
      catch(IOException aEx)
      {
         System.err.println("Client: exception creating new Input/output Streams: " + aEx);
         return false;
      }
      
      _thread = new ClientThread();
      _thread.start();
      
      System.out.println("Client: ready to process messages from server");
      
      return true;
   }
   
   /**
    * Close the Input/Output streams and disconnect socket
    */
   public void disconnect()
   {
      _thread._keepListening = false;
      
      sendMessage(new Message(Type.eLOGOUT));
      
      try
      {
         if(_inStream != null) _inStream.close();
      }
      catch(Exception aEx)
      {
         System.err.println("Client: exception while trying to close input stream: " + aEx);
      }
      
      try
      {
         if(_outStream != null) _outStream.close();
      }
      catch(Exception aEx)
      {
         System.err.println("Client: exception while trying to close output stream: " + aEx);
      }
      
      try
      {
         if(_socket != null) _socket.close();
      }
      catch(Exception aEx)
      {
         System.err.println("Client: exception while trying to close socket: " + aEx);
      }
   }
   
   /**
    * Process incoming message from server
    * @param aMsg
    */
   private void processMessage(Message aMsg)
   {
      if(null != aMsg)
      {
         System.out.println("ClientThread: received message: " + aMsg.toString());
         
         if(Type.eLOGIN_ACCEPTED == aMsg.type)
         {
            //keep track of our user name and client ID
            _clientID = aMsg.clientID;
            _userName = aMsg.userName;
         }
         
         fireMessageReceived(aMsg);
      }
      else
      {
         System.err.println("Client: processMessage: given message was null");
//         Thread.dumpStack();
//         System.exit(1);
      }
   }
   
   /**
    * To send a message to the server
    */
   public void sendMessage(Message aMsg)
   {
      aMsg.clientID = _clientID;
      aMsg.userName = _userName;
      
      System.out.println("Client: sending message: " + aMsg.toString());
      
      try
      {
         _outStream.writeObject(aMsg);
      }
      catch(IOException aEx)
      {
         System.err.println("Client: exception writing to server: " + aEx);
      }
   }
   
   /**
    * @return the state
    */
   public ClientState getState()
   {
      return _state;
   }

   /**
    * @param aState the state to set
    */
   public void setState(ClientState aState)
   {
      _state = aState;
   }

   /**
    * Thread which processes messages from the server
    */
   private class ClientThread extends Thread
   {
      boolean _keepListening = true;
      
      @Override
      public void run()
      {
         while(_keepListening)
         {
            Message tMsg = null;
            
            try
            {
               tMsg = (Message)_inStream.readObject();
            }
            catch(IOException aEx)
            {
               System.err.println("ClientThread: lost connection to server: " + aEx);
            }
            catch(ClassNotFoundException aEx)
            {
               System.err.println("ClientThread: unable to process message from server: " + aEx);
            }
            
            processMessage(tMsg);
         }
      }
   }
}
