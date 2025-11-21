package Chess;

public abstract class ChessPiece implements Cloneable {
    protected char color; // 'w' or 'b'

    public ChessPiece(char color) {
        this.color = color;
    }

    public char getColor() {
        return color;
    }

    public abstract boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, ChessBoard board);

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public abstract String toString();

    public abstract String getType();
    public abstract int getValue();
}