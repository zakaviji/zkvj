package com.zkvj.utils;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public abstract class BufferedImageComponent extends JComponent
{
   private static final long serialVersionUID = -921456873583346218L;
   
   /** display image and graphics */
   private BufferedImage _image;
   private Graphics2D _imageGraphics;
   
   /**
    * Constructor
    */
   public BufferedImageComponent()
   {
      setBufferedImageSize(new Dimension(1, 1));
   }
   
   /**
    * Clears the image graphics
    */
   private void clear()
   {
      _imageGraphics.setComposite(AlphaComposite.Clear);
      _imageGraphics.fillRect(0, 0, _image.getWidth(), _image.getHeight());
      _imageGraphics.setComposite(AlphaComposite.SrcOver);
   }
   
   /**
    * Implement this method to define how to draw this component.
    * @param aG - the graphics object
    */
   abstract protected void draw(Graphics2D aG);
   
   /**
    * Paints this display component by drawing the buffered image.
    */
   @Override
   protected void paintComponent(Graphics aG)
   {
      super.paintComponent(aG);
      aG.drawImage(_image, 0, 0, null);
   }
   
   /**
    * Sets the size of the buffered image.
    * @param aDim - new dimensions of the image 
    */
   public final void setBufferedImageSize(Dimension aDim)
   {
      _image = new BufferedImage(aDim.width, aDim.height,
                                 BufferedImage.TYPE_INT_ARGB);
      _imageGraphics = (Graphics2D) _image.getGraphics();
      _imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);
      updateBufferedImage();
   }
   
   /**
    * Updates the buffered image in the background, in preparation for the next call to paint.
    */
   public void updateBufferedImage()
   {
      clear();
      draw(_imageGraphics);
   }
}
