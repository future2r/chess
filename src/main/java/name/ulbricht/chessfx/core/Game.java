package name.ulbricht.chessfx.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class Game {

    private final Board board;
    private Player currentPlayer;
    private final Map<Square, List<Move>> legalMoves = new TreeMap<>();

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

    public Map<Square, List<Move>> getLegalMoves() {
        return Collections.unmodifiableMap(this.legalMoves);
    }

    private void findLegalMoves() {
        this.legalMoves.clear();
        this.legalMoves.putAll(Rules.findLegalMoves(this.board, this.currentPlayer));
    }

    public void performMove(Move move) {
        move.perform();
        this.currentPlayer = this.currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        findLegalMoves();
    }
}
