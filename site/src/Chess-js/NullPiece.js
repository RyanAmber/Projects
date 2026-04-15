class NullPiece extends ChessPiece {
    constructor(color) {
        super(color);
    }

    isValidMove(startRow, startCol, endRow, endCol, board) {
        return false;
    }

    toString() {
        return ".";
    }

    getType() {
        return "N";
    }

    getValue() {
        return 0;
    }
}