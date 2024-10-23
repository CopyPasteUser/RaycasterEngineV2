package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
    private Sprite sprites[];
    private java.util.List<Sprite> initializedSprites;
    private GameMap map;
    private Player player;
    private Renderer renderer;
    private boolean mouseVisible = false; // Flag for cursor visibility
    private JFrame frame;

    public Game(JFrame frame) {
        this.frame = frame; // Save reference to JFrame
        map = new GameMap();
        player = new Player();
        renderer = new Renderer(map, player);

        setLayout(new BorderLayout());
        add(renderer, BorderLayout.CENTER);
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this); // Add MouseMotionListener
        addMouseListener(this);       // Add MouseListener for clicks

        // Set the cursor to be invisible (transparent cursor)
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(new byte[0]).getImage(), // Empty image
                new Point(0, 0),
                "invisible"));

        // Timer for game updates
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        timer.start();
    }

    private void update() {
        double moveSpeed = player.getMovementSpeed();

        // Move the player
        if (player.moveForward) player.moveForward(moveSpeed, map);
        if (player.moveBackward) player.moveBackward(moveSpeed, map);
        if (player.moveLeft) player.moveLeft(moveSpeed, map);
        if (player.moveRight) player.moveRight(moveSpeed, map);

        // Rotate the player
        if (player.turnLeft) player.turnLeft(0.03);
        if (player.turnRight) player.turnRight(0.03);

        renderer.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            player.moveBackward = false; // Disable backward movement
            player.moveForward = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            player.moveForward = false; // Disable forward movement
            player.moveBackward = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.moveRight = false; // Disable right movement
            player.moveLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.moveLeft = false; // Disable left movement
            player.moveRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) player.running = true;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            toggleCursorVisibility(); // Toggle cursor visibility
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if (player.getShooting() == false){
                player.setShooting(true);
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) player.moveForward = false;
        if (e.getKeyCode() == KeyEvent.VK_S) player.moveBackward = false;
        if (e.getKeyCode() == KeyEvent.VK_A) player.moveLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_D) player.moveRight = false;
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) player.running = false;


    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!mouseVisible) {
            // Berechne Mausbewegung
            int mouseX = e.getX();
            int mouseY = e.getY();
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            // Aktualisiere Spielerrotation basierend auf Mausbewegung
            player.turnRight((mouseX - centerX) * 0.001); // Horizontal: Yaw
            player.updatePitch((centerY - mouseY) * 0.001);  // Vertikal: Pitch

            // Mauszeiger wieder in die Mitte des Fensters setzen
            setCursorPosition(centerX, centerY);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            System.out.println("Left mouse button clicked");
            if (player.getShooting() == false){
                //Sound.playTone(100,0.1);
                player.setShooting(true);
            }
            // Handle left mouse click
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            System.out.println("Right mouse button clicked");
            // Handle right mouse click
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private void toggleCursorVisibility() {
        mouseVisible = !mouseVisible;
        if (mouseVisible) {
            // Make cursor visible
            setCursor(Cursor.getDefaultCursor());
        } else {
            // Set the cursor to be invisible
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                    new ImageIcon(new byte[0]).getImage(), // Empty image
                    new Point(0, 0),
                    "invisible"));
        }
    }

    private void setCursorPosition(int x, int y) {
        // Set the mouse cursor back to the center of the window
        try {
            Robot robot = new Robot();
            robot.mouseMove(getLocationOnScreen().x + x, getLocationOnScreen().y + y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void maximizeWindow() {
        // Maximize the window to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true); // Make the window visible
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("amongs sus");
        Game game = new Game(frame);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.maximizeWindow(); // Maximize window on start
        frame.setVisible(true);
    }
}
