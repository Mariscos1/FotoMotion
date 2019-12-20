// imports

import javax.swing.*;
import java.awt.*;

/**
 * Operates GUI for FotoMotion application
 *
 * @author : Jay Acosta
 */
public class Notepad extends JFrame {

    private final String APP_NAME = "FotoMotion";
    public static final int HEIGHT = 500;
    public static final int WIDTH = 750;

    public Notepad() {

        // set the current properties for the initial notepad application
        setTitle(APP_NAME);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setResizable(false);
        setLayout(new BorderLayout());

        // create a navigation bar for the frame
        JMenuBar navigationBar = new JMenuBar();

        // initialize the current menus for the toolbar
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Paint");
        JMenu animateMenu = new JMenu("Animate");

        // set the menu options for any menus
        buildFileMenuOptions(fileMenu);

        // add the menus to the menu bar
        navigationBar.add(fileMenu);
        navigationBar.add(editMenu);
        navigationBar.add(animateMenu);

        // the ribbonPanel will contain all the tools necessary to animate
        JPanel ribbonPanel = new JPanel();
        buildRibbonOptions(ribbonPanel);

        // create the main drawing panel
        PagePanel drawingPanel = new PagePanel();

        // get the current content pane and add all components
        Container contentPane = getContentPane();
        contentPane.add(ribbonPanel, BorderLayout.NORTH);
        contentPane.add(drawingPanel);

        // set the current frame to visible and give the frame a menu bar
        setJMenuBar(navigationBar);
        setVisible(true);
    }

    private void buildFileMenuOptions(JMenu fileMenu) {

        // make the menu items for each component
        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem exit = new JMenuItem("Exit");

        // add mnemonics (key-bindings for menu options)

        // show pop up dialog pop boxes saying that functionality is not ready yet
        save.addActionListener(e -> JOptionPane.showMessageDialog(null, "Save has not been implemented"));
        open.addActionListener(e -> JOptionPane.showMessageDialog(null, "Open has not been implemented"));

        // exit is selected
        exit.addActionListener(e -> System.exit(1));

        // add the menus to the JMenu fileMenu
        fileMenu.add(save);
        fileMenu.add(open);
        fileMenu.add(exit);
    }

    private void buildRibbonOptions(JPanel ribbon) {

        ribbon.setLayout(new FlowLayout(FlowLayout.LEFT));

        // build all tool components
        JCompGrouper toolGrouper = new JCompGrouper("Tools");
        toolGrouper.add(new JButton("Brush"));
        toolGrouper.add(new JButton("Erase"));

        // create a grouper for the size object chooser
        // add all numbers from [4, 64) by increments of 4 as size options
        JCompGrouper sizeGrouper = new JCompGrouper("Size");
        JComboBox<Integer> sizes = new JComboBox<>();
        for(int i = 4; i <= 64; i+=4) {
            sizes.addItem(i);
        }

        // this action listener sets the page panel stroke size to the item at a given "size" index
        sizes.addActionListener(e -> PagePanel.strokeSize = sizes.getItemAt(sizes.getSelectedIndex()));
        sizeGrouper.add(sizes);

        // build the color component
        JCompGrouper colorGrouper = new JCompGrouper("Color");

        // add a color button that allows user to choose the color of their brush
        JButton colorButton = new JButton(" ");
        colorButton.setBackground(Color.BLACK); // set default color to black

        // when color button is pressed, make the JColorChooser pop up
        colorButton.addActionListener(e -> {

            // get color from JColorChooser (parent is colorButton)
            Color selectColor = JColorChooser.showDialog(colorButton, "Choose Brush color", colorButton.getBackground());

            // set the background of the color button to the color selected
            // change PagePanel's brush color to the selected color
            colorButton.setBackground(selectColor);
            PagePanel.color = selectColor;
        });

        colorGrouper.add(colorButton);

        // test.setBackground(ribbonPanel.getBackground());

        // add the grouper s to the ribbon
        ribbon.add(toolGrouper);
        ribbon.add(sizeGrouper);
        ribbon.add(colorGrouper);
    }

    private static class JCompGrouper extends JPanel {

        private JPanel innerContainer = new JPanel();

        private JCompGrouper() {
            // set the current layout as a border layout
            setLayout(new BorderLayout());

            // set the inner container as a JPanel with a flow layout
            innerContainer.setLayout(new FlowLayout());
            super.add(innerContainer);

            this.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, Color.GRAY));
        }

        private JCompGrouper(String label) {

            // call default constructor
            this();

            // create a label for this group
            JLabel groupLabel = new JLabel(label);

            // add the group layout that is center-aligned to the bottom
            groupLabel.setHorizontalAlignment(JLabel.CENTER);
            add(groupLabel, BorderLayout.SOUTH);
        }

        public void setBackground(Color color) {

            // set the color of the inner background and this background to the color
            super.setBackground(color);

            if(innerContainer != null) {
                innerContainer.setBackground(color);
            }
        }

        public Component add(Component component) {

            // add the component to the inner container
            return innerContainer.add(component);
        }
    }
}
