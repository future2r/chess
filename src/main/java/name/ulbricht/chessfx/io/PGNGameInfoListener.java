package name.ulbricht.chessfx.io;

import name.ulbricht.chessfx.io.antlr.PGNBaseListener;
import name.ulbricht.chessfx.io.antlr.PGNParser;

import java.util.ArrayList;
import java.util.List;

final class PGNGameInfoListener extends PGNBaseListener implements PGNDatabaseListener<List<PGNGameInfo>> {

    private final List<PGNGameInfo> gameInfos = new ArrayList<>();

    private PGNGameInfo gameInfo;
    private String tagName;
    private String tagValue;

    @Override
    public List<PGNGameInfo> getData() {
        return this.gameInfos;
    }

    @Override
    public void enterPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.gameInfo = new PGNGameInfoImpl();
    }

    @Override
    public void exitPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.gameInfos.add(this.gameInfo);
    }

    @Override
    public void enterTag_name(PGNParser.Tag_nameContext ctx) {
        this.tagName = ctx.SYMBOL().getText();
    }

    @Override
    public void enterTag_value(PGNParser.Tag_valueContext ctx) {
        this.tagValue = PGNUtils.dequote(ctx.STRING().getText());
    }

    @Override
    public void exitTag_pair(PGNParser.Tag_pairContext ctx) {
        switch (this.tagName) {
            case PGN.EVENT_TAG:
                this.gameInfo.setEvent(this.tagValue);
                break;
            case PGN.SITE_TAG:
                this.gameInfo.setSite(this.tagValue);
                break;
            case PGN.DATE_TAG:
                this.gameInfo.setDate(this.tagValue);
                break;
            case PGN.ROUND_TAG:
                this.gameInfo.setRound(this.tagValue);
                break;
            case PGN.WHITE_TAG:
                this.gameInfo.setWhite(this.tagValue);
                break;
            case PGN.BLACK_TAG:
                this.gameInfo.setBlack(this.tagValue);
                break;
            case PGN.RESULT_TAG:
                this.gameInfo.setResult(PGNUtils.toResult(this.tagValue));
        }
    }
}
