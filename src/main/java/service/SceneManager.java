package service;

import javafx.animation.FadeTransition;import javafx.fxml.FXMLLoader;import javafx.scene.Parent;import javafx.scene.Scene;import javafx.stage.Stage;import javafx.util.Duration;
import java.io.IOException;import java.util.Objects;
public final class SceneManager {
 private static Stage stage; private SceneManager(){}
 public static void initialise(Stage value){stage=Objects.requireNonNull(value);}
 public static void switchScene(String fxml){
  try{Parent root=FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/view/"+fxml)));Scene scene=new Scene(root,1200,760);scene.getStylesheets().add(Objects.requireNonNull(SceneManager.class.getResource("/css/style.css")).toExternalForm());var learningCss=SceneManager.class.getResource("/css/learning-notes.css");if(learningCss!=null)scene.getStylesheets().add(learningCss.toExternalForm());stage.setScene(scene);FadeTransition fade=new FadeTransition(Duration.millis(450),root);fade.setFromValue(0);fade.setToValue(1);fade.play();}
  catch(IOException|NullPointerException ex){throw new IllegalStateException("Cannot load scene "+fxml,ex);}
 }
 public static Stage getStage(){return stage;}
}
