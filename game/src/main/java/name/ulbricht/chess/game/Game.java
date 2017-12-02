package name.ulbricht.chess.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a game on the board.
 */
public final class Game {

    private final Piece[] board;
    private Player activePlayer;
    private CheckState checkState;
    private final List<Ply> validPlies = new ArrayList<>();

    private boolean whiteKingSideCastlingAvailable;
    private boolean whiteQueenSideCastlingAvailable;
    private boolean blackKingSideCastlingAvailable;
    private boolean blackQueenSideCastlingAvailable;
    private Coordinate enPassantTarget;

    /**
     * Creates a new game. This game will have a new board with the initial positions of the pieces.
     */
    public Game() {
        this(Setup.standard());
    }

    /**
     * Creates a new game. This game will have a new board with the initial positions of the pieces.
     */
    public Game(Setup setup) {
        this.board = new Piece[Coordinate.COLUMNS * Coordinate.ROWS];

        // TODO we should check if there is a king for each side
        for (Coordinate coordinate : Coordinate.values()) {
            setPiece(coordinate, setup.getPiece(coordinate));
        }

        this.activePlayer = setup.getActivePlayer();

        this.whiteKingSideCastlingAvailable = setup.isWhiteKingSideCastlingAvailable();
        this.whiteQueenSideCastlingAvailable = setup.isWhiteQueenSideCastlingAvailable();
        this.blackKingSideCastlingAvailable = setup.isBlackKingSideCastlingAvailable();
        this.blackQueenSideCastlingAvailable = setup.isBlackQueenSideCastlingAvailable();

        // TODO we should check if this can be valid
        this.enPassantTarget = setup.getEnPassantTarget();

        updateValidPlies();
    }

    public Setup getSetup() {
        Setup setup = Setup.empty();

        for (Coordinate coordinate : Coordinate.values()) {
            setup.setPiece(coordinate, getPiece(coordinate));
        }
        setup.setActivePlayer(this.activePlayer);

        setup.setWhiteKingSideCastlingAvailable(this.whiteKingSideCastlingAvailable);
        setup.setWhiteQueenSideCastlingAvailable(this.whiteQueenSideCastlingAvailable);
        setup.setBlackKingSideCastlingAvailable(this.blackKingSideCastlingAvailable);
        setup.setBlackQueenSideCastlingAvailable(this.blackQueenSideCastlingAvailable);

        setup.setEnPassantTarget(this.enPassantTarget);

        // TODO half move clock
        // TODO full move number

        return setup;
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getActivePlayer() {
        return activePlayer;
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
        return this.board[Objects.requireNonNull(coordinate, "coordinate cannot be null").ordinal()];
    }

    private void setPiece(Coordinate coordinate, Piece piece) {
        this.board[coordinate.ordinal()] = piece;
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
        List<Coordinate> attacked = Rules.attacks(this.board, this.activePlayer.opponent());

        // check if the king is in check
        Coordinate kingPosition = Rules.king(this.board, this.activePlayer);
        if (attacked.contains(kingPosition)) this.checkState = CheckState.CHECK;

        // find legal plies
        List<Ply> plies = new ArrayList<>();
        for (Coordinate source : Coordinate.values()) {
            Piece piece = getPiece(source);
            if (piece != null && piece.player == this.activePlayer) {
                switch (piece.type) {
                    case QUEEN:
                        plies.addAll(Rules.plies(this.board, source, Integer.MAX_VALUE, MoveDirection.values()));
                        break;
                    case KING:
                        plies.addAll(Rules.plies(this.board, source, 1, MoveDirection.values()));
                        if (piece.player == Player.WHITE) {
                            if (this.whiteKingSideCastlingAvailable) {
                                Ply ply = Rules.kingSideCastlingPly(this.board, source, attacked);
                                if (ply != null) plies.add(ply);
                            }
                            if (this.whiteQueenSideCastlingAvailable) {
                                Ply ply = Rules.queenSideCastlingPly(this.board, source, attacked);
                                if (ply != null) plies.add(ply);
                            }
                        } else {
                            if (this.blackKingSideCastlingAvailable) {
                                Ply ply = Rules.kingSideCastlingPly(this.board, source, attacked);
                                if (ply != null) plies.add(ply);
                            }
                            if (this.blackQueenSideCastlingAvailable) {
                                Ply ply = Rules.queenSideCastlingPly(this.board, source, attacked);
                                if (ply != null) plies.add(ply);
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
                        plies.addAll(Rules.pawnPlies(this.board, source, this.enPassantTarget));
                        break;
                }
            }
        }

        // simulate all plies to verify that there is no check after the move
        for (Ply ply : plies) {

            // perform the ply on a temporary board
            Piece[] simBoard = this.board.clone();
            Rules.performPly(simBoard, ply);

            // find the king of this player (may have moved) for check test
            kingPosition = Rules.king(simBoard, this.activePlayer);

            // check if the king is in check after this move
            List<Coordinate> simAttacked = Rules.attacks(simBoard, this.activePlayer.opponent());
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
    public void performPly(Ply ply) {
        // must be a known legal ply
        if (!this.validPlies.contains(ply))
            throw new IllegalArgumentException("Not a valid ply");

        Rules.performPly(this.board, ply);

        // set en passant target
        if (ply.type == PlyType.PAWN_DOUBLE_ADVANCE) {
            Coordinate target = ply.target;
            this.enPassantTarget = Coordinate.valueOf(target.columnIndex, ply.piece.player == Player.WHITE ? 2 : 5);
        } else {
            this.enPassantTarget = null;
        }

        // update castling availability
        switch (ply.piece) {
            case WHITE_ROOK:
                if (ply.source == Coordinate.a1) this.whiteQueenSideCastlingAvailable = false;
                if (ply.source == Coordinate.h1) this.whiteKingSideCastlingAvailable = false;
                break;
            case BLACK_ROOK:
                if (ply.source == Coordinate.a8) this.blackQueenSideCastlingAvailable = false;
                if (ply.source == Coordinate.h8) this.blackKingSideCastlingAvailable = false;
                break;
            case WHITE_KING:
                if (ply.source == Coordinate.e1) {
                    this.whiteQueenSideCastlingAvailable = false;
                    this.whiteKingSideCastlingAvailable = false;
                }
                break;
            case BLACK_KING:
                if (ply.source == Coordinate.d8) {
                    this.blackQueenSideCastlingAvailable = false;
                    this.blackKingSideCastlingAvailable = false;
                }
                break;
        }
        if (ply.capturedPiece != null) {
            switch (ply.capturedPiece) {
                case WHITE_ROOK:
                    if (ply.source == Coordinate.a1) this.whiteQueenSideCastlingAvailable = false;
                    if (ply.source == Coordinate.h1) this.whiteKingSideCastlingAvailable = false;
                    break;
                case BLACK_ROOK:
                    if (ply.source == Coordinate.a8) this.blackQueenSideCastlingAvailable = false;
                    if (ply.source == Coordinate.h8) this.blackKingSideCastlingAvailable = false;
                    break;
            }
        }

        this.activePlayer = this.activePlayer.opponent();
        updateValidPlies();
    }
}
