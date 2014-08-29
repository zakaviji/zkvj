package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;

/**
 * Class responsible for drawing the area which displays a close-up image of the currently
 * selected card.
 */
public class PlayerDetailsArea extends JPanel
{
   private static final long serialVersionUID = -6000029103485694628L;

   private Client _client;
   private final Conjurer _player;
   
   /** dynamic elements of this panel */
   private JLabel _health;
   private JLabel _energy;
   private JLabel _deck;
   private JLabel _hand;
   
   /**
    * Constructor
    * @param aClient - the client
    * @param aPlayer - the player to show on this panel
    */
   public PlayerDetailsArea(Client aClient, Conjurer aPlayer)
   {
      _client = aClient;
      _player = aPlayer;

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
      
      JLabel tName = new JLabel(_player.getName());
      tName.setForeground(Color.WHITE);
      tName.setFont(new Font(tName.getFont().getName(), Font.BOLD, 20));
      tConstraints.insets = new Insets(5,5,5,5);
      tConstraints.gridx = 0;
      tConstraints.gridy = 0;
      tConstraints.gridwidth = 3;
      tConstraints.weightx = 1;
      tConstraints.weighty = 0.2;
      tMainPanel.add(tName, tConstraints);

      JLabel tHealthLabel = new JLabel("Health:");
      tHealthLabel.setForeground(Color.WHITE);
      tConstraints.insets = new Insets(0,5,5,5);
      tConstraints.gridx = 0;
      tConstraints.gridy = 1;
      tConstraints.gridwidth = 1;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.2;
      tMainPanel.add(tHealthLabel, tConstraints);
      
      _health = new JLabel(""+_player.getHealth());
      _health.setForeground(Color.WHITE);
      tConstraints.gridx = 1;
      tConstraints.gridy = 1;
      tConstraints.gridwidth = 2;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.2;
      tMainPanel.add(_health, tConstraints);

      JLabel tEnergyLabel = new JLabel("Energy:");
      tEnergyLabel.setForeground(Color.WHITE);
      tConstraints.gridx = 0;
      tConstraints.gridy = 2;
      tConstraints.gridwidth = 1;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.2;
      tMainPanel.add(tEnergyLabel, tConstraints);
      
      _energy = new JLabel(""+_player.getEnergy());
      _energy.setForeground(Color.WHITE);
      tConstraints.gridx = 1;
      tConstraints.gridy = 2;
      tConstraints.gridwidth = 2;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.2;
      tMainPanel.add(_energy, tConstraints);

      JLabel tDeckLabel = new JLabel("Deck:");
      tDeckLabel.setForeground(Color.WHITE);
      tConstraints.gridx = 0;
      tConstraints.gridy = 3;
      tConstraints.gridwidth = 1;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.2;
      tMainPanel.add(tDeckLabel, tConstraints);
      
      _deck = new JLabel(""+_player.getDeck().size());
      _deck.setForeground(Color.WHITE);
      tConstraints.gridx = 1;
      tConstraints.gridy = 3;
      tConstraints.gridwidth = 2;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.2;
      tMainPanel.add(_deck, tConstraints);

      JLabel tHandLabel = new JLabel("Hand:");
      tHandLabel.setForeground(Color.WHITE);
      tConstraints.gridx = 0;
      tConstraints.gridy = 4;
      tConstraints.gridwidth = 1;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.2;
      tMainPanel.add(tHandLabel, tConstraints);
      
      _hand = new JLabel(""+_player.getHand().size());
      _hand.setForeground(Color.WHITE);
      tConstraints.gridx = 1;
      tConstraints.gridy = 4;
      tConstraints.gridwidth = 2;
      tConstraints.weightx = .5;
      tConstraints.weighty = 0.2;
      tMainPanel.add(_hand, tConstraints);
      
      this.add(tMainPanel, tMainPanelConstraints);
   }
   
//   /**
//    * Draws this display component.
//    */
//   @Override
//   public void draw(Graphics2D aG)
//   {
//      aG.setColor(Constants.kUI_BKGD_COLOR);
//      aG.fillRect(10, 10, getWidth()-20, getHeight()-20);
//      
//      if(null != _data)
//      {
//         aG.setColor(Color.WHITE);
//         
//         Conjurer tPlayer = _data.getOpponent();
//         if(_player)
//         {
//            tPlayer = _data.getPlayer();
//         }
//         
//         int tX = 20;
//         int tY = 40;
//         int tDy = 30;
//         
//         aG.drawString(tPlayer.getName(), tX, tY);
//         tY += tDy;
//         aG.drawString("Health: " + tPlayer.getHealth(), tX, tY);
//         tY += tDy;
//         aG.drawString("Deck: " + tPlayer.getDeck().size(), tX, tY);
//         tY += tDy;
//         aG.drawString("Hand: " + tPlayer.getHand().size(), tX, tY);
//         tY += tDy;
//         aG.drawString("Energy: " + tPlayer.getEnergy(), tX, tY);
//      }
//      else
//      {
//         System.out.println("PlayerDetailsArea.draw(): WARNING: game data was null");
//      }
//   }
}
