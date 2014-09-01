package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameData;
import com.zkvj.conjurers.core.GameModel;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.GameModel.GameModelListener;
import com.zkvj.conjurers.core.Message.Type;

/**
 * Class responsible for drawing the area which displays a close-up image of the currently
 * selected card.
 */
public class PlayerDetailsArea extends JPanel
{
   private static final long serialVersionUID = -6000029103485694628L;

   private final Client _client;
   private final GameModel _model;
   
   /** keep track of which player is shown on this panel */
   private final int _playerID;
   
   /** dynamic elements of this panel */
   private JSpinner _health;
   private JLabel _energy;
   private JLabel _deck;
   private JLabel _hand;
   
   private final ChangeListener _healthListener = new ChangeListener()
   {
      @Override
      public void stateChanged(ChangeEvent aEvent)
      {
         if(aEvent.getSource() == _health)
         {
            int tHealth = ((SpinnerNumberModel)_health.getModel()).
                     getNumber().intValue();
            
            /**
             * If health has changed, but game model has not changed yet,
             * then this change was made via our GUI, and we need to send
             * our updated game data to the server
             */
            if(tHealth != getPlayer().getHealth())
            {
               getPlayer().setHealth(tHealth);

               Message tGameDataMsg = new Message(Type.eGAME_DATA);
               tGameDataMsg.gameData = new GameData(_model.getGameData());
               
               _client.sendMessage(tGameDataMsg);
            }
         }
      }
   };
   
   private final GameModelListener _modelListener = new GameModelListener()
   {
      @Override
      public void gameDataChanged()
      {
         _health.setValue(new Integer(getPlayer().getHealth()));
         _energy.setText(""+getPlayer().getEnergy());
         _deck.setText(""+getPlayer().getDeck().size());
         _hand.setText(""+getPlayer().getHand().size());
      }
   };
   
   /**
    * Constructor
    * @param aClient - the client
    * @param aModel - the game data model
    * @param aPlayerID - the ID for the player displayed on this panel
    */
   public PlayerDetailsArea(Client aClient, GameModel aModel, int aPlayerID)
   {
      _client = aClient;
      _model = aModel;
      _playerID = aPlayerID;
      
      _model.addListener(_modelListener);
      
      setBackground(Constants.kBACKGROUND_COLOR);
      initComponents();
   }

   /**
    * Initializes the components of this panel.
    */
   private void initComponents()
   {
      setLayout(new GridBagLayout());
      GridBagConstraints tMainPanelConstraints = new GridBagConstraints();
      tMainPanelConstraints.fill = GridBagConstraints.BOTH;
      tMainPanelConstraints.insets = new Insets(Constants.kUI_PADDING,
               Constants.kUI_PADDING, Constants.kUI_PADDING, Constants.kUI_PADDING);
      tMainPanelConstraints.weightx = 1;
      tMainPanelConstraints.weighty = 1;
      
      JPanel tMainPanel = new JPanel(new GridBagLayout());
      tMainPanel.setBackground(Constants.kUI_BKGD_COLOR);
      
      GridBagConstraints tConstraints = new GridBagConstraints();
      tConstraints.fill = GridBagConstraints.BOTH;
      
      JLabel tName = new JLabel(getPlayer().getName());
      tName.setForeground(Color.WHITE);
      Font tFont = new Font(tName.getFont().getName(), Font.BOLD, 20);
      tName.setFont(tFont);
      tConstraints.insets = new Insets(20,20,20,20);
      tConstraints.gridx = 0;
      tConstraints.gridy = 0;
      tConstraints.gridwidth = 2;
      tConstraints.weightx = 1;
      tConstraints.weighty = 0.4;
      tMainPanel.add(tName, tConstraints);

      //HEALTH
      JPanel tHealthPanel = new JPanel();
      tHealthPanel.setBackground(Constants.kUI_BKGD_COLOR);
      tHealthPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      
      JLabel tHealthLabel = new JLabel("Health:");
      tFont = new Font(tHealthLabel.getFont().getName(), Font.PLAIN, 14);
      tHealthLabel.setFont(tFont);
      tHealthLabel.setForeground(Color.WHITE);
      tHealthPanel.add(tHealthLabel);

      SpinnerModel tModel = new SpinnerNumberModel(getPlayer().getHealth(),
                                                   Constants.kMIN_PLAYER_HEALTH,
                                                   Constants.kMAX_PLAYER_HEALTH,
                                                   1);
      _health = new JSpinner(tModel);
      JSpinner.NumberEditor tEditor = new JSpinner.NumberEditor(_health, "##");
      tEditor.getTextField().setBackground(Constants.kUI_BKGD_COLOR);
      tEditor.getTextField().setForeground(Color.WHITE);
      tEditor.getTextField().setFont(tFont);
      tEditor.getTextField().setEditable(false);
      tEditor.getTextField().setFocusable(false);
      _health.setEditor(tEditor);
      _health.addChangeListener(_healthListener);
      tHealthPanel.add(_health);

      tConstraints.gridx = 0;
      tConstraints.gridy = 1;
      tConstraints.gridwidth = 1;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.3;
      tMainPanel.add(tHealthPanel, tConstraints);

      //ENERGY
      JPanel tEnergyPanel = new JPanel();
      tEnergyPanel.setBackground(Constants.kUI_BKGD_COLOR);
      tEnergyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      
      JLabel tEnergyLabel = new JLabel("Energy:");
      tEnergyLabel.setFont(tFont);
      tEnergyLabel.setForeground(Color.WHITE);
      tEnergyPanel.add(tEnergyLabel);
      
      _energy = new JLabel(""+getPlayer().getEnergy());
      _energy.setFont(tFont);
      _energy.setForeground(Color.WHITE);
      tEnergyPanel.add(_energy);

      tConstraints.gridx = 0;
      tConstraints.gridy = 2;
      tMainPanel.add(tEnergyPanel, tConstraints);

      //DECK SIZE
      JPanel tDeckPanel = new JPanel();
      tDeckPanel.setBackground(Constants.kUI_BKGD_COLOR);
      tDeckPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      
      JLabel tDeckLabel = new JLabel("Deck:");
      tDeckLabel.setFont(tFont);
      tDeckLabel.setForeground(Color.WHITE);
      tDeckPanel.add(tDeckLabel);
      
      _deck = new JLabel(""+getPlayer().getDeck().size());
      _deck.setFont(tFont);
      _deck.setForeground(Color.WHITE);
      tDeckPanel.add(_deck);

      tConstraints.gridx = 1;
      tConstraints.gridy = 1;
      tMainPanel.add(tDeckPanel, tConstraints);

      //HAND SIZE
      JPanel tHandPanel = new JPanel();
      tHandPanel.setBackground(Constants.kUI_BKGD_COLOR);
      tHandPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      
      JLabel tHandLabel = new JLabel("Hand:");
      tHandLabel.setFont(tFont);
      tHandLabel.setForeground(Color.WHITE);
      tHandPanel.add(tHandLabel);
      
      _hand = new JLabel(""+getPlayer().getHand().size());
      _hand.setFont(tFont);
      _hand.setForeground(Color.WHITE);
      tHandPanel.add(_hand);

      tConstraints.gridx = 1;
      tConstraints.gridy = 2;
      tMainPanel.add(tHandPanel, tConstraints);
      
      this.add(tMainPanel, tMainPanelConstraints);
   }
   
   /**
    * Convenience method for getting the player displayed by this panel.
    * @return Conjurer
    */
   private Conjurer getPlayer()
   {
      return _model.getGameData().getPlayer(_playerID);
   }
   
   /**
    * Enables/disables this panel based on whether or not it is our turn
    */
   @Override
   public void setEnabled(boolean aEnabled)
   {
      super.setEnabled(aEnabled);
      
      _health.setEnabled(aEnabled);
   }
}
