import javax.swing.*;
import java.awt.*;

public class JCompGrouper extends JPanel {

    private JPanel innerContainer = new JPanel();

    private JCompGrouper() {
        // set the current layout as a border layout
        setLayout(new BorderLayout());

        // set the inner container as a JPanel with a flow layout
        innerContainer.setLayout(new FlowLayout());
        super.add(innerContainer);

        this.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));
    }

    public JCompGrouper(String label) {

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