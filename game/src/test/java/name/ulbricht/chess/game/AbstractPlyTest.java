package name.ulbricht.chess.game;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractPlyTest {

    static Game white(String... positions) {
        Setup setup = Setup.empty();
        setup.setActivePlayer(Player.WHITE);
        return game(setup, positions);
    }

    static Game black(String... positions) {
        Setup setup = Setup.empty();
        setup.setActivePlayer(Player.BLACK);
        return game(setup, positions);
    }

    private static Game game(Setup setup, String... positions) {
        for (String pos : positions) {
            Piece piece = FEN.piece(pos.charAt(0));
            Coordinate coordinate = Coordinate.valueOf(pos.substring(1));
            setup.setPiece(coordinate, piece);
        }
        return new Game(setup);
    }

    static void expectPlyNumber(Game game, String sourcePiece, int plyNumber) {
        List<Ply> plies = game.getActivePlayerPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertEquals(plyNumber, plies.size());
    }

    static void expectMove(Game game, String sourcePiece, String target) {
        Ply ply = Ply.move(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)),
                Coordinate.valueOf(target));

        List<Ply> plies = game.getActivePlayerPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectMoveRange(Game game, String sourcePiece, String... targets) {
        for (String target : targets) {
            Ply ply = Ply.move(FEN.piece(sourcePiece.charAt(0)),
                    Coordinate.valueOf(sourcePiece.substring(1)),
                    Coordinate.valueOf(target));

            List<Ply> plies = game.getActivePlayerPlies(Coordinate.valueOf(sourcePiece.substring(1)));
            assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
        }
    }

    static void expectMoveAndCaptures(Game game, String sourcePiece, String capturedPiece) {
        Ply ply = Ply.moveAndCaptures(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)),
                Coordinate.valueOf(capturedPiece.substring(1)),
                FEN.piece(capturedPiece.charAt(0)));

        List<Ply> plies = game.getActivePlayerPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectPawnDoubleAdvance(Game game, String sourcePiece) {
        Ply ply = Ply.pawnDoubleAdvance(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)));

        List<Ply> plies = game.getActivePlayerPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }
}
