class GebetaGame implements IGebetaGame {
    private Board board;
    private Player player1;
    private Player player2;
    private int currentPlayer = 1;
    private boolean gameStarted = false;
    private boolean extraTurn = false;
    private int lastSownIndex = -1; // Track where last seed was sown for animation
    private java.util.List<Integer> sowingPath; // Path of indices where seeds will be sown

    public GebetaGame() {
        this.board = new Board(6, 4); // 6 pits each side, 4 seeds each
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

    // Prepare move and return sowing path for animation
    public java.util.List<Integer> prepareMove(int pitIndex) {
        sowingPath = new java.util.ArrayList<>();
        lastSownIndex = -1;
        
        if (!gameStarted || isGameOver() || board.getSeeds(pitIndex) == 0) {
            return sowingPath;
        }

        int seeds = board.getSeeds(pitIndex);
        int currentIndex = pitIndex;
        
        // Calculate sowing path - seeds move counter-clockwise
        for (int i = 0; i < seeds; i++) {
            // Get next index in the sowing sequence
            currentIndex = getNextIndex(currentIndex, currentPlayer);
            
            // Skip opponent's store - if we land on it, move to next position
            while ((currentPlayer == 1 && currentIndex == -2) || 
                   (currentPlayer == 2 && currentIndex == -1)) {
                currentIndex = getNextIndex(currentIndex, currentPlayer);
            }
            
            sowingPath.add(currentIndex);
        }
        
        return sowingPath;
    }
    
    // Sow one seed at the next position in the path (for animation)
    public boolean sowNextSeed(int pitIndex) {
        if (sowingPath == null || sowingPath.isEmpty()) {
            return false;
        }
        
        if (!sowingPath.isEmpty()) {
            int nextIndex = sowingPath.remove(0);
            board.addSeeds(nextIndex, 1);
            lastSownIndex = nextIndex;
            return !sowingPath.isEmpty(); // Return true if more seeds to sow
        }
        
        return false;
    }
    
    // Complete the move after animation (handle capture, extra turn, etc.)
    public void completeMove() {
        if (lastSownIndex == -1) return;
        
        int pitsPerSide = board.getPitsPerSide();
        extraTurn = false;
        
        // Check for capture (last seed lands in empty pit on player's side)
        if (lastSownIndex >= 0 && lastSownIndex < pitsPerSide * 2) {
            if (currentPlayer == 1 && lastSownIndex < pitsPerSide && board.getSeeds(lastSownIndex) == 1) {
                // Last seed landed in empty pit on player 1's side
                int oppositeIndex = (pitsPerSide * 2 - 1) - lastSownIndex;
                int capturedSeeds = board.getSeeds(oppositeIndex);
                if (capturedSeeds > 0) {
                    board.setSeeds(lastSownIndex, 0);
                    board.setSeeds(oppositeIndex, 0);
                    board.addSeeds(-1, capturedSeeds + 1); // Add to player 1's store
                }
            } else if (currentPlayer == 2 && lastSownIndex >= pitsPerSide && board.getSeeds(lastSownIndex) == 1) {
                // Last seed landed in empty pit on player 2's side
                int oppositeIndex = (pitsPerSide * 2 - 1) - lastSownIndex;
                int capturedSeeds = board.getSeeds(oppositeIndex);
                if (capturedSeeds > 0) {
                    board.setSeeds(lastSownIndex, 0);
                    board.setSeeds(oppositeIndex, 0);
                    board.addSeeds(-2, capturedSeeds + 1); // Add to player 2's store
                }
            }
        }

        // Check for extra turn (last seed lands in player's store)
        if ((currentPlayer == 1 && lastSownIndex == -1) || 
            (currentPlayer == 2 && lastSownIndex == -2)) {
            extraTurn = true;
        } else {
            // Switch player
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
        }

        // Update scores
        player1.setScore(board.getPlayer1Store());
        player2.setScore(board.getPlayer2Store());
        
        lastSownIndex = -1;
        sowingPath = null;
        
        displayBoard();
    }

    @Override
    public void makeMove(int pitIndex) {
        if (!gameStarted) {
            System.out.println("Game not started!");
            return;
        }

        if (isGameOver()) {
            System.out.println("Game is over!");
            return;
        }

        int pitsPerSide = board.getPitsPerSide();
        
        // Validate pit index based on current player
        if (currentPlayer == 1) {
            if (pitIndex < 0 || pitIndex >= pitsPerSide) {
                System.out.println("Invalid pit index for Player 1!");
                return;
            }
        } else {
            if (pitIndex < pitsPerSide || pitIndex >= pitsPerSide * 2) {
                System.out.println("Invalid pit index for Player 2!");
                return;
            }
        }

        // Check if pit is empty
        if (board.getSeeds(pitIndex) == 0) {
            System.out.println("Cannot move from empty pit!");
            return;
        }

        // Perform the move (non-animated version for console)
        int seeds = board.getSeeds(pitIndex);
        board.setSeeds(pitIndex, 0);
        
        int currentIndex = pitIndex;
        extraTurn = false;
        
        // Sow seeds counter-clockwise
        for (int i = 0; i < seeds; i++) {
            // Get next index in the sowing sequence
            currentIndex = getNextIndex(currentIndex, currentPlayer);
            
            // Skip opponent's store - if we land on it, move to next position
            while ((currentPlayer == 1 && currentIndex == -2) || 
                   (currentPlayer == 2 && currentIndex == -1)) {
                currentIndex = getNextIndex(currentIndex, currentPlayer);
            }
            
            board.addSeeds(currentIndex, 1);
        }

        // Check for capture (last seed lands in empty pit on player's side)
        if (currentIndex >= 0 && currentIndex < pitsPerSide * 2) {
            if (currentPlayer == 1 && currentIndex < pitsPerSide && board.getSeeds(currentIndex) == 1) {
                // Last seed landed in empty pit on player 1's side
                int oppositeIndex = (pitsPerSide * 2 - 1) - currentIndex;
                int capturedSeeds = board.getSeeds(oppositeIndex);
                if (capturedSeeds > 0) {
                    board.setSeeds(currentIndex, 0);
                    board.setSeeds(oppositeIndex, 0);
                    board.addSeeds(-1, capturedSeeds + 1); // Add to player 1's store
                }
            } else if (currentPlayer == 2 && currentIndex >= pitsPerSide && board.getSeeds(currentIndex) == 1) {
                // Last seed landed in empty pit on player 2's side
                int oppositeIndex = (pitsPerSide * 2 - 1) - currentIndex;
                int capturedSeeds = board.getSeeds(oppositeIndex);
                if (capturedSeeds > 0) {
                    board.setSeeds(currentIndex, 0);
                    board.setSeeds(oppositeIndex, 0);
                    board.addSeeds(-2, capturedSeeds + 1); // Add to player 2's store
                }
            }
        }

        // Check for extra turn (last seed lands in player's store)
        if ((currentPlayer == 1 && currentIndex == -1) || 
            (currentPlayer == 2 && currentIndex == -2)) {
            extraTurn = true;
        } else {
            // Switch player
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
        }

        // Update scores
        player1.setScore(board.getPlayer1Store());
        player2.setScore(board.getPlayer2Store());

        displayBoard();
    }

    private int getNextIndex(int currentIndex, int player) {
        int pitsPerSide = board.getPitsPerSide();
        
        // Handle stores - after a store, continue to opponent's leftmost pit
        if (currentIndex == -1) {
            // From player 1's store, continue counter-clockwise to player 2's leftmost pit (6)
            return pitsPerSide;
        } else if (currentIndex == -2) {
            // From player 2's store, continue counter-clockwise to player 1's leftmost pit (0)
            return 0;
        }
        
        // Handle regular pits
        if (currentIndex >= 0 && currentIndex < pitsPerSide) {
            // Player 1's pits: 0->1->2->3->4->5->store(-1)
            if (currentIndex == pitsPerSide - 1) {
                // From player 1's rightmost pit (5), go to player 1's store
                return -1;
            } else {
                // Move to next pit in player 1's side
                return currentIndex + 1;
            }
        } else if (currentIndex >= pitsPerSide && currentIndex < pitsPerSide * 2) {
            // Player 2's pits: 6->7->8->9->10->11->store(-2)
            if (currentIndex == pitsPerSide * 2 - 1) {
                // From player 2's rightmost pit (11), go to player 2's store
                return -2;
            } else {
                // Move to next pit in player 2's side (6->7->8->9->10->11)
                return currentIndex + 1;
            }
        }
        
        // Should never reach here, but return currentIndex as fallback
        return currentIndex;
    }

    @Override
    public boolean isGameOver() {
        int pitsPerSide = board.getPitsPerSide();
        
        // Check if player 1's side is empty
        boolean player1Empty = true;
        for (int i = 0; i < pitsPerSide; i++) {
            if (board.getSeeds(i) > 0) {
                player1Empty = false;
                break;
            }
        }
        
        // Check if player 2's side is empty
        boolean player2Empty = true;
        for (int i = pitsPerSide; i < pitsPerSide * 2; i++) {
            if (board.getSeeds(i) > 0) {
                player2Empty = false;
                break;
            }
        }
        
        if (player1Empty || player2Empty) {
            // Collect remaining seeds
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

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
    
    public int getLastSownIndex() {
        return lastSownIndex;
    }
}

