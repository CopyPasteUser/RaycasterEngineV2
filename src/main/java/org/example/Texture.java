package org.example;

import java.awt.image.BufferedImage;

public class Texture{

    private BufferedImage image;
    private double sizeFactor; // Skalierungsfaktor für die Größe
    private String type; // Typ des Sprites, z.B. "enemy", "item", "prop"
    boolean movable;
    boolean animated;
    int animationFrames;
    int animationSpeed;

    public Texture(BufferedImage image,double sizeFactor,String type,boolean movable,boolean animated,int animationSpeed,int animationFrames){
        this.image = image;
        this.sizeFactor = sizeFactor;
        this.type = type;
        this.movable = movable;
        this.animated = animated;
        this.animationFrames = animationFrames;
        this.animationSpeed = animationSpeed;
    }

    // setters
    public void setImage(BufferedImage image){
        this.image = image;
    }
    public void setSizeFactor(double sizeFactor){
        this.sizeFactor = sizeFactor;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setMovable(boolean movable){
        this.movable = movable;
    }
    public void setAnimated(boolean animated){
        this.animated = animated;
    }
    public void setAnimationFrames(int animationFrames){
        this.animationFrames = animationFrames;
    }
    public void setAnimationSpeed(int animationSpeed){
        this.animationSpeed = animationSpeed;
    }

    // getters

    public BufferedImage getImage() {
        return image;
    }
    public double getSizeFactor() {
        return sizeFactor;
    }
    public String getType(){
        return type;
    }
    public boolean isMovable() {
        return movable;
    }
    public boolean isAnimated() {
        return animated;
    }
    public int getAnimationFrames() {
        return animationFrames;
    }
    public int getAnimationSpeed() {
        return animationSpeed;
    }
}