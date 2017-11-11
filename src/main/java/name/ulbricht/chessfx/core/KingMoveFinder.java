package name.ulbricht.chessfx.core;

import java.util.ArrayList;
import java.util.List;

final class KingMoveFinder implements MoveFinder {

    private static KingMoveFinder instance;

    static KingMoveFinder getInstance() {
        if (instance == null) {
            instance = new KingMoveFinder();
        }
        return instance;
    }

    @Override
    public List<Move> findMoves(Board board, Board.Square square) {
        return new ArrayList<>();
    }
}
