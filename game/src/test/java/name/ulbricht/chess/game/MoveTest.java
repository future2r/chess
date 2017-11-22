package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class MoveTest {

    @Test
    void testSimpleNoCapture() {
        Game game = new Game();

        Coordinate source = Coordinate.d4;
        game.setup(c -> c == source ? new Piece(PieceType.PAWN, Player.WHITE) : null, Player.WHITE);

        Coordinate target = Coordinate.d5;

        Move move = Move.simple(game, source, target);

        assertEquals(source, move.getSource());
        assertEquals(target, move.getTarget());
        assertNull(move.getCaptures());
    }

    @Test
    void testSimpleCaptures() {
        Game game = new Game();

        Coordinate source = Coordinate.d4;
        Coordinate target = Coordinate.d5;

        game.setup(c -> {
            if (c == source) return new Piece(PieceType.QUEEN, Player.WHITE);
            if (c == target) return new Piece(PieceType.PAWN, Player.BLACK);
            return null;
        }, Player.WHITE);

        Move move = Move.simple(game, source, target);

        assertEquals(source, move.getSource());
        assertEquals(target, move.getTarget());
        assertEquals(target, move.getCaptures());
    }
}
