# Pacman Graphics Setup Guide

This guide helps you set up graphical display for the Pacman game in GitHub Codespaces using VNC (Virtual Network Computing).

## Quick Start (Automated)

```bash
cd /workspaces/AOA/AI/MultiAgentPacman
./setup_graphics.sh full
```

This single command will:
1. Install VNC server and XFCE4 desktop
2. Set up VNC password
3. Start the VNC server
4. Show you connection instructions

## Manual Commands

### Installation
```bash
./setup_graphics.sh install
```
- Installs VNC server, desktop environment, and noVNC
- Sets up default VNC password: `pacman123`

### Start VNC Server
```bash
./setup_graphics.sh start
```
- Starts VNC on port 5901
- Resolution: 1280x1024

### Stop VNC Server
```bash
./setup_graphics.sh stop
```

### Check Status
```bash
./setup_graphics.sh status
```

### Play Pacman
```bash
./setup_graphics.sh play
```
- Starts VNC if not running
- Launches Pacman with graphics
- Pass additional arguments: `./setup_graphics.sh play -l smallClassic -z 2`

### Connection Instructions
```bash
./setup_graphics.sh connect
```

## Connecting to VNC

### Option 1: Direct VNC Client (Easiest)
1. Install a VNC viewer on your local computer:
   - **Windows**: [RealVNC Viewer](https://www.realvnc.com/en/connect/download/viewer/)
   - **Mac**: [RealVNC Viewer](https://www.realvnc.com/en/connect/download/viewer/) or built-in
   - **Linux**: `sudo apt install remmina`

2. Forward the port from Codespace to your local machine:
   ```bash
   # From your local terminal
   ssh -L 5901:localhost:5901 codespace@<your-codespace-host>
   ```

3. Open VNC viewer and connect to: `localhost:5901`
   - Password: `pacman123`

### Option 2: Web-Based VNC (Browser)
```bash
# Terminal 1: Start noVNC web server
websockify --web=/usr/share/novnc 6080 localhost:5901

# Terminal 2: Port forward
ssh -L 6080:localhost:6080 codespace@<your-codespace-host>

# Browser: Open http://localhost:6080/vnc.html
```

### Option 3: SSH Tunnel + Native VNC
On your local machine:
```bash
# Create the tunnel
ssh -L 5901:localhost:5901 codespace@<your-codespace-host>

# In a new terminal, connect with VNC viewer to localhost:5901
```

## Playing Pacman

### Via VNC (Graphical)
1. Connect to VNC as described above
2. In the VNC window, open terminal
3. Run:
   ```bash
   cd /workspaces/AOA/AI/MultiAgentPacman
   python pacman.py
   ```

Or use the quick command:
```bash
cd /workspaces/AOA/AI/MultiAgentPacman
./setup_graphics.sh play
```

### Via Text Mode (No Graphics)
```bash
cd /workspaces/AOA/AI/MultiAgentPacman
python pacman.py -t
```

## Command-Line Options for Pacman

```bash
python pacman.py [options]

Options:
  -l LAYOUT       Layout to use (default: custom)
  -p AGENT        Pacman agent type (default: ExpectimaxAgent)
  -g GHOST        Ghost agent type (default: MediumGhost)
  -n GAMES        Number of games (default: 10)
  -z ZOOM         Zoom level (default: 1.0)
  -t              Text graphics mode
  -q              Quiet mode (no output)
  --frameTime MS  Delay between frames
  -k NUMGHOSTS    Number of ghosts (default: 10)

Examples:
  python pacman.py                           # Default game
  python pacman.py -l smallClassic -z 2      # Smaller layout, zoomed
  python pacman.py -n 1 -z 1.5               # Single game
  python pacman.py -p GreedyAgent -g RandomGhost  # Specific agents
```

## Troubleshooting

### VNC server won't start
```bash
# Kill any existing VNC processes
vncserver -kill :1

# Try starting again
./setup_graphics.sh start
```

### Can't connect to VNC
1. Check VNC is running: `./setup_graphics.sh status`
2. Verify port forwarding is active
3. Check firewall settings
4. Try web-based VNC (Option 2) instead

### Pacman won't display graphics
1. Make sure VNC is running and you're connected
2. Try text mode: `python pacman.py -t`
3. Check the DISPLAY environment variable: `echo $DISPLAY` (should show `:1`)

### Permission denied error
```bash
chmod +x /workspaces/AOA/AI/MultiAgentPacman/setup_graphics.sh
```

## Default Credentials
- **VNC Password**: `pacman123`
- **Display**: `:1`
- **VNC Port**: `5901`
- **noVNC Port**: `6080`

To change the VNC password:
```bash
vncpasswd
```

## Resources
- [Pacman Project Documentation](https://inst.eecs.berkeley.edu/~cs188/sp09/pacman.html)
- [RealVNC Documentation](https://www.realvnc.com/en/connect/)
- [VNC Overview](https://en.wikipedia.org/wiki/Virtual_Network_Computing)
