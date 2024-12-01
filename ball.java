package projectPhysics;

import java.awt.*;
import java.util.List;

public class ball {
    public double x, y;
    public double vx, vy;
    public Color color;
    public boolean inPocket = false;
    public int number; // Added ball number
    public boolean isStriped; // Added striped flag

    public ball(double x, double y, double vx, double vy, Color color, int number, boolean isStriped) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.color = color;
        this.number = number;
        this.isStriped = isStriped;
    }

    public void move() {
        x += vx;
        y += vy;
        vx *= basePhysics.FRICTION;
        vy *= basePhysics.FRICTION;
        if (Math.abs(vx) < 0.01) vx = 0;
        if (Math.abs(vy) < 0.01) vy = 0;
    }

    public void checkWallCollision() {
        if (x - basePhysics.BALL_RADIUS <= 0 || x + basePhysics.BALL_RADIUS >= basePhysics.SCREEN_WIDTH) {
            vx = -vx;
        }
        if (y - basePhysics.BALL_RADIUS <= 0 || y + basePhysics.BALL_RADIUS >= basePhysics.SCREEN_HEIGHT) {
            vy = -vy;
        }
    }

    public void checkPocketCollision(List<Point> pockets) {
        for (Point pocket : pockets) {
            double dx = pocket.x - x;
            double dy = pocket.y - y;
            if (Math.sqrt(dx * dx + dy * dy) <= basePhysics.POCKET_RADIUS) {
                inPocket = true;
                vx = 0;
                vy = 0;
            }
        }
    }

    public void checkBallCollision(ball other) {
        if (this.inPocket || other.inPocket) return;

        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= basePhysics.BALL_RADIUS * 2) {
            double nx = dx / distance;
            double ny = dy / distance;
            double dvx = other.vx - this.vx;
            double dvy = other.vy - this.vy;
            double dotProduct = dvx * nx + dvy * ny;

            double impulse = 2 * dotProduct / 2;
            this.vx += impulse * nx;
            this.vy += impulse * ny;
            other.vx -= impulse * nx;
            other.vy -= impulse * ny;
        }
    }

    public boolean isStopped() {
        return vx == 0 && vy == 0;
    }

    public void draw(Graphics g) {
        if (!inPocket) {
            // Draw ball base
            g.setColor(color);
            g.fillOval((int) (x - basePhysics.BALL_RADIUS), (int) (y - basePhysics.BALL_RADIUS), basePhysics.BALL_RADIUS * 2, basePhysics.BALL_RADIUS * 2);

            // Draw stripes if applicable
            if (isStriped) {
                g.setColor(Color.WHITE);
                g.fillRect((int) (x - basePhysics.BALL_RADIUS / 2), (int) (y - basePhysics.BALL_RADIUS), basePhysics.BALL_RADIUS, basePhysics.BALL_RADIUS * 2);
            }

            // Draw ball number (except for the cue ball)
            if (number != 0) {
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(number), (int) x - 4, (int) y + 4);
            }
        }
    }
}
