package Chess;

public class Queen extends ChessPiece {
    public Queen(char color) { super(color); }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, ChessBoard boardObj) {
        // Queen = Rook + Bishop
        if (fromRow == toRow || fromCol == toCol) {
            return new Rook(color).isValidMove(fromRow, fromCol, toRow, toCol, boardObj);
        }
        if (Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol)) {
            return new Bishop(color).isValidMove(fromRow, fromCol, toRow, toCol, boardObj);
        }
        return false;
    }

    @Override
    public String toString() { return color == 'b' ? "\u2655" : "\u265B"; }
    @Override
    public String getType() { return "Q"; }
    @Override
    public int getValue() { return 9; }
}
