# BIOVIS: Water Quest

A JavaFX 21 educational escape-room game for Malaysian Matriculation Biology (SB015). Players complete six water-science experiments, collect Water Crystals, and unlock the laboratory through a randomized final quiz.

## Downloadable desktop apps

Download the latest version from the
[GitHub Releases page](https://github.com/thomasleeyh/h2oescapegame-matrik/releases/latest).
Choose the file for your computer:

- `WaterQuest-Windows-x64.zip` — extract it and run `WaterQuest.exe`
- `WaterQuest-macOS-Intel.zip` — for Macs with an Intel processor
- `WaterQuest-macOS-Apple-Silicon.zip` — for Macs with an M1, M2, M3, M4, or later Apple chip

Each download is self-contained and includes Java 21, JavaFX, and the other
required dependencies. You do not need to install Java or Maven.

### macOS installation

1. Open the Apple menu and select **About This Mac** to identify your chip.
2. Download the matching Intel or Apple Silicon ZIP from the latest release.
3. Double-click the ZIP file to extract `WaterQuest.app`.
4. Move `WaterQuest.app` to the **Applications** folder if desired.
5. Control-click `WaterQuest.app`, select **Open**, and then select **Open**
   again on the first launch.

### Windows installation

1. Download `WaterQuest-Windows-x64.zip` from the latest release.
2. Right-click the ZIP file and select **Extract All**.
3. Open the extracted `WaterQuest` folder.
4. Double-click `WaterQuest.exe` to start the game.
5. If Microsoft Defender SmartScreen appears, select **More info**, verify that
   you downloaded the file from this repository, and select **Run anyway**.

Keep the contents of the extracted `WaterQuest` folder together. Moving only
the `.exe` file will prevent the application from starting.

The macOS packages are currently unsigned. macOS may therefore ask the user to
approve the application the first time it is opened.

## Gameplay

Each mission requires a correct experiment sequence before its scientific checkpoint appears. Incorrect arrangements shake and reset. Checkpoint answers update XP, lives, accuracy, and achievements. The final escape protocol randomly chooses 10 questions and gives 20 seconds per answer.

Audio is optional and safely degrades when clips are unavailable. To add licensed audio, place MP3 files such as `correct.mp3`, `wrong.mp3`, and `crystal.mp3` in `src/main/resources/audio`; `AudioManager` loads them by logical name.

## How to play

1. Start Water Quest and enter your player name.
2. Read the mission instructions and begin the first experiment.
3. Arrange each experiment's steps in the correct scientific sequence.
4. Answer the checkpoint question to earn XP and a Water Crystal.
5. Complete all six missions while preserving your available lives.
6. Finish the randomized 10-question escape protocol. Each question has a
   20-second time limit.

Use the in-game **Learning Notes** when you need to review a water-science
concept. The **Achievements** screen records milestones earned during play.

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

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for the
complete terms.
