package name.ulbricht.chess.fx.design;

import name.ulbricht.chess.game.Board;
import name.ulbricht.chess.game.Coordinate;

public interface BoardRendererContext {

    Board getBoard();

    boolean isBoardFocused();

    Coordinate getFocusedSquare();

    Coordinate getSelectedSquare();

    boolean isDisplayedToSquare(Coordinate coordinate);

    boolean isDisplayedCapturedSquare(Coordinate coordinate);
}
