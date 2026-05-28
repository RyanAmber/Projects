import random
from typing import Optional, List, Tuple


class TicTacToeBoard:
    
    def __init__(self, other=None):
        if other:
            self.grid = [
                [
                    [row[:] for row in sub_board]
                    for sub_board in row
                ]
                for row in other.grid
            ]
        else:
            self.grid = [
                [
                    [
                        [" " for _ in range(3)]
                        for _ in range(3)
                    ]
                    for _ in range(3)
                ]
                for _ in range(3)
            ]
    
    def isValid(self, corner: int, move: int) -> bool:
        if move < 1 or move > 9:
            return False
        m = move - 1
        c = corner - 1
        return self.grid[c // 3][c % 3][m // 3][m % 3] == " "
    
    def makeMove(self, corner: int, move: int, team: str):
        m = move - 1
        c = corner - 1
        self.grid[c // 3][c % 3][m // 3][m % 3] = team
    
    def winner(self, corner: int) -> str:
        c = corner - 1
        g = self.grid[c // 3][c % 3]
        
        for r in range(3):
            if g[r][0] != " " and g[r][0] == g[r][1] and g[r][0] == g[r][2]:
                return g[r][0]
        
        for col in range(3):
            if g[0][col] != " " and g[0][col] == g[1][col] and g[0][col] == g[2][col]:
                return g[0][col]

        if g[0][0] != " " and g[0][0] == g[1][1] and g[0][0] == g[2][2]:
            return g[0][0]
        if g[0][2] != " " and g[0][2] == g[1][1] and g[0][2] == g[2][0]:
            return g[0][2]
        
        return " "
    
    def full(self, corner: int) -> bool:
        c = corner - 1
        sub = self.grid[c // 3][c % 3]
        for r in range(3):
            for col in range(3):
                if sub[r][col] == " ":
                    return False
        return True
    
    def _sum(self, arr: List[str], team: str) -> int:
        opp = "O" if team == "X" else "X"
        total = 0
        for v in arr:
            if v == team:
                total += 1
            elif v == opp:
                total -= 1
        return total
    
    def _check(self, arr: List[str], team: str, check_type: int) -> int:
        """Check scoring for a line"""
        s = self._sum(arr, team)
        if s == 2:
            if check_type == 1:
                return 10
            if check_type == 2:
                return -10
            if check_type == 4:
                return 1000
            return 20
        if s == -2:
            return -18
        return 0
    
    def _lines(self, g):
        """Get all winning lines from a 3x3 grid"""
        return [
            [g[0][0], g[1][0], g[2][0]],
            [g[0][1], g[1][1], g[2][1]],
            [g[0][2], g[1][2], g[2][2]],
            [g[0][0], g[1][1], g[2][2]],
            [g[0][2], g[1][1], g[2][0]],
            [g[0][0], g[0][1], g[0][2]],
            [g[1][0], g[1][1], g[1][2]],
            [g[2][0], g[2][1], g[2][2]],
        ]
    
    def cornerCheck(self, i: int, j: int, test, team: str, check_type: int) -> int:
        """Calculate score for a corner"""
        score = 0
        for line in self._lines(test.grid[i][j]):
            score += self._check(line, team, check_type)
        return score
    
    def boardCheck(self, wins, team: str, check_type: int) -> int:
        """Calculate score for the overall board"""
        score = 0
        for line in self._lines(wins):
            score += self._check(line, team, check_type)
        return score
    
    def calcWorth(self, wins, corner: int, team: str) -> int:
        """Calculate the worth of a corner"""
        worth = 0
        if corner == 5:
            worth = 6
            for board in range(1, 10):
                if board == 5:
                    continue
                r1 = (board - 1) // 3
                c1 = (board - 1) % 3
                r2 = (9 - board - 1) // 3
                c2 = (9 - board - 1) % 3
                if wins[r1][c1] == team and wins[r2][c2] == team:
                    worth += 2
                elif wins[r1][c1] == team and wins[r2][c2] == " ":
                    worth += 1
        elif corner % 2 == 1:
            worth = 4
            worth += self._sum(wins[corner // 3], team)
        else:
            worth = 2
        return worth
    
    def score(self, wins, corner: int, move: int, player: int, team: str) -> int:
        """Calculate score for a move"""
        test = TicTacToeBoard(self)
        sc = 0
        nw = [row[:] for row in wins]
        test.makeMove(corner, move, team)
        
        if player == 3:
            if move == 5:
                sc += 3
            elif move % 2 == 1:
                sc += 1
            
            c = corner - 1
            i = c // 3
            j = c % 3
            worth = 3 if corner == 5 else (2 if c % 2 == 0 else 1)
            
            if test.winner(corner) == team:
                sc += 50 * worth
                nw[i][j] = team
            
            sc += self.cornerCheck(i, j, test, team, 1) * worth
            worth = 4 if move == 5 else (3 if move % 2 == 1 else 2)
            sc += self.cornerCheck((move - 1) // 3, (move - 1) % 3, test, team, 2) * worth
            
            if wins[(move - 1) // 3][(move - 1) % 3] != " ":
                sc -= 25
        
        elif player == 4:
            if move == 5 and corner != 5:
                sc += 3
            elif move % 2 == 1 and move != 5:
                sc += 1
            
            c = corner - 1
            i = c // 3
            j = c % 3
            worth = self.calcWorth(wins, corner, team) if corner == 5 else (3 if c % 2 == 0 else 2)
            
            if test.winner(corner) == team:
                sc += 50 * worth
                nw[i][j] = team
            
            sc += self.cornerCheck(i, j, test, team, 1) * worth
            worth = self.calcWorth(wins, move, team) if move == 5 else (3 if move % 2 == 1 else 2)
            sc += self.cornerCheck((move - 1) // 3, (move - 1) % 3, test, team, 2) * worth
            
            if wins[(move - 1) // 3][(move - 1) % 3] != " ":
                sc -= 75
            
            sc += 10 * self.boardCheck(wins, team, 1)
        
        return sc
    
    def cornerScore(self, corner: int, player: int, team: str, wins) -> int:
        """Calculate score for choosing a corner"""
        score = 0
        ci = (corner - 1) // 3
        cj = (corner - 1) % 3
        score += self.cornerCheck(ci, cj, self, team, 3)
        
        if corner == 5:
            score += 2
        elif corner % 2 == 1:
            score += 1
        
        nw = [row[:] for row in wins]
        if self.cornerCheck(ci, cj, self, team, 4) > 10000:
            nw[ci][cj] = team
        
        if TicTacToeGame.winner(nw) == team:
            score += 200
        
        if player == 4:
            score += 10 * self.boardCheck(nw, team, 1)
        
        nw[ci][cj] = "O" if team == "X" else "X"
        if TicTacToeGame.winner(nw) == ("O" if team == "X" else "X"):
            score -= 100
        
        if player == 4:
            best_move_score = -10000
            for i in range(1, 10):
                if self.isValid(corner, i):
                    move_score = self.score(wins, corner, i, player, team)
                    if move_score > best_move_score:
                        best_move_score = move_score
            
            if best_move_score > -10000:
                score += best_move_score * 0.25
        
        return score


class TicTacToeGame:
    """Static methods for game logic"""
    
    @staticmethod
    def winner(grid) -> str:
        """Check if there's a winner on the 3x3 board"""
        # Check rows
        for r in range(3):
            if grid[r][0] != " " and grid[r][0] == grid[r][1] and grid[r][0] == grid[r][2]:
                return grid[r][0]
        
        # Check columns
        for c in range(3):
            if grid[0][c] != " " and grid[0][c] == grid[1][c] and grid[0][c] == grid[2][c]:
                return grid[0][c]
        
        # Check diagonals
        if grid[1][1] != " " and grid[0][0] == grid[1][1] and grid[1][1] == grid[2][2]:
            return grid[1][1]
        if grid[1][1] != " " and grid[0][2] == grid[1][1] and grid[1][1] == grid[2][0]:
            return grid[1][1]
        
        return " "
    
    @staticmethod
    def fullGrid(sub) -> bool:
        """Check if a sub-board is full"""
        for r in range(3):
            for c in range(3):
                if sub[r][c] == " ":
                    return False
        return True
    
    @staticmethod
    def getValidBoards(board: TicTacToeBoard, wins) -> List[int]:
        """Get valid boards for the next move"""
        valid = []
        for i in range(3):
            for j in range(3):
                if wins[i][j] == " " and not TicTacToeGame.fullGrid(board.grid[i][j]):
                    valid.append(i * 3 + j + 1)
        return valid
    
    @staticmethod
    def getMove(board: TicTacToeBoard, corner: int, player: int, team: str, wins) -> Optional[int]:
        """Get the next move for AI or None for human"""
        if player == 1:
            return None
        
        if player == 2:
            while True:
                a = random.randint(1, 9)
                if board.isValid(corner, a):
                    return a
        
        # Player 3 or 4
        max_score = -10000
        possible = []
        
        for i in range(1, 10):
            if not board.isValid(corner, i):
                continue
            sc = board.score(wins, corner, i, player, team)
            if sc > max_score:
                possible = [i]
                max_score = sc
            elif sc == max_score:
                possible.append(i)
        
        if not possible:
            return -1
        
        return random.choice(possible)
    
    @staticmethod
    def getCorner(board: TicTacToeBoard, wins, player: int, team: str) -> Optional[int]:
        """Get the corner choice for AI or None for human"""
        valid = TicTacToeGame.getValidBoards(board, wins)
        if not valid:
            return -1
        
        if player == 1:
            return None
        
        if player == 2:
            return random.choice(valid)
        
        # Player 3 or 4
        top_score = -5000
        options = []
        
        for i in valid:
            attempt = board.cornerScore(i, 4 if player >= 4 else player, team, wins)
            if attempt > top_score:
                options = [i]
                top_score = attempt
            elif attempt == top_score:
                options.append(i)
        
        return random.choice(options)


class SuperTicTacToeGame:
    """Main game controller"""
    
    def __init__(self):
        self.board = None
        self.wins = None
        self.current_player = 1
        self.current_corner = 5
        self.p1type = 1
        self.p2type = 3
        self.game_over = False
        self.choosing_board = False
        self.scores = {"X": 0, "O": 0}
    
    def get_player_type(self, player=None):
        if player is None:
            player = self.current_player
        return self.p1type if player == 1 else self.p2type
    
    def get_team(self, player=None):
        if player is None:
            player = self.current_player
        return "X" if player == 1 else "O"
    
    def needs_board_choice(self):
        oi = (self.current_corner - 1) // 3
        oj = (self.current_corner - 1) % 3
        return self.wins[oi][oj] != " " or TicTacToeGame.fullGrid(self.board.grid[oi][oj])
    
    def init_game(self):
        self.board = TicTacToeBoard()
        self.wins = [[" " for _ in range(3)] for _ in range(3)]
        self.current_player = 1
        self.current_corner = 5
        self.game_over = False
        self.choosing_board = False
        self.scores = {"X": 0, "O": 0}
    
    def render_board(self):
        """Display the board in terminal"""
        print("\n" + "=" * 80)
        print(f"Scores: X-Boards: {self.scores['X']} | O-Boards: {self.scores['O']}")
        print("=" * 80)
        
        for oi in range(3):
            # Top border for this row of sub-boards
            for oj in range(3):
                print("+-------", end="")
            print("+")
            
            # Content rows for this row of sub-boards
            for ci in range(3):
                for oj in range(3):
                    print("|", end="")
                    for cj in range(3):
                        val = self.board.grid[oi][oj][ci][cj]
                        print(f" {val} ", end="")
                print("|")
            
            # Check if this row of boards has a winner
            for oj in range(3):
                win = self.wins[oi][oj]
                if win != " ":
                    print(f" [{win}] ", end="")
                else:
                    print(" [ ] ", end="")
        
        # Bottom border
        for oj in range(3):
            print("+-------", end="")
        print("+")
        print()
    
    def render_board_positions(self):
        """Display board and move positions"""
        print("\nBoard Positions (for choosing corners):")
        print("+-------+-------+-------+")
        print("| 1 2 3 | 1 2 3 | 1 2 3 |")
        print("| 4 5 6 | 4 5 6 | 4 5 6 |")
        print("| 7 8 9 | 7 8 9 | 7 8 9 |")
        print("+-------+-------+-------+")
        print("| 1 2 3 | 1 2 3 | 1 2 3 |")
        print("| 4 5 6 | 4 5 6 | 4 5 6 |")
        print("| 7 8 9 | 7 8 9 | 7 8 9 |")
        print("+-------+-------+-------+")
        print("| 1 2 3 | 1 2 3 | 1 2 3 |")
        print("| 4 5 6 | 4 5 6 | 4 5 6 |")
        print("| 7 8 9 | 7 8 9 | 7 8 9 |")
        print("+-------+-------+-------+")
        print("(Corners: 1=top-left, 3=top-right, 5=center, 7=bottom-left, 9=bottom-right)")
    
    def get_human_move(self):
        """Get move from human player"""
        while True:
            try:
                move = int(input(f"Enter move (1-9): "))
                if 1 <= move <= 9 and self.board.isValid(self.current_corner, move):
                    return move
                else:
                    print("Invalid move! Cell is occupied or out of range.")
            except ValueError:
                print("Invalid input! Enter a number 1-9.")
    
    def get_human_corner(self):
        """Get corner choice from human player"""
        valid = TicTacToeGame.getValidBoards(self.board, self.wins)
        print(f"Valid boards: {valid}")
        
        while True:
            try:
                corner = int(input(f"Enter board (1-9): "))
                if corner in valid:
                    return corner
                else:
                    print(f"Invalid board! Must be one of: {valid}")
            except ValueError:
                print("Invalid input! Enter a number 1-9.")
    
    def execute_move(self, corner: int, move: int):
        """Execute a move"""
        if self.game_over:
            return
        
        team = self.get_team()
        self.board.makeMove(corner, move, team)
        
        # Update sub-board win state
        oi = (corner - 1) // 3
        oj = (corner - 1) % 3
        w = self.board.winner(corner)
        
        if w != " ":
            self.wins[oi][oj] = w
            self.scores[w] += 1
        elif self.board.full(corner):
            self.wins[oi][oj] = "C"
        
        # Check overall game winner
        overall = TicTacToeGame.winner(self.wins)
        if overall != " ":
            self.game_over = True
            return
        
        # Check all boards exhausted
        if all(self.wins[r][c] != " " for r in range(3) for c in range(3)):
            self.game_over = True
            return
        
        # Switch player
        self.current_player = 2 if self.current_player == 1 else 1
        self.current_corner = move
        
        # Determine if next player needs to choose a board
        self.choosing_board = self.needs_board_choice()
        
        if self.choosing_board:
            valid = TicTacToeGame.getValidBoards(self.board, self.wins)
            if not valid:
                self.game_over = True
    
    def run_turn(self):
        """Execute one turn"""
        if self.game_over:
            return
        
        player_type = self.get_player_type()
        team = self.get_team()
        
        self.render_board()
        
        if self.choosing_board:
            print(f"Player {self.current_player} ({team}) must choose a board.")
            self.render_board_positions()
            
            if player_type == 1:
                corner = self.get_human_corner()
                self.current_corner = corner
                self.choosing_board = False
            else:
                corner = TicTacToeGame.getCorner(self.board, self.wins, player_type, team)
                print(f"AI chooses board {corner}")
                self.current_corner = corner
                self.choosing_board = False
        else:
            print(f"Player {self.current_player} ({team}) plays in board {self.current_corner}")
            
            if player_type == 1:
                move = self.get_human_move()
                print(f"Player {self.current_player} plays position {move}")
            else:
                move = TicTacToeGame.getMove(self.board, self.current_corner, player_type, team, self.wins)
                if move == -1:
                    print("AI has no valid moves!")
                    self.game_over = True
                    return
                print(f"AI plays position {move}")
            
            self.execute_move(self.current_corner, move)
    
    def show_result(self):
        """Display game result"""
        self.render_board()
        overall = TicTacToeGame.winner(self.wins)
        
        if overall != " ":
            winner_num = 1 if overall == "X" else 2
            type_names = ["", "Human", "Easy", "Hard", "Extreme"]
            player_type = self.p1type if overall == "X" else self.p2type
            print(f"\n🏆 Player {winner_num} ({overall}) wins! ({type_names[player_type]})")
        else:
            print("\n🤝 Draw game! No more moves available.")
        
        print(f"Final scores - X-Boards: {self.scores['X']}, O-Boards: {self.scores['O']}")
    
    def setup_game(self):
        print("Player difficulty levels:")
        print("1. Human")
        print("2. Easy (random moves)")
        print("3. Hard (smarter AI)")
        print("4. Extreme (best AI)")
        print()
        
        while True:
            try:
                p1 = int(input("Player 1 (X) difficulty (1-4): "))
                if 1 <= p1 <= 4:
                    self.p1type = p1
                    break
                print("Please enter 1-4")
            except ValueError:
                print("Invalid input!")
        
        while True:
            try:
                p2 = int(input("Player 2 (O) difficulty (1-4, default 3): "))
                if 1 <= p2 <= 4:
                    self.p2type = p2
                    break
                print("Please enter 1-4")
            except ValueError:
                print("Invalid input!")
        
        self.init_game()
    
    def play(self):
        self.setup_game()
        
        while not self.game_over:
            self.run_turn()
        
        self.show_result()
        
        while True:
            again = input("\nPlay again? (y/n): ").lower()
            if again == "y":
                self.play()
                return
            elif again == "n":
                print("Thanks for playing!")
                return
            else:
                print("Invalid input! Enter 'y' or 'n'")


def main():
    game = SuperTicTacToeGame()
    game.play()


if __name__ == "__main__":
    main()
