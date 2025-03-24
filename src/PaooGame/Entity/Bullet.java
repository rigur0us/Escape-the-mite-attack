package PaooGame.Entity;

import PaooGame.BasicInput.KeyManager;
import PaooGame.Collision;
import PaooGame.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class Bullet extends Entity{

    private int size;
    //KeyManager key;

    BufferedImage bulletImage;

    public boolean active; // starea glontului exista sau nu exista

    Game gg;
    Rectangle bounds;

    Collision toCkeck = new Collision(gg);
    public Bullet(Game gg, int startX, int startY, String direction, int speed) {

        this.gg = gg;
        this.x = startX +2;
        this.y = startY +2;
        this.speed = speed;
        isSolidCh = new Rectangle(24, 20, 8, 8);
        this.size = 10;
        this.direction = direction;
        this.bounds = new Rectangle(x, y, 8, 8);


        //this.key = key;
        LoadBulletImage();
        active = true;

    }
    public void LoadBulletImage()
    {
        try{
            bulletImage = ImageIO.read(getClass().getResourceAsStream("/textures/Bullet48.png"));
        }catch(IOException e )
        {
            e.printStackTrace();
        }
    }

    public void update() {

        // System.out.println(direction);
        switch (direction) {
            case "up":
                y -= speed;
                //System.out.println(direction);
                break;
            case "down":
                y += speed;
                //System.out.println(direction);
                break;
            case "left":
                x -= speed;
                //System.out.println("LEFTt");
                break;
            case "right":
                x += speed;
                //System.out.println("RIGHT");
                break;
        }
        collision = false;
        gg.toCheck.checkTile(this);
//        int futureX = x;
//        int futureY = y;
        if (!collision) {
//            x = futureX;
//            y = futureY;
        } else {

            active = false;
            }

//        if (x < 0 || x > 768 || y < 0 || y > 576) {
//            active = false;
//        }
        LinkedList<Enemy> current = gg.getEnemy();
        for(int i = 0;i < current.size();++i)
        {
            if (toCkeck.checkEnemy(current.get(i), this))
            {
                active = false;
                current.get(i).active = false;
                //gg.getEnemy().hp -= 10;

                //System.out.println("jsodkifhsodiufh " + x + "  " + y);
            }
        }
        //bounds.setBounds(x, y, 8, 8);


        //System.out.println(x + " " + y);
    }

    public void draw(Graphics g) {
        if(!active) {return;}

         g.drawImage(bulletImage, x, y, null);
//        g.setColor(Color.RED);
//        g.drawRect(x + isSolidCh.x, y + isSolidCh.y, isSolidCh.width, isSolidCh.height);

        //
//        g.setColor(Color.BLACK);
//        g.fillOval(x, y, size, size);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Rectangle getBounds() {
        return bounds;
    }
}
