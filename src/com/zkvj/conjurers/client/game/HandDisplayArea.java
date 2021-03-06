package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.zkvj.conjurers.client.game.SelectionMgr.PlayedCardListener;
import com.zkvj.conjurers.core.Card;
import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameModel;
import com.zkvj.conjurers.core.GameModel.GameModelListener;

/**
 * Class responsible for drawing the area which displays a .
 */
public class HandDisplayArea extends JPanel
{
   private static final long serialVersionUID = 8520685473956498948L;

   private final GameModel _model;
   
   /** keep track of which player is us */
   private final int _playerID;
   
   private HandTableModel _tableModel;
   private JTable _table;
   
   /** set selected card based on table selection */
   private ListSelectionListener _selectionListener = new ListSelectionListener()
   {
      @Override
      public void valueChanged(ListSelectionEvent aEvent)
      {
         int tSelectedRow = _table.getSelectedRow();
         
         if(tSelectedRow >= 0 &&
            tSelectedRow < _tableModel.getRowCount())
         {
            SelectionMgr.setSelectedCard(_tableModel.getCardAt(tSelectedRow));
         }
      }
   };
   
   /** set focus card when hovering mouse over table */
   private final MouseMotionAdapter _mouseMotionAdapter = new MouseMotionAdapter()  
   {  
      public void mouseMoved(MouseEvent aEvent)  
      {  
         int tMouseOverRow = _table.rowAtPoint(aEvent.getPoint());
         
         if (tMouseOverRow >= 0 &&
             tMouseOverRow < _tableModel.getRowCount())  
         {
            SelectionMgr.setFocusCard(_tableModel.getCardAt(tMouseOverRow));
         }
      }  
   };
   
   /** listen for when selected cards are played */
   private final PlayedCardListener _playedCardListener = new PlayedCardListener()
   {
      @Override
      public void selectedCardPlayed()
      {
         updateFromModel();
      }
   };
   
   /** listen for game data changes */
   private final GameModelListener _modelListener = new GameModelListener()
   {
      @Override
      public void gameDataChanged()
      {
         //if hand has changed
         //(assuming hand can only change by adding/removing a single card at a time)
         if(getPlayer().getHand().size() != _tableModel.getRowCount())
         {
            SelectionMgr.setSelectedCard(null);
            
            updateFromModel();
         }
      }
   };
   
   /**
    * Constructor
    * @param aClient
    * @param aModel
    */
   public HandDisplayArea(GameModel aModel, int aPlayerID)
   {
      _model = aModel;
      _playerID = aPlayerID;
      
      _model.addListener(_modelListener);
      
      SelectionMgr.addPlayedCardListener(_playedCardListener);
      
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
      _tableModel.setHand(getPlayer().getHand());
      _table = new JTable(_tableModel);
      _table.setTableHeader(null);
      _table.setBackground(Constants.kUI_BKGD_COLOR);
      _table.setForeground(Color.WHITE);
      _table.setSelectionBackground(Color.BLACK);
      _table.setSelectionForeground(Color.WHITE);
      _table.setFillsViewportHeight(true);
      _table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      _table.getSelectionModel().addListSelectionListener(_selectionListener);
      _table.addMouseMotionListener(_mouseMotionAdapter);
      tMainPanel.add(new JScrollPane(_table), tConstraints);
      
      this.add(tMainPanel, tMainPanelConstraints);
   }
   
   /**
    * Convenience method for getting the player from the model.
    * @return Conjurer
    */
   private Conjurer getPlayer()
   {
      return _model.getGameData().getPlayer(_playerID);
   }
   
   /**
    * Updates this panel based on current data from model.
    */
   private void updateFromModel()
   {
      _table.clearSelection();
      _tableModel.setHand(getPlayer().getHand());
   }
   
   /**
    * Model for the hand table
    */
   private class HandTableModel extends AbstractTableModel
   {
      private static final long serialVersionUID = 7677048714758679696L;
      
      private List<Card> _hand = new ArrayList<Card>();

      @Override
      public int getColumnCount()
      {
         return 2;
      }

      @Override
      public int getRowCount()
      {
         return _hand.size();
      }

      @Override
      public Object getValueAt(int aRow, int aCol)
      {
         Object tReturn = null;
         
         if(aRow >= 0 && aRow < _hand.size())
         {
            switch(aCol)
            {
               case 0:
               {
                  tReturn = _hand.get(aRow).getName();
                  break;
               }
               case 1:
               {
                  tReturn = _hand.get(aRow).getEnergyCost();
                  break;
               }
               default:
                  break;
            }
         }
         else
         {
            System.err.println("HandTableModel:getValueAt: given row is out of bounds");
         }
         
         return tReturn;
      }
      
      /**
       * Sets the hand for this model.
       * @param aHand
       */
      public void setHand(List<Card> aHand)
      {
         if(null != aHand)
         {
            _hand = aHand;
            fireTableDataChanged();
         }
         else
         {
            System.err.println("HandTableModel: setHand: given list was null");
         }
      }
      
      /**
       * Returns the card at the given row, or null if row is out of bounds.
       * @param aRow
       * @return Card (or null)
       */
      public Card getCardAt(int aRow)
      {
         Card tReturn = null;
         
         if(aRow >= 0 && aRow < _hand.size())
         {
            tReturn = _hand.get(aRow);
         }
         else
         {
            System.err.println("HandTableModel:getCardAt: given row is out of bounds");
         }
         
         return tReturn;
      }
   }
}
