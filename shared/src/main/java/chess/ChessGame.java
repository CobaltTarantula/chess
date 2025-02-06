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
    public ChessGame() {
        currBoard = new ChessBoard(); // creates the chessboard
        currBoard.resetBoard(); // sets all the pieces
        currTurn = TeamColor.WHITE; // white starts
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
        ChessPiece movePiece = getBoard().getPiece(startPosition); // identify piece to move
        if(movePiece == null) return null; // if no piece at position, there are no moves to make from that spot

        Collection<ChessMove> possMoves = movePiece.pieceMoves(getBoard(), startPosition); // retrieve valid/possible moves

        Collection<ChessMove> legalMoves = new ArrayList<>(); // initialize collection of legal moves

        for(ChessMove move : possMoves){ // parse through possible moves to id legal moves
            if(safeMove(move)){ // if safe move (doesn't put King in check) then add to legalMoves
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

    private boolean safeMove(ChessMove move){
        ChessBoard test = testBoard(getBoard());
        TeamColor pieceColor = test.getPiece(move.getStartPosition()).getTeamColor(); // set color
        makeMove(move); // move the piece
        return (!isInCheck(pieceColor)); // check if in check
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());
        if(piece == null){ // if no piece then throw exception
            throw new InvalidMoveException("No piece to move.");
        }

        if(piece.getTeamColor()!=getTeamTurn()){ // if not color turn then throw exception
            throw new InvalidMoveException("Not " + piece.getTeamColor() + "'s turn");
        }

        Collection<ChessMove> legalMoves = validMoves(move.getStartPosition()); // validate move legality
        if(legalMoves == null || !legalMoves.contains(move)){
            throw new InvalidMoveException("Invalid move.");
        }

        // check for check
        if(!safeMove(move)) throw new InvalidMoveException("Unsafe move leaves your king in check.");

        // check for checkmate
        if(isInCheckmate(getTeamTurn())) throw new InvalidMoveException("CHECKMATE.");

        // check for stalemate
        if(isInStalemate(getTeamTurn())) throw new InvalidMoveException("STALEMATE.");

        // actually move the pieces (copy piece to new spot, set old spot to null)
        if (move.getPromotionPiece() != null) { // promotion needed?
            TeamColor pawnColor = getBoard().getPiece(move.getStartPosition()).getTeamColor();
            ChessPiece promotedPawn = new ChessPiece(pawnColor, move.getPromotionPiece());
            getBoard().addPiece(move.getEndPosition(), promotedPawn);
            getBoard().removePiece(move.getStartPosition());
        } else {
            getBoard().addPiece(move.getEndPosition(), getBoard().getPiece(move.getStartPosition()));
            getBoard().removePiece(move.getStartPosition());
        }

        // pass turn after making a move
        if(getTeamTurn() == TeamColor.BLACK) setTeamTurn(TeamColor.WHITE);
        else if(getTeamTurn() == TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allMoves = getAllMoves(teamColor);
        // iterate over moves to see if any of opponents moves lands them on king
        for(ChessMove move: allMoves){
            // if so, return true
            if (move.getEndPosition().equals(kingPosition(teamColor, getBoard()))) return true;
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
        Collection<ChessMove> moves = getAllMoves(teamColor); // rework
        for(ChessMove move : moves){
            if(safeMove(move)) return false;
        }
        return true;
    }

    private ChessPosition kingPosition(TeamColor teamColor, ChessBoard board){
        for (int row = 1; row <= 8; row++) { // iterate over whole board
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && teamColor == getTeamTurn() && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return pos; // as long as there is a piece at the position, and the color and type match desired
                }
            }
        }
        return null;
    }

    private Collection<ChessMove> getAllMoves(TeamColor teamColor){
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (int row = 1; row <= 8; row++) { // iterate over whole board
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = getBoard().getPiece(position);

                // if piece's color matches teamColor, add move to list
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(getBoard(), position);
                    allMoves.addAll(moves);
                }
            }
        }
        return allMoves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) return false; // not in stalemate if in Check
        Collection<ChessMove> allMoves = getAllMoves(teamColor);
        ChessBoard test = testBoard(getBoard());

        for(ChessMove move:allMoves){
            makeMove(move);
            setBoard(test);
            if(isInCheck(teamColor)) return false;
            test = testBoard(getBoard());
        }
        // if(neither team can make a legal move){
        // return true;
        return true;
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
        for(int i = 1; i <= 8; i++){ // set testBoard to be identical to currBoard
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
