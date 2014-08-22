package com.zkvj.conjurers.client.game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.client.desktop.ChatHistoryPanel;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameData;
import com.zkvj.utils.BufferedImageComponent;

public class GamePanel extends JLayeredPane
{
   private static final long serialVersionUID = 3916891242722466815L;
   
   private Client _client;
   
   /** game data */
   private final GameData _data;

   /** display components */
   private BoardPanel _boardPanel = null;
   private CardDetailsArea _cardDetailsArea = null;
   private HistoryArea _historyArea = null;
   private HandDisplayArea _handDisplayArea = null;
   private PlayerDetailsArea _playerArea = null;
   private PlayerDetailsArea _oppArea = null;
   
   private ChatHistoryPanel _chatHistoryPanel;
   
   /** mouse listener */
   private final MouseListener _mouseListener = new MouseListener()
   {
      @Override
      public void mouseClicked(MouseEvent aEvent)
      {
         updateDisplayComponents();
         repaint();
      }

      @Override
      public void mousePressed(MouseEvent aEvent) {}

      @Override
      public void mouseReleased(MouseEvent aEvent) {}

      @Override
      public void mouseEntered(MouseEvent aEvent) {}

      @Override
      public void mouseExited(MouseEvent aEvent) {}
   };

   /** Mouse listener */
   private final MouseMotionListener _mouseMotionListener =
      new MouseMotionListener()
   {
      @Override
      public void mouseDragged(MouseEvent aEvent){}

      @Override
      public void mouseMoved(MouseEvent aEvent){}
   };

   /** Key listener */
   private final KeyListener _keyListener = new KeyListener()
   {
      @Override
      public void keyPressed(KeyEvent aEvent)
      {
         switch(aEvent.getKeyCode())
         {
            case KeyEvent.VK_R:
               _data.getBoard().randomizeBoard();
               break;
            default:
               break;
         }

         updateDisplayComponents();
         repaint();
      }

      @Override
      public void keyTyped(KeyEvent aEvent){}

      @Override
      public void keyReleased(KeyEvent aEvent){}
   };
   
   /**
    * Constructor
    * @param aData - the game data
    */
   public GamePanel(Client aClient, GameData aData)
   {
      _client = aClient;
      _data = aData;
      
      this.setPreferredSize(new Dimension(Constants.kWIDTH, Constants.kHEIGHT));
      this.setFocusable(true);
      this.addMouseListener(_mouseListener);
      this.addMouseMotionListener(_mouseMotionListener);
      this.addKeyListener(_keyListener);
      
      initComponents();
      
      updateDisplayComponents();
   }
   
   private void initComponents()
   {
      setLayout(null);
      
      int tWidth = getPreferredSize().width;
      int tHeight = getPreferredSize().height;
      
      //background layer
      JPanel tBackground = new JPanel();
      tBackground.setLocation(0,0);
      tBackground.setSize(getPreferredSize());
      tBackground.setBackground(Constants.kBACKGROUND_COLOR);
      add(tBackground, Constants.kBACKGROUND_LAYER);
      
      //board component is a square, which takes up the full height of the game panel
      _boardPanel = new BoardPanel(_data);
      _boardPanel.setLocation(new Point(tWidth/2 - tHeight/2, 0));
      _boardPanel.setSize(new Dimension(tHeight, tHeight));
      _boardPanel.setBufferedImageSize(_boardPanel.getSize());
      add(_boardPanel, Constants.kBOARD_LAYER);
      
      //card details area
      _cardDetailsArea = new CardDetailsArea(_data);
      _cardDetailsArea.setLocation(new Point(0, tHeight * 1/4));
      _cardDetailsArea.setSize(new Dimension(tHeight * 1/2 * 2/3, tHeight * 1/2));
      _cardDetailsArea.setBufferedImageSize(_cardDetailsArea.getSize());
      add(_cardDetailsArea, Constants.kINFO_LAYER);
      
      //history area
//      _historyArea = new HistoryArea(_data);
//      _historyArea.setLocation(new Point(tWidth  * 4/5, 0));
//      _historyArea.setSize(new Dimension(tWidth * 1/5, tHeight * 3/4));
//      _historyArea.setBufferedImageSize(_historyArea.getSize());
//      add(_historyArea, Constants.kINFO_LAYER);
      
      _chatHistoryPanel = new ChatHistoryPanel(_client, false);
      _chatHistoryPanel.setLocation(new Point(tWidth  * 4/5, 0));
      _chatHistoryPanel.setSize(new Dimension(tWidth * 1/5, tHeight * 3/4));
      add(_chatHistoryPanel, Constants.kINFO_LAYER);
      
      //hand display area
      _handDisplayArea = new HandDisplayArea(_data);
      _handDisplayArea.setLocation(new Point(tWidth  * 5/7, tHeight * 3/4));
      _handDisplayArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      _handDisplayArea.setBufferedImageSize(_handDisplayArea.getSize());
      add(_handDisplayArea, Constants.kINFO_LAYER);
      
      //player area
      _playerArea = new PlayerDetailsArea(_data, true);
      _playerArea.setLocation(new Point(0, tHeight * 3/4));
      _playerArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      _playerArea.setBufferedImageSize(_playerArea.getSize());
      add(_playerArea, Constants.kINFO_LAYER);
      
      //opponent area
      _oppArea = new PlayerDetailsArea(_data, false);
      _oppArea.setLocation(new Point(0,0));
      _oppArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      _oppArea.setBufferedImageSize(_oppArea.getSize());
      add(_oppArea, Constants.kINFO_LAYER);
   }
   
   /**
    * Calls updateBufferedImage on all components.
    */
   private void updateDisplayComponents()
   {
      for(Component tComp : getComponents())
      {
         if(BufferedImageComponent.class.isInstance(tComp))
         {
            ((BufferedImageComponent)tComp).updateBufferedImage();
         }
      }
   }
}
