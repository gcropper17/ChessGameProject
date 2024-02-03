package chess;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * 
 * Note: You can add to this interface, but you should not alter the existing
 * methods.
 */
public interface ChessPiece {

    /**
     * The various different chess piece options
     */
    enum PieceType {
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
    ChessGame.TeamColor getTeamColor();

    /**
     * @return which type of chess piece this piece is
     */
    PieceType getPieceType();

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     * 
     * @return Collection of valid moves
     */
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
        public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            String pieceType = el.getAsJsonObject().get("pieceType").getAsString();
            if (Objects.equals(pieceType, "KING")) {
                //return ctx.deserialize(el, King.class);
                return new Gson().fromJson(el, King.class);
            } else if (Objects.equals(pieceType, "BISHOP")) {
                //return ctx.deserialize(el, Bishop.class);
                return new Gson().fromJson(el, Bishop.class);
            } else if (Objects.equals(pieceType, "KNIGHT")) {
                //return ctx.deserialize(el, Knight.class);
                return new Gson().fromJson(el, Knight.class);
            } else if (Objects.equals(pieceType, "PAWN")) {
                //return ctx.deserialize(el, Pawn.class);
                return new Gson().fromJson(el, Pawn.class);
            } else if (Objects.equals(pieceType, "QUEEN")) {
                //return ctx.deserialize(el, Queen.class);
                return new Gson().fromJson(el, Queen.class);
            } else {
                //return ctx.deserialize(el, Rook.class);
                return new Gson().fromJson(el, Rook.class);
            }
        }
    }
}
