//Jai Shree Ganesh
//Jai Shree Ram

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Main extends Application {

    // Variable Declaration
    // ----------------------------------------------------------------------------------------
    private List<ImageView> obstacles = new ArrayList<>();
    private List<ImageView> bullets = new ArrayList<>();

    static boolean isPaused = false;
    static boolean isFlying = false;
    static boolean isJumping = false;
    static boolean movingLeft = false;
    static boolean movingRight = false;
    static boolean bossScene = false;
    static boolean dadaFlag = false;
    static boolean otp = true;
    static boolean isSoundMuted = false;
    static boolean isMusicMuted = false;

    private MediaPlayer jumpSoundPlayer;
    private MediaPlayer hurtSoundPlayer;

    static int imgindex = 0;
    static int count = 10;
    static int highScore = 0;
    static int heart = 10;
    static int bossHeart = 100;
    public static int money = 0;

    static double marioVelocityY = 0;
    static double marioVelocityY2 = 0;
    static long flyingStartTime = 0;
    final static long flyingDuration = 6000;

    private BorderPane mainPane;
    private BorderPane settingsLayout;

    private Pane root;
    private Pane root2;

    private ImageView dada;
    private ImageView boss;
    private ImageView mario;
    private ImageView mario2;
    private ImageView bullet;
    private Image[] imgobj;

    private Label countLabel;
    private Label pauseLabel;

    Text text;

    Scene scend;
    Scene temp;

    AnimationTimer timer;
    AnimationTimer timer2;

    Background background_obj = new Background();
    WriteIntegersToFile writer_obj = new WriteIntegersToFile();

    private Stage primaryStage;

    @Override
    // Start Method
    // ------------------------------------------------------------------------------------------------
    public void start(Stage primaryStage) {

        // First Primary Stage Set

        this.primaryStage = primaryStage;
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");

        // Main Pane Set for Intro Scene

        mainPane = new BorderPane();
        background_obj.setDarkGradientBackground(mainPane);

        // LOGO IMAGE
        Image logoImage = new Image("images/mainLogo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(300);
        logoImageView.setFitHeight(300);

        // LOGO TEXT
        Text loadingText = new Text("Loading...");
        loadingText.setFill(Color.WHITE);
        loadingText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // START BUTTON
        Button startButton = createStyledButton("Start");
        startButton.setOnAction(event -> {
            Scene loadingScene = createLoadingScene();
            primaryStage.setScene(loadingScene);
            primaryStage.show();
            Scene gameScene = createGameScene();

            // Load assets or prepare the game scene in a background task
            CompletableFuture<Void> loadingTask = CompletableFuture.runAsync(() -> {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    primaryStage.setScene(gameScene);
                    primaryStage.setMaximized(true);
                });
            });

            // Show the loading screen until the game scene is ready
            loadingTask.thenRun(() -> {
                Platform.runLater(() -> primaryStage.setScene(gameScene));
            });
        });

        mainPane.setCenter(startButton);

        // CONTROL BUTTON
        Button contButton = new Button("Controls");
        contButton
                .setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        contButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        contButton.setPrefWidth(200);
        contButton.setPrefHeight(40);
        contButton.setOnAction(event -> {

        });

        // INTRO BUTTON
        Button introButton = new Button("Intro");
        introButton
                .setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        introButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        introButton.setPrefWidth(200);
        introButton.setPrefHeight(40);

        // Create a stack pane for the logo and loading text
        StackPane logoPane = new StackPane();
        logoPane.getChildren().addAll(logoImageView, loadingText);
        StackPane.setAlignment(loadingText, Pos.BOTTOM_CENTER);
        StackPane.setMargin(loadingText, new Insets(0, 0, 100, 0));

        // Add the logo to the center initially
        mainPane.setCenter(logoPane);

        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setVgap(20);
        centerPane.add(startButton, 0, 0);
        centerPane.add(contButton, 0, 1);

        // Create the fade transition for the logo
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), logoPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Start the fade in transition
        fadeIn.play();

        // SETTING BUTTON
        Button settingsButton = createIconButton("images/setting.png");
        settingsButton.setOnAction(event -> {
            try {
                toggleSettings();
            } catch (Exception e) {

                e.printStackTrace();
            }
        });

        // STORE BUTTON
        Button storeButton = createIconButton("images/storeIcon.png");
        storeButton.setOnAction(event -> {
            Store store = new Store();
            Scene storeScene = store.createStoreScene(primaryStage, temp); // Assuming 'temp' is the previous scene
            primaryStage.setScene(storeScene);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreen(true);
        });

        // FADE IN FADE OUT TRANSITION
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            // After 5 seconds, move to the main page
            VBox mainPageContent = new VBox(20, createButtonRow());
            mainPageContent.setAlignment(Pos.CENTER);
            mainPane.setCenter(mainPageContent);
            mainPane.setLeft(storeButton); // Set the store button on top left corner
            BorderPane.setAlignment(storeButton, Pos.TOP_LEFT);
            BorderPane.setMargin(storeButton, new Insets(10));
            mainPane.setRight(settingsButton); // Set the settings button on top right corner
            BorderPane.setAlignment(settingsButton, Pos.TOP_RIGHT);
            BorderPane.setMargin(settingsButton, new Insets(10));
            background_obj.setMainPageBackground(mainPane);
        }));
        timeline.play();

        temp = new Scene(mainPane);

        primaryStage.setScene(temp);
        primaryStage.setTitle("Game");
        primaryStage.getIcons().add(new Image("images/smallLogo.png"));
        primaryStage.show();
    }

    // Loading Scene
    // ---------------------------------------------------------------------------------------------
    private Scene createLoadingScene() {

        BorderPane loadingPane = new BorderPane();
        background_obj.setDarkGradientBackground(loadingPane);

        // Create a loading label
        Label loadingLabel = new Label("Loading...");
        loadingLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        BorderPane.setAlignment(loadingLabel, Pos.CENTER);

        // Add the loading label to the center of the loadingPane
        loadingPane.setCenter(loadingLabel);

        // Create a Scene with the loadingPane as its root
        Scene loadingScene = new Scene(loadingPane); // Set a size for the loading scene if needed
        primaryStage.setScene(loadingScene);

        primaryStage.setMaximized(true);

        return loadingScene;
    }

    // Create Buttons With CSS
    // ------------------------------------------------------------------------------------------------
    private Button createStyledButton(String text) {

        Button button = new Button(text);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        return button;
    }

    // Buttons For Main Intro Scene
    // ------------------------------------------------------------------------------------------------
    private VBox createButtonRow() {

        Button startButton = createStyledButton("Start");
        startButton.setOnAction(event -> {
            Scene gameScene = createGameScene();
            CompletableFuture<Void> loadingTask = CompletableFuture.runAsync(() -> {

                Platform.runLater(() -> {
                    primaryStage.setScene(gameScene);
                    primaryStage.setMaximized(true);
                });
            });

            // Show the loading screen until the game scene is ready
            loadingTask.thenRun(() -> {
                Platform.runLater(() -> primaryStage.setScene(gameScene));
            });
        });

        Button controlsButton = createStyledButton("Controls");
        controlsButton.setOnAction(event -> {
            Controls controlsScene = new Controls();
            Scene previousScene = primaryStage.getScene();
            Scene controlsSceneInstance = controlsScene.createStoreScene(primaryStage, previousScene);
            primaryStage.setScene(controlsSceneInstance);
            primaryStage.setFullScreen(true); // Set Controls stage to full-screen mode
        });

        Button introButton = createStyledButton("Intro");
        introButton.setOnAction(event -> {
            playVideo(primaryStage);
        });
        VBox buttonRow = new VBox(20, startButton, controlsButton, introButton);
        buttonRow.setAlignment(Pos.CENTER);
        return buttonRow;
    }

    // Play Intro Video
    // ------------------------------------------------------------------------------------------------
    private void playVideo(Stage primaryStage) {
        Media media = new Media(getClass().getResource("Intro.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(primaryStage.getWidth());
        mediaView.setFitHeight(primaryStage.getHeight());

        StackPane videoPane = new StackPane(mediaView);

        Button quitButton = new Button("Quit");
        quitButton.setPrefWidth(100);
        quitButton.setPrefHeight(40);

        quitButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 20px;");
        quitButton.setOnAction(event -> {
            mediaPlayer.stop();
            Stage stage = (Stage) quitButton.getScene().getWindow();
            stage.setScene(temp);
            stage.setFullScreen(true);
            primaryStage.setFullScreen(true);
        });

        // Set position of Quit button to top-right corner
        StackPane.setAlignment(quitButton, Pos.TOP_RIGHT);
        StackPane.setMargin(quitButton, new Insets(10));

        videoPane.getChildren().add(quitButton);

        Scene videoScene = new Scene(videoPane, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(videoScene);
        primaryStage.setMaximized(true);

        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            Stage stage = (Stage) quitButton.getScene().getWindow();
            stage.setScene(temp);
            stage.setFullScreen(true);
            primaryStage.setFullScreen(true);
        });
    }

    // Icon Button Creation
    // ------------------------------------------------------------------------------------------------
    private Button createIconButton(String iconFileName) {

        Button button = new Button();
        Image iconImage = new Image(iconFileName);
        ImageView iconImageView = new ImageView(iconImage);
        iconImageView.setFitWidth(32);
        iconImageView.setFitHeight(32);
        button.setGraphic(iconImageView);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF);");
        return button;
    }

    // Change to Setting Layout <-> Intro Layout
    // ------------------------------------------------------------------------------------------------
    private void toggleSettings() throws Exception {

        try {
            if (settingsLayout == null) {
                settingsLayout = createSettingsLayout();
            }
            if (!mainPane.getChildren().contains(settingsLayout)) {
                mainPane.setCenter(settingsLayout);
            } else {
                mainPane.setCenter(createButtonRow());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Setting Scene
    // ----------------------------------------------------------------------------------------------
    private BorderPane createSettingsLayout() {

        BorderPane settingsLayout = new BorderPane();
        settingsLayout.setStyle("-fx-background-image: url('startBack.png'); -fx-background-size: cover;");

        // Create sound toggle button
        Button soundToggleButton = createStyledButton("Sound : UnMuted");
        soundToggleButton.setOnMouseClicked(event -> {
            isSoundMuted = !isSoundMuted;
            if (isSoundMuted) {
                soundToggleButton.setText("Sound: Muted");
            } else {
                String jumpSoundFile = "jump.mp3";
                Media jumpSound = new Media(getClass().getResource(jumpSoundFile).toString());
                jumpSoundPlayer = new MediaPlayer(jumpSound);

                String hurtSounfFile = "hurt.mp3";
                Media hurtSound = new Media(getClass().getResource(hurtSounfFile).toString());
                hurtSoundPlayer = new MediaPlayer(hurtSound);
                soundToggleButton.setText("Sound: Unmuted");
            }
        });

        // Create music toggle button
        Button musicToggleButton = createStyledButton("Music : UnMuted");
        musicToggleButton.setOnMouseClicked(event -> {
            isMusicMuted = !isMusicMuted;
            if (isMusicMuted) {

                musicToggleButton.setText("Music: Muted");
            } else {

                musicToggleButton.setText("Music: Unmuted");
            }
        });

        // Create back button
        Button backButton = createStyledButton("Back to Home");
        backButton.setOnAction(event -> {
            background_obj.setMainPageBackground(mainPane);
            mainPane.setCenter(createButtonRow());
        });

        VBox settingsButtons = new VBox(20, soundToggleButton, musicToggleButton, backButton);
        settingsButtons.setAlignment(Pos.CENTER);
        settingsLayout.setCenter(settingsButtons);

        return settingsLayout;

    }

    // Main Game Scene
    // Scene-----------------------------------------------------------------------------------------
    private Scene createGameScene() {

        count = 0;
        heart = 15;
        root = new Pane();
        Scene gameScene = new Scene(root, Color.LIGHTBLUE);
        Image backgroundImage = new Image("images/back2.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(primaryStage.getWidth());
        backgroundImageView.setFitHeight(primaryStage.getHeight());
        root.getChildren().add(backgroundImageView);

        if (!isSoundMuted) {
            String jumpSoundFile = "jump.mp3";
            Media jumpSound = new Media(getClass().getResource(jumpSoundFile).toString());
            jumpSoundPlayer = new MediaPlayer(jumpSound);

            String hurtSounfFile = "hurt.mp3";
            Media hurtSound = new Media(getClass().getResource(hurtSounfFile).toString());
            hurtSoundPlayer = new MediaPlayer(hurtSound);
        }

        imgobj = new Image[] {
                new Image("images/charstopl.png"),
                new Image("images/charstop.png"),
                new Image("images/charflying.png")
        };

        mario = new ImageView(imgobj[1]);
        mario.setFitWidth(110);
        mario.setFitHeight(150);
        mario.setX(100);
        mario.setY(560);

        root.getChildren().add(mario);

        StackPane countPane = new StackPane();

        countLabel = new Label(" ");
        countLabel.setStyle("-fx-font-size: 20px;-fx-font-weight: BOLD");
        countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        countLabel.setTextFill(Color.WHITE);
        countLabel.setTranslateY(40);
        countLabel.setTranslateX(2);

        ImageView icon1 = new ImageView(new Image("images/bossHealth.png"));
        icon1.setFitWidth(72);
        icon1.setFitHeight(50);

        Text t2 = new Text(" X " + heart);
        t2.setFill(Color.WHITE);
        t2.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        t2.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Create HBox for icon and text
        HBox iconTextBox1 = new HBox(icon1, t2);
        iconTextBox1.setAlignment(Pos.CENTER_RIGHT);
        iconTextBox1.setSpacing(10);

        // Create HBox for top right buttons
        HBox topRightBox = new HBox(iconTextBox1);
        topRightBox.setAlignment(Pos.TOP_RIGHT);
        topRightBox.setLayoutX(primaryStage.getWidth() - 200);
        topRightBox.setLayoutY(10);

        countPane.getChildren().addAll(countLabel, iconTextBox1, topRightBox);
        countPane.setLayoutX(0);
        countPane.setLayoutY(0);

        StackPane.setAlignment(countLabel, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(iconTextBox1, javafx.geometry.Pos.BOTTOM_RIGHT);
        root.getChildren().add(countPane);

        pauseLabel = new Label("");
        pauseLabel.setTextFill(Color.WHITE);
        pauseLabel.setLayoutX(700);
        pauseLabel.setLayoutY(10);
        pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        root.getChildren().add(pauseLabel);

        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !isJumping && !isPaused) {
                marioVelocityY = -25;
                isJumping = true;
                playJumpSound();
            }
            if (event.getCode() == KeyCode.LEFT && !isPaused) {
                movingLeft = true;
                setCurrentImageIndex(0, 1);
                mario.setFitWidth(110);
                mario.setFitHeight(150);
            }
            if (event.getCode() == KeyCode.RIGHT && !isPaused) {
                movingRight = true;
                setCurrentImageIndex(1, 1);
                mario.setFitWidth(110);
                mario.setFitHeight(150);
            }
            if (event.getCode() == KeyCode.P) {
                isPaused = !isPaused;
                pauseLabel.setText(isPaused ? "PAUSED" : "");
            }
        });

        gameScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                movingLeft = false;
                setCurrentImageIndex(0, 1);
                mario.setFitWidth(110);
                mario.setFitHeight(150);
            }
            if (event.getCode() == KeyCode.RIGHT) {
                movingRight = false;
                setCurrentImageIndex(1, 1);
                mario.setFitWidth(110);
                mario.setFitHeight(150);
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (!isPaused) {
                    update();
                    countLabel.setText(" ");
                    t2.setText(" X " + heart);
                    if (isFlying) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - flyingStartTime > flyingDuration) {
                            isFlying = false;
                            setCurrentImageIndex(0, 1);
                        } else {
                            // Perform flying behavior
                            double waveY = Math.sin(currentTime * 0.01) * 30;
                            mario.setY(mario.getY() - waveY);
                            marioVelocityY = waveY;
                            if (mario.getY() < 150) {
                                mario.setY(150);
                            }
                            setCurrentImageIndex(2, 1);
                        }
                    }
                }
            }
        };
        timer.start();

        return gameScene;
    }

    // Jump Sound Play
    // ------------------------------------------------------------------------------------------------
    private void playJumpSound() {
        if (jumpSoundPlayer != null) {
            jumpSoundPlayer.stop();
            jumpSoundPlayer.play();
        }
    }

    // Hurt Sound Play
    // ------------------------------------------------------------------------------------------------
    private void playHurtSound() {
        if (hurtSoundPlayer != null) {
            hurtSoundPlayer.stop();
            hurtSoundPlayer.play();
        }
    }

    // Main Game Scene Update
    // -----------------------------------------------------------------------------------
    private void update() throws ArithmeticException {

        try {
            if (heart > highScore) {
                highScore = heart;
            }
            marioVelocityY += 1.2; // Gravity
            double newY = mario.getY() + marioVelocityY;

            if (newY > (primaryStage.getHeight() - (2.6*100))) { // Ground level
                newY =  (primaryStage.getHeight() - (2.6*100));
                marioVelocityY = 0;
                isJumping = false;
            }

            if (isFlying) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - flyingStartTime <= flyingDuration) {
                    double frequencyMultiplier = 0.005;
                    double waveY = Math.sin(currentTime * frequencyMultiplier) * 30;
                    newY = 290 - waveY;
                    mario.setY(newY);
                    marioVelocityY = waveY;
                    setCurrentImageIndex(2, 1);
                } else {
                    isFlying = false;
                    setCurrentImageIndex(1, 1);
                }
            } else {
                // Apply regular movement if not flying
                mario.setY(newY);
            }

            if (movingLeft && mario.getX() > 0) {
                mario.setX(mario.getX() - 5);
            }

            if (movingRight) {
                // Check if the player's X coordinate is within the left boundary
                if (mario.getX() < primaryStage.getWidth()) {
                    mario.setX(mario.getX() + 5);
                }
            }

            mario.setY(newY);

            // Check for collision with obstacles
            Iterator<ImageView> iterator = obstacles.iterator();
            while (iterator.hasNext()) {
                ImageView obstacle = iterator.next();
                if (obstacle.getBoundsInParent().intersects(mario.getBoundsInParent())) {
                    if (obstacle.getImage().getUrl().contains("dimond")) {
                        count++;
                        root.getChildren().remove(obstacle);
                        iterator.remove();
                        break;

                    } else if (obstacle.getImage().getUrl().contains("blue")) {

                        playHurtSound();
                        heart--;
                        root.getChildren().remove(obstacle);
                        iterator.remove();
                        break;
                    } else if (obstacle.getImage().getUrl().contains("wing")) {
                        setCurrentImageIndex(2, 1);
                        root.getChildren().remove(obstacle);
                        iterator.remove();
                        setCurrentImageIndex(2, 1);
                        isFlying = true;
                        flyingStartTime = System.currentTimeMillis();
                        break;
                    } else if (obstacle.getImage().getUrl().contains("boss")) {
                        root.getChildren().remove(obstacle);
                        iterator.remove();
                        heart++;
                        break;
                    }
                }
            }

            // Check if count reaches 0 to end the game
            if (heart <= 0) {
                resetGame();
                money = money + count;
                writer_obj.writeMoney();
                primaryStage.setScene(createEndScene());
                primaryStage.setFullScreen(true);
            }

            if (heart >= 15) {
                bossScene = true;
            }
            // Add obstacles periodically
            if (bossScene == false) {

                if (Math.random() < 0.008) {

                    Image obstacleImage;
                    ImageView obstacle;
                    Random rand = new Random();
                    int randomNum2 = rand.nextInt((10 - 0) + 1) + 0;

                    Image bonus = new Image("images/dimond.png");
                    ImageView bonusobj = new ImageView(bonus);
                    Image wingImage = new Image("images/wing3.png");
                    ImageView wingobj = new ImageView(wingImage);
                    if (Math.random() < 0.8) {

                        int randomNum = rand.nextInt((10 - 0) + 1) + 0;
                        if (randomNum > 5) {
                            obstacleImage = new Image("images/blue1.png");
                            obstacle = new ImageView(obstacleImage);
                        } else {
                            obstacleImage = new Image("images/blue2.png");
                            obstacle = new ImageView(obstacleImage);
                        }
                    } else {
                        obstacleImage = new Image("images/dimond.png");
                        obstacle = new ImageView(obstacleImage);
                    }
                    if (randomNum2 >= 0 && randomNum2 < 5) {
                        wingobj.setX(800);
                        wingobj.setY(200);
                        obstacles.add(wingobj);
                        root.getChildren().add(wingobj);
                    } else if (randomNum2 >= 5 && randomNum2 <= 7) {
                        obstacleImage = new Image("images/bossHealth.png");
                        obstacle = new ImageView(obstacleImage);
                    } else {
                        root.getChildren().remove(wingobj);
                    }
                    if (isFlying) {
                        if (randomNum2 >= 0) {
                            bonusobj.setX(800);
                            bonusobj.setY(200);
                            bonusobj.setFitHeight(70);
                            bonusobj.setFitWidth(70);
                            obstacles.add(bonusobj);
                            root.getChildren().add(bonusobj);
                        } else {
                            root.getChildren().remove(wingobj);
                        }
                    }
                    obstacle.setFitWidth(150);
                    obstacle.setFitHeight(100);
                    obstacle.setX(primaryStage.getWidth());
                    obstacle.setY(620);

                    bonusobj.setFitHeight(70);
                    bonusobj.setFitWidth(70);
                    bonusobj.setX(primaryStage.getWidth());
                    bonusobj.setY(430);

                    wingobj.setFitHeight(70);
                    wingobj.setFitWidth(70);
                    wingobj.setX(primaryStage.getWidth());
                    wingobj.setY(430);

                    obstacles.add(obstacle);
                    root.getChildren().add(obstacle);

                }
            } else {

                if (otp) {

                    text = new Text();
                    text.setFont(new Font(20));
                    text.setWrappingWidth(200);
                    text.setTextAlignment(TextAlignment.JUSTIFY);
                    text.setText("Ho Ho Ho, How are you my hero");
                    text.setFill(Color.WHITE);

                    text.setX(primaryStage.getWidth() - 50);
                    text.setY(360);

                    dada = new ImageView("images/dada.png");
                    dada.setFitWidth(110);
                    dada.setFitHeight(150);
                    dada.setX(primaryStage.getWidth());
                    dada.setY(360);
                    dadaFlag = true;
                    obstacles.clear();
                    obstacles.add(dada);
                    root.getChildren().add(dada);
                    root.getChildren().add(text);
                    otp = false;
                }
            }
            // Move obstacles to the left
            for (ImageView obstacle : obstacles) {
                obstacle.setX(obstacle.getX() - 5);
                if (dadaFlag) {
                    if (dada.getX() > 300) {
                        text.setX(text.getX() - 6);
                        dada.setX(dada.getX() - 2);
                    } else {
                        dada.setX(300);
                        text.setX(380);
                        text.setText("ARE YOU READY !!");
                        Button startButton = createStyledButton("Ready?");
                        root.getChildren().add(startButton);
                        startButton.setAlignment(Pos.CENTER);
                        startButton.setOnAction(event -> {

                            Scene gameScene = createMegaBossScene();
                            primaryStage.setScene(gameScene);
                            primaryStage.setMaximized(true);
                        });
                    }
                }
            }
            // Prevent overlapping obstacles
            for (int i = 0; i < obstacles.size() - 1; i++) {
                for (int j = i + 1; j < obstacles.size(); j++) {
                    ImageView ob1 = obstacles.get(i);
                    ImageView ob2 = obstacles.get(j);
                    if (ob1.getBoundsInParent().intersects(ob2.getBoundsInParent())) {
                        ob2.setX(ob1.getX() + ob1.getFitWidth() + 50);
                    }
                }
            }
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
        // Remove obstacles that are out of the screen
        obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getFitWidth() < 0);
    }

    // Boss Scene Update
    // ------------------------------------------------------------------------------------------
    private void MegaUpdate() throws ArithmeticException {

        marioVelocityY2 += 1.2;
        double newY = mario2.getY() + marioVelocityY2;

        try {
            if (newY >  (primaryStage.getHeight() - (2.6*100))) { // Ground level
                newY =  (primaryStage.getHeight() - (2.6*100));
                marioVelocityY2 = 0;
                isJumping = false;
            }
            if (movingLeft && mario2.getX() > 0) {
                mario2.setX(mario2.getX() - 5);
            }

            if (movingRight) {
                // Check if the player's X coordinate is within the left boundary
                if (mario2.getX() < primaryStage.getWidth()) {
                    mario2.setX(mario2.getX() + 5);
                }
            }
            mario2.setY(newY);

            // Check for collision with obstacles
            Iterator<ImageView> iterator = obstacles.iterator();
            while (iterator.hasNext()) {
                ImageView obstacle = iterator.next();
                if (obstacle.getBoundsInParent().intersects(mario2.getBoundsInParent())) {
                    if (obstacle.getImage().getUrl().contains("bossHealth")) {
                        heart++;
                        root2.getChildren().remove(obstacle);
                        iterator.remove(); // Use iterator to remove the obstacle
                        break; // Exit the loop after removing the obstacle
                    } else if (obstacle.getImage().getUrl().contains("fire")) {
                        // Collided with blue obstacle, decrease count and remove obstacle
                        playHurtSound();
                        heart--;
                        root2.getChildren().remove(obstacle);
                        iterator.remove(); // Use iterator to remove the obstacle
                        break;
                    } else if (obstacle.getImage().getUrl().contains("minion")) {

                        playHurtSound();
                        heart--;
                        root2.getChildren().remove(obstacle);
                        iterator.remove(); // Use iterator to remove the obstacle
                        break;
                    }

                }
            }

            Iterator<ImageView> iterator2 = bullets.iterator();
            if (iterator2 != null) {
                while (iterator2.hasNext()) {
                    ImageView gun = iterator2.next();
                    if (gun.getBoundsInParent().intersects(boss.getBoundsInParent())) {
                        if (gun.getImage().getUrl().contains("ice")) {
                            bossHeart--;
                            root2.getChildren().remove(gun);
                            iterator2.remove();
                            break;
                        }
                    }
                }
            }

            if (Math.random() < 0.008) {
                Image obstacleImage;
                ImageView obstacle;
                Random rand = new Random();

                int height = (int) primaryStage.getHeight();
                int randomNum2 = rand.nextInt((height - 400) + 1) + 300;
                if (Math.random() < 1) {

                    int randomNum = rand.nextInt((10 - 0) + 1) + 0;
                    if (randomNum < 8) {
                        obstacleImage = new Image("images/fireball.png");
                        obstacle = new ImageView(obstacleImage);
                        obstacle.setFitWidth(90);
                        obstacle.setFitHeight(90);
                        obstacle.setX(boss.getX());
                        obstacle.setY(randomNum2);
                    } else if (randomNum2 < 5) {
                        obstacleImage = new Image("images/minion1.png");
                        obstacle = new ImageView(obstacleImage);
                        obstacle.setFitWidth(90);
                        obstacle.setFitHeight(110);
                        obstacle.setX(boss.getX());
                        obstacle.setY(590);
                    } else {
                        obstacleImage = new Image("images/minion2.png");
                        obstacle = new ImageView(obstacleImage);
                        obstacle.setFitWidth(90);
                        obstacle.setFitHeight(110);
                        obstacle.setX(boss.getX());
                        obstacle.setY(590);
                    }
                } else {
                    obstacleImage = new Image("images/bossHealth.png");
                    obstacle = new ImageView(obstacleImage);
                    obstacle.setFitWidth(90);
                    obstacle.setFitHeight(90);
                    obstacle.setX(boss.getX());
                    obstacle.setY(randomNum2);
                }

                obstacles.add(obstacle);
                root2.getChildren().add(obstacle);
            }

            for (ImageView obstacle : obstacles) {
                obstacle.setX(obstacle.getX() - 6);
            }
            for (ImageView gun : bullets) {
                gun.setX(gun.getX() + 8);
            }

            // Prevent overlapping obstacles
            for (int i = 0; i < obstacles.size() - 1; i++) {
                for (int j = i + 1; j < obstacles.size(); j++) {
                    ImageView ob1 = obstacles.get(i);
                    ImageView ob2 = obstacles.get(j);
                    if (ob1.getBoundsInParent().intersects(ob2.getBoundsInParent())) {
                        ob2.setX(ob1.getX() + ob1.getFitWidth() + 50);
                    }
                }
            }
            // Remove obstacles that are out of the screen
            obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getFitWidth() < 0);

            if (bossHeart <= 0) {
                timer2.stop();
                try {
                    playWonVideo(primaryStage);
                } catch (Exception e) {

                    e.printStackTrace();
                }

            } else if (heart <= 0) {
                timer2.stop();
                playLossVideo(primaryStage);
            }
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
    }

    // Win video
    // -------------------------------------------------------------------------------------------
    private void playWonVideo(Stage primaryStage) throws Exception {

        try {
            Media media = new Media(getClass().getResource("bossDefeated.mp4").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(primaryStage.getWidth());
            mediaView.setFitHeight(primaryStage.getHeight());

            StackPane videoPane = new StackPane(mediaView);

            Button quitButton = new Button("Quit");
            quitButton.setPrefWidth(100);
            quitButton.setPrefHeight(40);
            quitButton.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 20px;");
            quitButton.setOnAction(event -> {
                mediaPlayer.stop();
                resetGame();
                bossScene = false;
                dadaFlag = false;
                otp = true;
                Stage stage = (Stage) quitButton.getScene().getWindow();
                stage.setScene(temp);
                stage.setFullScreen(true);
                primaryStage.setFullScreen(true);
            });

            // Set position of Quit button to top-right corner
            StackPane.setAlignment(quitButton, Pos.TOP_RIGHT);
            StackPane.setMargin(quitButton, new Insets(10));

            videoPane.getChildren().add(quitButton);

            Scene videoScene = new Scene(videoPane, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(videoScene);
            primaryStage.setFullScreen(true);

            mediaPlayer.play();

            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop();
                resetGame();
                bossScene = false;
                dadaFlag = false;
                otp = true;
                Stage stage = (Stage) quitButton.getScene().getWindow();
                stage.setScene(temp);
                stage.setFullScreen(true);
                primaryStage.setFullScreen(true);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Loss video
    // -----------------------------------------------------------------------------------------------
    private void playLossVideo(Stage primaryStage) {
        try {
            Media media = new Media(getClass().getResource("bossWon.mp4").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(primaryStage.getWidth());
            mediaView.setFitHeight(primaryStage.getHeight());

            StackPane videoPane = new StackPane(mediaView);

            Button quitButton = new Button("Quit");
            quitButton.setPrefWidth(100);
            quitButton.setPrefHeight(40);
            quitButton.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 20px;");
            quitButton.setOnAction(event -> {
                mediaPlayer.stop();
                resetGame();
                Stage stage = (Stage) quitButton.getScene().getWindow();
                stage.setScene(temp);
                stage.setFullScreen(true);
                bossScene = false;
                dadaFlag = false;
                otp = true;
                primaryStage.setFullScreen(true);
            });

            StackPane.setAlignment(quitButton, Pos.TOP_RIGHT);
            StackPane.setMargin(quitButton, new Insets(10));

            videoPane.getChildren().add(quitButton);

            Scene videoScene = new Scene(videoPane, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(videoScene);
            primaryStage.setFullScreen(true);

            mediaPlayer.play();

            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop();
                resetGame();
                bossScene = false;
                dadaFlag = false;
                otp = true;
                Stage stage = (Stage) quitButton.getScene().getWindow();
                stage.setScene(temp);
                stage.setFullScreen(true);
                primaryStage.setFullScreen(true);
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // Mega Boss Scene
    // ------------------------------------------------------------------------------------------------
    private Scene createMegaBossScene() {

        root2 = new Pane();
        heart = 30;
        bossHeart = 300;
        Scene megaScene = new Scene(root2, Color.LIGHTBLUE);
        Image backgroundImage = new Image("images/back11.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        root2.getChildren().add(backgroundImageView);
        backgroundImageView.setFitWidth(primaryStage.getWidth());
        backgroundImageView.setFitHeight(primaryStage.getHeight());

        if (!isSoundMuted) {
            String jumpSoundFile = "jump.mp3";
            Media jumpSound = new Media(getClass().getResource(jumpSoundFile).toString());
            jumpSoundPlayer = new MediaPlayer(jumpSound);

            String hurtSounfFile = "hurt.mp3";
            Media hurtSound = new Media(getClass().getResource(hurtSounfFile).toString());
            hurtSoundPlayer = new MediaPlayer(hurtSound);
        }

        ImageView icon = new ImageView(new Image("images/bossHealth.png"));
        icon.setFitWidth(50);
        icon.setFitHeight(50);

        Text t1 = new Text(" X " + heart);
        t1.setFill(Color.WHITE);
        t1.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        t1.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Create HBox for icon and text
        HBox iconTextBox = new HBox(icon, t1);
        iconTextBox.setAlignment(Pos.CENTER_LEFT);
        iconTextBox.setSpacing(10);

        // Create HBox for top left and top right buttons
        HBox topLeftBox = new HBox(iconTextBox);
        topLeftBox.setAlignment(Pos.TOP_LEFT);
        topLeftBox.setLayoutX(10);
        topLeftBox.setLayoutY(10);

        // button for boss hearts
        ImageView icon1 = new ImageView(new Image("images/bossHealth2.png"));
        icon1.setFitWidth(72);
        icon1.setFitHeight(50);

        Text t2 = new Text(" X " + bossHeart);
        t2.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        t2.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Create HBox for icon and text
        HBox iconTextBox1 = new HBox(icon1, t2);
        t2.setFill(Color.WHITE);
        iconTextBox1.setAlignment(Pos.CENTER_RIGHT);
        iconTextBox1.setSpacing(10); // Adjust spacing as needed

        // Create HBox for top right buttons
        HBox topRightBox = new HBox(iconTextBox1);
        topRightBox.setAlignment(Pos.TOP_RIGHT);
        topRightBox.setLayoutX(primaryStage.getWidth() - 200); // Adjust this as necessary
        topRightBox.setLayoutY(10);

        // Adding the HBoxes to root2 Pane
        root2.getChildren().addAll(topLeftBox, topRightBox);

        imgobj = new Image[] {
                new Image("images/charstopl.png"),
                new Image("images/charstop.png"),
                new Image("images/charflying.png")
        };

        mario2 = new ImageView(imgobj[1]);
        mario2.setFitWidth(110);
        mario2.setFitHeight(150);
        mario2.setX(100);
        mario2.setY(540);

        boss = new ImageView("images/boss.png");
        boss.setFitHeight(500);
        boss.setFitWidth(300);
        boss.setX(primaryStage.getWidth() - 250);
        boss.setY(200);

        root2.getChildren().add(boss);
        root2.getChildren().add(mario2);

        pauseLabel = new Label("");
        pauseLabel.setTextFill(Color.BLACK);
        pauseLabel.setLayoutX(700);
        pauseLabel.setLayoutY(10);
        pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        root2.getChildren().add(pauseLabel);

        megaScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !isJumping && !isPaused) {
                marioVelocityY2 = -25;
                isJumping = true;
                playJumpSound();
            }
            if (event.getCode() == KeyCode.LEFT && !isPaused) {
                movingLeft = true;
                setCurrentImageIndex(0, 0);
                mario2.setFitWidth(110);
                mario2.setFitHeight(150);
            }
            if (event.getCode() == KeyCode.RIGHT && !isPaused) {
                movingRight = true;
                setCurrentImageIndex(1, 0);
                mario2.setFitWidth(110);
                mario2.setFitHeight(150);
            }
            if (event.getCode() == KeyCode.P) {
                isPaused = !isPaused;
                pauseLabel.setText(isPaused ? "PAUSED" : "");
            }
            if (event.getCode() == KeyCode.F) {
                bullet = new ImageView("images/iceball.png");
                bullet.setX(mario2.getX());
                bullet.setY(mario2.getY());
                bullet.setFitHeight(70);
                bullet.setFitWidth(70);
                bullets.add(bullet);
                root2.getChildren().add(bullet);
            }
        });

        megaScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                movingLeft = false;
                setCurrentImageIndex(0, 0);
                mario2.setFitWidth(110);
                mario2.setFitHeight(150);
            }
            if (event.getCode() == KeyCode.RIGHT) {
                movingRight = false;
                setCurrentImageIndex(1, 0);
                mario2.setFitWidth(110);
                mario2.setFitHeight(150);
            }
        });

        timer2 = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    MegaUpdate();
                    t1.setText(" X " + heart);
                    t2.setText(" X " + bossHeart);
                }
            }
        };
        timer.stop();
        timer2.start();

        return megaScene;
    }

    // Change Image of Character When Event Occurs
    // ------------------------------------------------------------------------------------------------
    private void setCurrentImageIndex(int index, int scene) {
        if (index >= 0 && index < imgobj.length) {
            imgindex = index;
            if (scene == 1) {
                mario.setImage(imgobj[imgindex]);
            } else if (scene == 0) {
                mario2.setImage(imgobj[imgindex]);
            }
        }
    }

    // Reset the Game
    // ------------------------------------------------------------------------------------------------
    private void resetGame() {
        isPaused = false;
        isJumping = false;
        isFlying = false;
        count = 3;
        marioVelocityY = 0;
        imgindex = 0;
        flyingStartTime = 0;
        movingLeft = false;
        movingRight = false;
        obstacles.clear();
        root.getChildren().removeAll();
        timer.stop();
    }

    // End Scene of Main Game
    // ------------------------------------------------------------------------------------------------
    private Scene createEndScene() {

        BorderPane endingPane = new BorderPane();
        // Title
        Text title = new Text("!! GAME OVER !!");
        title.setStyle(
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 30; -fx-font-weight: bold; -fx-fill: #FFFFFF;");
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        BorderPane.setMargin(title, new javafx.geometry.Insets(50, 0, 0, 0)); // Adjusted top margin
        endingPane.setTop(title);

        // Restart Button
        ImageView restartIcon = new ImageView(new Image("images/restartIcon.png"));
        restartIcon.setFitWidth(45);
        restartIcon.setFitHeight(45);

        Button restartButton = new Button("", restartIcon);
        restartButton
                .setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        restartButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetGame();
                primaryStage.setScene(createGameScene());
                primaryStage.setMaximized(true);
            }
        });

        // High Score Button
        Button highScoreButton = new Button("Current Score : " + highScore);
        highScoreButton
                .setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        highScoreButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Back To Home Button
        Button backToHomeButton = new Button("Back To Home");
        backToHomeButton
                .setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        backToHomeButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        backToHomeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(temp);
                primaryStage.setFullScreen(true);
            }
        });

        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(restartButton, highScoreButton, backToHomeButton);

        endingPane.setCenter(buttonBox);

        // Create the scene
        background_obj.setJpgBackground(endingPane);
        scend = new Scene(endingPane);
        return scend;
    }

    // RUN
    // ------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        launch(args);
    }

}

// Background Image Set Class
// ------------------------------------------------------------------------------------------------
class Background {

    public void setDarkGradientBackground(BorderPane pane) {
        pane.setStyle("-fx-background: linear-gradient(to bottom right, #000066, #000000);");
    }

    public void setMainPageBackground(BorderPane pane) {
        pane.setStyle("-fx-background-image: url('images/startBack.png'); -fx-background-size: cover;");
    }

    public void setSettingsPageBackground(BorderPane settingsLayout) {
        settingsLayout.setStyle("-fx-background-image: url('images/startBack.png'); -fx-background-size: cover;");
    }

    public void setJpgBackground(BorderPane pane) {
        pane.setStyle("-fx-background-image: url('images/startBack.png'); -fx-background-size: cover;");
    }

}

class WriteIntegersToFile {

    public void writeMoney() {
        try (FileWriter writer = new FileWriter("money.txt", false)) {
            // The 'false' parameter ensures the file is overwritten
            writer.write(Main.money + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ReadIntegersFromFile {

    public int giveMoney() {
        try (BufferedReader reader = new BufferedReader(new FileReader("money.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int number = Integer.parseInt(line);
                return number;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}