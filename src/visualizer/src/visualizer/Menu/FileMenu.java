package visualizer.Menu;

import visualizer.Graph.GraphGUI;
import visualizer.MainFrame;

import javax.swing.*;

public class FileMenu extends JMenu {
    private JMenuItem newItem;
    private JMenuItem exitItem;
    private GraphGUI graph;
    private MainFrame mainFrame;

    public FileMenu(MainFrame mainFrame, GraphGUI graph) {
        super("File");
        setName("File");
        this.graph = graph;
        this.mainFrame = mainFrame;
        addItems();
    }

    private void addItems() {
        newItem = new JMenuItem("New");
        newItem.setName("New");
        exitItem = new JMenuItem("Exit");
        exitItem.setName("Exit");

        add(newItem);
        add(exitItem);

        setAction();
    }

    private void setNewItemAction() {
        this.newItem.addActionListener(e -> {
            graph.reset();
            mainFrame.removeAlgoLabel();
        });
    }

    private void setExitItemAction() {
        this.exitItem.addActionListener(e -> {
            System.exit(0);
        });
    }

    private void setAction() {
        setNewItemAction();
        setExitItemAction();
    }
}
