package name.ulbricht.chess.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import name.ulbricht.chess.fx.design.BoardRenderer;
import name.ulbricht.chess.game.*;

import java.util.List;
import java.util.Optional;

final class GameCanvas extends BoardCanvas {

    private Game game;

    private final ReadOnlyObjectWrapper<CheckState> checkStateProperty = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyBooleanWrapper undoAvailable = new ReadOnlyBooleanWrapper();
    private final ReadOnlyBooleanWrapper redoAvailable = new ReadOnlyBooleanWrapper();
    private final ReadOnlyListWrapper<Ply> displayedPliesProperty = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    GameCanvas() {
        selectedSquareProperty().addListener((observable, oldValue, newValue) -> updateDisplayedPlies());
        this.displayedPliesProperty.addListener((observable, oldValue, newValue) -> draw());

        setGame(new Game());
    }

    Game getGame() {
        return this.game;
    }

    void setGame(Game game) {
        this.game = game;
        setBoard(this.game.getBoard());
        updateGameProperties();
        //draw();
    }

    ReadOnlyObjectProperty<CheckState> checkStateProperty() {
        return this.checkStateProperty.getReadOnlyProperty();
    }

    ReadOnlyBooleanProperty undoAvailableProperty() {
        return this.undoAvailable.getReadOnlyProperty();
    }

    ReadOnlyBooleanProperty redoAvailableProperty() {
        return this.redoAvailable.getReadOnlyProperty();
    }

    void undo() {
        this.game.undo();
        selectedSquareProperty().set(null);
        updateGameProperties();
    }

    void redo() {
        this.game.redo();
        selectedSquareProperty().set(null);
        updateGameProperties();
    }

    @Override
    protected String createTooltipText(Coordinate coordinate) {
        Optional<Ply> ply = this.displayedPliesProperty.stream()
                .filter(m -> coordinate.equals(m.target) || coordinate.equals(m.captures))
                .findFirst();
        if (ply.isPresent()) {
            return ply.get().getDisplayName();
        } else {
            return super.createTooltipText(coordinate);
        }
    }

    protected void selectSquare(Coordinate coordinate) {
        // check if we can execute a go
        Optional<Ply> ply = this.displayedPliesProperty.stream()
                .filter(m -> coordinate.equals(m.target) || coordinate.equals(m.captures))
                .findFirst();

        if (ply.isPresent()) {
            performPly(ply.get());
        } else {
            super.selectSquare(coordinate);
        }
    }

    private void performPly(Ply ply) {
        if (ply.type == PlyType.PAWN_PROMOTION) {
            PieceType promotion = PromotionController.showDialog(this, getRenderer(), ply);
            if (promotion != null) {
                ply.promotion = promotion;
            } else {
                return;
            }
        }

        this.game.perform(ply);
        setBoard(this.game.getBoard());

        selectedSquareProperty().set(null);
        focusedSquareProperty().set(ply.target);
        updateGameProperties();
    }

    private void updateGameProperties() {
        super.updateBoardProperties();
        this.checkStateProperty.set(this.game.getCheckState());
        this.undoAvailable.set(this.game.isUndoAvailable());
        this.redoAvailable.set(this.game.isRedoAvailable());
    }

    @Override
    protected BoardRenderer.SquareIndicator createSquareIndicator(Coordinate coordinate) {
        if (this.displayedPliesProperty.stream().anyMatch(m -> coordinate.equals(m.captures))) {
            return BoardRenderer.SquareIndicator.CAPTURED;
        } else if (this.displayedPliesProperty.stream().anyMatch(m -> coordinate.equals(m.target))) {
            return BoardRenderer.SquareIndicator.TARGET;
        }
        return null;
    }

    private void updateDisplayedPlies() {
        this.displayedPliesProperty.clear();
        Coordinate selected = selectedSquareProperty().get();
        if (selected != null) {
            List<Ply> plies = this.game.getValidPlies(selected);
            if (!plies.isEmpty()) this.displayedPliesProperty.addAll(plies);
        }
    }
}
