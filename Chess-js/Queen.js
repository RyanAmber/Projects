class Queen extends ChessPiece {
    constructor(color) {
        super(color);
    }

    isValidMove(fromRow, fromCol, toRow, toCol, boardObj) {
        // Queen = Rook + Bishop
        if (fromRow === toRow || fromCol === toCol) {
            return new Rook(this.color).isValidMove(fromRow, fromCol, toRow, toCol, boardObj);
        }
        if (Math.abs(fromRow - toRow) === Math.abs(fromCol - toCol)) {
            return new Bishop(this.color).isValidMove(fromRow, fromCol, toRow, toCol, boardObj);
        }
        return false;
    }

    toString() { 
        return this.color === 'b' ? "\u265B" : "\u2655"; 
    }

    getType() { 
        return "Q"; 
    }

    getValue() { 
        return 9; 
    }
}