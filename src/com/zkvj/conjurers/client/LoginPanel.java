package com.zkvj.conjurers.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.zkvj.conjurers.core.Constants;

public class LoginPanel extends JPanel
{
   private static final long serialVersionUID = 1792989718696030500L;
   
   private final JTextField _username = new JTextField(20);
   private JPasswordField _password = new JPasswordField(20);

   public LoginPanel()
   {
      this.setPreferredSize(new Dimension(Constants.kLOGIN_WIDTH,
                                          Constants.kLOGIN_HEIGHT));
      this.setFocusable(true);
      this.setBackground(Constants.kBACKGROUND_COLOR);
      
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
   }
   
   @Override
   public synchronized void addKeyListener(KeyListener aListener)
   {
      super.addKeyListener(aListener);
      
      _username.addKeyListener(aListener);
      _password.addKeyListener(aListener);
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
