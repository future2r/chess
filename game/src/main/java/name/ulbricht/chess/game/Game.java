package name.ulbricht.chess.game;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a game on the board.
 */
public final class Game {

    private final Board board;
    private Player currentPlayer;
    private final Map<Coordinate, List<Move>> legalMoves = new TreeMap<>();

    /**
     * Creates a new game. This game will have a new board with the initial positions of the pieces.
     */
    public Game() {
        this.board = new Board();
        start();
    }

    /**
     * Returns the current board.
     *
     * @return the current board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Starts a new game. Everything will be reset to the initial values.
     */
    public void start() {
        this.board.setup();
        this.currentPlayer = Player.WHITE;
        findLegalMoves();
    }

    /**
     * Returns a map with legal move for the current player. The keys are the coordinates of the piece to move. The
     * values are lists with legal moves for this piece.
     *
     * @return a map with pieces and their legal moves
     */
    public Map<Coordinate, List<Move>> getLegalMoves() {
        return Collections.unmodifiableMap(this.legalMoves);
    }

    private void findLegalMoves() {
        this.legalMoves.clear();

        Rules rules = new Rules(this.board, this.currentPlayer);
        this.legalMoves.putAll(rules.getLegalMoves());
    }

    /**
     * Performs the specified move in this game. The piece will be moved the the current player is switched.
     *
     * @param move the move to perform
     */
    public void performMove(Move move) {
        move.perform(this.board);
        this.currentPlayer = this.currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        findLegalMoves();
    }
}
