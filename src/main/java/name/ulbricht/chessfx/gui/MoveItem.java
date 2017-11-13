package name.ulbricht.chessfx.gui;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Move;

public final class MoveItem {

    enum Type {
        ROOT, FROM, MOVE
    }

    static MoveItem root() {
        return new MoveItem(Type.ROOT, null, null, null);
    }

    static MoveItem from(Board board, Coordinate from) {
        return new MoveItem(Type.FROM, board, from, null);
    }

    static MoveItem move(Board board, Move move) {
        return new MoveItem(Type.MOVE, board, null, move);
    }

    private final Type type;
    private final Coordinate from;
    private final Move move;
    private final ReadOnlyStringWrapper nameProperty = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper capturedProperty = new ReadOnlyStringWrapper();

    private MoveItem(Type type, Board board, Coordinate from, Move move) {
        this.type = type;
        switch (this.type) {
            case ROOT:
                this.from = null;
                this.move = null;
                this.nameProperty.set("root");
                break;
            case FROM:
                this.from = from;
                this.move = null;
                this.nameProperty.set(board.getSquare(this.from).toString());
                break;
            case MOVE:
                this.from = move.getFrom();
                this.move = move;
                this.nameProperty.set(move.getTo().toString());
                this.move.getCaptured().ifPresent(c -> this.capturedProperty.set(board.getSquare(c).toString()));
                break;
            default:
                throw new IllegalArgumentException("Unexpected type " + this.type);
        }

    }

    Type getType() {
        return type;
    }

    Coordinate getFrom() {
        return this.from;
    }

    public ReadOnlyStringProperty nameProperty() {
        return this.nameProperty.getReadOnlyProperty();
    }

    public String getName() {
        return nameProperty.get();
    }

    public ReadOnlyStringProperty capturedProperty() {
        return this.capturedProperty.getReadOnlyProperty();
    }

    public String getCaptured() {
        return capturedProperty.get();
    }

    Move getMove() {
        return this.move;
    }
}
