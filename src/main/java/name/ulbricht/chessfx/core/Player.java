package name.ulbricht.chessfx.core;

public enum Player {
    WHITE, BLACK;

    public String getShortName() {
        return Messages.getString(String.format("%s.%s.shortName", this.getClass().getSimpleName(), name()));
    }

    public String getDisplayName() {
        return Messages.getString(String.format("%s.%s.displayName", this.getClass().getSimpleName(), name()));
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}