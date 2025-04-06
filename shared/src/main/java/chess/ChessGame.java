package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currTurn;
    private ChessBoard currBoard;
    private ChessMove lastMove;

    public ChessGame() {
        currBoard = new ChessBoard();
        currBoard.resetBoard();
        currTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece movePiece = getBoard().getPiece(startPosition);
        if(movePiece == null){
            return null;
        }

        Collection<ChessMove> possMoves = movePiece.pieceMoves(getBoard(), startPosition);

        Collection<ChessMove> legalMoves = new ArrayList<>();

        for(ChessMove move : possMoves){
            if(safeMove(move)){
                legalMoves.add(move);
            }
        }

        if(movePiece.getPieceType() == ChessPiece.PieceType.PAWN){
            legalMoves.addAll(enPassant(startPosition));
        }

        return legalMoves;
    }

    private Collection<ChessMove> enPassant(ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = getBoard().getPiece(pos);
        if(piece == null || piece.getPieceType() != ChessPiece.PieceType.PAWN){
            return moves;
        }

        if(lastMove == null){
            return moves;
        }

        ChessPosition lastStart = lastMove.getStartPosition();
        ChessPosition lastEnd = lastMove.getEndPosition();
        ChessPiece lastPiece = getBoard().getPiece(lastEnd);

        if(lastPiece == null || lastPiece.getPieceType()!= ChessPiece.PieceType.PAWN){
            return moves;
        }

        if(Math.abs(lastStart.getRow() - lastEnd.getRow()) == 2){
            if(Math.abs(lastEnd.getColumn() - pos.getColumn()) == 1){
                int dir = (piece.getTeamColor() == TeamColor.WHITE) ? 1 : -1;
                ChessPosition target = new ChessPosition(lastEnd.getRow()+dir, lastEnd.getColumn());
                moves.add(new ChessMove(pos, target, null));
            }
        }
        return moves;
    }

    private boolean safeMove(ChessMove move){
        ChessBoard test = testBoard(getBoard());
        ChessPiece movingPiece = test.getPiece(move.getStartPosition());
        if(movingPiece == null){
            return false;
        }

        test.removePiece(move.getEndPosition());
        test.addPiece(move.getEndPosition(), test.getPiece(move.getStartPosition()));
        test.removePiece(move.getStartPosition());

        return !isInCheck(movingPiece.getTeamColor(), test);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());
        if(piece == null){
            throw new InvalidMoveException("No piece to move.");
        }

        if(piece.getTeamColor()!=getTeamTurn()){
            throw new InvalidMoveException("Not " + piece.getTeamColor() + "'s turn");
        }

        Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());
        if(legalMoves == null || !legalMoves.contains(move)){
            throw new InvalidMoveException("Invalid move.");
        }

        if(!safeMove(move)){
            throw new InvalidMoveException("Unsafe move leaves your king in check.");
        }

        if(isInCheckmate(getTeamTurn())){
            throw new InvalidMoveException("CHECKMATE.");
        }

        if(isInStalemate(getTeamTurn())){
            throw new InvalidMoveException("STALEMATE.");
        }

        if(piece.getPieceType() == ChessPiece.PieceType.PAWN &&
                lastMove != null &&
                Math.abs(move.getStartPosition().getColumn() - lastMove.getEndPosition().getColumn()) == 1 &&
                move.getEndPosition().equals(new ChessPosition(lastMove.getEndPosition().getRow() +
                        (piece.getTeamColor() == TeamColor.WHITE ? 1 : -1), lastMove.getEndPosition().getColumn()))){
            getBoard().removePiece(lastMove.getEndPosition());
        }

        if (move.getPromotionPiece() != null) {
            TeamColor pawnColor = getBoard().getPiece(move.getStartPosition()).getTeamColor();
            ChessPiece promotedPawn = new ChessPiece(pawnColor, move.getPromotionPiece());
            getBoard().addPiece(move.getEndPosition(), promotedPawn);
            getBoard().removePiece(move.getStartPosition());
        } else {
            getBoard().addPiece(move.getEndPosition(), getBoard().getPiece(move.getStartPosition()));
            getBoard().removePiece(move.getStartPosition());
        }

        lastMove = move;

        if(getTeamTurn() == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }
        else if(getTeamTurn() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, getBoard());
    }

    private boolean isInCheck(TeamColor teamColor, ChessBoard board){
        Collection<ChessMove> allMoves = getAllMoves((teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE
        , board);
        for(ChessMove move: allMoves){
            if (move.getEndPosition().equals(kingPosition(teamColor, board))){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        Collection<ChessMove> moves = getAllMoves(teamColor, getBoard());
        for(ChessMove move : moves){
            if(safeMove(move)){
                return false;
            }
        }
        return true;
    }

    private ChessPosition kingPosition(TeamColor teamColor, ChessBoard board){
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        Collection<ChessMove> allMoves = getAllMoves(teamColor, getBoard());

        for(ChessMove move:allMoves){
            if(safeMove(move)){
                return false;
            }
        }
        return true;
    }

    private Collection<ChessMove> getAllMoves(TeamColor teamColor, ChessBoard board){
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(board, position);
                    allMoves.addAll(moves);
                }
            }
        }
        return allMoves;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currBoard;
    }

    private ChessBoard testBoard(ChessBoard board){
        ChessBoard test = new ChessBoard();
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                if(piece!=null){
                    test.addPiece(pos, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                }
            }
        }
        return test;
    }
}
