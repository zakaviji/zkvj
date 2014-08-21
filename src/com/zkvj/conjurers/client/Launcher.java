package com.zkvj.conjurers.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.zkvj.conjurers.core.Game;
import com.zkvj.utils.FrameUtil;

public class Launcher
{
   /** login panel, shown first */
   private final LoginPanel _loginPanel =  new LoginPanel();
   
   /** the JFrame */
   private JFrame _frame = null;
   
   /** game */
   private Game _game = null;
   
   /** Key listener */
   private final KeyListener _keyListener = new KeyListener()
   {
      @Override
      public void keyPressed(KeyEvent aEvent)
      {
         switch(aEvent.getKeyCode())
         {
            case KeyEvent.VK_ENTER:
               _game = new Game();
               _frame.setContentPane(_game.getGamePanel());
               _frame.pack();
               break;
            default:
               break;
         }
      }

      @Override
      public void keyTyped(KeyEvent aEvent){}

      @Override
      public void keyReleased(KeyEvent aEvent){}
   };
   
   /**
    * Constructor
    */
   public Launcher()
   {
      _loginPanel.addKeyListener(_keyListener);
      
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            _frame = FrameUtil.openInJFrame(getLoginPanel(), "Conjurers");
            _frame.setVisible(true);
         }
      });
   }

   /**
    * main
    */
   public static void main(String[] args)
   {
      new Launcher();
//      SwingUtilities.invokeLater(new Runnable()
//      {
//         @Override
//         public void run()
//         {
//            _frame = FrameUtil.openInJFrame((new Launcher()).getLoginPanel(),
//                                                   "Conjurers");
//            tFrame.setVisible(true);
//         }
//      });
   }

   /**
    * @return the loginPanel
    */
   public LoginPanel getLoginPanel()
   {
      return _loginPanel;
   }
}
