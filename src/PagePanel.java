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

    // class constants
    private final int DEFAULT_STROKE_SIZE = 4;
    private final Color DEFAULT_COLOR = Color.BLACK;
    private final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private final float SHADOW_TRANSPARENCY = .25f; // should be from [0, 1] where 0 is opaque

    // instance variables

    // brush and colors
    private Color backgroundColor, brushColor, currentColor;
    private int strokeSize;

    // coordinates of points for use in drawing
    private int oldX, oldY, currentX, currentY;

    // show shadow of previous slide
    private boolean showShadow;

    // buffers
    private Graphics2D g2BackBuffer;
    private Image backBuffer;
    private Image prevImage;

    // not using right now, maybe later
//    public PagePanel(int strokeSize, Color color, Color backgroundColor) {
//        this();
//        this.strokeSize = strokeSize;
//        this.currentColor = color;
//        this.backgroundColor = backgroundColor;
//    }

    // pre: none
    // post: construct a page panel that will handle drawing
    public PagePanel() {

        // set the current panel as focusable and inherit the mouse methods (MouseListener and MouseMotionListener)
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);

        // set the class defaults
        this.strokeSize = DEFAULT_STROKE_SIZE;
        this.currentColor = DEFAULT_COLOR;
        this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
        this.brushColor = currentColor;
    }

    // pre: none
    // post: set the properties of the drawing panel that could not be done in the constructor
    public void init() {

        // create buffer using a volatile image (lossless image)
        backBuffer = createVolatileImage(getWidth(), getHeight());

        // get the graphics of this backBuffer image
        g2BackBuffer = (Graphics2D) backBuffer.getGraphics();

        // draw a rectangle with the background color to "erase" the image
        g2BackBuffer.setColor(DEFAULT_BACKGROUND_COLOR);
        g2BackBuffer.fillRect(0, 0, getWidth(), getHeight());
    }

    /*
     * Painting Methods
     */

    //CAUTION
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //erases the panel

        Graphics2D g2 = (Graphics2D) g; // cast graphics to Graphics2D

        if (backBuffer != null) {

            // draw buffer if buffer != null
            g2.drawImage(backBuffer, 0, 0, null);
        }

        if (showShadow && prevImage != null) {
            drawTranslucentImage(g2, ((VolatileImage) prevImage).getSnapshot());
        }
    }

    // draws a translucent version of "image" onto the graphics object
    private void drawTranslucentImage(Graphics2D graphics, BufferedImage image) {

        // loop through all pixels in the buffered image
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                // if the current part of the image is not the background color...
                if (image.getRGB(x, y) != backgroundColor.getRGB()) {

                    // ... set the graphics color to the RGB value
                    graphics.setColor(new Color(image.getRGB(x, y)));

                    // set the alpha
                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, SHADOW_TRANSPARENCY));

                    // draw the pixel as a 1 x 1 filled rectangle
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //gets begin point for mouseDragged shape
        oldX = e.getX();
        oldY = e.getY();

        //places a dot wherever clicked
        g2BackBuffer.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2BackBuffer.setColor(currentColor);
        g2BackBuffer.fillOval(oldX - strokeSize / 2, oldY - strokeSize / 2, strokeSize, strokeSize);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        // g2.setStroke(new BasicStroke(strokeSize));
        g2BackBuffer.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2BackBuffer.setColor(currentColor);
        g2BackBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //draws lines with coordinates
        currentX = e.getX();
        currentY = e.getY();
        g2BackBuffer.drawLine(oldX, oldY, currentX, currentY);
        oldX = currentX;
        oldY = currentY;
        repaint();
    }

    public void clearPage() {
        g2BackBuffer.setColor(backgroundColor);
        g2BackBuffer.fillRect(0, 0, getWidth(), getHeight());
        repaint();
    }

    public void clearImage() {
        init();
        repaint();
    }

    public void removePrevImage() {
        prevImage = null;
        repaint();
    }

    /*
     * Properties
     */

    public void setEraseMode() {
        currentColor = backgroundColor;
    }

    public void setPaintMode() {
        currentColor = brushColor;
    }

    /*
     * Getters and setters
     */

    // pre: none (newColor != null?)
    // post: sets the current brush color to the new color
    public void setBrushColor(Color newColor) {
        currentColor = newColor;
        brushColor = newColor;
    }

    // pre: newSize > 0
    // post: sets the size of the current brush
    public void setBrushSize(int newSize) {

        // check preconditions
        if (newSize <= 0)
            throw new IllegalArgumentException("invalid size selected: " + newSize);

        strokeSize = newSize;
    }

    public Image getCurrentImage() {
        return backBuffer;
    }

    public void setImage(Image image) {
        backBuffer = image;
        g2BackBuffer = (Graphics2D) backBuffer.getGraphics();
        repaint();
    }

    public void setPrevImage(Image newImage) {
        prevImage = newImage;
        repaint();
    }

    public void showShadow(boolean showShadow) {
        this.showShadow = showShadow;
    }

    /*
     * Methods to implement
     */

    public void undo() {

    }

    public void redo() {

    }

    /*
     * Unimplemented methods
     */

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
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
}
