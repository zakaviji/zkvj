package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.client.Client.ClientMessageHandler;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Message;

/**
 * Class responsible for drawing the area which displays a .
 */
public class HandDisplayArea extends JPanel
{
   private static final long serialVersionUID = 8520685473956498948L;

   private Client _client;
   private final Conjurer _player;
   
   private HandTableModel _tableModel;
   private JTable _table;
   
   /** listener for when a user is selected from the table */
   private ListSelectionListener _selectionListener = new ListSelectionListener()
   {
      @Override
      public void valueChanged(ListSelectionEvent aEvent)
      {
         int tSelectedRow = _table.getSelectedRow();
         
         if(tSelectedRow >= 0 &&
            tSelectedRow < _tableModel.getRowCount())
         {
            //todo
         }
      }
   };
   
   private final ClientMessageHandler _messageHandler = new ClientMessageHandler()
   {
      @Override
      public void handleMessage(Message aMsg)
      {
         //todo?
      }
   };
   
   /**
    * Constructor
    * @param aClient
    * @param aPlayer
    */
   public HandDisplayArea(Client aClient, Conjurer aPlayer)
   {
      _client = aClient;
      _player = aPlayer;

      _client.addMessageHandler(_messageHandler);
      
      setBackground(Constants.kBACKGROUND_COLOR);
      initComponents();
   }

   /**
    * Initializes the components of this panel
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
      tConstraints.weightx = 1;
      tConstraints.weighty = 1;
      tConstraints.gridx = 0;
      tConstraints.gridy = 0;

      _tableModel = new HandTableModel();
      _table = new JTable(_tableModel);
      _table.setTableHeader(null);
      _table.setBackground(Constants.kUI_BKGD_COLOR);
      _table.setForeground(Color.WHITE);
      _table.setSelectionBackground(Color.BLACK);
      _table.setSelectionForeground(Color.WHITE);
      _table.setFillsViewportHeight(true);
      _table.getSelectionModel().addListSelectionListener(_selectionListener);
      tMainPanel.add(new JScrollPane(_table), tConstraints);
      
      this.add(tMainPanel, tMainPanelConstraints);
   }
   
   /**
    * Model for the hand table
    */
   private class HandTableModel extends AbstractTableModel
   {
      private static final long serialVersionUID = 7677048714758679696L;

      @Override
      public int getColumnCount()
      {
         return 2;
      }

      @Override
      public int getRowCount()
      {
         return _player.getHand().size();
      }

      @Override
      public Object getValueAt(int aRow, int aCol)
      {
         Object tReturn = null;
         
         if(aRow >= 0 && aRow < _player.getHand().size())
         {
            switch(aCol)
            {
               case 0:
               {
                  tReturn = _player.getHand().get(aRow).getName();
                  break;
               }
               case 1:
               {
                  tReturn = _player.getHand().get(aRow).getEnergyCost();
                  break;
               }
               default:
                  break;
            }
         }
         else
         {
            System.err.println("PlayerListTableModel:getValueAt: given row is out of bounds");
         }
         
         return tReturn;
      }
      
      /**
       * Call this to indicate that player's hand has changed, and the
       * table should be redrawn.
       */
      public void handChanged()
      {
         fireTableDataChanged();
      }
   }
}
