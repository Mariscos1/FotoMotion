import javax.swing.*;

/**
 * @author : Jay Acosta, Janlloyd Carangan, Ramon Marquez
 */

public class Main {

    // options to select when choosing which look and feel you want
    // themes depend on OS, use seeAvailableLookAndFeels() to see which are available
    private static final String
            METAL_THEME = "javax.swing.plaf.metal.MetalLookAndFeel", // this is the default theme
            NIMBUS_THEME = "javax.swing.plaf.nimbus.NimbusLookAndFeel", // this one looks the most professional imo
            MOTIF_THEME = "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
            WINDOWS_THEME = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
            WINDOWS_CLASSIC_THEME = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";

    // edit these values to alter the appearance of the JFrame
    private static final String CHOSEN_THEME = NIMBUS_THEME;
    private static final boolean DEFAULT_DECORATION = false;

    // Runs the main program
    public static void main(String[] args) {
        // seeAvailableLookAndFeels();
        initLookAndFeel();

        // ensures the current drawing panel is thread-safe
        // possibly useful for data compression algorithm
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Notepad();
            }
        });
    }

    // prints out all available look and feels installed on this operating system
    private static void seeAvailableLookAndFeels() {
        System.out.println("Available look and feels: ");

        // iterate through all installed look and feels and print them out
        for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
            System.out.println("- " + info.getClassName());
        }
    }

    // initialize the current JFrame's look and feel
    private static void initLookAndFeel() {

        try {

            // set the look and feel using the UIManager
            UIManager.setLookAndFeel(CHOSEN_THEME);

            // set the default look and feel for the JFrame upon creation
            JFrame.setDefaultLookAndFeelDecorated(DEFAULT_DECORATION);
        } catch (Exception e) {
            System.err.println("The theme \"" + CHOSEN_THEME + "\" is not available on this operating system.\n" +
                    "Choosing the default operating system theme.");
            e.printStackTrace();
        }
    }
}