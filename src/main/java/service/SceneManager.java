package service;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public final class SceneManager {
    private static final String APP_CREDIT =
            "Water Quest™ | Designed & Developed by BIOVIS | IICFE26";
    private static Stage stage;

    private SceneManager() {}

    public static void initialise(Stage value) {
        stage = Objects.requireNonNull(value);
    }

    public static void switchScene(String fxml) {
        try {
            Parent page = FXMLLoader.load(Objects.requireNonNull(
                    SceneManager.class.getResource("/view/" + fxml)));
            Parent root = withCreditFooter(page);

            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            Scene scene = stage.getScene();

            if (scene == null) {
                scene = new Scene(root, 1200, 760);
                scene.setFill(Color.web("#031b32"));
                scene.getStylesheets().add(Objects.requireNonNull(
                        SceneManager.class.getResource("/css/style.css")).toExternalForm());
                var learningCss = SceneManager.class.getResource("/css/learning-notes.css");
                if (learningCss != null) scene.getStylesheets().add(learningCss.toExternalForm());
                stage.setScene(scene);
            } else {
                // Reusing the Scene prevents native full-screen from being interrupted
                // when navigating between FXML screens, particularly on macOS.
                scene.setFill(Color.web("#031b32"));
                scene.setRoot(root);
            }

            // Some window managers briefly clear these flags while replacing content.
            Platform.runLater(() -> {
                if (wasMaximized && !stage.isMaximized()) stage.setMaximized(true);
                if (wasFullScreen && !stage.isFullScreen()) stage.setFullScreen(true);
            });

            // Reveal the page over the app's navy backdrop. Starting above zero
            // prevents the white flash produced by exposing JavaFX's default fill.
            FadeTransition fade = new FadeTransition(Duration.millis(260), root);
            fade.setFromValue(0.22);
            fade.setToValue(1);
            fade.setInterpolator(Interpolator.EASE_OUT);

            fade.play();
        } catch (IOException | NullPointerException ex) {
            throw new IllegalStateException("Cannot load scene " + fxml, ex);
        }
    }

    public static Stage getStage() {
        return stage;
    }

    private static Parent withCreditFooter(Parent page) {
        Label credit = new Label(APP_CREDIT);
        credit.getStyleClass().add("app-credit");
        credit.setMouseTransparent(true);

        StackPane frame = new StackPane(page, credit);
        frame.getStyleClass().add("scene-frame");
        StackPane.setAlignment(credit, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setMargin(credit, new javafx.geometry.Insets(0, 20, 10, 20));
        return frame;
    }
}
