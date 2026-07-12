package controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;
import model.LearningTopic;
import service.LearningNotesService;
import service.GameManager;
import service.SceneManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Animated, JSON-backed digital textbook that reuses one JavaFX scene. */
public final class LearningNotesController {
    private static final int READABLE_TOPIC_COUNT = 6;
    @FXML private VBox topicListBox, keyPointsBox, storyPane, pageHost, hotspotInfoCard, keyPointsSection, funFactCard, examTipCard;
    @FXML private AnchorPane moleculeHotspots;
    @FXML private Pane ambientLayer;
    @FXML private StackPane imageStage;
    @FXML private StackPane structureImageActionHost;
    @FXML private HBox bottomNavigation, journeyControls, imageStageHost;
    @FXML private ScrollPane sidebarScroll;
    @FXML private Label topicTitleLabel, topicNumberLabel, descriptionLabel, funFactLabel, examTipLabel, errorLabel,
            hotspotInstruction, hotspotNumberLabel, hotspotTitleLabel, hotspotDetailLabel, solventConclusionLabel,
            solventImageInfoLabel;
    @FXML private ProgressBar topicProgressBar;
    @FXML private ImageView mainImageView, secondaryImageView;
    @FXML private Button previousButton, nextButton, backButton, notesMenuButton, propertiesMenuButton,
            backLevelButton, zoomDeeperButton, structureImageActionButton;
    private final List<LearningTopic> topics = new ArrayList<>();
    private final boolean[] discoveredHotspots = new boolean[5];
    private Timeline hotspotPulse;
    private int currentIndex;
    private int structureJourneyStep;
    private int solventJourneyStep;
    private int propertyJourneyStep;

    @FXML public void initialize() {
        notesMenuButton.setText("‹ BACK TO MENU");
        createAmbientMotion();
        loadTopics();
    }

    /** A quiet, non-interactive particle layer keeps every lesson scene alive while idle. */
    private void createAmbientMotion() {
        Random random = new Random(15015);
        for (int i = 0; i < 14; i++) {
            Circle particle = new Circle(2.5 + random.nextDouble() * 5.5);
            particle.getStyleClass().add(i % 4 == 0 ? "ambient-spark" : "ambient-bubble");
            particle.setLayoutX(18 + random.nextDouble() * 724);
            particle.setLayoutY(35 + random.nextDouble() * 255);
            ambientLayer.getChildren().add(particle);

            TranslateTransition drift = new TranslateTransition(
                    Duration.seconds(3.8 + random.nextDouble() * 4.2), particle);
            drift.setByX(-18 + random.nextDouble() * 36);
            drift.setByY(-30 - random.nextDouble() * 42);
            drift.setInterpolator(Interpolator.EASE_BOTH);
            drift.setAutoReverse(true);
            drift.setCycleCount(Animation.INDEFINITE);
            drift.setDelay(Duration.millis(i * 170L));

            FadeTransition breathe = new FadeTransition(
                    Duration.seconds(1.8 + random.nextDouble() * 1.8), particle);
            breathe.setFromValue(.18);
            breathe.setToValue(.72);
            breathe.setInterpolator(Interpolator.EASE_BOTH);
            breathe.setAutoReverse(true);
            breathe.setCycleCount(Animation.INDEFINITE);
            breathe.setDelay(Duration.millis(i * 110L));
            drift.play();
            breathe.play();
        }
    }

    private void loadTopics() {
        try { topics.addAll(LearningNotesService.loadTopics()); }
        catch (Exception ex) { errorLabel.setText("Learning Notes could not be loaded. Please check the resource files."); System.err.println("Learning Notes error: " + ex.getMessage()); }
        if (!topics.isEmpty()) showHome(); else disableNavigation();
    }

    private void showHome() {
        setLessonVisible(false);
        sidebarScroll.setVisible(false); sidebarScroll.setManaged(false); topicTitleLabel.setText("Learning Notes"); topicNumberLabel.setText("2 MAIN TOPICS"); updateSessionProgress(); pageHost.getChildren().clear();
        Label welcome=new Label("Choose a biology topic to begin your interactive lesson."); welcome.getStyleClass().add("home-description");
        VBox structure=topicCard("01","Structure of Water","Explore H₂O, covalent bonds, polarity, the 104.5° angle, and hydrogen bonds.","OPEN TOPIC",()->showTopic(0));
        VBox properties=topicCard("02","Properties of Water","Open five subtopics: solvent, heat capacity, vaporisation, density, and cohesion.","VIEW SUBTOPICS",this::showPropertiesMenu);
        HBox cards=new HBox(22,structure,properties);cards.getStyleClass().add("main-topic-cards");pageHost.getChildren().addAll(welcome,cards);
    }
    private VBox topicCard(String number, String title, String description, String buttonText, Runnable action) {
        Label n = new Label(number); n.getStyleClass().add("main-topic-number");
        Label t = new Label(title); t.getStyleClass().add("main-topic-title");
        Label d = new Label(description); d.setWrapText(true); d.getStyleClass().add("main-topic-description");
        VBox.setVgrow(d, Priority.ALWAYS);
        Button open = new Button(buttonText + "  ›"); open.getStyleClass().add("primary-button"); open.setOnAction(e -> action.run());
        VBox box = new VBox(12, n, t, d, open); box.getStyleClass().add("main-topic-card"); box.setPrefWidth(390);
        return box;
    }
    private void showPropertiesMenu(){sidebarScroll.setVisible(false);sidebarScroll.setManaged(false);topicTitleLabel.setText("Properties of Water");topicNumberLabel.setText("5 SUBTOPICS");updateSessionProgress();pageHost.getChildren().clear();Label intro=new Label("Select one property to open its interactive lesson.");intro.getStyleClass().add("home-description");VBox list=new VBox(10);list.getStyleClass().add("property-menu");for(int i=1;i<=5;i++){final int index=i;Button b=new Button((i)+"  "+topics.get(i).title().replace("Properties • ",""));b.setMaxWidth(Double.MAX_VALUE);b.setAlignment(Pos.CENTER_LEFT);b.getStyleClass().add("property-button");b.setOnAction(e->showTopic(index));list.getChildren().add(b);}Button back=new Button("‹ BACK TO MAIN TOPICS");back.getStyleClass().add("navigation-button");back.setOnAction(e->showHome());pageHost.getChildren().addAll(intro,list,back);previousButton.setVisible(false);previousButton.setManaged(false);nextButton.setVisible(false);nextButton.setManaged(false);}

    private void updateSessionProgress() {
        topicProgressBar.setProgress(GameManager.getInstance().completedLearningTopicCount() / (double) READABLE_TOPIC_COUNT);
    }

    private void completeCurrentLearningTopic() {
        if (currentIndex < 0 || currentIndex >= topics.size()) return;
        GameManager.getInstance().completeLearningTopic(topics.get(currentIndex).id());
        updateSessionProgress();
    }

    private void setLessonVisible(boolean visible){
        for(Node node:List.of(topicTitleLabel,imageStageHost,hotspotInstruction,hotspotInfoCard,descriptionLabel,keyPointsSection,bottomNavigation)){
            node.setVisible(visible); node.setManaged(visible);
        }
        if (!visible) { solventConclusionLabel.setVisible(false); solventConclusionLabel.setManaged(false); }
        if (!visible) { solventImageInfoLabel.setVisible(false); solventImageInfoLabel.setManaged(false); }
        if (!visible) { structureImageActionHost.setVisible(false); structureImageActionHost.setManaged(false); }
        // These supplementary cards are intentionally excluded from every lesson.
        funFactCard.setVisible(false); funFactCard.setManaged(false);
        examTipCard.setVisible(false); examTipCard.setManaged(false);
    }

    private void buildSidebarForTopic(int selectedIndex) {
        topicListBox.getChildren().clear();
        int start = selectedIndex == 0 ? 0 : 1;
        int end = selectedIndex == 0 ? 0 : Math.min(5, topics.size() - 1);
        for (int i = start; i <= end; i++) {
            final int index=i;
            String title = topics.get(i).title().replace("Properties • ", "");
            Button button=new Button(title); button.setUserData(index); button.setMaxWidth(Double.MAX_VALUE); button.setAlignment(Pos.CENTER_LEFT);
            button.getStyleClass().add("topic-button"); button.setOnAction(e->showTopic(index)); topicListBox.getChildren().add(button);
        }
    }

    private Label sectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("diagram-section-label");
        return label;
    }

    private void showTopic(int index) {
        if (index < 0 || index >= topics.size()) return;
        pageHost.getChildren().clear();
        currentIndex=index; LearningTopic topic=topics.get(index); sidebarScroll.setVisible(true);sidebarScroll.setManaged(true);
        storyPane.getStyleClass().remove("structure-reference-theme");
        if (index >= 0 && index <= 5) storyPane.getStyleClass().add("structure-reference-theme");
        buildSidebarForTopic(index);
        setLessonVisible(true);
        notesMenuButton.setVisible(true); notesMenuButton.setManaged(true);
        propertiesMenuButton.setVisible(index > 0); propertiesMenuButton.setManaged(index > 0);
        previousButton.setVisible(index > 1); previousButton.setManaged(index > 1);
        nextButton.setVisible(index >= 1 && index < 5); nextButton.setManaged(index >= 1 && index < 5);
        topicTitleLabel.setText(topic.title()); topicNumberLabel.setText("TOPIC "+(index+1)+" / "+READABLE_TOPIC_COUNT); updateSessionProgress();
        descriptionLabel.setText(topic.description()); funFactLabel.setText(topic.funFact()); examTipLabel.setText(topic.examTip());
        solventConclusionLabel.setVisible(false); solventConclusionLabel.setManaged(false);
        solventImageInfoLabel.setVisible(false); solventImageInfoLabel.setManaged(false);
        loadImageSafely(mainImageView,topic.imagePath()); loadImageSafely(secondaryImageView,topic.secondaryImagePath()); secondaryImageView.setVisible(false);secondaryImageView.setManaged(false);
        structureJourneyStep = 0;
        solventJourneyStep = 0;
        propertyJourneyStep = 0;
        structureImageActionHost.setVisible(false); structureImageActionHost.setManaged(false);
        if (index == 0) loadStructureJourneyStep(0);
        else if (index == 1) loadSolventJourneyStep(0);
        else if (index >= 2 && index <= 5) loadPropertyJourneyStep(index, 0);
        else configureImageInteraction(index);
        keyPointsBox.getChildren().clear(); for(String point:topic.keyPoints()){Label label=new Label("◆  "+point);label.setWrapText(true);label.getStyleClass().add("key-point");keyPointsBox.getChildren().add(label);}
        keyPointsSection.setVisible(index > 5); keyPointsSection.setManaged(index > 5);
        animateTopicEntry(); updateSidebarSelection();
    }

    private void loadImageSafely(ImageView view,String path){if(path==null||path.isBlank()){view.setImage(null);view.setVisible(false);view.setManaged(false);return;}var stream=getClass().getResourceAsStream(path);if(stream==null){System.err.println("Missing resource: "+path);view.setImage(null);view.setVisible(false);view.setManaged(false);return;}Image image=new Image(stream);view.setImage(image);view.setPreserveRatio(true);view.setSmooth(true);view.setVisible(true);view.setManaged(true);if(view==mainImageView)resizeImageStageToImage(image);}

    /** Fits the decorative frame tightly around the image's preserved aspect ratio. */
    private void resizeImageStageToImage(Image image) {
        if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) return;
        double scale = Math.min(760.0 / image.getWidth(), 310.0 / image.getHeight());
        double displayedWidth = Math.ceil(image.getWidth() * scale);
        double displayedHeight = Math.ceil(image.getHeight() * scale);
        double framedWidth = displayedWidth + 24;
        double framedHeight = displayedHeight + 24;
        imageStage.setMinSize(framedWidth, framedHeight);
        imageStage.setPrefSize(framedWidth, framedHeight);
        imageStage.setMaxSize(framedWidth, framedHeight);
        ambientLayer.setPrefSize(displayedWidth, displayedHeight);
        ambientLayer.setMaxSize(displayedWidth, displayedHeight);
        ambientLayer.setClip(new Rectangle(displayedWidth, displayedHeight));
        moleculeHotspots.setPrefSize(displayedWidth, displayedHeight);
        moleculeHotspots.setMaxSize(displayedWidth, displayedHeight);
    }

    private void animateTopicEntry(){topicTitleLabel.setOpacity(0);descriptionLabel.setOpacity(0);mainImageView.setOpacity(0);TranslateTransition title=new TranslateTransition(Duration.millis(420),topicTitleLabel);title.setFromY(-18);title.setToY(0);FadeTransition titleFade=fade(topicTitleLabel);FadeTransition imageFade=fade(mainImageView);ScaleTransition imageScale=new ScaleTransition(Duration.millis(650),mainImageView);imageScale.setFromX(.94);imageScale.setFromY(.94);imageScale.setToX(1);imageScale.setInterpolator(Interpolator.EASE_OUT);FadeTransition textFade=fade(descriptionLabel);new ParallelTransition(new SequentialTransition(title,titleFade),new ParallelTransition(imageFade,imageScale),textFade).play();for(int i=0;i<keyPointsBox.getChildren().size();i++){Node point=keyPointsBox.getChildren().get(i);point.setOpacity(0);FadeTransition f=fade(point);f.setDelay(Duration.millis(130L*i));f.play();}}
    private FadeTransition fade(Node n){FadeTransition f=new FadeTransition(Duration.millis(420),n);f.setFromValue(0);f.setToValue(1);return f;}

    private record Hotspot(String marker, String title, String detail, double x, double y) {}

    private void configureImageInteraction(int topicIndex) {
        List<Hotspot> content = hotspotsForTopic(topicIndex);
        if (topicIndex > 5) {
            journeyControls.setVisible(false); journeyControls.setManaged(false);
        }
        boolean enabled = !content.isEmpty();
        moleculeHotspots.setVisible(enabled); moleculeHotspots.setManaged(enabled);
        hotspotInstruction.setVisible(enabled); hotspotInstruction.setManaged(enabled);
        hotspotInfoCard.setVisible(enabled); hotspotInfoCard.setManaged(enabled);
        if (hotspotPulse != null) hotspotPulse.stop();
        if (!enabled) return;
        java.util.Arrays.fill(discoveredHotspots, false);
        hotspotInstruction.setText("Select a glowing marker to inspect the illustration • 0 of " + content.size() + " discovered");
        hotspotNumberLabel.setText("?"); hotspotTitleLabel.setText("Choose a marker");
        hotspotDetailLabel.setText("Information will appear here one concept at a time.");
        moleculeHotspots.getChildren().removeIf(node -> node instanceof Line || node instanceof Circle);
        List<Node> markers = moleculeHotspots.getChildren().stream().filter(node -> node instanceof Button).toList();
        for (int i = 0; i < markers.size(); i++) {
            Node marker = markers.get(i);
            marker.getStyleClass().removeAll("molecule-hotspot-selected", "molecule-hotspot-discovered");
            marker.setVisible(i < content.size()); marker.setManaged(i < content.size());
            if (i >= content.size()) continue;
            Hotspot hotspot = content.get(i);
            double[] offset = calloutOffset(topicIndex, i);
            double markerX = hotspot.x() + offset[0];
            double markerY = hotspot.y() + offset[1];
            marker.relocate(markerX, markerY);
            Line pointer = new Line(markerX + 15, markerY + 15, hotspot.x(), hotspot.y());
            pointer.setMouseTransparent(true); pointer.setStrokeLineCap(StrokeLineCap.ROUND);
            pointer.getStyleClass().add("hotspot-pointer");
            moleculeHotspots.getChildren().add(i, pointer);
            Circle endpoint = new Circle(hotspot.x(), hotspot.y(), 3.2);
            endpoint.setMouseTransparent(true); endpoint.getStyleClass().add("hotspot-endpoint");
            moleculeHotspots.getChildren().add(i + 1, endpoint);
            if (marker instanceof Button button) {
                button.setText(String.valueOf(i + 1));
                button.setAccessibleText("Explore " + hotspot.title());
                final int selectedIndex = i;
                button.setOnAction(event -> {
                    revealHotspot(selectedIndex, hotspot, button, content.size());
                    boolean journeyTarget = currentIndex == 0 &&
                            (structureJourneyStep < 4 || (structureJourneyStep == 4 && selectedIndex == 0));
                    boolean solventTarget = currentIndex == 1 && solventJourneyStep < 4;
                    boolean propertyTarget = currentIndex >= 2 && currentIndex <= 5 && propertyJourneyStep < 4;
                    if (journeyTarget || solventTarget || propertyTarget) prepareJourneyAdvance();
                });
            }
        }
        hotspotPulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(moleculeHotspots.opacityProperty(), .72)),
                new KeyFrame(Duration.millis(850), new KeyValue(moleculeHotspots.opacityProperty(), 1, Interpolator.EASE_BOTH)));
        hotspotPulse.setAutoReverse(true); hotspotPulse.setCycleCount(Animation.INDEFINITE); hotspotPulse.play();
    }

    /** Keeps number badges off the feature while the pointer terminates on its exact target. */
    private double[] calloutOffset(int topic, int index) {
        if (topic == 1) return solventCalloutOffset(index);
        double[][] standard = {{-48,-38},{42,-36},{-52,28},{45,26},{18,46}};
        double[][] structure = {{-5,-73},{-69,12},{-68,-33},{-5,52},{49,22}};
        double[][] heat = {{-57,29},{-30,-52},{-19,36},{-21,29},{17,29}};
        double[][] latent = {{-45,-32},{-48,25},{38,24},{38,-32},{-45,-34}};
        double[][] density = {{-45,-34},{-48,24},{36,-34},{38,22},{-38,24}};
        double[][] cohesion = {{-44,-34},{42,-26},{-48,24},{40,24},{16,38}};
        double[][] chosen = switch (topic) {
            case 0 -> structure; case 2 -> heat;
            case 3 -> latent; case 4 -> density; case 5, 6 -> cohesion;
            default -> standard;
        };
        return chosen[Math.min(index, chosen.length - 1)];
    }

    private double[] solventCalloutOffset(int index) {
        double[][] offsets = switch (solventJourneyStep) {
            case 0 -> new double[][]{{54,-39}};   // badge beside, not over, the salt spoon
            case 1 -> new double[][]{{83,6}};     // badge beside the crystal pile
            case 2 -> new double[][]{{123,-38}};  // badge outside the glass
            case 3 -> new double[][]{{-141,-37}}; // badge in left margin; endpoint is on shell outline
            default -> new double[][]{
                    {128,-19},  // outer-right margin from Na+
                    {-151,0},   // outer-left margin from Cl−
                    {188,-43},  // outer-right/top margin from the oxygen region
                    {-211,54},  // outer-left/bottom margin from the hydrogen region
                    {-15,125}   // open lower centre beneath the two shells
            };
        };
        return offsets[Math.min(index, offsets.length - 1)];
    }

    private void revealHotspot(int index, Hotspot hotspot, Node selected, int total) {
        if (hotspotPulse != null) { hotspotPulse.stop(); moleculeHotspots.setOpacity(1); }
        discoveredHotspots[index] = true;
        for (Node marker : moleculeHotspots.getChildren()) marker.getStyleClass().remove("molecule-hotspot-selected");
        selected.getStyleClass().add("molecule-hotspot-discovered");
        selected.getStyleClass().add("molecule-hotspot-selected");
        hotspotNumberLabel.setText(String.valueOf(index + 1)); hotspotTitleLabel.setText(hotspot.title()); hotspotDetailLabel.setText(hotspot.detail());
        long count = 0; for (boolean discovered : discoveredHotspots) if (discovered) count++;
        hotspotInstruction.setText(count == total ? "Excellent — all " + total + " details discovered!" : "Select a glowing marker to inspect the illustration • " + count + " of " + total + " discovered");
        if (currentIndex == 0 && structureJourneyStep == 5 && count == total) showKeyPointsSummary();
        if (currentIndex == 1 && solventJourneyStep == 4 && count == total) showKeyPointsSummary();
        if (currentIndex == 1 && solventJourneyStep == 4 && count == total) showSolventConclusion();
        if (currentIndex >= 2 && currentIndex <= 5 && propertyJourneyStep == 4 && count == total) showKeyPointsSummary();
        hotspotInfoCard.setOpacity(0); hotspotInfoCard.setTranslateY(12);
        FadeTransition fade = new FadeTransition(Duration.millis(260), hotspotInfoCard); fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(260), hotspotInfoCard); slide.setToY(0); slide.setInterpolator(Interpolator.EASE_OUT);
        ScaleTransition pop = new ScaleTransition(Duration.millis(260), selected); pop.setFromX(.72); pop.setFromY(.72); pop.setToX(1); pop.setToY(1);
        new ParallelTransition(fade, slide, pop).play();
    }

    private void showKeyPointsSummary() {
        keyPointsSection.setVisible(true); keyPointsSection.setManaged(true);
        keyPointsSection.setOpacity(0); keyPointsSection.setTranslateY(18);
        FadeTransition fade = new FadeTransition(Duration.millis(420), keyPointsSection); fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(420), keyPointsSection);
        slide.setToY(0); slide.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(fade, slide).play();
        for (int i = 0; i < keyPointsBox.getChildren().size(); i++) {
            Node point = keyPointsBox.getChildren().get(i); point.setOpacity(0);
            FadeTransition item = fade(point); item.setDelay(Duration.millis(110L * i)); item.play();
        }
    }

    private void showSolventConclusion() {
        solventConclusionLabel.setVisible(true); solventConclusionLabel.setManaged(true);
        solventConclusionLabel.setOpacity(0); solventConclusionLabel.setTranslateY(12);
        FadeTransition fade = new FadeTransition(Duration.millis(380), solventConclusionLabel); fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(380), solventConclusionLabel);
        slide.setToY(0); slide.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(fade, slide).play();
    }

    private List<Hotspot> hotspotsForTopic(int topic) {
        return switch (topic) {
            case 0 -> structureHotspots();
            case 1 -> solventHotspots();
            case 2, 3, 4, 5 -> propertyHotspots(topic);
            case 6 -> List.of();
            default -> List.of();
        };
    }

    private List<Hotspot> structureHotspots() {
        return switch (structureJourneyStep) {
            case 0 -> List.of(new Hotspot("1", "Open the water bottle", "Click the bottle to zoom from the classroom into the water it contains.", 302, 245));
            case 1 -> List.of(new Hotspot("1", "Look inside the water", "Click the water inside the bottle to move from the visible liquid to its molecular scale.", 380, 220));
            case 2 -> List.of(new Hotspot("1", "Water molecules", "At the molecular scale, many water molecules move freely through the liquid.", 377, 165));
            case 3 -> List.of(new Hotspot("1", "Choose a water molecule", "Click the central molecule to isolate and inspect its atomic structure.", 377, 165));
            case 4 -> List.of(
                    new Hotspot("1", "Zoom out from this molecule", "This is one polar H₂O molecule. Click its oxygen centre to see how it can hydrogen-bond with neighbours.", 380, 112),
                    new Hotspot("2", "Hydrogen atoms (δ+)", "The two hydrogen atoms carry partial positive charges.", 291, 235),
                    new Hotspot("3", "O–H covalent bond", "Strong polar covalent bonds hold oxygen and hydrogen together within the molecule.", 334, 178),
                    new Hotspot("4", "104.5° angle", "Electron-pair repulsion produces water's bent 104.5° shape.", 380, 178),
                    new Hotspot("5", "Oxygen atom (δ−)", "Oxygen attracts shared electrons more strongly and carries a partial negative charge.", 452, 109));
            default -> List.of(
                    new Hotspot("1", "Central water molecule", "One water molecule can participate in a maximum of four hydrogen bonds.", 363, 115),
                    new Hotspot("2", "Hydrogen bond 1", "One lone pair on the central oxygen can accept a hydrogen bond from this neighbour.", 274, 40),
                    new Hotspot("3", "Hydrogen bond 2", "The other lone pair can accept a second hydrogen bond.", 489, 108),
                    new Hotspot("4", "Hydrogen bond 3", "One central δ+ hydrogen can donate a hydrogen bond to a neighbouring oxygen.", 305, 230),
                    new Hotspot("5", "Hydrogen bond 4", "The second central hydrogen can donate another bond: two accepted plus two donated equals four.", 417, 230));
        };
    }

    private List<Hotspot> solventHotspots() {
        return switch (solventJourneyStep) {
            case 0 -> List.of(new Hotspot("1", "Salt and water", "Sodium chloride is an ionic solid. Select the spoon to add NaCl to polar water.", 506, 149));
            case 1 -> List.of(new Hotspot("1", "Salt enters the water", "The NaCl crystals begin separating as water molecules attract ions at the crystal surface.", 377, 244));
            case 2 -> List.of(new Hotspot("1", "A clear solution", "The salt has not disappeared. Na⁺ and Cl⁻ ions are dispersed throughout the water at a scale too small to see.", 377, 158));
            case 3 -> List.of(new Hotspot("1", "Hydration shells", "The circular boundary encloses the oriented water molecules surrounding an ion. This layer is its hydration shell.", 211, 102));
            default -> List.of(
                    new Hotspot("1", "Sodium ion, Na⁺", "The positively charged sodium ion attracts the partially negative oxygen region of water.", 552, 89),
                    new Hotspot("2", "Chloride ion, Cl⁻", "The negatively charged chloride ion attracts the partially positive hydrogen regions of water.", 211, 210),
                    new Hotspot("3", "Oxygen faces Na⁺", "Water molecules orient with δ− oxygen atoms pointing towards the cation.", 492, 63),
                    new Hotspot("4", "Hydrogen faces Cl⁻", "Water molecules orient with δ+ hydrogen atoms pointing towards the anion.", 271, 201));
        };
    }

    private List<Hotspot> propertyHotspots(int topic) {
        if (propertyJourneyStep < 4) {
            String[][] titles = {
                    {}, {},
                    {"Compare land and water", "Add heat energy", "Inspect hydrogen bonds", "Observe temperature stability"},
                    {"Observe sweat", "Follow body heat", "Break hydrogen bonds", "Watch evaporation"},
                    {"Cool water to 4°C", "Cool below 4°C", "Inspect the ice lattice", "Compare solid and liquid"},
                    {"Inspect water molecules", "Join neighbouring molecules", "Follow the xylem column", "Inspect the water surface"}
            };
            String[][] details = {
                    {}, {},
                    {"Land changes temperature faster than water.", "Water absorbs considerable thermal energy before its temperature rises greatly.", "Energy is used to disrupt hydrogen bonds before molecular movement increases substantially.", "Water buffers rapid temperature changes in organisms and environments."},
                    {"Sweat places liquid water on warm skin.", "Thermal energy transfers from the body into the sweat.", "Considerable energy is required to overcome hydrogen bonds between water molecules.", "High-energy molecules escape as vapour and carry heat away."},
                    {"Liquid water reaches maximum density at 4°C.", "Below 4°C, hydrogen bonding begins arranging molecules farther apart.", "The open ice lattice contains more empty space than liquid water.", "Ice is less dense than liquid water and therefore floats."},
                    {"Hydrogen bonds attract neighbouring water molecules.", "This water–water attraction is cohesion.", "Cohesion maintains an unbroken column of water in xylem.", "Cohesion at the air–water boundary creates surface tension."}
            };
            return List.of(new Hotspot("1", titles[topic][propertyJourneyStep], details[topic][propertyJourneyStep], 380, 155));
        }
        return switch (topic) {
            case 2 -> List.of(
                    new Hotspot("1", "Water-rich organism", "Water's high specific heat capacity helps resist sudden internal temperature changes.", 188, 196),
                    new Hotspot("2", "Hydrogen bond", "Hydrogen bonds absorb energy before breaking.", 337, 154),
                    new Hotspot("3", "Heat energy", "A large energy input produces only a relatively small temperature change.", 365, 168),
                    new Hotspot("4", "Temperature buffer", "This property provides stable conditions for enzymes, cells and habitats.", 532, 182));
            case 3 -> List.of(
                    new Hotspot("1", "Sweat on skin", "Sweat receives thermal energy from the body.", 532, 182),
                    new Hotspot("2", "Hydrogen bonding", "Substantial energy is required to separate liquid water molecules.", 337, 154),
                    new Hotspot("3", "Evaporation", "The most energetic molecules escape from liquid to gas.", 500, 82),
                    new Hotspot("4", "Cooling result", "Escaping molecules remove heat and lower body temperature.", 520, 205));
            case 4 -> List.of(
                    new Hotspot("1", "Open ice lattice", "Hydrogen bonds hold ice molecules in an open arrangement with more empty space.", 337, 134),
                    new Hotspot("2", "Liquid water", "At 4°C, liquid molecules pack more closely and water reaches maximum density.", 434, 134),
                    new Hotspot("3", "Floating ice", "Less-dense ice remains at the surface.", 379, 229),
                    new Hotspot("4", "Insulated water", "The ice layer reduces heat loss so liquid water and aquatic life remain below.", 392, 265));
            default -> List.of(
                    new Hotspot("1", "Cohesion", "Hydrogen bonds cause water molecules to stick to one another.", 360, 184),
                    new Hotspot("2", "Adhesion", "Water is also attracted to polar materials in the xylem wall.", 408, 184),
                    new Hotspot("3", "Continuous column", "Cohesion helps transmit tension through an unbroken column of xylem water.", 318, 198),
                    new Hotspot("4", "Surface tension", "At the air–water boundary, inward cohesive forces support small organisms.", 551, 57));
        };
    }

    private void loadPropertyJourneyStep(int topic, int step) {
        int finalStep = topic == 2 ? 5 : topic == 3 ? 7 : topic == 4 ? 5 : 4;
        propertyJourneyStep = Math.min(step, finalStep);
        keyPointsSection.setVisible(false); keyPointsSection.setManaged(false);
        String[][] images = {
                {}, {},
                {"/images/1.High Specific Heat Capacity.png", "/images/High Specific Heat Capacity Step 1.png", "/images/High Specific Heat Capacity Step 2.png", "/images/High Specific Heat Capacity Step 3.png", "/images/High Specific Heat Capacity Step 4.png", "/images/High Specific Heat Capacity Step 5.png"},
                {"/images/High Latent Heat Clean.png", "/images/High Latent Heat Step 1.png", "/images/High Latent Heat Step 2.png", "/images/High Latent Heat Step 3.png", "/images/High Latent Heat Step 4.png", "/images/High Latent Heat Step 5.png", "/images/High Latent Heat Step 6.png", "/images/High Latent Heat Step 7.png"},
                {"/images/Maximum Density Clean.png", "/images/Maximum Density Step 1.png", "/images/Maximum Density Step 2.png", "/images/Maximum Density Step 3.png", "/images/Maximum Density Step 4.png", "/images/Maximum Density Step 5.png"},
                {"/images/Cohesion Step 1.png", "/images/Cohesion Step 2.png", "/images/Cohesion Step 3.png", "/images/Cohesion Step 4.png", "/images/Cohesion Step 5.png"}
        };
        morphToImage(images[topic][propertyJourneyStep]);
        if (topic == 2) {
            if (hotspotPulse != null) hotspotPulse.stop();
            moleculeHotspots.setVisible(false); moleculeHotspots.setManaged(false);
            hotspotInstruction.setVisible(false); hotspotInstruction.setManaged(false);
            hotspotInfoCard.setVisible(false); hotspotInfoCard.setManaged(false);
            journeyControls.setVisible(false); journeyControls.setManaged(false);
            mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT);

            String[] titles = {"ZOOM INTO SUNNY DAY  ›", "ZOOM INTO CLEAR NIGHT  ›",
                    "ZOOM INTO LAND  ›", "ZOOM INTO WATER  ›", "ZOOM INTO HYDROGEN BONDS  ›",
                    "HIGH SPECIFIC HEAT  ✓"};
            boolean finalImage = propertyJourneyStep == 5;
            structureImageActionHost.setVisible(true);
            structureImageActionHost.setManaged(true);
            structureImageActionHost.setTranslateY(0);
            structureImageActionButton.setText(titles[propertyJourneyStep]);
            structureImageActionButton.setDisable(finalImage);
            if (finalImage) { showKeyPointsSummary(); completeCurrentLearningTopic(); }
            return;
        }
        if (topic == 4) {
            if (hotspotPulse != null) hotspotPulse.stop();
            moleculeHotspots.setVisible(false); moleculeHotspots.setManaged(false);
            hotspotInstruction.setVisible(false); hotspotInstruction.setManaged(false);
            hotspotInfoCard.setVisible(false); hotspotInfoCard.setManaged(false);
            journeyControls.setVisible(false); journeyControls.setManaged(false);
            mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT);

            String[] titles = {"ZOOM INTO THE GLASS  ›", "ZOOM CLOSER INTO GLASS  ›",
                    "ZOOM INTO LIQUID WATER  ›", "ZOOM INTO ICE STRUCTURE  ›",
                    "FUNCTION OF FLOATING ICE  ›", "MAXIMUM DENSITY  ✓"};
            boolean finalImage = propertyJourneyStep == 5;
            structureImageActionHost.setVisible(true);
            structureImageActionHost.setManaged(true);
            structureImageActionHost.setTranslateY(0);
            structureImageActionButton.setText(titles[propertyJourneyStep]);
            structureImageActionButton.setDisable(finalImage);
            if (finalImage) { showKeyPointsSummary(); completeCurrentLearningTopic(); }
            return;
        }
        if (topic == 3) {
            if (hotspotPulse != null) hotspotPulse.stop();
            moleculeHotspots.setVisible(false); moleculeHotspots.setManaged(false);
            hotspotInstruction.setVisible(false); hotspotInstruction.setManaged(false);
            hotspotInfoCard.setVisible(false); hotspotInfoCard.setManaged(false);
            journeyControls.setVisible(false); journeyControls.setManaged(false);
            mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT);

            String[] titles = {"ZOOM INTO RUNNING  ›", "ZOOM INTO BODY HEAT  ›",
                    "ZOOM INTO SWEATING  ›", "ZOOM INTO SWEAT GLAND  ›",
                    "ZOOM INTO HEAT ABSORPTION  ›", "ZOOM INTO EVAPORATION  ›",
                    "ZOOM INTO BODY COOLING  ›", "EVAPORATIVE COOLING  ✓"};
            boolean finalImage = propertyJourneyStep == 7;
            structureImageActionHost.setVisible(true);
            structureImageActionHost.setManaged(true);
            structureImageActionHost.setTranslateY(0);
            structureImageActionButton.setText(titles[propertyJourneyStep]);
            structureImageActionButton.setDisable(finalImage);
            if (finalImage) { showKeyPointsSummary(); completeCurrentLearningTopic(); }
            return;
        }
        if (topic == 5) {
            if (hotspotPulse != null) hotspotPulse.stop();
            moleculeHotspots.setVisible(false); moleculeHotspots.setManaged(false);
            hotspotInstruction.setVisible(false); hotspotInstruction.setManaged(false);
            hotspotInfoCard.setVisible(false); hotspotInfoCard.setManaged(false);
            journeyControls.setVisible(false); journeyControls.setManaged(false);
            mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT);

            String[] titles = {"ZOOM INTO THE TREE  ›", "ZOOM INTO XYLEM  ›",
                    "ZOOM INTO INSECT ON THE WATER SURFACE  ›", "ZOOM INTO MOLECULAR ATTRACTION  ›",
                    "COHESION  ✓"};
            boolean finalImage = propertyJourneyStep == 4;
            structureImageActionHost.setVisible(true);
            structureImageActionHost.setManaged(true);
            structureImageActionHost.setTranslateY(0);
            structureImageActionButton.setText(titles[propertyJourneyStep]);
            structureImageActionButton.setDisable(finalImage);
            if (finalImage) { showKeyPointsSummary(); completeCurrentLearningTopic(); }
            return;
        }
        configureImageInteraction(topic);
        String[] names = {"", "", "Specific heat capacity", "Latent heat of vaporisation", "Density at 4°C", "Cohesion and adhesion"};
        hotspotInstruction.setText("Step " + (propertyJourneyStep + 1) + " of 5 • " +
                (propertyJourneyStep < 4 ? "Select the highlighted feature" : "Explore the four key details of " + names[topic]));
        journeyControls.setVisible(propertyJourneyStep > 0); journeyControls.setManaged(propertyJourneyStep > 0);
        backLevelButton.setVisible(propertyJourneyStep > 0); backLevelButton.setManaged(propertyJourneyStep > 0);
        zoomDeeperButton.setVisible(false); zoomDeeperButton.setManaged(false);
        configurePropertyImageTarget(topic);
    }

    private void configurePropertyImageTarget(int topic) {
        if (propertyJourneyStep >= 4) { mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT); return; }
        int expectedStep = propertyJourneyStep;
        mainImageView.setCursor(Cursor.HAND);
        mainImageView.setOnMouseClicked(event -> {
            double dx = event.getX() - 380, dy = event.getY() - 155;
            if (dx * dx + dy * dy <= 145 * 145 && propertyJourneyStep == expectedStep && currentIndex == topic) {
                moleculeHotspots.getChildren().stream().filter(node -> node instanceof Button && node.isVisible())
                        .map(node -> (Button) node).findFirst().ifPresent(Button::fire);
            }
        });
    }

    private void loadStructureJourneyStep(int step) {
        structureJourneyStep = Math.min(step, 5);
        keyPointsSection.setVisible(false); keyPointsSection.setManaged(false);
        String[] images = {"/images/Structure of Water Step 1.png", "/images/Structure of Water Step 2.png",
                "/images/Structure of Water Step 3.png", "/images/Structure of Water Step 4.png",
                "/images/Structure of Water Step 5.png", "/images/Structure of Water Step 6.png"};
        morphToImage(images[structureJourneyStep]);
        if (hotspotPulse != null) hotspotPulse.stop();
        moleculeHotspots.setVisible(false); moleculeHotspots.setManaged(false);
        hotspotInstruction.setVisible(false); hotspotInstruction.setManaged(false);
        hotspotInfoCard.setVisible(false); hotspotInfoCard.setManaged(false);
        journeyControls.setVisible(false); journeyControls.setManaged(false);
        mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT);

        String[] actions = {"ZOOM INTO BOTTLE  ›", "LOOK CLOSER AT WATER  ›",
                "SELECT A WATER MOLECULE  ›", "ZOOM INTO MOLECULE  ›", "SHOW HYDROGEN BONDS  ›"};
        boolean hasNextImage = structureJourneyStep < 5;
        structureImageActionHost.setVisible(hasNextImage);
        structureImageActionHost.setManaged(hasNextImage);
        structureImageActionHost.setTranslateY(0);
        structureImageActionButton.setDisable(false);
        if (hasNextImage) structureImageActionButton.setText(actions[structureJourneyStep]);
        else { showKeyPointsSummary(); completeCurrentLearningTopic(); }
    }

    /**
     * Morphs between storyboard frames by interpolating depth, focus and opacity.
     * This gives raster illustrations a continuous object-morph feel without a hard image swap.
     */
    private void morphToImage(String path) {
        var stream = getClass().getResourceAsStream(path);
        if (stream == null) { loadImageSafely(mainImageView, path); return; }

        Image previous = mainImageView.getImage();
        Image next = new Image(stream);
        resizeImageStageToImage(next);
        if (previous == null) { mainImageView.setImage(next); return; }

        ImageView outgoing = new ImageView(previous);
        outgoing.setFitWidth(mainImageView.getFitWidth());
        outgoing.setFitHeight(mainImageView.getFitHeight());
        outgoing.setPreserveRatio(true);
        outgoing.setSmooth(true);
        outgoing.setMouseTransparent(true);
        GaussianBlur outgoingBlur = new GaussianBlur(0);
        outgoing.setEffect(outgoingBlur);

        int mainIndex = imageStage.getChildren().indexOf(mainImageView);
        imageStage.getChildren().add(mainIndex + 1, outgoing);
        mainImageView.setImage(next);
        mainImageView.setOpacity(0);
        mainImageView.setScaleX(1.13); mainImageView.setScaleY(1.13);

        FadeTransition reveal = new FadeTransition(Duration.millis(680), mainImageView);
        reveal.setToValue(1); reveal.setInterpolator(Interpolator.EASE_BOTH);
        ScaleTransition focus = new ScaleTransition(Duration.millis(760), mainImageView);
        focus.setToX(1); focus.setToY(1); focus.setInterpolator(Interpolator.EASE_BOTH);

        FadeTransition dissolve = new FadeTransition(Duration.millis(610), outgoing);
        dissolve.setToValue(0); dissolve.setInterpolator(Interpolator.EASE_BOTH);
        ScaleTransition depth = new ScaleTransition(Duration.millis(720), outgoing);
        depth.setToX(1.18); depth.setToY(1.18); depth.setInterpolator(Interpolator.EASE_BOTH);
        Timeline defocus = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(outgoingBlur.radiusProperty(), 0)),
                new KeyFrame(Duration.millis(650), new KeyValue(outgoingBlur.radiusProperty(), 18, Interpolator.EASE_BOTH)));

        ParallelTransition morph = new ParallelTransition(reveal, focus, dissolve, depth, defocus);
        morph.setOnFinished(event -> imageStage.getChildren().remove(outgoing));
        morph.play();
    }

    private void configureStructureImageTarget() {
        if (structureJourneyStep >= 5) {
            mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT); return;
        }
        double[][] targets = {{302,245,50},{380,220,95},{377,165,100},{377,165,70},{380,112,76}};
        int expectedStep = structureJourneyStep;
        mainImageView.setCursor(Cursor.HAND);
        mainImageView.setOnMouseClicked(event -> {
            double[] target = targets[expectedStep];
            double dx = event.getX() - target[0], dy = event.getY() - target[1];
            if (dx * dx + dy * dy <= target[2] * target[2] && structureJourneyStep == expectedStep) {
                moleculeHotspots.getChildren().stream()
                        .filter(node -> node instanceof Button && node.isVisible())
                        .map(node -> (Button) node).findFirst().ifPresent(Button::fire);
            }
        });
    }

    private void loadSolventJourneyStep(int step) {
        solventJourneyStep = Math.min(step, 5);
        solventConclusionLabel.setVisible(false); solventConclusionLabel.setManaged(false);
        keyPointsSection.setVisible(false); keyPointsSection.setManaged(false);
        String[] images = {"/images/Universal Solvent Step 1.png", "/images/Universal Solvent Step 2.png",
                "/images/Universal Solvent Step 3.png", "/images/Universal Solvent Step 4.png",
                "/images/Universal Solvent Step 5.png", "/images/Universal Solvent Step 6.png"};
        morphToImage(images[solventJourneyStep]);
        if (hotspotPulse != null) hotspotPulse.stop();
        moleculeHotspots.setVisible(false); moleculeHotspots.setManaged(false);
        hotspotInstruction.setVisible(false); hotspotInstruction.setManaged(false);
        hotspotInfoCard.setVisible(false); hotspotInfoCard.setManaged(false);
        journeyControls.setVisible(false); journeyControls.setManaged(false);
        mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT);

        String[] actions = {"ZOOM INTO SALT  ›", "ADD SALT TO WATER  ›", "ZOOM INTO THE IONS  ›",
                "INSPECT ION ATTRACTION  ›", "VIEW DISSOLVED IONS  ›", "SALT DISSOLVED IN WATER  ✓"};
        String[] imageInfo = {"",
                "Salt (NaCl) consists of Na⁺ and Cl⁻ ions arranged in a crystal lattice.",
                "Water attracts ions at the crystal surface, causing the salt crystals to separate.",
                "Water molecules surround Na⁺ and Cl⁻ ions, forming hydration shells.",
                "Na⁺ attracts partially negative oxygen (δ⁻); Cl⁻ attracts partially positive hydrogen (δ⁺).",
                "Hydration shells keep the ions separated and dissolved, preventing them from clumping."};
        boolean showImageInfo = solventJourneyStep > 0;
        solventImageInfoLabel.setText(imageInfo[solventJourneyStep]);
        solventImageInfoLabel.setVisible(showImageInfo);
        solventImageInfoLabel.setManaged(showImageInfo);
        boolean finalImage = solventJourneyStep == 5;
        structureImageActionHost.setVisible(true);
        structureImageActionHost.setManaged(true);
        structureImageActionHost.setTranslateY(0);
        structureImageActionButton.setText(actions[solventJourneyStep]);
        structureImageActionButton.setDisable(finalImage);
        if (finalImage) {
            showKeyPointsSummary();
            showSolventConclusion();
            completeCurrentLearningTopic();
        }
    }

    private void configureSolventImageTarget() {
        if (solventJourneyStep >= 4) {
            mainImageView.setOnMouseClicked(null); mainImageView.setCursor(Cursor.DEFAULT); return;
        }
        double[][] targets = {{506,149,55},{377,244,65},{377,158,100},{391,115,115}};
        int expectedStep = solventJourneyStep;
        mainImageView.setCursor(Cursor.HAND);
        mainImageView.setOnMouseClicked(event -> {
            double[] target = targets[expectedStep];
            double dx = event.getX() - target[0], dy = event.getY() - target[1];
            if (dx * dx + dy * dy <= target[2] * target[2] && solventJourneyStep == expectedStep) {
                moleculeHotspots.getChildren().stream().filter(node -> node instanceof Button && node.isVisible())
                        .map(node -> (Button) node).findFirst().ifPresent(Button::fire);
            }
        });
    }

    private void prepareJourneyAdvance() {
        String[] structureLabels = {"ZOOM INTO BOTTLE  ›", "LOOK CLOSER AT WATER  ›",
                "CHOOSE A MOLECULE  ›", "ZOOM INTO MOLECULE  ›", "SHOW HYDROGEN BONDS  ›"};
        String[] solventLabels = {"ADD SALT TO WATER  ›", "STIR THE MIXTURE  ›",
                "ZOOM TO DISSOLVED IONS  ›", "INSPECT ION ORIENTATION  ›"};
        String[][] propertyLabels = {
                {}, {},
                {"COMPARE TEMPERATURES  ›", "INSPECT THE WATER  ›", "FOLLOW THE ENERGY  ›", "SHOW THE SUMMARY  ›"},
                {"FOLLOW A SWEAT DROPLET  ›", "INSPECT THE MOLECULES  ›", "START EVAPORATION  ›", "SHOW THE COOLING RESULT  ›"},
                {"COOL BELOW 4°C  ›", "INSPECT THE LATTICE  ›", "COMPARE SOLID AND LIQUID  ›", "LOOK BENEATH THE ICE  ›"},
                {"JOIN THE MOLECULES  ›", "FOLLOW THE WATER COLUMN  ›", "INSPECT THE XYLEM WALL  ›", "SHOW SURFACE TENSION  ›"}
        };
        journeyControls.setVisible(true); journeyControls.setManaged(true);
        if (currentIndex == 0) zoomDeeperButton.setText(structureLabels[structureJourneyStep]);
        else if (currentIndex == 1) zoomDeeperButton.setText(solventLabels[solventJourneyStep]);
        else zoomDeeperButton.setText(propertyLabels[currentIndex][propertyJourneyStep]);
        zoomDeeperButton.setVisible(true); zoomDeeperButton.setManaged(true);
        zoomDeeperButton.setOpacity(0); zoomDeeperButton.setTranslateX(10);
        FadeTransition fade = new FadeTransition(Duration.millis(260), zoomDeeperButton); fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(260), zoomDeeperButton);
        slide.setToX(0); slide.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(fade, slide).play();
    }

    @FXML private void advanceStructureJourney() {
        if (currentIndex == 0 && structureJourneyStep < 5) loadStructureJourneyStep(structureJourneyStep + 1);
        else if (currentIndex == 1 && solventJourneyStep < 5) loadSolventJourneyStep(solventJourneyStep + 1);
        else if (currentIndex == 2 && propertyJourneyStep < 5) loadPropertyJourneyStep(currentIndex, propertyJourneyStep + 1);
        else if (currentIndex == 3 && propertyJourneyStep < 7) loadPropertyJourneyStep(currentIndex, propertyJourneyStep + 1);
        else if (currentIndex == 4 && propertyJourneyStep < 5) loadPropertyJourneyStep(currentIndex, propertyJourneyStep + 1);
        else if (currentIndex == 5 && propertyJourneyStep < 4) loadPropertyJourneyStep(currentIndex, propertyJourneyStep + 1);
    }

    @FXML private void showPreviousJourneyLevel() {
        if (currentIndex == 0 && structureJourneyStep > 0) loadStructureJourneyStep(structureJourneyStep - 1);
        else if (currentIndex == 1 && solventJourneyStep > 0) loadSolventJourneyStep(solventJourneyStep - 1);
        else if (currentIndex >= 2 && currentIndex <= 5 && propertyJourneyStep > 0) loadPropertyJourneyStep(currentIndex, propertyJourneyStep - 1);
    }

    // Initial FXML handlers; configureImageInteraction replaces them with topic-specific actions.
    @FXML private void showOxygenInfo(javafx.event.ActionEvent e){}
    @FXML private void showHydrogenInfo(javafx.event.ActionEvent e){}
    @FXML private void showCovalentBondInfo(javafx.event.ActionEvent e){}
    @FXML private void showAngleInfo(javafx.event.ActionEvent e){}
    @FXML private void showHydrogenBondInfo(javafx.event.ActionEvent e){}
    private void updateSidebarSelection(){for(Node node:topicListBox.getChildren()){if(!(node instanceof Button button))continue;button.getStyleClass().setAll("topic-button");if(Integer.valueOf(currentIndex).equals(button.getUserData()))button.getStyleClass().add("topic-button-active");}}
    private void disableNavigation(){previousButton.setDisable(true);nextButton.setDisable(true);}
    @FXML private void showNextTopic(){if(currentIndex>=1 && currentIndex<5)showTopic(currentIndex+1);}
    @FXML private void showPreviousTopic(){if(currentIndex>0)showTopic(currentIndex-1);}
    @FXML private void returnToMenu(){SceneManager.switchScene("MainMenu.fxml");}
    @FXML private void backToNotesHome(){showHome();}
    @FXML private void backToProperties(){setLessonVisible(false);showPropertiesMenu();}
}
