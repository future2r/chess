package name.ulbricht.chess.fx.design;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class Messages {

    private static final String BUNDLE_NAME = "name/ulbricht/chess/fx/design/messages";

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
