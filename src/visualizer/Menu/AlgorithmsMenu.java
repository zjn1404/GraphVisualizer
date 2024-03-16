package visualizer.Menu;

import visualizer.Graph.GraphGUI;
import visualizer.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AlgorithmsMenu extends JMenu {

    private enum algorithm {
        DFS(1),
        BFS(2),
        DIJKSTRA(3),
        PRIM(4);
        final int algoID;
        algorithm(int algoID) {
            this.algoID = algoID;
        }
    }
    private JMenuItem DFSItem;
    private JMenuItem BFSItem;
    private JMenuItem DijkstraItem;
    private JMenuItem PrimItem;
    private MainFrame mainFrame;
    private GraphGUI graphGUI;
    public AlgorithmsMenu(MainFrame mainFrame, GraphGUI graphGUI) {
        super("Algorithms");
        this.mainFrame = mainFrame;
        this.graphGUI = graphGUI;
        addItems();
    }
    private void addItems() {
        DFSItem = new JMenuItem("Depth-First Search");
        DFSItem.setName("Depth-First Search");
        BFSItem = new JMenuItem("Breadth-First Search");
        BFSItem.setName("Breadth-First Search");
        DijkstraItem = new JMenuItem("Dijkstra's Algorithm");
        DijkstraItem.setName("Dijkstra's Algorithm");
        PrimItem = new JMenuItem("Prim's Algorithm");
        PrimItem.setName("Prim's Algorithm");

        add(DFSItem);
        add(BFSItem);
        add(DijkstraItem);
        add(PrimItem);

        setAction();
    }

    private void setUp() {
        mainFrame.setModeLabel("Current Mode -> None");
        mainFrame.setVisibleAlgoLabel();
        graphGUI.resetColorItems();
        mainFrame.setAlgoLabel("Please choose a starting vertex");
    }

    private void setDFSItemAction() {
        this.DFSItem.addActionListener(e -> {
            setUp();
            mainFrame.setAlgoID(algorithm.DFS.algoID);
        });
    }

    private void setBFSItemAction() {
        this.BFSItem.addActionListener(e -> {
            setUp();
            mainFrame.setAlgoID(algorithm.BFS.algoID);
        });
    }

    private void setDijkstraItem() {
        this.DijkstraItem.addActionListener(e -> {
            setUp();
            mainFrame.setAlgoID(algorithm.DIJKSTRA.algoID);
        });
    }

    private void setPrimItem() {
        this.PrimItem.addActionListener(e -> {
            setUp();
            mainFrame.setAlgoID(algorithm.PRIM.algoID);
        });
    }

    private void setAction() {
        setDFSItemAction();
        setBFSItemAction();
        setDijkstraItem();
        setPrimItem();
    }
}
