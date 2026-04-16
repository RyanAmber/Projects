class TicTacToeBoard {
  constructor(other) {
    if (other) {
      this.grid = other.grid.map(row =>
        row.map(col =>
          col.map(r => [...r])
        )
      );
    } else {
      this.grid = Array.from({length: 3}, () =>
        Array.from({length: 3}, () =>
          Array.from({length: 3}, () => Array(3).fill(" "))
        )
      );
    }
  }

  isValid(corner, move) {
    if (move < 1 || move > 9) return false;
    const m = move - 1, c = corner - 1;
    return this.grid[Math.floor(c/3)][c%3][Math.floor(m/3)][m%3] === " ";
  }

  makeMove(corner, move, team) {
    const m = move - 1, c = corner - 1;
    this.grid[Math.floor(c/3)][c%3][Math.floor(m/3)][m%3] = team;
  }

  winner(corner) {
    const c = corner - 1;
    const g = this.grid[Math.floor(c/3)][c%3];
    // rows
    for (let r = 0; r < 3; r++)
      if (g[r][0] !== " " && g[r][0] === g[r][1] && g[r][0] === g[r][2]) return g[r][0];
    // cols
    for (let col = 0; col < 3; col++)
      if (g[0][col] !== " " && g[0][col] === g[1][col] && g[0][col] === g[2][col]) return g[0][col];
    // diagonals
    if (g[0][0] !== " " && g[0][0] === g[1][1] && g[0][0] === g[2][2]) return g[0][0];
    if (g[0][2] !== " " && g[0][2] === g[1][1] && g[0][2] === g[2][0]) return g[0][2];
    return " ";
  }

  full(corner) {
    const c = corner - 1;
    const sub = this.grid[Math.floor(c/3)][c%3];
    for (let r = 0; r < 3; r++)
      for (let col = 0; col < 3; col++)
        if (sub[r][col] === " ") return false;
    return true;
  }

  _sum(arr, team) {
    const opp = team === "X" ? "O" : "X";
    let total = 0;
    for (const v of arr) {
      if (v === team) total++;
      else if (v === opp) total--;
    }
    return total;
  }

  _check(arr, team, type) {
    const s = this._sum(arr, team);
    if (s === 2) {
      if (type === 1) return 10;
      if (type === 2) return -10;
      if (type === 4) return 100000000;
      return 20;
    }
    if (s === -2) return -18;
    return 0;
  }

  _lines(g) {
    return [
      [g[0][0], g[1][0], g[2][0]],
      [g[0][1], g[1][1], g[2][1]],
      [g[0][2], g[1][2], g[2][2]],
      [g[0][0], g[1][1], g[2][2]],
      [g[0][2], g[1][1], g[2][0]],
      [g[0][0], g[0][1], g[0][2]],
      [g[1][0], g[1][1], g[1][2]],
      [g[2][0], g[2][1], g[2][2]],
    ];
  }

  cornerCheck(i, j, test, team, type) {
    let score = 0;
    for (const line of this._lines(test.grid[i][j]))
      score += this._check(line, team, type);
    return score;
  }

  boardCheck(wins, team, type) {
    let score = 0;
    for (const line of this._lines(wins))
      score += this._check(line, team, type);
    return score;
  }

  calcWorth(wins, corner, team) {
    let worth = 0;
    if (corner === 5) {
      worth = 6;
      for (let board = 1; board < 10; board++) {
        if (board === 5) continue;
        const r1 = Math.floor((board-1)/3), c1 = (board-1)%3;
        const r2 = Math.floor((10-board-1)/3), c2 = (10-board-1)%3;
        if (wins[r1][c1] === team && wins[r2][c2] === team) worth += 2;
        else if (wins[r1][c1] === team && wins[r2][c2] === " ") worth++;
      }
    } else if (corner % 2 === 1) {
      worth = 4;
      worth += this._sum(wins[Math.floor((corner-1)/3)], team);
    } else {
      worth = 2;
    }
    return worth;
  }

  score(wins, corner, move, player, team) {
    const test = new TicTacToeBoard(this);
    let sc = 0;
    const nw = wins.map(r => [...r]);
    test.makeMove(corner, move, team);

    if (player === 3) {
      if (move === 5) sc += 3;
      else if (move % 2 === 1) sc++;
      const c = corner - 1;
      const i = Math.floor(c/3), j = c % 3;
      let worth = (corner === 5) ? 3 : (c % 2 === 0 ? 2 : 1);
      if (test.winner(corner) === team) { sc += 50 * worth; nw[i][j] = team; }
      sc += this.cornerCheck(i, j, test, team, 1) * worth;
      worth = (move === 5) ? 4 : (move % 2 === 1 ? 3 : 2);
      sc += this.cornerCheck(Math.floor((move-1)/3), (move-1)%3, test, team, 2) * worth;
      if (wins[Math.floor((move-1)/3)][(move-1)%3] !== " ") sc -= 25;

    } else if (player === 4) {
      if (move === 5 && corner !== 5) sc += 3;
      else if (move % 2 === 1 && move !== 5) sc++;
      const c = corner - 1;
      const i = Math.floor(c/3), j = c % 3;
      let worth = (corner === 5) ? this.calcWorth(wins, corner, team) : (c % 2 === 0 ? 3 : 2);
      if (test.winner(corner) === team) { sc += 50 * worth; nw[i][j] = team; }
      sc += this.cornerCheck(i, j, test, team, 1) * worth;
      worth = (move === 5) ? this.calcWorth(wins, move, team) : (move % 2 === 1 ? 3 : 2);
      sc += this.cornerCheck(Math.floor((move-1)/3), (move-1)%3, test, team, 2) * worth;
      if (wins[Math.floor((move-1)/3)][(move-1)%3] !== " ") sc -= 75;
      sc += 10 * this.boardCheck(wins, team, 1);
    }
    return sc;
  }

  cornerScore(corner, player, team, wins) {
    let score = 0;
    const ci = Math.floor((corner-1)/3), cj = (corner-1)%3;
    score += this.cornerCheck(ci, cj, this, team, 3);
    if (corner === 5) score += 2;
    else if (corner % 2 === 1) score++;

    const nw = wins.map(r => [...r]);
    if (this.cornerCheck(ci, cj, this, team, 4) > 10000) nw[ci][cj] = team;
    if (TicTacToeGame.winner(nw) === team) score += 200;
    if (player === 4) score += 10 * this.boardCheck(nw, team, 1);

    nw[ci][cj] = team === "X" ? "O" : "X";
    if (TicTacToeGame.winner(nw) === (team === "X" ? "O" : "X")) score -= 100;
    return score;
  }
}

// ============================================================
// TicTacToeGame — translated static methods
// ============================================================
class TicTacToeGame {
  static winner(grid) {
    for (let r = 0; r < 3; r++)
      if (grid[r][0] !== " " && grid[r][0] === grid[r][1] && grid[r][0] === grid[r][2]) return grid[r][0];
    for (let c = 0; c < 3; c++)
      if (grid[0][c] !== " " && grid[0][c] === grid[1][c] && grid[0][c] === grid[2][c]) return grid[0][c];
    // Note: original Java checks grid[1][1] != " " then grid[0][0].equals(grid[1][1]) — preserved here
    if (grid[1][1] !== " " && grid[0][0] === grid[1][1] && grid[1][1] === grid[2][2]) return grid[1][1];
    if (grid[1][1] !== " " && grid[0][2] === grid[1][1] && grid[1][1] === grid[2][0]) return grid[1][1];
    return " ";
  }

  static fullGrid(sub) {
    for (let r = 0; r < 3; r++)
      for (let c = 0; c < 3; c++)
        if (sub[r][c] === " ") return false;
    return true;
  }

  static getValidBoards(board, wins) {
    const valid = [];
    for (let i = 0; i < 3; i++)
      for (let j = 0; j < 3; j++)
        if (wins[i][j] === " " && !TicTacToeGame.fullGrid(board.grid[i][j]))
          valid.push(i * 3 + j + 1);
    return valid;
  }

  // Returns a move (1–9) for AI, or null for human
  static getMove(board, corner, player, team, wins) {
    if (player === 1) return null;
    if (player === 2) {
      let a;
      do { a = Math.floor(Math.random() * 9) + 1; } while (!board.isValid(corner, a));
      return a;
    }
    // player 3 or 4
    let max = -10000, possible = [];
    for (let i = 1; i <= 9; i++) {
      if (!board.isValid(corner, i)) continue;
      const sc = board.score(wins, corner, i, player, team);
      if (sc > max) { possible = [i]; max = sc; }
      else if (sc === max) possible.push(i);
    }
    if (possible.length === 0) return -1;
    return possible[Math.floor(Math.random() * possible.length)];
  }

  // Returns a board number for AI, or null for human
  static getCorner(board, wins, player, team) {
    const valid = TicTacToeGame.getValidBoards(board, wins);
    if (valid.length === 0) return -1;
    if (player === 1) return null;
    if (player === 2) return valid[Math.floor(Math.random() * valid.length)];
    // player 3 or 4
    let topScore = -5000, options = [];
    for (const i of valid) {
      const attempt = board.cornerScore(i, player >= 4 ? 4 : player, team, wins);
      if (attempt > topScore) { options = [i]; topScore = attempt; }
      else if (attempt === topScore) options.push(i);
    }
    return options[Math.floor(Math.random() * options.length)];
  }
}

// ============================================================
// Game State & Controller
// ============================================================
let board, wins, currentPlayer, currentCorner, p1type, p2type;
let gameOver, choosingBoard, aiThinking;
let scores = { X: 0, O: 0 };

function startGame() {
  p1type = parseInt(document.getElementById('p1type').value);
  p2type = parseInt(document.getElementById('p2type').value);
  scores = { X: 0, O: 0 };
  initGame();
  document.getElementById('setup').style.display = 'none';
  document.getElementById('game').style.display = 'block';
  document.getElementById('win-banner').classList.remove('show');
}

function rematch() {
  document.getElementById('win-banner').classList.remove('show');
  scores = { X: 0, O: 0 };
  initGame();
}

function initGame() {
  board = new TicTacToeBoard();
  wins = Array.from({length: 3}, () => Array(3).fill(" "));
  currentPlayer = 1;
  currentCorner = 5;
  gameOver = false;
  choosingBoard = false;
  aiThinking = false;
  updateScoreDisplay();
  render();
  scheduleNextAction();
}

function showSetup() {
  document.getElementById('win-banner').classList.remove('show');
  document.getElementById('game').style.display = 'none';
  document.getElementById('setup').style.display = 'flex';
}

function getPlayerType(p) {
  p = p || currentPlayer;
  return p === 1 ? p1type : p2type;
}

function getTeam(p) {
  p = p || currentPlayer;
  return p === 1 ? "X" : "O";
}

function setStatus(msg, cls) {
  const el = document.getElementById('status');
  el.className = cls || '';
  el.textContent = msg;
}

function updateScoreDisplay() {
  document.getElementById('score-x').textContent = `X-Boards: ${scores.X}`;
  document.getElementById('score-o').textContent = `O-Boards: ${scores.O}`;
}

function needsBoardChoice() {
  const oi = Math.floor((currentCorner-1)/3), oj = (currentCorner-1)%3;
  return wins[oi][oj] !== " " || TicTacToeGame.fullGrid(board.grid[oi][oj]);
}

// After rendering, decide what to do next
function scheduleNextAction() {
  if (gameOver) return;
  const ptype = getPlayerType();
  const team = getTeam();

  if (choosingBoard) {
    if (ptype === 1) {
      setStatus(`🎯 Player ${currentPlayer} (${team}) — board ${currentCorner} is taken. Click a glowing board!`, team === 'X' ? 'x-turn' : 'o-turn');
    } else {
      setStatus(`Player ${currentPlayer} (${team}) is choosing a board`, (team === 'X' ? 'x-turn' : 'o-turn') + ' thinking-dots');
      setTimeout(aiChooseBoard, 400);
    }
  } else {
    if (ptype === 1) {
      setStatus(`▶ Player ${currentPlayer} (${team}) — pick your move in board ${currentCorner}`, team === 'X' ? 'x-turn' : 'o-turn');
    } else {
      setStatus(`Player ${currentPlayer} (${team}) is thinking`, (team === 'X' ? 'x-turn' : 'o-turn') + ' thinking-dots');
      setTimeout(aiMakeMove, 400);
    }
  }
}

function aiChooseBoard() {
  if (gameOver) return;
  const ptype = getPlayerType();
  const corner = TicTacToeGame.getCorner(board, wins, ptype, getTeam());
  currentCorner = corner;
  choosingBoard = false;
  render();
  setStatus(`Player ${currentPlayer} (${getTeam()}) is thinking`, (getTeam() === 'X' ? 'x-turn' : 'o-turn') + ' thinking-dots');
  setTimeout(aiMakeMove, 300);
}

function aiMakeMove() {
  if (gameOver) return;
  const ptype = getPlayerType();
  const move = TicTacToeGame.getMove(board, currentCorner, ptype, getTeam(), wins);
  if (move == null || move === -1) return;
  executeMove(currentCorner, move);
}

function executeMove(corner, move) {
  if (gameOver) return;
  const team = getTeam();
  board.makeMove(corner, move, team);

  // Update sub-board win state
  const oi = Math.floor((corner-1)/3), oj = (corner-1)%3;
  const w = board.winner(corner);
  if (w !== " ") {
    wins[oi][oj] = w;
    scores[w]++;
    updateScoreDisplay();
  } else if (board.full(corner)) {
    wins[oi][oj] = "C";
  }

  // Check overall game winner
  const overall = TicTacToeGame.winner(wins);
  if (overall !== " ") {
    gameOver = true;
    render();
    showWinBanner(overall);
    return;
  }

  // Check all boards exhausted
  const allDone = wins.every(row => row.every(v => v !== " "));
  if (allDone) {
    gameOver = true;
    render();
    showWinBanner(null);
    return;
  }

  // Switch player and set next corner
  currentPlayer = currentPlayer === 1 ? 2 : 1;
  currentCorner = move;

  // Determine if next player needs to choose a board
  choosingBoard = needsBoardChoice();
  if (choosingBoard) {
    const valid = TicTacToeGame.getValidBoards(board, wins);
    if (valid.length === 0) {
      gameOver = true;
      render();
      showWinBanner(null);
      return;
    }
    // For AI: auto-pick. For human: currentCorner stays as hint, board clicking is enabled
  }

  render();
  scheduleNextAction();
}

function showWinBanner(winner) {
  const textEl = document.getElementById('win-banner-text');
  const subEl = document.getElementById('win-banner-sub');
  if (winner) {
    textEl.textContent = `Player ${winner === 'X' ? 1 : 2} Wins!`;
    textEl.className = 'win-banner-text ' + winner;
    const ptype = winner === 'X' ? p1type : p2type;
    const types = ['', 'Human', 'Easy', 'Hard', 'Extreme'];
    subEl.textContent = `${winner} (${types[ptype]}) claims victory`;
    setStatus(`🏆 Player ${winner === 'X' ? 1 : 2} (${winner}) wins!`, 'win');
  } else {
    textEl.textContent = "Draw!";
    textEl.className = 'win-banner-text tie';
    subEl.textContent = "No more moves available";
    setStatus("Draw game!", 'win');
  }
  setTimeout(() => document.getElementById('win-banner').classList.add('show'), 600);
}

// ============================================================
// Render
// ============================================================
function render() {
  const container = document.getElementById('outer-board');
  container.innerHTML = '';
  const ptype = getPlayerType();
  const validBoards = choosingBoard ? TicTacToeGame.getValidBoards(board, wins) : [];

  for (let oi = 0; oi < 3; oi++) {
    for (let oj = 0; oj < 3; oj++) {
      const boardNum = oi * 3 + oj + 1;
      const div = document.createElement('div');
      div.className = 'sub-board';

      const isActive = !gameOver && !choosingBoard && boardNum === currentCorner && wins[oi][oj] === " ";
      const isChoosable = !gameOver && choosingBoard && validBoards.includes(boardNum);

      if (isActive) div.classList.add('active');
      if (isChoosable) {
        div.classList.add('choosable');
        if (ptype === 1) {
          div.addEventListener('click', () => handleBoardChoice(boardNum));
        }
      }

      // Win overlay
      const winVal = wins[oi][oj];
      if (winVal !== " ") {
        const overlay = document.createElement('div');
        overlay.className = 'win-overlay ' + winVal;
        overlay.textContent = winVal === 'C' ? 'DRAW' : winVal;
        div.appendChild(overlay);
      }

      // Cells
      for (let ci = 0; ci < 3; ci++) {
        for (let cj = 0; cj < 3; cj++) {
          const moveNum = ci * 3 + cj + 1;
          const val = board.grid[oi][oj][ci][cj];
          const cell = document.createElement('div');
          cell.className = 'cell' + (val !== " " ? ' ' + val : '');
          cell.textContent = val === " " ? "" : val;

          const humanCanClick = !gameOver && isActive && val === " " && !choosingBoard && ptype === 1;
          if (humanCanClick) {
            cell.classList.add('clickable');
            const bn = boardNum, mn = moveNum;
            cell.addEventListener('click', () => handleCellClick(bn, mn));
          }
          div.appendChild(cell);
        }
      }
      container.appendChild(div);
    }
  }
}

function handleBoardChoice(boardNum) {
  if (!choosingBoard || gameOver) return;
  if (getPlayerType() !== 1) return;
  currentCorner = boardNum;
  choosingBoard = false;
  render();
  scheduleNextAction();
}

function handleCellClick(boardNum, moveNum) {
  if (gameOver || choosingBoard) return;
  if (getPlayerType() !== 1) return;
  if (boardNum !== currentCorner) return;
  if (!board.isValid(boardNum, moveNum)) return;
  executeMove(boardNum, moveNum);
}