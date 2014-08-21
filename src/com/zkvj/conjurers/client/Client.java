package com.zkvj.conjurers.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;

public class Client
{
   /** for I/O */
   private ObjectInputStream _inStream;      // to read from the socket
   private ObjectOutputStream _outStream;    // to write on the socket
   private Socket _socket;
   
   /** the host, the port */
   private String _hostName;
   private int _port;
   
   /** handle to launcher to pass back events */
   private final Launcher _launcher;
   
   /** thread on which we listen to server */
   private ClientThread _thread;
   
   /**
    * Constructor
    * @param aHostName
    * @param aPort
    */
   public Client(String aHostName, int aPort, Launcher aLauncher)
   {
      _hostName = aHostName;
      _port = aPort;
      _launcher = aLauncher;
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
      
      //send a logout message
      Message tLogout = new Message();
      tLogout._type = Type.eLOGOUT;
      sendMessage(tLogout);
      
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
    * To send a message to the server
    */
   public void sendMessage(Message aMsg)
   {
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
    * Thread which processes messages from the server
    */
   class ClientThread extends Thread
   {
      boolean _keepListening = true;
      
      @Override
      public void run()
      {
         while(_keepListening)
         {
            Message tMessage = null;
            
            try
            {
               tMessage = (Message)_inStream.readObject();
            }
            catch(IOException aEx)
            {
               System.err.println("ClientThread: lost connection to server: " + aEx);
            }
            catch(ClassNotFoundException aEx)
            {
               System.err.println("ClientThread: unable to process message from server: " + aEx);
            }
            
            if(null != tMessage)
            {
               System.out.println("ClientThread: received message: " + tMessage._type);
               
               if(Type.eLOGIN_ACCEPTED == tMessage._type)
               {
                  _launcher.loginSuccessful();
               }
            }
         }
      }
   }
}
