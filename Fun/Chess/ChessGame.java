package Chess;
import java.util.*;

public class ChessGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Who is player one? [1] Human, [2] Robot, [3] Random");
        ChessPlayer p1 = new ChessPlayer(scanner.nextInt());

        System.out.println("Who is player two? [1] Human, [2] Robot, [3] Random");
        ChessPlayer p2 = new ChessPlayer(scanner.nextInt());

        int w1 = 0;
        int w2 = 0;
        int t = 0;

        for (int i = 0; i <1; i++) {
            System.out.println("Starting game                                                 " + (i + 1));
            char currentPlayer = 'w';
            boolean gameOver = false;
            ChessBoard board = new ChessBoard();
            Map<String, Integer> boardStates = new HashMap<String, Integer>();

            while (!gameOver) {
                board.printBoard();

                if (board.isInCheckmate(currentPlayer)) {
                    System.out.println((currentPlayer == 'w' ? "White" : "Black") + " is in checkmate. Game over!");
                    if (currentPlayer == 'w') {
                        w2++;
                    } else {
                        w1++;
                    }
                    gameOver = true;
                    continue;
                }
                if (boardStates.containsKey(board.toString()) && boardStates.get(board.toString()) >= 3) {
                    System.out.println("Draw by threefold repetition! Game over!");
                    t++;
                    gameOver = true;
                    continue;
                }

                if (board.isInStalemate(currentPlayer)) {
                    System.out.println("Stalemate! Game over!");
                    t++;
                    gameOver = true;
                    continue;
                }

                if (board.isFiftyMoveRule()) {
                    System.out.println("Draw by 50-move rule! Game over!");
                    t++;
                    gameOver = true;                 
                    continue;
                }

                if (board.isInsufficientMaterial()) {
                    System.out.println("Draw by insufficient material! Game over!");
                    t++;
                    gameOver = true;
                    continue;
                }

                if (board.isInCheck(currentPlayer)) {
                    System.out.println((currentPlayer == 'w' ? "White" : "Black") + " is in check!");
                }

                if (!gameOver) {
                    System.out.println((currentPlayer == 'w' ? "White" : "Black") + "'s move.");
                    String[] move;
                    if (currentPlayer == 'w') {
                        move = p1.getMove(board, 'w',boardStates,scanner);
                    } else {
                        move = p2.getMove(board, 'b',boardStates,scanner);
                    }

                    /*
                     try {
                         Thread.sleep(1000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                    */

                    boolean moveResult = board.movePiece(move[0], move[1], currentPlayer, scanner);
                    if (moveResult) {
                        currentPlayer = (currentPlayer == 'w') ? 'b' : 'w';
                        if(boardStates.containsKey(board.toString())) {
                            boardStates.put(board.toString(), boardStates.get(board.toString()) + 1);
                        } else {
                            boardStates.put(board.toString(), 1);
                        }
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                }
            }
        }

        System.out.println("Player one wins: " + w1);
        System.out.println("Player two wins: " + w2);
        System.out.println("Ties: " + t);

        scanner.close();
    }
}
