class ChessPlayer {
    constructor(type) {
        this.type = type; // 1: Human, 2: Robot (AI), 3: Random, 4: Advanced AI
    }

    getType() {
        return this.type;
    }

    getMove(board, team, boardStates) {
        let move = ['', ''];

        if (this.type === 1) {
            // Human move - would need UI input in a real app
            // For now, return first legal move
            const moves = board.getAllLegalMoves(team);
            if (moves.length > 0) {
                const m = moves[0];
                move[0] = String.fromCharCode('a'.charCodeAt(0) + m[1]) + (8 - m[0]);
                move[1] = String.fromCharCode('a'.charCodeAt(0) + m[3]) + (8 - m[2]);
            }
        } else if (this.type === 2) {
            // Simple AI
            console.log("Current board score: " + this.score(board.copyBoard(), team, board.halfmoveClock, this.getWeights(), boardStates));
            const allMoves = board.getAllLegalMoves(team);
            const scores = new Map();

            for (let m of allMoves) {
                const testboard = board.copyBoard();
                let moves = board.halfmoveClock;
                if (testboard[m[2]][m[3]] !== null || testboard[m[0]][m[1]].getType() === "P") {
                    moves = 0;
                } else {
                    moves++;
                }
                testboard[m[2]][m[3]] = testboard[m[0]][m[1]];
                testboard[m[0]][m[1]] = null;
                scores.set(m, this.score(testboard, team, moves, this.getWeights(), boardStates));
            }

            let minimaxscore = team === 'w' ? Number.NEGATIVE_INFINITY : Number.POSITIVE_INFINITY;
            let bestMoves = [];
            for (let [m, score] of scores.entries()) {
                if (team === 'w' ? score > minimaxscore : score < minimaxscore) {
                    minimaxscore = score;
                    bestMoves = [m];
                } else if (score === minimaxscore) {
                    bestMoves.push(m);
                }
            }

            if (bestMoves.length > 0) {
                const moveIndices = bestMoves[Math.floor(Math.random() * bestMoves.length)];
                move[0] = String.fromCharCode('a'.charCodeAt(0) + moveIndices[1]) + (8 - moveIndices[0]);
                move[1] = String.fromCharCode('a'.charCodeAt(0) + moveIndices[3]) + (8 - moveIndices[2]);
            }
        } else if (this.type === 3) {
            // Random move
            const allMoves = board.getAllLegalMoves(team);
            if (allMoves.length > 0) {
                const moveIndices = allMoves[Math.floor(Math.random() * allMoves.length)];
                move[0] = String.fromCharCode('a'.charCodeAt(0) + moveIndices[1]) + (8 - moveIndices[0]);
                move[1] = String.fromCharCode('a'.charCodeAt(0) + moveIndices[3]) + (8 - moveIndices[2]);
            }
        }

        console.log("Chosen move: " + move[0] + " to " + move[1]);
        return move;
    }

    getWeights() {
        return [0.0, 1.0, 3.0, 3.0, 5.0, 9.0, 0.02]; // Piece values: P, N, B, R, Q, K, mobility
    }

    score(board, team, moves, weights, boardStates) {
        const b = new ChessBoard();
        b.setupBoard(board);
        let score = 0;

        score += this.pieceValues(board, team, weights) * 2;

        let wmodifier = 0.0;
        let bmodifier = 0.0;

        // Piece safety evaluation
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                if (board[i][j] !== null) {
                    if (board[i][j].getColor() === 'w') {
                        if (b.isSquareAttacked(i, j, 'b')) {
                            const temp = board[i][j];
                            const atkval = b.minSquareAttacked(i, j, 'b');
                            const numatkers = b.numSquareAttacked(i, j, 'b');
                            const numdefenders = b.numSquareDefended(i, j, 'w');
                            
                            if (atkval > 0 && (numdefenders >= numatkers)) {
                                wmodifier -= 0.5;
                                switch (temp.getType()) {
                                    case "P": wmodifier -= Math.max(0, 1.0 - atkval); break;
                                    case "N": wmodifier -= Math.max(0, 3.0 - atkval); break;
                                    case "B": wmodifier -= Math.max(0, 3.0 - atkval); break;
                                    case "R": wmodifier -= Math.max(0, 5.0 - atkval); break;
                                    case "Q": wmodifier -= Math.max(0, 9.0 - atkval); break;
                                }
                            } else if (atkval > 0) {
                                wmodifier -= 0.25;
                                switch (temp.getType()) {
                                    case "P": wmodifier -= 1.0; break;
                                    case "N": wmodifier -= 3.0; break;
                                    case "B": wmodifier -= 3.0; break;
                                    case "R": wmodifier -= 5.0; break;
                                    case "Q": wmodifier -= 9.0; break;
                                }
                            }
                            score -= 1;
                        }
                    } else if (board[i][j].getColor() === 'b') {
                        if (b.isSquareAttacked(i, j, 'w')) {
                            const temp = board[i][j];
                            const atkval = b.minSquareAttacked(i, j, 'w');
                            
                            if (atkval > 0 && b.isSquareDefended(i, j, 'b')) {
                                bmodifier += 0.5;
                                switch (temp.getType()) {
                                    case "P": bmodifier += Math.max(0, 1.0 - atkval); break;
                                    case "N": bmodifier += Math.max(0, 3.0 - atkval); break;
                                    case "B": bmodifier += Math.max(0, 3.0 - atkval); break;
                                    case "R": bmodifier += Math.max(0, 5.0 - atkval); break;
                                    case "Q": bmodifier += Math.max(0, 9.0 - atkval); break;
                                }
                            } else if (atkval > 0) {
                                bmodifier += 0.25;
                                switch (temp.getType()) {
                                    case "P": bmodifier += 1.0; break;
                                    case "N": bmodifier += 3.0; break;
                                    case "B": bmodifier += 3.0; break;
                                    case "R": bmodifier += 5.0; break;
                                    case "Q": bmodifier += 9.0; break;
                                }
                            }
                            score += 1;
                        }
                    }
                }
            }
        }

        if (team === 'w') {
            score += wmodifier * 2;
            score += bmodifier * 0.2;
        } else {
            score += bmodifier * 2;
            score += wmodifier * 0.2;
        }

        score += 0.4 * this.kingSafety(board, 'w');
        score -= 0.4 * this.kingSafety(board, 'b');
        score += 0.4 * this.rookFiles(board, 'w');
        score -= 0.4 * this.rookFiles(board, 'b');
        score += 1.0 * this.pawnProgress(board, 'w');
        score -= 1.0 * this.pawnProgress(board, 'b');
        score += 0.3 * this.activePieces(board, 'w');
        score -= 0.3 * this.activePieces(board, 'b');

        // Mobility bonus
        const allMoves = b.getAllLegalMoves('w');
        for (let move of allMoves) {
            score += weights[6];
        }
        const blackMoves = b.getAllLegalMoves('b');
        for (let move of blackMoves) {
            score -= weights[6];
        }

        // Repetition penalty
        if (boardStates.has(b.toString()) && team === 'w') {
            score -= 2.0 * boardStates.get(b.toString());
        } else if (boardStates.has(b.toString()) && team === 'b') {
            score += 2.0 * boardStates.get(b.toString());
        }

        // Check/Checkmate evaluation
        if (b.isInCheck('w')) {
            score -= 0.3;
        } else if (b.isInCheck('b')) {
            score += 0.3;
        }

        if (b.isInStalemate('w')) {
            score = 0;
        }
        if (b.isInStalemate('b')) {
            score = 0;
        }
        if (b.isInsufficientMaterial()) {
            score = 0;
        }
        if (moves >= 98) {
            score = 0;
        }
        if (b.isInCheckmate('w')) {
            score = -200000;
        } else if (b.isInCheckmate('b')) {
            score = 200000;
        }

        if (b.onlyQueen('w')) {
            score += 100 * this.queenSolve(board, 'w');
            if (b.isInStalemate('b')) {
                score -= 500;
            }
        } else if (b.onlyQueen('b')) {
            score -= 100 * this.queenSolve(board, 'b');
            if (b.isInStalemate('w')) {
                score += 500;
            }
        }

        return Math.round(score * 1000.0) / 2000.0;
    }

    queenSolve(board, team) {
        let qX = -1, qY = -1, kX = -1, kY = -1, oX = -1, oY = -1;
        let score = 100;

        for (let r = 0; r < 8; r++) {
            for (let c = 0; c < 8; c++) {
                if (board[r][c] !== null) {
                    if (board[r][c].getType() === "Q" && board[r][c].getColor() === team) {
                        qY = r; qX = c;
                    }
                    if (board[r][c].getType() === "K" && board[r][c].getColor() === team) {
                        kY = r; kX = c;
                    }
                    if (board[r][c].getType() === "K" && board[r][c].getColor() !== team) {
                        oY = r; oX = c;
                    }
                }
            }
        }

        let distY = qY - oY;
        let distX = oX - qX;
        if (distY === 1 && distX === 2) {
            score += 5;
        }
        if (qY === 1 && qX === 4 && oY === 0 && oX > 6) {
            score += 10;
            score -= Math.abs(2 - kY);
            score -= Math.abs(6 - kX);
        }
        score -= Math.abs(oY - qY);
        score -= Math.abs(oX - qX);
        if (Math.abs(oY - qY) <= 1 && Math.abs(oX - qX) <= 1) {
            score -= 13;
        }
        return score;
    }

    pieceValues(board, team, weights) {
        let score = 0;
        for (let r = 0; r < 8; r++) {
            for (let c = 0; c < 8; c++) {
                if (board[r][c] !== null) {
                    let val = 0;
                    switch (board[r][c].getType()) {
                        case "P": val = weights[1]; break;
                        case "N": val = weights[2]; break;
                        case "B": val = weights[3]; break;
                        case "R": val = weights[4]; break;
                        case "Q": val = weights[5]; break;
                    }
                    if (board[r][c].getColor() === 'w') {
                        score += val;
                    } else {
                        score -= val;
                    }
                }
            }
        }
        return score;
    }

    static kingSafety(board, team) {
        const b = new ChessBoard();
        b.setupBoard(board);
        let safety = 0;
        let kingRow = -1, kingCol = -1;

        for (let r = 0; r < 8; r++) {
            for (let c = 0; c < 8; c++) {
                if (board[r][c] !== null && board[r][c].getType() === "K" && board[r][c].getColor() === team) {
                    kingRow = r;
                    kingCol = c;
                }
            }
        }

        if (kingRow !== -1 && kingCol !== -1) {
            const directions = [[-1, 0], [1, 0], [0, -1], [0, 1], [-1, -1], [-1, 1], [1, -1], [1, 1]];
            for (let dir of directions) {
                const newRow = kingRow + dir[0];
                const newCol = kingCol + dir[1];
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    if (board[newRow][newCol] === null || board[newRow][newCol].getColor() === team) {
                        safety += 0.5;
                        if (board[newRow][newCol] !== null && board[newRow][newCol].getColor() === team && board[newRow][newCol].getType() === "P") {
                            safety += 1;
                        }
                    } else if (board[newRow][newCol].getColor() !== team) {
                        safety -= 1;
                    } else if (b.isSquareAttacked(newRow, newCol, team === 'w' ? 'b' : 'w')) {
                        safety -= 1;
                    }
                } else {
                    safety += 1;
                }
            }
        }
        return safety;
    }

    static rookFiles(board, team) {
        let openFiles = 0;
        for (let c = 0; c < 8; c++) {
            let hasRook = false;
            let isOpen = true;
            for (let r = 0; r < 8; r++) {
                if (board[r][c] !== null) {
                    if (board[r][c].getType() === "R" && board[r][c].getColor() === team) {
                        hasRook = true;
                    } else if (board[r][c].getType() === "P" && board[r][c].getColor() === team) {
                        isOpen = false;
                    }
                }
            }
            if (hasRook && isOpen) {
                openFiles += 0.3;
            }
        }
        return openFiles;
    }

    static pawnProgress(board, team) {
        let progress = 0;
        for (let c = 0; c < 8; c++) {
            for (let r = 0; r < 8; r++) {
                if (board[r][c] !== null && board[r][c].getType() === "P" && board[r][c].getColor() === team) {
                    if (team === 'b') {
                        progress += r / 10.0;
                    } else {
                        progress += (7.0 - r) / 10.0;
                    }
                    if (r === 7 || r === 0) {
                        progress += 2;
                    }
                }
            }
        }
        return progress;
    }

    static activePieces(board, team) {
        let active = 0;
        for (let c = 0; c < 8; c++) {
            if (team === 'w') {
                if (board[7][c] !== null && board[7][c].getColor() === team && 
                    (board[7][c].getType() === "B" || board[7][c].getType() === "N")) {
                    active -= 1;
                }
            } else {
                if (board[0][c] !== null && board[0][c].getColor() === team && 
                    (board[0][c].getType() === "B" || board[0][c].getType() === "N")) {
                    active += 1;
                }
            }
        }
        return active;
    }
}