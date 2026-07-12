package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import service.GameManager;
import service.SceneManager;

public final class NameEntryController {
    @FXML private TextField playerName;
    @FXML private Label errorLabel;

    @FXML public void initialize() {
        playerName.setOnAction(event -> enterLobby());
    }

    @FXML private void enterLobby() {
        String name = playerName.getText() == null ? "" : playerName.getText().trim();
        if (name.isEmpty()) {
            errorLabel.setText("Please enter your name to continue.");
            playerName.requestFocus();
            return;
        }
        GameManager.getInstance().newGame(name);
        SceneManager.switchScene("MainMenu.fxml");
    }
}
