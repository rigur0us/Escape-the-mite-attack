package PaooGame;

import PaooGame.Entity.Bullet;
import PaooGame.Entity.Character;

import PaooGame.BasicInput.KeyManager;
import PaooGame.Entity.Enemy;
import PaooGame.GameWindow.GameState;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Maps.Map;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
//import java.util.Map;
import PaooGame.Maps.Map.*;

import javax.swing.*;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)

                ------------
                |           |
                |     ------------
    60 times/s  |     |  Update  |  -->{ actualizeaza variabile, stari, pozitii ale elementelor grafice etc.
        =       |     ------------
     16.7 ms    |           |
                |     ------------
                |     |   Draw   |  -->{ deseneaza totul pe ecran
                |     ------------
                |           |
                -------------
    Implementeaza interfata Runnable:

        public interface Runnable {
            public void run();
        }

    Interfata este utilizata pentru a crea un nou fir de executie avand ca argument clasa Game.
    Clasa Game trebuie sa aiba definita metoda "public void run()", metoda ce va fi apelata
    in noul thread(fir de executie). Mai multe explicatii veti primi la curs.

    In mod obisnuit aceasta clasa trebuie sa contina urmatoarele:
        - public Game();            //constructor
        - private void init();      //metoda privata de initializare
        - private void update();    //metoda privata de actualizare a elementelor jocului
        - private void draw();      //metoda privata de desenare a tablei de joc
        - public run();             //metoda publica ce va fi apelata de noul fir de executie
        - public synchronized void start(); //metoda publica de pornire a jocului
        - public synchronized void stop()   //metoda publica de oprire a jocului
 */
public class Game implements Runnable {

    private static Game gameInstance = null;

    public GameWindow wnd;

    /*!< Fereastra in care se va desena tabla jocului*/
    public final Map map;
    public boolean runState;   /*!< Flag ce starea firului de executie.*/
    private Thread gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    /// Sunt cateva tipuri de "complex buffer strategies", scopul fiind acela de a elimina fenomenul de
    /// flickering (palpaire) a ferestrei.
    /// Modul in care va fi implementata aceasta strategie in cadrul proiectului curent va fi triplu buffer-at

    ///                         |------------------------------------------------>|
    ///                         |                                                 |
    ///                 ****************          *****************        ***************
    ///                 *              *   Show   *               *        *             *
    /// [ Ecran ] <---- * Front Buffer *  <------ * Middle Buffer * <----- * Back Buffer * <---- Draw()
    ///                 *              *          *               *        *             *
    ///                 ****************          *****************        ***************

    private Graphics g;          /*!< Referinta catre un context grafic.*/

    KeyManager key = new KeyManager();

    public Collision toCheck = new Collision(this);
    //public Collision toCheck2 = new Collision(this);

    //WORLD
//    public final int maxCol = 50;
//    public final int maxRow = 50;
//
//    public final int WorldHeight = Tile.TILE_HEIGHT*maxCol;
//    public final int WorldWidth = Tile.TILE_WIDTH*maxRow


//    int playerX = 100;
//    int playerY = 170;
//
//    int playerSpeed = 2;

    Character player = new Character(this, key);
    LinkedList<Enemy> en = new LinkedList<Enemy>();


    Timer gameTimer;

    boolean levelCompleteCheck = false;

    //game state
    public GameState gameState;

    //Bullet bullet = new Bullet(this, player.x, player.y, player.direction, 2);


    //private Tile tile; /*!< variabila membra temporara. Este folosita in aceasta etapa doar pentru a desena ceva pe ecran.*/

    /*! \fn public Game(String title, int width, int height)
        \brief Constructor de initializare al clasei Game.

        Acest constructor primeste ca parametri titlul ferestrei, latimea si inaltimea
        acesteia avand in vedere ca fereastra va fi construita/creata in cadrul clasei Game.

        \param title Titlul ferestrei.
        \param width Latimea ferestrei in pixeli.
        \param height Inaltimea ferestrei in pixeli.
     */
    private Game() {
        /// Obiectul GameWindow este creat insa fereastra nu este construita
        /// Acest lucru va fi realizat in metoda init() prin apelul
        /// functiei BuildGameWindow();
        map = new Map();


//        wnd = new GameWindow(title, width, height);
        /// Resetarea flagului runState ce indica starea firului de executie (started/stoped)
        runState = false;

        gameTimer = new Timer(this, 120);
    }
    public static synchronized Game getInstance()
    {
        if (gameInstance == null)
            gameInstance = new Game();

        return gameInstance;
    }

    public LinkedList<Enemy> getEnemy() {
        return en;
    }

//    Enemy getEnemy()
//    {
//        return en;
//    }
    /*! \fn private void init()
        \brief  Metoda construieste fereastra jocului, initializeaza aseturile, listenerul de tastatura etc.

        Fereastra jocului va fi construita prin apelul functiei BuildGameWindow();
        Sunt construite elementele grafice (assets): dale, player, elemente active si pasive.

     */
public void InitGame() {
    wnd = new GameWindow("Escape the mite attack", 768, 576);
    /// Este construita fereastra grafica.
    wnd.BuildGameWindow();
    wnd.GetWndFrame().addKeyListener(key);

    /// Se incarca toate elementele grafice (dale)
    Assets.Init();

    //load level
    InitGameLevel();
}

public void InitGameLevel(){

    System.out.println("Load level");
    //load map by level
    map.loadLevel(player.level);

    //load enemyes by player level
    if (player.level == 1){
        //list of enemys for level 1
        en.add(new Enemy(this, 50, 100));
    }else if (player.level == 2){
        //list of enemys for level 2
        en.add(new Enemy(this, 50, 100));
//        en.add(new Enemy(this, 100, 100));
    }else if (player.level == 3){
        //list of enemys for level 3
        en.add(new Enemy(this, 50, 100));
//        en.add(new Enemy(this, 100, 100));
//        en.add(new Enemy(this, 200, 100));
    }

    //reset player position
    player.SetDefaultCoord();

}


    /*! \fn public void run()
        \brief Functia ce va rula in thread-ul creat.

        Aceasta functie va actualiza starea jocului si va redesena tabla de joc (va actualiza fereastra grafica)
     */
    public void run() {
        /// Initializeaza obiectul game
        InitGame();
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/

        /// Apelul functiilor Update() & Draw() trebuie realizat la fiecare 16.7 ms
        /// sau mai bine spus de 60 ori pe secunda.

        final int framesPerSecond = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final double timeFrame = 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

        /// Atat timp timp cat threadul este pornit Update() & Draw()
        while (runState == true) {
            /// Se obtine timpul curent
            curentTime = System.nanoTime();
            /// Daca diferenta de timp dintre curentTime si oldTime mai mare decat 16.6 ms
            if ((curentTime - oldTime) > timeFrame) {
                /// Actualizeaza pozitiile elementelor
                Update();
                /// Deseneaza elementele grafica in fereastra.
                Draw();
                oldTime = curentTime;
                if (!player.isAlive()) {
                    ShowGameOverWindow();

                    StopGame();

                }
            }
        }

    }
    public Character getPlayer() {
        return player;
    }
//    private void ShowGameOverWindow() {
//        JOptionPane.showMessageDialog(null, "Game Over", "Game Over", JOptionPane.INFORMATION_MESSAGE);
//    }
    private void ShowGameOverWindow() {
        JFrame gameOverFrame = new JFrame("Game Over");
        JLabel gameOverLabel = new JLabel("Game Over");

//        gameOverFrame.setFont(new Font("Arial", Font.BOLD, 50));
//        gameOverLabel.setForeground(Color.RED);
//        gameOverFrame.setBackground(Color.LIGHT_GRAY);

        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gameOverLabel.setForeground(Color.BLUE);
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameOverFrame.getContentPane().add(gameOverLabel, BorderLayout.CENTER);
        gameOverFrame.setSize(300, 300);
        gameOverFrame.setLocationRelativeTo(null); // Centrarea ferestrei pe ecran
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOverFrame.setVisible(true);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Închide fereastra "Game Over" când butonul "OK" este apăsat
                gameOverFrame.dispose();
            }
        });


        gameOverFrame.getContentPane().add(okButton, BorderLayout.SOUTH);

        gameOverFrame.setSize(300, 200);
        gameOverFrame.setLocationRelativeTo(null); // Centrarea ferestrei pe ecran
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOverFrame.setVisible(true);
    }

    /*! \fn public synchronized void start()
        \brief Creaza si starteaza firul separat de executie (thread).

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StartGame() {
        if (runState == false) {
            /// Se actualizeaza flagul de stare a threadului
            runState = true;
            /// Se construieste threadul avand ca parametru obiectul Game. De retinut faptul ca Game class
            /// implementeaza interfata Runnable. Threadul creat va executa functia run() suprascrisa in clasa Game.
            gameThread = new Thread(this);
            /// Threadul creat este lansat in executie (va executa metoda run())
            gameThread.start();
        } else {
            /// Thread-ul este creat si pornit deja
            return;
        }
    }

    /*! \fn public synchronized void stop()
        \brief Opreste executie thread-ului.

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StopGame() {
        if (runState == true) {
            /// Actualizare stare thread
            runState = false;
            /// Metoda join() arunca exceptii motiv pentru care trebuie incadrata intr-un block try - catch.
            try {
                /// Metoda join() pune un thread in asteptare panca cand un altul isi termina executie.
                /// Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                gameThread.join();
            } catch (InterruptedException ex) {
                /// In situatia in care apare o exceptie pe ecran vor fi afisate informatii utile pentru depanare.
                ex.printStackTrace();
            }
        } else {
            /// Thread-ul este oprit deja.
            return;
        }
    }

    /*! \fn private void Update()
        \brief Actualizeaza starea elementelor din joc.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Update() {
        //System.out.println("GIUGYUFUTF");
        player.update();

        for(int i=0;i<en.size();++i)
        {
            if(en.get(i).active)
                en.get(i).update();
            else
                en.remove(i);

        }
        if(en.isEmpty() && !levelCompleteCheck && player.level<4)
        {
            levelCompleteCheck = true;
            //felicitary victory window
            Draw();

            if (player.level < 3){
                this.gameTimer.showVictoryWindow();
            }else{
                this.gameTimer.showFinalVictoryWindow();
            }

//            StopGame();
        }
        //
//        if (player.getBullet() != null) {
//            Bullet bullet = player.getBullet();
//
//            // Verifică coliziunea cu inamicul
//            Rectangle bulletBounds = bullet.getBounds();
//            Rectangle enemyBounds = new Rectangle(en.x + en.isSolidCh.x, en.y + en.isSolidCh.y, en.isSolidCh.width, en.isSolidCh.height);
//            if (bulletBounds.intersects(enemyBounds)) {
//                // Coliziunea cu inamicul
//                en.setAlive(false); // Marchează inamicul ca fiind mort
//                bullet.setActive(false); // Marchează gloanța ca fiind inactivă
//                // Poți adăuga aici logica specifică pentru ce se întâmplă la coliziunea cu inamicul
//            }
//
//            //
//            if (bullet.getX() < 0 || bullet.getX() > GetWidth() || bullet.getY() < 0 || bullet.getY() > GetHeight()) {
//                bullet.setActive(false); // Marchează gloanța ca fiind inactivă
//            }
//
//            //
//            bullet.update();
//        }

        //
        gameTimer.update();
    }



    /*! \fn private void Draw()
        \brief Deseneaza elementele grafice in fereastra coresponzator starilor actualizate ale elementelor.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    public int GetWidth()
    {
        return wnd.GetWndWidth();
    }
    public int GetHeight()
    {
        return wnd.GetWndHeight();
    }
    public KeyManager GetKeyManager()
    {
        return key;
    }
    private void Draw() {
        /// Returnez bufferStrategy pentru canvasul existent
        bs = wnd.GetCanvas().getBufferStrategy();

        /// Verific daca buffer strategy a fost construit sau nu
        if (bs == null) {
            /// Se executa doar la primul apel al metodei Draw()
            try {
                /// Se construieste tripul buffer
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            } catch (Exception e) {
                /// Afisez informatii despre problema aparuta pentru depanare.
                e.printStackTrace();
            }
        }
        /// Se obtine contextul grafic curent in care se poate desena.
        g = bs.getDrawGraphics();


        /// Se sterge ce era
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
        //Map mm = new Map();
        map.drawMapp(g);
        g.setColor(Color.green);
        player.draw(g);

        for(int i=0;i<en.size();++i)
        {
            en.get(i).draw(g);
        }
        gameTimer.draw(g);
        for(Bullet b : player.getBullet())
        {
            b.draw(g);
        }

//        if (player.getBullet() != null)
//        {
//            player.getBullet().draw(g);
//        }



        //player.getBullet().draw(g);

        //g.fillRect(g);



            /// operatie de desenare
            // ...............
        /*for(int i=0;i<800/16;++i)
        {
            for(int j=0;j<600/16;++j)
            {
                    Tile.grassTile.Draw(g, j*Tile.TILE_WIDTH, i * Tile.TILE_HEIGHT);
            }
        }*/


        //Tile.grassTile.Draw(g, 0 * Tile.TILE_WIDTH, 0);
//            Tile.soilTile.Draw(g, 1 * Tile.TILE_WIDTH, 0);
//            Tile.waterTile.Draw(g, 2 * Tile.TILE_WIDTH, 0);
//            Tile.mountainTile.Draw(g, 3 * Tile.TILE_WIDTH, 0);
//            Tile.treeTile.Draw(g, 4 * Tile.TILE_WIDTH, 0);
//            Tile.grassTile.Draw(g, 5 * Tile.TILE_WIDTH, 0);
//            Tile.soilTile.Draw(g, 6 * Tile.TILE_WIDTH, 0);
//            Tile.waterTile.Draw(g, 7 * Tile.TILE_WIDTH, 0);
//            Tile.mountainTile.Draw(g, 8 * Tile.TILE_WIDTH, 0);
//            Tile.treeTile.Draw(g, 9 * Tile.TILE_WIDTH, 0);

            //g.drawRect(1 * Tile.TILE_WIDTH, 1 * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
        //g.fillRect(playerX, playerY, Tile.TILE_WIDTH, Tile.TILE_WIDTH);

            // end operatie de desenare
            /// Se afiseaza pe ecran
        bs.show();


            /// Elibereaza resursele de memorie aferente contextului grafic curent (zonele de memorie ocupate de
            /// elementele grafice ce au fost desenate pe canvas).
        g.dispose();
    }

}

