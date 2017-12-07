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
                .filter(p -> p.target == Coordinate.e5)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected ply not found"));
        game.perform(ply);

        // check plies for white pawn
        expectPlyNumber(game, "Pf5", 2);
        expectMove(game, "Pf5", "f6");
        expectPawnEnPassant(game, "Pf5", "e6");
    }

    @Test
    void whitePromotion() {
        Game game = white("ke8", "rh8", "Pg7", "pb2", "Ra1", "Ke1");
        expectPlyNumber(game, "Pg7", 2);
        expectPawnPromotion(game, "Pg7", "g8");
        expectPawnPromotionAndCaptures(game, "Pg7", "rh8");
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

    @Test
    void blackEnPassant() {
        Game game = white("ke8", "pf4", "Pe2", "Ke1");

        // white pawn double advances
        Ply ply = game.getValidPlies(Coordinate.e2).stream()
                .filter(p -> p.target == Coordinate.e4)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected ply not found"));
        game.perform(ply);

        // check plies for black pawn
        expectPlyNumber(game, "pf4", 2);
        expectMove(game, "pf4", "f3");
        expectPawnEnPassant(game, "pf4", "e3");
    }

    @Test
    void blackPromotion() {
        Game game = black("ke8", "rh8", "Pg7", "pb2", "Ra1", "Ke1");
        expectPlyNumber(game, "pb2", 2);
        expectPawnPromotion(game, "pb2", "b1");
        expectPawnPromotionAndCaptures(game, "pb2", "Ra1");
    }
}
