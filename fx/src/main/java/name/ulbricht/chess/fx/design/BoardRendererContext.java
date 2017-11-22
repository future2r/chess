package name.ulbricht.chess.fx.design;

import name.ulbricht.chess.game.Coordinate;
import name.ulbricht.chess.game.Game;

public interface BoardRendererContext {

    Game getBoard();

    boolean isBoardFocused();

    Coordinate getFocusedSquare();

    Coordinate getSelectedSquare();

    boolean isDisplayedToSquare(Coordinate coordinate);

    boolean isDisplayedCapturedSquare(Coordinate coordinate);
}
