package com.zkvj.conjurers.display;

import java.awt.Color;
import java.awt.Graphics2D;

import com.zkvj.conjurers.core.Conjurer;
import com.zkvj.conjurers.core.Constants;
import com.zkvj.conjurers.core.GameData;
import com.zkvj.utils.BufferedImageComponent;

/**
 * Class responsible for drawing the area which displays a close-up image of the currently
 * selected card.
 */
public class PlayerDetailsArea extends BufferedImageComponent
{
   private static final long serialVersionUID = -6000029103485694628L;
   
   /** game data */
   private final GameData _data;
   
   /** true for player, false for opponent */
   private boolean _player = true;
   
   /**
    * Constructor
    * @param aData - the game data
    * @param aPlayer - true for the player, false for the opponent
    */
   public PlayerDetailsArea(GameData aData, boolean aPlayer)
   {
      _data = aData;
      _player = aPlayer;
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
         
         Conjurer tPlayer = _data.getOpponent();
         if(_player)
         {
            tPlayer = _data.getPlayer();
         }
         
         int tX = 20;
         int tY = 40;
         int tDy = 30;
         
         aG.drawString(tPlayer.getName(), tX, tY);
         tY += tDy;
         aG.drawString("Health: " + tPlayer.getHealth(), tX, tY);
         tY += tDy;
         aG.drawString("Deck: " + tPlayer.getDeck().size(), tX, tY);
         tY += tDy;
         aG.drawString("Hand: " + tPlayer.getHand().size(), tX, tY);
         tY += tDy;
         aG.drawString("Energy: " + tPlayer.getEnergy(), tX, tY);
      }
      else
      {
         System.out.println("PlayerDetailsArea.draw(): WARNING: game data was null");
      }
   }
}
