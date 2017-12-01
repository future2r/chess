package name.ulbricht.chess.game;

public enum CheckState {

    NONE, CHECK, CHECKMATE;

    public String getDisplayName() {
        return Messages.getString(getClass().getSimpleName() + '.' + name() + ".displayName");
    }
}
