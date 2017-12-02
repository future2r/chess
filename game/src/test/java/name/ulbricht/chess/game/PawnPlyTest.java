package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

final class PawnPlyTest extends AbstractPlyTest {

    @Test
    void whiteMove() {
        Game game = white("ke7", "Pc3", "Ke2");
        expectPlyNumber(game, "Pc3", 1);
        expectMove(game, "Pc3", "c4");
    }

    @Test
    void whiteDoubleAdvance() {
        Game game = white("ke7", "Pc2", "Ke2");
        expectPlyNumber(game, "Pc2", 2);
        expectMove(game, "Pc2", "c3");
        expectPawnDoubleAdvance(game, "Pc2");
    }

    @Test
    void whiteCaptures() {
        Game game = white("ke7", "Pc3", "rb4", "rd4", "Ke2");
        expectPlyNumber(game, "Pc3", 3);
        expectMove(game, "Pc3", "c4");
        expectMoveAndCaptures(game, "Pc3", "rb4");
        expectMoveAndCaptures(game, "Pc3", "rd4");
    }

    @Test
    void whiteEnPassant() {
        Game game = black("ke8", "pe7", "Pf5", "Ke1");

        // black pawn double advances
        Ply ply = game.getValidPlies(Coordinate.e7).stream()
                .filter(p -> p.getTarget() == Coordinate.e5)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected ply not found"));
        game.performPly(ply);

        // check plies for white pawn
        expectPlyNumber(game, "Pf5", 2);
        expectMove(game, "Pf5", "f6");
    }

    @Test
    void blackMove() {
        Game game = black("ke7", "pc6", "Ke2");
        expectPlyNumber(game, "pc6", 1);
        expectMove(game, "pc6", "c5");
    }

    @Test
    void blackDoubleAdvance() {
        Game game = black("ke7", "pc7", "Ke2");
        expectPlyNumber(game, "pc7", 2);
        expectMove(game, "pc7", "c6");
        expectPawnDoubleAdvance(game, "pc7");
    }

    @Test
    void blackCaptures() {
        Game game = black("ke7", "pc6", "Rb5", "Rd5", "Ke2");
        expectPlyNumber(game, "pc6", 3);
        expectMove(game, "pc6", "c5");
        expectMoveAndCaptures(game, "pc6", "Rb5");
        expectMoveAndCaptures(game, "pc6", "Rd5");
    }
}
