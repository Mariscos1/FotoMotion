// imports

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

/**
 * Handles User-Interface Interaction by using the Graphics
 * class and JPanel
 *
 * @author : Jay Acosta
 */
public class PagePanel extends JPanel implements MouseListener, MouseMotionListener{

    public PagePanel() {
        setBackground(Color.WHITE);
        setFocusable(true);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Click!");
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
