package controller;

import javafx.fxml.FXML;
import service.SceneManager;

public final class HowToPlayController {
    @FXML private void back() { SceneManager.switchScene("MainMenu.fxml"); }
}
