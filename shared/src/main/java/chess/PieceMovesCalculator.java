package chess;
import java.util.Collection;
public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
//    pieceMoves(board: ChessBoard, position: ChessPosition) {
//        return Collection<ChessMove> monkey;
//    }
}

