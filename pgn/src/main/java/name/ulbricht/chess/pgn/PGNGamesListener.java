package name.ulbricht.chess.pgn;

import name.ulbricht.chess.game.SAN;
import name.ulbricht.chess.pgn.antlr.PGNBaseListener;
import name.ulbricht.chess.pgn.antlr.PGNParser;

import java.util.ArrayList;
import java.util.List;

final class PGNGamesListener extends PGNBaseListener {

    private final List<PGNGame> games = new ArrayList<>();

    private PGNGame currentGame;
    private String currentTagName;
    private String currentTagValue;

    public List<PGNGame> getGames() {
        return this.games;
    }

    @Override
    public void enterPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.currentGame = new PGNGame();
    }

    @Override
    public void exitPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.games.add(this.currentGame);
        this.currentGame = null;
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
            SAN.Ply ply = SAN.ply(ctx.SYMBOL().getText());
            this.currentGame.getPlies().add(ply);
        }
    }
}
