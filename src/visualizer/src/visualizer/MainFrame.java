package visualizer;

import visualizer.Graph.GraphGUI;
import visualizer.Menu.AlgorithmsMenu;
import visualizer.Menu.FileMenu;
import visualizer.Menu.ModeMenu;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private GraphGUI graphGUI = null;
    private JLabel modeLabel = null;
    private JLabel algoLabel = null;
    private int algoID;
    public MainFrame() {
        super("Graph-Algorithms Visualizer");
        int width = 800;
        int height = 600;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUpModeLabel();
        this.getContentPane().setBackground(Color.BLACK);

        graphGUI = new GraphGUI(this, getWidth(), getHeight());
        add(graphGUI);
        addMenuBar();
        add(modeLabel);
        setAlgoLabel("Please choose a starting vertex");
    }

    public int getModeID() {
        String labelText = modeLabel.getText();
        if (labelText.contains("Add a Vertex")) {
            return 1;
        } else if (labelText.contains("Add an Edge")) {
            return 2;
        } else if (labelText.contains("Remove a Vertex")) {
            return 3;
        } else if (labelText.contains("Remove an Edge")) {
            return 4;
        } else if (labelText.contains("None")) {
            return 5;
        }
        return 0;
    }
    public void setModeLabel(String text) {
        modeLabel.setText(text);
    }
    public void setAlgoID(int id) {
        this.algoID = id;
    }
    public int getAlgoID() {
        return this.algoID;
    }
    private void setUpModeLabel() {
        modeLabel = new JLabel();
        modeLabel.setName("Mode");
        modeLabel.setOpaque(false);
        Font font = new Font("Arial", Font.PLAIN, 15);
        modeLabel.setFont(font);
        modeLabel.setForeground(Color.WHITE);
        modeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        modeLabel.setVerticalAlignment(SwingConstants.TOP);
    }
    public void setAlgoLabel(String text) {
        if (algoLabel == null) {
            algoLabel = new JLabel(text, JLabel.CENTER);
            algoLabel.setBackground(Color.WHITE);
            algoLabel.setName("Display");
            algoLabel.setVisible(false);
            algoLabel.setOpaque(true);
            add(BorderLayout.SOUTH, algoLabel);
        }

        algoLabel.setText(text);
    }

    public void setVisibleAlgoLabel() {
        algoLabel.setVisible(true);
    }
    public void removeAlgoLabel() {
        if (algoLabel != null) {
            algoLabel.setText("Please choose a starting vertex");
            algoLabel.setVisible(false);
        }
    }
    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        ModeMenu modeMenu = new ModeMenu(this, graphGUI);
        FileMenu fileMenu = new FileMenu(this, graphGUI);
        AlgorithmsMenu algorithmsMenu = new AlgorithmsMenu(this, graphGUI);
        menuBar.add(modeMenu);
        menuBar.add(fileMenu);
        menuBar.add(algorithmsMenu);
    }
}