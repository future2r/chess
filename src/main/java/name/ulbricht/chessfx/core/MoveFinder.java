package name.ulbricht.chessfx.core;

import java.util.List;

interface MoveFinder {

    List<Move> findMoves(Board board, Board.Square square);

    static MoveFinder of(Piece.Type pieceType) {
        switch (pieceType) {
            case PAWN:
                return PawnMoveFinder.getInstance();
            case ROOK:
                return RookMoveFinder.getInstance();
            case KNIGHT:
                return KnightMoveFinder.getInstance();
            case BISHOP:
                return BishopMoveFinder.getInstance();
            case QUEEN:
                return QueenMoveFinder.getInstance();
            case KING:
                return KingMoveFinder.getInstance();
            default:
                throw new IllegalArgumentException("Illegal piece type " + pieceType);
        }
    }
}
