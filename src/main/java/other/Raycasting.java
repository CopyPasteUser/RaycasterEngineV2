package other;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Raycasting extends JPanel implements KeyListener {

    // Spielfeldkarte (1 = Wand, 0 = leerer Bereich)
    private int[][] map = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
    };

    // Spieler Position und Richtung
    private double playerX = 2.5, playerY = 2.5;  // Startposition
    private double playerDirX = -1, playerDirY = 0;  // Blickrichtung
    private double planeX = 0, planeY = 0.66;  // Kameraebene

    // Steuerungsvariablen
    private boolean moveForward, moveBackward, turnLeft, turnRight, running;

    // Konstruktor
    public Raycasting() {
        setPreferredSize(new Dimension(800, 400));  // Fenstergröße
        setFocusable(true);
        addKeyListener(this);
        Timer timer = new Timer(16, e -> update());
        timer.start();
    }

    // Aktualisiere Spielstatus
    private void update() {
        double baseSpeed = 0.05;  // Normale Bewegungsgeschwindigkeit des Spielers
        double moveSpeed = running ? baseSpeed * 2 : baseSpeed;  // Laufgeschwindigkeit bei gedrücktem Shift
        double rotSpeed = 0.03;   // Drehgeschwindigkeit

        // Bewegen vorwärts
        if (moveForward) {
            // Prüfen der Kollision mit Wänden beim Bewegen
            if (map[(int)(playerX + playerDirX * moveSpeed)][(int)playerY] == 0)
                playerX += playerDirX * moveSpeed;
            if (map[(int)playerX][(int)(playerY + playerDirY * moveSpeed)] == 0)
                playerY += playerDirY * moveSpeed;
        }
        // Bewegen rückwärts
        if (moveBackward) {
            // Prüfen der Kollision mit Wänden beim Bewegen
            if (map[(int)(playerX - playerDirX * moveSpeed)][(int)playerY] == 0)
                playerX -= playerDirX * moveSpeed;
            if (map[(int)playerX][(int)(playerY - playerDirY * moveSpeed)] == 0)
                playerY -= playerDirY * moveSpeed;
        }
        // Links drehen
        if (turnLeft) {
            double oldDirX = playerDirX;
            playerDirX = playerDirX * Math.cos(rotSpeed) - playerDirY * Math.sin(rotSpeed);
            playerDirY = oldDirX * Math.sin(rotSpeed) + playerDirY * Math.cos(rotSpeed);
            double oldPlaneX = planeX;
            planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
            planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
        }
        // Rechts drehen
        if (turnRight) {
            double oldDirX = playerDirX;
            playerDirX = playerDirX * Math.cos(-rotSpeed) - playerDirY * Math.sin(-rotSpeed);
            playerDirY = oldDirX * Math.sin(-rotSpeed) + playerDirY * Math.cos(-rotSpeed);
            double oldPlaneX = planeX;
            planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
            planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
        }

        repaint();
    }



    // Rendering der 3D-Ansicht
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        // Für jeden Strahl (jede Spalte)
        for (int x = 0; x < width; x++) {
            double cameraX = 2 * x / (double) width - 1;
            double rayDirX = playerDirX + planeX * cameraX;
            double rayDirY = playerDirY + planeY * cameraX;

            // In welcher Zelle auf der Karte befindet sich der Strahl?
            int mapX = (int) playerX;
            int mapY = (int) playerY;

            // Länge des Strahls von der aktuellen Position zur nächsten X- oder Y-Seite
            double sideDistX, sideDistY;

            // Länge des Strahls von einer X- oder Y-Seite zur nächsten
            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double perpWallDist;

            // Schritt und Richtung
            int stepX, stepY;
            boolean hit = false;
            int side = 0;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (playerX - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - playerX) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (playerY - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - playerY) * deltaDistY;
            }

            // DDA-Algorithmus (Digital Differential Analyzer)
            while (!hit) {
                // Gehe zum nächsten Kartenquadrat
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                // Prüfe, ob der Strahl auf eine Wand trifft
                if (map[mapX][mapY] > 0) hit = true;
            }

            // Berechne die genaue Entfernung zur Wand
            if (side == 0) perpWallDist = (mapX - playerX + (1 - stepX) / 2) / rayDirX;
            else perpWallDist = (mapY - playerY + (1 - stepY) / 2) / rayDirY;

            // Höhe der Wand
            int lineHeight = (int)(height / perpWallDist);

            // Zeichne die Wand
            int drawStart = -lineHeight / 2 + height / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + height / 2;
            if (drawEnd >= height) drawEnd = height - 1;

            // Farbe je nach Wandseite
            Color color = side == 1 ? Color.RED : Color.ORANGE;
            g.setColor(color);
            g.drawLine(x, drawStart, x, drawEnd);
        }

        // Minimap zeichnen (oben rechts)
        drawMiniMap(g);
    }

    // Zeichne Minimap
    private void drawMiniMap(Graphics g) {
        int mapSize = 100;  // Größe der Minimap
        int blockSize = mapSize / map.length;  // Größe der Map-Blöcke auf der Minimap
        int offsetX = getWidth() - mapSize - 20;  // Abstand von der rechten oberen Ecke
        int offsetY = 20;

        // Zeichne die Map
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 1) g.setColor(Color.GRAY);  // Wand
                else g.setColor(Color.WHITE);  // Freier Bereich
                g.fillRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
            }
        }

        // Zeichne den Spieler
        g.setColor(Color.BLUE);
        int playerMiniX = offsetX + (int)(playerX * blockSize);
        int playerMiniY = offsetY + (int)(playerY * blockSize);
        g.fillOval(playerMiniX - 3, playerMiniY - 3, 6, 6);  // Spieler als kleiner Punkt

        // Zeichne Blickrichtung
        int dirLineX = (int)(playerMiniX + playerDirX * 10);
        int dirLineY = (int)(playerMiniY + playerDirY * 10);
        g.setColor(Color.RED);
        g.drawLine(playerMiniX, playerMiniY, dirLineX, dirLineY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) moveForward = true;
        if (e.getKeyCode() == KeyEvent.VK_S) moveBackward = true;
        if (e.getKeyCode() == KeyEvent.VK_A) turnLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_D) turnRight = true;
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) running = true;  // Sprinten bei Shift
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) moveForward = false;
        if (e.getKeyCode() == KeyEvent.VK_S) moveBackward = false;
        if (e.getKeyCode() == KeyEvent.VK_A) turnLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_D) turnRight = false;
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) running = false;  // Sprinten stoppen bei Loslassen von Shift
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Raycasting Engine with Minimap");
        Raycasting game = new Raycasting();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
