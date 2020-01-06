import javax.swing.*;
import java.awt.*;

public class JCompGrouper extends JPanel {

    private JPanel innerContainer = new JPanel();

    public JCompGrouper() {
        // set the current layout as a border layout
        // set the inner container as a JPanel with a flow layout
        init(null);

    }

    public JCompGrouper(LayoutManager layout){
        init(layout);
    }

    public void init(LayoutManager layout){
        setLayout(new BorderLayout());
        if (layout != null) {
            innerContainer.setLayout(layout);
        } else {
            innerContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        }
        super.add(innerContainer);
    }

    public JCompGrouper(String label, LayoutManager layout) {

        // call default constructor
        this(layout);

        // create a label for this group
        JLabel groupLabel = new JLabel(label);

        // add the group layout that is center-aligned to the bottom
        groupLabel.setHorizontalAlignment(JLabel.CENTER);
        add(groupLabel, BorderLayout.SOUTH);
    }

    public void setBorder(int top, int left, int bottom, int right){
        this.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.GRAY));
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

    public void add(Component component, GridBagConstraints gbc){
        innerContainer.add(component, gbc);
    }

    public void remove(int currentIndex) {
        if(innerContainer.getComponents().length > 0)
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

    public JPanel getInnerContainer(){
        return innerContainer;
    }

    public int getLength(){
        return innerContainer.getComponents().length;
    }

    public void removeAll(){
        innerContainer.removeAll();
    }

    public Component getComponentAt(int index) {
        return innerContainer.getComponent(index);
    }
}