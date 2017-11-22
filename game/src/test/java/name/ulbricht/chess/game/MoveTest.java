package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        assertNull(move.getCaptures());
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
        assertEquals(to, move.getCaptures());
    }
}
