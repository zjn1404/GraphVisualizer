package visualizer.Graph;

import javax.swing.*;
import java.util.*;
import visualizer.Util.Pair;
public class GraphAlgorithmHandler {
    private final Map<String, Vertex> vertices;
    private final Set<Edge> edges;
    private final Map<String, JLabel> edgeLabels;
    Map<String, List<Edge>> adj;

    public GraphAlgorithmHandler(Map<String, Vertex> vertices, Set<Edge> edges, Map<String, JLabel> edgeLabels) {
        this.vertices = vertices;
        this.edges = edges;
        this.edgeLabels = edgeLabels;
        createADJ();
    }

    private void createADJ() {
        this.adj = new HashMap<>();
        vertices.keySet().forEach(vertex -> {
            List<Edge> vEdges = new ArrayList<>();
            edges.forEach(edge -> {
                if (edge.getStartVertex().equals(vertex)) {
                    vEdges.add(edge);
                }
            });

            vEdges.sort((edge, otherEdge) -> {
                if (edgeLabels.containsKey(edge.getId()) && edgeLabels.containsKey(otherEdge.getId())) {
                    int edgeWeight = Integer.parseInt(edgeLabels.get(edge.getId()).getText());
                    int otherEdgeWeight = Integer.parseInt(edgeLabels.get(otherEdge.getId()).getText());
                    return Integer.compare(edgeWeight, otherEdgeWeight);
                }
                return 0;
            });
            adj.put(vertex, vEdges);
        });
    }

    private Edge findTheSameEdge(Edge edge) {
        String id = String.format("<%s -> %s>", edge.getEndVertex(), edge.getStartVertex());
        for (Edge e : edges) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    private Edge findTheSameEdge(String v1, String v2) {
        String id = String.format("<%s -> %s>", v1, v2);
        for (Edge e : edges) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    private void DFS(String vertex, Map<String, Boolean> visited, List<String> traversalPath) {
        if (visited.getOrDefault(vertex, false)) return;
        traversalPath.add(vertex);
        vertices.get(vertex).setDoesRepaint();
        visited.putIfAbsent(vertex, true);
        List<Edge> edges = adj.getOrDefault(vertex, null);
        if (edges == null) return;
        for (Edge edge : edges) {
            if (!visited.getOrDefault(edge.getEndVertex(), false)) {
                System.out.println(edge);
                edge.setDoesRepaint();
                findTheSameEdge(edge).setDoesRepaint();
                DFS(edge.getEndVertex(), visited, traversalPath);
            }
        }
    }

    public List<String> DFS(String startVertex) {
        Map<String, Boolean> visited = new HashMap<>();
        List<String> traversalPath = new ArrayList<>();
        DFS(startVertex, visited, traversalPath);
        return traversalPath;
    }

    private void BFS(String vertex, Map<String, Boolean> visited, List<String> traversalPath) {
        Deque<String> queue = new ArrayDeque<>();
        queue.add(vertex);
        visited.putIfAbsent(vertex, true);
        traversalPath.add(vertex);

        while (!queue.isEmpty()) {
            vertex = queue.pop();
            vertices.get(vertex).setDoesRepaint();
            adj.get(vertex).forEach(edge -> {
                if (!visited.getOrDefault(edge.getEndVertex(), false)) {
                    queue.add(edge.getEndVertex());
                    visited.putIfAbsent(edge.getEndVertex(), true);
                    traversalPath.add(edge.getEndVertex());
                    edge.setDoesRepaint();
                    findTheSameEdge(edge).setDoesRepaint();
                }
            });
        }
    }

    public List<String> BFS(String startVertex) {
        Map<String, Boolean> visited = new HashMap<>();
        List<String> traversalPath = new ArrayList<>();
        BFS(startVertex, visited, traversalPath);
        return traversalPath;
    }

    private Map<String, Integer> Dijkstra(String vertex, Map<String, Boolean> visited) {
        Map<String, Integer> distances = new LinkedHashMap<>();
        vertices.keySet().forEach(v -> {
            distances.put(v, Integer.MAX_VALUE);
        });
        distances.replace(vertex, 0);
        PriorityQueue<Pair<Integer, String>> pq = new PriorityQueue<>(new PairISComparator());
        pq.add(new Pair<>(0, vertex));
        while (!pq.isEmpty()) {
            Pair<Integer, String> weightAndVertex = pq.poll();
            if (visited.getOrDefault(weightAndVertex.getSecond(), false)) continue;
            visited.putIfAbsent(weightAndVertex.getSecond(),true);
            adj.get(weightAndVertex.getSecond()).forEach(edge -> {
                int weight = 0;
                if (edgeLabels.containsKey(edge.getId())) {
                    weight = Integer.parseInt(edgeLabels.get(edge.getId()).getText());
                } else {
                    weight = Integer.parseInt((edgeLabels.get(findTheSameEdge(edge).getId()).getText()));
                }
                int distance = weightAndVertex.getFirst() + weight;
                if (!visited.getOrDefault(edge.getEndVertex(), false) && distances.get(edge.getEndVertex()) > distance) {
                    distances.replace(edge.getEndVertex(), distance);
                    pq.add(new Pair<>(distance, edge.getEndVertex()));
                }
            });
        }
        return distances;
    }

    public Map<String, Integer> Dijkstra(String startVertex) {
        Map<String, Boolean> visited = new HashMap<>();
        return Dijkstra(startVertex, visited);
    }

    private Map<String, String> Prim(String vertex, Map<String, Boolean> visited) {
        Map<String, String> parents = new HashMap<>();
        Map<String, Integer> distances = new LinkedHashMap<>();
        vertices.keySet().forEach(v -> {
            distances.put(v, Integer.MAX_VALUE);
        });
        distances.replace(vertex, 0);
        PriorityQueue<Pair<Integer, String>> pq = new PriorityQueue<>(new PairISComparator());
        pq.add(new Pair<>(0, vertex));
        while (!pq.isEmpty()) {
            Pair<Integer, String> weightAndVertex = pq.poll();
            if (visited.getOrDefault(weightAndVertex.getSecond(), false)) continue;
            visited.putIfAbsent(weightAndVertex.getSecond(),true);
            adj.get(weightAndVertex.getSecond()).forEach(edge -> {
                int weight = 0;
                if (edgeLabels.containsKey(edge.getId())) {
                    weight = Integer.parseInt(edgeLabels.get(edge.getId()).getText());
                } else {
                    weight = Integer.parseInt((edgeLabels.get(findTheSameEdge(edge).getId()).getText()));
                }
                if (!visited.getOrDefault(edge.getEndVertex(), false) && distances.get(edge.getEndVertex()) > weight) {
                    distances.replace(edge.getEndVertex(), weight);
                    pq.add(new Pair<>(weight, edge.getEndVertex()));
                    parents.put(edge.getEndVertex(), weightAndVertex.getSecond());
                }
            });
        }
        return parents;
    }
    private String getEdgeID(String v1, String v2) {
        if (v1.compareTo(v2) > 0) {
            String tmp = v1;
            v1 = v2;
            v2 = tmp;
        }

        return String.format("<%s -> %s>", v1, v2);
    }
    private void colorEdge(Map<String, String> edgeMap) {
        edgeMap.forEach((v1, v2) -> {
            vertices.get(v1).setDoesRepaint();
            vertices.get(v2).setDoesRepaint();
            edges.forEach(e -> {
                if (e.getId().equals(getEdgeID(v1, v2))) e.setDoesRepaint();
                Objects.requireNonNull(findTheSameEdge(v2, v1)).setDoesRepaint();
            });
        });
    }
    public Map<String, String> Prim(String startVertex) {
        Map<String, Boolean> visited = new HashMap<>();
        Map<String, String> edgeMap = Prim(startVertex, visited);
        colorEdge(edgeMap);
        return  edgeMap;
    }

    private static class PairISComparator implements Comparator<Pair<Integer, String>> {

        @Override
        public int compare(Pair pair, Pair otherPair) {
            int res = Integer.compare((Integer) pair.getFirst(), (Integer) otherPair.getFirst());
            if (res == 0) {
                res = ((String) pair.getSecond()).compareTo((String) otherPair.getSecond());
            }
            return res;
        }
    }
}
