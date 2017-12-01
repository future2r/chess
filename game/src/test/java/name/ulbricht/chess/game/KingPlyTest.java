package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

final class KingPlyTest extends AbstractPlyTest {

    @Test
    void whiteMove() {
        Game game = white("ke7", "Ke2");
        expectPlyNumber(game, "Ke2", 8);
        expectMove(game, "Ke2", "e3");
        expectMove(game, "Ke2", "f3");
        expectMove(game, "Ke2", "f2");
        expectMove(game, "Ke2", "f1");
        expectMove(game, "Ke2", "e1");
        expectMove(game, "Ke2", "d1");
        expectMove(game, "Ke2", "d2");
        expectMove(game, "Ke2", "d3");
    }

    @Test
    void whiteMove_leftBottomCorner() {
        Game game = white("ke7", "Ka1");
        expectPlyNumber(game, "Qa1", 3);
        expectMove(game, "Ka1", "a2");
        expectMove(game, "Ka1", "b2");
        expectMove(game, "Ka1", "b1");
    }

    @Test
    void whiteMove_leftTopCorner() {
        Game game = white("ke7", "Ka8");
        expectPlyNumber(game, "Ka8", 3);
        expectMove(game, "Ka8", "b8");
        expectMove(game, "Ka8", "b7");
        expectMove(game, "Ka8", "a7");
    }

    @Test
    void whiteMove_rightTopCorner() {
        Game game = white("ke7", "Kh8");
        expectPlyNumber(game, "Kh8", 3);
        expectMove(game, "Kh8", "h7");
        expectMove(game, "Kh8", "g7");
        expectMove(game, "Kh8", "g8");
    }

    @Test
    void whiteMove_rightBottomCorner() {
        Game game = white("ke7", "Kh1");
        expectPlyNumber(game, "Kh1", 3);
        expectMove(game, "Kh1", "g1");
        expectMove(game, "Kh1", "g2");
        expectMove(game, "Kh1", "h2");
    }

    @Test
    void whiteSelfBlocking() {
        Game game = white();
        expectPlyNumber(game, "ke1", 0);
    }

    @Test
    void blackMove() {
        Game game = black("ke7", "Ke2");
        expectPlyNumber(game, "ke7", 8);
        expectMove(game, "ke7", "e8");
        expectMove(game, "ke7", "f8");
        expectMove(game, "ke7", "f7");
        expectMove(game, "ke7", "f6");
        expectMove(game, "ke7", "e6");
        expectMove(game, "ke7", "d6");
        expectMove(game, "ke7", "d7");
        expectMove(game, "ke7", "d8");
    }

    @Test
    void blackMove_leftBottomCorner() {
        Game game = black("ka1", "Ke2");
        expectPlyNumber(game, "ka1", 3);
        expectMove(game, "ka1", "a2");
        expectMove(game, "ka1", "b2");
        expectMove(game, "ka1", "b1");
    }

    @Test
    void blackMove_leftTopCorner() {
        Game game = black("ka8", "Ke2");
        expectPlyNumber(game, "ka8", 3);
        expectMove(game, "ka8", "b8");
        expectMove(game, "ka8", "b7");
        expectMove(game, "ka8", "a7");
    }

    @Test
    void blackMove_rightTopCorner() {
        Game game = black("kh8", "Ke2");
        expectPlyNumber(game, "kh8", 3);
        expectMove(game, "kh8", "h7");
        expectMove(game, "kh8", "g7");
        expectMove(game, "kh8", "g8");
    }

    @Test
    void blackMove_rightBottomCorner() {
        Game game = black("kh1", "Ke2");
        expectPlyNumber(game, "kh1", 3);
        expectMove(game, "kh1", "g1");
        expectMove(game, "kh1", "g2");
        expectMove(game, "kh1", "h2");
    }

    @Test
    void blackSelfBlocking() {
        Game game = black();
        expectPlyNumber(game, "ke8", 0);
    }
}
