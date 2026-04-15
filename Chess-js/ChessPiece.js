class ChessPiece {
    constructor(color) {
        this.color = color; // 'w' or 'b'
    }

    getColor() {
        return this.color;
    }

    isValidMove(fromRow, fromCol, toRow, toCol, chessBoard) {
        throw new Error("isValidMove must be implemented by subclass");
    }

    clone() {
        return Object.assign(Object.create(Object.getPrototypeOf(this)), this);
    }

    toString() {
        throw new Error("toString must be implemented by subclass");
    }

    getType() {
        throw new Error("getType must be implemented by subclass");
    }

    getValue() {
        throw new Error("getValue must be implemented by subclass");
    }
}