package com.zkvj.conjurers.client.desktop;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.client.Client.ClientMessageHandler;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;

public class PlayerListPanel extends JPanel
{
   private static final long serialVersionUID = -2457542333076681346L;
   
   private static final String kINVITE = "Invite to Game";
   //private static final String kWAITING = "Waiting on Opponent...";

   private Client _client;
   
   private PlayerListTableModel _tableModel;
   
   private JTable _table;
   
   private JButton _inviteButton;
   
   /** listener for the invite button */
   private final ActionListener _actionListener = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent aEvent)
      {
         Object tSource = aEvent.getSource();
         int tRow = _table.getSelectedRow();
         
         if(tSource == _inviteButton &&
            tRow >= 0 &&
            tRow < _tableModel.getRowCount())
         {
            String tOpponent = _tableModel.getUserNameAt(tRow);
            
            if(tOpponent.equals(_client.getUserName()))
            {
               System.err.println("PlayerListPanel: error: somehow attempted to invite self to game");
               return;
            }
            
            Message tGameRequest = new Message(Type.eGAME_REQUEST);
            tGameRequest.opponent = tOpponent;
            
            _client.sendMessage(tGameRequest);
         }
      }
   };
   
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
            String tSelection = _tableModel.getUserNameAt(tSelectedRow);
            
            if(null != tSelection &&
               !tSelection.equals(_client.getUserName()))
            {
               _inviteButton.setEnabled(true);
            }
            else
            {
               _inviteButton.setEnabled(false);
            }
         }
         else
         {
            _inviteButton.setEnabled(false);
         }
      }
   };
   
   private final ClientMessageHandler _messageHandler = new ClientMessageHandler()
   {
      @Override
      public void handleMessage(Message aMsg)
      {
         if(Type.eUSER_LIST == aMsg.type)
         {
            _tableModel.setUserList(aMsg.userList);
         }
      }
   };

   /**
    * Constructor
    * 
    * @param aClient
    */
   public PlayerListPanel(Client aClient)
   {
      _client = aClient;

      setBackground(Constants.kBACKGROUND_COLOR);
      
      _client.addMessageHandler(_messageHandler);

      initComponents();
   }

   /**
    * Initializes the components of this panel
    */
   private void initComponents()
   {
      setLayout(new GridBagLayout());

      GridBagConstraints tConstraints = new GridBagConstraints();
      tConstraints.fill = GridBagConstraints.BOTH;
      tConstraints.weightx = 1;
      
      _tableModel = new PlayerListTableModel();
      _table = new JTable(_tableModel);
      _table.setTableHeader(null);
      _table.setShowGrid(false);
      _table.setBackground(Constants.kUI_BKGD_COLOR);
      _table.setForeground(Color.WHITE);
      _table.setSelectionBackground(Color.BLACK);
      _table.setSelectionForeground(Color.WHITE);
      _table.setFillsViewportHeight(true);
      _table.getSelectionModel().addListSelectionListener(_selectionListener);
      tConstraints.insets = new Insets(Constants.kUI_PADDING,
            Constants.kUI_PADDING, Constants.kUI_PADDING, Constants.kUI_PADDING);
      tConstraints.weighty = 1;
      tConstraints.gridy = 0;
      this.add(new JScrollPane(_table), tConstraints);
      
      JPanel tButtonPanel = new JPanel();
      tButtonPanel.setBackground(Constants.kBACKGROUND_COLOR);
      _inviteButton = new JButton(kINVITE);
      _inviteButton.setEnabled(false);
      _inviteButton.setBackground(Constants.kUI_BKGD_COLOR);
      _inviteButton.setForeground(Color.WHITE);
      _inviteButton.addActionListener(_actionListener);
      tButtonPanel.add(_inviteButton);
      tConstraints.insets = new Insets(0,
            Constants.kUI_PADDING, Constants.kUI_PADDING, Constants.kUI_PADDING);
      tConstraints.weighty = 0;
      tConstraints.gridy = 1;
      this.add(tButtonPanel, tConstraints);
      
   }
   
   /**
    * Resets the table and buttons to there default state
    */
   public void reset()
   {
      _table.getSelectionModel().clearSelection();
      _inviteButton.setText(kINVITE);
   }
   
   private class PlayerListTableModel extends AbstractTableModel
   {
      private static final long serialVersionUID = -8903469997553242200L;

      private List<String> _userList = new ArrayList<String>();
      
      @Override
      public int getColumnCount()
      {
         return 1;
      }

      @Override
      public int getRowCount()
      {
         return _userList.size();
      }

      @Override
      public Object getValueAt(int aRow, int aCol)
      {
         Object tReturn = null;
         
         if(aRow >= 0 && aRow < _userList.size())
         {
            tReturn = _userList.get(aRow);
         }
         else
         {
            System.err.println("PlayerListTableModel:getValueAt: given row is out of bounds");
         }
         
         return tReturn;
      }
      
      /**
       * Returns the user name at the given row, or null if row is out of bounds.
       * @param aRow
       * @return String (or null)
       */
      public String getUserNameAt(int aRow)
      {
         String tReturn = null;
         
         if(aRow >= 0 && aRow < _userList.size())
         {
            tReturn = _userList.get(aRow);
         }
         else
         {
            System.err.println("PlayerListTableModel:getUserAt: given row is out of bounds");
         }
         
         return tReturn;
      }
      
      protected void setUserList(List<String> aUserList)
      {
         if(null != aUserList)
         {
            _userList = aUserList;
            fireTableDataChanged();
         }
         else
         {
            System.err.println("PlayerListTableModel: setUserList: given list was null");
         }
      }
   }
}
