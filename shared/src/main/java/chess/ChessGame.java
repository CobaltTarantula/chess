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
    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;
    private boolean whiteRookLeftMoved = false;
    private boolean whiteRookRightMoved = false;
    private boolean blackRookLeftMoved = false;
    private boolean blackRookRightMoved = false;
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

        // enPassant
        if(movePiece.getPieceType() == ChessPiece.PieceType.PAWN){
            legalMoves.addAll(enPassant(startPosition));
        }

        // castling
        if (movePiece.getPieceType() == ChessPiece.PieceType.KING) {
            legalMoves.addAll(castle(startPosition));
        }

        return legalMoves;
    }

    private Collection<ChessMove> castle(ChessPosition kingPos){
        Collection<ChessMove> moves = new ArrayList<>();
        TeamColor color = getBoard().getPiece(kingPos).getTeamColor();

        // check king and rook movement
        if ((color == TeamColor.WHITE && whiteKingMoved) || (color == TeamColor.BLACK && blackKingMoved)) {
            return moves; // King has already moved, no castling allowed
        }

        int row = (color == TeamColor.WHITE) ? 1 : 8;
        ChessPosition kingStart = new ChessPosition(row, 5); // Default starting position of the king

        if (!kingPos.equals(kingStart)) return moves; // King not in its initial position

        // Castling to the right (Kingside)
        if (canCastle(kingStart, new ChessPosition(row, 8), color)) {
            moves.add(new ChessMove(kingPos, new ChessPosition(row, 7), null)); // Move king two spaces right
        }

        // Castling to the left (Queenside)
        if (canCastle(kingStart, new ChessPosition(row, 1), color)) {
            moves.add(new ChessMove(kingPos, new ChessPosition(row, 3), null)); // Move king two spaces left
        }

        return moves;
    }

    private boolean canCastle(ChessPosition start, ChessPosition end, TeamColor color){
        ChessPiece piece = getBoard().getPiece(start);

        if(piece == null || piece.getPieceType() != ChessPiece.PieceType.KING) return false;

        // Check if the king has already moved
        if(piece.getTeamColor() == TeamColor.WHITE && whiteKingMoved) return false;
        if(piece.getTeamColor() == TeamColor.BLACK && blackKingMoved) return false;

        // Check if the rook involved has already moved
        if(piece.getTeamColor() == TeamColor.WHITE) {
            if(end.getColumn() == 3 && !whiteRookLeftMoved) {
                // Check if the path between the king and rook is clear
                if(isPathClear(start, end)) {
                    ChessMove move = new ChessMove(start, end, null);
                    return safeMove(move);  // Use safeMove to check if the move puts the king in check
                }
            } else if(end.getColumn() == 6 && !whiteRookRightMoved) {
                // Check if the path between the king and rook is clear
                if(isPathClear(start, end)) {
                    ChessMove move = new ChessMove(start, end, null);
                    return safeMove(move);  // Use safeMove to check if the move puts the king in check
                }
            }
        } else {
            if(end.getColumn() == 3 && !blackRookLeftMoved) {
                // Check if the path between the king and rook is clear
                if(isPathClear(start, end)) {
                    ChessMove move = new ChessMove(start, end, null);
                    return safeMove(move);  // Use safeMove to check if the move puts the king in check
                }
            } else if(end.getColumn() == 6 && !blackRookRightMoved) {
                // Check if the path between the king and rook is clear
                if(isPathClear(start, end)) {
                    ChessMove move = new ChessMove(start, end, null);
                    return safeMove(move);  // Use safeMove to check if the move puts the king in check
                }
            }
        }

        return false;
    }

    // Helper method to check if the path between the king and the rook is clear
    private boolean isPathClear(ChessPosition kingStart, ChessPosition kingEnd) {
        int startColumn = kingStart.getColumn();
        int endColumn = kingEnd.getColumn();
        int row = kingStart.getRow();

        // Check for squares between the king and rook
        if (startColumn < endColumn) {
            // Check for kingside castling
            for (int col = startColumn + 1; col < endColumn; col++) {
                if (getBoard().getPiece(new ChessPosition(row, col)) != null) {
                    return false;  // Path is blocked
                }
            }
        } else {
            // Check for queenside castling
            for (int col = startColumn - 1; col > endColumn; col--) {
                if (getBoard().getPiece(new ChessPosition(row, col)) != null) {
                    return false;  // Path is blocked
                }
            }
        }
        return true;
    }

    private Collection<ChessMove> enPassant(ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = getBoard().getPiece(pos);
        if(piece == null || piece.getPieceType() != ChessPiece.PieceType.PAWN) return moves;

        if(lastMove == null) return moves;

        ChessPosition lastStart = lastMove.getStartPosition();
        ChessPosition lastEnd = lastMove.getEndPosition();
        ChessPiece lastPiece = getBoard().getPiece(lastEnd);

        if(lastPiece == null || lastPiece.getPieceType()!= ChessPiece.PieceType.PAWN) return moves;

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
        if(movingPiece == null) return false; // no piece to move

        // Apply the move on the test board
        test.removePiece(move.getEndPosition()); // Remove any captured piece
        test.addPiece(move.getEndPosition(), test.getPiece(move.getStartPosition()));
        test.removePiece(move.getStartPosition());

        return !isInCheck(movingPiece.getTeamColor(), test); // Pass the test board to check if king is in check
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

        // handle enPassant
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN && lastMove != null &&  Math.abs(move.getStartPosition().getColumn() - lastMove.getEndPosition().getColumn()) == 1 && move.getEndPosition().equals(new ChessPosition(lastMove.getEndPosition().getRow() + (piece.getTeamColor() == TeamColor.WHITE ? 1 : -1), lastMove.getEndPosition().getColumn()))){
            getBoard().removePiece(lastMove.getEndPosition());
        }

        // handle castling
        if (piece.getPieceType() == ChessPiece.PieceType.KING && Math.abs(move.getStartPosition().getColumn() - move.getEndPosition().getColumn()) == 2) {
            // Kingside Castling
            if (move.getEndPosition().getColumn() == 7) {
                getBoard().addPiece(new ChessPosition(move.getStartPosition().getRow(), 6), getBoard().getPiece(new ChessPosition(move.getStartPosition().getRow(), 8)));
                getBoard().removePiece(new ChessPosition(move.getStartPosition().getRow(), 8));
            }
            // Queenside Castling
            else if (move.getEndPosition().getColumn() == 3) {
                getBoard().addPiece(new ChessPosition(move.getStartPosition().getRow(), 4), getBoard().getPiece(new ChessPosition(move.getStartPosition().getRow(), 1)));
                getBoard().removePiece(new ChessPosition(move.getStartPosition().getRow(), 1));
            }
        }

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == TeamColor.WHITE) whiteKingMoved = true;
            else blackKingMoved = true;
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (piece.getTeamColor() == TeamColor.WHITE) {
                if (move.getStartPosition().getColumn() == 1) whiteRookLeftMoved = true;
                if (move.getStartPosition().getColumn() == 8) whiteRookRightMoved = true;
            } else {
                if (move.getStartPosition().getColumn() == 1) blackRookLeftMoved = true;
                if (move.getStartPosition().getColumn() == 8) blackRookRightMoved = true;
            }
        }

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

        lastMove = move;

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
        return isInCheck(teamColor, getBoard());
    }

    private boolean isInCheck(TeamColor teamColor, ChessBoard board){
        Collection<ChessMove> allMoves = getAllMoves((teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE
        , board);
        // iterate over moves to see if any of opponents moves lands them on king
        for(ChessMove move: allMoves){
            // if so, return true
            if (move.getEndPosition().equals(kingPosition(teamColor, board))) return true;
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
        if(!isInCheck(teamColor)) return false; // Must be in check first
        Collection<ChessMove> moves = getAllMoves(teamColor, getBoard()); // if in check, then make sure there are no safe moves
        for(ChessMove move : moves){
            if(safeMove(move)){
                return false;
            }
        }
        return true; // if no SAFE moves then in checkmate
    }

    private ChessPosition kingPosition(TeamColor teamColor, ChessBoard board){
        for (int row = 1; row <= 8; row++) { // iterate over whole board
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return pos; // as long as there is a piece at the position, and the color and type match desired
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
        if(isInCheck(teamColor)) return false; // not in stalemate if in Check
        Collection<ChessMove> allMoves = getAllMoves(teamColor, getBoard());

        for(ChessMove move:allMoves){ // if any safe move then not in stalemate
            if(safeMove(move)) return false;
        }
        return true;
    }

    private Collection<ChessMove> getAllMoves(TeamColor teamColor, ChessBoard board){
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (int row = 1; row <= 8; row++) { // iterate over whole board
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                // if piece's color matches teamColor, add move to list
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
