package Chess;

public class Pawn extends ChessPiece {
    public Pawn(char color) { super(color); }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, ChessBoard boardObj) {
        ChessPiece[][] board = boardObj.board;
        int direction = (color == 'w') ? -1 : 1;
        // Forward move
        if (fromCol == toCol && board[toRow][toCol] == null) {
            if (toRow - fromRow == direction) return true;
            if ((color == 'w' && fromRow == 6 || color == 'b' && fromRow == 1)
                    && toRow - fromRow == 2 * direction && board[fromRow + direction][fromCol] == null)
                return true;
        }
        // Capture
        if (Math.abs(fromCol - toCol) == 1 && toRow - fromRow == direction) {
            if (board[toRow][toCol] != null && board[toRow][toCol].getColor() != color)
                return true;
            // En passant
            if (boardObj.enPassantTarget != null
                    && toRow == boardObj.enPassantTarget[0]
                    && toCol == boardObj.enPassantTarget[1]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() { return color == 'b' ? "\u2659" : "\u265F"; }
    @Override
    public String getType() { return "P"; }
    @Override
    public int getValue() { return 1; }
}