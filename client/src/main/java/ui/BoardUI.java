package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.util.Collection;

import chess.*;
import static ui.EscapeSequences.*;


public class BoardUI {
    private static final int BOARD_SIZE = 8;
    private static final int DRAWING_SIZE = 10;

    private final ChessBoard board;
    private final PrintStream out;
    private ChessGame.TeamColor team;

    public BoardUI(PrintStream out, ChessGame game) {
        this.board = game.getBoard();
        this.out = out;
    }

    public void printBoard(ChessGame.TeamColor teamColor) {
        this.team = teamColor;
        out.println();
        drawHeader();
        drawRows(false, null, null);
        drawHeader();
    }

    private void drawHeader() {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(team == ChessGame.TeamColor.WHITE
                ? "    A   B   C   D   E   F   G   H    "
                : "    H   G   F   E   D   C   B   A    ");
        out.println(RESET_TEXT_COLOR + RESET_BG_COLOR);
    }

    private void drawRows(boolean highlightMoves, Collection<ChessPosition> endPositions, ChessPosition selectedPos) {
        int startRow = (team == ChessGame.TeamColor.WHITE) ? BOARD_SIZE : 1;
        int endRow = (team == ChessGame.TeamColor.WHITE) ? 1 : BOARD_SIZE;
        int rowStep = (team == ChessGame.TeamColor.WHITE) ? -1 : 1;

        for (int row = startRow; row != (endRow + rowStep); row += rowStep) {
            for (int col = 0; col < DRAWING_SIZE; col++) {
                printRow(row, col, highlightMoves, endPositions, selectedPos);
            }
            out.println(RESET_BG_COLOR);
        }
    }

    private void printRow(int row, int col, boolean highlightMoves, Collection<ChessPosition> endPositions, ChessPosition selectedPos) {
        if (col == 0 || col == DRAWING_SIZE - 1) {
            out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
            out.printf(" %d ", row);
        } else {
            setSquareColor(row, col, highlightMoves, endPositions, selectedPos);
            out.print(getPiece(row, col));
            out.print(RESET_TEXT_COLOR);
        }
    }

    private void setSquareColor(int row, int col, boolean highlightMoves, Collection<ChessPosition> endPositions, ChessPosition selectedPos) {
        boolean isDark = (row + col) % 2 == 0;
        String baseColor = isDark ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY;

        if (highlightMoves && endPositions != null && isMove(row, col, endPositions)) {
            out.print(isDark ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN);
        } else if (highlightMoves && selectedPos != null && selectedPos.getRow() == row && selectedPos.getColumn() == col) {
            out.print(SET_BG_COLOR_YELLOW);
        } else {
            out.print(baseColor);
        }
    }

    private String getPiece(int row, int col) {
        ChessPosition position = new ChessPosition(row, col + 1); // Ensure correct indexing
        ChessPiece piece = board.getPiece(position);
        if (piece == null) return EMPTY;

        String pieceString;
        if(piece.getTeamColor()== ChessGame.TeamColor.WHITE) {
            pieceString = switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case ROOK -> WHITE_ROOK;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case PAWN -> WHITE_PAWN;
            };
        }
        else{
            pieceString = switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case ROOK -> BLACK_ROOK;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case PAWN -> BLACK_PAWN;
            };
        }

        return (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK) + pieceString;
    }

    private boolean isMove(int row, int col, Collection<ChessPosition> endPositions) {
        return endPositions.stream().anyMatch(pos -> pos.getRow() == row && pos.getColumn() == col);
    }
}
