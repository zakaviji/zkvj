package com.zkvj.conjurers.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.zkvj.conjurers.client.desktop.DesktopPanel;
import com.zkvj.conjurers.client.game.GamePanel;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameData;

public class Launcher extends JFrame
{
   private static final long serialVersionUID = 1337850054206041167L;

   /** the various content panels */
   private LoginPanel _loginPanel;
   private DesktopPanel _desktopPanel;
   private GamePanel _gamePanel;
   
   /** the client object */
   private Client _client;
   
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
      
      _client = new Client(Constants.kHOST_NAME, Constants.kPORT_NUMBER, this);
      
      if(!_client.start())
      {
         System.err.println("Launcher: failed to connect to server, exiting");
         System.exit(1);
      }
      
      _loginPanel = new LoginPanel(_client);
      
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
      if(null == _desktopPanel)
      {
         _desktopPanel = new DesktopPanel(_client);
      }
      
      this.setContentPane(_desktopPanel);
      this.pack();
   }

   /**
    * Start a new game and switch to game view
    */
   protected void startGame()
   {
      _gamePanel = new GamePanel(_client, new GameData());
      
      this.setContentPane(_gamePanel);
      this.pack();
   }
}
