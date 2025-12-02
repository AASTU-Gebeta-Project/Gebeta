import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

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
    private boolean isAnimating = false;
    private int animatingPitIndex = -1;

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

        Scene scene = new Scene(root, 1100, 700);
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
        storeBox.setMinWidth(100);
        storeBox.setMinHeight(120);
        storeBox.setStyle("-fx-background-color: #8b4513; -fx-background-radius: 10; -fx-border-color: #654321; -fx-border-width: 3; -fx-border-radius: 10;");

        Label storeLabel = new Label("Store");
        storeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        storeLabel.setTextFill(Color.WHITE);

        Label scoreLabel = new Label("0");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        scoreLabel.setTextFill(Color.WHITE);

        storeBox.getChildren().addAll(storeLabel, scoreLabel);

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

        // Create grid for pits
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(15);
        boardGrid.setVgap(15);
        boardGrid.setPadding(new Insets(20));

        // Player 2 pits (top row, right to left)
        for (int i = 11; i >= 6; i--) {
            Button pit = createPitButton(i);
            pitButtons.add(pit);
            buttonToPitIndex.put(pit, i); // Store mapping
            boardGrid.add(pit, 11 - i, 0);
        }

        // Player 1 pits (bottom row, left to right)
        for (int i = 0; i < 6; i++) {
            Button pit = createPitButton(i);
            pitButtons.add(pit);
            buttonToPitIndex.put(pit, i); // Store mapping
            boardGrid.add(pit, i, 1);
        }

        // Current player indicator
        currentPlayerLabel = new Label("Current Player: Player 1");
        currentPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        currentPlayerLabel.setTextFill(Color.WHITE);

        // Status label
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
        pit.setStyle("-fx-background-color: #8b4513; -fx-background-radius: 40; " +
                     "-fx-border-color: #654321; -fx-border-width: 2; " +
                     "-fx-border-radius: 40; -fx-cursor: hand;");
        pit.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        pit.setTextFill(Color.WHITE);

        pit.setOnAction(e -> handlePitClick(pitIndex));

        return pit;
    }

    private VBox createControlSection() {
        VBox controlSection = new VBox(15);
        controlSection.setAlignment(Pos.TOP_CENTER);
        controlSection.setPadding(new Insets(20));
        controlSection.setMinWidth(250);
        controlSection.setMaxWidth(250);
        controlSection.setStyle("-fx-background-color: #34495e;");

        Label titleLabel = new Label("Controls");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);

        newGameButton = new Button("New Game");
        newGameButton.setMinWidth(200);
        newGameButton.setMinHeight(40);
        newGameButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                              "-fx-font-size: 14px; -fx-font-weight: bold; " +
                              "-fx-background-radius: 5; -fx-cursor: hand;");
        newGameButton.setOnAction(e -> startNewGame());

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #7f8c8d;");

        Label rulesTitleLabel = new Label("Game Rules");
        rulesTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        rulesTitleLabel.setTextFill(Color.WHITE);

        // Create detailed rules content
        VBox rulesContent = new VBox(10);
        rulesContent.setPadding(new Insets(10));
        
        String rulesText = "OBJECTIVE:\n" +
                           "Capture more seeds than your opponent.\n\n" +
                           "SETUP:\n" +
                           "• 6 pits per player, 4 seeds each\n" +
                           "• Each player has a store (mancala)\n\n" +
                           "HOW TO PLAY:\n" +
                           "1. On your turn, click one of your pits\n" +
                           "2. All seeds from that pit are picked up\n" +
                           "3. Seeds are distributed counter-clockwise, one per pit/store\n" +
                           "4. Skip your opponent's store when sowing\n\n" +
                           "SPECIAL RULES:\n" +
                           "• Capture: If your last seed lands in an empty pit on your side, capture all seeds from the opposite pit\n" +
                           "• Extra Turn: If your last seed lands in your store, you get another turn\n\n" +
                           "GAME END:\n" +
                           "The game ends when one player's side is empty. Remaining seeds go to that player's store. Player with most seeds wins!";

        Label rulesLabel = new Label(rulesText);
        rulesLabel.setFont(Font.font("Arial", 11));
        rulesLabel.setTextFill(Color.LIGHTGRAY);
        rulesLabel.setWrapText(true);
        rulesLabel.setLineSpacing(2);

        ScrollPane rulesScroll = new ScrollPane(rulesLabel);
        rulesScroll.setFitToWidth(true);
        rulesScroll.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #7f8c8d; -fx-border-width: 1;");
        rulesScroll.setPrefHeight(400);
        rulesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rulesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        controlSection.getChildren().addAll(titleLabel, newGameButton, separator, rulesTitleLabel, rulesScroll);

        return controlSection;
    }

    private void handlePitClick(int pitIndex) {
        if (isAnimating) {
            return; // Prevent clicks during animation
        }

        if (game.isGameOver()) {
            statusLabel.setText("Game Over! Click 'New Game' to play again.");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        int currentPlayer = game.getCurrentPlayer();
        
        // Validate move
        if (currentPlayer == 1 && pitIndex >= 6) {
            statusLabel.setText("Invalid move! Select a pit from your side.");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (currentPlayer == 2 && pitIndex < 6) {
            statusLabel.setText("Invalid move! Select a pit from your side.");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (game.getBoard().getSeeds(pitIndex) == 0) {
            statusLabel.setText("Cannot move from empty pit!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        // Start animated move
        animateMove(pitIndex);
    }
    
    private void animateMove(int pitIndex) {
        isAnimating = true;
        animatingPitIndex = pitIndex;
        
        // Disable all buttons during animation
        for (Button pit : pitButtons) {
            pit.setDisable(true);
        }
        newGameButton.setDisable(true);
        
        // Get number of seeds to sow
        int seeds = game.getBoard().getSeeds(pitIndex);
        
        // Prepare the move and get sowing path (this calculates the path)
        game.prepareMove(pitIndex);
        
        // Clear the source pit
        game.getBoard().setSeeds(pitIndex, 0);
        updateDisplay();
        
        // Animate sowing seeds one by one
        Timeline timeline = new Timeline();
        for (int i = 0; i < seeds; i++) {
            final int seedNum = i;
            KeyFrame keyFrame = new KeyFrame(
                Duration.millis(300 * (i + 1)), // Delay each seed by 300ms
                event -> {
                    // Sow next seed
                    game.sowNextSeed(animatingPitIndex);
                    updateDisplay();
                    
                    // Highlight the current pit being sown
                    highlightPit(game.getLastSownIndex());
                }
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        
        // Add completion frame after all seeds are sown
        KeyFrame completeFrame = new KeyFrame(
            Duration.millis(300 * seeds + 500), // Wait after last seed
            completeEvent -> {
                game.completeMove();
                isAnimating = false;
                newGameButton.setDisable(false);
                
                // Update status
                if (game.isGameOver()) {
                    int score1 = game.getPlayer1Score();
                    int score2 = game.getPlayer2Score();
                    String winner = score1 > score2 ? "Player 1" : (score2 > score1 ? "Player 2" : "Tie");
                    statusLabel.setText("Game Over! " + winner + " wins! (" + score1 + " - " + score2 + ")");
                    statusLabel.setTextFill(Color.GOLD);
                } else if (game.hasExtraTurn()) {
                    statusLabel.setText("Extra turn! Player " + game.getCurrentPlayer() + " goes again.");
                    statusLabel.setTextFill(Color.LIGHTGREEN);
                } else {
                    statusLabel.setText("Player " + game.getCurrentPlayer() + "'s turn");
                    statusLabel.setTextFill(Color.LIGHTGREEN);
                }
                
                // Re-enable buttons (this will properly enable/disable based on current player)
                updateDisplay();
            }
        );
        timeline.getKeyFrames().add(completeFrame);
        
        timeline.play();
    }
    
    private void highlightPit(int pitIndex) {
        // Reset all pits to normal style
        for (Button pit : pitButtons) {
            int index = buttonToPitIndex.get(pit);
            int seeds = game.getBoard().getSeeds(index);
            if (seeds == 0) {
                pit.setStyle("-fx-background-color: #5a4a3a; -fx-background-radius: 40; " +
                           "-fx-border-color: #654321; -fx-border-width: 2; " +
                           "-fx-border-radius: 40; -fx-cursor: default;");
            } else {
                pit.setStyle("-fx-background-color: #8b4513; -fx-background-radius: 40; " +
                           "-fx-border-color: #654321; -fx-border-width: 2; " +
                           "-fx-border-radius: 40; -fx-cursor: hand;");
            }
        }
        
        // Highlight the current pit (if it's a button)
        if (pitIndex >= 0 && pitIndex < 12) {
            for (Button pit : pitButtons) {
                if (buttonToPitIndex.get(pit) == pitIndex) {
                    pit.setStyle("-fx-background-color: #f39c12; -fx-background-radius: 40; " +
                               "-fx-border-color: #e67e22; -fx-border-width: 3; " +
                               "-fx-border-radius: 40; -fx-cursor: hand;");
                    break;
                }
            }
        }
        
        // Highlight stores if needed
        if (pitIndex == -1) {
            player1StoreBox.setStyle("-fx-background-color: #f39c12; -fx-background-radius: 10; " +
                                   "-fx-border-color: #e67e22; -fx-border-width: 4; -fx-border-radius: 10;");
        } else if (pitIndex == -2) {
            player2StoreBox.setStyle("-fx-background-color: #f39c12; -fx-background-radius: 10; " +
                                   "-fx-border-color: #e67e22; -fx-border-width: 4; -fx-border-radius: 10;");
        }
    }

    private void updateDisplay() {
        // Update pit buttons
        Board board = game.getBoard();
        for (Button pit : pitButtons) {
            int pitIndex = buttonToPitIndex.get(pit); // Get actual pit index
            int seeds = board.getSeeds(pitIndex);
            pit.setText(String.valueOf(seeds));
            
            // Disable empty pits
            if (seeds == 0) {
                pit.setStyle("-fx-background-color: #5a4a3a; -fx-background-radius: 40; " +
                           "-fx-border-color: #654321; -fx-border-width: 2; " +
                           "-fx-border-radius: 40; -fx-cursor: default;");
            } else {
                pit.setStyle("-fx-background-color: #8b4513; -fx-background-radius: 40; " +
                           "-fx-border-color: #654321; -fx-border-width: 2; " +
                           "-fx-border-radius: 40; -fx-cursor: hand;");
            }
        }

        // Update scores
        player1ScoreLabel.setText(String.valueOf(game.getPlayer1Score()));
        player2ScoreLabel.setText(String.valueOf(game.getPlayer2Score()));

        // Update current player label
        currentPlayerLabel.setText("Current Player: Player " + game.getCurrentPlayer());
        
        // Highlight current player's section
        if (game.getCurrentPlayer() == 1) {
            player1StoreBox.setStyle("-fx-background-color: #27ae60; -fx-background-radius: 10; " +
                                   "-fx-border-color: #2ecc71; -fx-border-width: 3; -fx-border-radius: 10;");
            player2StoreBox.setStyle("-fx-background-color: #8b4513; -fx-background-radius: 10; " +
                                   "-fx-border-color: #654321; -fx-border-width: 3; -fx-border-radius: 10;");
        } else {
            player2StoreBox.setStyle("-fx-background-color: #27ae60; -fx-background-radius: 10; " +
                                   "-fx-border-color: #2ecc71; -fx-border-width: 3; -fx-border-radius: 10;");
            player1StoreBox.setStyle("-fx-background-color: #8b4513; -fx-background-radius: 10; " +
                                   "-fx-border-color: #654321; -fx-border-width: 3; -fx-border-radius: 10;");
        }

        // Disable/enable pits based on current player (only if not animating)
        if (!isAnimating) {
            for (Button pit : pitButtons) {
                int pitIndex = buttonToPitIndex.get(pit); // Get actual pit index
                if (game.isGameOver()) {
                    pit.setDisable(true);
                } else {
                    int currentPlayer = game.getCurrentPlayer();
                    boolean isPlayer1Pit = pitIndex < 6;
                    boolean isPlayer2Pit = pitIndex >= 6;
                    
                    if ((currentPlayer == 1 && isPlayer1Pit) || (currentPlayer == 2 && isPlayer2Pit)) {
                        pit.setDisable(board.getSeeds(pitIndex) == 0);
                    } else {
                        pit.setDisable(true);
                    }
                }
            }
        }
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

