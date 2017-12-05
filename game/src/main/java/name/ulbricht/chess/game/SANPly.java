package name.ulbricht.chess.game;

import java.util.Objects;

public final class SANPly {

    public final SANPlyType type;
    public final PieceType piece;
    public final char sourceColumn;
    public final char sourceRow;
    public final boolean captures;
    public final Coordinate target;
    public final PieceType promotion;
    public final boolean check;

    SANPly(SANPlyType type, PieceType piece, char sourceColumn, char sourceRow,
           boolean captures, Coordinate target, PieceType promotion, boolean check) {
        this.type = Objects.requireNonNull(type);
        this.piece = Objects.requireNonNull(piece);
        this.sourceColumn = sourceColumn;
        this.sourceRow = sourceRow;
        this.captures = captures;
        this.target = target;
        this.promotion = promotion;
        this.check = check;
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + this.type +
                " piece=" + this.piece +
                " sourceColumn=" + this.sourceColumn +
                " sourceRow=" + this.sourceRow +
                " captures=" + this.captures +
                " target=" + this.target +
                " promotion=" + this.promotion +
                " check=" + this.check +
                "}";
    }
}
