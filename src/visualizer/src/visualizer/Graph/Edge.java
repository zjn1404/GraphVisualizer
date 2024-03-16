package visualizer.Graph;

import javax.swing.*;
import java.awt.*;

public class Edge extends JComponent {

    private final double MAXVALIDDISTANCE = 10;
    private String id;
    private int[][] rects;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private boolean doesRepaint;

    private String startVertex;
    private String endVertex;


    public Edge(String idV1, String idV2, int rectXV1, int rectYV1, int rectXV2, int rectYV2) {
        this.id = String.format("<%s -> %s>", idV1, idV2);
        this.startVertex = idV1;
        this.endVertex = idV2;
        setName(String.format("Edge %s", id));
        setBackground(Color.BLACK);
        setOpaque(false);
        setRects(rectXV1, rectYV1, rectXV2, rectYV2);
        calculateBounds();
        setBounds(minX, minY, maxX - minX, maxY - minY);
        x1 = rects[0][0] - minX;
        y1 = rects[0][1] - minY;
        x2 = rects[1][0] - minX;
        y2 = rects[1][1] - minY;
        setLayout(null);
    }

    private void setRects(int rectXV1, int rectYV1, int rectXV2, int rectYV2) {
        this.rects = new int[2][2];
        rects[0][0] = rectXV1;
        rects[0][1] = rectYV1;
        rects[1][0] = rectXV2;
        rects[1][1] = rectYV2;
    }

    private void calculateBounds() {
        minX = Math.min(rects[0][0], rects[1][0]);
        minY = Math.min(rects[0][1], rects[1][1]);
        maxX = Math.max(rects[0][0], rects[1][0]);
        maxY = Math.max(rects[0][1], rects[1][1]);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D)g;
        setForeground(Color.WHITE);
        g2D.setStroke(new BasicStroke(5.0f));
        g2D.setColor(Color.WHITE);
        g2D.drawLine(x1, y1, x2, y2);

        if (doesRepaint) {
            setForeground(Color.YELLOW);
            g2D.setStroke(new BasicStroke(5.0f));
            g2D.setColor(Color.YELLOW);
            g2D.drawLine(x1, y1, x2, y2);
        }
    }

    public String getId() {
        return this.id;
    }

    public String getStartVertex(){
        return this.startVertex;
    }

    public String getEndVertex() {
        return this.endVertex;
    }

    private double calculateDistance(int pX, int pY) {
        double A = rects[1][1] - rects[0][1];
        double B = rects[0][0] - rects[1][0];
        double C = -A * rects[0][0] -B * rects[0][1];
        return Math.abs(A * pX + B * pY + C) / Math.sqrt(A * A + B * B);
    }
    public boolean canBeCleared(int targetX, int targetY) {
        double distance = calculateDistance(targetX, targetY);
        return distance <= MAXVALIDDISTANCE;
    }

    public void setDoesRepaint() {
        this.doesRepaint = true;
        repaint();
    }

    public void resetDoesRepaint(){
        this.doesRepaint = false;
        repaint();
    }
    public void clearPaint() {
        setBounds(0, 0, 0, 0);
        repaint();
    }
}
