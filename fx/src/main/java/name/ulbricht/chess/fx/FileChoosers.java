package name.ulbricht.chess.fx;

import javafx.scene.Node;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

final class FileChoosers {

    enum Category {
        BOARDS;

        final String key;
        final FileChooser.ExtensionFilter filter;

        Category() {
            key = "FileChoosers.Category." + name();
            filter = new FileChooser.ExtensionFilter("Forsyth-Edwards-Notation", "*.fen");
        }
    }

    static Path openFile(Node ownerNode, Category category) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(category.filter);
        chooser.setSelectedExtensionFilter(category.filter);
        chooser.setInitialDirectory(Settings.getPath(category.key).toFile());
        File file = chooser.showOpenDialog(ownerNode.getScene().getWindow());

        if (file != null) {
            Path path = file.toPath();
            Settings.putPath(category.key, path.getParent());
            return path;
        }
        return null;
    }
}
