// imports

import javax.swing.*;
import java.awt.*;

/**
 * Operates GUI for FotoMotion application
 *
 * @author : Jay Acosta
 */
public class Notepad {

    private final String APP_NAME = "FotoMotion";
    public static final int HEIGHT = 500;
    public static final int WIDTH = 750;

    private JFrame mainFrame;

    public Notepad() {

        mainFrame = new JFrame(APP_NAME);

        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = mainFrame.getContentPane();

        PagePanel panel = new PagePanel();
        contentPane.add(panel);

        mainFrame.setVisible(true);
    }
}
