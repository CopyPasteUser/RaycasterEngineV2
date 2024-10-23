package org.example;

public class Player {
    // Spieler Position und Richtung
    private boolean shooting = false;
    private long lastDamageTime = 0;
    private double x = 2.5; // Startposition x
    private double y = 2.5; // Startposition y
    private double planeX = 0.6; // Kameraebene x
    private double planeY = 0; // Kameraebene y
    private double dirX = 0; // Blickrichtung x
    private double dirY = 0.9; // Blickrichtung y
    private double dirV = 0;
    private double playerSprintSpeed = 0.15;
    private double playerSpeed = 0.25;
    private short HP = 20;

    public void updatePitch(double amount) {
        // Aktualisiere dirV und stelle sicher, dass er innerhalb der Grenzen bleibt
        dirV += amount;

        // Beschränke dirV zwischen -0.3 und 0.3
        if (dirV > 0.4) {
            dirV = 0.4;
        } else if (dirV < -0.4) {
            dirV = -0.4;
        }
    }



    public double getDirV(){
        return dirV;
    }
    public boolean getShooting(){
        return shooting;
    }

    public void setShooting(boolean shooting){
        this.shooting = shooting;
    }

    public double calculateFOV() {
        // Die Länge des Vektors der Kameraebene (planeX, planeY) gibt die Weite des Sichtfeldes an
        double planeLength = Math.sqrt(planeX * planeX + planeY * planeY);
        double fov = 2 * Math.toDegrees(Math.atan(planeLength));
        return fov;
    }

    public long getLastDamageTime() {
        return lastDamageTime;
    }

    public void setLastDamageTime(long lastDamageTime){
        this.lastDamageTime = lastDamageTime;
    }
    public short getHP(){
        return HP;
    }

    public void addHP(short hp){
      if(HP != 20){
          HP += hp;
      }
    }

    public void removeHP(short hp){
        if(HP != 0){
            HP -= hp;
        }
    }

    // Spielergröße
    private double radius = 0.15; // Radius des Spielers
    private double height = 0.55; // Beispielhöhe des Spielers

    // Steuerungsvariablen
    public boolean moveForward;
    public boolean moveBackward;
    public boolean moveLeft;  // Hinzugefügt für seitliche Bewegung
    public boolean moveRight; // Hinzugefügt für seitliche Bewegung
    public boolean turnLeft;
    public boolean turnRight;
    public boolean running;

    // Getter für die Spielerposition und Blickrichtung
    public double getX() {
        return x;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }

    public double getPlaneX() {
        return planeX;
    }

    public double getPlaneY() {
        return planeY;
    }

    public double getMovementSpeed() {
        return running ? playerSpeed : playerSprintSpeed; // Geschwindigkeit abhängig vom Sprinten
    }

    public boolean isMoving() {
        boolean isMoving = false;
        if (moveForward) {
            isMoving = true;
        }
        if (moveBackward) {
            isMoving = true;
        }

        if (moveLeft) {
            isMoving = true;
        }
        if (moveRight) {
            isMoving = true;
        }
        return isMoving;
    }


    // Neue Methode: Überprüfen, ob der Wert NICHT zwischen 1 und 4 liegt
    public boolean isCollidable(int value) {
        return value < 1 || value > 9;
    }

    public void moveForward(double speed, GameMap map) {
        // Prüfen der Kollision mit Wänden beim Bewegen
        if (isCollidable(map.getWallMapValue((int)(x + dirX * speed + radius), (int)y)) &&
                isCollidable(map.getWallMapValue((int)(x + dirX * speed - radius), (int)y))) {
            x += dirX * speed; // Bewege nur, wenn kein Zusammenstoß
        }

        if (isCollidable(map.getWallMapValue((int)x, (int)(y + dirY * speed + radius))) &&
                isCollidable(map.getWallMapValue((int)x, (int)(y + dirY * speed - radius)))) {
            y += dirY * speed; // Bewege nur, wenn kein Zusammenstoß
        }
    }

    public void moveBackward(double speed, GameMap map) {
        // Prüfen der Kollision mit Wänden beim Bewegen
        if (isCollidable(map.getWallMapValue((int)(x - dirX * speed + radius), (int)y)) &&
                isCollidable(map.getWallMapValue((int)(x - dirX * speed - radius), (int)y))) {
            x -= dirX * speed; // Bewege nur, wenn kein Zusammenstoß
        }

        if (isCollidable(map.getWallMapValue((int)x, (int)(y - dirY * speed + radius))) &&
                isCollidable(map.getWallMapValue((int)x, (int)(y - dirY * speed - radius)))) {
            y -= dirY * speed; // Bewege nur, wenn kein Zusammenstoß
        }
    }

    public void moveLeft(double speed, GameMap map) {
        double leftDirX = -dirY;
        double leftDirY = dirX;

        // Reduziere die seitliche Geschwindigkeit
        double sideSpeed = speed * 0.7; // Zum Beispiel 70% der normalen Geschwindigkeit

        if (isCollidable(map.getWallMapValue((int)(x + leftDirX * sideSpeed + radius), (int)y)) &&
                isCollidable(map.getWallMapValue((int)(x + leftDirX * sideSpeed - radius), (int)y))) {
            x += leftDirX * sideSpeed;
        }

        if (isCollidable(map.getWallMapValue((int)x, (int)(y + leftDirY * sideSpeed + radius))) &&
                isCollidable(map.getWallMapValue((int)x, (int)(y + leftDirY * sideSpeed - radius)))) {
            y += leftDirY * sideSpeed;
        }
    }

    public void moveRight(double speed, GameMap map) {
        double rightDirX = dirY;
        double rightDirY = -dirX;

        // Reduziere die seitliche Geschwindigkeit
        double sideSpeed = speed * 0.7; // Zum Beispiel 70% der normalen Geschwindigkeit

        if (isCollidable(map.getWallMapValue((int)(x + rightDirX * sideSpeed + radius), (int)y)) &&
                isCollidable(map.getWallMapValue((int)(x + rightDirX * sideSpeed - radius), (int)y))) {
            x += rightDirX * sideSpeed;
        }

        if (isCollidable(map.getWallMapValue((int)x, (int)(y + rightDirY * sideSpeed + radius))) &&
                isCollidable(map.getWallMapValue((int)x, (int)(y + rightDirY * sideSpeed - radius)))) {
            y += rightDirY * sideSpeed;
        }
    }

    public void turnLeft(double angle) {
        double oldDirX = dirX;
        dirX = dirX * Math.cos(angle) - dirY * Math.sin(angle);
        dirY = oldDirX * Math.sin(angle) + dirY * Math.cos(angle);

        double oldPlaneX = planeX;
        planeX = planeX * Math.cos(angle) - planeY * Math.sin(angle);
        planeY = oldPlaneX * Math.sin(angle) + planeY * Math.cos(angle);
    }

    public void turnRight(double angle) {
        double oldDirX = dirX;
        dirX = dirX * Math.cos(-angle) - dirY * Math.sin(-angle);
        dirY = oldDirX * Math.sin(-angle) + dirY * Math.cos(angle);

        double oldPlaneX = planeX;
        planeX = planeX * Math.cos(-angle) - planeY * Math.sin(-angle);
        planeY = oldPlaneX * Math.sin(-angle) + planeY * Math.cos(angle);
    }
}
