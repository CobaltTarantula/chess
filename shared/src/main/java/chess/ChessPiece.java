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
                assert paths != null;
                for (int[] path : paths){
                    moves.addAll(getPathLines(board, myPosition, path[0], path[1]));
                }
            }
            case KING, KNIGHT -> {
                // one of small list of spots
                int[][] paths = getPaths(pieceType);
                assert paths != null;
                for (int[] path : paths) {
                    ChessMove newMove = getPathSpot(board, myPosition, path[0], path[1]);
                    if (newMove != null){ moves.add(newMove);}
                }
            }
            case PAWN -> {
                int[][] paths = getPaths(pieceType);
                assert paths != null;
                for (int[] path : paths) {
                    ChessMove singleMove = getPawnMove(board, myPosition, path[0], path[1]);
                    ChessMove doubleMove = getDoublePawnMoveIfValid(board, myPosition, path);

                    if (singleMove != null && canPromote(singleMove, teamColor)) {
                        moves.addAll(getPawnPromotions(singleMove));
                    }
                    else if (singleMove != null) {
                        moves.add(singleMove);
                    }

                    if (doubleMove != null) {
                        moves.add(doubleMove);
                    }
                }
            }
        }
        return moves;
    }

    private ChessMove getDoublePawnMoveIfValid(ChessBoard board, ChessPosition myPosition, int[] path) {
        if (path[1] != 0 || !isFirstMove(teamColor, myPosition)) {
            return null;
        }

        int direction = teamColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        ChessPosition oneStepForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        ChessPosition twoStepsForward = new ChessPosition(myPosition.getRow() + 2 * direction, myPosition.getColumn());

        if (board.getPiece(oneStepForward) == null && board.getPiece(twoStepsForward) == null) {
            return getPawnMove(board, myPosition, path[0] + direction, path[1]);
        }

        return null;
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
                return getPawnPaths();
            }
        }
        return null;
    }

    private int[][] getPawnPaths() {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            return new int[][]{{1, -1}, {1, 0}, {1, 1}};
        } else {
            return new int[][]{{-1, -1}, {-1, 0}, {-1, 1}};
        }
    }

    private boolean isOccupied (ChessBoard board, ChessPosition nextPos){
        return board.getPiece(nextPos) != null;
    }

    private boolean isOutOfBounds(ChessPosition position) {
        return position.getRow() <= 0
                || position.getRow() > 8
                || position.getColumn() <= 0
                || position.getColumn() > 8;
    }

    private Collection<ChessMove> getPathLines(ChessBoard board, ChessPosition myPos, int vert, int horizon) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        for (int i = 1; i < 8; i++) {
            int newRow = myPos.getRow() + (i * vert);
            int newCol = myPos.getColumn() + (i * horizon);
            ChessPosition newPos = new ChessPosition(newRow, newCol);

            if (isOutOfBounds(newPos)){ break;}

            if (isOccupied(board, newPos)) {
                if (board.getPiece(newPos).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPos, newPos, null));
                }
                break;
            } else{ possibleMoves.add(new ChessMove(myPos, newPos, null));}
        }
        return possibleMoves;
    }

    private ChessMove getPathSpot(ChessBoard board, ChessPosition myPos, int vert, int horizon) {
        int newRow = myPos.getRow() + vert;
        int newCol = myPos.getColumn() + horizon;
        ChessPosition newPos = new ChessPosition(newRow, newCol);

        if (isOutOfBounds(newPos)) {
            return null;
        }

        if (isOccupied(board, newPos)) {
            if (board.getPiece(newPos).getTeamColor() != this.getTeamColor()) {
                return new ChessMove(myPos, newPos, null);
            }
            else{ return null;}
        }
        else{ return new ChessMove(myPos, newPos, null);}
    }

    private ChessMove getPawnMove(ChessBoard board, ChessPosition myPos, int vert, int horizon) {
        int nextRow = myPos.getRow() + vert;
        int nextCol = myPos.getColumn() + horizon;
        ChessPosition nextPos = new ChessPosition(nextRow, nextCol);

        if (isOutOfBounds(nextPos)){ return null;}
        boolean occupied = isOccupied(board, nextPos);

        if (horizon != 0){
            if (occupied && (board.getPiece(nextPos).getTeamColor() != this.getTeamColor())) {
                return new ChessMove(myPos, nextPos, null);
            }
        }

        else {
            if (!occupied){ return new ChessMove(myPos, nextPos, null);}
        }

        return null;
    }

    private boolean isFirstMove(ChessGame.TeamColor teamColor, ChessPosition myPos) {
        switch (teamColor){
            case WHITE -> {
                if (myPos.getRow() == 2){ return true;}
            }
            case BLACK -> {
                if (myPos.getRow() == 7){ return true;}
            }
        }
        return false;
    }

    private boolean canPromote(ChessMove newMove, ChessGame.TeamColor teamColor) {
        switch(teamColor){
            case WHITE -> {
                if (newMove.getEndPosition().getRow() == 8){ return true;}
            }
            case BLACK -> {
                if (newMove.getEndPosition().getRow() == 1){ return true;}
            }
        }
        return false;
    }

    private Collection<ChessMove> getPawnPromotions(ChessMove newMove) {
        ArrayList<ChessMove> promotions = new ArrayList<>();
        promotions.add(new ChessMove(newMove.getStartPosition(), newMove.getEndPosition(), PieceType.QUEEN));
        promotions.add(new ChessMove(newMove.getStartPosition(), newMove.getEndPosition(), PieceType.ROOK));
        promotions.add(new ChessMove(newMove.getStartPosition(), newMove.getEndPosition(), PieceType.KNIGHT));
        promotions.add(new ChessMove(newMove.getStartPosition(), newMove.getEndPosition(), PieceType.BISHOP));
        return promotions;
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
