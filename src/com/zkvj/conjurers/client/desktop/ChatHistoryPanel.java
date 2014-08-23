package com.zkvj.conjurers.client.desktop;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.zkvj.conjurers.client.Client;
import com.zkvj.conjurers.client.Client.ClientMessageHandler;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Message;
import com.zkvj.conjurers.core.Message.Type;

public class ChatHistoryPanel extends JPanel
{
   private static final long serialVersionUID = -12255694380636798L;

   private JTextField _textField;
   private JTextArea _textArea;
   
   private Client _client;
   
   /** true for the desktop chat panel, false for game chat panel */
   private boolean _desktopMode;
   
   private final ActionListener _actionListener = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent aEvent)
      {
         Object tSource = aEvent.getSource();
         
         if(tSource == _textField)
         {
            String tText = _client.getUserName() + ": " + _textField.getText();
            _textField.setText("");
            
            Message tMessage = new Message();
            
            if(_desktopMode)
            {
               tMessage.type = Type.eDESKTOP_CHAT;
            }
            else
            {
               tMessage.type = Type.eGAME_CHAT;
            }
            
            tMessage.chatMessage = tText;
            _client.sendMessage(tMessage);
         }
      }
   };
   
   private final ClientMessageHandler _messageHandler = new ClientMessageHandler()
   {
      @Override
      public void handleMessage(Message aMsg)
      {
         if((_desktopMode && Type.eDESKTOP_CHAT == aMsg.type) ||
            (!_desktopMode && Type.eGAME_CHAT == aMsg.type))
         {
            _textArea.append(aMsg.chatMessage + "\n");
         }
      }
   };
   
   /**
    * Constructor
    * @param aClient
    * @param aDesktopMode - true for desktop chat, false for game chat
    */
   public ChatHistoryPanel(Client aClient, boolean aDesktopMode)
   {
      _client = aClient;
      _desktopMode = aDesktopMode;
      
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
      tConstaints.insets = new Insets(Constants.kUI_PADDING, Constants.kUI_PADDING,
                                      0, Constants.kUI_PADDING);
      
      _textArea = new JTextArea("Welcome to Conjurers\n", 100, 25);
      _textArea.setEditable(false);
      _textArea.setLineWrap(true);
      _textArea.setWrapStyleWord(true);
      _textArea.setForeground(Color.WHITE);
      _textArea.setBackground(Constants.kUI_BKGD_COLOR);
      tConstaints.gridy = 0;
      tConstaints.weightx = 1;
      tConstaints.weighty = 1;
      add(new JScrollPane(_textArea),tConstaints);
      
      _textField = new JTextField("<enter text here>");
      _textField.setForeground(Color.WHITE);
      _textField.setBackground(Constants.kUI_BKGD_COLOR);
      _textField.addActionListener(_actionListener);
      tConstaints.gridy = 1;
      tConstaints.weightx = 1;
      tConstaints.weighty = 0;
      add(_textField,tConstaints);
   }
}
