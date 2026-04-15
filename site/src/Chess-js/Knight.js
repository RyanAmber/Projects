class Knight extends ChessPiece {
    constructor(color) {
        super(color);
    }

    isValidMove(fromRow, fromCol, toRow, toCol, boardObj) {
        const dr = Math.abs(fromRow - toRow);
        const dc = Math.abs(fromCol - toCol);
        
        if (dr * dc === 2) {
            const target = boardObj.board[toRow][toCol];
            return target === null || target.getColor() !== this.color;
        }
        return false;
    }

    toString() { 
        return this.color === 'b' ? "\u2658" : "\u265E"; 
    }

    getType() { 
        return "N"; 
    }

    getValue() { 
        return 3; 
    }
}