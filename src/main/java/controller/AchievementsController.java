package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Achievement;
import model.Player;
import service.GameManager;
import service.SceneManager;

public final class AchievementsController {
    @FXML private VBox achievementList;
    @FXML private Label summary, recordOwner;

    @FXML public void initialize() {
        Player player = GameManager.getInstance().getPlayer();
        recordOwner.setText(player.getName() + "’s Research Record");
        summary.setText(player.getAchievements().size() + " / " + Achievement.values().length + " UNLOCKED");
        for (Achievement achievement : Achievement.values()) {
            boolean unlocked = player.getAchievements().contains(achievement);
            VBox card = new VBox(4);
            card.getStyleClass().addAll("achievement-card", unlocked ? "achievement-unlocked" : "achievement-locked");
            Label title = new Label((unlocked ? "◆  " : "◇  ") + achievement.title());
            title.getStyleClass().add("achievement-title");
            Label description = new Label(achievement.description());
            description.setWrapText(true);
            description.getStyleClass().add("achievement-description");
            card.getChildren().addAll(title, description);
            achievementList.getChildren().add(card);
        }
    }

    @FXML private void back() { SceneManager.switchScene("MainMenu.fxml"); }
}
