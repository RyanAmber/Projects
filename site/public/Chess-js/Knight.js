class Knight extends ChessPiece {
    constructor(color) {
        super(color);
    }

    isValidMove(fromRow, fromCol, toRow, toCol, boardObj) {
        const dr = Math.abs(fromRow - toRow);
        const dc = Math.abs(fromCol - toCol);
        
        if (dr * dc === 2) {
            const target = boardObj.board[toRow][toCol];
            return target == null || target.getColor() !== this.color;
        }
        return false;
    }

    toString() { 
        return this.color === 'b' ? "\u265E" : "\u2658"; 
    }

    getType() { 
        return "N"; 
    }

    getValue() { 
        return 3; 
    }
}