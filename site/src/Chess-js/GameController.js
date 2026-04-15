class GameController {
    constructor() {
        this.board = null;
        this.gameActive = false;
        this.selectedSquare = null;
        this.validMoves = [];
        this.p1 = null;
        this.p2 = null;
        this.currentPlayer = 'w';
        this.moveCount = 0;
        this.moveHistory = [];
        this.boardStates = new Map();
        this.gameOverFlag = false;
        
        this.initializeUI();
    }

    initializeUI() {
        // Board setup
        const boardElement = document.getElementById('chessBoard');
        this.initializeBoard(boardElement);

        // Event listeners
        document.getElementById('startBtn').addEventListener('click', () => this.startGame());
        document.getElementById('resetBtn').addEventListener('click', () => this.resetGame());
        boardElement.addEventListener('click', (e) => this.handleBoardClick(e));
    }

    initializeBoard(boardElement) {
        boardElement.innerHTML = '';
        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const square = document.createElement('div');
                square.className = `square ${(row + col) % 2 === 0 ? 'light' : 'dark'}`;
                square.id = `square-${row}-${col}`;
                
                // Add coordinates
                if (col === 0) {
                    const rank = document.createElement('span');
                    rank.className = 'coordinate rank';
                    rank.textContent = 8 - row;
                    square.appendChild(rank);
                }
                if (row === 7) {
                    const file = document.createElement('span');
                    file.className = 'coordinate file';
                    file.textContent = String.fromCharCode(97 + col);
                    square.appendChild(file);
                }
                
                boardElement.appendChild(square);
            }
        }
    }

    startGame() {
        if (this.gameActive) return;

        const p1Type = parseInt(document.getElementById('player1').value);
        const p2Type = parseInt(document.getElementById('player2').value);

        this.board = new ChessBoard();
        this.p1 = new ChessPlayer(p1Type);
        this.p2 = new ChessPlayer(p2Type);
        this.currentPlayer = 'w';
        this.moveCount = 0;
        this.moveHistory = [];
        this.boardStates = new Map();
        this.gameActive = true;
        this.gameOverFlag = false;
        this.selectedSquare = null;

        document.getElementById('startBtn').disabled = true;
        document.getElementById('gameOverMessage').style.display = 'none';
        document.getElementById('player1').disabled = true;
        document.getElementById('player2').disabled = true;

        this.updateUI();
        this.board.printBoard();

        // If AI is playing white, make its move
        if (p1Type !== 1) {
            setTimeout(() => this.makeAIMove(), 500);
        }
    }

    resetGame() {
        this.gameActive = false;
        this.gameOverFlag = false;
        this.selectedSquare = null;
        this.validMoves = [];
        
        document.getElementById('startBtn').disabled = false;
        document.getElementById('player1').disabled = false;
        document.getElementById('player2').disabled = false;
        document.getElementById('gameOverMessage').style.display = 'none';
        document.getElementById('moveLog').innerHTML = '';
        document.getElementById('currentTurn').textContent = 'Waiting...';
        document.getElementById('moveCount').textContent = '0';
        document.getElementById('lastMove').textContent = '-';

        this.initializeBoard(document.getElementById('chessBoard'));
        this.renderBoard();
    }

    handleBoardClick(e) {
        if (!this.gameActive || this.gameOverFlag) return;

        const square = e.target.closest('.square');
        if (!square) return;

        const squareId = square.id;
        const [row, col] = squareId.split('-').slice(1).map(Number);

        // If a valid move square is clicked, execute the move
        if (this.validMoves.some(m => m[0] === row && m[1] === col)) {
            this.executeMove(this.selectedSquare[0], this.selectedSquare[1], row, col);
            return;
        }

        // If human player's turn, allow piece selection
        if ((this.currentPlayer === 'w' && this.p1.getType() === 1) ||
            (this.currentPlayer === 'b' && this.p2.getType() === 1)) {
            
            const piece = this.board.board[row][col];
            if (piece && piece.getColor() === this.currentPlayer) {
                this.selectedSquare = [row, col];
                this.validMoves = this.getValidMovesForPiece(row, col);
                this.renderBoard();
            } else {
                this.selectedSquare = null;
                this.validMoves = [];
                this.renderBoard();
            }
        }
    }

    getValidMovesForPiece(row, col) {
        const allMoves = this.board.getAllLegalMoves(this.currentPlayer);
        return allMoves.filter(m => m[0] === row && m[1] === col).map(m => [m[2], m[3]]);
    }

    executeMove(fromRow, fromCol, toRow, toCol) {
        const fromNotation = String.fromCharCode(97 + fromCol) + (8 - fromRow);
        const toNotation = String.fromCharCode(97 + toCol) + (8 - toRow);

        const success = this.board.movePiece(fromNotation, toNotation, this.currentPlayer, 1);

        if (success) {
            this.moveCount++;
            this.moveHistory.push(`${fromNotation} → ${toNotation}`);
            
            if (this.boardStates.has(this.board.toString())) {
                this.boardStates.set(this.board.toString(), this.boardStates.get(this.board.toString()) + 1);
            } else {
                this.boardStates.set(this.board.toString(), 1);
            }

            this.checkGameEnd();

            if (!this.gameOverFlag) {
                this.currentPlayer = this.currentPlayer === 'w' ? 'b' : 'w';
                this.selectedSquare = null;
                this.validMoves = [];
                this.renderBoard();
                this.updateUI();

                // Auto-play AI move if needed
                if (!this.gameOverFlag) {
                    if ((this.currentPlayer === 'w' && this.p1.getType() !== 1) ||
                        (this.currentPlayer === 'b' && this.p2.getType() !== 1)) {
                        setTimeout(() => this.makeAIMove(), 800);
                    }
                }
            }
        } else {
            console.log("Invalid move");
        }
    }

    makeAIMove() {
        if (!this.gameActive || this.gameOverFlag) return;

        const player = this.currentPlayer === 'w' ? this.p1 : this.p2;
        const move = player.getMove(this.board, this.currentPlayer, this.boardStates);

        if (move[0] && move[1]) {
            const fromIdx = this.board.parsePosition(move[0]);
            const toIdx = this.board.parsePosition(move[1]);
            this.executeMove(fromIdx[0], fromIdx[1], toIdx[0], toIdx[1]);
        }
    }

    checkGameEnd() {
        const player = this.currentPlayer;

        if (this.board.isInCheckmate(player)) {
            this.endGame(`${player === 'w' ? 'Black' : 'White'} wins by checkmate!`);
            return;
        }

        if (this.boardStates.has(this.board.toString()) && this.boardStates.get(this.board.toString()) >= 3) {
            this.endGame("Draw by threefold repetition!");
            return;
        }

        if (this.board.isInStalemate(player)) {
            this.endGame("Draw by stalemate!");
            return;
        }

        if (this.board.isFiftyMoveRule()) {
            this.endGame("Draw by 50-move rule!");
            return;
        }

        if (this.board.isInsufficientMaterial()) {
            this.endGame("Draw by insufficient material!");
            return;
        }
    }

    endGame(message) {
        this.gameOverFlag = true;
        this.gameActive = false;
        document.getElementById('gameOverMessage').textContent = message;
        document.getElementById('gameOverMessage').style.display = 'block';
        document.getElementById('startBtn').disabled = false;
        document.getElementById('player1').disabled = false;
        document.getElementById('player2').disabled = false;
    }

    renderBoard() {
        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const square = document.getElementById(`square-${row}-${col}`);
                const piece = this.board.board[row][col];

                // Reset classes
                square.className = `square ${(row + col) % 2 === 0 ? 'light' : 'dark'}`;

                // Add selected highlight
                if (this.selectedSquare && this.selectedSquare[0] === row && this.selectedSquare[1] === col) {
                    square.classList.add('selected');
                }

                // Add valid move highlights
                if (this.validMoves.some(m => m[0] === row && m[1] === col)) {
                    square.classList.add('valid-move');
                }

                // Add check highlight
                if (this.board.isInCheck(this.currentPlayer)) {
                    const kingPos = this.currentPlayer === 'w' 
                        ? [this.board.kingPosition[0], this.board.kingPosition[1]]
                        : [this.board.kingPosition[2], this.board.kingPosition[3]];
                    if (kingPos[0] === row && kingPos[1] === col) {
                        square.classList.add('in-check');
                    }
                }

                // Render piece
                const pieceContent = square.querySelector('.coordinate') ? 
                    Array.from(square.querySelectorAll('.coordinate')) : [];
                square.innerHTML = '';
                pieceContent.forEach(el => square.appendChild(el));
                
                if (piece) {
                    const pieceSymbol = document.createElement('span');
                    pieceSymbol.textContent = piece.toString();
                    pieceSymbol.style.pointerEvents = 'none';
                    square.insertBefore(pieceSymbol, square.firstChild);
                }
            }
        }
    }

    updateUI() {
        document.getElementById('currentTurn').textContent = this.currentPlayer === 'w' ? '♙ White' : '♟ Black';
        document.getElementById('moveCount').textContent = this.moveCount;
        document.getElementById('lastMove').textContent = this.moveHistory.length > 0 
            ? this.moveHistory[this.moveHistory.length - 1] 
            : '-';

        // Update move log
        const moveLog = document.getElementById('moveLog');
        moveLog.innerHTML = this.moveHistory.map((move, idx) => 
            `<div class="log-entry">Move ${idx + 1}: ${move}</div>`
        ).join('');
        moveLog.scrollTop = moveLog.scrollHeight;

        // Render board
        this.renderBoard();
    }
}

// Initialize game when page loads
document.addEventListener('DOMContentLoaded', () => {
    new GameController();
});