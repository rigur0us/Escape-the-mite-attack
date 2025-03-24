package PaooGame.Entity;

import PaooGame.BasicInput.KeyManager;
import PaooGame.Game;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Maps.Map;
import PaooGame.Tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Objects;

public class Character extends Entity{

    private final String DBpath = "jdbc:sqlite:/home/begu/IdeaProjects/1208A Cojocaru Valentin et3/ScheletProiectPAOO/res/database.sqlite";
    public Connection conn;

    Game gg ;
    KeyManager key;
    private BufferedImage image;


    LinkedList<Bullet> bullet;

    int hp;
    int currentHp;
    private long lastCollisionTime; //timpul cand s a facut ultima coliziune
    public String username;
    public int level;

    //GameWindow wnd;
    public Character(Game gg, KeyManager key)
    {
        //init db connection
        try {
            conn =  DriverManager.getConnection(DBpath);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //set default user
        username = "Pizdobol foo";
        level = 1;

        //sync with db
        syncWithDB();

        bullet = new LinkedList<Bullet>();
        this.gg = gg;
        this.key = key;
        isSolidCh = new Rectangle();
        isSolidCh.x = 14;
        isSolidCh.y = 14;
        isSolidCh.width = 20;
        isSolidCh.height = 20;
        hp = 100;
        currentHp = hp;
        //bullet = new Bullet(gg, x, y, speed);
//        screenX = 768/2 - Tile.TILE_HEIGHT/2;
//        screenY = 576/2;
        SetDefaultCoord();
        LoadPlayerImage();
        lastCollisionTime = System.currentTimeMillis();

        //wnd.GetWndFrame().addKeyListener(key);

    }

    public void syncWithDB(){
        //geyt user data
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE username='"+username+"'");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM user");
            if (rs.next()){
                String username = rs.getString("username");
                String level = rs.getString("level");
                System.out.println("User: " + username + " [ lvl " + level + " ]");
                this.level = Integer.parseInt(level);
                this.username = username;
            }else{
                System.out.println("!User not find");

                conn.createStatement().executeQuery("INSERT INTO user (username) VALUES('"+username+"')");

                System.out.println("!New user created");
            }
        }catch (Exception e){
//            System.err.println(e);
        }
    }

    public void SetDefaultCoord()
    {
//        mainX = Tile.TILE_HEIGHT*23;
//        mainY = Tile.TILE_HEIGHT*21;
        x = 50;
        y = 50;
        speed = 3;
        direction = "right";
    }


    public void LoadPlayerImage()
    {
        try{
            upDir = ImageIO.read(getClass().getResourceAsStream("/textures/MainCharacter48.png"));
            downDir = ImageIO.read(getClass().getResourceAsStream("/textures/Main48Down.png"));
            rightDir = ImageIO.read(getClass().getResourceAsStream("/textures/Main48Right.png"));
            leftDir = ImageIO.read(getClass().getResourceAsStream("/textures/MainLeft48.png"));
        }catch(IOException e )
        {
            e.printStackTrace();
        }
    }

    public void shoot(){
        bullet.add( new Bullet(gg, x, y, copyDirection, 4));
        //System.out.println("ASDASD");
    }
    public void levelUp(){
        level++;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery("UPDATE user SET level=" + level +" WHERE username='"+username+"';");
        } catch (SQLException e) {
//            throw new RuntimeException(e);
//            print(e);
        }
        System.out.println("LEVEL UP TO: " + level);
    }

    public void update()
    {

        if (key.spacePressed && canCollide()) {
            shoot();
           // System.out.println("AM IMPUSCAT");
        }

        if(key.downPressed == true)
        {
            //System.out.println("asdasdasdasdasd");
            direction = "down";
            //y += speed;

        }
        else if(key.upPressed == true)
        {
            direction = "up";
            //y -= speed;
        }
        else if(key.leftPressed == true)
        {
            direction = "left";
            //x -= speed;
        }
        else if(key.rightPressed == true)
        {
            direction = "right";
            //x += speed;
        }
        if(!Objects.equals(direction, ""))
        {
            copyDirection = direction;
        }
        collision = false;
        gg.toCheck.checkTile(this);
        if(collision == false)
        {
            switch(direction)
            {
                case "up":
                    y -= speed;
                    break;
                case "down":
                    y += speed;
                    break;
                case "left":
                    x -= speed;
                    break;
                case  "right":
                    x += speed;
                    break;

            }
        }
        for(int i=0;i<bullet.size();++i)
        {
            if(bullet.get(i).active)
                bullet.get(i).update();
            else
                bullet.remove(i);

        }
//        if(bullet!=null)
//            bullet.update();
        if (hp <= 0) {

            System.out.println("Eroul a murit!");
            gg.StopGame();
        }
    }
    //BufferedImage image5 = null;
    public void draw(Graphics g)
    {
//        g.fillRect(x, y, Tile.TILE_WIDTH, Tile.TILE_WIDTH);
        //if(direction)
        //BufferedImage image = null;
        if(direction.equals("up"))
        {
            image = upDir;
        }
        if(direction.equals("down"))
        {
            image = downDir;
        }
        if(direction.equals("left"))
        {
            image = leftDir;
        }
        if(direction.equals("right"))
        {
            image = rightDir;
        }

        direction = "";

        g.drawImage(image, x, y, Tile.TILE_WIDTH, Tile.TILE_WIDTH, null);
        drawHealthBar(g);


        g.setFont(new Font("Arial", Font.BOLD, 16));

        g.setColor(Color.WHITE);
        g.drawString(username + " [ lvl " + level + " ]", 10, 20);

        g.setColor(Color.RED);
        g.drawString("HP: " + hp, 10, 40);

//        if (bullet != null) {
//            bullet.draw(g);
//        }
//        g.setColor(Color.blue);
//        g.fillRect(x + isSolidCh.x, y+isSolidCh.y, isSolidCh.width, isSolidCh.height);
//        g.setColor(Color.RED);
//        g.drawRect(x + isSolidCh.x, y + isSolidCh.y, isSolidCh.width, isSolidCh.height);
    }
    public void reduceHP(int amount) {
        hp -= amount;
        if (hp < 0) {
            hp = 0;
        }
    }

//    public int getHP() {
//        return hp;
//    }
//
    public boolean isAlive()
    {
        return hp>0;
    }
//
//
//    public void setHP(int hp) {
//        this.hp = hp;
//    }
    public boolean canCollide() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCollisionTime >= 700) { // 1 secundă pauză între coliziuni
            lastCollisionTime = currentTime;
            return true;
        }
        return false;
    }
    public LinkedList<Bullet> getBullet() {
        return bullet;
    }

    public void drawHealthBar(Graphics g) {

        int barWidth = 50;
        int barHeight = 7;
        int barX = x;
        int barY = y - 20;

        // Calculează lungimea barei de viață în funcție de viața curentă
        int healthWidth = (int)((double)hp / currentHp * barWidth);


        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);


        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, healthWidth, barHeight);
    }

}
