package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.util.Collection;

import chess.*;
import static ui.EscapeSequences.*;


public class BoardUI {
    private static final int BOARD_SIZE = 8;

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

        String headerLine = switch (team) {
            case WHITE -> "    A   B  C   D   E  F   G   H    ";

            case BLACK -> "    H   G  F   E   D  C   B   A    ";
        };
        out.println(headerLine + RESET_TEXT_COLOR + RESET_BG_COLOR);
    }

    private void drawRows(boolean highlightMoves, Collection<ChessPosition> endPositions, ChessPosition selectedPos) {
        int startRow = (team == ChessGame.TeamColor.WHITE) ? BOARD_SIZE : 1;
        int endRow = (team == ChessGame.TeamColor.WHITE) ? 1 : BOARD_SIZE;
        int rowStep = (team == ChessGame.TeamColor.WHITE) ? -1 : 1;

        for (int row = startRow; row != endRow + rowStep; row += rowStep) {
            printRow(row, highlightMoves, endPositions, selectedPos);
            out.println(RESET_BG_COLOR);
        }
    }

    private void printRow(int row, boolean highlightMoves, Collection<ChessPosition> endPositions, ChessPosition selectedPos) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.printf(" %d ", row);

        for (int c = 1; c <= BOARD_SIZE; c++) {
            int col = (team == ChessGame.TeamColor.WHITE) ? c : BOARD_SIZE - c + 1;
            setSquareColor(row, col, highlightMoves, endPositions, selectedPos);
            out.print(getPiece(row, col));
            out.print(RESET_TEXT_COLOR);
        }

        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.printf(" %d ", row);
    }

    private void setSquareColor(int row, int col, boolean highlightMoves, Collection<ChessPosition> endPositions, ChessPosition selectedPos) {
        boolean isDark = (row + col) % 2 == 0;
        String baseColor = isDark ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY;

        if (selectedPos != null && selectedPos.getRow() == row && selectedPos.getColumn() == col) {
            out.print(SET_BG_COLOR_YELLOW);
        } else if (highlightMoves && endPositions != null && isMove(row, col, endPositions)) {
            out.print(isDark ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN);
        } else {
            out.print(baseColor);
        }
    }

    private String getPiece(int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return EMPTY;
        }

        String pieceString = switch (piece.getPieceType()) {
            case KING -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING);
            case QUEEN -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN);
            case ROOK -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK);
            case BISHOP -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP);
            case KNIGHT -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT);
            case PAWN -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN);
        };

        return (piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK) + pieceString;
    }

    private boolean isMove(int row, int col, Collection<ChessPosition> endPositions) {
        return endPositions.stream().anyMatch(pos -> pos.getRow() == row && pos.getColumn() == col);
    }
}
