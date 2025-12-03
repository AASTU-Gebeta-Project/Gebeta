package com.example;

public interface IGebetaGame {
    // Start a new game
    void startGame();

    // Make a move from a selected pit
    void makeMove(int pitIndex);

    // Check if the game has ended
    boolean isGameOver();

    // Display the current board state
    void displayBoard();

    // Get current player (1 or 2)
    int getCurrentPlayer();

    // Get player scores
    int getPlayer1Score();
    int getPlayer2Score();
}

