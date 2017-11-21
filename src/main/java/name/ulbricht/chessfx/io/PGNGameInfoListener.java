package name.ulbricht.chessfx.io;

import name.ulbricht.chessfx.io.antlr.PGNBaseListener;
import name.ulbricht.chessfx.io.antlr.PGNParser;

import java.util.ArrayList;
import java.util.List;

final class PGNGameInfoListener extends PGNBaseListener implements PGNDatabaseListener<List<PGNGameInfo>> {

    private final List<PGNGameInfo> gameInfos = new ArrayList<>();

    private PGNGameInfo currentGameInfo;
    private String currentTagName;
    private String currentTagValue;

    @Override
    public List<PGNGameInfo> getData() {
        return this.gameInfos;
    }

    @Override
    public void enterPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.currentGameInfo = new PGNGameInfoImpl();
    }

    @Override
    public void exitPgn_game(PGNParser.Pgn_gameContext ctx) {
        this.gameInfos.add(this.currentGameInfo);
    }

    @Override
    public void enterTag_name(PGNParser.Tag_nameContext ctx) {
        this.currentTagName = ctx.SYMBOL().getText();
    }

    @Override
    public void enterTag_value(PGNParser.Tag_valueContext ctx) {
        this.currentTagValue = PGNUtils.dequote(ctx.STRING().getText());
    }

    @Override
    public void exitTag_pair(PGNParser.Tag_pairContext ctx) {
        switch (this.currentTagName) {
            case PGN.EVENT_TAG:
                this.currentGameInfo.setEvent(this.currentTagValue);
                break;
            case PGN.SITE_TAG:
                this.currentGameInfo.setSite(this.currentTagValue);
                break;
            case PGN.DATE_TAG:
                this.currentGameInfo.setDate(this.currentTagValue);
                break;
            case PGN.ROUND_TAG:
                this.currentGameInfo.setRound(this.currentTagValue);
                break;
            case PGN.WHITE_TAG:
                this.currentGameInfo.setWhite(this.currentTagValue);
                break;
            case PGN.BLACK_TAG:
                this.currentGameInfo.setBlack(this.currentTagValue);
                break;
            case PGN.RESULT_TAG:
                this.currentGameInfo.setResult(PGNUtils.toResult(this.currentTagValue));
        }
    }
}
