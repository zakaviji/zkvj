package com.zkvj.conjurers.client.game;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.zkvj.conjurers.client.game.FocusCardMgr.FocusCardListener;
import com.zkvj.conjurers.core.Card;
import com.zkvj.conjurers.core.Constants;

/**
 * Class responsible for drawing the area which displays a close-up image of the currently
 * selected card.
 */
public class CardDetailsArea extends JPanel
{
   private static final long serialVersionUID = -740993523942217182L;
   
   //dynamic UI elements
   private JLabel _name;
   private JLabel _energyCost;
   private JTextArea _text;
   
   //minion-specific dynamic UI elements
   private JLabel _element;
   private JLabel _power;
   private JLabel _health;
   
   /** Listener for focus card changes */
   private final FocusCardListener _listener = new FocusCardListener()
   {
      @Override
      public void focusCardChanged()
      {
         Card tFocusCard = FocusCardMgr.getFocusCard();
         
         _name.setText(tFocusCard.getName());
         _energyCost.setText("Energy Cost: "+tFocusCard.getEnergyCost());
         _text.setText(tFocusCard.getText());
         _element.setText("Element: "+tFocusCard.getElement().toString());
         _power.setText("Power: "+tFocusCard.getPower());
         _health.setText("Health: "+tFocusCard.getHealth());
         
         //only show these fields for minion cards
         boolean tVisible = tFocusCard.isMinion();
         _element.setVisible(tVisible);
         _power.setVisible(tVisible);
         _health.setVisible(tVisible);
      }
   };
   
   /**
    * Constructor
    */
   public CardDetailsArea()
   {
      FocusCardMgr.addListener(_listener);

      initComponents();
   }

   /**
    * Initializes the components of this panel.
    */
   private void initComponents()
   {
      int tGap = Constants.kUI_PADDING;
      
      this.setBorder(BorderFactory.createMatteBorder(0, tGap, 0, tGap, Constants.kBACKGROUND_COLOR));
      this.setBackground(Constants.kUI_BKGD_COLOR);
      
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      //NAME
      _name = new JLabel("<mouse over a card for details>");
      Font tFont = new Font(_name.getFont().getName(), Font.BOLD, 16);
      _name.setFont(tFont);
      _name.setForeground(Color.WHITE);
      _name.setAlignmentX(LEFT_ALIGNMENT);
      _name.setBorder(BorderFactory.createEmptyBorder(tGap, tGap, tGap, tGap));
      this.add(_name);

      //ENERGY
      _energyCost = new JLabel("Energy Cost: ---");
      tFont = new Font(_energyCost.getFont().getName(), Font.PLAIN, 14);
      _energyCost.setFont(tFont);
      _energyCost.setForeground(Color.WHITE);
      _energyCost.setAlignmentX(LEFT_ALIGNMENT);
      _energyCost.setBorder(BorderFactory.createEmptyBorder(0, tGap, tGap, tGap));
      this.add(_energyCost);
      
      //TEXT
      JLabel tTextLabel = new JLabel("Text:");
      tTextLabel.setFont(tFont);
      tTextLabel.setForeground(Color.WHITE);
      tTextLabel.setAlignmentX(LEFT_ALIGNMENT);
      tTextLabel.setBorder(BorderFactory.createEmptyBorder(0, tGap, 0, tGap));
      this.add(tTextLabel);
      
      _text = new JTextArea("---", 3, 20);
      _text.setFont(tFont);
      _text.setEditable(false);
      _text.setLineWrap(true);
      _text.setWrapStyleWord(true);
      _text.setForeground(Color.WHITE);
      _text.setBackground(Constants.kUI_BKGD_COLOR);
      _text.setBorder(BorderFactory.createEmptyBorder());
      _text.setAlignmentX(LEFT_ALIGNMENT);
      _text.setBorder(BorderFactory.createEmptyBorder(0, tGap, tGap, tGap));
      _text.setMaximumSize(_text.getPreferredSize());
      this.add(_text);

      //ELEMENT
      _element = new JLabel("Element: ---");
      _element.setFont(tFont);
      _element.setForeground(Color.WHITE);
      _element.setAlignmentX(LEFT_ALIGNMENT);
      _element.setBorder(BorderFactory.createEmptyBorder(0, tGap, tGap, tGap));
      this.add(_element);

      //POWER
      _power = new JLabel("Power: ---");
      _power.setFont(tFont);
      _power.setForeground(Color.WHITE);
      _power.setAlignmentX(LEFT_ALIGNMENT);
      _power.setBorder(BorderFactory.createEmptyBorder(0, tGap, tGap, tGap));
      this.add(_power);

      //HEALTH
      _health = new JLabel("Health: ---");
      _health.setFont(tFont);
      _health.setForeground(Color.WHITE);
      _health.setAlignmentX(LEFT_ALIGNMENT);
      _health.setBorder(BorderFactory.createEmptyBorder(0, tGap, tGap, tGap));
      this.add(_health);
   }
}
