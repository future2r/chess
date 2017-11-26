package name.ulbricht.chess.game;

public interface GameSetup {

    Piece getPiece(Coordinate coordinate);

    Player getActivePlayer();

    boolean isWhiteKingSideCastlingAvailable();

    boolean isWhiteQueenSideCastlingAvailable();

    boolean isBlackKingSideCastlingAvailable();

    boolean isBlackQueenSideCastlingAvailable();

    Coordinate getEnPassantTarget();

    int getHalfMoveClock();

    int getFullMoveNumber();
}
