package PaooGame.Maps;

import PaooGame.Entity.Enemy;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {
    public static int[][] tiles;

    //public Map map = new Map();
    public static final int tileSize = 3;
    private static final int WIDTH = 16 * tileSize; // Lățimea hărții
    private static final int HEIGHT = 12 * tileSize; // Înălțimea hărții
    private static final int TILE_SIZE = 32; // Mărimea unui tile în pixeli

    public static Tile GetTile(int x, int y)
    {
        Tile tt = Tile.tiles[tiles[x][y]];
        return tt;
    }
    public Map() {
        tiles = new int[16*Tile.TILE_WIDTH][12*Tile.TILE_HEIGHT];
        /*for(int i=0;i<12;++i)
        {
            for(int j=0;j<16;++j)
            {
                Random rand = new Random();
                tiles[i][j] = rand.nextInt(4);
                Tile tt =w tileSet[tiles[i][j]];
                tt.draw(g, i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }*/
        //loadTiles();
//        loadMapFromFile("map2.txt");

//        loadLevel(1);
    }

    public void loadLevel(int level){

        if (level == 1) {
            loadMapFromFile("map1.txt");
        }else if (level == 2) {
            loadMapFromFile("map2.txt");
        }else if (level == 3) {
            loadMapFromFile("map3.txt");
        }else {
            loadMapFromFile("map4.txt");
        }
    }


    private void loadMapFromFile(String fileName) {
        try {
            File file = new File("res/" + fileName);
            Scanner scanner = new Scanner(file);
            int row = 0;
            while (scanner.hasNextLine() && row < HEIGHT) {
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");
                for (int col = 0; col < WIDTH && col < tokens.length; col++) {
                    tiles[col][row] = Integer.parseInt(tokens[col]);
                }
                row++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
//    private void loadTiles() {
//        // Aici poți crea și inițializa obiectele Tile corespunzătoare
//        // În funcție de valorile din matricea tiles
//        Tile.tiles[0] = new GrassTile(0);
//        Tile.tiles[1] = new GrassTile(1);
//        Tile.tiles[2] = new GrassTile(2);
//        Tile.tiles[3] = new GrassTile(3);
//
//        // De exemplu:
////        tileSet[0] = new GrassTile(0); // Pamant
////        tileSet[1] = new MountainTile(1);  // Apa
////        tileSet[2] = new WaterTile(2); // Iarba
////        tileSet[3] = new TreeTile(3); // Iarba
////        tileSet[4] = new SoilTile(4); // Iarba
//    }
    public void drawMapp(Graphics g){
        for(int i=0;i<WIDTH;++i)
        {
            for(int j=0;j<HEIGHT;++j)
            {
                int type = tiles[i][j];
                Tile x = Tile.tiles[type];

                x.Draw(g, i*Tile.TILE_HEIGHT, j*Tile.TILE_HEIGHT);
//                if(type==1) {
//                    g.setColor(Color.black);
//                    g.fillRect(i*Tile.TILE_HEIGHT, j*Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//                }

                //g.fillRect(playerX, playerY, Tile.TILE_WIDTH, Tile.TILE_WIDTH);
            }
        }
    }

}
