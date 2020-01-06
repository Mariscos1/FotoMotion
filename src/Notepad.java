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
    private final String FONT = "Serif";
    public static final int FONT_SIZE = 16;
    public static final int HEIGHT = 570;
    public static final int WIDTH = 860;
    public static final int DRAWING_HEIGHT = 440;
    public static final int DRAWING_WIDTH = 750;
    public static final Dimension BUTTON_DIMENSION = new Dimension(48, 32);
    public static final Dimension VER_BUTTON_DIMENSION = new Dimension(48, 26);

    private final int RIBBON_HEIGHT = 60;
    private final int RIBBON_WIDTH = 90;

    private JPanel parentRibbon;

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
        JMenuBar verticalBar = new JMenuBar();

        // initialize the current menus for the toolbar
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        // set the menu options for any menus
        buildFileMenuOptions(fileMenu);
        buildEditMenuOptions(editMenu);

        // add the menus to the menu bar
        verticalBar.add(fileMenu);
        verticalBar.add(editMenu);

        // create the main animation panel
        anim = new AnimationPanel(DRAWING_WIDTH, DRAWING_HEIGHT);

        // the ribbonPanel will contain all the tools necessary to animate
        JPanel ribbonPanel = new JPanel();
        buildRibbonOptions(ribbonPanel);

        JPanel navRibbon = new JPanel();
        buildVerticalRibbon(navRibbon);

        // get the current content pane and add all components
        Container contentPane = getContentPane();
        contentPane.add(ribbonPanel, BorderLayout.NORTH);
        contentPane.add(navRibbon, BorderLayout.WEST);
        contentPane.add(anim);

        // set the current frame to visible and give the frame a menu bar
        setJMenuBar(verticalBar);

        // pack the current JFrame to get preferred sizes and then make visible
        pack();
        setVisible(true);

        anim.initDrawPanel();
        panelsGrouper.add(addPanel());
    }

    private void buildFileMenuOptions(JMenu fileMenu) {

        // make the menu items for each component
        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem exit = new JMenuItem("Exit");

        // add mnemonics (key-bindings for menu options)

        // show pop up dialog pop boxes saying that functionality is not ready yet
        save.addActionListener(e -> anim.save(save));
        open.addActionListener(e -> Notepad.showErrorMessage("Open has not been implemented"));

        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

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
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");

        prev.setAccelerator(KeyStroke.getKeyStroke("LEFT"));
        next.setAccelerator(KeyStroke.getKeyStroke("RIGHT"));
        newFrame.setAccelerator(KeyStroke.getKeyStroke('+'));
        removeFrame.setAccelerator(KeyStroke.getKeyStroke('-'));
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));

        prev.addActionListener(e -> backwardOneFrame());
        next.addActionListener(e -> forwardOneFrame());

        newFrame.addActionListener(e -> add());
        removeFrame.addActionListener(e -> remove());

        undo.addActionListener(e -> anim.undo());
        redo.addActionListener(e -> anim.redo());

        copy.addActionListener(e -> copy());
        paste.addActionListener(e -> paste());

        editMenu.add(paint);
        editMenu.add(animate);
        editMenu.addSeparator();
        editMenu.add(prev);
        editMenu.add(next);
        editMenu.addSeparator();
        editMenu.add(newFrame);
        editMenu.add(removeFrame);
        editMenu.addSeparator();
        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.add(copy);
        editMenu.add(paste);
    }

    private void buildRibbonOptions(JPanel ribbon) {
        parentRibbon = ribbon;
        parentRibbon.setLayout(new CardLayout());

        JPanel animateRibbon = new JPanel();
        buildAnimateRibbon(animateRibbon);

        parentRibbon.add(animateRibbon, "ANIMATE");
    }

    private void buildVerticalRibbon(JPanel verRibbon) {
        //add all components before gbc, please try to keep them in order
        verRibbon.setLayout(new GridBagLayout());

        JComboBox<Integer> sizes = new JComboBox<>();
        for (int i = 4; i <= 64; i += 4) {
            sizes.addItem(i);
        }
        sizes.setSelectedIndex(3);

        // this action listener sets the page panel stroke size to the item at a given "size" index
        sizes.addActionListener(e -> {
            anim.setBrushSize((Integer) sizes.getSelectedItem());
        });

        JButton erase = new JButton("E");
        erase.addActionListener(e -> anim.erase());

        JButton brush = new JButton("B");
        brush.addActionListener(e -> anim.brush());

        // add a color button that allows user to choose the color of their brush
        JButton colorButton = new JButton(" ");
        colorButton.setFocusable(false);
        colorButton.setPreferredSize(VER_BUTTON_DIMENSION);
        colorButton.setBackground(Color.BLACK); // set default color to black

        // when color button is pressed, make the JColorChooser pop up
        colorButton.addActionListener(e -> {

            // get color from JColorChooser (parent is colorButton)
            Color selectColor = JColorChooser.showDialog(colorButton, "Choose Brush color", colorButton.getBackground());

            if (selectColor != null) {
                // set the background of the color button to the color selected
                // change PagePanel's brush color to the selected color
                anim.setBrushColor(selectColor);
                colorButton.setBackground(new Color(selectColor.getRGB()));
            }
        });

        JLabel frameLabel = new JLabel("Frame");
        JComboBox<Integer> frameIndices = new JComboBox<>();
        anim.setJComboBox(frameIndices);
        frameIndices.addActionListener(e ->
        {
            if (frameIndices.getSelectedIndex() >= 0) {
                anim.setCurrentFrame(frameIndices.getSelectedIndex());
            }
        });

        JButton prev = new JButton("<-");
        prev.setPreferredSize(VER_BUTTON_DIMENSION);
        prev.addActionListener(e -> backwardOneFrame());

        JButton next = new JButton("->");
        next.setPreferredSize(VER_BUTTON_DIMENSION);
        next.addActionListener(e -> anim.forwardOneFrame());

        JButton undoButton = new JButton("U");
        undoButton.setPreferredSize(VER_BUTTON_DIMENSION);
        undoButton.addActionListener(e -> anim.undo());

        JButton redoButton = new JButton("R");
        redoButton.setPreferredSize(VER_BUTTON_DIMENSION);
        redoButton.addActionListener(e -> anim.redo());

        JButton copyButton = new JButton("C");
        copyButton.setPreferredSize(VER_BUTTON_DIMENSION);
        copyButton.addActionListener(e -> copy());

        JButton pasteButton = new JButton("P");
        pasteButton.addActionListener(e -> paste());

        JButton clearPage = new JButton("x");
        clearPage.addActionListener(e -> anim.clearPage());

        JButton clearAll = new JButton("X");
        clearAll.addActionListener(e -> anim.clearAll());


        //adding with GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Paint Block
        addSeparator(gbc, verRibbon, "Paint");
        gbc.gridx = 1;
        gbc.gridy++;
        verRibbon.add(sizes, gbc);
        addButtonSet(erase, brush, verRibbon, gbc);
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        verRibbon.add(colorButton, gbc);
        gbc.gridwidth = 1;

        //navigation block
        addSeparator(gbc, verRibbon, "Navigation");

        //for frame indices
        gbc.gridy++;
        gbc.gridx = 1;
        verRibbon.add(frameIndices, gbc);

        addButtonSet(prev, next, verRibbon, gbc);

        //edit block
        addSeparator(gbc, verRibbon, "Edit");
        addButtonSet(undoButton, redoButton, verRibbon, gbc);
        addButtonSet(copyButton, pasteButton, verRibbon, gbc);
        addButtonSet(clearPage, clearAll, verRibbon, gbc);

        //padding to move all the components to the top
        for (int i = 0; i < 6; i++) {
            JPanel dud = new JPanel();
            JPanel dud2 = new JPanel();
            dud.setPreferredSize(VER_BUTTON_DIMENSION);
            dud2.setPreferredSize(VER_BUTTON_DIMENSION);
            addButtonSet(dud, dud2, verRibbon, gbc);
        }
    }

    private void addButtonSet(JComponent comp1, JComponent comp2, JPanel panel, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(comp1, gbc);
        gbc.gridx++;
        panel.add(comp2, gbc);
    }

    private void addSeparator(GridBagConstraints gbc, JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT, Font.ITALIC, FONT_SIZE));
        label.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));
        centerLabel(label);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);
        gbc.gridwidth = 1;
    }

    private void centerLabel(JLabel label) {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
    }

    private void buildAnimateRibbon(JPanel animateRibbon) {
        // sets the size of the current animateRibbon and the current layout of the current animateRibbon
        animateRibbon.setPreferredSize(new Dimension(WIDTH, RIBBON_HEIGHT));
        animateRibbon.setLayout(new FlowLayout(FlowLayout.LEFT));

        // build all tool components
        JCompGrouper frameGrouper = new JCompGrouper("Frame", new FlowLayout());
        frameGrouper.setBorder(0, 0, 2, 2);

        JCheckBox shadowBox = new JCheckBox("Shadow");
        shadowBox.addActionListener(e -> anim.showShadow(shadowBox.isSelected()));

        JButton addFrame = new JButton("+");
        addFrame.addActionListener(e -> add());

        JButton removeFrame = new JButton("-");
        removeFrame.addActionListener(e -> remove());

        JButton removeAll = new JButton("=");
        removeAll.addActionListener(e -> removeAllPanels());

        frameGrouper.add(shadowBox);
        frameGrouper.add(addFrame);
        frameGrouper.add(removeFrame);
        frameGrouper.add(removeAll);


        JCompGrouper animGrouper = new JCompGrouper("Animation", new FlowLayout());
        animGrouper.setBorder(0, 0, 0, 2);

        JButton animateButton = new JButton("Play");
        animateButton.addActionListener(e ->
        {
            anim.play();

            // potential implementation for play/pause
//            if(animateButton.getText().equals("Play")) {
//                animateButton.setText("Pause");
//            } else {
//                animateButton.setText("Play");
//            }
        });

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

        panelsGrouper = new JCompGrouper();
        JScrollPane windowPain = new JScrollPane(panelsGrouper, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        windowPain.setPreferredSize(new Dimension(370, RIBBON_HEIGHT));
        windowPain.setFocusable(false);

        animateRibbon.add(frameGrouper);
        animateRibbon.add(animGrouper);
        animateRibbon.add(windowPain);
    }

    private void copy() {
        anim.copy();
    }

    private void paste() {
        anim.paste();
    }

    private void backwardOneFrame() {
        panelsGrouper.getComponentAt(anim.getCurrentIndex()).repaint();
        anim.backwardOneFrame();
    }

    private void forwardOneFrame() {
        panelsGrouper.getComponentAt(anim.getCurrentIndex()).repaint();
        anim.forwardOneFrame();
    }

    private void removeAllPanels() {
        anim.removeAllPanels();
        panelsGrouper.removeAll();
        panelsGrouper.add(addPanel());
    }

    private JButton addPanel() {
        JButton panel = new MyButton();
        panel.setFocusable(false);

        panel.setPreferredSize(BUTTON_DIMENSION);

        panel.addActionListener(e -> anim.setCurrentFrame(panelsGrouper.getIndexAt(panel)));

        revalidate();
        return panel;
    }

    private class MyButton extends JButton {
        public void paintComponent(Graphics g) {
            g.drawImage(anim.getImage(panelsGrouper.getIndexAt(this)), 0, 0, getWidth(), getHeight(), null);
        }
    }

    private void add() {
        anim.addPanel();
        panelsGrouper.add(addPanel());
    }

    private void remove() {
        if (panelsGrouper.getLength() > 1) {
            panelsGrouper.remove(anim.getCurrentIndex());
            anim.removePanel();
        } else {
            anim.clearPage();
        }
    }

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}