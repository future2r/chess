package name.ulbricht.chess.game;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a game on the board.
 */
public final class Game {

    private final Piece[] board;
    private Player activePlayer;
    private final List<Ply> legalPlies = new ArrayList<>();
    private final List<Ply> plyHistory = new ArrayList<>();

    /**
     * Creates a new game. This game will have a new board with the initial positions of the pieces.
     */
    public Game() {
        this.board = new Piece[Coordinate.COLUMNS * Coordinate.ROWS];
        setupDefault();
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setup(FENPositions positions) {
        Arrays.fill(this.board, null);
        for (Coordinate coordinate : Coordinate.values()) {
            setPiece(coordinate, positions.getPiece(coordinate));
        }
        this.activePlayer = positions.getActivePlayer();
        findLegalMoves();
    }

    public void setupDefault() {
        setup(FENPositions.ofDefault());
    }

    /**
     * Returns a map with all pieces on the board. The keys are the coordinates and the values are the pieces. Empty
     * square will not be returned.
     *
     * @return a map with coordinates and pieces
     */
    public Map<Coordinate, Piece> pieces() {
        return Stream.of(Coordinate.values())
                .filter(c -> this.board[c.ordinal()] != null)
                .collect(Collectors.toMap(c -> c, c -> this.board[c.ordinal()]));
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
        if (piece == null) throw new IllegalStateException("No piece to go");
        removePiece(source);
        setPiece(target, piece);
    }

    private void removePiece(Coordinate coordinate) {
        setPiece(coordinate, null);
    }

    /**
     * Returns a list with legal go for the current player.
     *
     * @return a list of legal moves
     */
    public List<Ply> getLegalPlies() {
        return Collections.unmodifiableList(this.legalPlies);
    }

    public List<Ply> getLegalMoves(Coordinate source) {
        return this.legalPlies.stream().filter(m -> m.getSource() == source).collect(Collectors.toList());
    }

    private void findLegalMoves() {
        this.legalPlies.clear();

        for (Coordinate coordinate : Coordinate.values()) {
            Piece piece = getPiece(coordinate);
            if (piece != null && piece.player == this.activePlayer) {
                this.legalPlies.addAll(findLegalMoves(coordinate));
            }
        }
    }

    /**
     * Performs the specified ply in this game. The piece will be moved and the the current player is switched.
     *
     * @param ply the ply to perform
     */
    public void performMove(Ply ply) {
        if (!this.legalPlies.contains(ply))
            throw new IllegalArgumentException("Not a legal ply");

        switch (ply.getType()) {
            case SIMPLE:
                if (ply.getCaptures() != null) removePiece(ply.getCaptures());
                movePiece(ply.getSource(), ply.getTarget());
                break;
            case PAWN_DOUBLE_ADVANCE:
                movePiece(ply.getSource(), ply.getTarget());
                // TODO set en passant target
                break;
            default:
                throw new IllegalArgumentException("Unsupported ply type: " + ply.getType());
        }

        this.plyHistory.add(ply);
        this.activePlayer = this.activePlayer.opponent();
        findLegalMoves();
    }

    private List<Ply> findLegalMoves(Coordinate source) {
        Piece piece = getPiece(source);
        if (piece != null) {
            switch (piece.type) {
                case PAWN:
                    return findPawnMoves(source);
                case ROOK:
                    return findDirectionalMoves(source, Integer.MAX_VALUE,
                            MoveDirection.UP, MoveDirection.RIGHT, MoveDirection.DOWN, MoveDirection.LEFT);
                case KNIGHT:
                    return findKnightMoves(source);
                case BISHOP:
                    return findDirectionalMoves(source, Integer.MAX_VALUE,
                            MoveDirection.UP_LEFT, MoveDirection.UP_RIGHT, MoveDirection.DOWN_RIGHT, MoveDirection.DOWN_LEFT);
                case QUEEN:
                    return findDirectionalMoves(source, Integer.MAX_VALUE, MoveDirection.values());
                case KING:
                    return findDirectionalMoves(source, 1, MoveDirection.values());
            }
        }
        return Collections.emptyList();
    }

    private List<Ply> findPawnMoves(Coordinate source) {
        Piece piece = expectPiece(source, this.activePlayer, PieceType.PAWN);
        List<Ply> plies = new ArrayList<>();
        MoveDirection moveDirection = MoveDirection.forward(this.activePlayer);
        int startRow = this.activePlayer == Player.WHITE ? 1 : 6;

        // one step forward
        Coordinate target = source.go(moveDirection);
        if (target != null) {
            if (getPiece(target) == null) plies.add(Ply.simple(piece, source, target));

            // two steps forward (if not yet moved)
            if (source.rowIndex == startRow && getPiece(target) == null) {
                target = source.go(moveDirection, 2);
                if (target != null && getPiece(target) == null)
                    plies.add(Ply.pawnDoubleAdvance(piece, source));
            }
        }

        // check captures
        for (MoveDirection captures : new MoveDirection[]{MoveDirection.forwardLeft(this.activePlayer), MoveDirection.forwardRight(this.activePlayer)}) {
            target = source.go(captures);
            if (target != null) {
                Piece capturedPiece = getPiece(target);
                if (capturedPiece != null && capturedPiece.player.isOpponent(getActivePlayer()))
                    plies.add(Ply.simpleCaptures(piece, source, target, capturedPiece));
            }
        }

        return plies;
    }

    private List<Ply> findKnightMoves(Coordinate source) {
        Piece piece = expectPiece(source, this.activePlayer, PieceType.KNIGHT);
        List<Ply> plies = new ArrayList<>();
        for (KnightJump jump : KnightJump.values()) {
            Coordinate target = source.go(jump);
            if (target != null) {
                Piece capturedPiece = getPiece(target);
                if (capturedPiece == null)
                    plies.add(Ply.simple(piece, source, target));
                else if (capturedPiece.player.isOpponent(this.activePlayer))
                    plies.add(Ply.simpleCaptures(piece, source, target, capturedPiece));
            }
        }
        return plies;
    }

    private List<Ply> findDirectionalMoves(Coordinate source, int maxSteps, MoveDirection... directions) {
        Piece piece = expectPiece(source, this.activePlayer, PieceType.ROOK, PieceType.BISHOP, PieceType.QUEEN, PieceType.KING);
        List<Ply> plies = new ArrayList<>();
        for (MoveDirection moveDirection : directions) {
            Coordinate target;
            int step = 1;
            do {
                target = source.go(moveDirection, step);
                if (target != null) {
                    Piece capturedPiece = getPiece(target);
                    if (capturedPiece == null) plies.add(Ply.simple(piece, source, target));
                    else if (capturedPiece.player.isOpponent(this.activePlayer)) {
                        plies.add(Ply.simpleCaptures(piece, source, target, capturedPiece));
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
