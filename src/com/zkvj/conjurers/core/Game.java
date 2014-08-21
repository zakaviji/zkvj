package com.zkvj.conjurers.core;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.zkvj.conjurers.display.GamePanel;
import com.zkvj.utils.FrameUtil;

/**
 * Java implementation of Conjurers.
 */
public class Game
{
   /** game data */
   private final GameData _data = new GameData();

   /** game display panel */
   private final GamePanel _panel =  new GamePanel(_data);
   
   /** Constructor */
   public Game()
   {
      _data.getBoard().initializeBoard();
   }

   /**
    * @return the _panel
    */
   public GamePanel getGamePanel()
   {
      return _panel;
   }

   /**
    * Main
    * @param aArgs
    */
   public static void main(String[] aArgs)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            JFrame tFrame = FrameUtil.openInJFrame((new Game()).getGamePanel(),
                                                   "Conjurers");
            tFrame.setVisible(true);
         }
      });
   }
}