package name.ulbricht.chessfx.gui.design;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class Messages {

    static String getString(String key) {
        try {
            return ResourceBundle.getBundle("name/ulbricht/chessfx/gui/design/messages").getString(key);
        } catch (MissingResourceException ex) {
            return "!" + key + "!";
        }
    }

    private Messages() {
        // hidden
    }
}
