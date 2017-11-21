package name.ulbricht.chessfx.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PGNGame {

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
