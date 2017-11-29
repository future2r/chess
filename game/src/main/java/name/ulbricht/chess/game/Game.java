package name.ulbricht.chess.game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a game on the board.
 */
public final class Game {

    private final Piece[] board;
    private Player activePlayer;
    private final List<Ply> opponentPlayerPlies = new ArrayList<>();
    private final List<Ply> activePlayerPlies = new ArrayList<>();

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

        for (Coordinate coordinate : Coordinate.values()) {
            setPiece(coordinate, setup.getPiece(coordinate));
        }
        this.activePlayer = setup.getActivePlayer();

        this.whiteKingSideCastlingAvailable = setup.isWhiteKingSideCastlingAvailable();
        this.whiteQueenSideCastlingAvailable = setup.isWhiteQueenSideCastlingAvailable();
        this.blackKingSideCastlingAvailable = setup.isBlackKingSideCastlingAvailable();
        this.blackQueenSideCastlingAvailable = setup.isBlackQueenSideCastlingAvailable();

        updateLegalPlies();
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

    private void movePiece(Coordinate source, Coordinate target) {
        Piece piece = getPiece(source);
        if (piece == null) throw new IllegalStateException("No piece to move");
        removePiece(source);
        setPiece(target, piece);
    }

    private void removePiece(Coordinate coordinate) {
        setPiece(coordinate, null);
    }

    public List<Ply> getActivePlayerPlies(Coordinate source) {
        return this.activePlayerPlies.stream().filter(m -> m.getSource() == source).collect(Collectors.toList());
    }

    private void updateLegalPlies() {
        this.opponentPlayerPlies.clear();
        this.activePlayerPlies.clear();

        // update opponent plies
        for (Coordinate coordinate : Coordinate.values()) {
            Piece piece = getPiece(coordinate);
            if (piece != null && piece.player == this.activePlayer.opponent()) {
                this.opponentPlayerPlies.addAll(findLegalPlies(this.activePlayer.opponent(), coordinate));
            }
        }

        // update activate player plies
        for (Coordinate coordinate : Coordinate.values()) {
            Piece piece = getPiece(coordinate);
            if (piece != null && piece.player == this.activePlayer) {
                this.activePlayerPlies.addAll(findLegalPlies(this.activePlayer, coordinate));
            }
        }
    }

    /**
     * Performs the specified ply in this game. The piece will be moved and the the current player is switched.
     *
     * @param ply the ply to perform
     */
    public void performPly(Ply ply) {
        // must be a known legal ply
        if (!this.activePlayerPlies.contains(ply))
            throw new IllegalArgumentException("Not a legal ply");

        // move the pieces
        switch (ply.getType()) {
            case MOVE:
                if (ply.getCaptures() != null) removePiece(ply.getCaptures());
                movePiece(ply.getSource(), ply.getTarget());
                break;
            case PAWN_DOUBLE_ADVANCE:
                movePiece(ply.getSource(), ply.getTarget());
                // TODO set en passant target
                break;
            case KING_SIDE_CASTLING: {
                movePiece(ply.getSource(), ply.getTarget());
                int row = this.activePlayer == Player.WHITE ? 0 : 7;
                movePiece(Coordinate.valueOf(7, row), Coordinate.valueOf(5, row));
            }
            break;
            case QUEEN_SIDE_CASTLING: {
                movePiece(ply.getSource(), ply.getTarget());
                int row = this.activePlayer == Player.WHITE ? 0 : 7;
                movePiece(Coordinate.valueOf(0, row), Coordinate.valueOf(2, row));
            }
            break;
            default:
                throw new IllegalArgumentException("Unsupported ply type: " + ply.getType());
        }

        // update castling availability
        switch (ply.getPiece()) {
            case WHITE_ROOK:
                if (ply.getSource() == Coordinate.a1) this.whiteQueenSideCastlingAvailable = false;
                if (ply.getSource() == Coordinate.h1) this.whiteKingSideCastlingAvailable = false;
                break;
            case BLACK_ROOK:
                if (ply.getSource() == Coordinate.a8) this.blackQueenSideCastlingAvailable = false;
                if (ply.getSource() == Coordinate.h8) this.blackKingSideCastlingAvailable = false;
                break;
            case WHITE_KING:
                if (ply.getSource() == Coordinate.e1) {
                    this.whiteQueenSideCastlingAvailable = false;
                    this.whiteKingSideCastlingAvailable = false;
                }
                break;
            case BLACK_KING:
                if (ply.getSource() == Coordinate.d8) {
                    this.blackQueenSideCastlingAvailable = false;
                    this.blackKingSideCastlingAvailable = false;
                }
                break;
        }
        if (ply.getCapturedPiece() != null) {
            switch (ply.getCapturedPiece()) {
                case WHITE_ROOK:
                    if (ply.getSource() == Coordinate.a1) this.whiteQueenSideCastlingAvailable = false;
                    if (ply.getSource() == Coordinate.h1) this.whiteKingSideCastlingAvailable = false;
                    break;
                case BLACK_ROOK:
                    if (ply.getSource() == Coordinate.a8) this.blackQueenSideCastlingAvailable = false;
                    if (ply.getSource() == Coordinate.h8) this.blackKingSideCastlingAvailable = false;
                    break;
            }
        }

        this.activePlayer = this.activePlayer.opponent();
        updateLegalPlies();
    }

    private List<Ply> findLegalPlies(Player player, Coordinate source) {
        Piece piece = getPiece(source);
        if (piece != null) {
            switch (piece.type) {
                case PAWN:
                    return findPawnPlies(player, source);
                case ROOK:
                    return findDirectionalPlies(player, source, Integer.MAX_VALUE,
                            MoveDirection.UP, MoveDirection.RIGHT, MoveDirection.DOWN, MoveDirection.LEFT);
                case KNIGHT:
                    return findKnightPlies(player, source);
                case BISHOP:
                    return findDirectionalPlies(player, source, Integer.MAX_VALUE,
                            MoveDirection.UP_LEFT, MoveDirection.UP_RIGHT, MoveDirection.DOWN_RIGHT, MoveDirection.DOWN_LEFT);
                case QUEEN:
                    return findDirectionalPlies(player, source, Integer.MAX_VALUE, MoveDirection.values());
                case KING:
                    return findKingPlies(player, source);
            }
        }
        return Collections.emptyList();
    }

    private List<Ply> findPawnPlies(Player player, Coordinate source) {
        Piece piece = expectPiece(source, player, PieceType.PAWN);
        List<Ply> plies = new ArrayList<>();
        MoveDirection moveDirection = MoveDirection.forward(this.activePlayer);
        int startRow = player == Player.WHITE ? 1 : 6;

        // one step forward
        Coordinate target = source.go(moveDirection);
        if (target != null) {
            if (getPiece(target) == null) plies.add(Ply.move(piece, source, target));

            // two steps forward (if not yet moved)
            if (source.rowIndex == startRow && getPiece(target) == null) {
                target = source.go(moveDirection, 2);
                if (target != null && getPiece(target) == null)
                    plies.add(Ply.pawnDoubleAdvance(piece, source));
            }
        }

        // check moveAndCaptures
        for (MoveDirection captures : new MoveDirection[]{MoveDirection.forwardLeft(player), MoveDirection.forwardRight(player)}) {
            target = source.go(captures);
            if (target != null) {
                Piece capturedPiece = getPiece(target);
                if (capturedPiece != null && capturedPiece.player.isOpponent(player))
                    plies.add(Ply.moveAndCaptures(piece, source, target, capturedPiece));
            }
        }

        return plies;
    }

    private List<Ply> findKnightPlies(Player player, Coordinate source) {
        Piece piece = expectPiece(source, player, PieceType.KNIGHT);
        List<Ply> plies = new ArrayList<>();
        for (KnightJump jump : KnightJump.values()) {
            Coordinate target = source.go(jump);
            if (target != null) {
                Piece capturedPiece = getPiece(target);
                if (capturedPiece == null)
                    plies.add(Ply.move(piece, source, target));
                else if (capturedPiece.player.isOpponent(player))
                    plies.add(Ply.moveAndCaptures(piece, source, target, capturedPiece));
            }
        }
        return plies;
    }

    /*
     * // TODO check these rules!
     * Eine Rochade kann nur dann ausgeführt werden, wenn
     * 1. der König noch nicht gezogen wurde,
     * 2. der beteiligte Turm noch nicht gezogen wurde,
     * 3. zwischen dem König und dem beteiligten Turm keine andere Figur steht,
     * 4. der König über kein Feld ziehen muss, das durch eine feindliche Figur bedroht wird,
     * 5. der König vor und nach Ausführung der Rochade nicht im Schach steht.
     */
    private List<Ply> findKingPlies(Player player, Coordinate source) {
        List<Ply> plies = new ArrayList<>();
        plies.addAll(findDirectionalPlies(player, source, 1, MoveDirection.values()));

        boolean kingSideAvailable = player == Player.WHITE ? this.whiteKingSideCastlingAvailable : this.blackKingSideCastlingAvailable;
        boolean queenSideAvailable = player == Player.WHITE ? this.whiteQueenSideCastlingAvailable : this.blackQueenSideCastlingAvailable;
        if (!kingSideAvailable && !queenSideAvailable) return plies;

        int row = player == Player.WHITE ? 0 : 7;
        if (source.rowIndex != row || source.columnIndex != 4) return plies;

        Piece piece = expectPiece(source, player, PieceType.KING);

        Piece rook = player == Player.WHITE ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;
        if (kingSideAvailable
                && getPiece(Coordinate.valueOf(5, row)) == null
                && getPiece(Coordinate.valueOf(6, row)) == null
                && getPiece(Coordinate.valueOf(7, row)) == rook) {
            plies.add(Ply.kingSideCastling(piece));
        }
        if (queenSideAvailable
                && getPiece(Coordinate.valueOf(3, row)) == null
                && getPiece(Coordinate.valueOf(2, row)) == null
                && getPiece(Coordinate.valueOf(1, row)) == null
                && getPiece(Coordinate.valueOf(0, row)) == rook) {
            plies.add(Ply.queenSideCastling(piece));
        }
        return plies;
    }

    private List<Ply> findDirectionalPlies(Player player, Coordinate source, int maxSteps, MoveDirection... directions) {
        Piece piece = expectPiece(source, player, PieceType.ROOK, PieceType.BISHOP, PieceType.QUEEN, PieceType.KING);
        List<Ply> plies = new ArrayList<>();
        for (MoveDirection moveDirection : directions) {
            Coordinate target;
            int step = 1;
            do {
                target = source.go(moveDirection, step);
                if (target != null) {
                    Piece capturedPiece = getPiece(target);
                    if (capturedPiece == null) plies.add(Ply.move(piece, source, target));
                    else if (capturedPiece.player.isOpponent(player)) {
                        plies.add(Ply.moveAndCaptures(piece, source, target, capturedPiece));
                        break;
                    } else break;
                }
                step++;
            } while (step <= maxSteps && target != null);
        }
        return plies;
    }

    private Piece expectPiece(Coordinate coordinate, Player player, PieceType... pieceTypes) {
        Piece piece = getPiece(coordinate);
        if (piece == null) throw new IllegalStateException("Piece expected");
        if (piece.player != player)
            throw new IllegalStateException("Expected piece of player: " + player);
        if (!Arrays.asList(pieceTypes).contains(piece.type))
            throw new IllegalArgumentException("Unexpected piece type: " + piece.type);
        return piece;
    }
}
