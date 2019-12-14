import javax.swing.*;
import java.awt.*;

public class Notepad {

    private final String APP_NAME = "Circle";
    private final int HEIGHT = 500;
    private final int WIDTH = 750;

    private JFrame mainFrame;
    private Graphics graphicsPanel;

    public Notepad() {

        mainFrame = new JFrame(APP_NAME);


        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        graphicsPanel = mainFrame.getContentPane().getGraphics();

        // add any methods you want before initializing
        printCircle();
    }

    private void printCircle() {

        int radius = 100;

        graphicsPanel.fillOval(WIDTH / 2 - radius / 2, HEIGHT / 2 - radius / 2, radius, radius);
    }
}
