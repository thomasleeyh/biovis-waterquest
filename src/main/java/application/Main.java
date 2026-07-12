package application;

import javafx.application.Application;
import javafx.stage.Stage;
import service.SceneManager;

public final class Main extends Application {
    @Override public void start(Stage stage) {
        SceneManager.initialise(stage);
        stage.setTitle("Water Quest: Escape the Biology Lab");
        stage.setMinWidth(960); stage.setMinHeight(640);
        SceneManager.switchScene("Instruction.fxml");
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
