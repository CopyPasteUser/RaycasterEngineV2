package org.example;

import java.awt.image.BufferedImage;

public class Sprite {

    private BufferedImage image;
    private double sizeFactor; // Skalierungsfaktor für die Größe
    private String type; // Typ des Sprites, z.B. "enemy", "item", "prop"
    private double height;
    private double mapX; // Map-Koordinate X
    private double mapY; // Map-Koordinate Y
    boolean movable;
    boolean animated;
    int id;
    int animationFrames;
    String itemType;
    double hp;
    int animationSpeed;
    private long lastSpriteTime;
    private int currentRunCount;


    // Konstruktor
    public Sprite(BufferedImage image, double sizeFactor, double height, String type, boolean movable, int id, boolean animated, int animationFrames, double hp,int animationSpeed) {
        this.image = image;
        this.sizeFactor = sizeFactor;
        this.height = height;
        this.type = type;
        this.mapX = 0; // Standardwert
        this.mapY = 0; // Standardwert
        this.animationSpeed = animationSpeed;
        this.movable = movable;
        this.id = id;
        this.animated = animated;
        this.animationFrames = animationFrames; // Standardwert
        this.itemType = null;
        this.hp = hp;

        // lastSpriteTime initialisieren, aber nicht im Konstruktor setzen
        this.lastSpriteTime = 0; // Initialwert
    }

    // Methode zum Setzen der letzten Sprite-Zeit
    public void setLastSpriteTime(long lastSpriteTime) {
        this.lastSpriteTime = lastSpriteTime;
    }

    // Methode zum Abrufen der letzten Sprite-Zeit
    public long getLastSpriteTime() {
        return this.lastSpriteTime;
    }
    // Getter und Setter für currentRunCount
    public int getCurrentRunCount() {
        return currentRunCount;
    }

    public void setCurrentRunCount(int currentRunCount) {
        this.currentRunCount = currentRunCount;
    }

    // Weitere Methoden...

    public void setAnimationSpeed(int animationSpeed){
        this.animationSpeed = animationSpeed;
    }

    public int getAnimationSpeed(){
        return animationSpeed;
    }

    public void setMovable(boolean movable){
        this.movable = movable;
    }

    public void setType(String settedType){
        this.type = settedType;
    }

    public double getHp(){
        return hp;
    }

    public void addHp(double hpAdded){
        hp += hpAdded;
    }

    // Getter-Methoden
    public BufferedImage getImage() {
        return image;
    }

    public double getSizeFactor() {
        return sizeFactor;
    }

    public String getType() {
        return type;
    }

    public double getHeight() {
        return height;
    }

    public double getMapX() {
        return mapX;
    }

    public double getMapY() {
        return mapY;
    }

    public int getId(){
        return id;
    }

    public boolean getMovable(){
        return movable;
    }

    public boolean getAnimated() {
        return animated;
    }

    public int getAnimationFrames() {
        return animationFrames;
    }

    public String getItemType() {
        return itemType;
    }

    // Setter-Methoden

    public void addMapY(double mapY) {
        this.mapY += mapY;
    }

    public void addMapX(double mapX) {
        this.mapX += mapX;
    }

    public void addId(int id) { this.id += id; }

    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    public void setMapY(double mapY) {
        this.mapY = mapY;
    }

    public void setAnimationFrames(int animationFrames) {
        this.animationFrames = animationFrames;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }
}
