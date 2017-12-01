package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

final class BishopPlyTest extends AbstractPlyTest {

    @Test
    void whiteMove() {
        Game game = white("ke7", "Bd4", "Ke2");
        expectPlyNumber(game, "Bd4", 13);
        expectMoveRange(game, "Bd4", "e5", "f6", "g7", "h8");
        expectMoveRange(game, "Bd4", "e3", "f2", "g1");
        expectMoveRange(game, "Bd4", "c3", "b2", "a1");
        expectMoveRange(game, "Bd4", "c5", "b6", "a7");
    }

    @Test
    void whiteMove_leftBottomCorner() {
        Game game = white("ke7", "Ba1", "Ke2");
        expectPlyNumber(game, "Ba1", 7);
        expectMoveRange(game, "Ba1", "b2", "c3", "d4", "e5", "f6", "g7", "h8");
    }

    @Test
    void whiteMove_leftTopCorner() {
        Game game = white("ke7", "Ba8", "Ke2");
        expectPlyNumber(game, "Ba8", 7);
        expectMoveRange(game, "Ba8", "b7", "c6", "d5", "e4", "f3", "g2", "h1");
    }

    @Test
    void whiteMove_rightTopCorner() {
        Game game = white("ke7", "Bh8", "Ke2");
        expectPlyNumber(game, "Bh8", 7);
        expectMoveRange(game, "Bh8", "g7", "f6", "e5", "d4", "c3", "b2", "a1");
    }

    @Test
    void whiteMove_rightBottomCorner() {
        Game game = white("ke7", "Bh1", "Ke2");
        expectPlyNumber(game, "Bh1", 7);
        expectMoveRange(game, "Bh1", "g2", "f3", "e4", "d5", "c6", "b7", "a8");
    }

    @Test
    void whiteCaptures() {
        Game game = white("ka8", "Bd4", "pf6", "rf2", "rb2", "pb6", "Kh1");
        expectPlyNumber(game, "Bd4", 8);
        expectMove(game, "Bd4", "e5");
        expectMoveAndCaptures(game, "Bd4", "pf6");
        expectMove(game, "Bd4", "e3");
        expectMoveAndCaptures(game, "Bd4", "rf2");
        expectMove(game, "Bd4", "c3");
        expectMoveAndCaptures(game, "Bd4", "rb2");
        expectMove(game, "Bd4", "c5");
        expectMoveAndCaptures(game, "Bd4", "pb6");
    }

    @Test
    void whiteCapturesDirect() {
        Game game = white("ka8", "Bd4", "pe5", "re3", "rc3", "pc5", "Kh1");
        expectPlyNumber(game, "Bd4", 4);
        expectMoveAndCaptures(game, "Bd4", "pe5");
        expectMoveAndCaptures(game, "Bd4", "re3");
        expectMoveAndCaptures(game, "Bd4", "rc3");
        expectMoveAndCaptures(game, "Bd4", "pc5");
    }

    @Test
    void whiteSelfBlocking() {
        Game game = white("ka8", "Bd4", "Pf6", "Rf2", "Rb2", "Pb6", "Kh1");
        expectPlyNumber(game, "Bd4", 4);
        expectMove(game, "Bd4", "e5");
        expectMove(game, "Bd4", "e3");
        expectMove(game, "Bd4", "c3");
        expectMove(game, "Bd4", "c5");
    }

    @Test
    void whiteSelfBlockingDirect() {
        Game game = white("ke8", "Bd4", "Pe5", "Re3", "Rc3", "Pc5", "Ke1");
        expectPlyNumber(game, "Bd4", 0);
    }

    @Test
    void blackMove() {
        Game game = black("ke7", "bd4", "Ke2");
        expectPlyNumber(game, "bd4", 13);
        expectMoveRange(game, "bd4", "e5", "f6", "g7", "h8");
        expectMoveRange(game, "bd4", "e3", "f2", "g1");
        expectMoveRange(game, "bd4", "c3", "b2", "a1");
        expectMoveRange(game, "bd4", "c5", "b6", "a7");
    }

    @Test
    void blackMove_leftBottomCorner() {
        Game game = black("ke7", "ba1", "Ke2");
        expectPlyNumber(game, "ba1", 7);
        expectMoveRange(game, "ba1", "b2", "c3", "d4", "e5", "f6", "g7", "h8");
    }

    @Test
    void blackMove_leftTopCorner() {
        Game game = black("ke7", "ba8", "Ke2");
        expectPlyNumber(game, "ba8", 7);
        expectMoveRange(game, "ba8", "b7", "c6", "d5", "e4", "f3", "g2", "h1");
    }

    @Test
    void blackMove_rightTopCorner() {
        Game game = black("ke7", "bh8", "Ke2");
        expectPlyNumber(game, "bh8", 7);
        expectMoveRange(game, "bh8", "g7", "f6", "e5", "d4", "c3", "b2", "a1");
    }

    @Test
    void blackMove_rightBottomCorner() {
        Game game = black("ke7", "bh1", "Ke2");
        expectPlyNumber(game, "qh1", 7);
        expectMoveRange(game, "bh1", "g2", "f3", "e4", "d5", "c6", "b7", "a8");
    }

    @Test
    void blackCaptures() {
        Game game = black("ka8", "bd4", "Pf6", "Rf2", "Rb2", "Pb6", "Kh1");
        expectPlyNumber(game, "bd4", 8);
        expectMove(game, "bd4", "e5");
        expectMoveAndCaptures(game, "bd4", "Pf6");
        expectMove(game, "bd4", "e3");
        expectMoveAndCaptures(game, "bd4", "Rf2");
        expectMove(game, "bd4", "c3");
        expectMoveAndCaptures(game, "bd4", "Rb2");
        expectMove(game, "bd4", "c5");
        expectMoveAndCaptures(game, "bd4", "Pb6");
    }

    @Test
    void blackCapturesDirect() {
        Game game = black("ka8", "bd4", "Pe5", "Re3", "Rc3", "Pc5", "Kh1");
        expectPlyNumber(game, "bd4", 4);
        expectMoveAndCaptures(game, "bd4", "Pe5");
        expectMoveAndCaptures(game, "bd4", "Re3");
        expectMoveAndCaptures(game, "bd4", "Rc3");
        expectMoveAndCaptures(game, "bd4", "Pc5");
    }

    @Test
    void blackSelfBlocking() {
        Game game = black("ka8", "bd4", "pf6", "rf2", "rb2", "pb6", "Kh1");
        expectPlyNumber(game, "bd4", 4);
        expectMove(game, "bd4", "e5");
        expectMove(game, "bd4", "e3");
        expectMove(game, "bd4", "c3");
        expectMove(game, "bd4", "c5");
    }

    @Test
    void blackSelfBlockingDirect() {
        Game game = black("ka8", "bd4", "pe5", "re3", "rc3", "pc5", "Kh1");
        expectPlyNumber(game, "bd4", 0);
    }
}
