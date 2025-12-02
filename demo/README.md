# Gebeta Game (JavaFX)

A JavaFX implementation of the traditional Gebeta (Mancala) board game.

## Features

- Complete Gebeta game logic with proper rules
- Beautiful JavaFX GUI with visual board representation
- Interactive gameplay with clickable pits
- Score tracking and game status display
- Turn management with extra turn detection
- Capture mechanics

## Game Rules

1. **Setup**: The board has 6 pits on each side, each starting with 4 seeds. Each player has a store (mancala) on their side.

2. **Turn**: On your turn, click one of your pits to pick up all seeds from it.

3. **Sowing**: Seeds are distributed counter-clockwise, one seed per pit/store, skipping the opponent's store.

4. **Capture**: If your last seed lands in an empty pit on your side, you capture all seeds from the opposite pit and add them to your store.

5. **Extra Turn**: If your last seed lands in your store, you get another turn.

6. **Game End**: The game ends when one player's side is empty. The remaining seeds go to that player's store. The player with the most seeds wins.

## Running the Game

### Using Maven

```bash
mvn clean javafx:run
```

### Using Java directly

Make sure you have JavaFX modules in your classpath:

```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml GebetaGameGUI
```

### Using an IDE

1. Import the project into your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Make sure JavaFX SDK is added to your project
3. Run `GebetaGameGUI.java` as the main class

## Project Structure

- `IGebetaGame.java` - Game interface
- `Board.java` - Board state management
- `Player.java` - Player information
- `GebetaGame.java` - Complete game logic implementation
- `GebetaGameGUI.java` - JavaFX GUI application
- `Main.java` - Entry point

## Requirements

- Java 11 or higher
- JavaFX SDK 17 or higher

