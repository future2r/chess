package name.ulbricht.chess.game;

public enum KnightJump implements Direction {

    UP_LEFT(-1, 2),
    UP_RIGHT(1, 2),
    LEFT_UP(-2, 1),
    LEFT_DOWN(-2, -1),
    RIGHT_UP(2, 1),
    RIGHT_DOWN(2, -1),
    DOWN_LEFT(-1, -2),
    DOWN_RIGHT(1, -2);

    private final int column;
    private final int row;

    KnightJump(int column, int row) {
        this.column = column;
        this.row = row;
    }

    @Override
    public int column() {
        return this.column;
    }

    @Override
    public int row() {
        return this.row;
    }
}
