package Chess;

import java.util.*;

public class ChessBoard {
    ChessPiece[][] board = new ChessPiece[8][8];
    // Castling rights and en passant
    boolean[] canCastleKingSide = {true, true}; // [white, black]
    boolean[] canCastleQueenSide = {true, true};
    int[] kingPosition = {7, 4, 0, 4}; // [wRow, wCol, bRow, bCol]
    int[] enPassantTarget = null; // {row, col}
    int halfmoveClock = 0;
    int fullmoveNumber = 1;

    public ChessBoard() { setupPieces(); }
    /*private void setupTestBoard() {
        // Example test setup
        board[7][4] = new King('w');
        board[0][4] = new King('b');
        board[5][5] = new Bishop('w');
        board[1][1] =new Rook('b');
        board[2][1]=new Pawn('b');
        board[1][2]=new Pawn('b');

        kingPosition = new int[]{7, 4, 0, 4};
    }*/
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(board[i][j] == null ? "." : board[i][j].toString());
            }
            sb.append("/");
        }
        return sb.toString();
    }
    private void setupPieces() {
        // Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn('b');
            board[6][i] = new Pawn('w');
        }
        // Major pieces
        board[0][0] = new Rook('b');   board[0][7] = new Rook('b');
        board[7][0] = new Rook('w');   board[7][7] = new Rook('w');
        board[0][1] = new Knight('b'); board[0][6] = new Knight('b');
        board[7][1] = new Knight('w'); board[7][6] = new Knight('w');
        board[0][2] = new Bishop('b'); board[0][5] = new Bishop('b');
        board[7][2] = new Bishop('w'); board[7][5] = new Bishop('w');
        board[0][3] = new Queen('b');  board[0][4] = new King('b');
        board[7][3] = new Queen('w');  board[7][4] = new King('w');
    }
    public void setupBoard(ChessPiece[][] newBoard) {
        board = new ChessPiece[8][8];
        kingPosition=new int[]{-1,-1,-1,-1};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = newBoard[i][j];
                if (board[i][j] instanceof King) {
                    if (board[i][j].getColor() == 'w') {
                        kingPosition[0] = i;
                        kingPosition[1] = j;
                    } else if(board[i][j].getColor() == 'b'){
                        kingPosition[2] = i;
                        kingPosition[3] = j;
                    }
                }
            }
        }
    }

    public boolean movePiece(String from, String to, char player, Scanner scanner) {
        int[] fromIdx = parsePosition(from);
        int[] toIdx = parsePosition(to);
        int end1=toIdx[0];
        int end2=toIdx[1];
        ChessPiece endPiece=board[end1][end2];
        if (fromIdx == null || toIdx == null) return false;
        
        ChessPiece piece = board[fromIdx[0]][fromIdx[1]];
        if (piece == null || piece.getColor() != player) return false;

        // Save state for undo
        ChessPiece[][] snapshot = copyBoard();
        boolean[] castleK = canCastleKingSide.clone();
        boolean[] castleQ = canCastleQueenSide.clone();
        int[] kingPosSnap = kingPosition.clone();
        int[] enPassantSnap = enPassantTarget == null ? null : enPassantTarget.clone();

        boolean valid = piece.isValidMove(fromIdx[0], fromIdx[1], toIdx[0], toIdx[1], this);
        if (!valid) return false;

        // Handle special moves
        boolean isCastling = false, isPromotion = false, isEnPassant = false;
        if(isCastling && isPromotion && isEnPassant){
            isCastling=false;
        }
        // Castling
        if (piece instanceof King && Math.abs(fromIdx[1] - toIdx[1]) == 2) {
            isCastling = true;
            doCastling(fromIdx, toIdx, player);
        }
        // En Passant
        else if (piece instanceof Pawn &&
                enPassantTarget != null &&
                toIdx[0] == enPassantTarget[0] && toIdx[1] == enPassantTarget[1]) {
            isEnPassant = true;
            board[toIdx[0]][toIdx[1]] = piece;
            board[fromIdx[0]][fromIdx[1]] = null;
            board[fromIdx[0]][toIdx[1]] = null; // capture
        }
        // Promotion
        else if (piece instanceof Pawn &&
                (toIdx[0] == 0 || toIdx[0] == 7)) {
            isPromotion = true;
            board[toIdx[0]][toIdx[1]] = piece;
            board[fromIdx[0]][fromIdx[1]] = null;
        }
        // Normal move
        else {
            board[toIdx[0]][toIdx[1]] = piece;
            board[fromIdx[0]][fromIdx[1]] = null;
        }

        // Update king position
        if (piece instanceof King) {
            if (player == 'w') { kingPosition[0] = toIdx[0]; kingPosition[1] = toIdx[1]; }
            else { kingPosition[2] = toIdx[0]; kingPosition[3] = toIdx[1]; }
            // Update castling rights
            if (player == 'w') { canCastleKingSide[0] = false; canCastleQueenSide[0] = false; }
            else { canCastleKingSide[1] = false; canCastleQueenSide[1] = false; }
        }
        // Update rook moves for castling rights
        if (piece instanceof Rook) {
            if (fromIdx[0] == 7 && fromIdx[1] == 0) canCastleQueenSide[0] = false;
            if (fromIdx[0] == 7 && fromIdx[1] == 7) canCastleKingSide[0] = false;
            if (fromIdx[0] == 0 && fromIdx[1] == 0) canCastleQueenSide[1] = false;
            if (fromIdx[0] == 0 && fromIdx[1] == 7) canCastleKingSide[1] = false;
        }
        // Update en passant target
        if (piece instanceof Pawn && Math.abs(fromIdx[0] - toIdx[0]) == 2) {
            enPassantTarget = new int[] {(fromIdx[0] + toIdx[0]) / 2, fromIdx[1]};
        } else {
            enPassantTarget = null;
        }

        // Check for self-check
        if (isInCheck(player)) {
            // Undo move
            board = snapshot;
            canCastleKingSide = castleK;
            canCastleQueenSide = castleQ;
            kingPosition = kingPosSnap;
            enPassantTarget = enPassantSnap;
            return false;
        }

        // Promotion input
        if (isPromotion) {
            System.out.print("Promote to (Q, R, B, N): ");
            //String promo = scanner.next().toUpperCase();
            String promo = "Q"; // Auto-queen for simplicity
            ChessPiece promoted = switch (promo) {
                case "Q" -> new Queen(player);
                case "R" -> new Rook(player);
                case "B" -> new Bishop(player);
                case "N" -> new Knight(player);
                default -> new Queen(player);
            };
            board[toIdx[0]][toIdx[1]] = promoted;
        }
        halfmoveClock = (piece instanceof Pawn || (endPiece != null)) ? 0 : halfmoveClock + 1;
        System.out.println(halfmoveClock);
        if (player == 'b') fullmoveNumber++;
        return true;
    }

    // Castling logic
    private void doCastling(int[] fromIdx, int[] toIdx, char player) {
        int row = fromIdx[0];
        if (toIdx[1] == 6) { // King side
            board[row][6] = board[row][4];
            board[row][4] = null;
            board[row][5] = board[row][7];
            board[row][7] = null;
        } else if (toIdx[1] == 2) { // Queen side
            board[row][2] = board[row][4];
            board[row][4] = null;
            board[row][3] = board[row][0];
            board[row][0] = null;
        }
    }

    // Simple deep copy for undo
    public ChessPiece[][] copyBoard() {
        ChessPiece[][] copy = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                copy[i][j] = board[i][j] == null ? null : board[i][j].clone();
        return copy;
    }

    public boolean isInCheck(char player) {
        int kRow = player == 'w' ? kingPosition[0] : kingPosition[2];
        int kCol = player == 'w' ? kingPosition[1] : kingPosition[3];
        return isSquareAttacked(kRow, kCol, player == 'w' ? 'b' : 'w');
    }

    public boolean isSquareAttacked(int row, int col, char byPlayer) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null && board[i][j].getColor() == byPlayer){
                    if((board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1)){
                        return true;
                    }
                    else if (!(board[i][j] instanceof King)&&board[i][j].isValidMove(i, j, row, col, this))
                        return true;
                }
        return false;
    }
    public int minSquareAttacked(int row, int col, char byPlayer) {
        int min=Integer.MAX_VALUE;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null && board[i][j].getColor() == byPlayer){
                    if((board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1)){
                        min=Math.min(min, board[i][j].getValue());
                    }
                    else if (!(board[i][j] instanceof King)&&board[i][j].isValidMove(i, j, row, col, this))
                        min=Math.min(min, board[i][j].getValue());
                }
        return min;
    }
    public boolean isSquareDefended(int row, int col, char byPlayer) {
        if (board[row][col] == null || board[row][col].getColor() != byPlayer) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].getColor() == byPlayer) {
                    if (i == row && j == col) continue; // skip the piece on the square itself
                    // Temporarily remove the piece at (row, col) to allow isValidMove to work as intended
                    ChessPiece original = board[row][col];
                    board[row][col] = new NullPiece(byPlayer=='w'?'b':'w');
                    boolean defended = (board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1) ||
                        (!(board[i][j] instanceof King) && board[i][j].isValidMove(i, j, row, col, this));
                    board[row][col] = original;
                    if (defended) { 
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public int numSquareAttacked(int row, int col, char byPlayer) {
        int count=0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null && board[i][j].getColor() == byPlayer){
                    if((board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1)){
                        count++;
                    }
                    else if (!(board[i][j] instanceof King)&&board[i][j].isValidMove(i, j, row, col, this))
                        count++;
                }
        return count;
    }
    public int numSquareDefended(int row, int col, char byPlayer) {
        int count=0;
        if (board[row][col] == null || board[row][col].getColor() != byPlayer) {
            return 0;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].getColor() == byPlayer) {
                    if (i == row && j == col) continue;// skip the piece on the square itself
                    // Temporarily remove the piece at (row, col) to allow isValidMove to work as intended
                    ChessPiece original = board[row][col];
                    board[row][col] = new NullPiece(byPlayer=='w'?'b':'w');
                    boolean defended = (board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1) ||
                        (!(board[i][j] instanceof King) && board[i][j].isValidMove(i, j, row, col, this));
                    board[row][col] = original;
                    if (defended) { 
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public boolean isInCheckmate(char player) {
        if (!isInCheck(player)) return false;
        return !hasLegalMoves(player);
    }
    public boolean isFiftyMoveRule() {
        return halfmoveClock > 100;
    }
    public boolean isInsufficientMaterial() {
        List<ChessPiece> pieces = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null)
                    pieces.add(board[i][j]);
        if (pieces.size() == 2) return true; // Only kings
        if (pieces.size() == 3) {
            for (ChessPiece p : pieces)
                if (p instanceof Bishop || p instanceof Knight)
                    return true;
        }
        if (pieces.size() == 4) {
            int bishopCount = 0;
            for (ChessPiece p : pieces)
                if (p instanceof Bishop)
                    bishopCount++;
            if (bishopCount == 2) {
                // Check if both bishops are on same color
                int b1Row = -1, b1Col = -1, b2Row = -1, b2Col = -1;
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++)
                        if (board[i][j] instanceof Bishop) {
                            if (b1Row == -1) { b1Row = i; b1Col = j; }
                            else { b2Row = i; b2Col = j; }
                        }
                return ((b1Row + b1Col) % 2) == ((b2Row + b2Col) % 2);
            }
        }
        return false;
    }
    public boolean isInStalemate(char player) {
        if (isInCheck(player)) return false;
        //System.out.println("Checking for stalemate for player " + player);
        return !hasLegalMoves(player);
    }

    private boolean hasLegalMoves(char player) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null && board[i][j].getColor() == player)
                    for (int r = 0; r < 8; r++)
                        for (int c = 0; c < 8; c++) {
                            ChessPiece[][] snapshot = copyBoard();
                            boolean[] castleK = canCastleKingSide.clone();
                            boolean[] castleQ = canCastleQueenSide.clone();
                            int[] kingPosSnap = kingPosition.clone();
                            int[] enPassantSnap = enPassantTarget == null ? null : enPassantTarget.clone();
                            if (i == r && j == c) continue;
                            if (board[i][j].isValidMove(i, j, r, c, this)) {
                                //ChessPiece target = board[r][c];
                                board[r][c] = board[i][j];
                                board[i][j] = null;
                                if (board[r][c] instanceof King) {
                                    if (player == 'w') { kingPosition[0] = r; kingPosition[1] = c; }
                                    else { kingPosition[2] = r; kingPosition[3] = c; }
                                }
                                boolean legal = !isInCheck(player);
                                // Undo
                                board = snapshot;
                                canCastleKingSide = castleK;
                                canCastleQueenSide = castleQ;
                                kingPosition = kingPosSnap;
                                enPassantTarget = enPassantSnap;

                                if (legal) {
                                    //System.out.println("Legal move found: " + (char)('a' + j) + (8 - i) + " to " + (char)('a' + c) + (8 - r));
                                    return true;
                                }
                            }
                        }
        return false;
    }
    public List<List<Integer>> getAllLegalMoves(char player) {
        List<List<Integer>> allMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                if (piece != null && piece.getColor() == player) {
                    for (int r = 0; r < 8; r++) {
                        for (int c = 0; c < 8; c++) {
                            if (i == r && j == c) continue;
                            if (piece.isValidMove(i, j, r, c, this)) {
                                // Save state
                                ChessPiece[][] snapshot = copyBoard();
                                boolean[] castleK = canCastleKingSide.clone();
                                boolean[] castleQ = canCastleQueenSide.clone();
                                int[] kingPosSnap = kingPosition.clone();
                                int[] enPassantSnap = enPassantTarget == null ? null : enPassantTarget.clone();

                                // Simulate move
                                ChessPiece movedPiece = board[i][j];
                                //ChessPiece captured = board[r][c];
                                board[r][c] = movedPiece;
                                board[i][j] = null;
                                if (movedPiece instanceof King) {
                                    if (player == 'w') { kingPosition[0] = r; kingPosition[1] = c; }
                                    else { kingPosition[2] = r; kingPosition[3] = c; }
                                }

                                boolean legal = !isInCheck(player);

                                // Undo
                                board = snapshot;
                                canCastleKingSide = castleK;
                                canCastleQueenSide = castleQ;
                                kingPosition = kingPosSnap;
                                enPassantTarget = enPassantSnap;

                                if (legal) {
                                    allMoves.add(Arrays.asList(i, j, r, c));
                                }
                            }
                        }
                    }
                }
            }
        }
        return allMoves;
    }

    private int[] parsePosition(String pos) {
        if (pos.length() != 2) return null;
        int col = pos.charAt(0) - 'a';
        int row = 8 - (pos.charAt(1) - '0');
        if (col < 0 || col > 7 || row < 0 || row > 7) return null;
        return new int[]{row, col};
    }
    public boolean onlyQueen(char team){
        int count=0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (board[i][j] instanceof Queen) {
                        count++;
                    } else if (!(board[i][j] instanceof King)) {
                        return false;
                    }
                }
            }
        }
        return count == 1;
    }
    public boolean onlyPawn(char team){
        int count=0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (board[i][j] instanceof Pawn && board[i][j].getColor() == team) {
                        count++;
                    } else if (!(board[i][j] instanceof King) && board[i][j].getColor() != team) {
                        return false;
                    }
                }
            }
        }
        return count == 1;
    }

    public void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                System.out.print((piece == null ? "." : piece) + " ");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }
    public ChessPiece[][] getBoard() {
        return board;
    }
    public ChessBoard(ChessPiece[][] b) {
        setupBoard(b);
    }
}
