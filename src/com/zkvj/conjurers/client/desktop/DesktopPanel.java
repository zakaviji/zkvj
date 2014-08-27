package com.zkvj.conjurers.client.desktop;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JPanel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.core.Constants;

public class DesktopPanel extends JPanel
{
   private static final long serialVersionUID = -3236532239583089232L;

   private ChatHistoryPanel _chatHistoryPanel;
   private PlayerListPanel _playerListPanel;
   
   private Client _client;
   
   /**
    * Constructor
    * @param aClient
    */
   public DesktopPanel(Client aClient)
   {
      _client = aClient;
      
      this.setPreferredSize(new Dimension(Constants.kWIDTH,
                                          Constants.kHEIGHT));
      this.setBackground(Constants.kBACKGROUND_COLOR);
      
      initComponents();
   }

   /**
    * Initializes the components of this panel
    */
   private void initComponents()
   {
      setLayout(null);
      
      int tWidth = getPreferredSize().width;
      int tHeight = getPreferredSize().height;
      
      JPanel tLibraryPanel = new JPanel();
      tLibraryPanel.setBackground(Constants.kBACKGROUND_COLOR);
      tLibraryPanel.setLocation(new Point(0,0));
      tLibraryPanel.setSize(new Dimension(tWidth * 4/5, tHeight));
      add(tLibraryPanel);
      
      _chatHistoryPanel = new ChatHistoryPanel(_client, true);
      _chatHistoryPanel.setLocation(new Point(tWidth  * 4/5, 0));
      _chatHistoryPanel.setSize(new Dimension(tWidth * 1/5, tHeight * 3/4));
      add(_chatHistoryPanel);
      
      _playerListPanel = new PlayerListPanel(_client);
      _playerListPanel.setLocation(new Point(tWidth  * 4/5, tHeight * 3/4));
      _playerListPanel.setSize(new Dimension(tWidth * 1/5, tHeight * 1/4));
      add(_playerListPanel);
   }
   
   /**
    * Resets the components of this panel back to their default state
    */
   public void reset()
   {
      _playerListPanel.reset();
   }
}
