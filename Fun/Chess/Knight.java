package Chess;

public class Knight extends ChessPiece {
    public Knight(char color) { super(color); }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, ChessBoard boardObj) {
        int dr = Math.abs(fromRow - toRow), dc = Math.abs(fromCol - toCol);
        if (dr * dc == 2) {
            ChessPiece target = boardObj.board[toRow][toCol];
            return target == null || target.getColor() != color;
        }
        return false;
    }

    @Override
    public String toString() { return color == 'b' ? "\u2658" : "\u265E"; }
    @Override
    public String getType() { return "N"; }
    @Override
    public int getValue() { return 3; }
}