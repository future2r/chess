package name.ulbricht.chessfx.core;

public final class Game {

    private final Board board;
    private Player currentPlayer;

    public Game() {
        this.board = new Board();
        start();
    }

    public Board getBoard() {
        return this.board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void start() {
        this.board.setup();
        this.currentPlayer = Player.WHITE;
    }

}
