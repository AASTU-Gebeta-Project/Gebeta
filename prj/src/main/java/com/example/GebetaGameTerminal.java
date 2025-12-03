package com.example;

import java.util.Scanner;

public class GebetaGameTerminal {
    private GebetaGame game;
    private Scanner scanner;

    public GebetaGameTerminal() {
        this.game = new GebetaGame();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Welcome to Gebeta Game ===");
        System.out.println("Player 1 controls pits 0-5 (bottom row)");
        System.out.println("Player 2 controls pits 6-11 (top row)");
        System.out.println("Enter pit index to make a move.\n");
        
        game.startGame();

        while (!game.isGameOver()) {
            int currentPlayer = game.getCurrentPlayer();
            System.out.println("=== Player " + currentPlayer + "'s Turn ===");
            
            int pitIndex = getValidPitInput(currentPlayer);
            
            if (pitIndex == -1) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }

            game.makeMove(pitIndex);
            
            if (game.hasExtraTurn()) {
                System.out.println(">>> Extra turn! Player " + currentPlayer + " goes again! <<<\n");
            }
        }

        // Game over
        int score1 = game.getPlayer1Score();
        int score2 = game.getPlayer2Score();
        System.out.println("\n=== GAME OVER ===");
        System.out.println("Player 1 score: " + score1);
        System.out.println("Player 2 score: " + score2);
        
        if (score1 > score2) {
            System.out.println("Player 1 wins!");
        } else if (score2 > score1) {
            System.out.println("Player 2 wins!");
        } else {
            System.out.println("It's a tie!");
        }
        
        scanner.close();
    }

    private int getValidPitInput(int currentPlayer) {
        int pitsPerSide = game.getBoard().getPitsPerSide();
        int minPit = (currentPlayer == 1) ? 0 : pitsPerSide;
        int maxPit = (currentPlayer == 1) ? pitsPerSide - 1 : pitsPerSide * 2 - 1;
        
        System.out.print("Enter pit index (" + minPit + "-" + maxPit + "): ");
        
        try {
            String input = scanner.nextLine().trim();
            int pitIndex = Integer.parseInt(input);
            
            // Check if pit is in valid range for current player
            if ((currentPlayer == 1 && (pitIndex < 0 || pitIndex >= pitsPerSide)) ||
                (currentPlayer == 2 && (pitIndex < pitsPerSide || pitIndex >= pitsPerSide * 2))) {
                System.out.println("Error: You can only select pits from your side!");
                return -1;
            }
            
            // Check if pit has seeds
            if (game.getBoard().getSeeds(pitIndex) == 0) {
                System.out.println("Error: This pit is empty! Choose another pit.");
                return -1;
            }
            
            return pitIndex;
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
            return -1;
        }
    }

    public static void main(String[] args) {
        GebetaGameTerminal terminalGame = new GebetaGameTerminal();
        terminalGame.start();
    }
}

