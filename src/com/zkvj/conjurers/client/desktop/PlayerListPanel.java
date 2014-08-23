package com.zkvj.conjurers.client.desktop;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.client.Client.ClientMessageHandler;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;

public class PlayerListPanel extends JPanel
{
   private static final long serialVersionUID = -2457542333076681346L;

   private Client _client;
   
   private PlayerListTableModel _tableModel;
   
   private final ClientMessageHandler _messageHandler = new ClientMessageHandler()
   {
      @Override
      public void handleMessage(Message aMsg)
      {
         if(Type.eUSER_LIST == aMsg.type)
         {
            _tableModel.setUserList(
               new ArrayList<String>(Arrays.asList(aMsg.userList)));
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

      GridBagConstraints tConstaints = new GridBagConstraints();
      tConstaints.fill = GridBagConstraints.BOTH;
      tConstaints.insets = new Insets(Constants.kUI_PADDING,
            Constants.kUI_PADDING, Constants.kUI_PADDING, Constants.kUI_PADDING);
      tConstaints.weightx = 1;
      tConstaints.weighty = 1;
      
      _tableModel = new PlayerListTableModel();
      JTable tTable = new JTable(_tableModel);
      tTable.setTableHeader(null);

      tTable.setBackground(Constants.kUI_BKGD_COLOR);
      tTable.setForeground(Color.WHITE);
      this.add(new JScrollPane(tTable), tConstaints);
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
         return _userList.get(aRow);
      }
      
      protected void setUserList(ArrayList<String> aUserList)
      {
         _userList = aUserList;
         fireTableDataChanged();
      }
   }
}
