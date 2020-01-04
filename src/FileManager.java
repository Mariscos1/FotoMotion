import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.rmi.server.ExportException;

/**
 * Handles back-end development
 *
 * Possibly include a compression algorithm or
 * a custom file extension that saves the image objects
 *
 * Recommended that a designed implementation of the application
 * is created before starting methods like open() and export()
 *
 * @author : Jay Acosta, Ramon Marquez
 */
public class FileManager {

    private static JFileChooser directory = new JFileChooser();
    private static String THE_THING = "/";

    //Sets up the information for the File Chooser, such as the directories ur allowed to use, like how Karen used me
    static {
        directory.setCurrentDirectory(null);
        directory.setDialogTitle("I'm sorry karen, i'm good at cornhole, give me the kidz");
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    public static Image getImage(String pathName) {
        return null;
    }

    public static void save(Image currentPanel, Component save) {
        String pathway = "";
        if(directory.showOpenDialog(save) == JFileChooser.APPROVE_OPTION){
            pathway = directory.getSelectedFile().getAbsolutePath() + THE_THING;
        }

        File f1 = new File(pathway + "gay.jpeg");

        try {
            BufferedImage temp;
            if(currentPanel instanceof BufferedImage) {
                temp = (BufferedImage) currentPanel;
            } else if (currentPanel instanceof VolatileImage) {
                temp = ((VolatileImage) currentPanel).getSnapshot();
            } else  {
                throw new IllegalArgumentException("gay please");
            }
            ImageIO.write(temp, "jpeg", f1);
            Notepad.showErrorMessage("Karen Still doesn't love you, and she never did. \n She just used you for your money Adam.");

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void open() {

    }

    public static void export() {

    }

    public static void saveAs() {

    }
}
