import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Operates the animation and frame handling of all page panels
 *
 * @author : Jay Acosta
 */
public class AnimationPanel extends JPanel {

    // class constants
    private final int DEFAULT_DELAY = 1000; // in milliseconds

    // instance variables

    // drawing panel
    private PagePanel currentPanel;

    // frame list and index
    private List<Image> frames;
    private List<Deque<Image>> undoStack;
    private List<Deque<Image>> redoStack;
    private int currentIndex;

    private JComboBox<Integer> frameIndices; // boxes

    // conditionals
    private boolean removing;

    // timing/animation
    private boolean isLooping;
    private Timer timer;
    private Image copiedImage;
    private int delay;

    // initialize the current animation panel instance variables
    public AnimationPanel() {
        frames = new ArrayList<>(); // create a new list to contain the frames
        undoStack = new LinkedList<>();
        redoStack = new LinkedList<>();

        // set the properties for the timer
        delay = DEFAULT_DELAY;
        timer = new Timer(delay, new AnimationTask());
    }

    public AnimationPanel(int width, int height) {
        this(); // default constructor call

        // set preferred size to width and height
        Dimension preferredSize = new Dimension(width, height);
        setPreferredSize(preferredSize);

        // add a blank drawing panel
        addBlankPanel(preferredSize);
    }

    public void addBlankPanel(Dimension preferredSize) {

        // create a new panel and add the current panel to the JPanel
        PagePanel addThis = new PagePanel();
        addThis.setPreferredSize(preferredSize);
        add(addThis);
        currentPanel = addThis; // currentPanel points to PagePanel instance
    }

    // initializes properties that cannot be instantiated during constructor call
    public void initDrawPanel() {

        // call init for PagePanel "currentPanel"
        currentPanel.init();

        // add frame image to the list and drop down index menu
        frames.add(currentPanel.getCurrentImage());
        undoStack.add(currentPanel.getUndoStack());
        redoStack.add(currentPanel.getRedoStack());
        frameIndices.addItem(frames.size());
    }

    /*
     * Navigating through frames
     * Includes adding/deleting frames and setting the current frame
     */

    public void addPanel() {

        // update the frame option box
        frameIndices.addItem(frames.size() + 1);

        // set the current frame to the current image at the current index
        frames.set(currentIndex, currentPanel.getCurrentImage());
        undoStack.set(currentIndex, currentPanel.getUndoStack());
        redoStack.set(currentIndex, currentPanel.getRedoStack());

        // add frame clear the current panel and add another frame by incrementing the index
        currentPanel.clearImage();
        frames.add(++currentIndex, currentPanel.getCurrentImage());
        undoStack.add(currentIndex, currentPanel.getUndoStack());
        redoStack.add(currentIndex, currentPanel.getRedoStack());

        updateBoxNum(); // update index drop down
    }

    public void removePanel() {

        // if there is more than one frame
        if (frames.size() > 1) {

            // set removing and remove frame at current index
            frames.remove(currentIndex);
            undoStack.remove(currentIndex);
            redoStack.remove(currentIndex);

            // remove all items from drop down index menu and update the indices
            removing = true;
            frameIndices.removeAllItems();
            resetIndices();
            removing = false;

            // if the currentIndex is bigger than the size of the frames list, decrement the index
            // occurs when removing an item at the end of the list
            if (currentIndex >= frames.size()) {
                currentIndex = frames.size() - 1;
            }

            // set the current frame to the current index
            setCurrentFrame(currentIndex);
        }
    }

    public void removeAllPanels() {
        for (int i = frames.size() - 1; i >= 0; i--) {
            setCurrentFrame(i);
            removePanel();
        }
        clearPage();
    }

    private void resetIndices() {

        // add every item from [0, frames.size)
        for (int i = 0; i < frames.size(); i++) {
            frameIndices.addItem(i + 1);
        }
    }

    public void forwardOneFrame() {

        if (currentIndex + 1 < frames.size()) {

            // only increment if next frame does not go above frames.size()
            setCurrentFrame(++currentIndex);
        }

        updateBoxNum();
    }


    public void backwardOneFrame() {
        if (currentIndex > 0) {

            // only decrement if prev frame does not go below zero
            setCurrentFrame(--currentIndex);
        }

        updateBoxNum();
    }

    public void setCurrentFrame(int newIndex) {
        if (!removing) {

            currentIndex = newIndex;
            currentPanel.setImage(frames.get(currentIndex), false);
            currentPanel.setUndoStack(undoStack.get(currentIndex));
            currentPanel.setRedoStack(redoStack.get(currentIndex));

            if (currentIndex - 1 >= 0) {
                currentPanel.setPrevImage(frames.get(currentIndex - 1));
            }

            updateBoxNum();
        }
    }


    /*
     * Getters and Setters
     */

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int framesLength() {
        return frames.size();
    }

    public Image getImage(int index) {
        if (frames.size() > 0)
            return frames.get(index);
        System.out.println("returning null");
        return null;
    }

    /*
     * Frame Index Menu Box
     */

    public void updateBoxNum() {
        frameIndices.setSelectedItem(currentIndex + 1);
    }

    public void setJComboBox(JComboBox<Integer> other) {
        frameIndices = other;
    }

    /*
     * Calls to PagePanel
     */

    public void setBrushSize(int brushSize) {
        currentPanel.setBrushSize(brushSize);
    }

    public void setBrushColor(Color brushColor) {
        currentPanel.setBrushColor(brushColor);
    }

    public void erase() {
        currentPanel.setEraseMode();
    }

    public void brush() {
        currentPanel.setPaintMode();
    }

    public void clearPage() {
        currentPanel.clearPage();
    }

    public void clearAll() {
        for (int i = frames.size() - 1; i >= 0; i--) {
            setCurrentFrame(i);
            clearPage();
        }
    }

    public void copy(){
        copiedImage = currentPanel.deepCopy(frames.get(currentIndex));
    }

    public void paste(){

        undoStack.get(currentIndex).push(currentPanel.deepCopy(copiedImage));

        frames.set(currentIndex, copiedImage);
        currentPanel.setImage(frames.get(currentIndex), false);

    }

    public void undo() {

        Image undoImage = currentPanel.undo();

        if (undoImage != null) {
            frames.set(currentIndex, undoImage);
            currentPanel.setImage(frames.get(currentIndex), true);
        } else {
            //System.out.println("cleared");
            currentPanel.clearImage();
        }
    }

    public void redo() {
        Image redoImage = currentPanel.redo();
        System.out.println(redoImage);

        if (redoImage != null) {
            frames.set(currentIndex, redoImage);
        }

        currentPanel.setImage(frames.get(currentIndex), true);
    }

    public void showShadow(boolean show) {
        currentPanel.showShadow(show);
        setCurrentFrame(currentIndex);
    }

    /*
     * Animation
     */

    public void play() {
        timer.start();
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    public void stop() {
        timer.stop();
    }

    public void setFPS(int FPS) {
        timer.setDelay(1000 / FPS);
        timer.setInitialDelay(1000 / FPS);
    }

    private class AnimationTask implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (currentIndex + 1 < frames.size()) {

                // if not at the end, call forward one frame
                forwardOneFrame();
            } else if (isLooping) {

                // else, if looping is enabled, set frame to index 0
                setCurrentFrame(0);
            } else {

                // otherwise, stop the timer
                timer.stop();
            }
        }
    }

    public void save(Component save) {
        FileManager.save(frames.get(currentIndex), save);
    }
}
