// imports

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Handles User-Interface Interaction by using the Graphics
 * class and JPanel
 *
 * @author : Jay Acosta, Janlloyd Carangan
 */
public class PagePanel extends JPanel implements MouseListener, MouseMotionListener {

    // class constants
    private final int DEFAULT_STROKE_SIZE = 16;
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

    // undo/redo
    private Deque<Image> undoStack;
    private Deque<Image> redoStack;

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

        undoStack = new LinkedList<>();
        redoStack = new LinkedList<>();

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

        //init the stack
        if(undoStack.isEmpty())
            undoStack.push(deepCopy(backBuffer));
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
            g2.drawImage(prevImage, 0, 0, getWidth(), getHeight(), null);
            //drawTranslucentImage(g2, ((VolatileImage) prevImage).getSnapshot());
        }
    }

    // draws a translucent version of "image" onto the graphics object
    private BufferedImage makeTranslucentImage(Image newImage, float transparency) {

        BufferedImage image;
        if(newImage instanceof VolatileImage) {
            image = ((VolatileImage) newImage).getSnapshot();
        } else if (newImage instanceof BufferedImage) {
            image = (BufferedImage) newImage;
        } else {
            throw new IllegalArgumentException("Fuck");
        }

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // loop through all pixels in the buffered image
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                // if the current part of the image is not the background color...
                if (image.getRGB(x, y) != backgroundColor.getRGB()) {

                    Color oldColor = new Color(image.getRGB(x, y));
                    Color newColor = new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(),(int)(transparency*255));

                    result.setRGB(x, y, newColor.getRGB());

//                    // ... set the graphics color to the RGB value
//                    result.setColor(new Color(image.getRGB(x, y)));
//
//                    // set the alpha
//                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, SHADOW_TRANSPARENCY));
//
//                    // draw the pixel as a 1 x 1 filled rectangle
//                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }

        return result;
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

    @Override
    public void mouseReleased(MouseEvent e) {

        Image copy = deepCopy(backBuffer);
        undoStack.push(copy);

        if(!redoStack.isEmpty())
            redoStack.clear();

        System.out.println("printing");
        printStacks();
    }

    public void clearPage() {
        g2BackBuffer.setColor(backgroundColor);
        g2BackBuffer.fillRect(0, 0, getWidth(), getHeight());
        repaint();
    }

    public void clearImage() {
        clearStacks();
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

    public void setUndoStack(Deque<Image> undoStack) {
        this.undoStack = undoStack;
    }

    public Deque<Image> getUndoStack() {
        return deepCopy(undoStack);
    }

    public void setRedoStack(Deque<Image> redoStack){
        this.redoStack = redoStack;
    }

    public Deque<Image> deepCopy(Deque<Image> other) {
        Deque<Image> copy = new LinkedList<>();

        for (Image image : other) {
            copy.addLast(deepCopy(image));
        }

        return copy;
    }

    public Deque<Image> getRedoStack(){
        return deepCopy(redoStack);
    }

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

    public void setImage(Image image, boolean deepCopy) {
        backBuffer = deepCopy? deepCopy(image): image;
        g2BackBuffer = (Graphics2D) backBuffer.getGraphics();
        repaint();
    }

    public void setPrevImage(Image newImage) {

        prevImage = makeTranslucentImage(newImage, SHADOW_TRANSPARENCY);
        repaint();
    }

    public void showShadow(boolean showShadow) {
        this.showShadow = showShadow;
    }

    /*
     * Methods to implement
     */

    public Image undo() {

        if(undoStack.size() > 1) {
            redoStack.push(undoStack.pop());
        }
        System.out.println("undo");
        printStacks();
        return undoStack.peek();
    }

    public Image redo() {

        if (redoStack.size() > 0) {
            undoStack.push(redoStack.pop());
            System.out.println("redo");
            printStacks();
            return undoStack.peek();
        }
        return null;
    }

    private void printStacks(){
        System.out.println("================================");
        System.out.println("Undo: "+ undoStack);
        System.out.println("Redo: "+ redoStack);
    }

    public int getUndosLeft() {
        return undoStack.size();
    }

    public void setFirstStack(Image img){
        if (!undoStack.isEmpty())
            undoStack.pop();
        undoStack.push(img);
    }


    public void clearStacks() {

        while(undoStack.size() > 1) {
            undoStack.pop();
        }

        while(redoStack.size() > 1) {
            redoStack.pop();
        }
    }



    private Image deepCopy(Image image) {

        Image result = createVolatileImage(getWidth(), getHeight());
        Graphics graphics = result.getGraphics();
        graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        return result;
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
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
