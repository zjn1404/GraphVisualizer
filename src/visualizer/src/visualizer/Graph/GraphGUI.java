package visualizer.Graph;

import visualizer.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class GraphGUI extends JPanel {
    private final Map<String, Vertex> vertices;
    private final Set<Edge> edges;
    private final Map<String, JLabel> edgeLabels;
    final int VERTEXSIZE = 50;
    private final MainFrame mainFrame;
    private int weight = -1;
    private Vertex clickedVertex1;
    private Vertex clickedVertex2;
    private enum mode {
        ADDVERTEX(1),
        ADDEDGE(2),
        REMOVEVERTEX(3),
        REMOVEEDGE(4),
        None(5);
        final int modeID;
        mode(int modeID) {
            this.modeID = modeID;
        }
    }

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

    public GraphGUI(MainFrame mainFrame, int width, int height) {
        setOpaque(false);
        setSize(width, height);
        setBackground(Color.BLACK);
        setName("Graph");
        setLayout(null); // No layout manager

        this.mainFrame = mainFrame;
        this.vertices = new HashMap<>();
        this.edges = new LinkedHashSet<>();
        this.edgeLabels = new HashMap<>();

        addMouseListener(customMouseListener(this));
        clickedVertex1 = null;
        clickedVertex2 = null;
    }
    public void resetClickedVertex() {
        if (clickedVertex1 != null) {
            clickedVertex1.resetDoesRepaint();
            clickedVertex1 = null;
        }
        if (clickedVertex2 != null) {
            clickedVertex2.resetDoesRepaint();
            clickedVertex2 = null;
        }
    }
    private MouseListener customMouseListener(GraphGUI graphGUI) {
        return new MouseListener() {
            private String getInputFromOptionPane(String message, String title) {
                return JOptionPane.showInputDialog(graphGUI,
                        message,
                        title,
                        JOptionPane.OK_CANCEL_OPTION);
            }

            private void handleAddVertex(MouseEvent mouseEvent) {
                int rectX = mouseEvent.getX() - VERTEXSIZE/2;
                int rectY = mouseEvent.getY() - VERTEXSIZE/2;
                Vertex vertexTemp = new Vertex("temp", rectX, rectY, VERTEXSIZE, VERTEXSIZE);
                if(isOverlapVertex(vertexTemp)) return;

                String input = getInputFromOptionPane("Enter the Vertex ID (Should be 1 char):","Vertex");
                if (input == null) return;
                while (input.length() != 1 || input.isBlank()) {
                    input = getInputFromOptionPane("Enter the Vertex ID (Should be 1 char):", "Vertex");
                }

                if(vertices.containsKey(input)) return;
                mainFrame.setVisible(false);
                addVertex(input, rectX, rectY);
                mainFrame.setVisible(true);
            }

            private String getEdgeWeight() {
                String input = getInputFromOptionPane("Enter Weight", "Input");
                while (input != null && (!input.matches("-?\\d+") || input.isBlank())) {
                    input = getInputFromOptionPane("Enter Weight", "Input");
                }
                return input;
            }

            private Vertex getClickedVertex(MouseEvent mouseEvent) {
                for (Vertex vertex : vertices.values()) {
                    Rectangle bounds = vertex.getBounds();
                    if (bounds.contains(mouseEvent.getPoint())) {
                        return vertex;
                    }
                }
                return null;
            }
            private void addEdgeLabel(String idV1, String idV2, int rectXV1, int rectYV1, int rectXV2, int rectYV2, int weight) {
                JLabel edgeLabel = new JLabel(Integer.toString(weight), JLabel.CENTER);
                edgeLabel.setBounds((rectXV1 + rectXV2)/2, (rectYV1 + rectYV2)/2 - 10, VERTEXSIZE/2, VERTEXSIZE/2);
                String id = String.format("<%s -> %s>", idV1, idV2);
                System.out.println(id);
                edgeLabel.setName(String.format("EdgeLabel %s", id));
                edgeLabel.setBackground(new Color(0, 0, 0, 0));
                edgeLabel.setForeground(Color.WHITE);
                add(edgeLabel);
                edgeLabels.put(id, edgeLabel);
            }

            private void handleAddEdge(MouseEvent mouseEvent) {

                if (clickedVertex1 == null) {
                    clickedVertex1 = getClickedVertex(mouseEvent);
                    if (clickedVertex1 != null) {
                        clickedVertex1.setDoesRepaint();
                    }
                } else if (clickedVertex2 == null) {
                    clickedVertex2 = getClickedVertex(mouseEvent);
                    if (clickedVertex2 != null) {
                        clickedVertex2.setDoesRepaint();
                        String weightBuffer = getEdgeWeight();
                        if (weightBuffer == null) {
                            resetClickedVertex();
                            return;
                        }
                        weight = Integer.parseInt(weightBuffer);
//                        if (clickedVertex1.getId().compareTo(clickedVertex2.getId()) > 0) {
//                            Vertex tmp = clickedVertex1;
//                            clickedVertex1 = clickedVertex2;
//                            clickedVertex2 = tmp;
//                        }
                        setVisible(false);
                        addEdge(clickedVertex1.getId(), clickedVertex2.getId(), clickedVertex1.getRectX(), clickedVertex1.getRectY(), clickedVertex2.getRectX(), clickedVertex2.getRectY());
                        addEdgeLabel(clickedVertex1.getId(), clickedVertex2.getId(), clickedVertex1.getRectX(), clickedVertex1.getRectY(), clickedVertex2.getRectX(), clickedVertex2.getRectY(), weight);
                        addEdge(clickedVertex2.getId(), clickedVertex1.getId(), clickedVertex1.getRectX(), clickedVertex1.getRectY(), clickedVertex2.getRectX(), clickedVertex2.getRectY());
                        setVisible(true);

                        resetClickedVertex();
                    }
                }
            }

            private void removeEdgeByVertex(String vertexId) {
                List<Edge> removedEdge = new ArrayList<>();
                edges.forEach(edge -> {
                    if (edge.getId().contains(vertexId)) {
                        JLabel label = edgeLabels.get(edge.getId());
                        if(label != null) {
                            remove(label);
                            edgeLabels.remove(edge.getId());
                        }
                        remove(edge);
                        removedEdge.add(edge);
                    }
                });
                for (Edge edge : removedEdge) {
                    mainFrame.setVisible(false);
                    edge.clearPaint();
                    mainFrame.setVisible(true);
                    edges.remove(edge);
                }
            }

            private void handleRemoveVertex(MouseEvent mouseEvent) {
                List<Vertex> removedVertexes = new ArrayList<>();

                for (Vertex vertex : vertices.values()) {
                    Rectangle bounds = vertex.getBounds();
                    if (bounds.contains(mouseEvent.getPoint())) {
                        removeEdgeByVertex(vertex.getId());
                        remove(vertex);
                        removedVertexes.add(vertex);
                    }
                }

                for (Vertex vertex : removedVertexes) {
                    mainFrame.setVisible(false);
                    vertex.clearPaint();
                    mainFrame.setVisible(true);

                    vertices.remove(vertex.getId());
                }
            }

            private void handleRemoveEdge(MouseEvent mouseEvent) {
                List<Edge> removedEdge = new ArrayList<>();
                edges.forEach(edge -> {
                    Rectangle bounds = edge.getBounds();
                    if (bounds.contains(mouseEvent.getPoint()) && edge.canBeCleared(mouseEvent.getX(), mouseEvent.getY())) {
                        JLabel label = edgeLabels.get(edge.getId());
                        if(label != null) {
                            edgeLabels.remove(edge.getId());
                            remove(label);
                        }
                        remove(edge);
                        removedEdge.add(edge);
                    }
                });
                for (Edge edge : removedEdge) {
                    mainFrame.setVisible(false);
                    edge.clearPaint();
                    mainFrame.setVisible(true);
                    edges.remove(edge);
                }
            }

            private void handleBFS(MouseEvent mouseEvent) {
                Vertex startVertex = getClickedVertex(mouseEvent);
                if (startVertex != null) {
                    mainFrame.setAlgoLabel("Please wait...");

                    // Execute BFS algorithm in a separate thread
                    new Thread(() -> {
                        GraphAlgorithmHandler algorithmHandler = new GraphAlgorithmHandler(vertices, edges, edgeLabels);
                        startVertex.setDoesRepaint();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        List<String> traversalPath = algorithmHandler.BFS(startVertex.getId());
                        StringBuilder builder = new StringBuilder("BFS : ");
                        for (String edge : traversalPath) {
                            builder.append(edge).append(" -> ");
                        }
                        String traversalResult = builder.length() > 4 ? builder.substring(0, builder.length() - 4) : "";

                        // Update GUI on the EDT
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.setAlgoLabel(traversalResult);
                        });
                    }).start();
                }
            }

            private void handleDFS(MouseEvent mouseEvent) {
                Vertex startVertex = getClickedVertex(mouseEvent);
                if (startVertex != null) {
                    mainFrame.setAlgoLabel("Please wait...");

                    // Execute BFS algorithm in a separate thread
                    new Thread(() -> {
                        GraphAlgorithmHandler algorithmHandler = new GraphAlgorithmHandler(vertices, edges, edgeLabels);
                        startVertex.setDoesRepaint();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        List<String> traversalPath = algorithmHandler.DFS(startVertex.getId());
                        StringBuilder builder = new StringBuilder("DFS : ");
                        for (String edge : traversalPath) {
                            builder.append(edge).append(" -> ");
                        }
                        String traversalResult = builder.length() > 4 ? builder.substring(0, builder.length() - 4) : "";

                        // Update GUI on the EDT
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.setAlgoLabel(traversalResult);
                        });
                    }).start();
                }
            }

            void handleDijkstra(MouseEvent mouseEvent) {
                Vertex startVertex = getClickedVertex(mouseEvent);
                if (startVertex != null) {
                    mainFrame.setAlgoLabel("Please wait...");

                    new Thread(() -> {
                        GraphAlgorithmHandler algorithmHandler = new GraphAlgorithmHandler(vertices, edges, edgeLabels);
                        startVertex.setDoesRepaint();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Map<String, Integer> vertexAndWeight = algorithmHandler.Dijkstra(startVertex.getId());
                        StringBuilder builder = new StringBuilder();
                        vertexAndWeight.forEach((vertex, weight) -> {
                            if (!vertex.equals(startVertex.getId())) {
                                builder.append(String.format("%s=%d, ", vertex, weight));
                            }
                        });
                        String result = builder.substring(0, builder.length()-2);

                        // Update GUI on the EDT
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.setAlgoLabel(result);
                        });
                    }).start();
                }
            }

            void handlePrim(MouseEvent mouseEvent) {
                Vertex startVertex = getClickedVertex(mouseEvent);
                if (startVertex != null) {
                    mainFrame.setAlgoLabel("Please wait...");

                    new Thread(() -> {
                        GraphAlgorithmHandler algorithmHandler = new GraphAlgorithmHandler(vertices, edges, edgeLabels);
                        startVertex.setDoesRepaint();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Map<String, String> traversedEdges = algorithmHandler.Prim(startVertex.getId());
                        StringBuilder builder = new StringBuilder();
                        traversedEdges.forEach((v1, v2) -> {
                            builder.append(String.format("%s=%s, ", v1, v2));
                        });
                        String result = builder.substring(0, builder.length()-2);

                        // Update GUI on the EDT
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.setAlgoLabel(result);
                        });
                    }).start();
                }
            }

            private void handleAlgo(int algoID, MouseEvent mouseEvent) throws InterruptedException {
                if (algoID == algorithm.BFS.algoID) {
                    handleBFS(mouseEvent);
                } else if (algoID == algorithm.DFS.algoID) {
                    handleDFS(mouseEvent);
                } else if (algoID == algorithm.DIJKSTRA.algoID) {
                    handleDijkstra(mouseEvent);
                } else if (algoID == algorithm.PRIM.algoID) {
                    handlePrim(mouseEvent);
                }
            }

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int modeID = mainFrame.getModeID();
                if(modeID == mode.ADDVERTEX.modeID) {
                    handleAddVertex(mouseEvent);
                } else if(modeID == mode.ADDEDGE.modeID) {
                    handleAddEdge(mouseEvent);
                } else if(modeID == mode.REMOVEVERTEX.modeID) {
                    handleRemoveVertex(mouseEvent);
                } else if(modeID == mode.REMOVEEDGE.modeID) {
                    handleRemoveEdge(mouseEvent);
                } else if(modeID == mode.None.modeID) {
                    int algoID = mainFrame.getAlgoID();
                    try {
                        handleAlgo(algoID, mouseEvent);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {}

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {}

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {}

            @Override
            public void mouseExited(MouseEvent mouseEvent) {}
        };
    }
    private boolean isOverlapVertex(Vertex checkedVertex) {
        for (Vertex vertex : vertices.values()) {
            Rectangle bounds = vertex.getBounds();
            if (bounds.intersects(checkedVertex.getBounds())) {
                return true;
            }
        }
        return false;
    }
    public void addVertex(String id, int rectX, int rectY) {
        Vertex vertex = new Vertex(id, rectX, rectY, VERTEXSIZE, VERTEXSIZE);
        vertices.put(id, vertex);
        add(vertex);
    }

    public Edge addEdge(String idV1, String idV2, int rectXV1, int rectYV1, int rectXV2, int rectYV2) {
        rectXV1 += VERTEXSIZE/2;
        rectXV2 += VERTEXSIZE/2;
        rectYV1 += VERTEXSIZE/2;
        rectYV2 += VERTEXSIZE/2;
        Edge edge = new Edge(idV1, idV2, rectXV1, rectYV1, rectXV2, rectYV2);
        add(edge);
        edges.add(edge);
        return edge;
    }

    private void clearEdge() {
        List<Edge> removedEdges = new ArrayList<>();
        edges.forEach(edge -> {
            JLabel label = edgeLabels.get(edge.getId());
            if(label != null) {
                remove(label);
                edgeLabels.remove(edge.getId());
            }
            remove(edge);
            removedEdges.add(edge);
        });

        for (Edge edge : removedEdges) {
            mainFrame.setVisible(false);
            edge.clearPaint();
            mainFrame.setVisible(true);
            edges.remove(edge);
        }
    }

    private void clearVertex() {
        List<Vertex> removedVertexes = new ArrayList<>();

        for (Vertex vertex : vertices.values()) {
            remove(vertex);
            removedVertexes.add(vertex);
        }

        for (Vertex vertex : removedVertexes) {
            mainFrame.setVisible(false);
            vertex.clearPaint();
            mainFrame.setVisible(true);

            vertices.remove(vertex.getId());
        }
    }

    public void reset() {
        resetClickedVertex();
        clearEdge();
        clearVertex();
    }

    private void resetVerticeColor() {
        for (Vertex vertex : vertices.values()) {
            vertex.resetDoesRepaint();
        }
    }

    private void resetEdgesColor() {
        for (Edge edge : edges) {
            edge.resetDoesRepaint();
        }
    }
    public void resetColorItems() {
        resetVerticeColor();
        resetEdgesColor();
    }
}
