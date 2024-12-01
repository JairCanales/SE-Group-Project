package projectPhysics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class basePhysics extends JPanel {

    // Screen dimensions
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 400;

    // Ball settings
    static final int BALL_RADIUS = 12;
    static final double FRICTION = 0.98;
    static final int POCKET_RADIUS = 30;
    static final double MAX_FORCE = 15; // Maximum force for the shot
    static final int EIGHT_BALL_NUMBER = 8; // The 8-ball number

    private final List<ball> balls = new ArrayList<>();
    private final List<Point> pockets = new ArrayList<>();
    private ball cueBall;
    private boolean isDragging = false;
    private Point initialDragPoint = null; // To store the point where the drag started
    private boolean gameOver = false; // Track if the game is over
    private TurnManager turnManager;
    private String gameOverMessage = ""; // Message when the game is over
    private boolean playerOneTurnFinished = false;
    private boolean playerTwoTurnFinished = false;

    public basePhysics() {
        cueBall = new ball(250, SCREEN_HEIGHT / 2, 0, 0, Color.WHITE, 0, false);
        balls.add(cueBall);

        // Initialize TurnManager
        turnManager = new TurnManager();

        // Colors for the numbered balls
        Color[] ballColors = {
            Color.YELLOW, Color.BLUE, Color.RED, Color.CYAN, 
            Color.ORANGE, Color.GREEN, Color.MAGENTA, Color.BLACK, Color.PINK, Color.BLUE, Color.YELLOW, Color.RED, Color.ORANGE, Color.CYAN, Color.GREEN
        };

        // Initialize balls in a triangle formation (right side of the board)
        int triangleStartX = SCREEN_WIDTH - 200;
        int triangleStartY = SCREEN_HEIGHT / 2;
        int rowSpacing = BALL_RADIUS * 2;
        int ballIndex = 0;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col <= row; col++) {
                int x = triangleStartX - (row * rowSpacing / 2) + (col * rowSpacing);
                int y = triangleStartY + row * rowSpacing;

                boolean isStripe = (ballIndex + 1 != EIGHT_BALL_NUMBER) && (ballIndex % 2 == 1);
                if (ballIndex + 1 == EIGHT_BALL_NUMBER) isStripe = true;

                balls.add(new ball(x, y, 0, 0, ballColors[ballIndex % ballColors.length], ballIndex + 1, isStripe));
                ballIndex++;
            }
        }

        // Pockets (corners and midpoints of the table sides)
        pockets.add(new Point(0, 0));
        pockets.add(new Point(SCREEN_WIDTH / 2, 0));
        pockets.add(new Point(SCREEN_WIDTH, 0));
        pockets.add(new Point(0, SCREEN_HEIGHT));
        pockets.add(new Point(SCREEN_WIDTH / 2, SCREEN_HEIGHT));
        pockets.add(new Point(SCREEN_WIDTH, SCREEN_HEIGHT));

        // Mouse input for cue ball
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (cueBall.isStopped()) {
                    isDragging = true;
                    initialDragPoint = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    double dx = e.getX() - cueBall.x;
                    double dy = e.getY() - cueBall.y;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double force = Math.min(distance / 10, MAX_FORCE);
                    cueBall.vx = force * dx / distance;
                    cueBall.vy = force * dy / distance;
                    isDragging = false;
                    initialDragPoint = null;
                }
            }
        });

        // Animation loop
        new Timer(16, e -> {
            if (!gameOver) {
                updatePhysics();
                repaint();
            }
        }).start();
    }

    private void updatePhysics() {
        for (ball ball : balls) {
            if (!ball.inPocket) {
                ball.move();
                ball.checkWallCollision();
                ball.checkPocketCollision(pockets);
            }
        }

        // Check if the cue ball is pocketed
        checkCueBallPocketed();

        // Check if 8-ball is pocketed
        for (ball ball : balls) {
            if (ball.number == EIGHT_BALL_NUMBER && ball.inPocket) {
                gameOver = true;
                gameOverMessage = turnManager.isPlayerOneTurn() ? "Player 2 wins!" : "Player 1 wins!";
                System.out.println(gameOverMessage);
            }
        }

        // Handle ball collisions
        for (int i = 0; i < balls.size(); i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                balls.get(i).checkBallCollision(balls.get(j));
            }
        }

        turnManager.checkTurn(this);
    }

    // Function to reset the cue ball if it is pocketed
    private void checkCueBallPocketed() {
        if (cueBall.inPocket) {
            cueBall.x = 250; // Reset to starting X position
            cueBall.y = SCREEN_HEIGHT / 2; // Reset to starting Y position
            cueBall.vx = 0; // Reset velocity
            cueBall.vy = 0;
            cueBall.inPocket = false; // Set back to not in pocket
        }
    }

    public void setGameOverMessage(String message) {
        this.gameOverMessage = message;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw table
        g.setColor(new Color(39, 119, 20)); 
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Draw pockets
        g.setColor(Color.BLACK);
        for (Point pocket : pockets) {
            g.fillOval(pocket.x - POCKET_RADIUS, pocket.y - POCKET_RADIUS, POCKET_RADIUS * 2, POCKET_RADIUS * 2);
        }

        // Draw balls
        for (ball ball : balls) {
            ball.draw(g);
        }

        // Draw cue line if dragging
        if (isDragging && initialDragPoint != null) {
            g.setColor(Color.RED);
            Point mousePosition = getMousePosition();
            if (mousePosition != null) {
                g.drawLine((int) cueBall.x, (int) cueBall.y, mousePosition.x, mousePosition.y);
            }
        }

        // Display whose turn it is
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String turnText = turnManager.isPlayerOneTurn() ? "Player 1's Turn" : "Player 2's Turn";
        g.drawString(turnText, SCREEN_WIDTH - 150, 30);

        // Display the winner message
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(gameOverMessage, SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2 + 40);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pool Game Physics Simulation");
        basePhysics poolGame = new basePhysics();
        poolGame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        frame.add(poolGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Getters for the balls
    public List<ball> getBalls() {
        return balls;
    }

    // Methods to check turn completion
    public boolean isPlayerOneTurnFinished() {
        return playerOneTurnFinished;
    }

    public boolean isPlayerTwoTurnFinished() {
        return playerTwoTurnFinished;
    }

    // Set the turn finished status
    public void setPlayerOneTurnFinished(boolean finished) {
        this.playerOneTurnFinished = finished;
    }

    public void setPlayerTwoTurnFinished(boolean finished) {
        this.playerTwoTurnFinished = finished;
    }
}
