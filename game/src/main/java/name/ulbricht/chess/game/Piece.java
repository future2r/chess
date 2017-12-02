package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Represents a piece on a board. A piece is defined by a piece type and the player that owns this piece.
 */
public enum Piece {

    WHITE_PAWN(PieceType.PAWN, Player.WHITE),
    WHITE_ROOK(PieceType.ROOK, Player.WHITE),
    WHITE_KNIGHT(PieceType.KNIGHT, Player.WHITE),
    WHITE_BISHOP(PieceType.BISHOP, Player.WHITE),
    WHITE_QUEEN(PieceType.QUEEN, Player.WHITE),
    WHITE_KING(PieceType.KING, Player.WHITE),
    BLACK_PAWN(PieceType.PAWN, Player.BLACK),
    BLACK_ROOK(PieceType.ROOK, Player.BLACK),
    BLACK_KNIGHT(PieceType.KNIGHT, Player.BLACK),
    BLACK_BISHOP(PieceType.BISHOP, Player.BLACK),
    BLACK_QUEEN(PieceType.QUEEN, Player.BLACK),
    BLACK_KING(PieceType.KING, Player.BLACK);

    public final PieceType type;
    public final Player player;

    Piece(PieceType type, Player player) {
        this.type = type;
        this.player = player;
    }

    public String getDisplayName() {
        return String.format("%s (%s)", this.type.getDisplayName(), this.player.getDisplayName());
    }

    public static Piece valueOf(PieceType type, Player player) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(player);

        for (Piece piece : values()) {
            if (piece.type == type && piece.player == player) return piece;
        }

        throw new IllegalStateException("Piece " + type + " not found for player " + player);
    }
}
