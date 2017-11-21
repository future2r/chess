package name.ulbricht.chess.fx;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class Messages {

    static final String BUNDLE_NAME = "name/ulbricht/chess/fx/messages";

    static String getString(String key) {
        try {
            return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
        } catch (MissingResourceException ex) {
            return "!" + key + "!";
        }
    }

    private Messages() {
        // hidden
    }
}
