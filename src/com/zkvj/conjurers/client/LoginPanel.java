package com.zkvj.conjurers.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.Message;

public class LoginPanel extends JPanel
{
   private static final long serialVersionUID = 1792989718696030500L;
   
   private final JTextField _username = new JTextField(20);
   private JPasswordField _password = new JPasswordField(20);
   
   private Client _client;
   
   /** Key listener */
   private final KeyListener _keyListener = new KeyListener()
   {
      @Override
      public void keyPressed(KeyEvent aEvent)
      {
         Object tSource = aEvent.getSource();
         int aKey = aEvent.getKeyCode();
         
         if((tSource == _username || tSource == _password) &&
             KeyEvent.VK_ENTER == aKey)
         {
            String tUsername = _username.getText().trim();
            _client.setUserName(tUsername);
            
            char[] tPassword = _password.getPassword();
            
            Message tLoginRequest = new Message();
            tLoginRequest._type = Message.Type.eLOGIN_REQUEST;
            tLoginRequest._password = String.valueOf(tPassword);
            
            _client.sendMessage(tLoginRequest);
         }
      }

      @Override
      public void keyTyped(KeyEvent aEvent){}

      @Override
      public void keyReleased(KeyEvent aEvent){}
   };

   public LoginPanel(Client aClient)
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
      tConstraints.fill = GridBagConstraints.HORIZONTAL;

      JLabel tUsernameLabel = new JLabel("Username: ");
      tConstraints.gridx = 0;
      tConstraints.gridy = 0;
      tConstraints.gridwidth = 1;
      this.add(tUsernameLabel, tConstraints);

      tConstraints.gridx = 1;
      tConstraints.gridy = 0;
      tConstraints.gridwidth = 2;
      this.add(_username, tConstraints);

      JLabel tPasswordLabel = new JLabel("Password: ");
      tConstraints.gridx = 0;
      tConstraints.gridy = 1;
      tConstraints.gridwidth = 1;
      this.add(tPasswordLabel, tConstraints);

      tConstraints.gridx = 1;
      tConstraints.gridy = 1;
      tConstraints.gridwidth = 2;
      this.add(_password, tConstraints);
      
      _username.addKeyListener(_keyListener);
      _password.addKeyListener(_keyListener);
      
      _username.requestFocus();
   }
   
   /**
    * Returns the text entered as the username
    * @return String
    */
   public String getUsername()
   {
      return _username.getText().trim();
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
