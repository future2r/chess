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

    static void showInfo(Node ownerNode, String contentText) {
        showAlert(ownerNode, Messages.getString("GUIUtils.infoAlert.title"), contentText);
    }

    static void showError(Node ownerNode, String contentText) {
        showAlert(ownerNode, Messages.getString("GUIUtils.errorAlert.title"), contentText);
    }

    private static void showAlert(Node ownerNode, String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.initOwner(ownerNode.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    static Optional<ButtonType> showQuestion(Node ownerNode, String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setDefaultButton(true);

        alert.initOwner(ownerNode.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(contentText);
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
