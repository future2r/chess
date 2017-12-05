package name.ulbricht.chess.pgn;

import name.ulbricht.chess.game.SAN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PGNGame {

    private final Map<String, String> tags = new HashMap<>();
    private final List<SAN.Ply> plies = new ArrayList<>();

    public String getTag(String key) {
        return this.tags.get(key);
    }

    public void setTag(String key, String value) {
        if (value != null) this.tags.put(key, value);
        else this.tags.remove(key);
    }

    public String getEvent() {
        return getTag(PGN.EVENT_TAG);
    }

    public void setEvent(String event) {
        setTag(PGN.EVENT_TAG, event);
    }

    public String getSite() {
        return getTag(PGN.SITE_TAG);
    }

    public void setSite(String site) {
        setTag(PGN.SITE_TAG, site);
    }

    public String getDate() {
        return getTag(PGN.DATE_TAG);
    }

    public void setDate(String date) {
        setTag(PGN.DATE_TAG, date);
    }

    public String getRound() {
        return getTag(PGN.ROUND_TAG);
    }

    public void setRound(String round) {
        setTag(PGN.ROUND_TAG, round);
    }

    public String getWhite() {
        return getTag(PGN.WHITE_TAG);
    }

    public void setWhite(String white) {
        setTag(PGN.WHITE_TAG, white);
    }

    public String getBlack() {
        return getTag(PGN.BLACK_TAG);
    }

    public void setBlack(String black) {
        setTag(PGN.BLACK_TAG, black);
    }

    public PGNResult getResult() {
        return PGNUtils.toResult(getTag(PGN.RESULT_TAG));
    }

    public void setResult(PGNResult result) {
        setTag(PGN.RESULT_TAG, result.getText());
    }

    public List<SAN.Ply> getPlies() {
        return this.plies;
    }
}
