package name.ulbricht.chess.fx;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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

    static class DialogBuilder<T> {

        private ButtonType[] buttons;
        private ButtonType defaultButton;
        private Node owner;
        private String title;
        private String headerText;
        private Node content;

        private DialogBuilder<T> buttons(ButtonType... values) {
            this.buttons = values;
            return this;
        }

        private DialogBuilder<T> defaultButton(ButtonType value) {
            this.defaultButton = value;
            return this;
        }

        public DialogBuilder<T> owner(Node owner) {
            this.owner = owner;
            return this;
        }

        public DialogBuilder<T> title(String value) {
            this.title = value;
            return this;
        }

        public DialogBuilder<T> headerText(String value) {
            this.headerText = value;
            return this;
        }

        public DialogBuilder<T> content(Node value) {
            this.content = value;
            return this;
        }

        public Dialog<T> build() {
            Dialog<T> dialog = new Dialog<>();

            dialog.setResizable(true);

            if (this.buttons != null) dialog.getDialogPane().getButtonTypes().addAll(this.buttons);
            if (defaultButton != null) {
                Button button = (Button) dialog.getDialogPane().lookupButton(this.defaultButton);
                if (button != null) button.setDefaultButton(true);
            }
            if (this.owner != null) dialog.initOwner(this.owner.getScene().getWindow());
            if (this.title != null) dialog.setTitle(this.title);
            if (this.headerText != null) dialog.setHeaderText(this.headerText);
            if (this.content != null) dialog.getDialogPane().setContent(this.content);

            return dialog;
        }
    }

    static DialogBuilder<ButtonType> okCancelDialog() {
        return new DialogBuilder<ButtonType>()
                .buttons(ButtonType.OK, ButtonType.CANCEL)
                .defaultButton(ButtonType.OK);
    }

    static Collection<Image> loadImages(String resourceName) {
        String imageNames = Messages.getString(resourceName);
        return Stream.of(imageNames.split(","))
                .map(n -> GUIUtils.class.getResource(n).toString())
                .map(Image::new)
                .collect(Collectors.toList());
    }

}
