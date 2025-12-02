import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GebetaGameGUI extends Application {
    private GebetaGame game;
    private List<Button> pitButtons;
    private Map<Button, Integer> buttonToPitIndex; // Map button to actual pit index
    private Label statusLabel;
    private Label player1ScoreLabel;
    private Label player2ScoreLabel;
    private Label currentPlayerLabel;
    private Button newGameButton;
    private VBox player1StoreBox;
    private VBox player2StoreBox;
    private GridPane boardGrid;

    @Override
    public void start(Stage primaryStage) {
        game = new GebetaGame();
        game.startGame();
        pitButtons = new ArrayList<>();
        buttonToPitIndex = new HashMap<>();

        // Create main container
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2c3e50;");

        // Top section - Player 2 info and store
        VBox topSection = createPlayerSection(2, true);
        root.setTop(topSection);

        // Center section - Game board
        VBox centerSection = createBoardSection();
        root.setCenter(centerSection);

        // Bottom section - Player 1 info and store
        VBox bottomSection = createPlayerSection(1, false);
        root.setBottom(bottomSection);

        // Right section - Controls
        VBox rightSection = createControlSection();
        root.setRight(rightSection);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("Gebeta Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        updateDisplay();
    }

    private VBox createPlayerSection(int playerNum, boolean isTop) {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: #34495e;");

        Label playerLabel = new Label("Player " + playerNum);
        playerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        playerLabel.setTextFill(Color.WHITE);

        VBox storeBox = new VBox(5);
        storeBox.setAlignment(Pos.CENTER);
        storeBox.setMinSize(100, 120);
        storeBox.setStyle("-fx-background-color: #8b4513; -fx-background-radius: 10; -fx-border-color: #654321; -fx-border-width: 3; -fx-border-radius: 10;");

        Label scoreLabel = new Label("0");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        scoreLabel.setTextFill(Color.WHITE);

        storeBox.getChildren().addAll(new Label("Store"), scoreLabel);
        if (playerNum == 1) {
            player1ScoreLabel = scoreLabel;
            player1StoreBox = storeBox;
        } else {
            player2ScoreLabel = scoreLabel;
            player2StoreBox = storeBox;
        }

        if (isTop) {
            section.getChildren().addAll(playerLabel, storeBox);
        } else {
            section.getChildren().addAll(storeBox, playerLabel);
        }
        return section;
    }

    private VBox createBoardSection() {
        VBox boardSection = new VBox(20);
        boardSection.setAlignment(Pos.CENTER);
        boardSection.setPadding(new Insets(20));
        boardSection.setStyle("-fx-background-color: #2c3e50;");

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(15);
        boardGrid.setVgap(15);
        boardGrid.setPadding(new Insets(20));

        for (int i = 11; i >= 6; i--) {
            Button pit = createPitButton(i);
            pitButtons.add(pit);
            buttonToPitIndex.put(pit, i);
            boardGrid.add(pit, 11 - i, 0);
        }
        for (int i = 0; i < 6; i++) {
            Button pit = createPitButton(i);
            pitButtons.add(pit);
            buttonToPitIndex.put(pit, i);
            boardGrid.add(pit, i, 1);
        }

        currentPlayerLabel = new Label("Current Player: Player 1");
        currentPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        currentPlayerLabel.setTextFill(Color.WHITE);

        statusLabel = new Label("Game Started! Player 1's turn");
        statusLabel.setFont(Font.font("Arial", 14));
        statusLabel.setTextFill(Color.LIGHTGREEN);

        boardSection.getChildren().addAll(currentPlayerLabel, boardGrid, statusLabel);
        return boardSection;
    }

    private Button createPitButton(int pitIndex) {
        Button pit = new Button();
        pit.setMinSize(80, 80);
        pit.setMaxSize(80, 80);
        pit.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 40; -fx-border-color: #654321; -fx-border-width: 2; -fx-border-radius: 40; -fx-cursor: hand;");
        pit.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        pit.setTextFill(Color.WHITE);
        pit.setOnAction(e -> handlePitClick(pitIndex));
        return pit;
    }

    private VBox createControlSection() {
        VBox controlSection = new VBox(15);
        controlSection.setAlignment(Pos.TOP_CENTER);
        controlSection.setPadding(new Insets(20));
        controlSection.setMinWidth(200);
        controlSection.setMaxWidth(200);
        controlSection.setStyle("-fx-background-color: #34495e;");

        newGameButton = new Button("New Game");
        newGameButton.setMinSize(150, 40);
        newGameButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
        newGameButton.setOnAction(e -> startNewGame());
        controlSection.getChildren().add(newGameButton);
        return controlSection;
    }

    private void handlePitClick(int pitIndex) {
        if (game.isGameOver()) {
            statusLabel.setText("Game Over! Click 'New Game' to play again.");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        int currentPlayer = game.getCurrentPlayer();
        if ((currentPlayer == 1 && pitIndex >= 6) || (currentPlayer == 2 && pitIndex < 6)) {
            statusLabel.setText("Invalid move! Select a pit from your side.");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (game.getBoard().getSeeds(pitIndex) == 0) {
            statusLabel.setText("Cannot move from empty pit!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        game.makeMove(pitIndex);
        updateDisplay();

        if (game.isGameOver()) {
            int s1 = game.getPlayer1Score(), s2 = game.getPlayer2Score();
            String winner = s1 > s2 ? "Player 1" : (s2 > s1 ? "Player 2" : "Tie");
            statusLabel.setText("Game Over! " + winner + " wins! (" + s1 + " - " + s2 + ")");
            statusLabel.setTextFill(Color.GOLD);
        } else {
            statusLabel.setText(game.hasExtraTurn() ? "Extra turn! Player " + game.getCurrentPlayer() + " goes again." : "Player " + game.getCurrentPlayer() + "'s turn");
            statusLabel.setTextFill(Color.LIGHTGREEN);
        }
    }

    private void updateDisplay() {
        Board board = game.getBoard();
        String pitStyle = "-fx-background-color: #2c3e50; -fx-background-radius: 40; -fx-border-color: #654321; -fx-border-width: 2; -fx-border-radius: 40;";
        
        for (Button pit : pitButtons) {
            int pitIndex = buttonToPitIndex.get(pit);
            int seeds = board.getSeeds(pitIndex);
            pit.setText(String.valueOf(seeds));
            pit.setStyle(pitStyle + (seeds == 0 ? " -fx-cursor: default;" : " -fx-cursor: hand;"));
            
            if (game.isGameOver()) {
                pit.setDisable(true);
            } else {
                int cp = game.getCurrentPlayer();
                boolean isOwnPit = (cp == 1 && pitIndex < 6) || (cp == 2 && pitIndex >= 6);
                pit.setDisable(!isOwnPit || seeds == 0);
            }
        }

        player1ScoreLabel.setText(String.valueOf(game.getPlayer1Score()));
        player2ScoreLabel.setText(String.valueOf(game.getPlayer2Score()));
        currentPlayerLabel.setText("Current Player: Player " + game.getCurrentPlayer());
    }

    private void startNewGame() {
        game.startGame();
        updateDisplay();
        statusLabel.setText("New game started! Player 1's turn");
        statusLabel.setTextFill(Color.LIGHTGREEN);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

