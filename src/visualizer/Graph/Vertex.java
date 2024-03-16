package visualizer.Graph;

import javax.swing.*;
import java.awt.*;

public class Vertex extends JPanel {
    private String id;
    private int rectX;
    private int rectY;
    boolean doesRepaint;
    public Vertex(String id, int rectX, int rectY, int width, int height) {
        this.id = id;
        this.rectX = rectX;
        this.rectY = rectY;
        this.doesRepaint = false;
        setName(String.format("Vertex %s", id));
        setBounds(rectX, rectY, width, height);
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);
        setLayout(new BorderLayout());
        JLabel vertexLabel = new JLabel(id, JLabel.CENTER);
        vertexLabel.setName(String.format("VertexLabel %s", id));
        add(vertexLabel);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRectX(int rectX) {
        this.rectX = rectX;
    }

    public void setRectY(int rectY) {
        this.rectY = rectY;
    }

    public String getId() {
        return this.id;
    }

    public int getRectX() {
        return this.rectX;
    }

    public int getRectY() {
        return this.rectY;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(doesRepaint) {
            g.setColor(Color.YELLOW);
            g.fillOval(0, 0, getWidth(), getHeight());
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, getWidth(), getHeight());
        }
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
