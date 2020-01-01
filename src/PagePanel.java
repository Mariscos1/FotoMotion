// imports

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

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
    private boolean showShadow;

    public Color backgroundColor;
    public static Color color;
    public static Color brushColor;
    public static int strokeSize;

    private int oldX, oldY, currentX, currentY;

    private Image backBuffer;
    private Image prevImage;

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
        this.brushColor = color;
    }

    public void init() {
        backBuffer = createVolatileImage(getWidth(), getHeight());
        g2BackBuffer = (Graphics2D) backBuffer.getGraphics();

        g2BackBuffer.setColor(DEFAULT_BACKGROUND_COLOR);
        g2BackBuffer.fillRect(0, 0, getWidth(), getHeight());
    }

    //CAUTION
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //erases the panel
        Graphics2D g2 = (Graphics2D) g;

        if (backBuffer != null) {
            g2.drawImage(backBuffer, 0, 0, null);
        }

        if (showShadow && prevImage != null) {
            drawTranslucentImage(g2, ((VolatileImage)prevImage).getSnapshot());
        }
    }

    public void showShadow(boolean showShadow) {
        this.showShadow = showShadow;
    }

    private void drawTranslucentImage(Graphics2D graphics, BufferedImage image) {

        // loop through all pixels in the buffered image
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {

                // if the current part of the image is not the background color
                if(image.getRGB(x, y) != backgroundColor.getRGB()) {

                    // set the graphics color to the RGB value
                    graphics.setColor(new Color(image.getRGB(x, y)));

                    // set the alpha to .1 or 10%
                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f));

                    // draw the pixel as a 1 x 1 filled rectangle
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //gets begin point for mouseDragged shape
        oldX = e.getX();
        oldY = e.getY();

        //places a dot wherever clicked
        g2BackBuffer.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2BackBuffer.setColor(color);
        g2BackBuffer.fillOval(oldX - strokeSize / 2, oldY - strokeSize / 2, strokeSize, strokeSize);
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

    public void setPrevImage(Image newImage) {
        prevImage = newImage;
        repaint();
    }

    public void removePrevImage(){
        prevImage = null;
        repaint();
    }

    public void clearImage() {
        init();
        repaint();
    }

    public void setImage(Image image) {
        backBuffer = image;
        g2BackBuffer = (Graphics2D) backBuffer.getGraphics();
        repaint();
    }

    public void erase() {
        color = backgroundColor;
    }

    public void brush() {
        color = brushColor;
    }

    public void deletePage() {
        g2BackBuffer.setColor(backgroundColor);
        g2BackBuffer.fillRect(0, 0, getWidth(), getHeight());
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
