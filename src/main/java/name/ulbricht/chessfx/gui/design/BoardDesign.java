package name.ulbricht.chessfx.gui.design;

import java.util.*;

public final class BoardDesign {

    private static List<BoardDesign> designs = new ArrayList<>();

    static {
        designs.add(new BoardDesign(ClassicBoardRenderer.ID, ClassicBoardRenderer.class));
        designs.add(new BoardDesign(SimpleBoardRenderer.ID, SimpleBoardRenderer.class));
    }

    public static List<BoardDesign> getDesigns(){
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

    public BoardRenderer createRenderer() {
        try {
            return this.rendererClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new InternalError(e);
        }
    }
}
