package main;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable{
    // Screen Settings
    final int originalTileSize = 16; // 16*16 tile
    final int scale = 3;
    final int tileSize = originalTileSize * scale; // 48*48 tile 
    final int maxScreenCol = 26;
    final int maxScreenRow = 12; 
    final int Screen_Width = tileSize*maxScreenCol; // 768 pixel
    final int Screen_Heigth = tileSize*maxScreenRow; // 576 pixel
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    int fps = 90;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run(){

        double drawInterval = 1000000000/fps; // We are able to draw 60 times per second
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            /*
            1. Update information like the character position
            2. Draw the screen with updated information
            */

            // Update
            update();
            // Paint
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime);
                nextDrawTime += drawInterval;
                
            } catch (Exception e) {
                // TODO: handle exception
            }

            
        }
    }

    public void update() {
        if (keyH.upPressed == true) {
            playerY -= playerSpeed;
        }

        if (keyH.downPressed == true) {
            playerY += playerSpeed;
        }

        if (keyH.leftPressed == true) {
            playerX -= playerSpeed;
        }

        if (keyH.rightPressed == true) {
            playerX += playerSpeed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; // Gives us more control compared to Graphics
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(Screen_Width,Screen_Heigth));
        this.setBackground(Color.darkGray);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
}
