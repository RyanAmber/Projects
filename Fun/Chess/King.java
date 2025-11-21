package Chess;

public class King extends ChessPiece {
    public King(char color) { super(color); }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, ChessBoard boardObj) {
        ChessPiece[][] board = boardObj.board;
        int dr = Math.abs(fromRow - toRow), dc = Math.abs(fromCol - toCol);
        if (dr <= 1 && dc <= 1) {
            ChessPiece target = board[toRow][toCol];
            return target == null || target.getColor() != color;
        }
        // Castling
        if (dr == 0 && dc == 2) {
            boolean isWhite = color == 'w';
            boolean kingSide = toCol == 6;
            boolean canCastle = isWhite ? (kingSide ? boardObj.canCastleKingSide[0] : boardObj.canCastleQueenSide[0])
                                        : (kingSide ? boardObj.canCastleKingSide[1] : boardObj.canCastleQueenSide[1]);
            if (!canCastle) return false;
            // Check squares between king and rook
            int row = fromRow;
            int[] cols = kingSide ? new int[]{5, 6} : new int[]{1, 2, 3};
            for (int col : cols)
                if (board[row][col] != null) return false;
            // Check squares not attacked
            int[] kingPath = kingSide ? new int[]{4,5,6} : new int[]{4,3,2};
            for (int col : kingPath)
                if (boardObj.isSquareAttacked(row, col, isWhite ? 'b' : 'w'))
                    return false;
            // Rook must be present
            int rookCol = kingSide ? 7 : 0;
            ChessPiece rook = board[row][rookCol];
            if (!(rook instanceof Rook) || rook.getColor() != color) return false;
            return true;
        }
        return false;
    }

    @Override
    public String toString() { return color == 'b' ? "\u2654" : "\u265A"; }
    @Override
    public String getType() { return "K"; }
    @Override
    public int getValue() { return 10000; }
}