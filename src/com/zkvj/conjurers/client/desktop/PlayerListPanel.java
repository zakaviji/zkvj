package com.zkvj.conjurers.client.desktop;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.core.Constants;

public class PlayerListPanel extends JPanel
{
   private static final long serialVersionUID = -2457542333076681346L;

   private Client _client;
   
   private final ActionListener _actionListener = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent aEvent)
      {
         Object tSource = aEvent.getSource();
         
         
      }
   };

   /**
    * Constructor
    * @param aClient
    */
   public PlayerListPanel(Client aClient)
   {
      _client = aClient;
      
      setBackground(Constants.kBACKGROUND_COLOR);
      
      initComponents();
   }
   
   /**
    * Initializes the components of this panel
    */
   private void initComponents()
   {
      setLayout(new GridBagLayout());
      
      GridBagConstraints tConstaints = new GridBagConstraints();
      tConstaints.fill = GridBagConstraints.BOTH;
      tConstaints.insets = new Insets(Constants.kUI_PADDING, Constants.kUI_PADDING,
                                      Constants.kUI_PADDING, Constants.kUI_PADDING);
      tConstaints.weightx = 1;
      tConstaints.weighty = 1;
      
      JPanel tPane = new JPanel();
      tPane.setBackground(Constants.kUI_BKGD_COLOR);
      
      this.add(tPane,tConstaints);
   }
}
