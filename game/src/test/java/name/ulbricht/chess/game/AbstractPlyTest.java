package name.ulbricht.chess.game;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractPlyTest {

    static Game whiteInitial() {
        Board board = Board.initial();
        board.setActivePlayer(Player.WHITE);
        return new Game(board);
    }

    static Game white(String... positions) {
        Board board = new Board();
        board.setActivePlayer(Player.WHITE);
        return game(board, positions);
    }

    static Game whiteCastlingAvailable(String... positions) {
        Board board = new Board();
        board.setActivePlayer(Player.WHITE);
        board.setWhiteKingSideCastlingAvailable(true);
        board.setWhiteQueenSideCastlingAvailable(true);
        return game(board, positions);
    }

    static Game black(String... positions) {
        Board board = new Board();
        board.setActivePlayer(Player.BLACK);
        return game(board, positions);
    }

    static Game blackStandard() {
        Board board = Board.initial();
        board.setActivePlayer(Player.BLACK);
        return new Game(board);
    }

    static Game blackCastlingAvailable(String... positions) {
        Board board = new Board();
        board.setActivePlayer(Player.BLACK);
        board.setBlackKingSideCastlingAvailable(true);
        board.setBlackQueenSideCastlingAvailable(true);
        return game(board, positions);
    }

    private static Game game(Board board, String... positions) {
        for (String pos : positions) {
            Piece piece = FEN.piece(pos.charAt(0));
            Coordinate coordinate = Coordinate.valueOf(pos.substring(1));
            board.setPiece(coordinate, piece);
        }
        return new Game(board);
    }

    static void expectPlyNumber(Game game, String sourcePiece, int plyNumber) {
        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertEquals(plyNumber, plies.size());
    }

    static void expectMove(Game game, String sourcePiece, String target) {
        Ply ply = Ply.move(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)),
                Coordinate.valueOf(target));

        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectMoveRange(Game game, String sourcePiece, String... targets) {
        for (String target : targets) {
            Ply ply = Ply.move(FEN.piece(sourcePiece.charAt(0)),
                    Coordinate.valueOf(sourcePiece.substring(1)),
                    Coordinate.valueOf(target));

            List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
            assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
        }
    }

    static void expectMoveAndCaptures(Game game, String sourcePiece, String capturedPiece) {
        Ply ply = Ply.moveAndCaptures(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)),
                Coordinate.valueOf(capturedPiece.substring(1)),
                FEN.piece(capturedPiece.charAt(0)));

        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectPawnDoubleAdvance(Game game, String sourcePiece) {
        Ply ply = Ply.pawnDoubleAdvance(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)));

        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectPawnEnPassant(Game game, String sourcePiece, String target) {
        Ply ply = Ply.pawnEnPassant(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)),
                Coordinate.valueOf(target));

        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectPawnPromotion(Game game, String sourcePiece, String target) {
        Ply ply = Ply.pawnPromotion(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)),
                Coordinate.valueOf(target));

        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectPawnPromotionAndCaptures(Game game, String sourcePiece, String capturedPiece) {
        Ply ply = Ply.pawnPromotionAndCaptures(FEN.piece(sourcePiece.charAt(0)),
                Coordinate.valueOf(sourcePiece.substring(1)),
                Coordinate.valueOf(capturedPiece.substring(1)),
                FEN.piece(capturedPiece.charAt(0)));

        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectKingSideCastling(Game game, String sourcePiece) {
        Ply ply = Ply.kingSideCastling(FEN.piece(sourcePiece.charAt(0)));

        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }

    static void expectQueenSideCastling(Game game, String sourcePiece) {
        Ply ply = Ply.queenSideCastling(FEN.piece(sourcePiece.charAt(0)));

        List<Ply> plies = game.getValidPlies(Coordinate.valueOf(sourcePiece.substring(1)));
        assertTrue(plies.contains(ply), "Expected ply not found: " + ply);
    }
}
