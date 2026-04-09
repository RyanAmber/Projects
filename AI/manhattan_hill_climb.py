"""
Manhattan Distance Hospital Placement — Hill Climbing Visualizer
================================================================
1-indexed grid.  x increases left→right, y increases bottom→top.

Default grid : 10 × 5
Default houses : (2,2) (3,4) (7,1) (9,5)
Default hospitals: (5,5) (10,2)

Controls
--------
• Click "Hill Climb Step" to move one hospital by one cell to reduce total cost.
• Click "Add House" then click a cell to place a new house.
• Click "Add Hospital" then click a cell to place a new hospital.
• Click "Reset" to restore defaults.
"""

import tkinter as tk

# ── Defaults ────────────────────────────────────────────────────
DEFAULT_N = 10          # columns  (x: 1..N)
DEFAULT_M = 5           # rows     (y: 1..M)
DEFAULT_HOUSES = [(2, 2), (3, 4), (7, 1), (9, 5)]
DEFAULT_HOSPITALS = [(5, 5), (10, 2)]

CELL = 64               # pixel size of one cell
PAD = 48                # canvas margin for axis labels

# colours
COL_GRID      = "#d0d0d0"
COL_HOUSE     = "#e74c3c"
COL_HOSPITAL  = "#2980b9"
COL_PATH      = "#3498db"
COL_PATH_ALT  = "#e67e22"
COL_BG        = "#f7f7f7"
COL_CELL_BG   = "#ffffff"
COL_HIGHLIGHT = "#ffffcc"

# ── Helpers ─────────────────────────────────────────────────────

def manhattan(a, b):
    return abs(a[0] - b[0]) + abs(a[1] - b[1])


def total_cost(houses, hospitals):
    if not hospitals:
        return float("inf")
    return sum(min(manhattan(h, hp) for hp in hospitals) for h in houses)


def nearest_hospital(house, hospitals):
    return min(hospitals, key=lambda hp: manhattan(house, hp))


def path_cells(src, dst):
    """Return list of (x,y) cells on an L-shaped Manhattan path:
       horizontal first, then vertical."""
    cells = []
    x, y = src
    tx, ty = dst
    step_x = 1 if tx >= x else -1
    while x != tx:
        cells.append((x, y))
        x += step_x
    step_y = 1 if ty >= y else -1
    while y != ty:
        cells.append((x, y))
        y += step_y
    cells.append((tx, ty))
    return cells


# ── Application ─────────────────────────────────────────────────

class App:
    def __init__(self, root):
        self.root = root
        self.root.title("Manhattan Distance — Hill Climbing")
        self.root.configure(bg="#ececec")

        self.N = DEFAULT_N
        self.M = DEFAULT_M
        self.houses = list(DEFAULT_HOUSES)
        self.hospitals = list(DEFAULT_HOSPITALS)
        self.click_mode = None          # None | "house" | "hospital"
        self.step_count = 0
        self.converged = False

        # ── top bar ────────────────────────────────────────────
        top = tk.Frame(root, bg="#ececec")
        top.pack(side=tk.TOP, fill=tk.X, padx=8, pady=(8, 0))

        self.btn_step = tk.Button(top, text="▶  Hill Climb Step",
                                  font=("Helvetica", 12, "bold"),
                                  command=self.hill_climb_step,
                                  bg="#27ae60", fg="white",
                                  activebackground="#2ecc71",
                                  padx=12, pady=4)
        self.btn_step.pack(side=tk.LEFT, padx=(0, 6))

        self.btn_house = tk.Button(top, text="+ House",
                                   font=("Helvetica", 11),
                                   command=lambda: self.set_mode("house"),
                                   padx=8, pady=4)
        self.btn_house.pack(side=tk.LEFT, padx=2)

        self.btn_hosp = tk.Button(top, text="+ Hospital",
                                  font=("Helvetica", 11),
                                  command=lambda: self.set_mode("hospital"),
                                  padx=8, pady=4)
        self.btn_hosp.pack(side=tk.LEFT, padx=2)

        self.btn_reset = tk.Button(top, text="Reset",
                                   font=("Helvetica", 11),
                                   command=self.reset,
                                   padx=8, pady=4)
        self.btn_reset.pack(side=tk.LEFT, padx=2)

        self.mode_label = tk.Label(top, text="", font=("Helvetica", 10, "italic"),
                                   bg="#ececec", fg="#888")
        self.mode_label.pack(side=tk.LEFT, padx=10)

        # ── canvas ─────────────────────────────────────────────
        cw = PAD + self.N * CELL + PAD
        ch = PAD + self.M * CELL + PAD
        self.canvas = tk.Canvas(root, width=cw, height=ch, bg=COL_BG,
                                highlightthickness=0)
        self.canvas.pack(padx=8, pady=8)
        self.canvas.bind("<Button-1>", self.on_canvas_click)

        # ── info bar ───────────────────────────────────────────
        self.info = tk.Label(root, text="", font=("Courier", 13),
                             bg="#ececec", anchor="w", justify=tk.LEFT)
        self.info.pack(fill=tk.X, padx=12, pady=(0, 8))

        self.draw()

    # ── coordinate helpers ─────────────────────────────────────
    def grid_to_px(self, gx, gy):
        """Centre-pixel of grid cell (gx, gy) with 1-indexing, y-up."""
        px = PAD + (gx - 1) * CELL + CELL // 2
        py = PAD + (self.M - gy) * CELL + CELL // 2
        return px, py

    def px_to_grid(self, px, py):
        """Canvas pixel → 1-indexed grid coords (or None)."""
        gx = (px - PAD) // CELL + 1
        gy = self.M - (py - PAD) // CELL
        if 1 <= gx <= self.N and 1 <= gy <= self.M:
            return gx, gy
        return None

    # ── drawing ────────────────────────────────────────────────
    def draw(self):
        c = self.canvas
        c.delete("all")

        # cells
        for gx in range(1, self.N + 1):
            for gy in range(1, self.M + 1):
                x1 = PAD + (gx - 1) * CELL
                y1 = PAD + (self.M - gy) * CELL
                c.create_rectangle(x1, y1, x1 + CELL, y1 + CELL,
                                   fill=COL_CELL_BG, outline=COL_GRID)

        # axis labels
        for gx in range(1, self.N + 1):
            px = PAD + (gx - 1) * CELL + CELL // 2
            c.create_text(px, PAD + self.M * CELL + 18,
                          text=str(gx), font=("Helvetica", 10), fill="#555")
            c.create_text(px, PAD - 18,
                          text=str(gx), font=("Helvetica", 10), fill="#555")
        for gy in range(1, self.M + 1):
            py = PAD + (self.M - gy) * CELL + CELL // 2
            c.create_text(PAD - 18, py,
                          text=str(gy), font=("Helvetica", 10), fill="#555")

        # manhattan paths
        path_colours = [COL_PATH, COL_PATH_ALT, "#9b59b6", "#1abc9c",
                        "#e84393", "#6c5ce7", "#fdcb6e"]
        if self.hospitals:
            for hi, house in enumerate(self.houses):
                hp = nearest_hospital(house, self.hospitals)
                cells = path_cells(house, hp)
                colour = path_colours[hi % len(path_colours)]
                for (cx, cy) in cells:
                    px, py = self.grid_to_px(cx, cy)
                    r = CELL // 2 - 2
                    c.create_rectangle(px - r, py - r, px + r, py + r,
                                       fill=colour, outline="", stipple="gray50")
                # draw path line through centres
                if len(cells) >= 2:
                    coords = []
                    for (cx, cy) in cells:
                        coords.extend(self.grid_to_px(cx, cy))
                    c.create_line(*coords, fill=colour, width=2, dash=(4, 2))

                # distance label at midpoint
                mid = cells[len(cells) // 2]
                mx, my = self.grid_to_px(*mid)
                dist = manhattan(house, hp)
                c.create_text(mx, my - 14, text=str(dist),
                              font=("Helvetica", 9, "bold"), fill=colour)

        # houses
        for i, (hx, hy) in enumerate(self.houses):
            px, py = self.grid_to_px(hx, hy)
            s = 14
            # draw a small house icon (triangle roof + square base)
            c.create_polygon(px, py - s - 4, px - s, py - 2, px + s, py - 2,
                             fill=COL_HOUSE, outline="#c0392b", width=1.5)
            c.create_rectangle(px - s + 3, py - 2, px + s - 3, py + s - 2,
                               fill=COL_HOUSE, outline="#c0392b", width=1.5)
            c.create_text(px, py + s + 8,
                          text=f"h{i+1}({hx},{hy})",
                          font=("Helvetica", 8, "bold"), fill=COL_HOUSE)

        # hospitals
        for i, (hx, hy) in enumerate(self.hospitals):
            px, py = self.grid_to_px(hx, hy)
            s = 15
            # draw a cross
            c.create_rectangle(px - s, py - s // 3, px + s, py + s // 3,
                               fill=COL_HOSPITAL, outline="#1a5276", width=1.5)
            c.create_rectangle(px - s // 3, py - s, px + s // 3, py + s,
                               fill=COL_HOSPITAL, outline="#1a5276", width=1.5)
            c.create_text(px, py + s + 10,
                          text=f"H{i+1}({hx},{hy})",
                          font=("Helvetica", 8, "bold"), fill=COL_HOSPITAL)

        # info
        cost = total_cost(self.houses, self.hospitals)
        parts = []
        for i, house in enumerate(self.houses):
            if self.hospitals:
                hp = nearest_hospital(house, self.hospitals)
                d = manhattan(house, hp)
                parts.append(f"h{i+1}→H: {d}")
        detail = "  ".join(parts)
        converge_msg = "  ✓ Converged!" if self.converged else ""
        self.info.config(
            text=f"Total Cost: {cost}   |   Steps: {self.step_count}"
                 f"{converge_msg}\n{detail}"
        )

    # ── interaction ────────────────────────────────────────────
    def set_mode(self, mode):
        self.click_mode = mode
        self.mode_label.config(text=f"Click a cell to place a {mode}…")

    def on_canvas_click(self, event):
        pos = self.px_to_grid(event.x, event.y)
        if pos is None or self.click_mode is None:
            return
        if self.click_mode == "house":
            if pos not in self.houses:
                self.houses.append(pos)
        elif self.click_mode == "hospital":
            if pos not in self.hospitals:
                self.hospitals.append(pos)
        self.click_mode = None
        self.converged = False
        self.mode_label.config(text="")
        self.draw()

    def reset(self):
        self.houses = list(DEFAULT_HOUSES)
        self.hospitals = list(DEFAULT_HOSPITALS)
        self.step_count = 0
        self.converged = False
        self.click_mode = None
        self.mode_label.config(text="")
        self.draw()

    # ── hill climbing ──────────────────────────────────────────
    def hill_climb_step(self):
        """Try moving each hospital ±1 in x or y.  Apply the single
        move that yields the largest cost reduction.  Respect grid bounds."""
        if self.converged:
            return

        current_cost = total_cost(self.houses, self.hospitals)
        best_cost = current_cost
        best_config = None  # (hospital_index, new_position)

        deltas = [(-1, 0), (1, 0), (0, -1), (0, 1)]

        for idx, (hx, hy) in enumerate(self.hospitals):
            for dx, dy in deltas:
                nx, ny = hx + dx, hy + dy
                # stay in bounds (1-indexed)
                if nx < 1 or nx > self.N or ny < 1 or ny > self.M:
                    continue
                # don't overlap another hospital
                trial = list(self.hospitals)
                trial[idx] = (nx, ny)
                if len(set(trial)) < len(trial):
                    continue
                c = total_cost(self.houses, trial)
                if c < best_cost:
                    best_cost = c
                    best_config = (idx, (nx, ny))

        if best_config is not None:
            idx, pos = best_config
            self.hospitals[idx] = pos
            self.step_count += 1
            self.draw()
        else:
            self.converged = True
            self.draw()


# ── Main ────────────────────────────────────────────────────────
if __name__ == "__main__":
    root = tk.Tk()
    root.resizable(False, False)
    App(root)
    root.mainloop()
