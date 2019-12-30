import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Operates the animation and frame handling of all page panels
 *
 * @author : Jay Acosta
 */
public class AnimationPanel extends JPanel {
    private List<Image> frames;
    private PagePanel currentPanel;
    private int currentIndex;

    private Dimension preferredSize;

    private JComboBox<Integer> frameIndices;

    private boolean removing;

    public AnimationPanel() {
        frames = new ArrayList<>();
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

    public void addPanel(){

        frameIndices.addItem(frames.size() + 1);

        frames.set(currentIndex, currentPanel.getCurrentImage());
        currentPanel.clearImage();
        frames.add(++currentIndex, currentPanel.getCurrentImage());
    }

    public void removePanel() {

        if(frames.size() > 1){

            removing = true;
            frames.remove(currentIndex);
            frameIndices.removeAllItems(); //problem
            resetIndices();
            if(frames.size() <= currentIndex) {
                currentIndex--;
            }
            System.out.println("Remove Panel Index: " + currentIndex);
            removing = false;
            setCurrentFrame(currentIndex);
        }
    }

    private void resetIndices() {
        for(int i = 0; i < frames.size(); i++) {
            frameIndices.addItem(i + 1);
        }
    }

    public void forwardOneFrame() {
        if(currentIndex < frames.size() - 1) {
            currentPanel.setImage(frames.get(++currentIndex));
        }
        System.out.println("Index: " + currentIndex);
    }

    public void backwardOneFrame() {
        if(currentIndex > 0) {
            currentPanel.setImage(frames.get(--currentIndex));
        }

        System.out.println("Index: " + currentIndex);
    }

    public void setJComboBox(JComboBox<Integer> other) {
        frameIndices = other;
    }

    public void setCurrentFrame(int newIndex) {

        if(!removing) {
            currentIndex = newIndex;
            currentPanel.setImage(frames.get(currentIndex));
        }
    }

    public void erase() {
        currentPanel.erase();
    }

    public void brush() {
        currentPanel.brush();
    }

}
