package name.ulbricht.chessfx.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class Messages {

    public static String getString(String key) {
        try {
            return ResourceBundle.getBundle("name/ulbricht/chessfx/core/messages").getString(key);
        } catch (MissingResourceException ex) {
            return "!" + key + "!";
        }
    }

    private Messages() {
        // hidden
    }
}
