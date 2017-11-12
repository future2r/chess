package name.ulbricht.chessfx.core;

import java.util.*;
import java.util.stream.Collectors;

final class Rules {

    static Map<Square, List<Move>> findLegalMoves(Board board, Player player) {
        Map<Square, List<Move>> legalMoves = new TreeMap<>();

        List<Square> legalSquares = board.squares()
                .filter(square -> !square.isEmpty())
                .filter(square -> square.getPiece().getPlayer() == player)
                .collect(Collectors.toList());

        for (Square fromSquare : legalSquares) {
            List<Move> moves = findLegalMoves(board, player, fromSquare);
            if (!moves.isEmpty()) {
                legalMoves.put(fromSquare, moves);
            }
        }
        return legalMoves;
    }

    private static List<Move> findLegalMoves(Board board, Player player, Square fromSquare) {
        switch (fromSquare.getPiece().getType()) {
            case PAWN:
                return findPawnMoves(board, player, fromSquare);
            case ROOK:
                return Collections.emptyList();
            case KNIGHT:
                return findKnightMoves(board, player, fromSquare);
            case BISHOP:
                return Collections.emptyList();
            case QUEEN:
                return Collections.emptyList();
            case KING:
                return Collections.emptyList();
            default:
                return Collections.emptyList();
        }
    }

    private static List<Move> findPawnMoves(Board board, Player player, Square fromSquare) {
        List<Move> moves = new ArrayList<>();
        Coordinate from = fromSquare.getCoordinate();
        int direction = player == Player.WHITE ? 1 : -1;

        // one step forward
        Optional<Coordinate> to = from.moveTo(0, direction);
        if (to.isPresent()) {
            Square toSquare = board.getSquare(to.get());
            if (toSquare.isEmpty()) moves.add(new Move(fromSquare, toSquare));
        }

        // two steps forward (if not yet moved)
        if (fromSquare.getPiece().getMoveCount() == 0) {
            to = from.moveTo(0, 2 * direction);
            if (to.isPresent()) {
                Square toSquare = board.getSquare(to.get());
                if (toSquare.isEmpty()) moves.add(new Move(fromSquare, toSquare));
            }
        }

        return moves;
    }

    private static List<Move> findKnightMoves(Board board, Player player, Square fromSquare) {
        List<Move> moves = new ArrayList<>();
        Coordinate from = fromSquare.getCoordinate();

        int[][] jumps = new int[][]{{-1, 2}, {1, 2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {-1, -2}, {1, -2}};
        for (int[] jump : jumps) {
            Optional<Coordinate> to = from.moveTo(jump[0], jump[1]);
            if (to.isPresent()) {
                Square toSquare = board.getSquare(to.get());
                if (toSquare.isEmpty() || toSquare.getPiece().getPlayer() != player)
                    moves.add(new Move(fromSquare, toSquare));
            }
        }

        return moves;
    }
}
