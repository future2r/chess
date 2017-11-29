package name.ulbricht.chess.pgn;

import name.ulbricht.chess.game.SAN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PGNGame {

    private static final class TagGameInfo implements PGNGameInfo {

        private final PGNGame game;

        TagGameInfo(PGNGame game) {
            this.game = game;
        }

        @Override
        public String getEvent() {
            return this.game.getTag(PGN.EVENT_TAG);
        }

        @Override
        public void setEvent(String event) {
            this.game.setTag(PGN.EVENT_TAG, event);
        }

        @Override
        public String getSite() {
            return this.game.getTag(PGN.SITE_TAG);
        }

        @Override
        public void setSite(String site) {
            this.game.setTag(PGN.SITE_TAG, site);
        }

        @Override
        public String getDate() {
            return this.game.getTag(PGN.DATE_TAG);
        }

        @Override
        public void setDate(String date) {
            this.game.setTag(PGN.DATE_TAG, date);
        }

        @Override
        public String getRound() {
            return this.game.getTag(PGN.ROUND_TAG);
        }

        @Override
        public void setRound(String round) {
            this.game.setTag(PGN.ROUND_TAG, round);
        }

        @Override
        public String getWhite() {
            return this.game.getTag(PGN.WHITE_TAG);
        }

        @Override
        public void setWhite(String white) {
            this.game.setTag(PGN.WHITE_TAG, white);
        }

        @Override
        public String getBlack() {
            return this.game.getTag(PGN.BLACK_TAG);
        }

        @Override
        public void setBlack(String black) {
            this.game.setTag(PGN.BLACK_TAG, black);
        }

        @Override
        public PGNResult getResult() {
            return PGNUtils.toResult(this.game.getTag(PGN.RESULT_TAG));
        }

        @Override
        public void setResult(PGNResult result) {
            this.game.setTag(PGN.RESULT_TAG, result.getText());
        }
    }

    private final Map<String, String> tags = new HashMap<>();
    private PGNGameInfo info;
    private final List<SAN.Ply> plies = new ArrayList<>();

    public String getTag(String key) {
        return this.tags.get(key);
    }

    public void setTag(String key, String value) {
        if (value != null) this.tags.put(key, value);
        else this.tags.remove(key);
    }

    public PGNGameInfo getInfo() {
        if (this.info == null) {
            this.info = new TagGameInfo(this);
        }
        return this.info;
    }

    public List<SAN.Ply> getPlies() {
        return this.plies;
    }
}
