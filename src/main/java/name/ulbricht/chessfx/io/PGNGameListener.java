package name.ulbricht.chessfx.io;

import name.ulbricht.chessfx.io.antlr.PGNBaseListener;
import name.ulbricht.chessfx.io.antlr.PGNParser;

final class PGNGameListener extends PGNBaseListener implements PGNDatabaseListener<PGNGame> {

    private PGNGame game;
    private int gameIndex;

    private int currentGameIndex = -1;
    private String tagName;
    private String tagValue;

    PGNGameListener(int gameIndex) {
        this.gameIndex = gameIndex;
    }

    @Override
    public PGNGame getData() {
        return this.game;
    }

    @Override
    public void enterPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.currentGameIndex++;
        if (this.currentGameIndex == this.gameIndex) {
            this.game = new PGNGame();
        }
    }

    @Override
    public void enterTag_name(PGNParser.Tag_nameContext ctx) {
        if (this.currentGameIndex == this.gameIndex) {
            this.tagName = ctx.SYMBOL().getText();
        }
    }

    @Override
    public void enterTag_value(PGNParser.Tag_valueContext ctx) {
        if (this.currentGameIndex == this.gameIndex) {
            this.tagValue = PGNUtils.dequote(ctx.STRING().getText());
        }
    }

    @Override
    public void exitTag_pair(PGNParser.Tag_pairContext ctx) {
        if (this.currentGameIndex == this.gameIndex) {
            this.game.setTag(this.tagName, this.tagValue);
        }
    }

    @Override
    public void enterSan_move(PGNParser.San_moveContext ctx) {
        if (this.currentGameIndex == this.gameIndex) {
            SANMove move = SANMove.of(ctx.SYMBOL().getText());
            this.game.getMoves().add(move);
        }
    }
}
