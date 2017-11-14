package name.ulbricht.chessfx.gui.design;

import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;

public interface BoardRendererContext {

    Board getBoard();

    boolean isBoardFocused();

    Coordinate getFocusedSquare();

    Coordinate getSelectedSquare();

    boolean isDisplayedToSquare(Coordinate coordinate);

    boolean isDisplayedCapturedSquare(Coordinate coordinate);
}
