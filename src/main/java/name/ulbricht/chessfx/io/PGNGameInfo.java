package name.ulbricht.chessfx.io;

public interface PGNGameInfo {

    String getEvent();

    void setEvent(String event);

    String getSite();

    void setSite(String site);

    String getDate();

    void setDate(String date);

    String getRound();

    void setRound(String round);

    String getWhite();

    void setWhite(String white);

    String getBlack();

    void setBlack(String black);

    PGNResult getResult();

    void setResult(PGNResult result);
}
