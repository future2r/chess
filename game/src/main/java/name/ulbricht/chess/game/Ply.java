package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Represents a go on a board.
 */
public final class Ply {

    static Ply simple(Piece piece, Coordinate source, Coordinate target) {
        Objects.requireNonNull(piece);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        return new Ply(PlyType.SIMPLE, piece, source, target, null, null);
    }

    static Ply simpleCaptures(Piece piece, Coordinate source, Coordinate target, Piece capturedPiece) {
        Objects.requireNonNull(piece);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        Objects.requireNonNull(capturedPiece);

        return new Ply(PlyType.SIMPLE, piece, source, target, target, capturedPiece);
    }

    static Ply pawnDoubleAdvance(Piece piece, Coordinate source) {
        Objects.requireNonNull(piece);
        Objects.requireNonNull(source);

        Coordinate target;
        if ((piece.player == Player.WHITE && source.rowIndex != 1) || (piece.player == Player.BLACK && source.rowIndex != 6))
            throw new IllegalArgumentException("Illegal source for player.");

        target = source.go(MoveDirection.forward(piece.player), 2);
        return new Ply(PlyType.PAWN_DOUBLE_ADVANCE, piece, source, target, null, null);
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
        return Objects.hash(this.type, this.source, this.target, this.captures);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Ply other = (Ply) obj;

        return Objects.equals(this.type, other.type)
                && Objects.equals(this.source, other.source)
                && Objects.equals(this.target, other.target)
                && Objects.equals(this.captures, other.captures);
    }
}
