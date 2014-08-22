package com.zkvj.conjurers.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Message;

public class LoginPanel extends JPanel
{
   private static final long serialVersionUID = 1792989718696030500L;
   
   private final JTextField _userName = new JTextField(20);
   private JPasswordField _password = new JPasswordField(20);
   
   private Client _client;
   
   private final ActionListener _actionListener = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent aEvent)
      {
         Object tSource = aEvent.getSource();
         
         if(tSource == _userName || tSource == _password)
         {
            String tUsername = _userName.getText().trim();
            _client.setUserName(tUsername);
            
            char[] tPassword = _password.getPassword();
            
            Message tLoginRequest = new Message();
            tLoginRequest.type = Message.Type.eLOGIN_REQUEST;
            tLoginRequest.password = String.valueOf(tPassword);
            
            _client.sendMessage(tLoginRequest);

            _userName.setText("");
            _password.setText("");
         }
      }
   };

   /**
    * Constructor
    * @param aClient
    */
   public LoginPanel(Client aClient)
   {
      _client = aClient;
      
      this.setPreferredSize(new Dimension(Constants.kLOGIN_WIDTH,
                                          Constants.kLOGIN_HEIGHT));
      this.setBackground(Constants.kBACKGROUND_COLOR);
      
      initComponents();
   }
   
   /**
    * Initializes the components of this panel
    */
   private void initComponents()
   {
      this.setLayout(new GridBagLayout());
      
      GridBagConstraints tConstraints = new GridBagConstraints();
      tConstraints.fill = GridBagConstraints.HORIZONTAL;

      JLabel tUsernameLabel = new JLabel("Username: ");
      tConstraints.gridx = 0;
      tConstraints.gridy = 0;
      tConstraints.gridwidth = 1;
      this.add(tUsernameLabel, tConstraints);

      tConstraints.gridx = 1;
      tConstraints.gridy = 0;
      tConstraints.gridwidth = 2;
      this.add(_userName, tConstraints);

      JLabel tPasswordLabel = new JLabel("Password: ");
      tConstraints.gridx = 0;
      tConstraints.gridy = 1;
      tConstraints.gridwidth = 1;
      this.add(tPasswordLabel, tConstraints);

      tConstraints.gridx = 1;
      tConstraints.gridy = 1;
      tConstraints.gridwidth = 2;
      this.add(_password, tConstraints);
      
      _userName.addActionListener(_actionListener);
      _password.addActionListener(_actionListener);
      
      _userName.requestFocus();
   }
   
   /**
    * Returns the text entered as the username
    * @return String
    */
   public String getUsername()
   {
      return _userName.getText().trim();
   }

   /**
    * Returns the text entered as the password
    * @return String
    */
   public String getPassword()
   {
      return new String(_password.getPassword());
   }
}
