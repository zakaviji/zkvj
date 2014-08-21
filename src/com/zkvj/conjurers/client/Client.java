package com.zkvj.conjurers.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.zkvj.conjurers.core.Constants;

public class Client
{
   public static void main(String[] args) throws IOException
   {
      String tHostName = Constants.kHOST_NAME;
      int tPortNumber = Constants.kPORT_NUMBER;
      
      System.out.println("ConjurersClient: attempting to connect to server " + 
                         tHostName + " on port " + tPortNumber);

      try(Socket tSocket = new Socket(tHostName, tPortNumber);
          PrintWriter tOut = new PrintWriter(tSocket.getOutputStream(), true);
          BufferedReader tIn = new BufferedReader(new InputStreamReader(tSocket.getInputStream()));)
      {
         //get username/pwd and send to server
         
         
         
//         BufferedReader tStdIn = new BufferedReader(new InputStreamReader(System.in));
//         String tFromServer;
//         String tFromUser;
//
//         while((tFromServer = tIn.readLine()) != null)
//         {
//            System.out.println("Server: " + tFromServer);
//            if(tFromServer.equals("Bye."))
//               break;
//
//            tFromUser = tStdIn.readLine();
//            if(tFromUser != null)
//            {
//               System.out.println("Client: " + tFromUser);
//               tOut.println(tFromUser);
//            }
//         }
      }
      catch(UnknownHostException aEx)
      {
         System.err.println("Don't know about host " + tHostName);
         System.exit(1);
      }
      catch(IOException aEx)
      {
         System.err.println("Couldn't get I/O for the connection to "
                  + tHostName);
         System.exit(1);
      }
   }
}
