package name.ulbricht.chess.game;

public enum Direction {
    UP(0, 1),
    UP_RIGHT(1, 1),
    RIGHT(1, 0),
    DOWN_RIGHT(1, -1),
    DOWN(0, -1),
    DOWN_LEFT(-1, -1),
    LEFT(-1, 0),
    UP_LEFT(-1, 1);

    static Direction forward(Player player) {
        return player == Player.WHITE ? UP : DOWN;
    }

    static Direction forwardLeft(Player player) {
        return player == Player.WHITE ? UP_LEFT : DOWN_RIGHT;
    }

    static Direction forwardRight(Player player) {
        return player == Player.WHITE ? UP_RIGHT : DOWN_LEFT;
    }

    final int columnOffset;
    final int rowOffset;

    Direction(int columnOffset, int rowOffset) {
        this.columnOffset = columnOffset;
        this.rowOffset = rowOffset;
    }
}
