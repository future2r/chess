package name.ulbricht.chess.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import name.ulbricht.chess.fx.design.BoardDesign;
import name.ulbricht.chess.fx.design.BoardRenderer;
import name.ulbricht.chess.fx.design.BoardRendererContext;
import name.ulbricht.chess.game.*;

import java.util.List;
import java.util.Optional;

final class BoardCanvas extends Canvas {

    private static final class Dimensions {

        final double xOffset;
        final double yOffset;
        final double borderSize;
        final double squareSize;

        Dimensions(double width, double height, BoardRenderer renderer) {
            double prefSquareSize = renderer.getPrefSquareSize();
            double prefBorderSize = renderer.getPrefBorderSize();
            double prefBoardWidth = 2 * prefBorderSize + Coordinate.COLUMNS * prefSquareSize;
            double prefBoardHeight = 2 * prefBorderSize + Coordinate.ROWS * prefSquareSize;

            double widthScale = width / prefBoardWidth;
            double heightScale = height / prefBoardHeight;
            double scale = Math.min(widthScale, heightScale);

            this.xOffset = (width - (scale * prefBoardWidth)) / 2;
            this.yOffset = (height - (scale * prefBoardHeight)) / 2;

            this.borderSize = scale * prefBorderSize;
            this.squareSize = scale * prefSquareSize;
        }
    }

    private class RendererContextImpl implements BoardRendererContext {

        @Override
        public Game getBoard() {
            return BoardCanvas.this.game;
        }

        @Override
        public boolean isBoardFocused() {
            return BoardCanvas.this.isFocused();
        }

        @Override
        public Coordinate getFocusedSquare() {
            return BoardCanvas.this.focusedSquareProperty.get();
        }

        @Override
        public Coordinate getSelectedSquare() {
            return BoardCanvas.this.selectedSquareProperty.get();
        }

        @Override
        public boolean isDisplayedToSquare(Coordinate coordinate) {
            return BoardCanvas.this.displayedMovesProperty.stream()
                    .anyMatch(m -> coordinate.equals(m.getTo()));
        }

        @Override
        public boolean isDisplayedCapturedSquare(Coordinate coordinate) {
            return BoardCanvas.this.displayedMovesProperty.stream()
                    .anyMatch(m -> m.getCaptures().isPresent() && m.getCaptures().get().equals(coordinate));
        }
    }

    private Tooltip tooltip;

    private Game game;
    private BoardRenderer renderer;

    private final ReadOnlyObjectWrapper<Player> currentPlayerProperty = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<Coordinate> selectedSquareProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Coordinate> focusedSquareProperty = new SimpleObjectProperty<>();
    private final ReadOnlyListWrapper<Move> displayedMovesProperty = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    BoardCanvas() {
        this.game = new Game();

        setFocusTraversable(true);

        this.tooltip = new Tooltip();
        this.tooltip.setShowDelay(Duration.ZERO);
        this.tooltip.setOnShowing(e -> tooltipShowing());

        setOnMouseMoved(this::mouseMoved);
        setOnMousePressed(this::mousePressed);
        setOnKeyPressed(this::keyPressed);

        widthProperty().addListener((observable, oldValue, newValue) -> draw());
        heightProperty().addListener((observable, oldValue, newValue) -> draw());
        focusedProperty().addListener((observable, oldValue, newValue) -> draw());

        this.selectedSquareProperty.addListener((observable, oldValue, newValue) -> draw());
        this.selectedSquareProperty.addListener((observable, oldValue, newValue) -> updateDisplayedMoves());
        this.focusedSquareProperty.addListener((observable, oldValue, newValue) -> draw());
        this.displayedMovesProperty.addListener((observable, oldValue, newValue) -> draw());

        this.currentPlayerProperty.set(this.game.getCurrentPlayer());
    }

    Game getGame() {
        return this.game;
    }

    void setDesign(BoardDesign design) {
        this.renderer = design.createRenderer(new RendererContextImpl());
        draw();
    }

    void newGame() {
        this.game.start();
        this.currentPlayerProperty.set(this.game.getCurrentPlayer());
        this.focusedSquareProperty.set(null);
        this.selectedSquareProperty.set(null);
    }

    ReadOnlyObjectProperty<Player> currentPlayerProperty() {
        return this.currentPlayerProperty.getReadOnlyProperty();
    }

    ObjectProperty<Coordinate> selectedSquareProperty() {
        return this.selectedSquareProperty;
    }

    private void tooltipShowing() {
        Point2D pos = localToScreen(0, 0);
        this.tooltip.setX(pos.getX());
        this.tooltip.setY(pos.getY());
    }

    private void mouseMoved(MouseEvent e) {
        Coordinate coordinate = getSquareAt(e.getX(), e.getY());
        if (coordinate != null) {
            Piece piece = this.game.getPiece(coordinate);
            if (piece != null)
                tooltip.setText(coordinate.toString() + ' ' + piece.getType().getDisplayName() + ' ' + piece.getPlayer().getDisplayName());
            else tooltip.setText(coordinate.toString());
            Tooltip.install(this, tooltip);
            this.focusedSquareProperty.set(coordinate);
        } else {
            this.tooltip.setText("");
            Tooltip.uninstall(this, tooltip);
            this.focusedSquareProperty.set(null);
        }
    }

    private void mousePressed(MouseEvent e) {
        if (!isFocused()) requestFocus();

        Coordinate square = getSquareAt(e.getX(), e.getY());
        if (square != null) selectSquare(square);
    }

    private void keyPressed(KeyEvent e) {
        Coordinate focused = this.focusedSquareProperty.get();
        Coordinate selected = this.selectedSquareProperty.get();

        switch (e.getCode()) {
            case LEFT:
                if (focused != null) {
                    Coordinate left = focused.moveLeft();
                    if (left != null) this.focusedSquareProperty.set(left);
                    else
                        this.focusedSquareProperty.set(Coordinate.valueOf(Coordinate.COLUMNS - 1, focused.getRowIndex()));
                } else this.focusedSquareProperty.set(Coordinate.a1);
                e.consume();
                break;
            case RIGHT:
                if (focused != null) {
                    Coordinate right = focused.moveRight();
                    if (right != null) this.focusedSquareProperty.set(right);
                    else this.focusedSquareProperty.set(Coordinate.valueOf(0, focused.getRowIndex()));
                } else this.focusedSquareProperty.set(Coordinate.a1);
                e.consume();
                break;
            case UP:
                if (focused != null) {
                    Coordinate up = focused.moveUp();
                    if (up != null) this.focusedSquareProperty.set(up);
                    else this.focusedSquareProperty.set(Coordinate.valueOf(focused.getColumnIndex(), 0));
                } else this.focusedSquareProperty.set(Coordinate.a1);
                e.consume();
                break;
            case DOWN:
                if (focused != null) {
                    Coordinate down = focused.moveDown();
                    if (down != null) this.focusedSquareProperty.set(down);
                    else
                        this.focusedSquareProperty.set(Coordinate.valueOf(focused.getColumnIndex(), Coordinate.ROWS - 1));
                } else this.focusedSquareProperty.set(Coordinate.a1);
                e.consume();
                break;
            case ENTER:
                if (focused != null) {
                    selectSquare(focused);
                }
                break;
            case ESCAPE:
                if (selected != null) this.selectedSquareProperty.set(null);
                else if (focused != null) this.focusedSquareProperty.set(null);
                e.consume();
                break;
        }
    }

    private void selectSquare(Coordinate coordinate) {
        // check if we can execute a move
        Optional<Move> move = this.displayedMovesProperty.stream()
                .filter(m -> coordinate.equals(m.getTo()) || (m.getCaptures().isPresent() && coordinate.equals(m.getCaptures().get())))
                .findFirst();

        if (move.isPresent()) {
            performMove(move.get());
        } else {
            this.selectedSquareProperty.set(coordinate);
        }
    }

    private void performMove(Move move) {
        this.game.performMove(move);

        //System.out.println("change from: " + this.currentPlayerProperty.get());
        this.currentPlayerProperty.set(this.game.getCurrentPlayer());
        //System.out.println("         to: " + this.currentPlayerProperty.get());
        this.selectedSquareProperty.set(null);
        this.focusedSquareProperty.set(move.getTo());
    }


    private Coordinate getSquareAt(double x, double y) {
        if (this.renderer != null) {
            Dimensions dim = new Dimensions(getWidth(), getHeight(), this.renderer);
            int columnIndex = (int) Math.floor((x - dim.xOffset - dim.borderSize) / dim.squareSize);
            int rowIndex = (int) Math.floor(Coordinate.ROWS - (y - dim.yOffset - dim.borderSize) / dim.squareSize);
            if (columnIndex >= 0 && columnIndex < Coordinate.COLUMNS && rowIndex >= 0 && rowIndex < Coordinate.ROWS) {
                return Coordinate.valueOf(columnIndex, rowIndex);
            }
        }
        return null;
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();

        // clear the drawing
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        if (this.renderer == null) return;

        // calculate the dimensions
        Dimensions dim = new Dimensions(width, height, this.renderer);

        // draw the canvas background
        this.renderer.drawBackground(gc, width, height);

        // draw vertical borders
        double xLeftBorder = dim.xOffset;
        double xRightBorder = dim.xOffset + dim.borderSize + (Coordinate.COLUMNS * dim.squareSize);
        for (int rowIndex = 0; rowIndex < Coordinate.ROWS; rowIndex++) {
            double yBorder = dim.yOffset + dim.borderSize + ((Coordinate.ROWS - rowIndex - 1) * dim.squareSize);

            // left border
            gc.save();
            gc.translate(xLeftBorder, yBorder);
            this.renderer.drawBorder(gc, dim.borderSize, dim.squareSize, BoardRenderer.Border.LEFT, rowIndex);
            gc.restore();

            // right border
            gc.save();
            gc.translate(xRightBorder, yBorder);
            this.renderer.drawBorder(gc, dim.borderSize, dim.squareSize, BoardRenderer.Border.RIGHT, rowIndex);
            gc.restore();
        }

        // draw the horizontal borders
        double yTopBorder = dim.yOffset;
        double yBottomBorder = dim.yOffset + dim.borderSize + (Coordinate.ROWS * dim.squareSize);
        for (int columnIndex = 0; columnIndex < Coordinate.COLUMNS; columnIndex++) {
            double xBorder = dim.xOffset + dim.borderSize + (columnIndex * dim.squareSize);
            // top border
            gc.save();
            gc.translate(xBorder, yTopBorder);
            this.renderer.drawBorder(gc, dim.squareSize, dim.borderSize, BoardRenderer.Border.TOP, columnIndex);
            gc.restore();

            // bottom border
            gc.save();
            gc.translate(xBorder, yBottomBorder);
            this.renderer.drawBorder(gc, dim.squareSize, dim.borderSize, BoardRenderer.Border.BOTTOM, columnIndex);
            gc.restore();
        }

        // draw top-left corner
        gc.save();
        gc.translate(xLeftBorder, yTopBorder);
        this.renderer.drawCorner(gc, dim.borderSize, BoardRenderer.Corner.TOP_LEFT);
        gc.restore();

        // draw top-right corner
        gc.save();
        gc.translate(xRightBorder, yTopBorder);
        this.renderer.drawCorner(gc, dim.borderSize, BoardRenderer.Corner.TOP_RIGHT);
        gc.restore();

        // draw bottom-left corner
        gc.save();
        gc.translate(xLeftBorder, yBottomBorder);
        this.renderer.drawCorner(gc, dim.borderSize, BoardRenderer.Corner.BOTTOM_LEFT);
        gc.restore();

        // draw bottom-right corner
        gc.save();
        gc.translate(xRightBorder, yBottomBorder);
        this.renderer.drawCorner(gc, dim.borderSize, BoardRenderer.Corner.BOTTOM_RIGHT);
        gc.restore();

        // draw the squares
        for (Coordinate coordinate : Coordinate.values()) {

            double squareXOffset = dim.borderSize + dim.xOffset + (coordinate.getColumnIndex() * dim.squareSize);
            double squareYOffset = dim.borderSize + (dim.yOffset + ((Coordinate.ROWS - 1) * dim.squareSize))
                    - (coordinate.getRowIndex() * dim.squareSize);

            gc.save();
            gc.translate(squareXOffset, squareYOffset);
            this.renderer.drawSquare(gc, dim.squareSize, coordinate);
            gc.restore();
        }
    }

    private void updateDisplayedMoves() {
        this.displayedMovesProperty.clear();
        Coordinate selected = this.selectedSquareProperty.get();
        if (selected != null) {
            List<Move> legalMoves = this.game.getLegalMoves().get(selected);
            if (legalMoves != null) this.displayedMovesProperty.addAll(legalMoves);
        }
    }
}
