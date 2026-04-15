class Rook extends ChessPiece {
    constructor(color) {
        super(color);
    }

    isValidMove(fromRow, fromCol, toRow, toCol, boardObj) {
        const board = boardObj.board;
        if (fromRow !== toRow && fromCol !== toCol) return false;
        
        const stepRow = Math.sign(toRow - fromRow);
        const stepCol = Math.sign(toCol - fromCol);
        
        let r = fromRow + stepRow, c = fromCol + stepCol;
        while (r !== toRow || c !== toCol) {
            if (board[r][c] !== null) return false;
            r += stepRow;
            c += stepCol;
        }
        return board[toRow][toCol] === null || board[toRow][toCol].getColor() !== this.color;
    }

    toString() { 
        return this.color === 'b' ? "\u2656" : "\u265C"; 
    }

    getType() { 
        return "R"; 
    }

    getValue() { 
        return 5; 
    }
}