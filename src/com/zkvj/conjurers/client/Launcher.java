package com.zkvj.conjurers.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Game;

public class Launcher extends JFrame
{
   private static final long serialVersionUID = 1337850054206041167L;

   private final LoginPanel _loginPanel;
   
   private Client _client = null;
   private Game _game = null;
   
   /**
    * Constructor
    */
   public Launcher()
   {
      super("Conjurers");
      
      _client = new Client(Constants.kHOST_NAME, Constants.kPORT_NUMBER, this);
      
      if(!_client.start())
      {
         System.err.println("Launcher: failed to connect to server, exiting");
         System.exit(1);
      }
      
      _loginPanel = new LoginPanel(_client);
      
      setContentPane(getLoginPanel());
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      addWindowListener(new WindowAdapter()
      {
          @Override
          public void windowClosing(WindowEvent aEvent)
          {
              System.out.println("Launcher: detected window close event; cleaning up and exiting");
              _client.disconnect();
              aEvent.getWindow().dispose();
          }
      });
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
    * @return the loginPanel
    */
   public LoginPanel getLoginPanel()
   {
      return _loginPanel;
   }
   
   /**
    * Called once our login is accepted by the server
    */
   protected void loginSuccessful()
   {
      _game = new Game();
      this.setContentPane(_game.getGamePanel());
      this.pack();
   }
}
