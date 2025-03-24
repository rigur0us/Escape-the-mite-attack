package PaooGame;

import PaooGame.Entity.Bullet;
import PaooGame.Entity.Enemy;
import PaooGame.Maps.Map;

import PaooGame.Entity.Entity;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Game;
import PaooGame.Tiles.Tile;

import java.awt.*;

import static PaooGame.Tiles.Tile.*;
//import static org.graalvm.compiler.nodes.calc.BinaryArithmeticNode.ReassociateMatch.x;
//import static org.graalvm.compiler.nodes.calc.BinaryArithmeticNode.ReassociateMatch.y;

public class Collision {

    Game gg;
    public Collision(Game gg)
    {
        this.gg = gg;
    }


    public void checkTile(Entity ent)
    {
        int entityLeftX = ent.x + ent.isSolidCh.x;
        int entityRightX = ent.x + ent.isSolidCh.x+ent.isSolidCh.width;
        int entityTopY = ent.y + ent.isSolidCh.y;
        int entityBottomY = ent.y + ent.isSolidCh.y + ent.isSolidCh.height;

        int entityLeftCol = entityLeftX / TILE_WIDTH;
        int entityRightCol = entityRightX / TILE_HEIGHT ;
        int entityTopRow = entityTopY / TILE_WIDTH;
        int entityBottomRow = entityBottomY / TILE_HEIGHT;

        Tile tile1, tile2;

        if(ent.direction.equals("up")) {
            entityTopRow = (entityTopY - ent.speed)/TILE_WIDTH;
            tile1 = Map.GetTile(entityLeftCol, entityTopRow);
            tile2 = Map.GetTile(entityRightCol, entityTopRow);
            if(tile1.IsSolid() || tile2.IsSolid())
            {
                //System.out.println("UP");
                ent.collision = true;
            }
        }
         if(ent.direction.equals("down")) {
            entityBottomRow = (entityBottomY + ent.speed)/TILE_WIDTH;
            tile1 = Map.GetTile(entityLeftCol, entityBottomRow);
            tile2 = Map.GetTile(entityRightCol, entityBottomRow);
            if(tile1.IsSolid() || tile2.IsSolid())
            {
                //System.out.println("DOWN");

                ent.collision = true;
            }
        }
        if(ent.direction.equals("left")) {
            entityLeftCol = (entityLeftX - ent.speed)/TILE_WIDTH;
            tile1 = Map.GetTile(entityLeftCol, entityTopRow);
            tile2 = Map.GetTile(entityLeftCol, entityBottomRow);
            if(tile1.IsSolid() || tile2.IsSolid())
            {
                //System.out.println("LEFT");
                ent.collision = true;
            }
        }
        if(ent.direction.equals("right")) {
            entityRightCol = (entityRightX + ent.speed)/TILE_WIDTH;
            tile1 = Map.GetTile(entityRightCol, entityTopRow);
            tile2 = Map.GetTile(entityRightCol, entityBottomRow);
            if(tile1.IsSolid() || tile2.IsSolid())
            {
                //System.out.println("RIGHT");
                ent.collision = true;
            }
        }

    }
//    public void checkTileCollision(Tile tile) {
//        if (tile.IsSolid()) {
//            Rectangle bulletBounds = new Rectangle(x, y, 8, 8);
//            Rectangle tileBounds = new Rectangle(tile.getX(), tile.getY(), Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//
//            if (bulletBounds.intersects(tileBounds)) {
//                active = false;
//
//            }f
//        }
//    }
    public boolean checkEnemy(Enemy en, Bullet bull)
    {
//        if (!bull.active) {
//            return false;
//        }
        Rectangle rt = new Rectangle(bull.getBounds());
        rt.x = bull.getX();
        rt.y = bull.getY();
        if (bull != null) {
            Rectangle r = new Rectangle(en.getBounds());
            r.x = en.getX();
            r.y = en.getY();
            if (rt.intersects(r)) {
                return true;
            }
        }
        return false;
    }

}
