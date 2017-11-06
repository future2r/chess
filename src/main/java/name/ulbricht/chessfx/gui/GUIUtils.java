package name.ulbricht.chessfx.gui;

import javafx.scene.image.Image;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class GUIUtils {

    static Collection<Image> loadImages(String resourceName) {
        String imageNames = Messages.getString(resourceName);
        return Stream.of(imageNames.split(","))
                .map(n -> GUIUtils.class.getResource(n).toString())
                .map(Image::new)
                .collect(Collectors.toList());
    }

}
