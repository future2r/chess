package name.ulbricht.chessfx.core;

import java.util.ArrayList;
import java.util.List;

final class KnightMoveFinder extends AbstractMoveFinder {

    private static KnightMoveFinder instance;

    static KnightMoveFinder getInstance() {
        if (instance == null) {
            instance = new KnightMoveFinder();
        }
        return instance;
    }

    @Override
    public List<Move> findMoves(Board board, Board.Square square) {
        List<Move> moves = new ArrayList<>();

        Player player = square.getPiece().getPlayer();

        int[][] targets = new int[][]{
                {-1, 2},
                {1, 2},
                {-2,1},
                {-2,-1},
                {2,1},
                {2,-1},
                {-1,-2},
                {1,-2}
        };

        for (int[] target : targets){
            Coordinate to = moveTo(square.getCoordinate(), target[0], target[1]);
            if (to != null && isValidTo(board.getSquare(to), player)) moves.add(new Move(square, board.getSquare(to)));
        }

        return moves;
    }

    private boolean isValidTo(Board.Square square, Player player) {
        return square.isEmpty() || square.getPiece().getPlayer() != player;
    }
}
