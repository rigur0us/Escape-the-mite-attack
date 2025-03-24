package PaooGame.Entity;

import PaooGame.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Entity {



    public int x, y;
    protected Game game;
//    public int mainX, mainY;
    public int speed;

    public BufferedImage upDir, downDir, rightDir, leftDir;
    public String direction;
    public String copyDirection;
    int hp;
//    public Entity(Game game, int x, int y, String direction) {
//        this.game = game;
//        this.x = x;
//        this.y = y;
//        this.direction = direction;
//        this.isSolidCh = new Rectangle(x, y, 32, 32); // dimensiunea și poziția entității
//        this.speed = 1;
//        this.collision = false;
//        this.hp = 100;
//    }
    public String getRandomDirection()
    {


        Random random = new Random();
        // Generăm un număr întreg aleatoriu între 1 și 4
        int randomNumber = random.nextInt(4) + 1;
        if(randomNumber == 1)
            return "up";
        if(randomNumber == 2)
            return "down";
        if(randomNumber == 3)
            return "right";
        if(randomNumber == 4)
            return "left";
        return "up";
    }

    public Rectangle getBounds() {
        return new Rectangle(x + isSolidCh.x, y + isSolidCh.y, isSolidCh.width, isSolidCh.height);
    }
    public void takeDamage() {
        hp -= 10;
    }
    public int getHealth() {
        return hp;
    }
    public void setAlive(boolean alive) {
        if (!alive) {
            hp = 0;
        }
    }
    public Rectangle isSolidCh;
    public boolean collision = false;

}
