package com.example;

class GebetaGame implements IGebetaGame {
    private Board board;
    private Player player1;
    private Player player2;
    private int currentPlayer = 1;
    private boolean gameStarted = false;
    private boolean extraTurn = false;

    public GebetaGame() {
        this.board = new Board(6, 4);
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
    }

    @Override
    public void startGame() {
        this.board = new Board(6, 4);
        this.currentPlayer = 1;
        this.gameStarted = true;
        this.extraTurn = false;
        player1.setScore(0);
        player2.setScore(0);
        System.out.println("Gebeta Game Started!");
        displayBoard();
    }

    @Override
    public void makeMove(int pitIndex) {
        if (!gameStarted || isGameOver() || board.getSeeds(pitIndex) == 0) return;

        int pitsPerSide = board.getPitsPerSide();
        if ((currentPlayer == 1 && (pitIndex < 0 || pitIndex >= pitsPerSide)) ||
            (currentPlayer == 2 && (pitIndex < pitsPerSide || pitIndex >= pitsPerSide * 2))) return;

        int seeds = board.getSeeds(pitIndex);
        board.setSeeds(pitIndex, 0);
        int currentIndex = pitIndex;
        extraTurn = false;

        for (int i = 0; i < seeds; i++) {
            currentIndex = getNextIndex(currentIndex, currentPlayer);
            while ((currentPlayer == 1 && currentIndex == -2) || (currentPlayer == 2 && currentIndex == -1)) {
                currentIndex = getNextIndex(currentIndex, currentPlayer);
            }
            board.addSeeds(currentIndex, 1);
        }

        if (currentIndex >= 0 && currentIndex < pitsPerSide * 2 && board.getSeeds(currentIndex) == 1) {
            boolean p1Capture = currentPlayer == 1 && currentIndex < pitsPerSide;
            boolean p2Capture = currentPlayer == 2 && currentIndex >= pitsPerSide;
            if (p1Capture || p2Capture) {
                int oppositeIndex = (pitsPerSide * 2 - 1) - currentIndex;
                int capturedSeeds = board.getSeeds(oppositeIndex);
                if (capturedSeeds > 0) {
                    board.setSeeds(currentIndex, 0);
                    board.setSeeds(oppositeIndex, 0);
                    board.addSeeds(p1Capture ? -1 : -2, capturedSeeds + 1);
                }
            }
        }

        if ((currentPlayer == 1 && currentIndex == -1) || (currentPlayer == 2 && currentIndex == -2)) {
            extraTurn = true;
        } else {
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
        }

        player1.setScore(board.getPlayer1Store());
        player2.setScore(board.getPlayer2Store());
        displayBoard();
    }

    private int getNextIndex(int currentIndex, int player) {
        int pitsPerSide = board.getPitsPerSide();
        if (currentIndex == -1) return pitsPerSide;
        if (currentIndex == -2) return 0;
        if (currentIndex >= 0 && currentIndex < pitsPerSide) {
            return currentIndex == pitsPerSide - 1 ? -1 : currentIndex + 1;
        }
        if (currentIndex >= pitsPerSide && currentIndex < pitsPerSide * 2) {
            return currentIndex == pitsPerSide * 2 - 1 ? -2 : currentIndex + 1;
        }
        return currentIndex;
    }

    @Override
    public boolean isGameOver() {
        int pitsPerSide = board.getPitsPerSide();
        boolean p1Empty = true, p2Empty = true;
        for (int i = 0; i < pitsPerSide && p1Empty; i++) {
            if (board.getSeeds(i) > 0) p1Empty = false;
        }
        for (int i = pitsPerSide; i < pitsPerSide * 2 && p2Empty; i++) {
            if (board.getSeeds(i) > 0) p2Empty = false;
        }
        
        if (p1Empty || p2Empty) {
            for (int i = 0; i < pitsPerSide; i++) {
                board.addSeeds(-1, board.getSeeds(i));
                board.setSeeds(i, 0);
            }
            for (int i = pitsPerSide; i < pitsPerSide * 2; i++) {
                board.addSeeds(-2, board.getSeeds(i));
                board.setSeeds(i, 0);
            }
            player1.setScore(board.getPlayer1Store());
            player2.setScore(board.getPlayer2Store());
            return true;
        }
        return false;
    }

    @Override
    public void displayBoard() {
        int[] pits = board.getBoardState();
        int pitsPerSide = board.getPitsPerSide();
        
        System.out.println("\nPlayer 2 Store: " + board.getPlayer2Store());
        System.out.print("Player 2 pits: ");
        for (int i = pitsPerSide * 2 - 1; i >= pitsPerSide; i--) {
            System.out.print(pits[i] + " ");
        }
        System.out.println();
        
        System.out.print("Player 1 pits: ");
        for (int i = 0; i < pitsPerSide; i++) {
            System.out.print(pits[i] + " ");
        }
        System.out.println();
        System.out.println("Player 1 Store: " + board.getPlayer1Store());
        System.out.println("Current Player: " + currentPlayer);
        System.out.println();
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public int getPlayer1Score() {
        return board.getPlayer1Store();
    }

    @Override
    public int getPlayer2Score() {
        return board.getPlayer2Store();
    }

    public boolean hasExtraTurn() {
        return extraTurn;
    }

    public Board getBoard() {
        return board;
    }
}

