package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class GameTest {

    private static Stream<Arguments> createSetupArguments() {

        Collection<Arguments> arguments = new ArrayList<>();

        arguments.add(Arguments.of(Coordinate.a1, new Piece(PieceType.ROOK, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.b1, new Piece(PieceType.KNIGHT, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.c1, new Piece(PieceType.BISHOP, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.d1, new Piece(PieceType.QUEEN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.e1, new Piece(PieceType.KING, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.f1, new Piece(PieceType.BISHOP, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.g1, new Piece(PieceType.KNIGHT, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.h1, new Piece(PieceType.ROOK, Player.WHITE)));

        arguments.add(Arguments.of(Coordinate.a2, new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.b2, new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.c2, new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.d2, new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.e2, new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.f2, new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.g2, new Piece(PieceType.PAWN, Player.WHITE)));
        arguments.add(Arguments.of(Coordinate.h2, new Piece(PieceType.PAWN, Player.WHITE)));

        for (int i = Coordinate.a3.ordinal(); i <= Coordinate.h6.ordinal(); i++) {
            arguments.add(Arguments.of(Coordinate.valueOf(i), null));
        }

        arguments.add(Arguments.of(Coordinate.a7, new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.b7, new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.c7, new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.d7, new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.e7, new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.f7, new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.g7, new Piece(PieceType.PAWN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.h7, new Piece(PieceType.PAWN, Player.BLACK)));

        arguments.add(Arguments.of(Coordinate.a8, new Piece(PieceType.ROOK, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.b8, new Piece(PieceType.KNIGHT, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.c8, new Piece(PieceType.BISHOP, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.d8, new Piece(PieceType.QUEEN, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.e8, new Piece(PieceType.KING, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.f8, new Piece(PieceType.BISHOP, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.g8, new Piece(PieceType.KNIGHT, Player.BLACK)));
        arguments.add(Arguments.of(Coordinate.h8, new Piece(PieceType.ROOK, Player.BLACK)));

        return arguments.stream();
    }

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @MethodSource("createSetupArguments")
    void testSetup(Coordinate coordinate, Piece piece) {
        Game game = new Game();

        if (piece == null) assertNull(game.getPiece(coordinate));
        else assertEquals(piece, game.getPiece(coordinate), "Unexpected piece");
    }
}
