package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

final class KnightPlyTest extends AbstractPlyTest {

    @Test
    void whiteMove() {
        Game game = white("ke8", "Nd4", "Ke1");
        expectPlyNumber(game, "Nd4", 8);
        expectMove(game, "Nd4", "c6");
        expectMove(game, "Nd4", "e6");
        expectMove(game, "Nd4", "f5");
        expectMove(game, "Nd4", "f3");
        expectMove(game, "Nd4", "e2");
        expectMove(game, "Nd4", "c2");
        expectMove(game, "Nd4", "b3");
        expectMove(game, "Nd4", "b5");
    }

    @Test
    void whiteMove_leftBottomCorner() {
        Game game = white("ke8", "Na1", "Ke1");
        expectPlyNumber(game, "Na1", 2);
        expectMove(game, "Na1", "b3");
        expectMove(game, "Na1", "c2");
    }

    @Test
    void whiteMove_leftTopCorner() {
        Game game = white("ke8", "Na8", "Ke1");
        expectPlyNumber(game, "Na8", 2);
        expectMove(game, "Na8", "c7");
        expectMove(game, "Na8", "b6");
    }

    @Test
    void whiteMove_rightTopCorner() {
        Game game = white("ke8", "Nh8", "Ke1");
        expectPlyNumber(game, "Nh8", 2);
        expectMove(game, "Nh8", "f7");
        expectMove(game, "Nh8", "g6");
    }

    @Test
    void whiteMove_rightBottomCorner() {
        Game game = white("ke8", "Nh1", "Ke1");
        expectPlyNumber(game, "Nh1", 2);
        expectMove(game, "Nh1", "g3");
        expectMove(game, "Nh1", "f2");
    }

    @Test
    void whiteCaptures() {
        Game game = white("ke8", "pc6", "pe6", "pf5", "rf3", "ne2", "nc2", "rb3", "pb5", "Nd4", "Kd1");
        expectPlyNumber(game, "Nd4", 8);
        expectMoveAndCaptures(game, "Nd4", "pc6");
        expectMoveAndCaptures(game, "Nd4", "pe6");
        expectMoveAndCaptures(game, "Nd4", "pf5");
        expectMoveAndCaptures(game, "Nd4", "rf3");
        expectMoveAndCaptures(game, "Nd4", "ne2");
        expectMoveAndCaptures(game, "Nd4", "nc2");
        expectMoveAndCaptures(game, "Nd4", "rb3");
        expectMoveAndCaptures(game, "Nd4", "pb5");
    }

    @Test
    void whiteSelfBlocking() {
        Game game = white("ke8", "Pc6", "Pe6", "Pf5", "Rf3", "Ne2", "Nc2", "Rb3", "Pb5", "Nd4", "Ke1");
        expectPlyNumber(game, "Nd4", 0);
    }

    @Test
    void blackMove() {
        Game game = black("ke8", "ne5", "Ke1");
        expectPlyNumber(game, "ne5", 8);
        expectMove(game, "ne5", "d7");
        expectMove(game, "ne5", "f7");
        expectMove(game, "ne5", "g6");
        expectMove(game, "ne5", "g4");
        expectMove(game, "ne5", "f3");
        expectMove(game, "ne5", "d3");
        expectMove(game, "ne5", "c4");
        expectMove(game, "ne5", "c6");
    }

    @Test
    void blackMove_leftBottomCorner() {
        Game game = black("ke8", "na1", "Ke1");
        expectPlyNumber(game, "na1", 2);
        expectMove(game, "na1", "b3");
        expectMove(game, "na1", "c2");
    }

    @Test
    void blackMove_leftTopCorner() {
        Game game = black("ke8", "na8", "Ke1");
        expectPlyNumber(game, "na8", 2);
        expectMove(game, "na8", "c7");
        expectMove(game, "na8", "b6");
    }

    @Test
    void blackMove_rightTopCorner() {
        Game game = black("ke8", "nh8", "Ke1");
        expectPlyNumber(game, "nh8", 2);
        expectMove(game, "nh8", "f7");
        expectMove(game, "nh8", "g6");
    }

    @Test
    void blackMove_rightBottomCorner() {
        Game game = black("ke8", "nh1", "Ke1");
        expectPlyNumber(game, "nh1", 2);
        expectMove(game, "nh1", "g3");
        expectMove(game, "nh1", "f2");
    }

    @Test
    void blackCaptures() {
        Game game = black("ke8", "ne5", "Nd7", "Nf7", "Rg6", "Pg4", "Pf3", "Pd3", "Pc4", "Rc6", "Ke1");
        expectPlyNumber(game, "ne5", 8);
        expectMoveAndCaptures(game, "ne5", "Nd7");
        expectMoveAndCaptures(game, "ne5", "Nf7");
        expectMoveAndCaptures(game, "ne5", "Rg6");
        expectMoveAndCaptures(game, "ne5", "Pg4");
        expectMoveAndCaptures(game, "ne5", "Pf3");
        expectMoveAndCaptures(game, "ne5", "Pd3");
        expectMoveAndCaptures(game, "ne5", "Pc4");
        expectMoveAndCaptures(game, "ne5", "Rc6");
    }

    @Test
    void blackSelfBlocking() {
        Game game = black("ke8", "ne5", "nd7", "nf7", "rg6", "pg4", "pf3", "pd3", "pc4", "rc6", "Ke1");
        expectPlyNumber(game, "ne5", 0);
    }

}
