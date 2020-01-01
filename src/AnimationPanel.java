import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Operates the animation and frame handling of all page panels
 *
 * @author : Jay Acosta
 */
public class AnimationPanel extends JPanel {
    private static final int DEFAULT_DELAY = 1000;

    private List<Image> frames;
    private PagePanel currentPanel;
    private int currentIndex;
    private int delay;

    private Dimension preferredSize;

    private JComboBox<Integer> frameIndices;

    private boolean removing;
    private boolean isLooping;
    private Timer timer; //if false, it is play, if true it is pause
    private boolean showPrevPanel;

    public AnimationPanel() {
        delay = DEFAULT_DELAY;
        frames = new ArrayList<>();
        timer = new Timer(delay, new AnimationTask());
        // currentIndex = -1;
    }

    public AnimationPanel(int width, int height) {
        this();
        //setBackground(Color.BLACK);
        preferredSize = new Dimension(width, height);
        setPreferredSize(preferredSize);

        addBlankPanel();
    }

    public void addBlankPanel() {
        PagePanel addThis = new PagePanel();
        addThis.setPreferredSize(preferredSize);
        add(addThis);

        currentPanel = addThis;
    }

    public void initDrawPanel() {
        currentPanel.init();
        frames.add(currentPanel.getCurrentImage());
        frameIndices.addItem(frames.size());
    }

    public void addPanel() {

        // update the frame option box
        frameIndices.addItem(frames.size() + 1);

        // set the current frame to the current image at the current index
        frames.set(currentIndex, currentPanel.getCurrentImage());
        // currentPanel.setPrevImage(currentPanel.getCurrentImage());

        // add frame clear the current panel and add another frame by incrementing the index
        currentPanel.clearImage();
        frames.add(++currentIndex, currentPanel.getCurrentImage());

        updateBoxNum();
    }

    public void removePanel() {

        if (frames.size() > 1) {

            removing = true;
            frames.remove(currentIndex);
            frameIndices.removeAllItems(); //problem
            resetIndices();
            if (frames.size() <= currentIndex) {
                currentIndex--;
            }
            System.out.println("Remove Panel Index: " + currentIndex);
            removing = false;
            setCurrentFrame(currentIndex);
        }
    }

    private void resetIndices() {
        for (int i = 0; i < frames.size(); i++) {
            frameIndices.addItem(i + 1);
        }
    }

    public void forwardOneFrame() {
        if (currentIndex < frames.size() - 1) {
            // currentPanel.setImage(frames.get(++currentIndex));
            setCurrentFrame(++currentIndex);
        }
        // System.out.println("Index: " + currentIndex);
        updateBoxNum();
    }


    public void backwardOneFrame() {
        if (currentIndex > 0) {
            // currentPanel.setImage(frames.get(--currentIndex));
            setCurrentFrame(--currentIndex);
        }
        updateBoxNum();

        // System.out.println("Index: " + currentIndex);
    }

    public void updateBoxNum() {
        frameIndices.setSelectedItem(currentIndex + 1);
    }

    public void setJComboBox(JComboBox<Integer> other) {
        frameIndices = other;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentFrame(int newIndex) {
        if (!removing) {
            currentIndex = newIndex;
            currentPanel.setImage(frames.get(currentIndex));

            if(currentIndex - 1 >= 0) {
                currentPanel.setPrevImage(frames.get(currentIndex - 1));
            }

            updateBoxNum();
        }
    }

    public void play() {
        timer.start();
    }

    public void setLooping(boolean isLooping){
        this.isLooping = isLooping;
    }

    public void stop(){
        timer.stop();
    }

    public void erase() {
        currentPanel.erase();
    }

    public void brush() {
        currentPanel.brush();
    }

    public void setFPS(int FPS) {
        timer.setDelay(1000 / FPS);
        timer.setInitialDelay(1000 / FPS);
    }

    public void seeOpaque(JCheckBox box){
        if(box.isSelected()){
            if(frames.size() > 1 && currentIndex != 0){
                currentPanel.setPrevImage(frames.get(currentIndex - 1));
            }
        }else{
            currentPanel.removePrevImage();
        }

        showPrevPanel = box.isSelected();
    }

    public void clearPage(){
        currentPanel.deletePage();
    }

    public int framesLength(){
        return frames.size();
    }

    public Image getImage(int index){
        if(frames.size() > 0)
            return frames.get(index);
        return null;
    }

    public void showShadow(boolean show) {
        currentPanel.showShadow(show);
    }

    private class AnimationTask implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (currentIndex + 1 < frames.size()) {
                forwardOneFrame();
            } else if (isLooping){
                setCurrentFrame(0);
            }else{
                timer.stop();
            }
        }
    }
}
