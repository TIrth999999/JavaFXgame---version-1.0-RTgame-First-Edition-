import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controls {

    public Scene createStoreScene(Stage primaryStage, Scene previousScene) {
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        BorderPane controlsPane = new BorderPane();
        setJpgBackground(controlsPane);

        // Title
        Text title = new Text("!! CONTROLS !!");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
        title.setFill(Color.LIGHTCYAN);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        BorderPane.setMargin(title, new javafx.geometry.Insets(50, 0, 0, 0)); // Adjusted top margin
        controlsPane.setTop(title);

        // Button 1
        ImageView button1Icon = new ImageView(new Image("images/upKey.png"));
        button1Icon.setFitWidth(45);
        button1Icon.setFitHeight(45);

        Button button1 = new Button(": CLICK TO JUMP    ", button1Icon);
        setButtonDimensions(button1);
        button1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-font-weight: bold;-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        setButtonEventHandler(button1, "Jumping action");

        // Button 2
        ImageView button2Icon = new ImageView(new Image("images/rightKey.png"));
        button2Icon.setFitWidth(45);
        button2Icon.setFitHeight(45);

        Button button2 = new Button(": MOVE FORWARD  ", button2Icon);
        setButtonDimensions(button2);
        button2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-font-weight: bold;-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        setButtonEventHandler(button2, "Moving forward action");

        // Button 3
        ImageView button3Icon = new ImageView(new Image("images/leftKey.png"));
        button3Icon.setFitWidth(45);
        button3Icon.setFitHeight(45);

        Button button3 = new Button(": MOVE BACKWARD", button3Icon);
        setButtonDimensions(button3);
        button3.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-font-weight: bold;-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        setButtonEventHandler(button3, "Moving backward action");

        // Back To Home Button
        Button backToHomeButton = new Button("Back To Home");
        setButtonDimensions(backToHomeButton);
        backToHomeButton.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        backToHomeButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        backToHomeButton.setOnAction(e -> switchToMainPage(primaryStage, previousScene));

        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER); // Adjusted alignment here
        buttonBox.getChildren().addAll(button1, button2, button3, backToHomeButton);

        controlsPane.setCenter(buttonBox);

        // Create the scene
        Scene scene = new Scene(controlsPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Controls");

        primaryStage.show();

        return scene;
    }

    private void switchToMainPage(Stage primaryStage, Scene previousScene) {
        primaryStage.setScene(previousScene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
    }

    // Method to set jpg background
    private static void setJpgBackground(BorderPane pane) {
        pane.setStyle("-fx-background-image: url('images/startBack.png'); -fx-background-size: cover;");
    }

    // Method to set fixed dimensions for buttons
    private static void setButtonDimensions(Button button) {
        button.setMinWidth(200);
        button.setMinHeight(60);
    }

    // Method to set event handler for buttons
    private static void setButtonEventHandler(Button button, String action) {
        button.setOnAction(e -> System.out.println("Performing action: " + action));
    }
}