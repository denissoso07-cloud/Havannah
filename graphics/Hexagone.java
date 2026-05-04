

import java.awt.Graphics2D;
import java.awt.Polygon;

public class Hexagone implements Shape {
    private Line line1, line2, line3, line4, line5, line6;
    private Color color = Color.BLACK;
    private boolean filled = false;  // ← AJOUT
    private double centerX, centerY, radius;

    public Hexagone(double centerX, double centerY, double radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        createLines();
    }

    private void createLines() {
        double[] xCoords = new double[6];
        double[] yCoords = new double[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3 * i;
            xCoords[i] = centerX + radius * Math.cos(angle);
            yCoords[i] = centerY + radius * Math.sin(angle);
        }
        line1 = new Line(xCoords[0], yCoords[0], xCoords[1], yCoords[1]);
        line2 = new Line(xCoords[1], yCoords[1], xCoords[2], yCoords[2]);
        line3 = new Line(xCoords[2], yCoords[2], xCoords[3], yCoords[3]);
        line4 = new Line(xCoords[3], yCoords[3], xCoords[4], yCoords[4]);
        line5 = new Line(xCoords[4], yCoords[4], xCoords[5], yCoords[5]);
        line6 = new Line(xCoords[5], yCoords[5], xCoords[0], yCoords[0]);
    }

    public int getX() { return (int) Math.round(centerX - radius); }
    public int getY() { return (int) Math.round(centerY - radius); }
    public int getWidth() { return (int) Math.round(radius * 2); }
    public int getHeight() { return (int) Math.round(radius * 2); }

    /**
     * Affiche le contour de l'hexagone (comme draw() dans Ellipse)
     */
    public void draw() {
        filled = false;  // ← contour seulement
        Canvas.getInstance().show(this);
    }

    /**
     * Remplit l'hexagone avec sa couleur (comme fill() dans Ellipse)
     */
    public void fill() {
        filled = true;   // ← remplissage
        Canvas.getInstance().show(this);
    }

    public void setColor(Color c) {
        color = c;
        line1.setColor(c); line2.setColor(c); line3.setColor(c);
        line4.setColor(c); line5.setColor(c); line6.setColor(c);
        Canvas.getInstance().repaint();
    }

    public Color getColor() { return color; }

    public void paintShape(Graphics2D g2) {
        int[] xs = new int[6];
        int[] ys = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3 * i;
            xs[i] = (int) Math.round(centerX + radius * Math.cos(angle));
            ys[i] = (int) Math.round(centerY + radius * Math.sin(angle));
        }
        Polygon p = new Polygon(xs, ys, 6);
        g2.setColor(new java.awt.Color(
            (int) color.getRed(),
            (int) color.getGreen(),
            (int) color.getBlue()
        ));
        if (filled) {
            g2.fill(p);   // ← rempli
        } else {
            g2.draw(p);   // ← contour
        }
    }
}