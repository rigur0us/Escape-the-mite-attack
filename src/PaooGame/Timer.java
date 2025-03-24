package PaooGame;

import PaooGame.Entity.Enemy;

import javax.swing.*;
import java.awt.*;

public class Timer {
    private int totalTime;
    private long startTime;
    Game gg;
    public Timer(Game gg, int totalTime) {
        this.totalTime = totalTime;
        this.startTime = System.nanoTime();
        this.gg = gg;
    }

    public int getRemainingTime() {
        long currentTime = System.nanoTime();
        int elapsedTime = (int) ((currentTime - startTime) / 1_000_000_000);
        return totalTime - elapsedTime;
    }

    public void update() {
        if (getRemainingTime() <= 0) {
//            showVictoryWindow();
//            gg.StopGame();
        }
    }

    public void draw(Graphics g) {
        int remainingTime = getRemainingTime();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Time: " + remainingTime, 10, 60);
    }

    public void showVictoryWindow() {

        JFrame victoryFrame = new JFrame("Victory");
        JLabel victoryLabel = new JLabel("Felicitari! Ai castigat!");
        victoryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        victoryLabel.setForeground(Color.GREEN);
        victoryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        victoryFrame.getContentPane().add(victoryLabel, BorderLayout.CENTER);
        victoryFrame.setSize(300, 200);
        victoryFrame.setLocationRelativeTo(null);
        victoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        victoryFrame.setVisible(true);

        JButton okButton = new JButton("Next level");
        okButton.addActionListener(e -> {
            victoryFrame.dispose();
            gg.wnd.closeWindow();

            gg.levelCompleteCheck = false;
            gg.StartGame();
        });

        victoryFrame.getContentPane().add(okButton, BorderLayout.SOUTH);
        victoryFrame.setVisible(true);

        gg.runState=false;
        gg.player.levelUp();
    }
    public void showFinalVictoryWindow() {

        JFrame victoryFrame = new JFrame("Victory");
        JLabel victoryLabel = new JLabel("Felicitari! Ai trecut toate nivelele!");
        victoryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        victoryLabel.setForeground(Color.GREEN);
        victoryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        victoryFrame.getContentPane().add(victoryLabel, BorderLayout.CENTER);
        victoryFrame.setSize(500, 200);
        victoryFrame.setLocationRelativeTo(null);
        victoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        victoryFrame.setVisible(true);

        JButton okButton = new JButton("Final");
        okButton.addActionListener(e -> {
            victoryFrame.dispose();
            gg.wnd.closeWindow();

            gg.levelCompleteCheck = false;
            gg.StartGame();
        });

        victoryFrame.getContentPane().add(okButton, BorderLayout.SOUTH);
        victoryFrame.setVisible(true);

        gg.runState=false;
        gg.player.levelUp();
    }
}
