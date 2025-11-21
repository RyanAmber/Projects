package Chess;

public class NullPiece extends ChessPiece {
    public NullPiece(char player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, ChessBoard board) {
        return false;
    }
    public String toString() {
        return ".";
    }
    public String getType() {
        return "N";
    }
    public int getValue() {
        return 0;
    }
    
}
