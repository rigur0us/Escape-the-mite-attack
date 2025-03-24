package PaooGame.BasicInput;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener{
    public boolean upPressed, downPressed, rightPressed, leftPressed, spacePressed;
//    public KeyManager() {
//        // Inițializăm toate variabilele de stare a tastelor cu false
//        upPressed = false;
//        downPressed = false;
//        rightPressed = false;
//        leftPressed = false;
//    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        //System.out.println("a");
        int toRead = e.getKeyCode();

        if (toRead==KeyEvent.VK_UP)
        {
            upPressed = true;
        }
        if (toRead==KeyEvent.VK_DOWN)
        {
            downPressed = true;
        }
        if (toRead==KeyEvent.VK_LEFT)
        {
            leftPressed = true;
        }
        if (toRead==KeyEvent.VK_RIGHT)
        {
            rightPressed = true;
        }
        if(toRead == KeyEvent.VK_SPACE)
        {
            spacePressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.print("asdasdasd");
        int toRead = e.getKeyCode();

        if (toRead==KeyEvent.VK_UP)
        {
            upPressed = false;
        }
        if (toRead==KeyEvent.VK_DOWN)
        {
            downPressed = false;
        }
        if (toRead==KeyEvent.VK_LEFT)
        {
            leftPressed = false;
        }
        if (toRead==KeyEvent.VK_RIGHT)
        {
            rightPressed = false;
        }
        if(toRead == KeyEvent.VK_SPACE)
        {
            spacePressed = false;
        }
    }
}
