package com.zkvj.conjurers.client.game;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.client.Client.ClientMessageHandler;
import com.zkvj.conjurers.client.desktop.ChatHistoryPanel;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameModel;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;

public class GamePanel extends JLayeredPane
{
   private static final long serialVersionUID = 3916891242722466815L;
   
   private final Client _client;
   private final GameModel _model;

   /** display components */
   private BoardPanel _boardPanel;
   private CardDetailsArea _cardDetailsArea;
   private HandDisplayArea _handDisplayArea;
   private PlayerDetailsArea _playerArea;
   private PlayerDetailsArea _oppArea;
   private ChatHistoryPanel _chatHistoryPanel;
   
   /** listener for messages received by this client */
   private final ClientMessageHandler _messageHandler = new ClientMessageHandler()
   {
      @Override
      public void handleMessage(Message aMsg)
      {
         if(Type.eGAME_DATA == aMsg.type)
         {
            //updates the game data and triggers components to refresh
            _model.setGameData(aMsg.gameData);
         }
      }
   };
   
   /**
    * Constructor
    * @param aData - the game data
    */
   public GamePanel(Client aClient, GameModel aModel)
   {
      _client = aClient;
      _model = aModel;
      
      _client.addMessageHandler(_messageHandler);
      
      this.setPreferredSize(new Dimension(Constants.kWIDTH, Constants.kHEIGHT));
      this.setFocusable(true);
      
      initComponents();
      
      _boardPanel.updateBufferedImage();
   }
   
   private void initComponents()
   {
      int tPlayerID = Conjurer.kPLAYER_B;
      int tOpponentID = Conjurer.kPLAYER_A;
      
      //determine which player we are
      if(_model.getGameData().getPlayer(Conjurer.kPLAYER_A).getName().
               equals(_client.getUserName()))
      {
         tPlayerID = Conjurer.kPLAYER_A;
         tOpponentID = Conjurer.kPLAYER_B;
      }
      
      //using absolute positioning, proportional to width/height
      setLayout(null);
      
      int tWidth = getPreferredSize().width;
      int tHeight = getPreferredSize().height;
      
      //background layer
      JPanel tBackground = new JPanel();
      tBackground.setLocation(0,0);
      tBackground.setSize(getPreferredSize());
      tBackground.setBackground(Constants.kBACKGROUND_COLOR);
      add(tBackground, Constants.kBACKGROUND_LAYER);
      
      //board component is a square, with full height of the game panel
      _boardPanel = new BoardPanel(_model);
      _boardPanel.setLocation(new Point(tWidth/2 - tHeight/2, 0));
      _boardPanel.setSize(new Dimension(tHeight, tHeight));
      _boardPanel.setBufferedImageSize(_boardPanel.getSize());
      add(_boardPanel, Constants.kBOARD_LAYER);
      
      //card details area
      _cardDetailsArea = new CardDetailsArea();
      _cardDetailsArea.setLocation(new Point(tWidth - (tHeight * 1/3), tHeight * 1/4));
      _cardDetailsArea.setSize(new Dimension(tHeight * 1/3, tHeight * 1/2));
      _cardDetailsArea.setBufferedImageSize(_cardDetailsArea.getSize());
      add(_cardDetailsArea, Constants.kUI_LAYER);
      
      //chat/history area
      _chatHistoryPanel = new ChatHistoryPanel(_client, false);
      _chatHistoryPanel.setLocation(new Point(0,0));
      _chatHistoryPanel.setSize(new Dimension(tWidth * 1/5, tHeight * 3/4));
      add(_chatHistoryPanel, Constants.kUI_LAYER);
      
      //hand display area
      _handDisplayArea = new HandDisplayArea(_client, _model, tPlayerID);
      _handDisplayArea.setLocation(new Point(tWidth  * 5/7, tHeight * 3/4));
      _handDisplayArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      add(_handDisplayArea, Constants.kUI_LAYER);
      
      //player area
      _playerArea = new PlayerDetailsArea(_client, _model, tPlayerID);
      _playerArea.setLocation(new Point(0, tHeight * 3/4));
      _playerArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      add(_playerArea, Constants.kUI_LAYER);
      
      //opponent area
      _oppArea = new PlayerDetailsArea(_client, _model, tOpponentID);
      _oppArea.setLocation(new Point(tWidth  * 5/7, 0));
      _oppArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      add(_oppArea, Constants.kUI_LAYER);
   }
}
