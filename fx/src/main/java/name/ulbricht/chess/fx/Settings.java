package name.ulbricht.chess.fx;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

final class Settings {

    static String get(String key, String defaultValue) {
        Preferences prefs = Preferences.userNodeForPackage(Settings.class);
        return prefs.get(key, defaultValue);
    }

    static void put(String key, String value) {
        Preferences prefs = Preferences.userNodeForPackage(Settings.class);
        if (value != null) prefs.put(key, value);
        else prefs.remove(key);
    }

    static Path getPath(String key) {
        String pathString = get(key, null);
        if (pathString != null) {
            try {
                return Paths.get(pathString);
            } catch (InvalidPathException ex) {
                // ignore
            }
        }
        return Paths.get(System.getProperty("user.dir"));
    }

    static void putPath(String key, Path path) {
        put(key, path != null ? path.toString() : null);
    }

}
