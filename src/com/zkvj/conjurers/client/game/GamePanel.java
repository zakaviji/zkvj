package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.client.Client.ClientMessageHandler;
import com.zkvj.conjurers.client.desktop.ChatHistoryPanel;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameData;
import com.zkvj.conjurers.core.GameModel;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.GameModel.GameModelListener;
import com.zkvj.conjurers.core.Message.Type;

public class GamePanel extends JLayeredPane
{
   private static final long serialVersionUID = 3916891242722466815L;
   
   private final Client _client;
   private final GameModel _model;
   
   private int _playerID;
   
   private boolean _myTurn;

   /** display components */
   private BoardPanel _boardPanel;
   private CardDetailsArea _cardDetailsArea;
   private HandDisplayArea _handDisplayArea;
   private PlayerDetailsArea _playerArea;
   private PlayerDetailsArea _oppArea;
   private ChatHistoryPanel _chatHistoryPanel;
   
   private JButton _endTurnButton;
   private JButton _quitGameButton;
   
   /** listener for the buttons */
   private final ActionListener _actionListener = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent aEvent)
      {
         Object tSource = aEvent.getSource();
         
         if(tSource == _endTurnButton)
         {
            setIsMyTurn(false);
            _model.getGameData().endTurn();
            
            Message tEndTurnMsg = new Message(Type.eGAME_DATA);
            tEndTurnMsg.gameData = new GameData(_model.getGameData());
            
            _client.sendMessage(tEndTurnMsg);
         }
         else if(tSource == _quitGameButton)
         {
            Object[] tOptions = {"Yes","Cancel"};
            int tResponse = JOptionPane.showOptionDialog(getParent(),
                  "Are you sure you want to quit the game?",
                  "Confirmation",
                  JOptionPane.OK_CANCEL_OPTION,
                  JOptionPane.PLAIN_MESSAGE,
                  null,
                  tOptions,
                  tOptions[1]);
            
            if(tResponse == JOptionPane.OK_OPTION)
            {
               Message tQuitMsg = new Message(Type.eGAME_QUIT);
               _client.sendMessage(tQuitMsg);
            }
         }
      }
   };
   
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
   
   /** listener for game data change events */
   private final GameModelListener _modelListener = new GameModelListener()
   {
      @Override
      public void gameDataChanged()
      {
         // if turn has changed
         if((_model.getGameData().getTurnPlayerID() == _playerID) != _myTurn)
         {
            setIsMyTurn(!_myTurn);
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
      _model.addListener(_modelListener);
      
      this.setPreferredSize(new Dimension(Constants.kWIDTH, Constants.kHEIGHT));
      this.setFocusable(true);
      
      initComponents();
      
      setIsMyTurn(_model.getGameData().getTurnPlayerID() == _playerID);
      
      _boardPanel.updateBufferedImage();
   }
   
   private void initComponents()
   {
      _playerID = Conjurer.kPLAYER_B;
      int tOpponentID = Conjurer.kPLAYER_A;
      
      //determine which player we are
      if(_model.getGameData().getPlayer(Conjurer.kPLAYER_A).getName().
               equals(_client.getUserName()))
      {
         _playerID = Conjurer.kPLAYER_A;
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
      _boardPanel = new BoardPanel(_model, _playerID);
      _boardPanel.setLocation(new Point(tWidth/2 - tHeight/2, 0));
      _boardPanel.setSize(new Dimension(tHeight, tHeight));
      _boardPanel.setBufferedImageSize(_boardPanel.getSize());
      add(_boardPanel, Constants.kBOARD_LAYER);
      
      //card details area
      _cardDetailsArea = new CardDetailsArea();
      _cardDetailsArea.setLocation(new Point(tWidth * 4/5, tHeight * 1/4));
      _cardDetailsArea.setSize(new Dimension(tWidth * 1/5, tHeight * 1/2));
      add(_cardDetailsArea, Constants.kUI_LAYER);
      
      //chat/history area
      _chatHistoryPanel = new ChatHistoryPanel(_client, false);
      _chatHistoryPanel.setLocation(new Point(0,0));
      _chatHistoryPanel.setSize(new Dimension(tWidth * 1/5, tHeight * 3/4));
      add(_chatHistoryPanel, Constants.kUI_LAYER);
      
      //hand display area
      _handDisplayArea = new HandDisplayArea(_client, _model, _playerID);
      _handDisplayArea.setLocation(new Point(tWidth  * 5/7, tHeight * 3/4));
      _handDisplayArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      add(_handDisplayArea, Constants.kUI_LAYER);
      
      //player area
      _playerArea = new PlayerDetailsArea(_client, _model, _playerID);
      _playerArea.setLocation(new Point(0, tHeight * 3/4));
      _playerArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      add(_playerArea, Constants.kUI_LAYER);
      
      //opponent area
      _oppArea = new PlayerDetailsArea(_client, _model, tOpponentID);
      _oppArea.setLocation(new Point(tWidth  * 5/7, 0));
      _oppArea.setSize(new Dimension(tWidth * 2/7, tHeight * 1/4));
      add(_oppArea, Constants.kUI_LAYER);
      
      //end turn button
      _endTurnButton = new JButton("End Turn");
      _endTurnButton.setLocation(new Point(tWidth * 5/7 - tWidth * 1/16, tHeight * 23/24 - Constants.kUI_PADDING));
      _endTurnButton.setSize(new Dimension(tWidth * 1/16, tHeight * 1/24));
      _endTurnButton.setBackground(Constants.kUI_BKGD_COLOR);
      _endTurnButton.setForeground(Color.WHITE);
      _endTurnButton.addActionListener(_actionListener);
      add(_endTurnButton, Constants.kUI_LAYER);
      
      //quit game button
      _quitGameButton = new JButton("Quit Game");
      _quitGameButton.setLocation(new Point(tWidth * 2/7, tHeight * 23/24 - Constants.kUI_PADDING));
      _quitGameButton.setSize(new Dimension(tWidth * 1/16, tHeight * 1/24));
      _quitGameButton.setBackground(Constants.kUI_BKGD_COLOR);
      _quitGameButton.setForeground(Color.WHITE);
      _quitGameButton.addActionListener(_actionListener);
      add(_quitGameButton, Constants.kUI_LAYER);
   }
   
   /**
    * Enables/disables certain features of this panel based on
    * whether or not it is our turn
    */
   public void setIsMyTurn(boolean aMyTurn)
   {
      _myTurn = aMyTurn;
      
      _endTurnButton.setEnabled(aMyTurn);
      
      _playerArea.setIsMyTurn(aMyTurn);
      _oppArea.setIsMyTurn(aMyTurn);
      _boardPanel.setIsMyTurn(aMyTurn);
   }
}
