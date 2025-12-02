package main; 
import javax.swing.JFrame; 
public class Main { 
    public static void main(String[] args) { 
        JFrame window = new JFrame(); 
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        window.setResizable(false); window.setTitle("Gebeta Project"); 
        GamePanel gamePanel = new GamePanel(); window.add(gamePanel); 
        window.pack(); // Window is to be fitted to the size of the preferred size and layouts of Game-Panel 
        window.setLocationRelativeTo(null); 
        window.setVisible(true); 
        gamePanel.startGameThread(); 
    } 
}