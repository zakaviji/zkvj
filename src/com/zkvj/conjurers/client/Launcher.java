package com.zkvj.conjurers.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.zkvj.conjurers.client.Client.ClientMessageHandler;
import com.zkvj.conjurers.client.desktop.DesktopPanel;
import com.zkvj.conjurers.client.game.GamePanel;
import com.zkvj.conjurers.core.ClientState;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Deck;
import com.zkvj.conjurers.core.GameData;
import com.zkvj.conjurers.core.Message;

public class Launcher extends JFrame
{
   private static final long serialVersionUID = 1337850054206041167L;

   /** the various content panels */
   private LoginPanel _loginPanel;
   private DesktopPanel _desktopPanel;
   private GamePanel _gamePanel;
   
   /** the client object */
   private Client _client;
   
   /** handle messages received by this client */
   private final ClientMessageHandler _messageHandler = new ClientMessageHandler()
   {
      @Override
      public void handleMessage(Message aMsg)
      {  
         switch(_client.getState())
         {
            case eLOGIN:
            {
               if(Message.Type.eLOGIN_ACCEPTED == aMsg.type)
               {
                  showDesktop();
//                  startGame(); //for testing purposes only
               }
               break;
            }
            case eDESKTOP:
            {
               if(Message.Type.eGAME_REQUEST == aMsg.type)
               {
                  showInviteDialog(aMsg.opponent);
               }
               else if(Message.Type.eGAME_START == aMsg.type)
               {
                  startGame(aMsg.userName, aMsg.opponent);
               }
               break;
            }
            default:
               break;
         }
      }
   };
   
   /** handle window close events so we can clean up and logout */
   private final WindowAdapter _windowListener = new WindowAdapter()
   {
      @Override
      public void windowClosing(WindowEvent aEvent)
      {
         System.out.println("Launcher: detected window close event; cleaning up and exiting");
         _client.disconnect();
         aEvent.getWindow().dispose();
      }
   };
   
   /**
    * Constructor
    */
   public Launcher()
   {
      super("Conjurers");
      
      _client = new Client(Constants.kHOST_NAME, Constants.kPORT_NUMBER);
      _client.addMessageHandler(_messageHandler);
      
      if(!_client.start())
      {
         System.err.println("Launcher: failed to connect to server, exiting");
         System.exit(1);
      }
      
      _loginPanel = new LoginPanel(_client);
      _desktopPanel = new DesktopPanel(_client);
      
      setContentPane(_loginPanel);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      addWindowListener(_windowListener);
   }

   /**
    * main
    */
   public static void main(String[] args)
   {
      Launcher tLauncher = new Launcher();
      tLauncher.pack();
      tLauncher.setVisible(true);
   }

   /**
    * Called to switch to desktop view
    */
   protected void showDesktop()
   {
      _desktopPanel.reset();
      
      _client.setState(ClientState.eDESKTOP);
      
      this.setContentPane(_desktopPanel);
      this.pack();
   }
   
   /**
    * Shows a dialog to ask if you want to accept a game invitation
    * @param aOpponent - the opponent who sent the invite
    */
   protected void showInviteDialog(String aOpponent)
   {
      Object[] tOptions = {"Accept","Ignore"};
      int tResponse = JOptionPane.showOptionDialog(this,
            aOpponent + " has invited you to play a game",
            "Game Invite",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            tOptions,
            tOptions[1]);
      
      if(tResponse == JOptionPane.OK_OPTION)
      {
         Message tAcceptMsg = new Message(Message.Type.eGAME_ACCEPT);
         tAcceptMsg.opponent = aOpponent;
         _client.sendMessage(tAcceptMsg);
      }
   }

   /**
    * Start a new game and switch to game view
    */
   protected void startGame(String aPlayer, String aOpponent)
   {
      Conjurer tPlayer = new Conjurer(aPlayer, new Deck());
      Conjurer tOpponent = new Conjurer(aOpponent, new Deck());
      
      _gamePanel = new GamePanel(_client, new GameData(tPlayer, tOpponent));
      
      _client.setState(ClientState.eGAME);
      
      this.setContentPane(_gamePanel);
      this.pack();
   }
}
