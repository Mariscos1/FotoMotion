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

        try {

            //Will function as the full pathway that the snippet will be saved to
            String pathway = ".png";

            //Runs if the actual save button is pressed, then it does all the good stuff below
            if(directory.showSaveDialog(save) == JFileChooser.APPROVE_OPTION){
                pathway = directory.getSelectedFile().getAbsolutePath() + pathway;
                System.out.println(pathway);

                File f1 = new File(pathway);

                //Snippet converts whatever image that was sent into the method into a BufferedImage
                BufferedImage temp;
                if(currentPanel instanceof BufferedImage) {
                    temp = (BufferedImage) currentPanel;
                } else if (currentPanel instanceof VolatileImage) {
                    temp = ((VolatileImage) currentPanel).getSnapshot();
                } else  {
                    throw new IllegalArgumentException("gay please");
                }

                //Makes the actual Image that gets saved into the directory of choosing. Turned into png
                ImageIO.write(temp, "png", f1);
                saveComplete("Karen Still doesn't love you, and she never did." +
                        "\n She just used you for your money Adam.\n" +
                        "Also it saved, but won't show up till you close the file bt dubs.");

            } else {
                //Shown whenever the person decides not to save their frame
                Notepad.showErrorMessage("You dumbass. You didn't save DUMBASS.");
            }

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

    private static void saveComplete(String message){
        JOptionPane.showMessageDialog(null, message, "The TRUTH", JOptionPane.QUESTION_MESSAGE);
    }
}
