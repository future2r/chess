package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

final class RookPlyTest extends AbstractPlyTest {

    @Test
    void whiteMove() {
        Game game = white("ke7", "Rd4", "Ke2");
        expectPlyNumber(game, "Rd4", 14);
        expectMoveRange(game, "Rd4", "d5", "d6", "d7", "d8");
        expectMoveRange(game, "Rd4", "e4", "f4", "g4", "h4");
        expectMoveRange(game, "Rd4", "d3", "d2", "d1");
        expectMoveRange(game, "Rd4", "c4", "b4", "a4");
    }

    @Test
    void whiteMove_leftBottomCorner() {
        Game game = white("ke7", "Ra1", "Ke2");
        expectPlyNumber(game, "Ra1", 14);
        expectMoveRange(game, "Ra1", "a2", "a3", "a4", "a5", "a6", "a7", "a8");
        expectMoveRange(game, "Ra1", "b1", "c1", "d1", "e1", "f1", "g1", "h1");
    }

    @Test
    void whiteMove_leftTopCorner() {
        Game game = white("ke7", "Ra8", "Ke2");
        expectPlyNumber(game, "Ra8", 14);
        expectMoveRange(game, "Ra8", "b8", "c8", "d8", "e8", "f8", "g8", "h8");
        expectMoveRange(game, "Ra8", "a7", "a6", "a5", "a4", "a3", "a2", "a1");
    }

    @Test
    void whiteMove_rightTopCorner() {
        Game game = white("ke7", "Rh8", "Ke2");
        expectPlyNumber(game, "Rh8", 14);
        expectMoveRange(game, "Rh8", "h7", "h6", "h5", "h4", "h3", "h2", "h1");
        expectMoveRange(game, "Rh8", "g8", "f8", "e8", "d8", "c8", "b8", "a8");
    }

    @Test
    void whiteMove_rightBottomCorner() {
        Game game = white("ke7", "Rh1", "Ke2");
        expectPlyNumber(game, "Rh1", 14);
        expectMoveRange(game, "Rh1", "g1", "f1", "e1", "d1", "c1", "b1", "a1");
        expectMoveRange(game, "Rh1", "h2", "h3", "h4", "h5", "h6", "h7", "h8");
    }

    @Test
    void whiteCaptures() {
        Game game = white("ke8", "Rd4", "pd6", "pf4", "rd2", "pb4", "Ke1");
        expectPlyNumber(game, "Rd4", 8);
        expectMove(game, "Rd4", "d5");
        expectMoveAndCaptures(game, "Rd4", "pd6");
        expectMove(game, "Rd4", "e4");
        expectMoveAndCaptures(game, "Rd4", "pf4");
        expectMove(game, "Rd4", "d3");
        expectMoveAndCaptures(game, "Rd4", "rd2");
        expectMove(game, "Rd4", "c4");
        expectMoveAndCaptures(game, "Rd4", "pb4");
    }

    @Test
    void whiteCapturesDirect() {
        Game game = white("ke8", "Rd4", "pd5", "pe4", "rd3", "pc4", "Ke1");
        expectPlyNumber(game, "Rd4", 4);
        expectMoveAndCaptures(game, "Rd4", "pd5");
        expectMoveAndCaptures(game, "Rd4", "pe4");
        expectMoveAndCaptures(game, "Rd4", "rd3");
        expectMoveAndCaptures(game, "Rd4", "pc4");
    }

    @Test
    void whiteSelfBlocking() {
        Game game = white("ke8", "Rd4", "Pd6", "Pf4", "Rd2", "Pb4", "Ke1");
        expectPlyNumber(game, "Rd4", 4);
        expectMove(game, "Rd4", "d5");
        expectMove(game, "Rd4", "e4");
        expectMove(game, "Rd4", "d3");
        expectMove(game, "Rd4", "c4");
    }

    @Test
    void whiteSelfBlockingDirect() {
        Game game = white("ke8", "Rd4", "Pd5", "Pe4", "Rd3", "Pc4", "Ke1");
        expectPlyNumber(game, "Rd4", 0);
    }

    @Test
    void blackMove() {
        Game game = black("ke7", "rd4", "Ke2");
        expectPlyNumber(game, "rd4", 14);
        expectMoveRange(game, "rd4", "d5", "d6", "d7", "d8");
        expectMoveRange(game, "rd4", "e4", "f4", "g4", "h4");
        expectMoveRange(game, "rd4", "d3", "d2", "d1");
        expectMoveRange(game, "rd4", "c4", "b4", "a4");
    }

    @Test
    void blackMove_leftBottomCorner() {
        Game game = black("ke7", "ra1", "Ke2");
        expectPlyNumber(game, "ra1", 14);
        expectMoveRange(game, "ra1", "a2", "a3", "a4", "a5", "a6", "a7", "a8");
        expectMoveRange(game, "ra1", "b1", "c1", "d1", "e1", "f1", "g1", "h1");
    }

    @Test
    void blackMove_leftTopCorner() {
        Game game = black("ke7", "ra8", "Ke2");
        expectPlyNumber(game, "ra8", 14);
        expectMoveRange(game, "ra8", "b8", "c8", "d8", "e8", "f8", "g8", "h8");
        expectMoveRange(game, "ra8", "a7", "a6", "a5", "a4", "a3", "a2", "a1");
    }

    @Test
    void blackMove_rightTopCorner() {
        Game game = black("ke7", "rh8", "Ke2");
        expectPlyNumber(game, "rh8", 14);
        expectMoveRange(game, "rh8", "h7", "h6", "h5", "h4", "h3", "h2", "h1");
        expectMoveRange(game, "rh8", "g8", "f8", "e8", "d8", "c8", "b8", "a8");
    }

    @Test
    void blackMove_rightBottomCorner() {
        Game game = black("ke7", "rh1", "Ke2");
        expectPlyNumber(game, "rh1", 14);
        expectMoveRange(game, "rh1", "g1", "f1", "e1", "d1", "c1", "b1", "a1");
        expectMoveRange(game, "rh1", "h2", "h3", "h4", "h5", "h6", "h7", "h8");
    }

    @Test
    void blackCaptures() {
        Game game = black("ke8", "rd4", "Pd6", "Pf4", "Rd2", "Pb4", "Ke1");
        expectPlyNumber(game, "rd4", 8);
        expectMove(game, "rd4", "d5");
        expectMoveAndCaptures(game, "rd4", "Pd6");
        expectMove(game, "rd4", "e4");
        expectMoveAndCaptures(game, "rd4", "Pf4");
        expectMove(game, "rd4", "d3");
        expectMoveAndCaptures(game, "rd4", "Rd2");
        expectMove(game, "rd4", "c4");
        expectMoveAndCaptures(game, "rd4", "Pb4");
    }

    @Test
    void blackCapturesDirect() {
        Game game = black("ke8", "rd4", "Pd5", "Pe4", "Rd3", "Pc4", "Ke1");
        expectPlyNumber(game, "rd4", 4);
        expectMoveAndCaptures(game, "rd4", "Pd5");
        expectMoveAndCaptures(game, "rd4", "Pe4");
        expectMoveAndCaptures(game, "rd4", "Rd3");
        expectMoveAndCaptures(game, "rd4", "Pc4");
    }

    @Test
    void blackSelfBlocking() {
        Game game = black("ke8", "rd4", "pd6", "pf4", "rd2", "pb4", "Ke1");
        expectPlyNumber(game, "rd4", 4);
        expectMove(game, "rd4", "d5");
        expectMove(game, "rd4", "e4");
        expectMove(game, "rd4", "d3");
        expectMove(game, "rd4", "c4");
    }

    @Test
    void blackSelfBlockingDirect() {
        Game game = black("ke8", "rd4", "pd5", "pe4", "rd3", "pc4", "Ke1");
        expectPlyNumber(game, "rd4", 0);
    }
}
