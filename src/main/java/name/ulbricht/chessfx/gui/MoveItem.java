package name.ulbricht.chessfx.gui;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Move;

public final class MoveItem {

    enum Type  {
        ROOT, SOURCE, MOVE;
    }

    static MoveItem root(){
        return new MoveItem(Type.ROOT, null,null);
    }

    static MoveItem source(Board.Square square){
        return new MoveItem(Type.SOURCE, square, null);
    }

    static MoveItem move(Move move){
        return new MoveItem(Type.MOVE, null, move);
    }

    private Type type;
    private final ReadOnlyStringWrapper nameProperty = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper capturesProperty = new ReadOnlyStringWrapper();
    private Move move;

    MoveItem(Move move) {
        nameProperty.set("?#");
        capturesProperty.set("?");
    }

    private MoveItem(Type type, Board.Square source, Move move) {
        this.type=type;
        switch (this.type){
            case ROOT:
                nameProperty.set("root");
                break;
            case SOURCE:
                nameProperty.set(source.toString());
                break;
            case MOVE:
                nameProperty.set("?#");
                capturesProperty.set("?");
                break;
        }

    }

    public ReadOnlyStringProperty nameProperty() {
        return this.nameProperty.getReadOnlyProperty();
    }

    public String getName() {
        return nameProperty.get();
    }

    public ReadOnlyStringProperty capturesProperty() {
        return this.capturesProperty.getReadOnlyProperty();
    }

    public String getCaptures() {
        return capturesProperty.get();
    }

    Move getMove(){
        return this.move;
    }
}
