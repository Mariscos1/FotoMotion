import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    //Actual pop-up that allows for directory search
    private static JFileChooser directory = new JFileChooser();
    private static ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();

    //Sets up the information for the File Chooser, such as the directories ur allowed to use, like how Karen used me
    static {
        //Gives the default directory given the OS, makes it less complicated really
        directory.setCurrentDirectory(null);

        directory.setDialogTitle("I'm sorry karen, i'm good at cornhole, give me the kidz");
        //Allows us to only access the directory and not the actual folder so there can't be any accidental overwrites
        directory.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    public static Image getImage(String pathName) {
        return null;
    }

    //Currently only saves the current frame to any file on the users computer
    public static void save(Image currentPanel, Component save) {
        try {

            //Will function as the full pathway that the snippet will be saved to
            String pathway = ".png";

            //Runs if the actual save button is pressed, then it does all the good stuff below
            if(directory.showSaveDialog(save) == JFileChooser.APPROVE_OPTION){
                pathway = directory.getSelectedFile().getAbsolutePath() + pathway;

                File f1 = new File(pathway);

                //Snippet converts whatever image that was sent into the method into a BufferedImage
                BufferedImage temp = turnBuff(currentPanel);

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


    public synchronized static List<Image> open(Component parent) {
        String pathway = "";
        List<Image> newGifFrames = new ArrayList<>();
        if(directory.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            //Gets the pathway of the gif that we are trying to dankify
            pathway = directory.getSelectedFile().getAbsolutePath() + pathway;
        }
        try {
            ImageInputStream input = ImageIO.createImageInputStream(new File(pathway));
            reader.setInput(input);

            int count = reader.getNumImages(true);

            for(int i = 0; i < count; i++){
                newGifFrames.add(reader.read(i));
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return newGifFrames;
    }

    public static void export() {

    }

    public static void saveAs(Component parent, List<Image> gifImages, int timeBetweenFrame) throws IOException {

        String pathway = ".gif";
        if(directory.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            //Gets the directory that the user wishes to save it at
            pathway = directory.getSelectedFile().getAbsolutePath() + pathway;

            if (gifImages.size() > 1) {
                BufferedImage firstOne = turnBuff(gifImages.get(0));
                // create a new BufferedOutputStream with the last argument
                ImageOutputStream output = new FileImageOutputStream(new File(pathway));

                // create a gif sequence with the type of the first image, 1 second
                // between frames, which loops continuously
                GifSequenceWriter writer = new GifSequenceWriter(output, firstOne.getType(), timeBetweenFrame, true);
                writer.writeToSequence(firstOne);
                for(int i = 1; i < gifImages.size(); i ++){
                    writer.writeToSequence(turnBuff(gifImages.get(i)));
                }
                writer.close();
                output.close();
            } else{
                Notepad.showErrorMessage("Dumbass make more than one slide, fucking idiot *sigh*");
            }
        }
    }

    //Helper method for the save method that allows the user to know that their work has been saved
    private static void saveComplete(String message){
        JOptionPane.showMessageDialog(null, message, "The TRUTH", JOptionPane.QUESTION_MESSAGE);
    }

    //Helper method that helps convert the images sent as a parameter to a BufferedImage Type, then proceeds to return the Buff Image #GAINZ
    private static BufferedImage turnBuff(Image currentImage){
        BufferedImage temp;
        if(currentImage instanceof BufferedImage) {
            temp = (BufferedImage) currentImage;
        } else if (currentImage instanceof VolatileImage) {
            temp = ((VolatileImage) currentImage).getSnapshot();
        } else  {
            throw new IllegalArgumentException("gay please");
        }
        return temp;
    }
}
