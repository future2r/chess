package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BoardTest {

    @Test
    public void testSetGetPiece() {

        Board board = new Board();
        Piece piece = new Piece(PieceType.PAWN, Player.WHITE);
        Coordinate[] coordinates = Coordinate.values();

        for (Coordinate setCoordinate : coordinates) {
            board.setPiece(setCoordinate, piece);

            for (Coordinate getCoordinate : coordinates) {

                if (setCoordinate.equals(getCoordinate)) {
                    assertEquals(piece, board.getPiece(getCoordinate).get(), "piece expected");
                } else {
                    assertFalse(board.getPiece(getCoordinate).isPresent(), "no piece expected");
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
