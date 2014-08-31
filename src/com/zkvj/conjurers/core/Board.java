package com.zkvj.conjurers.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Class representing the board in a game a Conjurers.
 */
public class Board implements Serializable
{
   private static final long serialVersionUID = 2310506492116570683L;

   /** array of pre-calculated offsets for neighbor hexes */
   private static final Point[] kNeighbors = {new Point( 1, 0),
                                              new Point( 1,-1),
                                              new Point( 0,-1),
                                              new Point(-1, 0),
                                              new Point(-1, 1),
                                              new Point( 0, 1)};
   
   /** map of the wells which make up the board */
   private Map<Point, Well> _wells = new HashMap<Point, Well>();
   
   /** map of the entities on the game board */
   private Map<Point, Entity> _entities = new HashMap<Point, Entity>();
   
   /** player positions */
   private Point _playerPosition;
   private Point _opponentPosition;
   
   /**
    * Constructor
    */
   public Board()
   {
      initializeBoard();
   }
   
   /**
    * Copy constructor
    * @param aBoard
    */
   public Board(Board aBoard)
   {
      _wells = new HashMap<Point, Well>(aBoard._wells);
      _entities = new HashMap<Point, Entity>(aBoard._entities);
      _playerPosition = new Point(aBoard._playerPosition);
      _opponentPosition = new Point(aBoard._opponentPosition);
   }

   /**
    * Prepares the board for a new game.
    */
   public void initializeBoard()
   {
      initializeWells();
      initializePlayerPositions();
   }
   
   /**
    * Prepares positions of the players for a new game.
    */
   public void initializePlayerPositions()
   {
      _playerPosition = Constants.kDEFAULT_PLAYER_POS;
      _opponentPosition = Constants.kDEFAULT_OPPONENT_POS;
   }
   
   /**
    * Prepares the wells on the board for a new game.
    */
   public void initializeWells()
   {
      _wells.clear();
      
      int tNumRings = Constants.kHEX_RINGS;
      for(int tX = 1 - tNumRings; tX < tNumRings; tX++)
      {
         for(int tZ = 1 - tNumRings; tZ < tNumRings; tZ++)
         {
            if(tNumRings > (Math.abs(tX) + Math.abs(tZ) + Math.abs(tX+tZ))/2)
            {
               setWell(tX, tZ, new Well());
            }
         }
      }
   }

   /**
    * Randomizes the wells and player positions on the board.
    */
   public void randomizeBoard()
   {
      randomizeWells();
      randomizePlayerPositions();
   }
   
   /**
    * Randomizes the player positions on the board.
    */
   public void randomizePlayerPositions()
   {
      _playerPosition = new Point(-1 * (int)Math.ceil(3*Math.random()),4);
      _opponentPosition = new Point((int)Math.ceil(3*Math.random()),-4);
   }

   /**
    * Randomizes the wells on the board.
    */
   public void randomizeWells()
   {
      //totally random
      for(Map.Entry<Point, Well>  tEntry: _wells.entrySet())
      {
         tEntry.getValue().setElementType(Element.getRandom());
      }
      
      //put nine wells of each element on the board, leaving the middle space neutral
//      _wells.clear();
//      int tNumRings = Constants.kHEX_RINGS;
//      ArrayList<Element> tList = new ArrayList<Element>();
//      for(int j = 0; j < 4; j++)
//      {
//         for(int i = 0; i < 9; i++)
//         {
//            tList.add(Element.getValues()[j]);
//         }
//      }
//      for(int tX = 1 - tNumRings; tX < tNumRings; tX++)
//      {
//         for(int tZ = 1 - tNumRings; tZ < tNumRings; tZ++)
//         {
//            if(tNumRings > (Math.abs(tX) + Math.abs(tZ) + Math.abs(tX+tZ))/2 &&
//               !(tX == 0 && tZ == 0))
//            {
//               int tIndex = (int) Math.floor(tList.size() * Math.random());
//               Element tElement = tList.remove(tIndex);
//               
//               Well tWell = new Well();
//               tWell.setElementType(tElement);
//
//               setWell(tX, tZ, tWell);
//            }
//         }
//      }
//      setWell(0, 0, new Well());
   }
   
   /**
    * Returns the Hex at the given coordinates
    * @param q
    * @param r
    * @return Well
    */
   public Well getWell(int q, int r)
   {
      return _wells.get(new Point(q,r));
   }
   
   /**
    * Returns the set of all wells which make up the board.
    * @return Set<Map.Entry<Point, Well>>
    */
   public Set<Map.Entry<Point, Well>> getWells()
   {
      return _wells.entrySet();
   }
   
   /**
    * Sets the given Hex at the given coordinates
    * @param q
    * @param r
    * @param aWell
    */
   public void setWell(int q, int r, Well aWell)
   {
      _wells.put(new Point(q,r),aWell);
   }
   
   /**
    * Returns the hex which neighbors the hex specified by (q,r) in the
    * direction specified by dir
    * @param q
    * @param r
    * @param dir
    * @return Hex
    */
   public Well getNeighbor(int q, int r, int dir)
   {
      Well tReturn = null;
      
      if(dir >= 0 && dir < kNeighbors.length)
      {
         tReturn = _wells.get(new Point(q + kNeighbors[dir].x,
                                        r + kNeighbors[dir].y));
      }
      
      return tReturn;
   }

   /**
    * Returns a string representation of the board
    * @return String
    */
   @Override
   public String toString()
   {
      StringBuilder tReturn = new StringBuilder();
      
      tReturn.append("Player is at (").
              append(_playerPosition.x).append(",").
              append(_playerPosition.y).append(")\n");
      
      tReturn.append("Opponent is at (").
              append(_opponentPosition.x).append(",").
              append(_opponentPosition.y).append(")\n");
      
      for(Map.Entry<Point, Well> tEntry : _wells.entrySet())
      {
         tReturn.append("(").append(tEntry.getKey().x).
                 append(",").append(tEntry.getKey().y).append(") = ").
                 append(tEntry.getValue().toString()).append("\n");
      }
      
      return tReturn.toString();
   }

   /**
    * @return the playerPosition
    */
   public Point getPlayerPosition()
   {
      return _playerPosition;
   }

   /**
    * @param aPlayerPosition the playerPosition to set
    */
   public void setPlayerPosition(Point aPlayerPosition)
   {
      _playerPosition = aPlayerPosition;
   }

   /**
    * @return the opponentPosition
    */
   public Point getOpponentPosition()
   {
      return _opponentPosition;
   }

   /**
    * @param aOpponentPosition the opponentPosition to set
    */
   public void setOpponentPosition(Point aOpponentPosition)
   {
      _opponentPosition = aOpponentPosition;
   }

   /**
    * Update this Board object with data from the given object
    * @param aBoard
    */
   public void updateFrom(Board aBoard)
   {
      _wells = aBoard._wells;
      _entities = aBoard._entities;
      _playerPosition = aBoard._playerPosition;
      _opponentPosition = aBoard._opponentPosition;
   }
}