package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor teamColor;
    private final PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        switch(pieceType) {
            case QUEEN, BISHOP, ROOK -> { // linear movements
                int[][] paths = getPaths(pieceType);
                for (int[] path : paths){
                    // to implement
                }
            }
            case KING -> {
                // one of small list of spots
            }
            case PAWN -> {
                // a bunch of rules
                // first move
                // en passante?
            }
            case KNIGHT -> {
                // also one from a small list of spots
            }
        }
        return moves;
    }

    private int[][] getPaths(PieceType type){
        switch(type){
            case KING, QUEEN -> { // any way
                return new int[][]{
                        {1, 0},   // Up
                        {1, 1},   // Up & Right
                        {0, 1},   // Right
                        {-1, 1},  // Down & Right
                        {-1, 0},  // Down
                        {-1, -1}, // Down & Left
                        {0, -1},  // Left
                        {1, -1}   // Up & Left
                };
            }
            case BISHOP -> { // diagonal
                return new int[][]{
                        {1, 1},   // Up & Right
                        {-1, 1},  // Down & Right
                        {-1, -1}, // Down & Left
                        {1, -1}   // Up & Left
                };
            }
            case KNIGHT -> { // L
                return new int[][]{
                        {1, 2}, // Top right L
                        {2, 1}, // Upper middle right L
                        {2, -1}, // Lower middle right L
                        {1, -2}, // Bottom right L
                        {-1, -2}, // Bottom left L
                        {-2, -1}, // Lower middle left L
                        {-2, 1}, // Upper middle left L
                        {-1, 2} // Bottom left L
                };
            }
            case ROOK -> {
                return new int[][]{
                        {0, 1}, // Up
                        {1, 0}, // Right
                        {0, -1}, // Down
                        {-1, 0} // Left

                };
            }
            case PAWN -> {
                return new int[][]{
                        // To implement
                };
            }
        }
        return null;
    }

    private boolean isOccupied (ChessBoard board, ChessPosition nextPos){
        return board.getPiece(nextPos) != null;
    }

    private boolean isOutOfBounds(ChessPosition position) {
        // next position is within bounds
        return position.getRow() <= 0
                || position.getRow() > 8
                || position.getColumn() <= 0
                || position.getColumn() > 8;  // next position is out of bounds
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "teamColor=" + teamColor +
                ", pieceType=" + pieceType +
                '}';
    }
}
