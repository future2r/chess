package name.ulbricht.chessfx.gui.design;

abstract class AbstractBoardDesign implements BoardDesign {

    @Override
    public String getDisplayName() {
        return Messages.getString(this.getClass().getSimpleName() + ".displayName");
    }

    @Override
    public double getPrefSquareSize() {
        return 100;
    }

    @Override
    public double getPrefBorderSize() {
        return 30;
    }
}
