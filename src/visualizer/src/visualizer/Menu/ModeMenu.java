package visualizer.Menu;

import visualizer.Graph.GraphGUI;
import visualizer.MainFrame;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.KeyEvent;

public class ModeMenu extends JMenu {
    private JMenuItem addAVertexItem;
    private JMenuItem addAnEdgeItem;

    private JMenuItem removeAVertexItem;
    private JMenuItem removeAnEdgeItem;
    private JMenuItem noneItem;
    private JLabel label;
    private final MainFrame mainFrame;
    private final GraphGUI graphGUI;
    public ModeMenu(MainFrame mainFrame, GraphGUI graphGUI) {
        super("Mode");
        setName("Mode");
        this.mainFrame = mainFrame;
        this.graphGUI = graphGUI;
        addLabel();
        setMnemonic(KeyEvent.VK_F);
        addItems();
    }
    private void addLabel() {
        label = new JLabel("Current Mode -> Add a Vertex");
        mainFrame.setModeLabel(label.getText());
        label.setVisible(false);
        add(label);
    }
    private void addItems() {
        addAVertexItem = new JMenuItem("Add a Vertex");
        addAVertexItem.setName("Add a Vertex");
        addAnEdgeItem = new JMenuItem("Add an Edge");
        addAnEdgeItem.setName("Add an Edge");
        removeAVertexItem = new JMenuItem("Remove a Vertex");
        removeAVertexItem.setName("Remove a Vertex");
        removeAnEdgeItem = new JMenuItem("Remove an Edge");
        removeAnEdgeItem.setName("Remove an Edge");
        noneItem = new JMenuItem("None");
        noneItem.setName("None");
        add(addAVertexItem);
        add(addAnEdgeItem);
        add(removeAVertexItem);
        add(removeAnEdgeItem);
        add(noneItem);
        setAction();
    }

    private void setAddAVertexItemAction() {
        this.addAVertexItem.addActionListener(e -> {
            this.label.setText("Current Mode -> Add a Vertex");
            mainFrame.setModeLabel(label.getText());
            mainFrame.removeAlgoLabel();
            graphGUI.resetClickedVertex();
            graphGUI.resetColorItems();
        });
    }

    private void setAddAnEdgeItemAction() {
        this.addAnEdgeItem.addActionListener(e -> {
            this.label.setText("Current Mode -> Add an Edge");
            mainFrame.setModeLabel(label.getText());
            mainFrame.removeAlgoLabel();
            graphGUI.resetClickedVertex();
            graphGUI.resetColorItems();
        });
    }
    private void setRemoveVertexAction() {
        this.removeAVertexItem.addActionListener(e -> {
            this.label.setText("Current Mode -> Remove a Vertex");
            mainFrame.setModeLabel(label.getText());
            mainFrame.removeAlgoLabel();
            graphGUI.resetClickedVertex();
            graphGUI.resetColorItems();
        });
    }

    private void setRemoveEdgeAction() {
        this.removeAnEdgeItem.addActionListener(e -> {
            this.label.setText("Current Mode -> Remove an Edge");
            mainFrame.setModeLabel(label.getText());
            mainFrame.removeAlgoLabel();
            graphGUI.resetClickedVertex();
            graphGUI.resetColorItems();
        });
    }
    private void setNoneItemAction() {
        this.noneItem.addActionListener(e -> {
            this.label.setText("Current Mode -> None");
            mainFrame.setModeLabel(label.getText());
            mainFrame.removeAlgoLabel();
            graphGUI.resetClickedVertex();
            graphGUI.resetColorItems();
        });
    }


    private void setAction() {
        this.setAddAVertexItemAction();
        this.setAddAnEdgeItemAction();
        this.setRemoveVertexAction();
        this.setRemoveEdgeAction();
        this.setNoneItemAction();
    }
}
