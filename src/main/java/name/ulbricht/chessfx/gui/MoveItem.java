package name.ulbricht.chessfx.gui;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import name.ulbricht.chessfx.core.Move;
import name.ulbricht.chessfx.core.Square;

public final class MoveItem {

    enum Type {
        ROOT, FROM, MOVE
    }

    static MoveItem root() {
        return new MoveItem(Type.ROOT, null, null);
    }

    static MoveItem from(Square square) {
        return new MoveItem(Type.FROM, square, null);
    }

    static MoveItem move(Move move) {
        return new MoveItem(Type.MOVE, null, move);
    }

    private final Type type;
    private final Square fromSquare;
    private final Move move;
    private final ReadOnlyStringWrapper nameProperty = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper capturedSquareProperty = new ReadOnlyStringWrapper();

    private MoveItem(Type type, Square fromSquare, Move move) {
        this.type = type;
        switch (this.type) {
            case ROOT:
                this.fromSquare = null;
                this.move = null;
                this.nameProperty.set("root");
                break;
            case FROM:
                this.fromSquare = fromSquare;
                this.move = null;
                this.nameProperty.set(fromSquare.toString());
                break;
            case MOVE:
                this.fromSquare = move.getFromSquare();
                this.move = move;
                this.nameProperty.set(move.getToSquare().getCoordinate().toString());
                this.capturedSquareProperty.set(move.getCapturedSquare() != null ? move.getCapturedSquare().toString() : null);
                break;
            default:
                throw new IllegalArgumentException("Unexpected type " + this.type);
        }

    }

    Type getType() {
        return type;
    }

    Square getFromSquare() {
        return this.fromSquare;
    }

    public ReadOnlyStringProperty nameProperty() {
        return this.nameProperty.getReadOnlyProperty();
    }

    public String getName() {
        return nameProperty.get();
    }

    public ReadOnlyStringProperty capturedSquareProperty() {
        return this.capturedSquareProperty.getReadOnlyProperty();
    }

    public String getCapturedSquare() {
        return capturedSquareProperty.get();
    }

    Move getMove() {
        return this.move;
    }
}
