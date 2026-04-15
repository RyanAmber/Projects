class ChessBoardNode {
    constructor(data, playerTurn, boardStates, move, aiTeam) {
        this.data = data;
        this.playerTurn = playerTurn;
        this.nextMoves = [];
        this.move = move;
        this.boardStates = boardStates;
        this.aiTeam = aiTeam;
    }

    addNext(node) {
        this.nextMoves.push(node);
    }

    getMove() {
        return this.move;
    }

    // Original entry point - starts alpha-beta with full window
    getScoreAtDepth(depth) {
        return this.getScoreAtDepthWithAlphaBeta(depth, Number.NEGATIVE_INFINITY, Number.POSITIVE_INFINITY);
    }

    // Alpha-beta pruning implementation
    // alpha = best score the maximizer (white) is guaranteed so far
    // beta  = best score the minimizer (black) is guaranteed so far
    // If beta <= alpha, the opponent already has a better option elsewhere, so we prune
    getScoreAtDepthWithAlphaBeta(depth, alpha, beta) {
        const p = new ChessPlayer(2);
        const moves = this.data.halfmoveClock;
        const currentScore = p.score(this.data.getBoard(), this.aiTeam, moves, p.getWeights(), this.boardStates);
        
        if (depth === 0 || this.nextMoves.length === 0) {
            return p.score(this.data.getBoard(), this.aiTeam, moves, p.getWeights(), this.boardStates);
        }

        if (this.playerTurn === 'w') {
            let bestScore = Number.NEGATIVE_INFINITY;
            for (let child of this.nextMoves) {
                child.getAllNextMoves();
                const childScore = child.getScoreAtDepthWithAlphaBeta(depth - 1, alpha, beta);
                const weight = 1.0 / (depth + 1);
                const blended = (1 - weight) * childScore + weight * currentScore;
                bestScore = Math.max(bestScore, blended);
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return bestScore;
        } else {
            let bestScore = Number.POSITIVE_INFINITY;
            for (let child of this.nextMoves) {
                child.getAllNextMoves();
                const childScore = child.getScoreAtDepthWithAlphaBeta(depth - 1, alpha, beta);
                const weight = 1.0 / (depth + 1);
                const blended = (1 - weight) * childScore + weight * currentScore;
                bestScore = Math.min(bestScore, blended);
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }
            return bestScore;
        }
    }

    getNextMoves() {
        return this.nextMoves;
    }

    getAllNextMoves() {
        this.nextMoves = [];
        const possibleMoves = this.data.getAllLegalMoves(this.playerTurn);
        
        for (let move of possibleMoves) {
            const newBoard = new ChessBoard();
            newBoard.setupBoard(this.data.getBoard());
            
            const startrow = move[0];
            const startcol = move[1];
            const endrow = move[2];
            const endcol = move[3];
            
            const tempBoard = newBoard.getBoard();
            tempBoard[endrow][endcol] = tempBoard[startrow][startcol];
            tempBoard[startrow][startcol] = null;
            newBoard.setupBoard(tempBoard);

            const newBoardStates = new Map(this.boardStates);
            const key = newBoard.toString();
            newBoardStates.set(key, (newBoardStates.get(key) || 0) + 1);

            const childNode = new ChessBoardNode(
                newBoard,
                this.playerTurn === 'w' ? 'b' : 'w',
                newBoardStates,
                move,
                this.aiTeam
            );
            this.addNext(childNode);
        }
    }
}