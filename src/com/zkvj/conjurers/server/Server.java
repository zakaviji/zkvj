package com.zkvj.conjurers.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.zkvj.conjurers.core.Constants;

public class Server
{
   public static void main(String[] args) throws IOException
   {
      int tPortNumber = Constants.kPORT_NUMBER;
      boolean tListening = true;

      System.out.println("ConjurersServer: listening on port " + tPortNumber);
      try(ServerSocket serverSocket = new ServerSocket(tPortNumber))
      {
         while(tListening)
         {
            new ServerThread(serverSocket.accept()).start();
            System.out.println("ConjurersServer: spawned a new thread");
         }
      }
      catch(IOException aEx)
      {
         System.err.println("Could not listen on port " + tPortNumber);
         System.exit(-1);
      }
   }
}
