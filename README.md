# Water Quest: Escape the Biology Lab

A JavaFX 21 educational escape-room game for Malaysian Matriculation Biology (SB015). Players complete six water-science experiments, collect Water Crystals, and unlock the laboratory through a randomized final quiz.

## Run

Requirement: JDK 21. The included Maven Wrapper downloads the correct Maven version automatically on its first run.

```bash
./mvnw clean javafx:run
```

In IntelliJ IDEA, open this directory as a Maven project, select a JDK 21 SDK, enable use of the Maven Wrapper when prompted, then run `application.Main` or the Maven goal `javafx:run`.

## Downloadable desktop apps

The GitHub Actions workflow in `.github/workflows/package-app.yml` creates
self-contained downloads for Windows, Intel Macs, and Apple Silicon Macs. Each
ZIP includes the application, Java 21 runtime, JavaFX, and all other required
dependencies. End users do not need to install Java or Maven.

To make packages without publishing a release, open the repository's **Actions**
tab, select **Package desktop apps**, and choose **Run workflow**. Download the
completed artifacts from the workflow run.

To publish the ZIPs on GitHub Releases, create and push a version tag:

```bash
git tag v1.0.0
git push origin v1.0.0
```

The workflow adds these downloads to the release:

- `WaterQuest-Windows-x64.zip` — extract it and run `WaterQuest.exe`
- `WaterQuest-macOS-Intel.zip` — for Intel-based Macs
- `WaterQuest-macOS-Apple-Silicon.zip` — for M-series Macs

The macOS packages are currently unsigned. macOS may therefore ask the user to
right-click `WaterQuest.app` and choose **Open** the first time. Public,
warning-free distribution requires an Apple Developer ID certificate and
notarization.

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
