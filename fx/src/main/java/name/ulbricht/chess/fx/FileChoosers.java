package name.ulbricht.chess.fx;

import javafx.scene.Node;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

final class FileChoosers {

    enum Category {
        BOARDS;

        final String settingsKey;

        Category() {
            settingsKey = "FileChoosers.Category." + name();
        }
    }

    enum Format {
        FEN;

        final String extension;

        Format() {
            extension = "fen";
        }

        String description() {
            return Messages.getString("FileChoosers.Format." + name() + ".description");
        }

        String defaultFileName() {
            return Messages.getString("FileChoosers.Format." + name() + ".defaultFileName") + '.' + this.extension;
        }
    }

    static Path openFile(Node ownerNode, Category category, Format format) {
        FileChooser chooser = createChooser(category, format);
        File file = chooser.showOpenDialog(ownerNode.getScene().getWindow());
        return handleResult(category, file);
    }

    static Path saveFile(Node ownerNode, Category category, Format format) {
        FileChooser chooser = createChooser(category, format);
        chooser.setInitialFileName(format.defaultFileName());
        File file = chooser.showSaveDialog(ownerNode.getScene().getWindow());
        return forceExtension(handleResult(category, file), format.extension);
    }

    private static FileChooser createChooser(Category category, Format format) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(format.description(), "*." + format.extension);
        chooser.getExtensionFilters().add(filter);
        chooser.setSelectedExtensionFilter(filter);
        chooser.setInitialDirectory(Settings.getPath(category.settingsKey).toFile());
        return chooser;
    }

    private static Path handleResult(Category category, File file) {
        if (file != null) {
            Path path = file.toPath();
            Settings.putPath(category.settingsKey, path.getParent());
            return path;
        }
        return null;
    }

    private static Path forceExtension(Path file, String extension) {
        if (file != null) {
            String fileName = file.getFileName().toString();
            if (!fileName.isEmpty()) {
                int sepIdx = fileName.indexOf('.');
                if (sepIdx < 0) fileName += '.';
                if (sepIdx == (fileName.length() - 1)) fileName += extension;
                return file.getParent().resolve(fileName);
            }
        }
        return file;
    }
}
