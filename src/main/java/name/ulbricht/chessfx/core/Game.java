package name.ulbricht.chessfx.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class Game {

    private final Board board;
    private Player currentPlayer;
    private final Map<Coordinate, List<Move>> legalMoves = new TreeMap<>();

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

    public Map<Coordinate, List<Move>> getLegalMoves() {
        return Collections.unmodifiableMap(this.legalMoves);
    }

    private void findLegalMoves() {
        this.legalMoves.clear();

        Rules rules = new Rules(this.board, this.currentPlayer);
        this.legalMoves.putAll(rules.getLegalMoves());
    }

    public void performMove(Move move) {
        move.perform(this.board);
        this.currentPlayer = this.currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        findLegalMoves();
    }
}
