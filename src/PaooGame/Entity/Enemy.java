package PaooGame.Entity;

import PaooGame.Game;
import PaooGame.Tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Enemy extends Entity {
    BufferedImage enemyImage;
    Game gg;
    public boolean active;

    public Enemy(Game gg, int x, int y) {
        this.gg = gg;
        active = true;
        this.x = x;
        this.y = y;
        isSolidCh = new Rectangle(14, 14, 20, 20);
        SetDefaultCoord();
        LoadPlayerImage();
    }

    public void SetDefaultCoord() {

        speed = 2;
        direction = "down";
    }

    public void LoadPlayerImage() {
        try {
            upDir = ImageIO.read(getClass().getResourceAsStream("/textures/enemy48UP.png"));
            downDir = ImageIO.read(getClass().getResourceAsStream("/textures/enemy48down.png"));
            rightDir = ImageIO.read(getClass().getResourceAsStream("/textures/enemy48right.png"));
            leftDir = ImageIO.read(getClass().getResourceAsStream("/textures/enemy48left.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        int futureX = x;
        int futureY = y;

        switch (direction) {
            case "up":
                futureY -= speed;
                break;
            case "down":
                futureY += speed;
                break;
            case "left":
                futureX -= speed;
                break;
            case "right":
                futureX += speed;
                break;
        }


        collision = false;
        gg.toCheck.checkTile(this);

        if (!collision) {
            x = futureX;
            y = futureY;
        } else {
            // Logica de schimbare a direcției la coliziune
            switch (direction) {
                case "up":
                    direction = getRandomDirection();
                    break;
                case "down":
                    direction = getRandomDirection();
                    break;
                case "left":
                    direction = getRandomDirection();
                    break;
                case "right":
                    direction = getRandomDirection();
                    break;
            }
        }
        checkCollisionWithPlayer();
    }
    private void checkCollisionWithPlayer() {
        Character player = gg.getPlayer();

        // Obține dreptunghiurile solide pentru inamic și erou
        Rectangle enemyRect = new Rectangle(x + isSolidCh.x, y + isSolidCh.y, isSolidCh.width, isSolidCh.height);
        Rectangle playerRect = new Rectangle(player.x + player.isSolidCh.x, player.y + player.isSolidCh.y, player.isSolidCh.width, player.isSolidCh.height);

        if (enemyRect.intersects(playerRect)) {
            if(player.canCollide())
                player.reduceHP(20);
            // Tratarea coliziunii cu eroul principal
            //System.out.println("L AM LOVIIIITTT");
        }
    }

    public void draw(Graphics g) {
        switch (direction) {
            case "up":
                enemyImage = upDir;
                break;
            case "down":
                enemyImage = downDir;
                break;
            case "left":
                enemyImage = leftDir;
                break;
            case "right":
                enemyImage = rightDir;
                break;
            default:
                enemyImage = rightDir; // Set a default image to avoid null
        }

        g.drawImage(enemyImage, x, y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);
        // g.setColor(Color.blue);
        // g.fillRect(x + isSolidCh.x, y + isSolidCh.y, isSolidCh.width, isSolidCh.height);
//        g.setColor(Color.RED); // Culoare pentru debugging
//        g.drawRect(x + isSolidCh.x, y + isSolidCh.y, isSolidCh.width, isSolidCh.height);
    }
    public int getX()
    {
        return x;

    }
    public int getY()
    {
        return y;
    }
}
