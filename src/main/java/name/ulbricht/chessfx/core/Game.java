package name.ulbricht.chessfx.core;

import java.util.*;
import java.util.stream.Collectors;

public final class Game {

    private final Board board;
    private Player currentPlayer;
    private final Map<Board.Square, List<Move>> legalMoves = new TreeMap<>();

    public Game() {
        this.board = new Board();
        start();
    }

    public Board getBoard() {
        return this.board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void start() {
        this.board.setup();
        this.currentPlayer = Player.WHITE;
        findLegalMoves();
    }

    public Map<Board.Square, List<Move>> getLegalMoves() {
        return Collections.unmodifiableMap(this.legalMoves);
    }

    private void findLegalMoves() {
        this.legalMoves.clear();

        List<Board.Square> legalSquares = this.board.getSquares()
                .filter(s -> !s.isEmpty())
                .filter(s -> s.getPiece().getPlayer() == this.currentPlayer)
                .collect(Collectors.toList());

        for (Board.Square legalSquare : legalSquares) {

            MoveFinder finder = MoveFinder.of(legalSquare.getPiece().getType());
            List<Move> moves = finder.findMoves(this.getBoard(), legalSquare);
            if (!moves.isEmpty()) {
                this.legalMoves.put(legalSquare, moves);
            }

        }
    }

}
