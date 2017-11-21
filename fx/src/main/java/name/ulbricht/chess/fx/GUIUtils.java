package name.ulbricht.chess.fx;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class GUIUtils {

    static void showInfoAlert(Node ownerNode, String titleResourceName, String contentTextResourceName) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.initOwner(ownerNode.getScene().getWindow());
        alert.setTitle(Messages.getString(titleResourceName));
        alert.setContentText(Messages.getString(contentTextResourceName));
        alert.showAndWait();
    }

    static Optional<ButtonType> showQuestionAlert(Node ownerNode, String titleResourceName, String contentTextResourceName) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setDefaultButton(true);

        alert.initOwner(ownerNode.getScene().getWindow());
        alert.setTitle(Messages.getString(titleResourceName));
        alert.setContentText(Messages.getString(contentTextResourceName));
        return alert.showAndWait();
    }

    static Collection<Image> loadImages(String resourceName) {
        String imageNames = Messages.getString(resourceName);
        return Stream.of(imageNames.split(","))
                .map(n -> GUIUtils.class.getResource(n).toString())
                .map(Image::new)
                .collect(Collectors.toList());
    }

}
