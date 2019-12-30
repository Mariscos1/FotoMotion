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

    public AnimationPanel() {
        frames = new ArrayList<>();
        currentIndex = -1;
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
    }

    public void addNewPanel(){
        frames.add(currentPanel.getCurrentImage());
        currentPanel.clearImage();
        currentIndex++;
    }

    public void forwardOneFrame() {
        if(currentIndex < frames.size() - 1) {
            currentPanel.setImage(frames.get(++currentIndex));
        }
    }

    public void backwardOneFrame() {
        if(currentIndex > 0) {
            currentPanel.setImage(frames.get(--currentIndex));
        }
    }
}
