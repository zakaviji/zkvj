package com.zkvj.conjurers.display;

import java.awt.Color;
import java.awt.Graphics2D;

import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameData;
import com.zkvj.utils.BufferedImageComponent;

/**
 * Class responsible for drawing the area which displays a .
 */
public class HandDisplayArea extends BufferedImageComponent
{
   private static final long serialVersionUID = 8520685473956498948L;
   
   /** game data */
   private final GameData _data;
   
   /**
    * Constructor
    * @param aData - the game data
    */
   public HandDisplayArea(GameData aData)
   {
      _data = aData;
   }
   
   /**
    * Draws this display component.
    */
   @Override
   public void draw(Graphics2D aG)
   {
      aG.setColor(Constants.kUI_BKGD_COLOR);
      aG.fillRect(10, 10, getWidth()-20, getHeight()-20);
      
      if(null != _data)
      {
         aG.setColor(Color.WHITE);
         
         aG.drawString("Hand size: " + _data.getPlayer().getHand().size(), 20, 40);
      }
      else
      {
         System.out.println("CardDetailsArea.draw(): WARNING: game data was null");
      }
   }
}
