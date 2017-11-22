package name.ulbricht.chess.pgn;

import name.ulbricht.chess.pgn.antlr.PGNBaseListener;
import name.ulbricht.chess.pgn.antlr.PGNParser;

import java.util.ArrayList;
import java.util.List;

final class PGNGameListener extends PGNBaseListener implements PGNDatabaseListener<List<PGNGame>> {

    private final List<PGNGame> games = new ArrayList<>();
    private int fromIndex;
    private int toIndex;

    private int currentIndex = -1;
    private PGNGame currentGame;
    private String currentTagName;
    private String currentTagValue;

    PGNGameListener(int fromIndex, int toIndex) {
        if (fromIndex < 0) throw new IllegalArgumentException("fromIndex cannot be < 0");
        if (toIndex < 0) throw new IllegalArgumentException("toIndex cannot be < 0");
        if (fromIndex > toIndex) throw new IllegalArgumentException("toIndex cannot be > toIndex");

        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    @Override
    public List<PGNGame> getData() {
        return this.games;
    }

    @Override
    public void enterPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.currentIndex++;
        if (this.currentIndex >= this.fromIndex && this.currentIndex <= this.toIndex) {
            this.currentGame = new PGNGame();
        }
    }

    @Override
    public void exitPgn_game(PGNParser.Pgn_gameContext ctx) {
        if (this.currentGame != null) {
            this.games.add(this.currentGame);
            this.currentGame = null;
        }
    }

    @Override
    public void enterTag_name(PGNParser.Tag_nameContext ctx) {
        if (this.currentGame != null) {
            this.currentTagName = ctx.SYMBOL().getText();
        }
    }

    @Override
    public void enterTag_value(PGNParser.Tag_valueContext ctx) {
        if (this.currentGame != null) {
            this.currentTagValue = PGNUtils.dequote(ctx.STRING().getText());
        }
    }

    @Override
    public void exitTag_pair(PGNParser.Tag_pairContext ctx) {
        if (this.currentGame != null) {
            this.currentGame.setTag(this.currentTagName, this.currentTagValue);
        }
    }

    @Override
    public void enterSan_move(PGNParser.San_moveContext ctx) {
        if (this.currentGame != null) {
            SANMove move = SANMove.of(ctx.SYMBOL().getText());
            this.currentGame.getMoves().add(move);
        }
    }
}
