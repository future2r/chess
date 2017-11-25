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
        FileChooser chooser = createChooser(category);
        File file = chooser.showOpenDialog(ownerNode.getScene().getWindow());
        return handleResult(category, file);
    }

    static Path saveFile(Node ownerNode, Category category) {
        FileChooser chooser = createChooser(category);
        File file = chooser.showSaveDialog(ownerNode.getScene().getWindow());
        return handleResult(category, file);
    }

    private static FileChooser createChooser(Category category) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(category.filter);
        chooser.setSelectedExtensionFilter(category.filter);
        chooser.setInitialDirectory(Settings.getPath(category.key).toFile());
        return chooser;
    }

    private static Path handleResult(Category category, File file) {
        if (file != null) {
            Path path = file.toPath();
            Settings.putPath(category.key, path.getParent());
            return path;
        }
        return null;
    }
}
