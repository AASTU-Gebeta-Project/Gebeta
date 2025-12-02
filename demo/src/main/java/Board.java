class Board {
    private int[] pits;       // stores seeds in each pit
    private int pitsPerSide;  // number of pits on each player's side
    private int player1Store; // Player 1's store (mancala)
    private int player2Store; // Player 2's store (mancala)

    public Board(int pitsPerSide, int seedsPerPit) {
        this.pitsPerSide = pitsPerSide;
        this.pits = new int[pitsPerSide * 2];
        this.player1Store = 0;
        this.player2Store = 0;
        
        // Fill each pit with seeds
        for (int i = 0; i < pits.length; i++) {
            pits[i] = seedsPerPit;
        }
    }

    public int getSeeds(int index) {
        if (index == -1) return player1Store; // Player 1 store
        if (index == -2) return player2Store; // Player 2 store
        return pits[index];
    }

    public void setSeeds(int index, int value) {
        if (index == -1) {
            player1Store = value;
        } else if (index == -2) {
            player2Store = value;
        } else {
            pits[index] = value;
        }
    }

    public void addSeeds(int index, int value) {
        if (index == -1) {
            player1Store += value;
        } else if (index == -2) {
            player2Store += value;
        } else {
            pits[index] += value;
        }
    }

    public int[] getBoardState() {
        return pits;
    }

    public int getPitsPerSide() {
        return pitsPerSide;
    }

    public int getTotalPits() {
        return pits.length;
    }

    public int getPlayer1Store() {
        return player1Store;
    }

    public int getPlayer2Store() {
        return player2Store;
    }

    public void setPlayer1Store(int value) {
        this.player1Store = value;
    }

    public void setPlayer2Store(int value) {
        this.player2Store = value;
    }
}

