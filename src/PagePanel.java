// imports

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

/**
 * Handles User-Interface Interaction by using the Graphics
 * class and JPanel
 *
 * @author : Jay Acosta, Janlloyd Carangan
 */
public class PagePanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int DEFAULT_STROKE_SIZE = 4;
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private Graphics2D g2BackBuffer;

    public Color backgroundColor;
    public static Color color;
    public static int strokeSize;

    private int oldX, oldY, currentX, currentY;

    private Image backBuffer;

    public PagePanel(int strokeSize, Color color, Color backgroundColor) {
        this();
        this.strokeSize = strokeSize;
        this.color = color;
        this.backgroundColor = backgroundColor;
    }

    public PagePanel() {
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);

        this.strokeSize = DEFAULT_STROKE_SIZE;
        this.color = DEFAULT_COLOR;
        this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
    }

    public void init(){
        backBuffer = createVolatileImage(getWidth(), getHeight());
        g2BackBuffer = (Graphics2D)backBuffer.getGraphics();

        g2BackBuffer.setColor(backgroundColor);
        g2BackBuffer.fillRect(0,0,getWidth(),getHeight());
    }

    //CAUTION
    public void paintComponent(Graphics g){
        super.paintComponent(g); //erases the panel
        Graphics2D g2 = (Graphics2D)g;
        if(backBuffer != null){
            g2.drawImage(backBuffer, 0, 0, Color.WHITE, this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Click!");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //gets begin point for mouseDragged shape
        oldX = e.getX();
        oldY = e.getY();

        //places a dot wherever clicked
        g2BackBuffer.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2BackBuffer.setColor(color);
        g2BackBuffer.fillOval(oldX - strokeSize/2, oldY - strokeSize/2, strokeSize, strokeSize);
        repaint();
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

        // g2.setStroke(new BasicStroke(strokeSize));
        g2BackBuffer.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2BackBuffer.setColor(color);
        g2BackBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //draws lines with coordinates
        currentX = e.getX();
        currentY = e.getY();
        g2BackBuffer.drawLine(oldX, oldY, currentX, currentY);
        oldX = currentX;
        oldY = currentY;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public Image getCurrentImage() {
        return backBuffer;
    }

    public void clearImage() {
        init();
        repaint();
    }

    public void setImage(Image image) {
        clearImage();
        backBuffer = image;
        repaint();
    }

    public void undo() {

    }

    public void redo() {

    }

    public void changeBrushSize() {

    }

    public void changeBrushColor() {

    }
}
