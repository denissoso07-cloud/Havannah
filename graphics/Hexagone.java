import java.awt.Graphics2D;

public class Hexagone implements Shape {
    private Line line1;
    private Line line2;
    private Line line3;
    private Line line4;
    private Line line5;
    private Line line6;
    private Color color = Color.BLACK;
    private double centerX;
    private double centerY;
    private double radius;

    /**
     * Constructs a hexagon with center and radius
     * @param centerX the x-coordinate of the center
     * @param centerY the y-coordinate of the center
     * @param radius the radius of the hexagon
     */
    public Hexagone(double centerX, double centerY, double radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        createLines();
    }

    /**
     * Creates the 6 lines that form the hexagon
     */
    private void createLines() {
        // Calculate the 6 vertices of the hexagon
        double[] xCoords = new double[6];
        double[] yCoords = new double[6];

        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3 * i; // 60 degrees between each vertex
            xCoords[i] = centerX + radius * Math.cos(angle);
            yCoords[i] = centerY + radius * Math.sin(angle);
        }

        // Create the 6 lines connecting the vertices
        line1 = new Line(xCoords[0], yCoords[0], xCoords[1], yCoords[1]);
        line2 = new Line(xCoords[1], yCoords[1], xCoords[2], yCoords[2]);
        line3 = new Line(xCoords[2], yCoords[2], xCoords[3], yCoords[3]);
        line4 = new Line(xCoords[3], yCoords[3], xCoords[4], yCoords[4]);
        line5 = new Line(xCoords[4], yCoords[4], xCoords[5], yCoords[5]);
        line6 = new Line(xCoords[5], yCoords[5], xCoords[0], yCoords[0]);
    }

    /**
     * Gets the leftmost x-position of the hexagon
     * @return the leftmost x-position
     */
    public int getX() {
        return (int) Math.round(centerX - radius);
    }

    /**
     * Gets the topmost y-position of the hexagon
     * @return the topmost y-position
     */
    public int getY() {
        return (int) Math.round(centerY - radius);
    }

    /**
     * Gets the width of the hexagon
     * @return the width
     */
    public int getWidth() {
        return (int) Math.round(radius * 2);
    }

    /**
     * Gets the height of the hexagon
     * @return the height
     */
    public int getHeight() {
        return (int) Math.round(radius * 2);
    }

    /**
     * Paints the hexagon by drawing all 6 lines
     * @param g2 the graphics object
     */
    public void paintShape(Graphics2D g2) {
        line1.paintShape(g2);
        line2.paintShape(g2);
        line3.paintShape(g2);
        line4.paintShape(g2);
        line5.paintShape(g2);
        line6.paintShape(g2);
    }

    /**
     * Sets the color of the hexagon
     * @param c the color
     */
    public void setColor(Color c) {
        color = c;
        line1.setColor(c);
        line2.setColor(c);
        line3.setColor(c);
        line4.setColor(c);
        line5.setColor(c);
        line6.setColor(c);
    }

    /**
     * Gets the color of the hexagon
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Displays the hexagon on the canvas
     */
    public void draw() {
        Canvas.getInstance().show(this);
    }
}
