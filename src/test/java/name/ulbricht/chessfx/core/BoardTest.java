package name.ulbricht.chessfx.core;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class BoardTest {

    @Test
    public void testSetGetPiece() {

        Board board = new Board();
        Piece piece = new Piece(PieceType.PAWN, Player.WHITE);
        List<Coordinate> coordinates = Coordinate.values().collect(Collectors.toList());

        for (Coordinate setCoordinate : coordinates) {
            board.setPiece(setCoordinate, piece);

            for (Coordinate getCoordinate : coordinates) {

                if (setCoordinate.equals(getCoordinate)) {
                    assertEquals("piece expected", piece, board.getPiece(getCoordinate).get());
                } else {
                    assertFalse("no piece expected", board.getPiece(getCoordinate).isPresent());
                }
            }

            board.setPiece(setCoordinate, null);
        }
    }

    @Test
    public void testClone() {
        Board board = new Board();

        Board clone = board.clone();

        assertFalse(board == clone);
        assertEquals(board, clone);

    }
}
