// imports

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

/**
 * Handles User-Interface Interaction by using the Graphics
 * class and JPanel
 *
 * @author : Jay Acosta
 */
public class PagePanel extends JPanel implements MouseListener, MouseMotionListener{

    private final int DEFAULT_STROKE_SIZE = 10;
    private final Color DEFAULT_COLOR = Color.BLACK;
    private Graphics2D g2;

    private Color color;
    private int strokeSize;
    Point beginPoint;
    Point endPoint;

    public PagePanel(int strokeSize, Color color){
        this();
        this.strokeSize = strokeSize;
        this.color = color;
    }

    public PagePanel() {
        setBackground(Color.WHITE);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);

        this.strokeSize = DEFAULT_STROKE_SIZE;
        this.color = DEFAULT_COLOR;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Click!");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //gets begin point for mouseDragged shape
        beginPoint = e.getPoint();

        //places a dot wherever clicked
        g2 = (Graphics2D)getGraphics();
        g2.setColor(color);
        g2.fillOval(e.getX(), e.getY(), strokeSize, strokeSize);
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
        //sets up graphics2d object
        g2 = (Graphics2D)getGraphics();
        g2.setStroke(new BasicStroke(strokeSize));
        g2.setColor(color);

        //draws the line
        endPoint = e.getPoint();
        Shape line = new Line2D.Double(beginPoint, endPoint);
        g2.draw(line);
        beginPoint = endPoint;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
