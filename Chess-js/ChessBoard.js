class ChessBoard {
    constructor(boardArray = null) {
        this.board = boardArray ? this.setupBoard(boardArray) : new Array(8);
        for (let i = 0; i < 8; i++) {
            if (!this.board[i]) this.board[i] = new Array(8);
        }
        
        this.canCastleKingSide = [true, true]; // [white, black]
        this.canCastleQueenSide = [true, true];
        this.kingPosition = [7, 4, 0, 4]; // [wRow, wCol, bRow, bCol]
        this.enPassantTarget = null; // {row, col}
        this.halfmoveClock = 0;
        this.fullmoveNumber = 1;
        
        if (!boardArray) this.setupPieces();
    }

    toString() {
        let sb = "";
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                sb += this.board[i][j] == null ? "." : this.board[i][j].toString();
            }
            sb += "/";
        }
        return sb;
    }

    setupPieces() {
        // Pawns
        for (let i = 0; i < 8; i++) {
            this.board[1][i] = new Pawn('b');
            this.board[6][i] = new Pawn('w');
        }
        // Major pieces
        this.board[0][0] = new Rook('b');   this.board[0][7] = new Rook('b');
        this.board[7][0] = new Rook('w');   this.board[7][7] = new Rook('w');
        this.board[0][1] = new Knight('b'); this.board[0][6] = new Knight('b');
        this.board[7][1] = new Knight('w'); this.board[7][6] = new Knight('w');
        this.board[0][2] = new Bishop('b'); this.board[0][5] = new Bishop('b');
        this.board[7][2] = new Bishop('w'); this.board[7][5] = new Bishop('w');
        this.board[0][3] = new Queen('b');  this.board[0][4] = new King('b');
        this.board[7][3] = new Queen('w');  this.board[7][4] = new King('w');
    }

    setupBoard(newBoard) {
        this.board = new Array(8);
        for (let i = 0; i < 8; i++) this.board[i] = new Array(8);
        
        this.kingPosition = [-1, -1, -1, -1];
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                this.board[i][j] = newBoard[i][j];
                if (this.board[i][j] instanceof King) {
                    if (this.board[i][j].getColor() === 'w') {
                        this.kingPosition[0] = i;
                        this.kingPosition[1] = j;
                    } else if (this.board[i][j].getColor() === 'b') {
                        this.kingPosition[2] = i;
                        this.kingPosition[3] = j;
                    }
                }
            }
        }
        return this.board;
    }

    movePiece(from, to, player, type) {
        const fromIdx = this.parsePosition(from);
        const toIdx = this.parsePosition(to);
        if (fromIdx == null || toIdx == null) return false;

        const piece = this.board[fromIdx[0]][fromIdx[1]];
        if (piece == null || piece.getColor() !== player) return false;

        // Save state for undo
        const snapshot = this.copyBoard();
        const castleK = [...this.canCastleKingSide];
        const castleQ = [...this.canCastleQueenSide];
        const kingPosSnap = [...this.kingPosition];
        const enPassantSnap = this.enPassantTarget == null ? null : [...this.enPassantTarget];

        const valid = piece.isValidMove(fromIdx[0], fromIdx[1], toIdx[0], toIdx[1], this);
        if (!valid) return false;

        let isCastling = false, isPromotion = false, isEnPassant = false;

        // Castling
        if (piece instanceof King && Math.abs(fromIdx[1] - toIdx[1]) === 2) {
            isCastling = true;
            this.doCastling(fromIdx, toIdx, player);
        }
        // En Passant
        else if (piece instanceof Pawn &&
                this.enPassantTarget != null &&
                toIdx[0] === this.enPassantTarget[0] && toIdx[1] === this.enPassantTarget[1]) {
            isEnPassant = true;
            this.board[toIdx[0]][toIdx[1]] = piece;
            this.board[fromIdx[0]][fromIdx[1]] = null;
            this.board[fromIdx[0]][toIdx[1]] = null; // capture
        }
        // Promotion
        else if (piece instanceof Pawn &&
                (toIdx[0] === 0 || toIdx[0] === 7)) {
            isPromotion = true;
            this.board[toIdx[0]][toIdx[1]] = piece;
            this.board[fromIdx[0]][fromIdx[1]] = null;
        }
        // Normal move
        else {
            this.board[toIdx[0]][toIdx[1]] = piece;
            this.board[fromIdx[0]][fromIdx[1]] = null;
        }

        // Update king position
        if (piece instanceof King) {
            if (player === 'w') { 
                this.kingPosition[0] = toIdx[0]; 
                this.kingPosition[1] = toIdx[1]; 
            } else { 
                this.kingPosition[2] = toIdx[0]; 
                this.kingPosition[3] = toIdx[1]; 
            }
            // Update castling rights
            if (player === 'w') { 
                this.canCastleKingSide[0] = false; 
                this.canCastleQueenSide[0] = false; 
            } else { 
                this.canCastleKingSide[1] = false; 
                this.canCastleQueenSide[1] = false; 
            }
        }
        // Update rook moves for castling rights
        if (piece instanceof Rook) {
            if (fromIdx[0] === 7 && fromIdx[1] === 0) this.canCastleQueenSide[0] = false;
            if (fromIdx[0] === 7 && fromIdx[1] === 7) this.canCastleKingSide[0] = false;
            if (fromIdx[0] === 0 && fromIdx[1] === 0) this.canCastleQueenSide[1] = false;
            if (fromIdx[0] === 0 && fromIdx[1] === 7) this.canCastleKingSide[1] = false;
        }
        
        // Update en passant target
        if (piece instanceof Pawn && Math.abs(fromIdx[0] - toIdx[0]) === 2) {
            this.enPassantTarget = [(fromIdx[0] + toIdx[0]) / 2, fromIdx[1]];
        } else {
            this.enPassantTarget = null;
        }

        // Check for self-check
        if (this.isInCheck(player)) {
            this.board = snapshot;
            this.canCastleKingSide = castleK;
            this.canCastleQueenSide = castleQ;
            this.kingPosition = kingPosSnap;
            this.enPassantTarget = enPassantSnap;
            return false;
        }

        // Promotion input
        if (isPromotion) {
            let promo = "Q";
            if (type === 1) {
                // In a real app, prompt user for promotion choice
                promo = "Q"; // Default to Queen
            }
            let promoted;
            switch (promo) {
                case "Q": promoted = new Queen(player); break;
                case "R": promoted = new Rook(player); break;
                case "B": promoted = new Bishop(player); break;
                case "N": promoted = new Knight(player); break;
                default: promoted = new Queen(player);
            }
            this.board[toIdx[0]][toIdx[1]] = promoted;
        }

        this.halfmoveClock = (piece instanceof Pawn || this.board[toIdx[0]][toIdx[1]] != null) ? 0 : this.halfmoveClock + 1;
        if (player === 'b') this.fullmoveNumber++;
        return true;
    }

    doCastling(fromIdx, toIdx, player) {
        const row = fromIdx[0];
        if (toIdx[1] === 6) { // King side
            this.board[row][6] = this.board[row][4];
            this.board[row][4] = null;
            this.board[row][5] = this.board[row][7];
            this.board[row][7] = null;
        } else if (toIdx[1] === 2) { // Queen side
            this.board[row][2] = this.board[row][4];
            this.board[row][4] = null;
            this.board[row][3] = this.board[row][0];
            this.board[row][0] = null;
        }
    }

    copyBoard() {
        const copy = new Array(8);
        for (let i = 0; i < 8; i++) {
            copy[i] = new Array(8);
            for (let j = 0; j < 8; j++) {
                copy[i][j] = this.board[i][j] == null ? null : this.board[i][j].clone();
            }
        }
        return copy;
    }

    isInCheck(player) {
        const kRow = player === 'w' ? this.kingPosition[0] : this.kingPosition[2];
        const kCol = player === 'w' ? this.kingPosition[1] : this.kingPosition[3];
        return this.isSquareAttacked(kRow, kCol, player === 'w' ? 'b' : 'w');
    }

    isSquareAttacked(row, col, byPlayer) {
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null && this.board[i][j].getColor() === byPlayer) {
                    if (this.board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1) {
                        return true;
                    } else if (!(this.board[i][j] instanceof King) && this.board[i][j].isValidMove(i, j, row, col, this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    minSquareAttacked(row, col, byPlayer) {
        let min = Number.MAX_VALUE;
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null && this.board[i][j].getColor() === byPlayer) {
                    if (this.board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1) {
                        min = Math.min(min, this.board[i][j].getValue());
                    } else if (!(this.board[i][j] instanceof King) && this.board[i][j].isValidMove(i, j, row, col, this)) {
                        min = Math.min(min, this.board[i][j].getValue());
                    }
                }
            }
        }
        return min;
    }

    isSquareDefended(row, col, byPlayer) {
        if (this.board[row][col] == null || this.board[row][col].getColor() !== byPlayer) {
            return false;
        }
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null && this.board[i][j].getColor() === byPlayer) {
                    if (i === row && j === col) continue;
                    const original = this.board[row][col];
                    this.board[row][col] = new NullPiece(byPlayer === 'w' ? 'b' : 'w');
                    const defended = (this.board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1) ||
                        (!(this.board[i][j] instanceof King) && this.board[i][j].isValidMove(i, j, row, col, this));
                    this.board[row][col] = original;
                    if (defended) return true;
                }
            }
        }
        return false;
    }

    numSquareAttacked(row, col, byPlayer) {
        let count = 0;
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null && this.board[i][j].getColor() === byPlayer) {
                    if (this.board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1) {
                        count++;
                    } else if (!(this.board[i][j] instanceof King) && this.board[i][j].isValidMove(i, j, row, col, this)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    numSquareDefended(row, col, byPlayer) {
        let count = 0;
        if (this.board[row][col] == null || this.board[row][col].getColor() !== byPlayer) {
            return 0;
        }
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null && this.board[i][j].getColor() === byPlayer) {
                    if (i === row && j === col) continue;
                    const original = this.board[row][col];
                    this.board[row][col] = new NullPiece(byPlayer === 'w' ? 'b' : 'w');
                    const defended = (this.board[i][j] instanceof King && Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1) ||
                        (!(this.board[i][j] instanceof King) && this.board[i][j].isValidMove(i, j, row, col, this));
                    this.board[row][col] = original;
                    if (defended) count++;
                }
            }
        }
        return count;
    }

    isInCheckmate(player) {
        if (!this.isInCheck(player)) return false;
        return !this.hasLegalMoves(player);
    }

    isFiftyMoveRule() {
        return this.halfmoveClock > 100;
    }

    isInsufficientMaterial() {
        const pieces = [];
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null) pieces.push(this.board[i][j]);
            }
        }
        
        if (pieces.length === 2) return true; // Only kings
        if (pieces.length === 3) {
            for (let p of pieces) {
                if (p instanceof Bishop || p instanceof Knight) return true;
            }
        }
        if (pieces.length === 4) {
            let bishopCount = 0;
            for (let p of pieces) {
                if (p instanceof Bishop) bishopCount++;
            }
            if (bishopCount === 2) {
                let b1Row = -1, b1Col = -1, b2Row = -1, b2Col = -1;
                for (let i = 0; i < 8; i++) {
                    for (let j = 0; j < 8; j++) {
                        if (this.board[i][j] instanceof Bishop) {
                            if (b1Row === -1) { b1Row = i; b1Col = j; }
                            else { b2Row = i; b2Col = j; }
                        }
                    }
                }
                return ((b1Row + b1Col) % 2) === ((b2Row + b2Col) % 2);
            }
        }
        return false;
    }

    isInStalemate(player) {
        if (this.isInCheck(player)) return false;
        return !this.hasLegalMoves(player);
    }

    hasLegalMoves(player) {
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null && this.board[i][j].getColor() === player) {
                    for (let r = 0; r < 8; r++) {
                        for (let c = 0; c < 8; c++) {
                            if (i === r && j === c) continue;
                            if (this.board[i][j].isValidMove(i, j, r, c, this)) {
                                const snapshot = this.copyBoard();
                                const castleK = [...this.canCastleKingSide];
                                const castleQ = [...this.canCastleQueenSide];
                                const kingPosSnap = [...this.kingPosition];
                                const enPassantSnap = this.enPassantTarget == null ? null : [...this.enPassantTarget];

                                this.board[r][c] = this.board[i][j];
                                this.board[i][j] = null;
                                
                                if (this.board[r][c] instanceof King) {
                                    if (player === 'w') { 
                                        this.kingPosition[0] = r; 
                                        this.kingPosition[1] = c; 
                                    } else { 
                                        this.kingPosition[2] = r; 
                                        this.kingPosition[3] = c; 
                                    }
                                }

                                const legal = !this.isInCheck(player);
                                
                                this.board = snapshot;
                                this.canCastleKingSide = castleK;
                                this.canCastleQueenSide = castleQ;
                                this.kingPosition = kingPosSnap;
                                this.enPassantTarget = enPassantSnap;

                                if (legal) return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    getAllLegalMoves(player) {
        const allMoves = [];
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                const piece = this.board[i][j];
                if (piece != null && piece.getColor() === player) {
                    for (let r = 0; r < 8; r++) {
                        for (let c = 0; c < 8; c++) {
                            if (i === r && j === c) continue;
                            if (piece.isValidMove(i, j, r, c, this)) {
                                const snapshot = this.copyBoard();
                                const castleK = [...this.canCastleKingSide];
                                const castleQ = [...this.canCastleQueenSide];
                                const kingPosSnap = [...this.kingPosition];
                                const enPassantSnap = this.enPassantTarget == null ? null : [...this.enPassantTarget];

                                const movedPiece = this.board[i][j];
                                this.board[r][c] = movedPiece;
                                this.board[i][j] = null;
                                
                                if (movedPiece instanceof King) {
                                    if (player === 'w') { 
                                        this.kingPosition[0] = r; 
                                        this.kingPosition[1] = c; 
                                    } else { 
                                        this.kingPosition[2] = r; 
                                        this.kingPosition[3] = c; 
                                    }
                                }

                                const legal = !this.isInCheck(player);

                                this.board = snapshot;
                                this.canCastleKingSide = castleK;
                                this.canCastleQueenSide = castleQ;
                                this.kingPosition = kingPosSnap;
                                this.enPassantTarget = enPassantSnap;

                                if (legal) allMoves.push([i, j, r, c]);
                            }
                        }
                    }
                }
            }
        }
        return allMoves;
    }

    parsePosition(pos) {
        if (pos.length !== 2) return null;
        const col = pos.charCodeAt(0) - 'a'.charCodeAt(0);
        const row = 8 - (pos.charCodeAt(1) - '0'.charCodeAt(0));
        if (col < 0 || col > 7 || row < 0 || row > 7) return null;
        return [row, col];
    }

    onlyQueen(team) {
        let count = 0;
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null) {
                    if (this.board[i][j] instanceof Queen) {
                        count++;
                    } else if (!(this.board[i][j] instanceof King)) {
                        return false;
                    }
                }
            }
        }
        return count === 1;
    }

    onlyPawn(team) {
        let count = 0;
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (this.board[i][j] != null) {
                    if (this.board[i][j] instanceof Pawn && this.board[i][j].getColor() === team) {
                        count++;
                    } else if (!(this.board[i][j] instanceof King) && this.board[i][j].getColor() !== team) {
                        return false;
                    }
                }
            }
        }
        return count === 1;
    }

    printBoard() {
        console.log("  a b c d e f g h");
        for (let i = 0; i < 8; i++) {
            let row = (8 - i) + " ";
            for (let j = 0; j < 8; j++) {
                const piece = this.board[i][j];
                row += (piece == null ? "." : piece.toString()) + " ";
            }
            console.log(row);
        }
        console.log("  a b c d e f g h");
    }

    getBoard() {
        return this.board;
    }
}