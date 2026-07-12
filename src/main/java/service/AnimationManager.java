package service;

import javafx.animation.*;import javafx.scene.Node;import javafx.util.Duration;
public final class AnimationManager {private AnimationManager(){}public static void pulse(Node node){ScaleTransition t=new ScaleTransition(Duration.millis(180),node);t.setFromX(1);t.setFromY(1);t.setToX(1.12);t.setToY(1.12);t.setAutoReverse(true);t.setCycleCount(2);t.play();}public static void shake(Node node){TranslateTransition t=new TranslateTransition(Duration.millis(55),node);t.setByX(10);t.setAutoReverse(true);t.setCycleCount(6);t.play();}public static void spin(Node node){RotateTransition t=new RotateTransition(Duration.millis(650),node);t.setByAngle(360);t.play();}}
