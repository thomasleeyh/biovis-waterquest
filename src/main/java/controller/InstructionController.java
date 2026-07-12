package controller;
import javafx.fxml.FXML;import javafx.scene.control.*;import service.*;
public final class InstructionController {@FXML private TextField playerName;@FXML private Label progressLabel;@FXML private ProgressBar progress;@FXML public void initialize(){int n=GameManager.getInstance().crystals();progress.setProgress(n/6.0);progressLabel.setText(n+" / 6 crystals");}@FXML private void begin(){GameManager.getInstance().newGame(playerName.getText());SceneManager.switchScene("Level1.fxml");}@FXML private void back(){SceneManager.switchScene("MainMenu.fxml");}}
