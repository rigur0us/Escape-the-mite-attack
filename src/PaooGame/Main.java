package PaooGame;

import PaooGame.GameWindow.GameWindow;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        Game paooGame = Game.getInstance();
        paooGame.StartGame();
    }
}
