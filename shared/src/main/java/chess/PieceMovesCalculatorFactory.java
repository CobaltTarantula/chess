package chess;

public class PieceMovesCalculatorFactory {
    public static PieceMovesCalculator getCalculator(ChessPiece.PieceType type) {
        return switch (type) {
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
