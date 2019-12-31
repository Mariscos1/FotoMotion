// imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Operates GUI for FotoMotion application
 *
 * @author : Jay Acosta
 */
public class Notepad extends JFrame {

    private final String APP_NAME = "FotoMotion";
    public static final int HEIGHT = 500;
    public static final int WIDTH = 750;
    public static final Dimension BUTTON_DIMENSION = new Dimension(48, 32);

    private final int RIBBON_HEIGHT = 60;

    private AnimationPanel anim;

    private JCompGrouper panelsGrouper;

    public Notepad() {

        // set the current properties for the initial notepad application
        setTitle(APP_NAME);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setResizable(false);
        setLayout(new BorderLayout());

        // create a navigation bar for the frame
        JMenuBar navigationBar = new JMenuBar();

        // initialize the current menus for the toolbar
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        // set the menu options for any menus
        buildFileMenuOptions(fileMenu);
        buildEditMenuOptions(editMenu);

        // add the menus to the menu bar
        navigationBar.add(fileMenu);
        navigationBar.add(editMenu);

        // create the main animation panel
        anim = new AnimationPanel(WIDTH, HEIGHT - RIBBON_HEIGHT);

        // the ribbonPanel will contain all the tools necessary to animate
        JPanel ribbonPanel = new JPanel();
        buildRibbonOptions(ribbonPanel);

        // get the current content pane and add all components
        Container contentPane = getContentPane();
        contentPane.add(ribbonPanel, BorderLayout.NORTH);
        contentPane.add(anim);

        // set the current frame to visible and give the frame a menu bar
        setJMenuBar(navigationBar);

        // pack the current JFrame to get preferred sizes and then make visible
        pack();
        setVisible(true);

        anim.initDrawPanel();
        panelsGrouper.add(addPanel(0));
    }

    private void buildFileMenuOptions(JMenu fileMenu) {

        // make the menu items for each component
        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem exit = new JMenuItem("Exit");

        // add mnemonics (key-bindings for menu options)

        // show pop up dialog pop boxes saying that functionality is not ready yet
        save.addActionListener(e -> Notepad.showErrorMessage("Save has not been implemented"));
        open.addActionListener(e -> Notepad.showErrorMessage("Open has not been implemented"));

        // exit is selected
        exit.addActionListener(e -> System.exit(1));

        // add the menus to the JMenu fileMenu
        fileMenu.add(save);
        fileMenu.add(open);
        fileMenu.add(exit);
    }

    private void buildEditMenuOptions(JMenu editMenu) {
        JMenuItem paint = new JMenuItem("Paint");
        JMenuItem animate = new JMenuItem("Animate");

        paint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        animate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));

        paint.addActionListener(e -> ((CardLayout) parentRibbon.getLayout()).show(parentRibbon, "PAINT"));
        animate.addActionListener(e -> ((CardLayout) parentRibbon.getLayout()).show(parentRibbon, "ANIMATE"));

        JMenuItem prev = new JMenuItem("Previous Frame");
        JMenuItem next = new JMenuItem("Next Frame");
        JMenuItem newFrame = new JMenuItem("New Frame");
        JMenuItem removeFrame = new JMenuItem("Remove Frame");

        prev.setAccelerator(KeyStroke.getKeyStroke("LEFT"));
        next.setAccelerator(KeyStroke.getKeyStroke("RIGHT"));
        newFrame.setAccelerator(KeyStroke.getKeyStroke('+'));
        removeFrame.setAccelerator(KeyStroke.getKeyStroke('-'));

        prev.addActionListener(e -> anim.backwardOneFrame());
        next.addActionListener(e -> anim.forwardOneFrame());

        newFrame.addActionListener(e -> add());
        removeFrame.addActionListener(e -> remove());

        editMenu.add(paint);
        editMenu.add(animate);
        editMenu.addSeparator();
        editMenu.add(prev);
        editMenu.add(next);
        editMenu.addSeparator();
        editMenu.add(newFrame);
        editMenu.add(removeFrame);
    }

    private void buildRibbonOptions(JPanel ribbon) {
        parentRibbon = ribbon;
        parentRibbon.setLayout(new CardLayout());

        JPanel paintRibbon = new JPanel();
        buildPaintingRibbon(paintRibbon);

        JPanel animateRibbon = new JPanel();
        buildAnimateRibbon(animateRibbon);

        parentRibbon.add(paintRibbon, "PAINT");
        parentRibbon.add(animateRibbon, "ANIMATE");
    }

    private void buildPaintingRibbon(JPanel paintRibbon) {

        // sets the size of the current paintRibbon and the current layout of the current paintRibbon
        paintRibbon.setPreferredSize(new Dimension(WIDTH, RIBBON_HEIGHT));
        paintRibbon.setLayout(new FlowLayout(FlowLayout.LEFT));

        // build all tool components
        JCompGrouper toolGrouper = new JCompGrouper("Tools");

        JCheckBox seePrevBox = new JCheckBox("Opaque Panel");
        seePrevBox.addActionListener(e -> anim.seeOpaque(seePrevBox));

        JButton erase = new JButton("Erase");
        erase.addActionListener(e -> anim.erase());

        JButton brush = new JButton("Brush");
        brush.addActionListener(e -> anim.brush());

        JButton clearPage = new JButton("Clear");
        clearPage.addActionListener(e -> anim.clearPage());

        //toolGrouper.add(seePrevBox);
        toolGrouper.add(brush);
        toolGrouper.add(erase);
        toolGrouper.add(clearPage);

        // create a grouper for the size object chooser
        // add all numbers from [4, 64] by increments of 4 as size options
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
            PagePanel.brushColor = selectColor;
        });

        colorGrouper.add(colorButton);

        // test.setBackground(ribbonPanel.getBackground());

        // add the grouper s to the paintRibbon
        paintRibbon.add(toolGrouper);
        paintRibbon.add(sizeGrouper);
        paintRibbon.add(colorGrouper);
    }

    private void buildAnimateRibbon(JPanel animateRibbon) {
        // sets the size of the current animateRibbon and the current layout of the current animateRibbon
        animateRibbon.setPreferredSize(new Dimension(WIDTH, RIBBON_HEIGHT));
        animateRibbon.setLayout(new FlowLayout(FlowLayout.LEFT));

        // build all tool components
        JCompGrouper frameAdders = new JCompGrouper("Frame");

        JButton addFrame = new JButton("+");
        addFrame.addActionListener(e -> add());

        JButton removeFrame = new JButton("-");
        removeFrame.addActionListener(e -> remove());

        frameAdders.add(addFrame);
        frameAdders.add(removeFrame);

        JCompGrouper navigation = new JCompGrouper("Navigation");
        JButton prev = new JButton("<-");
        prev.addActionListener(e -> anim.backwardOneFrame());

        JButton next = new JButton("->");
        next.addActionListener(e -> anim.forwardOneFrame());

        JComboBox<Integer> frameIndices = new JComboBox<>();
        // frameIndices.addItem(1);
        anim.setJComboBox(frameIndices);
        frameIndices.addActionListener(e ->
        {
            if(frameIndices.getSelectedIndex() >= 0) {
                anim.setCurrentFrame(frameIndices.getSelectedIndex());
            }
        });

        navigation.add(frameIndices);
        navigation.add(prev);
        navigation.add(next);

        JCompGrouper animGrouper = new JCompGrouper("Animation");

        JButton animateButton = new JButton("Play");
        animateButton.addActionListener(e -> anim.play());

        JCheckBox loopButton = new JCheckBox("Loop");
        animateButton.addActionListener(e -> anim.setLooping(loopButton.isSelected()));

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> anim.stop());

        JComboBox<Integer> fpsBox = new JComboBox<>();
        for (int i = 0; i < 60; i++) {
            fpsBox.addItem((i + 1));
        }

        fpsBox.addActionListener(e -> anim.setFPS((Integer) fpsBox.getSelectedItem()));

        animGrouper.add(fpsBox);
        animGrouper.add(animateButton);
        animGrouper.add(loopButton);
        animGrouper.add(stopButton);

        panelsGrouper = new JCompGrouper("Panels");

        animateRibbon.add(frameAdders);
        animateRibbon.add(navigation);
        animateRibbon.add(animGrouper);
        // animateRibbon.add(panelsGrouper);
    }

    private JButton addPanel(int index){
        JButton panel = new JButton();

        panel.setPreferredSize(BUTTON_DIMENSION);

        if(anim.framesLength() > 0) {
            panel.setIcon(new ImageIcon(anim.getImage(index)));
        }

        panel.addActionListener(e -> anim.setCurrentFrame(panelsGrouper.getIndexAt(panel)));
        return panel;
    }

    private void add() {
        anim.addPanel();
        panelsGrouper.add(addPanel(anim.getCurrentIndex()));
    }

    private void remove() {
        anim.removePanel();
        panelsGrouper.remove(anim.getCurrentIndex());
    }

    private JPanel parentRibbon;

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private static class JCompGrouper extends JPanel {

        private JPanel innerContainer = new JPanel();

        private JCompGrouper() {
            // set the current layout as a border layout
            setLayout(new BorderLayout());

            // set the inner container as a JPanel with a flow layout
            innerContainer.setLayout(new FlowLayout());
            super.add(innerContainer);

            this.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));
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

        public void remove(int currentIndex) {
            innerContainer.remove(currentIndex);
        }

        public int getIndexAt(Component other) {
            int index = -1;

            for(Component comp: innerContainer.getComponents()) {
                index++;
                if(comp.equals(other))
                    return index;
            }

            return index;
        }
    }
}
