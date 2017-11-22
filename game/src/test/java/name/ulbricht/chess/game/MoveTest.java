package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class MoveTest {

    @Test
    void testSimpleNoCpature() {
        Game game = new Game();
        game.clear();

        Coordinate from = Coordinate.d4;
        game.setPiece(from, new Piece(PieceType.PAWN, Player.WHITE));

        Coordinate to = Coordinate.d5;

        Move move = Move.simple(game, from, to);

        assertEquals(from, move.getFrom());
        assertEquals(to, move.getTo());
        assertFalse(move.getCaptures().isPresent());
    }

    @Test
    void testSimpleCaptures() {
        Game game = new Game();
        game.clear();

        Coordinate from = Coordinate.d4;
        game.setPiece(from, new Piece(PieceType.QUEEN, Player.WHITE));

        Coordinate to = Coordinate.d5;
        game.setPiece(to, new Piece(PieceType.PAWN, Player.BLACK));

        Move move = Move.simple(game, from, to);

        assertEquals(from, move.getFrom());
        assertEquals(to, move.getTo());
        assertTrue(move.getCaptures().isPresent());
        assertEquals(to, move.getCaptures().get());
    }
}
