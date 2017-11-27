package name.ulbricht.chess.game;

/**
 * Represents a piece on a board. A piece is defined by a piece type and the player that owns this piece.
 */
public enum Piece {

    WHITE_PAWN(PieceType.PAWN, Player.WHITE, 'P'),
    WHITE_ROOK(PieceType.ROOK, Player.WHITE, 'R'),
    WHITE_KNIGHT(PieceType.KNIGHT, Player.WHITE, 'N'),
    WHITE_BISHOP(PieceType.BISHOP, Player.WHITE, 'B'),
    WHITE_QUEEN(PieceType.QUEEN, Player.WHITE, 'Q'),
    WHITE_KING(PieceType.KING, Player.WHITE, 'K'),
    BLACK_PAWN(PieceType.PAWN, Player.BLACK, 'p'),
    BLACK_ROOK(PieceType.ROOK, Player.BLACK, 'r'),
    BLACK_KNIGHT(PieceType.KNIGHT, Player.BLACK, 'n'),
    BLACK_BISHOP(PieceType.BISHOP, Player.BLACK, 'b'),
    BLACK_QUEEN(PieceType.QUEEN, Player.BLACK, 'q'),
    BLACK_KING(PieceType.KING, Player.BLACK, 'k');

    public final PieceType type;
    public final Player player;
    public final char san;

    Piece(PieceType type, Player player, char san) {
        this.type = type;
        this.player = player;
        this.san = san;
    }

    public String getDisplayName(){
        return String.format("%s (%s)", this.type.getDisplayName(), this.player.getDisplayName());
    }

    public static Piece ofSan(char c) {
        for (Piece piece : values()) {
            if (piece.san == c) return piece;
        }
        throw new IllegalArgumentException("Not a SAN piece character: " + c);
    }
}
