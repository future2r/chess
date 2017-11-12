package name.ulbricht.chessfx.gui.design;

import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Square;

public interface BoardRendererContext {

    Board getBoard();

    boolean isBoardFocused();

    Square getFocusedSquare();

    Square getSelectedSquare();

    boolean isDisplayedToSquare(Square square);

    boolean isDisplayedCapturedSquare(Square square);
}
