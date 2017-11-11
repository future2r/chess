package name.ulbricht.chessfx.core;

import java.util.ArrayList;
import java.util.List;

final class RookMoveFinder implements MoveFinder {

    private static RookMoveFinder instance;

    static RookMoveFinder getInstance() {
        if (instance == null) {
            instance = new RookMoveFinder();
        }
        return instance;
    }

    @Override
    public List<Move> findMoves(Board board, Board.Square square) {
        return new ArrayList<>();
    }
}
