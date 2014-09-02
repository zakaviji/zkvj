package com.zkvj.conjurers.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Class representing the board in a game a Conjurers.
 */
public class Board implements Serializable
{
   private static final long serialVersionUID = 2310506492116570683L;
   
   /** Constants which define directions relative to a hex */
   public static final int kRIGHT = 0;
   public static final int kUP_RIGHT = 1;
   public static final int kUP_LEFT = 2;
   public static final int kLEFT = 3;
   public static final int kDOWN_LEFT = 4;
   public static final int kDOWN_RIGHT = 5;

   /** array of pre-calculated offsets for neighbor hexes */
   private static final Point[] kNeighborDirections = {new Point( 1, 0),
                                              new Point( 1,-1),
                                              new Point( 0,-1),
                                              new Point(-1, 0),
                                              new Point(-1, 1),
                                              new Point( 0, 1)};
   
   /** map of the wells which make up the board */
   private Map<Point, Well> _wells = new HashMap<Point, Well>();
   
   /** map of the entities on the game board */
   private Map<Point, Minion> _minions = new HashMap<Point, Minion>();
   
   /** player positions */
   private Point _playerA;
   private Point _playerB;
   
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
      _minions = new HashMap<Point, Minion>(aBoard._minions);
      _playerA = new Point(aBoard._playerA);
      _playerB = new Point(aBoard._playerB);
   }

   /**
    * Prepares the board for a new game.
    */
   public void initializeBoard()
   {
      initializeWells();
      initializePlayerPositions();
      
      List<Point> tPlayerNeighbors = getNeighbors(_playerA);
      for(Point tPos : tPlayerNeighbors)
      {
         Well tWell = _wells.get(tPos);
         if(null != tWell)
         {
            _wells.put(tPos, new Well(tWell.elementType, Conjurer.kPLAYER_A));
         }
      }
      
      List<Point> tOpponentNeighbors = getNeighbors(_playerB);
      for(Point tPos : tOpponentNeighbors)
      {
         Well tWell = _wells.get(tPos);
         if(null != tWell)
         {
            _wells.put(tPos, new Well(tWell.elementType, Conjurer.kPLAYER_B));
         }
      }
   }
   
   /**
    * Prepares positions of the players for a new game.
    */
   public void initializePlayerPositions()
   {
      _playerA = Constants.kDEFAULT_PLAYER_A_POS;
      _playerB = Constants.kDEFAULT_PLAYER_B_POS;
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
               setWell(new Point(tX, tZ), new Well());
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
      _playerA = new Point(-1 * (int)Math.ceil(3*Math.random()),4);
      _playerB = new Point((int)Math.ceil(3*Math.random()),-4);
   }

   /**
    * Randomizes the wells on the board.
    */
   public void randomizeWells()
   {
      //totally random
      for(Point tPos: _wells.keySet())
      {
         _wells.put(tPos, new Well(Element.getRandom(), Conjurer.kNONE));
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
    * @return set of all positions on the board
    */
   public Set<Point> getPositions()
   {
      return _wells.keySet();
   }
   
   /**
    * Returns the Well at the given coordinates, if any, else null.
    * @param aPos
    * @return Well
    */
   public Well getWell(Point aPos)
   {
      return _wells.get(aPos);
   }
   
   /**
    * Returns the set of all positions/wells which make up the board.
    * @return Set<Map.Entry<Point, Well>>
    */
   public Set<Map.Entry<Point, Well>> getWells()
   {
      return _wells.entrySet();
   }
   
   /**
    * Sets the given Well at the given position
    * @param aPos
    * @param aWell
    */
   public void setWell(Point aPos, Well aWell)
   {
      _wells.put(aPos,aWell);
   }
   
   /**
    * Returns the position which is a neighbor to the given position in the
    * given direction (i.e. kLEFT, kUP_LEFT, etc...).
    * Returns null if given direction is invalid.
    * @param aPosition
    * @param aDirection
    * @return Point
    */
   public Point getNeighbor(Point aPosition, int aDirection)
   {
      Point tReturn = null;
      
      if(aDirection >= 0 && aDirection < kNeighborDirections.length)
      {
         tReturn = new Point(aPosition.x + kNeighborDirections[aDirection].x,
                             aPosition.y + kNeighborDirections[aDirection].y);
      }
      
      return tReturn;
   }
   
   /**
    * Returns a list of the positions which are neighbors to the given position.
    * @param aPosition
    * @return List<Point>
    */
   public List<Point> getNeighbors(Point aPosition)
   {
      List<Point> tReturn = new ArrayList<Point>();
      
      for(Point tDirection : kNeighborDirections)
      {
         tReturn.add(new Point(aPosition.x + tDirection.x, aPosition.y + tDirection.y));
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
              append(_playerA.x).append(",").
              append(_playerA.y).append(")\n");
      
      tReturn.append("Opponent is at (").
              append(_playerB.x).append(",").
              append(_playerB.y).append(")\n");
      
      for(Map.Entry<Point, Well> tEntry : _wells.entrySet())
      {
         tReturn.append("(").append(tEntry.getKey().x).
                 append(",").append(tEntry.getKey().y).append(") = ").
                 append(tEntry.getValue().toString()).append("\n");
      }
      
      return tReturn.toString();
   }

   /**
    * @return the position of the player with the given ID
    */
   public Point getPlayerPosition(int aPlayerID)
   {
      Point tReturn = null;
      
      if(Conjurer.kPLAYER_A == aPlayerID)
      {
         tReturn = _playerA;
      }
      else if(Conjurer.kPLAYER_B == aPlayerID)
      {
         tReturn = _playerB;
      }
      
      return tReturn;
   }

   /**
    * @param aPlayerID - ID of the player
    * @param aPlayerPosition - position of the player
    */
   public void setPlayerPosition(int aPlayerID, Point aPlayerPosition)
   {
      if(Conjurer.kPLAYER_A == aPlayerID)
      {
         _playerA = aPlayerPosition;
      }
      else if(Conjurer.kPLAYER_B == aPlayerID)
      {
         _playerB = aPlayerPosition;
      }
   }

   /**
    * Update this Board object with data from the given object
    * @param aBoard
    */
   public void updateFrom(Board aBoard)
   {
      _wells = aBoard._wells;
      _minions = aBoard._minions;
      _playerA = aBoard._playerA;
      _playerB = aBoard._playerB;
   }

   /**
    * Returns the minion at the given position on the board, if any, else null.
    * @param aBoard
    * @return Minion
    */
   public Minion getMinion(Point aBoard)
   {
      Minion tReturn = null;
      
      if(null != aBoard)
      {
         tReturn = _minions.get(aBoard);
      }
      
      return tReturn;
   }
}