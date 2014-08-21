package com.zkvj.games.othello;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.zkvj.utils.FrameUtil;

/**
 * This class is an implementation of the board game Othello.
 * 
 * @author zakaviji
 */
public class Othello extends JPanel
{
   /** serial version UID */
   private static final long serialVersionUID = 8339412871662367521L;
   
   /** panel dimensions */
   private static final int kWIDTH = 400;
   private static final int kHEIGHT = 400;
   
   /** size of the game board (always square) */
   private static final int kBOARD_SIZE = 8;
   
   /** the image which is drawn to the screen */
   private final BufferedImage _image;

   /** the graphics object for our image */
   private final Graphics2D _imageGraphics;

   /** game state */
   private boolean _isWhiteTurn, _gameOver;

   /** current number of pieces for each color */
   private int _whiteCount, _blackCount;

   /** Two-dimensional array of pieces representing the game board */
   private Piece[][] _board = new Piece[kBOARD_SIZE][kBOARD_SIZE];
   
   /** Mouse listener */
   private final MouseListener _mouseListener = new MouseListener()
   {
      @Override
      public void mousePressed(MouseEvent aEvent)
      {
         int x = aEvent.getX();
         int y = aEvent.getY();

         x = (x - 30) / 40;
         y = (y - 52) / 40;

         // place a new, real piece if that move is legal
         if(x > -1 && x < kBOARD_SIZE && 
            y > -1 && y < kBOARD_SIZE &&
            isLegalMove(x, y))
         {
            _board[x][y] = new Piece(_isWhiteTurn, true);
            
            if(_isWhiteTurn)
               _whiteCount++;
            else
               _blackCount++;
            
            flipPieces(x, y);
            
            //next player's turn
            _isWhiteTurn = !_isWhiteTurn;
         }

         //game over condition
         if(_whiteCount + _blackCount >= kBOARD_SIZE * kBOARD_SIZE)
         {
            _gameOver = true;
         }

         updateBufferedImage();
         repaint();
      }

      @Override
      public void mouseReleased(MouseEvent aEvent){}

      @Override
      public void mouseClicked(MouseEvent aEvent){}

      @Override
      public void mouseEntered(MouseEvent aEvent){}

      @Override
      public void mouseExited(MouseEvent aEvent){}
   };
   
   /** Mouse motion listener */
   private final MouseMotionListener _mouseMotionListener = new MouseMotionListener()
   {
      @Override
      public void mouseDragged(MouseEvent aEvent){}

      @Override
      public void mouseMoved(MouseEvent aEvent)
      {
         int x = aEvent.getX();
         int y = aEvent.getY();

         x = (x - 30) / 40;
         y = (y - 52) / 40;

         // clear any other non-real pieces showing
         normalize();

         // place a non-real piece at the current position if legal
         if(x > -1 && x < kBOARD_SIZE && 
            y > -1 && y < kBOARD_SIZE &&
            isLegalMove(x, y))
         {
            _board[x][y] = new Piece(_isWhiteTurn, false);
         }

         updateBufferedImage();
         repaint();
      }
   };
   
   /** Key listener */
   private final KeyListener _keyListener = new KeyListener()
   {
      @Override
      public void keyTyped(KeyEvent aEvent)
      {
         int k = aEvent.getKeyCode();
         if(k == KeyEvent.VK_N && _gameOver)
         {
            newGame();
         }
      }
      
      @Override
      public void keyReleased(KeyEvent aEvent){}
      
      @Override
      public void keyPressed(KeyEvent aEvent){}
   };

   /**
    * Constructor
    */
   public Othello()
   {
      _image = new BufferedImage(kWIDTH, kHEIGHT, BufferedImage.TYPE_INT_RGB);
      _imageGraphics = (Graphics2D)_image.getGraphics();
      _imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);

      this.setPreferredSize(new Dimension(kWIDTH, kHEIGHT));
      this.setFocusable(true);

      addMouseListener(_mouseListener);
      addMouseMotionListener(_mouseMotionListener);
      addKeyListener(_keyListener);
      
      newGame();
   }

   /**
    * Main
    * @param args
    */
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            JFrame tFrame = FrameUtil.openInJFrame(new Othello(),
                                                   "Othello");
            tFrame.setVisible(true);
         }
      });
   }

   /**
    * Clears the image graphics
    */
   private void clearBufferedImage()
   {
      _imageGraphics.setColor(Color.BLACK);
      _imageGraphics.fillRect(0, 0, kWIDTH, kHEIGHT);
   }

   /**
    * Generates the image to be drawn based on the game state.
    */
   private void updateBufferedImage()
   {
      clearBufferedImage();
      
      drawBoard(_imageGraphics);
      drawPieces(_imageGraphics);

      _imageGraphics.setColor(Color.white);
      _imageGraphics.drawString("White: " + _whiteCount + 
                                "; Black: " + _blackCount, 40, 395);
      
      if(_gameOver)
      {
         if(_whiteCount > _blackCount)
         {
            _imageGraphics.drawString("White wins!", 260, 395);
         }
         else
         {
            _imageGraphics.drawString("Black wins!", 260, 395);
         }
      }
   }

   /**
    * Paints the buffered image to the screen.
    *
    * @param aG - screen graphics
    */
   @Override
   public void paintComponent(Graphics aG)
   {
      super.paintComponent(aG);
      aG.drawImage(_image, 0, 0, null);
   }

   /**
    * Flip the pieces which are affected by the piece at the given location.
    * 
    * @param x
    * @param y
    */
   public void flipPieces(int x, int y)
   {
      for(int i = 0; i < kBOARD_SIZE; i++)
      {
         for(int j = 0; j < kBOARD_SIZE; j++)
         {
            // if (i,j) is next to (x,y)
            if(Math.abs(x - i) <= 1 && Math.abs(y - j) <= 1)
            {
               if(_board[i][j] != null)
               {
                  // if that piece is the opposite color
                  if(_board[i][j]._isWhite != _isWhiteTurn)
                  {
                     int a = i + (i - x);
                     int b = j + (j - y);
                     
                     // while (a,b) is within bounds
                     while(a > -1 && a < 8 && b > -1 && b < 8)
                     {
                        if(_board[a][b] != null)
                        {
                           // if piece at (a,b) is same color
                           if(_board[a][b]._isWhite == _isWhiteTurn)
                           {
                              // until gets back to (i,j)
                              // move back towards (i,j), flipping each piece
                              while(a != i || b != j)
                              {
                                 a -= (i - x);
                                 b -= (j - y);
                                 _board[a][b].flip();
                                 if(_isWhiteTurn)
                                 {
                                    _whiteCount++;
                                    _blackCount--;
                                 }
                                 else
                                 {
                                    _blackCount++;
                                    _whiteCount--;
                                 }
                              }
                              break;
                           }
                        }
                        else
                           break;

                        a += (i - x);
                        b += (j - y);
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Check to see if the given location is a legal move.
    * 
    * @param x
    * @param y
    * @return boolean
    */
   public boolean isLegalMove(int x, int y)
   {
      //not a legal move if there is already a real piece here
      if(null != _board[x][y] &&
         _board[x][y]._isReal)
      {
         return false;
      }

      for(int i = 0; i < kBOARD_SIZE; i++)
      {
         for(int j = 0; j < kBOARD_SIZE; j++)
         {
            if(Math.abs(x - i) <= 1 && Math.abs(y - j) <= 1)
            {
               if(null != _board[i][j] && 
                  _board[i][j]._isWhite != _isWhiteTurn)
               {
                  int a = i + (i - x);
                  int b = j + (j - y);
                  while(a > -1 && a < kBOARD_SIZE && b > -1 && b < kBOARD_SIZE)
                  {
                     if(null != _board[a][b])
                     {
                        if(_board[a][b]._isWhite == _isWhiteTurn)
                        {
                           return true;
                        }
                     }
                     //empty spot, not a legal move according to this direction
                     else
                     {
                        break;
                     }

                     a += (i - x);
                     b += (j - y);
                  }
               }
            }
         }
      }

      return false;
   }

   /**
    * Clears any space with a non-real piece.
    * 
    * @param x
    * @param y
    */
   public void normalize()
   {
      for(int i = 0; i < kBOARD_SIZE; i++)
      {
         for(int j = 0; j < kBOARD_SIZE; j++)
         {
            if(null != _board[i][j] && 
               !_board[i][j]._isReal)
            {
                  _board[i][j] = null;
            }
         }
      }
   }

   /**
    * Renders the pieces to the given graphics context.
    * 
    * @param aG
    */
   public void drawPieces(Graphics2D aG)
   {
      Color tPieceColor;
      
      for(int i = 0; i < kBOARD_SIZE; i++)
      {
         for(int j = 0; j < kBOARD_SIZE; j++)
         {
            if(null != _board[i][j])
            {
               if(_board[i][j]._isWhite)
               {
                  tPieceColor = new Color(200, 200, 200);
               }
               else
               {
                  tPieceColor = new Color(30, 30, 30);
               }

               for(int c = 0; c < 10; c++)
               {
                  aG.setColor(tPieceColor);
                  aG.fillOval(33 + 40 * i + c,
                              55 + 40 * j + c,
                              34 - 3 * c,
                              34 - 3 * c);
                  tPieceColor = new Color(tPieceColor.getRed() + 5,
                                          tPieceColor.getGreen() + 5,
                                          tPieceColor.getBlue() + 5);
               }
            }
         }
      }
   }

   /**
    * Renders the game board to given graphics context.
    * 
    * @param aG
    */
   public void drawBoard(Graphics2D aG)
   {
      aG.setColor(new Color(205, 133, 0));
      aG.fillRect(10 + 15, 10 + 37, 330, 330);

      aG.setColor(Color.GREEN.darker());
      aG.fillRect(10 + 20, 10 + 42, 320, 320);

      aG.setColor(Color.BLACK);
      aG.drawRect(10 + 20, 10 + 42, 320, 320);
      aG.drawLine(10 + 15, 10 + 37, 10 + 20, 10 + 42);
      aG.drawLine(10 + 15, 377, 10 + 20, 372);
      aG.drawLine(350, 372, 355, 377);
      aG.drawLine(350, 10 + 42, 355, 10 + 37);

      for(int i = 0; i < kBOARD_SIZE; i++)
      {
         aG.drawLine(30 + 40 * i, 52, 30 + 40 * i, 372);// vertical
         aG.drawLine(30, 52 + 40 * i, 350, 52 + 40 * i);// horizontal
      }
   }

   /**
    * Resets the game in preparation for a new game.
    */
   public void newGame()
   {
      _isWhiteTurn = true;
      _gameOver = false;
      
      for(int i = 0; i < kBOARD_SIZE; i++)
      {
         for(int j = 0; j < kBOARD_SIZE; j++)
         {
            _board[i][j] = null;
         }
      }
      
      _board[3][3] = new Piece(true, true);
      _board[3][4] = new Piece(false, true);
      _board[4][3] = new Piece(false, true);
      _board[4][4] = new Piece(true, true);
      
      _whiteCount = 2;
      _blackCount = 2;
      
      updateBufferedImage();
      repaint();
   }
}