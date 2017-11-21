package name.ulbricht.chessfx.io;

final class PGNGameInfoImpl implements PGNGameInfo {

    private String event;
    private String site;
    private String date;
    private String round;
    private String white;
    private String black;
    private PGNResult result;

    @Override
    public String getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String getSite() {
        return this.site;
    }

    @Override
    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getRound() {
        return this.round;
    }

    @Override
    public void setRound(String round) {
        this.round = round;
    }

    @Override
    public String getWhite() {
        return this.white;
    }

    @Override
    public void setWhite(String white) {
        this.white = white;
    }

    @Override
    public String getBlack() {
        return this.black;
    }

    @Override
    public void setBlack(String black) {
        this.black = black;
    }

    @Override
    public PGNResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(PGNResult result) {
        this.result = result;
    }
}
