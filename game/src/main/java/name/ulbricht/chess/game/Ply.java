package name.ulbricht.chess.game;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a go on a board.
 */
public final class Ply {

    static Ply move(Piece piece, Coordinate source, Coordinate target) {
        requirePieceType(piece, PieceType.values());
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        return new Ply(PlyType.MOVE, piece, source, target, null, null);
    }

    static Ply captures(Piece piece, Coordinate source, Coordinate target, Piece capturedPiece) {
        requirePieceType(piece, PieceType.values());
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        Objects.requireNonNull(capturedPiece);

        return new Ply(PlyType.CAPTURES, piece, source, target, target, capturedPiece);
    }

    static Ply pawnDoubleAdvance(Piece piece, Coordinate source) {
        requirePieceType(piece, PieceType.PAWN);
        Objects.requireNonNull(source);

        Coordinate target;
        if ((piece.player == Player.WHITE && source.rowIndex != 1) || (piece.player == Player.BLACK && source.rowIndex != 6))
            throw new IllegalArgumentException("Illegal source for player.");
        target = source.go(MoveDirection.forward(piece.player), 2);

        return new Ply(PlyType.PAWN_DOUBLE_ADVANCE, piece, source, target, null, null);
    }

    static Ply kingSideCastling(Piece piece) {
        requirePieceType(piece, PieceType.KING);

        int row = piece.player == Player.WHITE ? 0 : 7;
        Coordinate source = Coordinate.valueOf(4, row);
        Coordinate target = Coordinate.valueOf(6, row);

        return new Ply(PlyType.KING_SIDE_CASTLING, piece, source, target, null, null);
    }

    static Ply queenSideCastling(Piece piece) {
        requirePieceType(piece, PieceType.KING);

        int row = piece.player == Player.WHITE ? 0 : 7;
        Coordinate source = Coordinate.valueOf(4, row);
        Coordinate target = Coordinate.valueOf(1, row);

        return new Ply(PlyType.QUEEN_SIDE_CASTLING, piece, source, target, null, null);
    }

    private static void requirePieceType(Piece piece, PieceType... validPieceTypes) {
        Objects.requireNonNull(piece, "piece cannot be null");
        if (!Arrays.asList(validPieceTypes).contains(piece.type))
            throw new IllegalArgumentException("Illegal piece type: " + piece.type);
    }

    private final PlyType type;
    private final Piece piece;
    private final Coordinate source;
    private final Coordinate target;
    private final Coordinate captures;
    private final Piece capturedPiece;

    private Ply(PlyType type, Piece piece, Coordinate source, Coordinate target, Coordinate captures, Piece capturedPiece) {
        this.type = type;
        this.piece = piece;
        this.source = source;
        this.target = target;
        this.captures = captures;
        this.capturedPiece = capturedPiece;
    }

    public PlyType getType() {
        return this.type;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public Coordinate getSource() {
        return this.source;
    }

    public Coordinate getTarget() {
        return this.target;
    }

    public Coordinate getCaptures() {
        return this.captures;
    }

    public Piece getCapturedPiece() {
        return this.capturedPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.piece, this.source, this.target, this.captures, this.capturedPiece);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Ply other = (Ply) obj;

        return Objects.equals(this.type, other.type)
                && Objects.equals(this.piece, other.piece)
                && Objects.equals(this.source, other.source)
                && Objects.equals(this.target, other.target)
                && Objects.equals(this.captures, other.captures)
                && Objects.equals(this.capturedPiece, other.capturedPiece);
    }

    public String getDisplayName() {
        String key = "PlyType." + this.type.name() + ".displayNamePattern";
        switch (this.type) {
            case MOVE:
                return String.format(Messages.getString(key), this.piece.getDisplayName(), this.source, this.target);
            case CAPTURES:
                return String.format(Messages.getString(key), this.piece.getDisplayName(), this.source,
                        this.target, this.capturedPiece.getDisplayName());
            case QUEEN_SIDE_CASTLING:
                return String.format(Messages.getString(key), this.piece.getDisplayName(), this.source, this.target);
            case KING_SIDE_CASTLING:
                return String.format(Messages.getString(key), this.piece.getDisplayName(), this.source, this.target);
            case PAWN_DOUBLE_ADVANCE:
                return String.format(Messages.getString(key), this.piece.getDisplayName(), this.source, this.target);
            case PAWN_EN_PASSANT:
                return String.format(Messages.getString(key), this.piece.getDisplayName(), this.source,
                        this.target, this.capturedPiece.getDisplayName(), this.captures);
            case PAWN_PROMOTION:
                return String.format(Messages.getString(key), this.piece.getDisplayName(), this.source,
                        this.target);
            case PAWN_PROMOTION_CAPTURES:
                return String.format(Messages.getString(key), this.piece.getDisplayName(), this.source,
                        this.target, this.capturedPiece.getDisplayName());
            default:
                return null;
        }
    }
}
