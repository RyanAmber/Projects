class King extends ChessPiece {
    constructor(color) {
        super(color);
    }

    isValidMove(fromRow, fromCol, toRow, toCol, boardObj) {
        if (Math.abs(fromRow - toRow) > 1 || Math.abs(fromCol - toCol) > 1) {
            // Castling
            if (fromRow === toRow && Math.abs(fromCol - toCol) === 2) {
                return this.isValidCastling(fromRow, fromCol, toCol, boardObj);
            }
            return false;
        }
        return boardObj.board[toRow][toCol] === null || boardObj.board[toRow][toCol].getColor() !== this.color;
    }

    isValidCastling(kingRow, kingCol, toCol, boardObj) {
        const board = boardObj.board;
        const player = this.color;
        
        // Check if king has moved
        if (player === 'w' && !boardObj.canCastleKingSide[0] && !boardObj.canCastleQueenSide[0]) {
            return false;
        }
        if (player === 'b' && !boardObj.canCastleKingSide[1] && !boardObj.canCastleQueenSide[1]) {
            return false;
        }

        // King-side castling
        if (toCol === 6) {
            if (player === 'w' && !boardObj.canCastleKingSide[0]) return false;
            if (player === 'b' && !boardObj.canCastleKingSide[1]) return false;
            if (board[kingRow][5] !== null || board[kingRow][6] !== null) return false;
            return board[kingRow][7] !== null && board[kingRow][7] instanceof Rook;
        }
        // Queen-side castling
        else if (toCol === 2) {
            if (player === 'w' && !boardObj.canCastleQueenSide[0]) return false;
            if (player === 'b' && !boardObj.canCastleQueenSide[1]) return false;
            if (board[kingRow][1] !== null || board[kingRow][2] !== null || board[kingRow][3] !== null) return false;
            return board[kingRow][0] !== null && board[kingRow][0] instanceof Rook;
        }
        return false;
    }

    toString() {
        return this.color === 'b' ? "\u2654" : "\u265A";
    }

    getType() {
        return "K";
    }

    getValue() {
        return 0; // King is invaluable
    }
}