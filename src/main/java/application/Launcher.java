package application;

/**
 * Plain Java entry point used by packaged builds. Keeping this separate from the
 * JavaFX Application subclass allows the bundled launcher to initialise JavaFX
 * from the application class path.
 */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}
