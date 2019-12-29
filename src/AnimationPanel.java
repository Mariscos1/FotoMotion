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
    private List<PagePanel> frame;
    private int currentIndex;

    private Dimension preferredSize;

    public AnimationPanel() {
        frame = new ArrayList<>();
    }

    public AnimationPanel(int width, int height) {
        this();
        setBackground(Color.BLACK);
        preferredSize = new Dimension(width, height);
        setPreferredSize(preferredSize);

        addBlankPanel();
    }

    public void addBlankPanel() {

    }

    public void forwardOneFrame() {

    }

    public void backwardOneFrame() {

    }
}
