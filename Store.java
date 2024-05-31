import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Store {
    private Stage primaryStage;
    private Scene previousScene;


    public Scene createStoreScene(Stage primaryStage, Scene previousScene) {

        ReadIntegersFromFile obj = new ReadIntegersFromFile();
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;

        BorderPane storePane = new BorderPane();
        setJpgBackground(storePane);

        // Adding images to the center
        ImageView image1 = new ImageView(new Image("images/store1.png"));
        ImageView image2 = new ImageView(new Image("images/store2.png"));
        ImageView image3 = new ImageView(new Image("images/store3.png"));

        image1.setOnMouseEntered((event) -> {
            image1.setStyle("-fx-effect: dropshadow(gaussian, lightblue, 10, 0.5, 0, 0);");
        });
        image1.setOnMouseExited((event) -> {
            image1.setStyle("-fx-effect: dropshadow(gaussian, lightblue, 0, 0, 0, 0);");
        });

        image2.setOnMouseEntered((event) -> {
            image2.setStyle("-fx-effect: dropshadow(gaussian, lightblue, 10, 0.5, 0, 0);");
        });
        image2.setOnMouseExited((event) -> {
            image2.setStyle("-fx-effect: dropshadow(gaussian, lightblue, 0, 0, 0, 0);");
        });

        image3.setOnMouseEntered((event) -> {
            image3.setStyle("-fx-effect: dropshadow(gaussian, lightblue, 10, 0.5, 0, 0);");
        });
        image3.setOnMouseExited((event) -> {
            image3.setStyle("-fx-effect: dropshadow(gaussian, lightblue, 0, 0, 0, 0);");
        });
        HBox imagesBox = new HBox(120); // Use HBox for horizontal alignment
        imagesBox.setAlignment(Pos.CENTER);
        imagesBox.getChildren().addAll(image1, image2, image3);

        storePane.setCenter(imagesBox);

        // Adding back button to the top right
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> switchToMainPage());
        backButton.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        backButton.setPrefWidth(200);
        backButton.setPrefHeight(40);

        // Adding icon and text separately in HBox
        ImageView icon = new ImageView(new Image("images/dimond.png"));
        icon.setFitWidth(40);
        icon.setFitHeight(40);

        Text moneyText = new Text(" X " + obj.giveMoney());
        System.out.println(Main.money);
        moneyText.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF); -fx-text-fill: black;");
        moneyText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
       

        // Create HBox for icon and text
        HBox iconTextBox = new HBox(icon, moneyText);
        iconTextBox.setAlignment(Pos.CENTER_LEFT);
        iconTextBox.setSpacing(10); // Adjust spacing as needed

        // Create HBox for top left and top right buttons
        HBox topLeftBox = new HBox(iconTextBox);
        topLeftBox.setAlignment(Pos.TOP_LEFT);

        HBox topRightBox = new HBox(backButton);
        topRightBox.setAlignment(Pos.TOP_RIGHT);

        // Adding label to the center of the top
        Label selectAvatarLabel = new Label("Select Avatar");
        selectAvatarLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        selectAvatarLabel.setStyle("-fx-text-fill: black;");
        HBox centerTopBox = new HBox(selectAvatarLabel);
        centerTopBox.setAlignment(Pos.CENTER);

        // Use a BorderPane to place the top left, top center, and top right HBox
        BorderPane topPane = new BorderPane();
        topPane.setLeft(topLeftBox);
        topPane.setCenter(centerTopBox);
        topPane.setRight(topRightBox);
        topPane.setStyle("-fx-background-color: linear-gradient(to bottom, #2BB5FF, #FFFFFF);"); // Dark gradient background

        storePane.setTop(topPane);

        Scene storeScene = new Scene(storePane);
        primaryStage.setFullScreen(true);

        return storeScene;
    }

    private void switchToMainPage() {
        primaryStage.setScene(previousScene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
    }

    private static void setJpgBackground(BorderPane pane) {
        pane.setStyle("-fx-background-image: url('images/startBack.png'); -fx-background-size: cover;");
    }
}