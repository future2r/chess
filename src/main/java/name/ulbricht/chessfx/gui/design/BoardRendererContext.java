package name.ulbricht.chessfx.gui.design;

import name.ulbricht.chessfx.core.Board;

public interface BoardRendererContext {

    Board getBoard();

    Board.Square getFocusedSquare();

    Board.Square getSelectedSquare();
}
