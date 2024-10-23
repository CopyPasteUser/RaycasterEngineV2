package org.example;


import org.w3c.dom.ls.LSOutput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Renderer extends JPanel {
    private GameMap map;
    private Player player;
    private boolean moveRightToLeft = true;
    private HashMap<Integer, int[]> animationMap;
    double time;
    int currentWeaponRunCount = 0;
    private BufferedImage[] textures;
    private Sprite sprites[];
    private List<Sprite> initializedSprites;
    private long lastTime;
    private long lastWeaponSpriteTime;
    private int frameCount;
    private double fps;

    private BufferedImage frameBuffer;
    private static final int WIDTH = 400; // Updated width for 400 rays
    private static final int HEIGHT = 400; // Height remains the samed



    public Renderer(GameMap map, Player player) {
        this.map = map;
        this.player = player;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadAssets();
        setDoubleBuffered(true);
        lastTime = System.nanoTime();
        frameCount = 0;
        frameBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        initializeSprites();
        this.animationMap =initializeSpriteAnimations();


    }

    public void loadAssets() {
        textures = new BufferedImage[9];
        textures[0] = loadTexture("brick.png");
        textures[1] = loadTexture("stone.png");
        textures[2] = loadTexture("wood.png");
        textures[3] = loadTexture("bookshelf.png");
        textures[4] = loadTexture("grass.png");
        textures[5] = loadTexture("redbrick.png");
        textures[6] = loadTexture("stonetile.png");
        textures[7] = loadTexture("exit.png");
        textures[8] = loadTexture("door.png");

        sprites = new Sprite[38];


        int baseId = textures.length + 1;

        // static sprites
        sprites[0] = new Sprite(loadSprite("static_sprites\\barrel.png"), 0.3, 8, "prop", false, baseId + 0, false, 0, 0,0);
        sprites[1] = new Sprite(loadSprite("static_sprites\\heart.png"), 0.5, 4, "item", false, baseId + 1, false, 0, 0,0);
        sprites[27] = new Sprite(loadSprite("static_sprites\\pillar.png"), 0.2, 2, "prop", false, baseId + 27, false, 0, 0,0);
        sprites[36] = new Sprite(loadSprite("static_sprites\\shield.png"), 1, 4, "item", false, baseId + 36, false, 0, 0,0);
        sprites[37] = new Sprite(loadSprite("static_sprites\\mana.png"), 1, 4, "item", false, baseId + 37, false, 0, 0,0);




        // ghost
        sprites[2] = new Sprite(loadSprite("ghost_animation\\ghost.png"), 1, 2, "enemy", true, baseId + 2, true, 3, 20,5);
        sprites[3] = new Sprite(loadSprite("ghost_animation\\ghost_2.png"), 0, 0, "item", false, baseId + 3, false, 0, 0,0);
        sprites[4] = new Sprite(loadSprite("ghost_animation\\ghost_3.png"), 0, 0, "item", false, baseId + 4, false, 0, 0,0);


        // torch
        sprites[5] = new Sprite(loadSprite("torch_animation\\torch.png"), 1.25, 3, "prop", false, baseId + 5, true, 5, 0,4);
        sprites[6] = new Sprite(loadSprite("torch_animation\\torch_2.png"), 1, 0.6, "prop", false, baseId + 6, false, 0, 0,0);
        sprites[7] = new Sprite(loadSprite("torch_animation\\torch_3.png"), 1, 0.6, "prop", false, baseId + 7, false, 0, 0,0);
        sprites[8] = new Sprite(loadSprite("torch_animation\\torch_4.png"), 1, 0.6, "prop", false, baseId + 8, false, 0, 0,0);
        sprites[9] = new Sprite(loadSprite("torch_animation\\torch_5.png"), 1, 0.6, "prop", false, baseId + 9, false, 0, 0,0);

        // lamp animation
        sprites[10] = new Sprite(loadSprite("lamp_animation\\lamp.png"), 1, 0.6, "prop", false, baseId + 10, true, 2, 0,10);
        sprites[11] = new Sprite(loadSprite("lamp_animation\\lamp_2.png"), 1, 0.6, "prop", false, baseId + 11, false, 0, 0,0);

        //chest animation
        sprites[28] = new Sprite(loadSprite("chest_animation\\chest.png"), 2, 3, "prop", false, baseId + 28, true, 8, 0,8);
        sprites[29] = new Sprite(loadSprite("chest_animation\\chest_2.png"), 1, 0.6, "prop", false, baseId + 29, false, 0, 0,0);
        sprites[30] = new Sprite(loadSprite("chest_animation\\chest_3.png"), 1, 0.6, "prop", false, baseId + 30, false, 0, 0,0);
        sprites[31] = new Sprite(loadSprite("chest_animation\\chest_4.png"), 1, 0.6, "prop", false, baseId + 31, false, 0, 0,0);
        sprites[32] = new Sprite(loadSprite("chest_animation\\chest_5.png"), 1, 0.6, "prop", false, baseId + 32, false, 0, 0,0);
        sprites[33] = new Sprite(loadSprite("chest_animation\\chest_6.png"), 1, 0.6, "prop", false, baseId + 33, false, 0, 0,0);
        sprites[34] = new Sprite(loadSprite("chest_animation\\chest_7.png"), 1, 0.6, "prop", false, baseId + 34, false, 0, 0,0);
        sprites[35] = new Sprite(loadSprite("chest_animation\\chest_8.png"), 1, 0.6, "prop", false, baseId + 35, false, 0, 0,0);

        // shotgun animation
        sprites[12] = new Sprite(loadSprite("sg_animation\\sg_1.png"), 0, 0, "item", false, baseId + 12, true, 15, 0,2);
        sprites[13] = new Sprite(loadSprite("sg_animation\\sg_2.png"), 0, 0, "item", false, baseId + 13, false, 0, 0,0);
        sprites[14] = new Sprite(loadSprite("sg_animation\\sg_3.png"), 0, 0, "item", false, baseId + 14, false, 0, 0,0);
        sprites[15] = new Sprite(loadSprite("sg_animation\\sg_4.png"), 0, 0, "item", false, baseId + 15, false, 0, 0,0);
        sprites[16] = new Sprite(loadSprite("sg_animation\\sg_5.png"), 0, 0, "item", false, baseId + 16, false, 0, 0,0);
        sprites[17] = new Sprite(loadSprite("sg_animation\\sg_6.png"), 0, 0, "item", false, baseId + 17, false, 0, 0,0);
        sprites[18] = new Sprite(loadSprite("sg_animation\\sg_7.png"), 0, 0, "item", false, baseId + 18, false, 0, 0,0);
        sprites[19] = new Sprite(loadSprite("sg_animation\\sg_8.png"), 0, 0, "item", false, baseId + 19, false, 0, 0,0);
        sprites[20] = new Sprite(loadSprite("sg_animation\\sg_9.png"), 0, 0, "item", false, baseId + 20, false, 0, 0,0);
        sprites[21] = new Sprite(loadSprite("sg_animation\\sg_10.png"), 0, 0, "item", false, baseId + 21, false, 0, 0,0);
        sprites[22] = new Sprite(loadSprite("sg_animation\\sg_11.png"), 0, 0, "item", false, baseId + 22, false, 0, 0,0);
        sprites[23] = new Sprite(loadSprite("sg_animation\\sg_12.png"), 0, 0, "item", false, baseId + 23, false, 0, 0,0);
        sprites[24] = new Sprite(loadSprite("sg_animation\\sg_13.png"), 0, 0, "item", false, baseId + 24, false, 0, 0,0);
        sprites[25] = new Sprite(loadSprite("sg_animation\\sg_14.png"), 0, 0, "item", false, baseId + 25, false, 0, 0,0);
        sprites[26] = new Sprite(loadSprite("sg_animation\\sg_15.png"), 0, 0, "item", false, baseId + 26, false, 0, 0,0);
    }



    public int[] createFrameList(int start, int length) {
        // Erstelle ein Array mit der angegebenen Länge
        int[] frameList = new int[length];

        // Fülle das Array mit aufeinanderfolgenden Zahlen ab dem Startwert
        for (int i = 0; i < length; i++) {
            frameList[i] = start + i;
        }

        return frameList;
    }

    public HashMap<Integer, int[]>  initializeSpriteAnimations() {
        HashMap<Integer, int[]> animationMap = new HashMap<>();

        for (Sprite sprite : sprites) {
            if (sprite.getAnimated()) {
                int key = sprite.getId(); // ID des Sprites
                int[] sharedArray = createFrameList(sprite.getId(), sprite.getAnimationFrames());

                for(int i:sharedArray){
                    animationMap.put(i, sharedArray);
                  // System.out.println("Schlüssel " + i + " hat den Wert: " + Arrays.toString(animationMap.get(key)));

                }


                // Ausgabe der Zuweisung
            }
        }
        return animationMap;
    }



    private void renderWeapon(Graphics2D g) {
        boolean loopEnd = true;
        int xPosition = (int) (WIDTH / 2.5); // Fixed base X position
        int yPosition = (int) (HEIGHT * 0.7); // Fixed base Y position
        double piFactor;
        double timeFactor;
        int currentWeaponFrame = 12;


        if (player.isMoving()) {
            time += 0.1; // Increment time only when moving
            piFactor = 2;
            timeFactor = 2;

            if(player.running){
                piFactor = 0.99999;
                timeFactor = 0.99999;
            }

            if (time > Math.PI * piFactor) {
                time = 0; // Reset the time to restart the half-circle motion
                moveRightToLeft = !moveRightToLeft;
            }



            if (moveRightToLeft) {
                xPosition = (int) (xPosition + 10 * Math.cos(time/timeFactor)); // Right to left
            } else {
                xPosition = (int) (xPosition - 10 * Math.cos(time/timeFactor)); // Left to right
            }
            yPosition = (int) (yPosition + 20 * Math.sin(time/timeFactor)); // Vertical movement stays the same
        }
        if(player.getShooting() == true){
            for (Sprite sprite : sprites) {
                if(sprite.getAnimated()){
                    if (sprite.getId() >= 22 && sprite.getId() <= 36){
                      updateWeaponSpriteAnimation(List.of(sprites),lastWeaponSpriteTime,10000,this.animationMap.get(sprite.getId()),sprite,2);
                        currentWeaponFrame = sprite.getId()- textures.length ;


                        if (sprite.getId()==36){
                            player.setShooting(false);
                            sprite.setId(22);
                            currentWeaponFrame = sprite.getId() - textures.length ;
                        }
                    }
                }
            }

        }



        g.drawImage(sprites[currentWeaponFrame].getImage(), xPosition, yPosition, 90, 130, null);
        g.setColor(Color.RED);
        g.drawString("X",WIDTH/2, (int) (HEIGHT*0.55));

    }



    private void renderMinimap(Graphics2D g) {
        // Minimap Hintergrund
        final int MINIMAP_SIZE = 100; // Größe der Minimap
        final int MINIMAP_X = WIDTH - MINIMAP_SIZE - 10; // X-Position der Minimap
        final int MINIMAP_Y = HEIGHT - MINIMAP_SIZE - 10; // Y-Position der Minimap
        g.setColor(new Color(0, 0, 0, 150)); // Schwarz mit Transparenz
        g.fillRect(MINIMAP_X, MINIMAP_Y, MINIMAP_SIZE, MINIMAP_SIZE);

        // Minimap Raster
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int wallValue = map.getWallMapValue(x, y);
                if (wallValue > 0) {
                    if (wallValue >textures.length ){
                        if(sprites[wallValue- textures.length -1 ].getType().equals("enemy")){
                            continue;
                        }
                        else if(sprites[wallValue-textures.length -1].getType().equals("item")){
                            continue;
                        }
                        else if(sprites[wallValue-textures.length-1 ].getType().equals("prop")){
                            g.setColor(Color.YELLOW);
                        }
                    }
                    else {
                        g.setColor(Color.GRAY);
                    }

                    int miniX = MINIMAP_X + (int) ((x / (double) map.getWidth()) * MINIMAP_SIZE);
                    int miniY = MINIMAP_Y + (int) ((y / (double) map.getHeight()) * MINIMAP_SIZE);
                    g.fillRect(miniX, miniY, MINIMAP_SIZE / map.getWidth(), MINIMAP_SIZE / map.getHeight());
                }
            }
        }

        // Spielerposition auf der Minimap
        int playerMiniX = MINIMAP_X + (int) ((player.getX() / (double) map.getWidth()) * MINIMAP_SIZE);
        int playerMiniY = MINIMAP_Y + (int) ((player.getY() / (double) map.getHeight()) * MINIMAP_SIZE);
        g.setColor(Color.BLUE); // Spielerfarbe
        g.fillRect(playerMiniX, playerMiniY, 5, 5); // Spieler als kleines Quadrat

        // Blickrichtung des Spielers
        double playerAngle = Math.atan2(player.getDirY(), player.getDirX());
        int dirX = (int) (Math.cos(playerAngle) * 10); // Länge der Blickrichtung
        int dirY = (int) (Math.sin(playerAngle) * 10);
        g.setColor(Color.RED); // Blickrichtung
        g.drawLine(playerMiniX + 2, playerMiniY + 2, playerMiniX + 2 + dirX, playerMiniY + 2 + dirY);

        // Gegnerpositionen auf der Minimap
        for (Sprite sprite : initializedSprites) {
            if (sprite.getType().equals("enemy")) {
                int enemyMiniX = MINIMAP_X + (int) ((sprite.getMapX() / (double) map.getWidth()) * MINIMAP_SIZE);
                int enemyMiniY = MINIMAP_Y + (int) ((sprite.getMapY() / (double) map.getHeight()) * MINIMAP_SIZE);
                g.setColor(Color.RED); // Gegnerfarbe
                g.fillRect(enemyMiniX, enemyMiniY, 5, 5); // Gegner als kleines Quadrat
            }
            if (sprite.getType().equals("item")) {
                int enemyMiniX = MINIMAP_X + (int) ((sprite.getMapX() / (double) map.getWidth()) * MINIMAP_SIZE);
                int enemyMiniY = MINIMAP_Y + (int) ((sprite.getMapY() / (double) map.getHeight()) * MINIMAP_SIZE);
                g.setColor(Color.GREEN); // Gegnerfarbe
                g.fillRect(enemyMiniX, enemyMiniY, 5, 5); // Gegner als kleines Quadrat
            }
        }
    }





    private BufferedImage loadTexture(String path) {
        String texturePrefix = "C:\\Users\\Simon Frank\\IdeaProjects\\doomers\\src\\main\\resources\\textures\\";
        try {
            return ImageIO.read(new File(texturePrefix + path));
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Textur: " + texturePrefix + path);
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage loadSprite(String path) {
        String spritePrefix = "C:\\Users\\Simon Frank\\IdeaProjects\\doomers\\src\\main\\resources\\sprites\\";
        try {
            return ImageIO.read(new File(spritePrefix + path));
        } catch (IOException e) {
            System.err.println("Fehler beim Laden des Sprites: " + spritePrefix + path);
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderToBuffer(); // Aufruf der nicht-parallelen Render-Methode
        g.drawImage(frameBuffer, 0, 0, getWidth(), getHeight(), null);

        drawFPS(g);
    }

    private void renderToBuffer() {
        int width = frameBuffer.getWidth();
        int height = frameBuffer.getHeight();


        Graphics2D g = frameBuffer.createGraphics();
        g.setColor(Color.BLACK); // Buffer löschen
        g.fillRect(0, 0, width, height);

        updateSprites(g);

        renderScene(g, width, height);



        renderSprites(g);

        renderHealthBar(g);
        renderMinimap(g);
        renderWeapon(g);


        g.dispose();
    }

    public void initializeSprites() {

        initializedSprites = new ArrayList<Sprite>();  // Liste für die initialisierten Sprites

        // Gehe die gesamte Map durch, um nach Sprites zu suchen
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                int wallMapValue = map.getWallMapValue(x, y);

                // Überprüfe, ob der Map-Wert einem Sprite entspricht
                if (wallMapValue > textures.length) {
                    int spriteID = wallMapValue - (textures.length+1);



                    // Setze die Position für das Sprite (mit einem Offset von 0.5)
                    double spriteX = x + 0.5;
                    double spriteY = y + 0.5;

                    sprites[spriteID].setMapX(spriteX);
                    sprites[spriteID].setMapY(spriteY);

                    //lade daten
                    BufferedImage image = sprites[spriteID].getImage();
                    double sizeFactor = sprites[spriteID].getSizeFactor();
                    double height = sprites[spriteID].getHeight();
                    String type = sprites[spriteID].getType();
                    boolean movable = sprites[spriteID].getMovable();
                    int id = sprites[spriteID].getId();
                    boolean animated = sprites[spriteID].getAnimated();
                    int animationFrames = sprites[spriteID].getAnimationFrames();
                    double hp = sprites[spriteID].getHp();
                    int animationSpeed = sprites[spriteID].getAnimationSpeed();



                    // Erstelle ein neues Sprite-Objekt
                    Sprite sprite = new Sprite(image,sizeFactor,height,type,movable,id,animated,animationFrames,hp,animationSpeed);
                    sprite.setMapX(sprites[spriteID].getMapX());
                    sprite.setMapY(sprites[spriteID].getMapY());
                    // Füge das Sprite zur Liste hinzu
                    initializedSprites.add(sprite);

                }
            }
        }


    }


    // Methode zur Überprüfung der Sichtlinie (Line of Sight) mit Wandwerten 1-4
    private boolean hasLineOfSight(double playerX, double playerY, double spriteX, double spriteY) {
        // Schritte in X und Y Richtung berechnen
        double dx = spriteX - playerX;
        double dy = spriteY - playerY;

        // Schrittweite pro Iteration
        double stepSize = 0.1;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double stepX = dx / distance * stepSize;
        double stepY = dy / distance * stepSize;

        // Raycasting von der Spielerposition bis zum Sprite
        double currentX = playerX;
        double currentY = playerY;
        for (double i = 0; i < distance; i += stepSize) {
            // Rundung der aktuellen Position, um auf Kartenkoordinaten zu prüfen
            int mapX = (int) currentX;
            int mapY = (int) currentY;

            // Prüfe, ob der aktuelle Punkt auf eine Wand trifft (Wert 1 bis texturen amount)
            int mapValue = map.getWallMapValue(mapX, mapY);
            if (mapValue >= 1 && mapValue <= textures.length ) {
                return false; // Es gibt eine Wand zwischen Spieler und Sprite
            }

            // Aktuelle Position in Richtung des Sprites aktualisieren
            currentX += stepX;
            currentY += stepY;
        }

        // Kein Hindernis zwischen Spieler und Sprite
        return true;
    }



    public void updateSpriteAnimation(
            List<Sprite> initializedSprites, // Liste aller initialisierten Sprites
            int spriteChangeInterval,        // Intervall für den Sprite-Wechsel (z.B. in Millisekunden)
            int[] spriteIds,                 // Array der möglichen Sprite-IDs
            Sprite currentSprite,            // Aktuell anzuzeigender Sprite
            int maxRunsPerId                 // Maximale Durchläufe pro Sprite-ID
    ) {
        long currentSpriteTime = System.nanoTime(); // Aktuelle Zeit in Nanosekunden

        // Überprüfen, ob das Intervall für einen Sprite-Wechsel vergangen ist
        if (currentSpriteTime - currentSprite.getLastSpriteTime() >= spriteChangeInterval) {
            // Zeit aktualisieren, nachdem das Intervall vorbei ist
            currentSprite.setLastSpriteTime(currentSpriteTime);

            int currentId = currentSprite.getId(); // Aktuelle ID des Sprites

            // Finde den Index der aktuellen ID im Array
            int currentIndex = -1;
            for (int i = 0; i < spriteIds.length; i++) {
                if (spriteIds[i] == currentId) {
                    currentIndex = i;
                    break;
                }
            }

            // Wenn die aktuelle ID im Array gefunden wurde
            if (currentIndex != -1) {
                // Überprüfen, ob der Sprite die maximale Anzahl an Durchläufen erreicht hat
                if (currentSprite.getCurrentRunCount() < maxRunsPerId) {
                    // Zähler erhöhen
                    currentSprite.setCurrentRunCount(currentSprite.getCurrentRunCount() + 1);
                } else {
                    // Reset des Zählers und Bestimmung der nächsten ID im Array (zyklisch)
                    currentSprite.setCurrentRunCount(1); // Zähler zurücksetzen auf 1 für die neue ID
                    int nextIndex = (currentIndex + 1) % spriteIds.length;
                    int nextId = spriteIds[nextIndex];

                    // Setze die nächste ID für den Sprite
                    for (Sprite changeSprite : initializedSprites) {
                        if (changeSprite.getId() == currentId) {
                            changeSprite.setId(nextId);
                        }
                    }
                }
            }
        }
    }


    public void updateWeaponSpriteAnimation(
            List<Sprite> initializedSprites,
            long lastSpriteTime,
            int spriteChangeInterval,
            int[] spriteIds,
            Sprite currentSprite,
            int maxRunsPerId //Maximal zulässige Durchläufe als Parameter
    ) {
        long currentWeaponSpriteTime = System.nanoTime();

        // Überprüfen, ob das Intervall für einen Sprite-Wechsel vergangen ist
        if (currentWeaponSpriteTime - this.lastWeaponSpriteTime >= spriteChangeInterval) {
            // Zeit aktualisieren NACHDEM das Intervall vorbei ist
            this.lastWeaponSpriteTime = currentWeaponSpriteTime;

            Sprite sprite = currentSprite;
            int currentId = sprite.getId();

            // Finde den Index der aktuellen ID im Array
            int currentIndex = -1;
            for (int i = 0; i < spriteIds.length; i++) {
                if (spriteIds[i] == currentId) {
                    currentIndex = i;
                    break;
                }
            }

            // Wenn die aktuelle ID im Array gefunden wurde
            if (currentIndex != -1) {
                // Überprüfen, ob wir die maximale Anzahl an Durchläufen erreicht haben
                if (currentWeaponRunCount < maxRunsPerId) {
                    // Zähler erhöhen
                    currentWeaponRunCount++;
                } else {
                    // Reset des Zählers und Bestimmung der nächsten ID im Array (zyklisch)
                    currentWeaponRunCount = 1; // Zähler zurücksetzen auf 1 für die neue ID
                    int nextIndex = (currentIndex + 1) % spriteIds.length;
                    int nextId = spriteIds[nextIndex];

                    // Setze die nächste ID für den Sprite
                    for (Sprite changeSprite : initializedSprites) {
                        if (changeSprite.getId() == currentId) {
                            changeSprite.setId(nextId);
                        }
                    }
                }
            }
        }
    }



    public void updateSprites(Graphics g) {

        for (Sprite sprite : initializedSprites) {
            double playerX = player.getX();
            double playerY = player.getY();
            double dirX = player.getDirX();
            double dirY = player.getDirY();
            double fov = player.calculateFOV();
            double dx = sprite.getMapX() - playerX;
            double dy = sprite.getMapY() - playerY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (sprite.getAnimated()) {



                if(animationMap.containsKey(sprite.getId())){
                    updateSpriteAnimation(initializedSprites, 100000 ,this.animationMap.get(sprite.getId()),sprite,sprite.getAnimationSpeed() );
                }




            }

            // Normalisiere den Richtungsvektor zum Gegner
            double directionToEnemyX = dx / distance;
            double directionToEnemyY = dy / distance;

            // Berechne das Skalarprodukt zwischen dem Blickrichtungsvektor und dem Vektor zum Gegner
            double dotProduct = dirX * directionToEnemyX + dirY * directionToEnemyY;

            // Berechne den Winkel zwischen der Blickrichtung des Spielers und dem Vektor zum Gegner
            double angleToEnemy = Math.acos(dotProduct);

            // Vergleiche den Winkel mit dem halben Sichtfeld (FOV)
            double halfFOV = Math.toRadians(fov /2);

            // Überprüfe, ob der Gegner im Sichtfeld ist
            if (angleToEnemy < halfFOV) {
                // Der Gegner ist im Sichtfeld des Spielers
                if (player.getShooting() && distance < 5 && sprite.getType().equals("enemy") && hasLineOfSight(playerX, playerY, sprite.getMapX(), sprite.getMapY())) {
                    Sound.playTone(300, 0.05); // Spiele den Ton nur ab, wenn der Spieler in die Richtung des Gegners schaut
                    sprite.addHp(-0.35);


                    if (sprite.getHp() <=  0 ){
                        sprite.setMovable(false);
                        sprite.setType("prop");
                        sprite.setAnimated(false);
                        sprite.addMapY(100);
                    }
                }
            }



            // Wenn die Distanz unter 1.5 ist, handle spezielle Sprite-Typen
            if (distance < 1.5) {
                String spriteType = sprite.getType(); // Typ des Sprites abrufen


                if (spriteType.equals("item")) {
                    int hpToHeal = 20 - player.getHP();
                    player.addHP((short) hpToHeal); // Beispiel für Interaktion mit "item"-Sprites
                    sprite.addMapY(100);
                    Sound.playTone(440, 0.2); // Spiele einen 440 Hz Ton für 0.2 Sekunden
                }

                if (spriteType.equals("enemy")) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - player.getLastDamageTime() >= 1000) { // 1000 ms
                        // Nur Schaden zufügen, wenn der Cooldown abgelaufen ist
                        player.removeHP((short) 1); // Beispiel für Interaktion mit "enemy"-Sprites
                        player.setLastDamageTime(currentTime); // Schaden wurde genommen, Cooldown zurücksetzen
                        Sound.playTone(220, 0.25); // Spiele einen 220 Hz Ton für 0.2 Sekunden
                    }
                }
            }

            // Bewegung für bewegliche Sprites (z.B. Gegner)
            if (sprite.movable) {
                if (hasLineOfSight(playerX, playerY, sprite.getMapX(), sprite.getMapY()) && distance < 17.5) {
                    // Berechne Bewegungsrichtung vom Sprite zum Spieler
                    double directionX = playerX - sprite.getMapX();
                    double directionY = playerY - sprite.getMapY();
                    directionX /= distance;
                    directionY /= distance;

                    // Bewege das Sprite in Richtung des Spielers
                    double moveSpeed = 0.03; // Passe die Geschwindigkeit nach Bedarf an
                    sprite.setMapX(sprite.getMapX() + directionX * moveSpeed);
                    sprite.setMapY(sprite.getMapY() + directionY * moveSpeed);
                }
            }
        }
    }

    private void renderSprites(Graphics2D g) {
        List<double[]> spritesToRender = new ArrayList<>();

        // Berechne häufig verwendete Werte nur einmal
        double playerX = player.getX();
        double playerY = player.getY();
        double dirX = player.getDirX();
        double dirY = player.getDirY();
        double fov = player.calculateFOV();
        double halfWidth = WIDTH / 2.0;
        double halfFovTan = Math.tan(Math.toRadians(fov / 2.0));
        double scaleFactorBase = 5; // Skalierungsbasis

        // Calculate pitch
        double pitch = Math.tan(player.getDirV()); // Pitch is the tangent of vertical direction
        int pitchOffset = (int) (pitch * HEIGHT); // Calculate pixel offset based on pitch

        // Iteriere über die bereits initialisierten Sprites
        for (Sprite sprite : initializedSprites) {
            if (!hasLineOfSight(playerX, playerY, sprite.getMapX(), sprite.getMapY())) {
                continue; // Sprite überspringen, wenn keine Sichtlinie besteht
            }

            // Berechne Differenzen und Entfernung
            double dx = sprite.getMapX() - playerX;
            double dy = sprite.getMapY() - playerY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Schnelle Abbruchbedingung: Wenn zu nah oder zu weit weg
            if (distance < 0.1 || distance > 50.0) {
                continue;
            }

            // Winkelberechnung und FOV-Abprüfung
            double dotProduct = dirX * dx + dirY * dy;
            double crossProduct = dirX * dy - dirY * dx;
            double angleDifference = Math.toDegrees(Math.atan2(crossProduct, dotProduct));

            if (Math.abs(angleDifference) > fov / 1.5) {
                continue; // Sprite außerhalb des FOV
            }

            // Berechne die projizierte X-Position auf dem Bildschirm
            double tanAngleDiff = Math.tan(Math.toRadians(angleDifference));
            double screenX = halfWidth * (1 - (tanAngleDiff / halfFovTan));

            // Skalierungsfaktor basierend auf der Entfernung
            double scaleFactor = scaleFactorBase / distance;

            // Berechne die Größe und Position des Sprites
            BufferedImage spriteImage = sprite.getImage();
            double sizeFactor = sprite.getSizeFactor();
            int spriteHeight = (int) (spriteImage.getHeight(null) * scaleFactor * sizeFactor);
            int spriteWidth = (int) (spriteImage.getWidth(null) * scaleFactor * sizeFactor);

            // Apply pitch offset to the sprite rendering
            int spriteScreenY = (int) ((HEIGHT / 2) - (spriteHeight / sprite.getHeight())) + pitchOffset;

            // Füge nur Sprites hinzu, die sichtbar und sinnvoll sind
            if (screenX + spriteWidth / 2 >= 0 && screenX - spriteWidth / 2 < WIDTH) {
                spritesToRender.add(new double[]{distance, screenX, spriteScreenY, spriteWidth, spriteHeight, sprite.getId()});
            }
        }

        // Sortiere die Sprites nach Distanz (absteigend) für korrektes Rendern
        spritesToRender.sort((a, b) -> Double.compare(b[0], a[0]));

        // Zeichne die Sprites in der richtigen Reihenfolge
        for (int i = 0; i < spritesToRender.size(); i++) {
            double[] spriteData = spritesToRender.get(i);

            int spriteID = (int) spriteData[5] - (textures.length + 1);
            BufferedImage sprite = sprites[spriteID].getImage();
            g.drawImage(sprite, (int) spriteData[1] - (int) spriteData[3] / 2, (int) spriteData[2], (int) spriteData[3], (int) spriteData[4], null);
        }
    }


    private void renderScene(Graphics2D g, int width, int height) {
        // Array to store the wall heights
        int[] wallHeightStart = new int[width];
        int[] wallHeightEnd = new int[width];

        // Calculate pitch (vertical direction) based on player.getDirV()
        double pitch = Math.tan(player.getDirV());  // Pitch is the tangent of vertical direction
        int pitchOffset = (int) (pitch * height);  // Calculate pixel offset based on pitch

        // Render walls
        renderWalls(width, height, wallHeightStart, wallHeightEnd, pitchOffset);

        // Render floor
        renderFloor(width, height, wallHeightStart, pitchOffset);

        // Render ceiling
        renderCeiling(width, height, wallHeightEnd, pitchOffset);
    }

    private void renderWalls(int width, int height, int[] wallHeightStart, int[] wallHeightEnd, int pitchOffset) {
        for (int x = 0; x < width; x++) {
            double cameraX = 2.0 * x / width - 1;
            double rayDirX = player.getDirX() + player.getPlaneX() * cameraX;
            double rayDirY = player.getDirY() + player.getPlaneY() * cameraX;

            int mapX = (int) player.getX();
            int mapY = (int) player.getY();

            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double sideDistX, sideDistY;

            int stepX = (rayDirX < 0) ? -1 : 1;
            sideDistX = (stepX == -1) ? (player.getX() - mapX) * deltaDistX : (mapX + 1.0 - player.getX()) * deltaDistX;

            int stepY = (rayDirY < 0) ? -1 : 1;
            sideDistY = (stepY == -1) ? (player.getY() - mapY) * deltaDistY : (mapY + 1.0 - player.getY()) * deltaDistY;

            // DDA algorithm to find the wall
            boolean hit = false;
            int side = 0;

            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0; // X-axis wall
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1; // Y-axis wall
                }

                // Check for a wall hit
                if (map.getWallMapValue(mapX, mapY) > 0 && map.getWallMapValue(mapX, mapY) <= textures.length) {
                    hit = true;
                }
            }

            double perpWallDist = (side == 0)
                    ? (mapX - player.getX() + (1 - stepX) / 2) / rayDirX
                    : (mapY - player.getY() + (1 - stepY) / 2) / rayDirY;

            int lineHeight = (int) (height / perpWallDist);

            // Apply pitch offset to wall rendering
            int drawStart = Math.max(-lineHeight / 2 + height / 2 + pitchOffset, 0);
            int drawEnd = Math.min(lineHeight / 2 + height / 2 + pitchOffset, height - 1);

            // Store the wall height for floor and ceiling rendering
            wallHeightStart[x] = drawEnd; // Save the end position of the wall
            wallHeightEnd[x] = drawStart;

            int texNum = map.getWallMapValue(mapX, mapY) - 1; // Map value is expected to be 1-based
            if (texNum < 0 || texNum >= textures.length) {
                continue; // Skip this ray if texNum is invalid
            }

            double wallX = (side == 0)
                    ? player.getY() + perpWallDist * rayDirY
                    : player.getX() + perpWallDist * rayDirX;
            wallX -= Math.floor(wallX);

            int texX = (int) (wallX * textures[texNum].getWidth());
            int[] textureColors = textures[texNum].getRGB(texX, 0, 1, textures[texNum].getHeight(), null, 0, 1);

            // Render the wall
            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (int) ((y + (lineHeight / 2 - height / 2 - pitchOffset)) / (double) lineHeight * textures[texNum].getHeight());
                if (texY >= 0 && texY < textures[texNum].getHeight()) {
                    int color = textureColors[texY];
                    frameBuffer.setRGB(x, y, color);
                }
            }
        }
    }

    private void renderFloor(int width, int height, int[] wallHeightStart, int pitchOffset) {
        for (int y = height / 2 + pitchOffset; y < height; y++) {
            double rayDirX0 = player.getDirX() - player.getPlaneX();
            double rayDirY0 = player.getDirY() - player.getPlaneY();
            double rayDirX1 = player.getDirX() + player.getPlaneX();
            double rayDirY1 = player.getDirY() + player.getPlaneY();

            int p = y - height / 2 - pitchOffset;
            double posZ = 0.5 * height; // Camera height
            double rowDistance = posZ / p; // Distance from camera to floor

            double floorStepX = rowDistance * (rayDirX1 - rayDirX0) / width;
            double floorStepY = rowDistance * (rayDirY1 - rayDirY0) / width;

            double floorX = player.getX() + rowDistance * rayDirX0;
            double floorY = player.getY() + rowDistance * rayDirY0;

            for (int x = 0; x < width; x++) {
                int mapX = (int) Math.floor(floorX);
                int mapY = (int) Math.floor(floorY);

                // Ensure the position is within map bounds
                if (mapX >= 0 && mapX < map.getWidth() && mapY >= 0 && mapY < map.getHeight()) {
                    int floorTextureId = map.getFloorMapValue(mapX, mapY) - 1;

                    if (floorTextureId >= 0 && floorTextureId < textures.length) {
                        BufferedImage floorTexture = textures[floorTextureId];
                        int floorTextureWidth = floorTexture.getWidth();
                        int floorTextureHeight = floorTexture.getHeight();

                        int texX = (int) (floorTextureWidth * (floorX - Math.floor(floorX))) & (floorTextureWidth - 1);
                        int texY = (int) (floorTextureHeight * (floorY - Math.floor(floorY))) & (floorTextureHeight - 1);

                        int color = floorTexture.getRGB(texX, texY);

                        // Only render the floor if we are below the end of the wall
                        if (y >= wallHeightStart[x]) {
                            frameBuffer.setRGB(x, y, color);
                        }
                    }
                }

                floorX += floorStepX;
                floorY += floorStepY;
            }
        }
    }

    private void renderCeiling(int width, int height, int[] wallHeightEnd, int pitchOffset) {
        for (int y = 0; y < height / 2 + pitchOffset; y++) {
            double rayDirX0 = player.getDirX() - player.getPlaneX();
            double rayDirY0 = player.getDirY() - player.getPlaneY();
            double rayDirX1 = player.getDirX() + player.getPlaneX();
            double rayDirY1 = player.getDirY() + player.getPlaneY();

            int p = y - height / 2 - pitchOffset;
            double posZ = 0.5 * height;  // Camera height
            double rowDistance = posZ / Math.abs(p);  // Distance from camera to ceiling

            double ceilingStepX = rowDistance * (rayDirX1 - rayDirX0) / width;
            double ceilingStepY = rowDistance * (rayDirY1 - rayDirY0) / width;

            double ceilingX = player.getX() + rowDistance * rayDirX0;
            double ceilingY = player.getY() + rowDistance * rayDirY0;

            for (int x = 0; x < width; x++) {
                int mapX = (int) Math.floor(ceilingX);
                int mapY = (int) Math.floor(ceilingY);

                // Ensure the position is within map bounds
                if (mapX >= 0 && mapX < map.getWidth() && mapY >= 0 && mapY < map.getHeight()) {
                    int ceilingTextureId = map.getCeilingMapValue(mapX, mapY) - 1;

                    if (ceilingTextureId >= 0 && ceilingTextureId < textures.length) {
                        BufferedImage ceilingTexture = textures[ceilingTextureId];
                        int ceilingTextureWidth = ceilingTexture.getWidth();
                        int ceilingTextureHeight = ceilingTexture.getHeight();

                        int texX = (int) (ceilingTextureWidth * (ceilingX - Math.floor(ceilingX))) & (ceilingTextureWidth - 1);
                        int texY = (int) (ceilingTextureHeight * (ceilingY - Math.floor(ceilingY))) & (ceilingTextureHeight - 1);

                        int color = ceilingTexture.getRGB(texX, texY);

                        // Only render the ceiling if we are above the start of the wall
                        if (y <= wallHeightEnd[x]) {
                            frameBuffer.setRGB(x, y, color);
                        }
                    }
                }

                ceilingX += ceilingStepX;
                ceilingY += ceilingStepY;
            }
        }
    }

    public void renderHealthBar(Graphics2D g) {
        int maxHP = 20;
        int currentHP = player.getHP();

        // Berechne das Verhältnis der aktuellen HP zu den maximalen HP
        double healthPercentage = (double) currentHP / maxHP;

        // Breite und Höhe der Lebensanzeige
        int barWidth = 40;
        int barHeight = 20;

        // Position der Lebensanzeige auf dem Bildschirm (z.B. links oben)
        int barX = 5;
        int barY = 20;

        // Berechne die aktuelle Breite der gefüllten Lebensanzeige basierend auf den HP
        int filledBarWidth = (int) (barWidth * healthPercentage);

        // Farbverlauf von grün (volle HP) nach rot (niedrige HP)
        Color healthColor = new Color(
                (int) Math.min(255, 255 * (1 - healthPercentage)), // Rot erhöht sich bei sinkenden HP
                (int) Math.min(255, 255 * healthPercentage),       // Grün sinkt bei sinkenden HP
                0 // Blau bleibt 0
        );

        // Zeichne den Rahmen der Lebensanzeige (unverändert)
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);

        // Zeichne den gefüllten Teil der Lebensanzeige basierend auf den HP
        g.setColor(healthColor);
        g.fillRect(barX + 1, barY + 1, filledBarWidth - 1, barHeight - 1); // Fülle das Rechteck (etwas kleiner als der Rahmen)
        g.drawString(String.valueOf((int)player.getHP()),30 ,15);
    }


    private void drawFPS(Graphics g) {
        long currentTime = System.nanoTime();
        frameCount++;
        if (currentTime - lastTime >= 1_000_000_000) {
            fps = frameCount;
            frameCount = 0;
            lastTime = currentTime;
        }
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + fps, 10, 20);
    }

}
