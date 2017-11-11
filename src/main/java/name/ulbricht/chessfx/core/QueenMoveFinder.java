package name.ulbricht.chessfx.core;

import java.util.ArrayList;
import java.util.List;

final class QueenMoveFinder implements MoveFinder {

    private static QueenMoveFinder instance;

    static QueenMoveFinder getInstance() {
        if (instance == null) {
            instance = new QueenMoveFinder();
        }
        return instance;
    }

    @Override
    public List<Move> findMoves(Board board, Board.Square square) {
        return new ArrayList<>();
    }
}
