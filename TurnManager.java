package projectPhysics;

public class TurnManager {
    private boolean playerOneTurn = true; // Player 1 starts
    private boolean playerOneHasBalls = true; // Player 1 has solid balls
    private boolean playerTwoHasBalls = true; // Player 2 has striped balls
    private boolean gameOver = false;
    private int playerOneBallsPocketed = 0;
    private int playerTwoBallsPocketed = 0;

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public boolean isPlayerTwoTurn() {
        return !playerOneTurn;
    }

    public void checkTurn(basePhysics poolGame) {
        // Switch turn if no ball was potted
        if (playerOneTurn && !poolGame.isPlayerOneTurnFinished()) {
            if (anyBallPotted(poolGame, true)) {
                // Player 1 potted a ball, continue their turn
            } else {
                // Player 1 missed, switch to Player 2
                playerOneTurn = false;
            }
        }

        if (!playerOneTurn && !poolGame.isPlayerTwoTurnFinished()) {
            if (anyBallPotted(poolGame, false)) {
                // Player 2 potted a ball, continue their turn
            } else {
                // Player 2 missed, switch to Player 1
                playerOneTurn = true;
            }
        }

        // Game over condition based on remaining balls
        checkGameOver(poolGame);
    }

    private boolean anyBallPotted(basePhysics poolGame, boolean isPlayerOne) {
        for (ball b : poolGame.getBalls()) {
            if (b.inPocket) {
                if (isPlayerOne && !b.isStriped) {
                    playerOneBallsPocketed++;
                    return true;
                } else if (!isPlayerOne && b.isStriped) {
                    playerTwoBallsPocketed++;
                    return true;
                }
            }
        }
        return false;
    }

    private void checkGameOver(basePhysics poolGame) {
        if (!playerOneHasBalls || !playerTwoHasBalls) {
            gameOver = true;
            poolGame.setGameOverMessage(isPlayerOneTurn() ? "Player 2 wins!" : "Player 1 wins!");
        }
    }

    // Getters for pocketed balls
    public int getPlayerOneBallsPocketed() {
        return playerOneBallsPocketed;
    }

    public int getPlayerTwoBallsPocketed() {
        return playerTwoBallsPocketed;
    }
}
