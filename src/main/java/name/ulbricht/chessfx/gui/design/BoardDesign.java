package name.ulbricht.chessfx.gui.design;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class BoardDesign {

    private static List<BoardDesign> designs = new ArrayList<>();

    static {
        designs.add(new BoardDesign(DefaultBoardRenderer.ID, DefaultBoardRenderer.class));
        designs.add(new BoardDesign(ClassicBoardRenderer.ID, ClassicBoardRenderer.class));
    }

    public static List<BoardDesign> getDesigns() {
        return Collections.unmodifiableList(designs);
    }

    private final String id;
    private final Class<? extends BoardRenderer> rendererClass;

    private BoardDesign(String id, Class<? extends BoardRenderer> rendererClass) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.rendererClass = Objects.requireNonNull(rendererClass, "rendererClass cannot be null");
    }

    public String getDisplayName() {
        return Messages.getString("BoardDesign." + this.id + ".displayName");
    }

    public BoardRenderer createRenderer(BoardRendererContext context) {
        try {
            BoardRenderer renderer = this.rendererClass.getConstructor().newInstance();
            renderer.setContext(context);
            return renderer;
        } catch (ReflectiveOperationException e) {
            throw new InternalError(e);
        }
    }
}
