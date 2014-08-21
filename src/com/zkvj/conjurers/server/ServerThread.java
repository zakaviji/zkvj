package com.zkvj.conjurers.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread
{
   private Socket _socket = null;

   public ServerThread(Socket aSocket)
   {
      super("ServerThread");
      this._socket = aSocket;
   }

   public void run()
   {
      try(PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
          BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));)
      {
//         String inputLine, outputLine;
//         KnockKnockProtocol kkp = new KnockKnockProtocol();
//         outputLine = kkp.processInput(null);
//         out.println(outputLine);
//
//         while((inputLine = in.readLine()) != null)
//         {
//            outputLine = kkp.processInput(inputLine);
//            out.println(outputLine);
//            if(outputLine.equals("Bye"))
//               break;
//         }
         _socket.close();
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
