package name.ulbricht.chessfx.core;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BoardTest {

    @Test
    public void testFields() {
        Board board = new Board();

        Coordinate.values().forEach(c -> assertEquals("field coordinate does not match " + c, c, board.getField(c).getCoordinate()));
    }

    @Test
    public void testSetGetFigure() {

        Board board = new Board();
        Figure figure = new Figure(Figure.Type.PAWN, Player.WHITE);
        List<Coordinate> coordinates = Coordinate.values().collect(Collectors.toList());

        for (Coordinate setCoordinate : coordinates) {
            board.setFigure(setCoordinate, figure);

            for (Coordinate getCoordinate : coordinates) {

                if (setCoordinate.equals(getCoordinate)) {
                    assertEquals("figure expected", figure, board.getFigure(getCoordinate));
                } else {
                    assertNull("no figure expected", board.getFigure(getCoordinate));
                }
            }

            board.setFigure(setCoordinate, null);
        }
    }
}
