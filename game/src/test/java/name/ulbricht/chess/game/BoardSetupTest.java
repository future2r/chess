package name.ulbricht.chess.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public final class BoardSetupTest {

    @Parameterized.Parameters(name = "{index}: {0} {1}")
    public static Collection<Object[]> createParameters() {

        Map<String, Piece> exp = new HashMap<>();

        exp.put("a1", new Piece(PieceType.ROOK, Player.WHITE));
        exp.put("b1", new Piece(PieceType.KNIGHT, Player.WHITE));
        exp.put("c1", new Piece(PieceType.BISHOP, Player.WHITE));
        exp.put("d1", new Piece(PieceType.QUEEN, Player.WHITE));
        exp.put("e1", new Piece(PieceType.KING, Player.WHITE));
        exp.put("f1", new Piece(PieceType.BISHOP, Player.WHITE));
        exp.put("g1", new Piece(PieceType.KNIGHT, Player.WHITE));
        exp.put("h1", new Piece(PieceType.ROOK, Player.WHITE));

        exp.put("a2", new Piece(PieceType.PAWN, Player.WHITE));
        exp.put("b2", new Piece(PieceType.PAWN, Player.WHITE));
        exp.put("c2", new Piece(PieceType.PAWN, Player.WHITE));
        exp.put("d2", new Piece(PieceType.PAWN, Player.WHITE));
        exp.put("e2", new Piece(PieceType.PAWN, Player.WHITE));
        exp.put("f2", new Piece(PieceType.PAWN, Player.WHITE));
        exp.put("g2", new Piece(PieceType.PAWN, Player.WHITE));
        exp.put("h2", new Piece(PieceType.PAWN, Player.WHITE));

        exp.put("a7", new Piece(PieceType.PAWN, Player.BLACK));
        exp.put("b7", new Piece(PieceType.PAWN, Player.BLACK));
        exp.put("c7", new Piece(PieceType.PAWN, Player.BLACK));
        exp.put("d7", new Piece(PieceType.PAWN, Player.BLACK));
        exp.put("e7", new Piece(PieceType.PAWN, Player.BLACK));
        exp.put("f7", new Piece(PieceType.PAWN, Player.BLACK));
        exp.put("g7", new Piece(PieceType.PAWN, Player.BLACK));
        exp.put("h7", new Piece(PieceType.PAWN, Player.BLACK));

        exp.put("a8", new Piece(PieceType.ROOK, Player.BLACK));
        exp.put("b8", new Piece(PieceType.KNIGHT, Player.BLACK));
        exp.put("c8", new Piece(PieceType.BISHOP, Player.BLACK));
        exp.put("d8", new Piece(PieceType.QUEEN, Player.BLACK));
        exp.put("e8", new Piece(PieceType.KING, Player.BLACK));
        exp.put("f8", new Piece(PieceType.BISHOP, Player.BLACK));
        exp.put("g8", new Piece(PieceType.KNIGHT, Player.BLACK));
        exp.put("h8", new Piece(PieceType.ROOK, Player.BLACK));

        return Coordinate.values()
                .map(c -> new Object[]{c, exp.getOrDefault(c.toString(), null)})
                .collect(Collectors.toList());
    }

    private static final Board board;

    static {
        board = new Board();
        board.setup();
    }

    private final Coordinate coordinate;
    private final Piece piece;

    public BoardSetupTest(Coordinate coordinate, Piece piece) {
        this.coordinate = coordinate;
        this.piece = piece;
    }

    @Test
    public void testSetup() {
        if (piece == null) assertFalse(board.getPiece(this.coordinate).isPresent());
        else assertEquals("Unexpected piece", piece, board.getPiece(this.coordinate).get());
    }
}
