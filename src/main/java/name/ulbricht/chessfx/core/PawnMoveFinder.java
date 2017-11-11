package name.ulbricht.chessfx.core;

import java.util.ArrayList;
import java.util.List;

final class PawnMoveFinder extends AbstractMoveFinder {

    private static PawnMoveFinder instance;

    static PawnMoveFinder getInstance() {
        if (instance == null) {
            instance = new PawnMoveFinder();
        }
        return instance;
    }

    @Override
    public List<Move> findMoves(Board board, Board.Square square) {
        List<Move> moves = new ArrayList<>();

        Player player = square.getPiece().getPlayer();

        int direction = player == Player.WHITE ? 1 : -1;

        Coordinate to = moveTo(square.getCoordinate(), 0, direction);
        if (to != null && board.getSquare(to).isEmpty()) moves.add(new Move(square, board.getSquare(to)));

        if (square.getPiece().getMoveCount() == 0) {
            to = moveTo(square.getCoordinate(), 0, 2 * direction);
            if (to != null && board.getSquare(to).isEmpty()) moves.add(new Move(square, board.getSquare(to)));
        }

        return moves;
    }
}
