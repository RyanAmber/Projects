class ChessGame {
    constructor() {
        this.w1 = 0;
        this.w2 = 0;
        this.t = 0;
    }

    startGame(p1Type, p2Type) {
        const p1 = new ChessPlayer(p1Type);
        const p2 = new ChessPlayer(p2Type);

        let currentPlayer = 'w';
        let gameOver = false;
        const board = new ChessBoard();
        const boardStates = new Map();

        return {
            board,
            currentPlayer,
            gameOver,
            boardStates,
            p1,
            p2
        };
    }

    playTurn(gameState) {
        const { board, boardStates, p1, p2, currentPlayer } = gameState;

        board.printBoard();

        // Check game end conditions
        if (board.isInCheckmate(currentPlayer)) {
            console.log((currentPlayer === 'w' ? "White" : "Black") + " is in checkmate. Game over!");
            console.log("The game was " + board.fullmoveNumber + " moves.");
            if (currentPlayer === 'w') {
                this.w2++;
            } else {
                this.w1++;
            }
            return { gameOver: true };
        }

        if (boardStates.has(board.toString())) {
            if (boardStates.get(board.toString()) >= 3) {
                console.log("Draw by threefold repetition! Game over!");
                this.t++;
                return { gameOver: true };
            }
        }

        if (board.isInStalemate(currentPlayer)) {
            console.log("Stalemate! Game over!");
            this.t++;
            return { gameOver: true };
        }

        if (board.isFiftyMoveRule()) {
            console.log("Draw by 50-move rule! Game over!");
            this.t++;
            return { gameOver: true };
        }

        if (board.isInsufficientMaterial()) {
            console.log("Draw by insufficient material! Game over!");
            this.t++;
            return { gameOver: true };
        }

        if (board.isInCheck(currentPlayer)) {
            console.log((currentPlayer === 'w' ? "White" : "Black") + " is in check!");
        }

        // Get player move
        const playerObj = currentPlayer === 'w' ? p1 : p2;
        const move = playerObj.getMove(board, currentPlayer, boardStates);

        // Execute move
        const moveResult = board.movePiece(move[0], move[1], currentPlayer, playerObj.getType());
        if (moveResult) {
            const nextPlayer = (currentPlayer === 'w') ? 'b' : 'w';
            if (boardStates.has(board.toString())) {
                boardStates.set(board.toString(), boardStates.get(board.toString()) + 1);
            } else {
                boardStates.set(board.toString(), 1);
            }
            return { gameOver: false, currentPlayer: nextPlayer };
        } else {
            console.log("Invalid move. Try again.");
            return { gameOver: false, currentPlayer };
        }
    }

    getResults() {
        return {
            player1Wins: this.w1,
            player2Wins: this.w2,
            ties: this.t
        };
    }
}