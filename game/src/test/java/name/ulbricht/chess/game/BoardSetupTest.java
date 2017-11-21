package name.ulbricht.chess.game;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class BoardSetupTest {

    private static Stream<Arguments> createArguments() {

        Collection<Arguments> arguments = new ArrayList<>();

        arguments.add(Arguments.of(Coordinate.valueOf("a1"), new Piece(PieceType.ROOK, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("b1"), new Piece(PieceType.KNIGHT, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("c1"), new Piece(PieceType.BISHOP, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("d1"), new Piece(PieceType.QUEEN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("e1"), new Piece(PieceType.KING, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("f1"), new Piece(PieceType.BISHOP, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("g1"), new Piece(PieceType.KNIGHT, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("h1"), new Piece(PieceType.ROOK, Player.WHITE)));

        arguments.add(Arguments.of(Coordinate.valueOf("a2"), new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("b2"), new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("c2"), new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("d2"), new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("e2"), new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("f2"), new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("g2"), new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.valueOf("h2"), new Piece(PieceType.PAWN, Player.WHITE)));

        for (int i = Coordinate.valueOf("a3").getIndex(); i <= Coordinate.valueOf("h6").getIndex(); i++) {
            arguments.add(Arguments.of(Coordinate.valueOf(i), null));
        }

        arguments.add(Arguments.of(Coordinate.valueOf("a7"), new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("b7"), new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("c7"), new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("d7"), new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("e7"), new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("f7"), new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("g7"), new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("h7"), new Piece(PieceType.PAWN, Player.BLACK)));

        arguments.add(Arguments.of(Coordinate.valueOf("a8"), new Piece(PieceType.ROOK, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("b8"), new Piece(PieceType.KNIGHT, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("c8"), new Piece(PieceType.BISHOP, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("d8"), new Piece(PieceType.QUEEN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("e8"), new Piece(PieceType.KING, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("f8"), new Piece(PieceType.BISHOP, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("g8"), new Piece(PieceType.KNIGHT, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.valueOf("h8"), new Piece(PieceType.ROOK, Player.BLACK)));

        return arguments.stream();
    }

    private static final Board board;

    static {
        board = new Board();
        board.setup();
    }

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @MethodSource("createArguments")
    public void testSetup(Coordinate coordinate, Piece piece) {
        if (piece == null) assertFalse(board.getPiece(coordinate).isPresent());
        else assertEquals(piece, board.getPiece(coordinate).get(), "Unexpected piece");
    }
}
