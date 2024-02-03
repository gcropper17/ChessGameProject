package chess;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove {
    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promPiece;

    public ChessMoveImpl(ChessPosition endPos, ChessPosition startPos, ChessPiece.PieceType promotionPiece) {
        endPosition = endPos;
        startPosition = startPos;
        promPiece = promotionPiece;
    }
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promPiece;
    }

    @Override
    public ChessMove deepCopyMove(ChessMove move) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImpl chessMove = (ChessMoveImpl) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promPiece == chessMove.promPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promPiece);
    }

    public ChessMove DeepCopyMove(ChessMove move) {
        ChessMove copyMove = new ChessMoveImpl(move.getEndPosition(), move.getStartPosition(), move.getPromotionPiece());
        copyMove = move;
        return copyMove;
    }
}
