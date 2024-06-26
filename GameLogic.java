/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.ArrayList;

class CoordPair { //pair object because JAVA doesn't have them built in for some reason
  public int x;
  public int y;
    
  public CoordPair(int x,int y){
    this.x = x;
    this.y = y;
  }
}

public class GameLogic {
  
  public static ArrayList<CoordPair> validMoves(int color, int[][] board){ //returns list of valid moves as int array 
    ArrayList<CoordPair> validList = new ArrayList<CoordPair>();
    for (int ypos = 0; ypos < 8; ypos++){
        for (int xpos = 0; xpos < 8; xpos++){
            if (isValid(color, xpos, ypos, board)) { 
                CoordPair valid = new CoordPair(xpos,ypos);
                validList.add(valid);
            }
        }
    }
    return validList;
  } 
    
    public static boolean isValid(int color,int x, int y, int[][] boardMatrix){  //given color, position and board determine if move is valid
        int oppColor = 1;
        if(color == 1)
            oppColor = 2;
        if(boardMatrix[x][y] != 0)
          return false;
        for(int ypos = -1; ypos <= 1; ypos++)                                    
            for(int xpos = -1; xpos <= 1; xpos++) {
                if(xpos == 0 && ypos == 0) 
                    continue;            
                if(x + xpos < 0 || x + xpos > 7 || y + ypos < 0 || y + ypos > 7) //if adjacent square out of bounds then skip
                    continue;
                
                if(boardMatrix[x+xpos][y+ypos] == oppColor){ //If adjacent square is opposite color-
                    int ix = x+xpos;
                    int iy = y+ypos;
                    while(!(ix + xpos < 0 || ix + xpos > 7 || iy + ypos < 0 || iy + ypos > 7)){ //start search in direction for same color same color if same color
                        //System.out.println("|| checking square[" + ix + "][" + iy + "]");
                        if(boardMatrix[ix][iy] == color)  
                            return true;
                        if(boardMatrix[ix][iy] == 0)
                          break;
                        ix+=xpos;
                        iy+=ypos;
                    }
                }
                
            }
        return false;
    }

    public static int[][] copyBoard(int[][] gameBoard) {
      int[][] resultBoard = new int[8][8];
    
      for (int i = 0; i < 8; i++) {
          for (int j = 0; j < 8; j++) {
              resultBoard[i][j] = gameBoard[i][j];
          }
      }
      return resultBoard;
    }
    
    private static int[][] flipCloneBoardDiscs(int x, int y, int discColor, int[][] Board) {
      int[][] resultBoard = new int [8][8];

       for (int i = 0; i < 8; i++) {
           for (int j = 0; j < 8; j++) {
               resultBoard[i][j] = Board[i][j];
           }
       }
      
       int[][] directions = {
           {-1, -1}, {-1, 0}, {-1, 1},
           {0, -1}, {0, 1},
           {1, -1}, {1, 0}, {1, 1}
       };

       for (int[] dir : directions) {
           int dx = dir[0];
           int dy = dir[1];
           int curX = x + dx;
           int curY = y + dy;

           while (curX >= 0 && curX < 8 && curY >= 0 && curY < 8 && resultBoard[curX][curY] == 3 - discColor) {
               curX += dx;
               curY += dy;
           }

           if (curX >= 0 && curX < 8 && curY >= 0 && curY < 8 && resultBoard[curX][curY] == discColor) {
               int flipX = x + dx;
               int flipY = y + dy;

               while (flipX != curX || flipY != curY) {
                   resultBoard[flipX][flipY] = discColor;
                   flipX += dx;
                   flipY += dy;
                   
               }
           }
       }

       return resultBoard;
   }

   public static int[][] processNewBoard(int[][] Board, int x, int y, int PlayerID) {

    Board[x][y] = PlayerID;
    Board = flipCloneBoardDiscs(x, y, PlayerID, Board);

    return Board;
  
  }


  public static int staticWeightHeuristic(int[][] board){
    int black = 0;
    int white = 0;

    int[][] boardweight = {
      {4, -3, 2, 2, 2, 2, -3, 4},
      {-3,-4, -1 ,-1, -1, -1, -4, -3}, 
      {2, -1, 1, 0, 0, 1, -1, 2},
      {2, -1, 0, 1, 1, 0, -1, 2}, 
      {2, -1, 0, 1, 1, 0, -1, 2},
      {2, -1, 1, 0, 0, 1, -1, 2}, 
      {-3, -4, -1, -1, -1, -1, -4, -3}, 
      {4, -3, 2, 2, 2, 2, -3, 4}
    };

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {   
          if (board[j][i] == 1) {
              black += boardweight[j][i];
          }
          else if (board[j][i] == 2) {
              white += boardweight[j][i];
          }
      }
    }
    return white - black;
  }

  public static int cornerWeightHeuristic(int[][] Board) {
    int black = 0;
    int white = 0;

     for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {   
            if (Board[j][i] == 1) {
                black++;
            }
            else if (Board[j][i] == 2) {
                white++;
            }
        }
    }

    int cornerWeight = 10;

    if (Board[0][0] == 1) {
        black += cornerWeight;
    }

    if (Board[0][7] == 1) {
        black += cornerWeight;
    }

    if (Board[7][0] == 1) {
        black += cornerWeight;
    }

    if (Board[7][7] == 1) {
        black += cornerWeight;
    }

    if (Board[0][0] == 2) {
        white += cornerWeight;
    }

    if (Board[0][7] == 2) {
        white += cornerWeight;
    }

    if (Board[7][0] == 2) {
        white += cornerWeight;
    }

    if (Board[7][7] == 2) {
        black += cornerWeight;
    }
    
    return white - black;
  }

  public static int evaluate(int[][] boardState, int heuristic){ //heuristic numbers 1 = corner weight, 2 = static weight
    if(heuristic == 1)
      return cornerWeightHeuristic(boardState);
    if(heuristic == 2)
      return staticWeightHeuristic(boardState);
    return 0;
  }

  private static void printBoardState(int[][] boardState) { //General version of printboard for debugging
    System.out.print("---------------------\n");
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            System.out.print(" " + boardState[j][i]);
        }
        System.out.println();
    }
    System.out.print("---------------------\n");
  }

  public static int[] miniMax(int[][] game, int depth, Boolean maximizing, int heuristic) {  //black minimizing, white maximizing
    int[] result = new int[4];     //initialize values for return result[0] = best value, result[1] = xcoord, result[2] = ycoord

    if (depth == 0) {              //recursion end condition, once end of tree reached return value of board 
        result[0] = evaluate(game, heuristic);
        result[3] = 1;
        return result;
    }

    if (maximizing) {            
      int bestValue = Integer.MIN_VALUE;
      ArrayList<CoordPair> validList = validMoves(2, game); // generate all possible moves for player
      for (int i = 0; i < validList.size(); i++) {                //iterate through list of valid moves  
        int[][] newStateBoard = copyBoard(game);
        newStateBoard = processNewBoard(newStateBoard, validList.get(i).x, validList.get(i).y, 2); //create new state board from valid move
             
        int[] value = miniMax(newStateBoard, depth - 1, false, heuristic);   
        result[3] += value[3]; //add amount of interations from child nods                                     //recursive call to find best move from valid move


        if (value[0] > bestValue) {  //returns coorinates of move as well. we dont need xy coordinates until depth 1 but we can use the values for tracing the path.                                         
          result[0] = value[0];
          bestValue = value[0];
          result[1] = validList.get(i).x;
          result[2] = validList.get(i).y;
        }
      }  
       return result;  
    }    
    else {    //minimizing
      int bestValue = Integer.MAX_VALUE;
      ArrayList<CoordPair> validList = validMoves(1, game); // generate all possible moves for player
      for (int i = 0; i < validList.size(); i++) {                //iterate through list of valid moves  
        int[][] newStateBoard = copyBoard(game);
        newStateBoard = processNewBoard(newStateBoard, validList.get(i).x, validList.get(i).y, 1); //create new state board from valid move
                 
        int[] value = miniMax(newStateBoard, depth - 1, true, heuristic);                                        //recursive call to find best move from valid move
        result[3] += value[3]; //add amount of interations from child nods

        if (value[0] < bestValue) {  //returns coorinates of move as well. we dont need xy coordinates until depth 1 but we can use the values for tracing the path.                                         
          result[0] = value[0];
          bestValue = value[0];
          result[1] = validList.get(i).x;
          result[2] = validList.get(i).y;
        }
      }
      return result; 
    }
  } 

  public static int[] miniMaxAB(int[][] game, int depth, Boolean maximizing, int alpha, int beta, int heuristic) {  //black minimizing, white maximizing 
    int[] result = new int[4];     //initialize values for return result[0] = best value, result[1] = xcoord, result[2] = ycoord, result[3] = iterations
    if (depth == 0) {              //recursion end condition, once end of tree reached return value of board 
        result[0] = evaluate(game, heuristic);
        result[3] = 1;
        return result;
    }

    if (maximizing) {            
      int bestValue = Integer.MIN_VALUE;
      ArrayList<CoordPair> validList = validMoves(2, game); // generate all possible moves for player
      for (int i = 0; i < validList.size(); i++) {                //iterate through list of valid moves  
        int[][] newStateBoard = copyBoard(game);
        newStateBoard = processNewBoard(newStateBoard, validList.get(i).x, validList.get(i).y, 2); //create new state board from valid move
                 
        int[] value = miniMaxAB(newStateBoard, depth - 1, false, bestValue, beta, heuristic); //recursive call to find best move from valid move, important-next call is for minimizing player
        result[3] += value[3]; //add amount of interations from child nods

        if (value[0] > bestValue) {  //returns coorinates of move as well. we dont need xy coordinates until depth 1 but we can use the values for tracing the path.                                         
          bestValue = value[0];
          result[0] = value[0];
          result[1] = validList.get(i).x;
          result[2] = validList.get(i).y;
        }
        if(bestValue >= beta){  //if max node finds value greater than beta value stop search early/prune and return the value
          System.out.println("Pruning because returned value: " + bestValue + " beta: " + beta);
          result[0] = bestValue;
          result[1] = validList.get(i).x;
          result[2] = validList.get(i).y;
          return result;
        }
      }
      return result;  
    }    
    else {    //minimizing
      int bestValue = Integer.MAX_VALUE;
      ArrayList<CoordPair> validList = validMoves(1, game); // generate all possible moves for player
      for (int i = 0; i < validList.size(); i++) {                //iterate through list of valid moves  
        int[][] newStateBoard = copyBoard(game);
        newStateBoard = processNewBoard(newStateBoard, validList.get(i).x, validList.get(i).y, 1); //create new state board from valid move
                 
        int[] value = miniMaxAB(newStateBoard, depth - 1, true, alpha, bestValue,heuristic);                                        //recursive call to find best move from valid move
        result[3] += value[3];

        if (value[0] < bestValue) {  //returns coorinates of move as well. we dont need xy coordinates until depth 1 but we can use the values for tracing the path.                                         
          bestValue = value[0];
          result[0] = value[0];
          result[1] = validList.get(i).x;
          result[2] = validList.get(i).y;
        }
        if(bestValue <= alpha){  //if min node finds value lesser than alpha value stop search early/prune and return the value
          System.out.println("Pruning because returned value: " + bestValue + " alpha: " + alpha);
          result[0] = bestValue;
          result[1] = validList.get(i).x;
          result[2] = validList.get(i).y;
          return result;
        }
      }
      return result; 
    }
  } 
      
         
}


