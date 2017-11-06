package name.ulbricht.chessfx.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public final class BoardSetupTest {

    @Parameterized.Parameters(name = "{index}: {0} {1}")
    public static Collection<Object[]> createParameters() {

        Map<String, Figure> exp = new HashMap<>();

        exp.put("a1", new Figure(Figure.Type.ROOK, Player.WHITE));
        exp.put("b1", new Figure(Figure.Type.KNIGHT, Player.WHITE));
        exp.put("c1", new Figure(Figure.Type.BISHOP, Player.WHITE));
        exp.put("d1", new Figure(Figure.Type.QUEEN, Player.WHITE));
        exp.put("e1", new Figure(Figure.Type.KING, Player.WHITE));
        exp.put("f1", new Figure(Figure.Type.BISHOP, Player.WHITE));
        exp.put("g1", new Figure(Figure.Type.KNIGHT, Player.WHITE));
        exp.put("h1", new Figure(Figure.Type.ROOK, Player.WHITE));

        exp.put("a2", new Figure(Figure.Type.PAWN, Player.WHITE));
        exp.put("b2", new Figure(Figure.Type.PAWN, Player.WHITE));
        exp.put("c2", new Figure(Figure.Type.PAWN, Player.WHITE));
        exp.put("d2", new Figure(Figure.Type.PAWN, Player.WHITE));
        exp.put("e2", new Figure(Figure.Type.PAWN, Player.WHITE));
        exp.put("f2", new Figure(Figure.Type.PAWN, Player.WHITE));
        exp.put("g2", new Figure(Figure.Type.PAWN, Player.WHITE));
        exp.put("h2", new Figure(Figure.Type.PAWN, Player.WHITE));

        exp.put("a7", new Figure(Figure.Type.PAWN, Player.BLACK));
        exp.put("b7", new Figure(Figure.Type.PAWN, Player.BLACK));
        exp.put("c7", new Figure(Figure.Type.PAWN, Player.BLACK));
        exp.put("d7", new Figure(Figure.Type.PAWN, Player.BLACK));
        exp.put("e7", new Figure(Figure.Type.PAWN, Player.BLACK));
        exp.put("f7", new Figure(Figure.Type.PAWN, Player.BLACK));
        exp.put("g7", new Figure(Figure.Type.PAWN, Player.BLACK));
        exp.put("h7", new Figure(Figure.Type.PAWN, Player.BLACK));

        exp.put("a8", new Figure(Figure.Type.ROOK, Player.BLACK));
        exp.put("b8", new Figure(Figure.Type.KNIGHT, Player.BLACK));
        exp.put("c8", new Figure(Figure.Type.BISHOP, Player.BLACK));
        exp.put("d8", new Figure(Figure.Type.QUEEN, Player.BLACK));
        exp.put("e8", new Figure(Figure.Type.KING, Player.BLACK));
        exp.put("f8", new Figure(Figure.Type.BISHOP, Player.BLACK));
        exp.put("g8", new Figure(Figure.Type.KNIGHT, Player.BLACK));
        exp.put("h8", new Figure(Figure.Type.ROOK, Player.BLACK));

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
    private final Figure figure;

    public BoardSetupTest(Coordinate coordinate, Figure figure) {
        this.coordinate = coordinate;
        this.figure = figure;
    }

    @Test
    public void testSetup() {
        assertEquals("Unexpected figure", figure, board.getSquare(this.coordinate).getFigure());
    }
}
