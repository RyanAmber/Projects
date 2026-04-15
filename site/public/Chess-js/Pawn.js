class Pawn extends ChessPiece {
    constructor(color) {
        super(color);
    }

    isValidMove(fromRow, fromCol, toRow, toCol, boardObj) {
        const board = boardObj.board;
        const direction = (this.color === 'w') ? -1 : 1;
        
        // Forward move
        if (fromCol === toCol && board[toRow][toCol] == null) {
            if (toRow - fromRow === direction) return true;
            if ((this.color === 'w' && fromRow === 6 || this.color === 'b' && fromRow === 1)
                    && toRow - fromRow === 2 * direction && board[fromRow + direction][fromCol] === null)
                return true;
        }
        
        // Capture
        if (Math.abs(fromCol - toCol) === 1 && toRow - fromRow === direction) {
            if (board[toRow][toCol] != null && board[toRow][toCol].getColor() !== this.color)
                return true;
            // En passant
            if (boardObj.enPassantTarget !== null
                    && toRow === boardObj.enPassantTarget[0]
                    && toCol === boardObj.enPassantTarget[1]) {
                return true;
            }
        }
        return false;
    }

    toString() { 
        return this.color === 'b' ? "\u265F" : "\u2659"; 
    }

    getType() { 
        return "P"; 
    }

    getValue() { 
        return 1; 
    }
}