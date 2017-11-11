package name.ulbricht.chessfx.core;

import java.util.ArrayList;
import java.util.List;

final class BishopMoveFinder implements MoveFinder {

    private static BishopMoveFinder instance;

    static BishopMoveFinder getInstance() {
        if (instance == null) {
            instance = new BishopMoveFinder();
        }
        return instance;
    }

    @Override
    public List<Move> findMoves(Board board, Board.Square square) {
        return new ArrayList<>();
    }
}
