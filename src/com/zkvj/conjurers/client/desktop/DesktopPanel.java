package com.zkvj.conjurers.client.desktop;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.core.Constants;

public class DesktopPanel extends JPanel
{
   private static final long serialVersionUID = -3236532239583089232L;

   private Client _client;
   
   private final ActionListener _actionListener = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent aEvent)
      {
         //todo
      }
   };
   
   public DesktopPanel(Client aClient)
   {
      _client = aClient;
      
      this.setPreferredSize(new Dimension(Constants.kLOGIN_WIDTH,
                                          Constants.kLOGIN_HEIGHT));
      this.setBackground(Constants.kBACKGROUND_COLOR);
      
      initComponents();
   }
   
   private void initComponents()
   {
      this.setLayout(new GridBagLayout());
      
      GridBagConstraints tConstraints = new GridBagConstraints();
      
      //chat panel, including text area and text field
      

   }
}
