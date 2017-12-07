package name.ulbricht.chess.game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a game on the board.
 */
public final class Game {

    private Board board;
    private CheckState checkState;
    private final List<Ply> validPlies = new ArrayList<>();

    private static class HistoryItem {
        final Board board;
        final Ply ply;

        private HistoryItem(Board board, Ply ply) {
            this.board = Objects.requireNonNull(board, "board cannot be null");
            this.ply = Objects.requireNonNull(ply, "ply cannot be null");
        }
    }

    private Stack<HistoryItem> history = new Stack<>();
    private Stack<HistoryItem> redo = new Stack<>();

    /**
     * Creates a new game. This game will have a new board with the initial positions of the pieces.
     */
    public Game() {
        this(Board.initial());
    }

    /**
     * Creates a new game. This game will have a new board with the initial positions of the pieces.
     */
    public Game(Board board) {
        // TODO we should check if there is a king for each side
        // TODO check if the en-passant target is correct
        this.board = board.clone();
        updateValidPlies();
    }

    public Board getBoard() {
        return this.board.clone();
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getActivePlayer() {
        return this.board.getActivePlayer();
    }

    public CheckState getCheckState() {
        return this.checkState;
    }

    /**
     * Returns the piece at the given coordinate. If the square is empty the returned value will be {@code null}.
     *
     * @param coordinate the coordinate
     * @return the piece or {@code null}
     */
    public Piece getPiece(Coordinate coordinate) {
        return this.board.getPiece(coordinate);
    }

    public List<Ply> getValidPlies() {
        return Collections.unmodifiableList(this.validPlies);
    }

    public List<Ply> getValidPlies(Coordinate source) {
        return this.validPlies.stream().filter(m -> m.source == source).collect(Collectors.toList());
    }

    /**
     * Updates the list of valid ply (half move).
     * <p>
     * A ply is valid if:
     * <ol>
     * <li>it is legal according to the move rules of the piece type. This may include checking for check of some
     * fields (e.g. for castling).</li>
     * <li>the king of the active player is not in check after the move</li>
     * </ol>
     */
    private void updateValidPlies() {
        this.checkState = CheckState.NONE;
        this.validPlies.clear();

        // find the squares attacked by the opponent
        List<Coordinate> attacked = Rules.attacks(this.board, this.board.getActivePlayer().opponent());

        // check if the king is in check
        Coordinate kingPosition = this.board.king(this.board.getActivePlayer());
        if (attacked.contains(kingPosition)) this.checkState = CheckState.CHECK;

        // find legal plies
        List<Ply> plies = new ArrayList<>();
        for (Coordinate source : Coordinate.values()) {
            Piece piece = getPiece(source);
            if (piece != null && piece.player == this.board.getActivePlayer()) {
                switch (piece.type) {
                    case QUEEN:
                        plies.addAll(Rules.plies(this.board, source, Integer.MAX_VALUE, MoveDirection.values()));
                        break;
                    case KING:
                        plies.addAll(Rules.plies(this.board, source, 1, MoveDirection.values()));
                        if (source == Rules.initialKingCoordinate(piece.player)) {
                            if (piece.player == Player.WHITE) {
                                if (this.board.isWhiteKingSideCastlingAvailable()) {
                                    Ply ply = Rules.kingSideCastlingPly(this.board, source, attacked);
                                    if (ply != null) plies.add(ply);
                                }
                                if (this.board.isWhiteQueenSideCastlingAvailable()) {
                                    Ply ply = Rules.queenSideCastlingPly(this.board, source, attacked);
                                    if (ply != null) plies.add(ply);
                                }
                            } else {
                                if (this.board.isBlackKingSideCastlingAvailable()) {
                                    Ply ply = Rules.kingSideCastlingPly(this.board, source, attacked);
                                    if (ply != null) plies.add(ply);
                                }
                                if (this.board.isBlackQueenSideCastlingAvailable()) {
                                    Ply ply = Rules.queenSideCastlingPly(this.board, source, attacked);
                                    if (ply != null) plies.add(ply);
                                }
                            }
                        }
                        break;
                    case ROOK:
                        plies.addAll(Rules.plies(this.board, source, Integer.MAX_VALUE,
                                MoveDirection.UP, MoveDirection.RIGHT, MoveDirection.DOWN, MoveDirection.LEFT));
                        break;
                    case BISHOP:
                        plies.addAll(Rules.plies(this.board, source, Integer.MAX_VALUE,
                                MoveDirection.UP_LEFT, MoveDirection.UP_RIGHT, MoveDirection.DOWN_RIGHT, MoveDirection.DOWN_LEFT));
                        break;
                    case KNIGHT:
                        plies.addAll(Rules.plies(this.board, source, 1, KnightJump.values()));
                        break;
                    case PAWN:
                        plies.addAll(Rules.pawnPlies(this.board, source, this.board.getEnPassantTarget()));
                        break;
                }
            }
        }

        // simulate all plies to verify that there is no check after the move
        for (Ply ply : plies) {

            // perform the ply on a temporary board
            Board simBoard = this.board.clone();
            Rules.performPly(simBoard, ply);

            // find the king of this player (may have moved) for check test
            kingPosition = simBoard.king(this.board.getActivePlayer());

            // check if the king is in check after this move
            List<Coordinate> simAttacked = Rules.attacks(simBoard, this.board.getActivePlayer().opponent());
            if (!simAttacked.contains(kingPosition)) {
                this.validPlies.add(ply);
            }
        }

        // if there are no valid move, this should be checkmate!
        if (this.validPlies.isEmpty()) this.checkState = CheckState.CHECKMATE;
    }

    /**
     * Performs the specified ply in this game. The piece will be moved and the the current player is switched.
     *
     * @param ply the ply to perform
     */
    public void perform(Ply ply) {
        // actually perform the ply
        doPerform(ply);

        // the game goes on, clear the redo buffer
        this.redo.clear();
    }

    private void doPerform(Ply ply) {
        // must be a known legal ply
        if (!this.validPlies.contains(ply))
            throw new IllegalArgumentException("Not a valid ply");

        // try to perform the ply on a copy of the board
        Board nextBoard = this.board.clone();
        Rules.performPly(nextBoard, ply);

        // add current board and ply to history
        history.push(new HistoryItem(this.board, ply));

        // replace the current board
        this.board = nextBoard;

        // find valid plies
        updateValidPlies();
    }

    public boolean isUndoAvailable() {
        return this.history.size() > 0;
    }

    public void undo() {
        if (isUndoAvailable()) {
            HistoryItem item = this.history.pop();

            // put the undone item into the redo stack
            this.redo.push(item);

            // replace the board with the previous board
            this.board = item.board;

            // find valid plies
            updateValidPlies();
        }
    }

    public boolean isRedoAvailable() {
        return this.redo.size() > 0;
    }

    public void redo() {
        if (isRedoAvailable()) {
            // get the latest item from the redo
            HistoryItem item = this.redo.pop();

            // perform the ply
            doPerform(item.ply);
        }
    }

    @Override
    public String toString() {
        return "{board=" + this.board +
                "\ncheckState=" + this.checkState +
                "\nvalidPlies=" + this.validPlies;
    }
}
