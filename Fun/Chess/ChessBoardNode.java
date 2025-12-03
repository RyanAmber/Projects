package Chess;
import java.util.*;

public class ChessBoardNode {
    private ChessBoard data;
    private List<ChessBoardNode> nextMoves;
    private List<Integer> move;
    private char playerTurn;
    private Map<String,Integer> boardStates;

    public ChessBoardNode(ChessBoard data, char playerTurn, Map<String,Integer> boardStates,List<Integer> move) {
        this.data = data;
        this.playerTurn = playerTurn;
        this.nextMoves = new ArrayList<>();
        this.move = move;
        this.boardStates = boardStates;
    }
    public void addNext(ChessBoardNode node) {
        nextMoves.add(node);
    }
    public List<Integer> getMove() {
        return move;
    }
    public double getScoreAtDepth(int depth) {
        ChessPlayer p=new ChessPlayer(3);
        int moves=data.halfmoveClock;
        if(depth==0) {
            double score=p.score(data.getBoard(),playerTurn,moves,p.getWeights(),boardStates);
            return score;
        }
        double bestScore = playerTurn == 'w' ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        for (ChessBoardNode child : nextMoves) {
            double childScore = child.getScoreAtDepth(depth - 1);
            if (playerTurn == 'w') {
                bestScore = Math.max(bestScore, childScore);
            } else {
                bestScore = Math.min(bestScore, childScore);
            }
        }
        return bestScore;
    }
    public List<ChessBoardNode> getNextMoves() {
        return nextMoves;
    }
    public void getAllNextMoves(){
        List<List<Integer>> possibleMoves = data.getAllLegalMoves(playerTurn);
        for (List<Integer> move : possibleMoves) {
            ChessBoard newBoard = new ChessBoard();
            newBoard.setupBoard(data.getBoard());
            int startrow=move.get(0);
            int startcol=move.get(1);
            int endrow=move.get(2);
            int endcol=move.get(3);
            ChessPiece[][] tempBoard = newBoard.getBoard();
            tempBoard[endrow][endcol] = tempBoard[startrow][startcol];
            tempBoard[startrow][startcol] = null;
            newBoard.setupBoard(tempBoard);
            boardStates.put(newBoard.toString(), boardStates.getOrDefault(newBoard.board.toString(), 0) + 1);
            ChessBoardNode childNode = new ChessBoardNode(newBoard, playerTurn == 'w' ? 'b' : 'w',boardStates,move);
            this.addNext(childNode);
        }
    }

}
