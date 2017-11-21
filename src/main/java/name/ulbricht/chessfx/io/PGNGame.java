package name.ulbricht.chessfx.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PGNGame {

    public static final String EVENT_TAG = "Event";
    public static final String SITE_TAG = "Site";
    public static final String DATE_TAG = "Date";
    public static final String ROUND_TAG = "Round";
    public static final String WHITE_TAG = "White";
    public static final String BLACK_TAG = "Black";
    public static final String RESULT_TAG = "Result";

    private final Map<String, String> tags = new HashMap<>();
    private final List<SANMove> moves = new ArrayList<>();

    public String getTag(String key) {
        return this.tags.get(key);
    }

    public void setTag(String key, String value) {
        if (value != null) this.tags.put(key, value);
        else this.tags.remove(key);
    }

    public List<SANMove> getMoves() {
        return this.moves;
    }
}
