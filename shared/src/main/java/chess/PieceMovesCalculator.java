package chess;

public interface PieceMovesCalculator {
    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessPiece PieceType){
        return switch (PieceType) {
            case PAWN -> new PawnMovesCalculator();
            case KNIGHT -> new KnightMovesCalculator();
            case BISHOP -> new BishopMovesCalculator();
            case ROOK -> new RookMovesCalculator();
            case QUEEN -> new QueenMovesCalculator();
            case KING -> new KingMovesCalculator();
            default -> throw new IllegalArgumentException("Unknown piece type: " + type);
        };
    }
}
