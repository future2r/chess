package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

final class QueenPlyTest extends AbstractPlyTest {

    @Test
    void whiteMove() {
        Game game = white("ke7", "Qd4", "Ke2");
        expectPlyNumber(game, "Qd4", 27);
        expectMoveRange(game, "Qd4", "d5", "d6", "d7", "d8");
        expectMoveRange(game, "Qd4", "e5", "f6", "g7", "h8");
        expectMoveRange(game, "Qd4", "e4", "f4", "g4", "h4");
        expectMoveRange(game, "Qd4", "e3", "f2", "g1");
        expectMoveRange(game, "Qd4", "d3", "d2", "d1");
        expectMoveRange(game, "Qd4", "c3", "b2", "a1");
        expectMoveRange(game, "Qd4", "c4", "b4", "a4");
        expectMoveRange(game, "Qd4", "c5", "b6", "a7");
    }

    @Test
    void whiteMove_leftBottomCorner() {
        Game game = white("ke7", "Qa1", "Ke2");
        expectPlyNumber(game, "Qa1", 21);
        expectMoveRange(game, "Qa1", "a2", "a3", "a4", "a5", "a6", "a7", "a8");
        expectMoveRange(game, "Qa1", "b2", "c3", "d4", "e5", "f6", "g7", "h8");
        expectMoveRange(game, "Qa1", "b1", "c1", "d1", "e1", "f1", "g1", "h1");
    }

    @Test
    void whiteMove_leftTopCorner() {
        Game game = white("ke7", "Qa8", "Ke2");
        expectPlyNumber(game, "Qa8", 21);
        expectMoveRange(game, "Qa8", "b8", "c8", "d8", "e8", "f8", "g8", "h8");
        expectMoveRange(game, "Qa8", "b7", "c6", "d5", "e4", "f3", "g2", "h1");
        expectMoveRange(game, "Qa8", "a7", "a6", "a5", "a4", "a3", "a2", "a1");
    }

    @Test
    void whiteMove_rightTopCorner() {
        Game game = white("ke7", "Qh8", "Ke2");
        expectPlyNumber(game, "Qh8", 21);
        expectMoveRange(game, "Qh8", "h7", "h6", "h5", "h4", "h3", "h2", "h1");
        expectMoveRange(game, "Qh8", "g7", "f6", "e5", "d4", "c3", "b2", "a1");
        expectMoveRange(game, "Qh8", "g8", "f8", "e8", "d8", "c8", "b8", "a8");
    }

    @Test
    void whiteMove_rightBottomCorner() {
        Game game = white("ke7", "Qh1", "Ke2");
        expectPlyNumber(game, "Qh1", 21);
        expectMoveRange(game, "Qh1", "g1", "f1", "e1", "d1", "c1", "b1", "a1");
        expectMoveRange(game, "Qh1", "g2", "f3", "e4", "d5", "c6", "b7", "a8");
        expectMoveRange(game, "Qh1", "h2", "h3", "h4", "h5", "h6", "h7", "h8");
    }

    @Test
    void whiteCaptures() {
        Game game = white("ka8", "Qd4", "pd6", "pf6", "pf4", "rf2", "qd2", "rb2", "pb4", "pb6", "Kh1");
        expectPlyNumber(game, "Qd4", 16);
        expectMove(game, "Qd4", "d5");
        expectMoveAndCaptures(game, "Qd4", "pd6");
        expectMove(game, "Qd4", "e5");
        expectMoveAndCaptures(game, "Qd4", "pf6");
        expectMove(game, "Qd4", "e4");
        expectMoveAndCaptures(game, "Qd4", "pf4");
        expectMove(game, "Qd4", "e3");
        expectMoveAndCaptures(game, "Qd4", "rf2");
        expectMove(game, "Qd4", "d3");
        expectMoveAndCaptures(game, "Qd4", "qd2");
        expectMove(game, "Qd4", "c3");
        expectMoveAndCaptures(game, "Qd4", "rb2");
        expectMove(game, "Qd4", "c4");
        expectMoveAndCaptures(game, "Qd4", "pb4");
        expectMove(game, "Qd4", "c5");
        expectMoveAndCaptures(game, "Qd4", "pb6");
    }

    @Test
    void whiteCapturesDirect() {
        Game game = white("ka8", "Qd4", "pd5", "pe5", "pe4", "re3", "qd3", "rc3", "pc4", "pc5", "Kh1");
        expectPlyNumber(game, "Qd4", 8);
        expectMoveAndCaptures(game, "Qd4", "pd5");
        expectMoveAndCaptures(game, "Qd4", "pe5");
        expectMoveAndCaptures(game, "Qd4", "pe4");
        expectMoveAndCaptures(game, "Qd4", "re3");
        expectMoveAndCaptures(game, "Qd4", "qd3");
        expectMoveAndCaptures(game, "Qd4", "rc3");
        expectMoveAndCaptures(game, "Qd4", "pc4");
        expectMoveAndCaptures(game, "Qd4", "pc5");
    }

    @Test
    void whiteSelfBlocking() {
        Game game = white("ka8", "Qd4", "Pd6", "Pf6", "Pf4", "Rf2", "Bd2", "Rb2", "Pb4", "Pb6", "Kh1");
        expectPlyNumber(game, "Qd4", 8);
        expectMove(game, "Qd4", "d5");
        expectMove(game, "Qd4", "e5");
        expectMove(game, "Qd4", "e4");
        expectMove(game, "Qd4", "e3");
        expectMove(game, "Qd4", "d3");
        expectMove(game, "Qd4", "c3");
        expectMove(game, "Qd4", "c4");
        expectMove(game, "Qd4", "c5");
    }

    @Test
    void whiteSelfBlockingDirect() {
        Game game = white("ke8", "Qd4", "Pd5", "Pe5", "Pe4", "Re3", "Qd3", "Rc3", "Pc4", "Pc5", "Ke1");
        expectPlyNumber(game, "Qd4", 0);
    }

    @Test
    void blackMove() {
        Game game = black("ke7", "qd4", "Ke2");
        expectPlyNumber(game, "qd4", 27);
        expectMoveRange(game, "qd4", "d5", "d6", "d7", "d8");
        expectMoveRange(game, "qd4", "e5", "f6", "g7", "h8");
        expectMoveRange(game, "qd4", "e4", "f4", "g4", "h4");
        expectMoveRange(game, "qd4", "e3", "f2", "g1");
        expectMoveRange(game, "qd4", "d3", "d2", "d1");
        expectMoveRange(game, "qd4", "c3", "b2", "a1");
        expectMoveRange(game, "qd4", "c4", "b4", "a4");
        expectMoveRange(game, "qd4", "c5", "b6", "a7");
    }

    @Test
    void blackMove_leftBottomCorner() {
        Game game = black("ke7", "qa1", "Ke2");
        expectPlyNumber(game, "qa1", 21);
        expectMoveRange(game, "qa1", "a2", "a3", "a4", "a5", "a6", "a7", "a8");
        expectMoveRange(game, "qa1", "b2", "c3", "d4", "e5", "f6", "g7", "h8");
        expectMoveRange(game, "qa1", "b1", "c1", "d1", "e1", "f1", "g1", "h1");
    }

    @Test
    void blackMove_leftTopCorner() {
        Game game = black("ke7", "qa8", "Ke2");
        expectPlyNumber(game, "qa8", 21);
        expectMoveRange(game, "qa8", "b8", "c8", "d8", "e8", "f8", "g8", "h8");
        expectMoveRange(game, "qa8", "b7", "c6", "d5", "e4", "f3", "g2", "h1");
        expectMoveRange(game, "qa8", "a7", "a6", "a5", "a4", "a3", "a2", "a1");
    }

    @Test
    void blackMove_rightTopCorner() {
        Game game = black("ke7", "qh8", "Ke2");
        expectPlyNumber(game, "qh8", 21);
        expectMoveRange(game, "qh8", "h7", "h6", "h5", "h4", "h3", "h2", "h1");
        expectMoveRange(game, "qh8", "g7", "f6", "e5", "d4", "c3", "b2", "a1");
        expectMoveRange(game, "qh8", "g8", "f8", "e8", "d8", "c8", "b8", "a8");
    }

    @Test
    void blackMove_rightBottomCorner() {
        Game game = black("ke7", "qh1", "Ke2");
        expectPlyNumber(game, "qh1", 21);
        expectMoveRange(game, "qh1", "g1", "f1", "e1", "d1", "c1", "b1", "a1");
        expectMoveRange(game, "qh1", "g2", "f3", "e4", "d5", "c6", "b7", "a8");
        expectMoveRange(game, "qh1", "h2", "h3", "h4", "h5", "h6", "h7", "h8");
    }

    @Test
    void blackCaptures() {
        Game game = black("ka8", "qd4", "Pd6", "Pf6", "Pf4", "Rf2", "Qd2", "Rb2", "Pb4", "Pb6", "Kh1");
        expectPlyNumber(game, "qd4", 16);
        expectMove(game, "qd4", "d5");
        expectMoveAndCaptures(game, "qd4", "Pd6");
        expectMove(game, "qd4", "e5");
        expectMoveAndCaptures(game, "qd4", "Pf6");
        expectMove(game, "qd4", "e4");
        expectMoveAndCaptures(game, "qd4", "Pf4");
        expectMove(game, "qd4", "e3");
        expectMoveAndCaptures(game, "qd4", "Rf2");
        expectMove(game, "qd4", "d3");
        expectMoveAndCaptures(game, "qd4", "Qd2");
        expectMove(game, "qd4", "c3");
        expectMoveAndCaptures(game, "qd4", "Rb2");
        expectMove(game, "qd4", "c4");
        expectMoveAndCaptures(game, "qd4", "Pb4");
        expectMove(game, "qd4", "c5");
        expectMoveAndCaptures(game, "qd4", "Pb6");
    }

    @Test
    void blackCapturesDirect() {
        Game game = black("ka8", "qd4", "Pd5", "Pe5", "Pe4", "Re3", "Qd3", "Rc3", "Pc4", "Pc5", "Kh1");
        expectPlyNumber(game, "qd4", 8);
        expectMoveAndCaptures(game, "qd4", "Pd5");
        expectMoveAndCaptures(game, "qd4", "Pe5");
        expectMoveAndCaptures(game, "qd4", "Pe4");
        expectMoveAndCaptures(game, "qd4", "Re3");
        expectMoveAndCaptures(game, "qd4", "Qd3");
        expectMoveAndCaptures(game, "qd4", "Rc3");
        expectMoveAndCaptures(game, "qd4", "Pc4");
        expectMoveAndCaptures(game, "qd4", "Pc5");
    }

    @Test
    void blackSelfBlocking() {
        Game game = black("ka8", "qd4", "pd6", "pf6", "pf4", "rf2", "bd2", "rb2", "pb4", "pb6", "Kh1");
        expectPlyNumber(game, "qd4", 8);
        expectMove(game, "qd4", "d5");
        expectMove(game, "qd4", "e5");
        expectMove(game, "qd4", "e4");
        expectMove(game, "qd4", "e3");
        expectMove(game, "qd4", "d3");
        expectMove(game, "qd4", "c3");
        expectMove(game, "qd4", "c4");
        expectMove(game, "qd4", "c5");
    }

    @Test
    void blackSelfBlockingDirect() {
        Game game = black("ka8", "qd4", "pd5", "pe5", "pe4", "re3", "qd3", "rc3", "pc4", "pc5", "Kh1");
        expectPlyNumber(game, "qd4", 0);
    }
}
