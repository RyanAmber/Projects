package Chess;
import java.util.*;

public class ChessBoardNode {
    private ChessBoard data;
    private List<ChessBoardNode> nextMoves;
    private List<Integer> move;
    private char playerTurn;
    private Map<String, Integer> boardStates;
    private char aiTeam;

    public ChessBoardNode(ChessBoard data, char playerTurn, Map<String, Integer> boardStates, List<Integer> move, char aiTeam) {
        this.data = data;
        this.playerTurn = playerTurn;
        this.nextMoves = new ArrayList<>();
        this.move = move;
        this.boardStates = boardStates;
        this.aiTeam = aiTeam;
    }

    public void addNext(ChessBoardNode node) {
        nextMoves.add(node);
    }

    public List<Integer> getMove() {
        return move;
    }

    // Original entry point - starts alpha-beta with full window
    public double getScoreAtDepth(int depth) {
        return getScoreAtDepth(depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    // Alpha-beta pruning implementation
    // alpha = best score the maximizer (white) is guaranteed so far
    // beta  = best score the minimizer (black) is guaranteed so far
    // If beta <= alpha, the opponent already has a better option elsewhere, so we prune
    public double getScoreAtDepth(int depth, double alpha, double beta) {
        ChessPlayer p = new ChessPlayer(2);
        int moves = data.halfmoveClock;
        double currentScore = p.score(data.getBoard(), aiTeam, moves, p.getWeights(), boardStates);
        if (depth == 0 || nextMoves.isEmpty()) {
            return p.score(data.getBoard(), aiTeam, moves, p.getWeights(), boardStates);
        }

        if (playerTurn == 'w') {
            double bestScore = Double.NEGATIVE_INFINITY;
            for (ChessBoardNode child : nextMoves) {
                child.getAllNextMoves();
                double childScore = child.getScoreAtDepth(depth - 1, alpha, beta);
                double weight = 1.0 / (depth + 1);
                double blended = (1 - weight) * childScore + weight * currentScore;
                bestScore = Math.max(bestScore, blended);
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) {
                    break; // Beta cutoff: black already has a better option, stop searching
                }
            }
            return bestScore;
        } else {
            double bestScore = Double.POSITIVE_INFINITY;
            for (ChessBoardNode child : nextMoves) {
                child.getAllNextMoves();
                double childScore = child.getScoreAtDepth(depth - 1, alpha, beta);
                double weight = 1.0 / (depth + 1);
                double blended = (1 - weight) * childScore + weight * currentScore;
                bestScore = Math.min(bestScore, blended);
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) {
                    break; // Alpha cutoff: white already has a better option, stop searching
                }
            }
            return bestScore;
        }
    }

    public List<ChessBoardNode> getNextMoves() {
        return nextMoves;
    }

    public void getAllNextMoves() {
        this.nextMoves = new ArrayList<>();
        List<List<Integer>> possibleMoves = data.getAllLegalMoves(playerTurn);
        for (List<Integer> move : possibleMoves) {
            ChessBoard newBoard = new ChessBoard();
            newBoard.setupBoard(data.getBoard());
            int startrow = move.get(0);
            int startcol = move.get(1);
            int endrow = move.get(2);
            int endcol = move.get(3);
            ChessPiece[][] tempBoard = newBoard.getBoard();
            tempBoard[endrow][endcol] = tempBoard[startrow][startcol];
            tempBoard[startrow][startcol] = null;
            newBoard.setupBoard(tempBoard);

            Map<String, Integer> newBoardStates = new HashMap<>(boardStates);
            newBoardStates.put(newBoard.toString(), newBoardStates.getOrDefault(newBoard.toString(), 0) + 1);

            ChessBoardNode childNode = new ChessBoardNode(
                newBoard,
                playerTurn == 'w' ? 'b' : 'w',
                newBoardStates,
                move,
                aiTeam
            );
            this.addNext(childNode);
        }
    }
}