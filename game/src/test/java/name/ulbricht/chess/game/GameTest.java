package name.ulbricht.chess.game;

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
        Board board = Board.initial();
        for (Coordinate coordinate : Coordinate.values()) {
            arguments.add(Arguments.of(coordinate, board.getPiece(coordinate)));
        }
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
