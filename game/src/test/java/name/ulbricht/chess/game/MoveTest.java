package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class MoveTest {

    @Test
    void testSimpleNoCapture() {
        Game game = new Game();
        game.clear();

        Coordinate source = Coordinate.d4;
        game.setPiece(source, new Piece(PieceType.PAWN, Player.WHITE));

        Coordinate target = Coordinate.d5;

        Move move = Move.simple(game, source, target);

        assertEquals(source, move.getSource());
        assertEquals(target, move.getTarget());
        assertNull(move.getCaptures());
    }

    @Test
    void testSimpleCaptures() {
        Game game = new Game();
        game.clear();

        Coordinate source = Coordinate.d4;
        game.setPiece(source, new Piece(PieceType.QUEEN, Player.WHITE));

        Coordinate target = Coordinate.d5;
        game.setPiece(target, new Piece(PieceType.PAWN, Player.BLACK));

        Move move = Move.simple(game, source, target);

        assertEquals(source, move.getSource());
        assertEquals(target, move.getTarget());
        assertEquals(target, move.getCaptures());
    }
}
