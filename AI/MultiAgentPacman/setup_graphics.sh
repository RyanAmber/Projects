#!/bin/bash
# Setup script for running Pacman with graphics in Codespace
# This script sets up VNC server for graphical display

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
VNC_DISPLAY=":1"
VNC_PORT=5901
NOVNC_PORT=6080

echo "=========================================="
echo "Pacman Graphics Setup Script"
echo "=========================================="
echo ""

# Function to display usage
usage() {
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  install    - Install VNC and desktop environment"
    echo "  start      - Start VNC server"
    echo "  stop       - Stop VNC server"
    echo "  status     - Check VNC server status"
    echo "  connect    - Show connection instructions"
    echo "  play       - Start VNC and run Pacman game"
    echo ""
}

# Check if user provided command
if [ $# -eq 0 ]; then
    usage
    echo "Running full setup..."
    COMMAND="full"
else
    COMMAND="$1"
fi

install_dependencies() {
    echo "[1/4] Updating package list..."
    sudo apt-get update -qq

    echo "[2/4] Installing VNC server..."
    sudo apt-get install -y -qq tigervnc-standalone-server tigervnc-common > /dev/null 2>&1

    echo "[3/4] Installing desktop environment..."
    sudo apt-get install -y -qq xfce4 xfce4-terminal > /dev/null 2>&1

    echo "[4/4] Installing noVNC (web-based VNC)..."
    sudo apt-get install -y -qq novnc websockify > /dev/null 2>&1

    echo "✓ Dependencies installed successfully"
    echo ""
}

setup_vnc_password() {
    echo "Setting up VNC password..."
    mkdir -p ~/.vnc
    
    # Create a non-interactive password file (8 chars default)
    echo "pacman123" | vncpasswd -f > ~/.vnc/passwd 2>/dev/null || true
    chmod 600 ~/.vnc/passwd
    
    echo "✓ VNC password set to: pacman123"
    echo "  (You can change this by running: vncpasswd)"
    echo ""
}

start_vnc() {
    echo "Starting VNC server on display $VNC_DISPLAY..."
    
    # Kill any existing VNC server on this display
    vncserver -kill $VNC_DISPLAY 2>/dev/null || true
    sleep 1
    
    # Start new VNC server
    vncserver $VNC_DISPLAY -geometry 1280x1024 -depth 24 -localhost no > /dev/null 2>&1
    
    if [ $? -eq 0 ]; then
        echo "✓ VNC server started on port $VNC_PORT"
        echo ""
        show_connection_info
    else
        echo "✗ Failed to start VNC server"
        exit 1
    fi
}

stop_vnc() {
    echo "Stopping VNC server..."
    vncserver -kill $VNC_DISPLAY 2>/dev/null || true
    echo "✓ VNC server stopped"
    echo ""
}

check_status() {
    if pgrep -f "Xvnc.*$VNC_DISPLAY" > /dev/null; then
        echo "✓ VNC server is running on port $VNC_PORT"
    else
        echo "✗ VNC server is NOT running"
    fi
    echo ""
}

show_connection_info() {
    echo "=========================================="
    echo "Connection Information"
    echo "=========================================="
    echo ""
    echo "Option A: Direct VNC Connection"
    echo "  Port: localhost:$VNC_PORT"
    echo "  Password: pacman123"
    echo ""
    echo "Option B: Web-based VNC (noVNC)"
    echo "  1. Start noVNC in another terminal:"
    echo "     $ websockify --web=/usr/share/novnc $NOVNC_PORT localhost:$VNC_PORT"
    echo "  2. Open browser: http://localhost:$NOVNC_PORT/vnc.html"
    echo ""
    echo "Option C: SSH Port Forwarding (from local computer)"
    echo "  $ ssh -L $VNC_PORT:localhost:$VNC_PORT [user]@[codespace-host]"
    echo "  Then connect VNC client to localhost:$VNC_PORT"
    echo ""
}

play_pacman() {
    echo "Starting VNC server and Pacman game..."
    echo ""
    
    # Start VNC if not running
    if ! pgrep -f "Xvnc.*$VNC_DISPLAY" > /dev/null; then
        start_vnc
    else
        echo "✓ VNC server already running"
        echo ""
    fi
    
    echo "Starting Pacman game..."
    echo "Display: $VNC_DISPLAY"
    echo ""
    
    cd "$SCRIPT_DIR"
    DISPLAY=$VNC_DISPLAY python pacman.py "$@"
}

# Execute command
case "$COMMAND" in
    install)
        install_dependencies
        setup_vnc_password
        echo "Setup complete! Run: $0 start"
        ;;
    start)
        start_vnc
        ;;
    stop)
        stop_vnc
        ;;
    status)
        check_status
        ;;
    connect)
        show_connection_info
        ;;
    play)
        play_pacman "${@:2}"
        ;;
    full)
        install_dependencies
        setup_vnc_password
        start_vnc
        echo "Setup complete! VNC server is running."
        echo ""
        echo "To play Pacman:"
        echo "  $ $0 play"
        echo ""
        ;;
    *)
        usage
        exit 1
        ;;
esac
