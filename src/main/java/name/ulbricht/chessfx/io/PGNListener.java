package name.ulbricht.chessfx.io;

import name.ulbricht.chessfx.io.antlr.PGNBaseListener;
import name.ulbricht.chessfx.io.antlr.PGNParser;

final class PGNListener extends PGNBaseListener {

    private PGNDatabase database;
    private PGNGame game;
    private String tagName;
    private String tagValue;

    PGNDatabase getDatabase() {
        return this.database;
    }

    @Override
    public void enterPgn_database(PGNParser.Pgn_databaseContext ctx) {
        this.database = new PGNDatabase();
    }

    @Override
    public void enterPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.game = new PGNGame();
    }

    @Override
    public void exitPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.database.getGames().add(this.game);
        this.game = null;
    }

    @Override
    public void enterTag_name(PGNParser.Tag_nameContext ctx) {
        this.tagName = ctx.SYMBOL().getText();
    }

    @Override
    public void enterTag_value(PGNParser.Tag_valueContext ctx) {
        this.tagValue = dequote(ctx.STRING().getText());
    }

    @Override
    public void exitTag_pair(PGNParser.Tag_pairContext ctx) {
        this.game.setTag(this.tagName, this.tagValue);
    }

    @Override
    public void enterSan_move(PGNParser.San_moveContext ctx) {
        SANMove move = SANMove.of(ctx.SYMBOL().getText());
        this.game.getMoves().add(move);
    }

    private static String dequote(String s) {
        return s.replaceAll("^\"", "").replaceAll("\"$", "");
    }
}
