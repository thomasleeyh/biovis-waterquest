# Water Quest: Escape the Biology Lab

A JavaFX 21 educational escape-room game for Malaysian Matriculation Biology (SB015). Players complete six water-science experiments, collect Water Crystals, and unlock the laboratory through a randomized final quiz.

## Run

Requirement: JDK 21. The included Maven Wrapper downloads the correct Maven version automatically on its first run.

```bash
./mvnw clean javafx:run
```

In IntelliJ IDEA, open this directory as a Maven project, select a JDK 21 SDK, enable use of the Maven Wrapper when prompted, then run `application.Main` or the Maven goal `javafx:run`.

## Architecture

- `application` — JavaFX entry point
- `controller` — FXML UI controllers
- `model` — player, achievements, questions, quiz, and scoring
- `service` — scene, game state, questions, audio, and animation services
- `resources/view` — FXML scenes
- `resources/css` — responsive visual theme
- `resources/images` — project PNG artwork

The application reuses one `Stage` through `SceneManager`. `GameManager` owns the game session, while controllers handle only scene interaction and presentation.

## Gameplay

Each mission requires a correct experiment sequence before its scientific checkpoint appears. Incorrect arrangements shake and reset. Checkpoint answers update XP, lives, accuracy, and achievements. The final escape protocol randomly chooses 10 questions and gives 20 seconds per answer.

Audio is optional and safely degrades when clips are unavailable. To add licensed audio, place MP3 files such as `correct.mp3`, `wrong.mp3`, and `crystal.mp3` in `src/main/resources/audio`; `AudioManager` loads them by logical name.
