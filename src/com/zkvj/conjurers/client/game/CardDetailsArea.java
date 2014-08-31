package com.zkvj.conjurers.client.game;

import java.awt.Graphics2D;

import com.zkvj.conjurers.core.Constants;
import com.zkvj.utils.BufferedImageComponent;

/**
 * Class responsible for drawing the area which displays a close-up image of the currently
 * selected card.
 */
public class CardDetailsArea extends BufferedImageComponent
{
   private static final long serialVersionUID = -740993523942217182L;
   
   /**
    * Constructor
    */
   public CardDetailsArea()
   {
   }
   
   /**
    * Draws this display component.
    */
   @Override
   public void draw(Graphics2D aG)
   {
      aG.setColor(Constants.kUI_BKGD_COLOR);
      aG.fillRect(10, 10, getWidth()-20, getHeight()-20);
      
//      if(null != _data)
//      {
//         aG.setColor(Color.WHITE);
//
//         if(null != _data.getFocusCard())
//         {
//            aG.drawString(_data.getFocusCard().getName(), 20, 40);
//         }
//         else
//         {
//            aG.drawString("No card selected", 20, 40);
//         }
//      }
//      else
//      {
//         System.out.println("CardDetailsArea.draw(): WARNING: game data was null");
//      }
   }
}
