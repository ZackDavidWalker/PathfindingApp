package core;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Graph
{
    private ArrayList<Node> allNodes;
    private ArrayList<Edge> allEdges;

    public Graph()
    {
        allNodes = new ArrayList<>();
        allEdges = new ArrayList<>();
    }

    public void addNode(Node node)
    {
        if (!allNodes.contains(node))
            allNodes.add(node);
    }

    public void connectNodes(Node node1, Node node2)
    {
        Edge edge = new Edge(node1, node2);
        node1.addEdge(edge);
        node2.addEdge(edge);
        if (!allEdges.contains(edge))
            allEdges.add(edge);
    }

    public Edge getEdgeBetween(Node node1, Node node2)
    {
        return allEdges.parallelStream()
                .filter(x -> x.connects(node1, node2))
                .findFirst()
                .orElse(null);
    }

    public ArrayList<Node> getNeighborsForNode(Node node)
    {
        return allNodes.parallelStream()
                .filter(x -> getEdgeBetween(x, node) != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean areConnected(Node node1, Node node2)
    {
        return internalAreConnected(node1, node2, new ArrayList<>());
    }

    private boolean internalAreConnected(Node sourceNode, Node endNode, ArrayList<Node> visitedNodes)
    {
        if (getEdgeBetween(sourceNode, endNode) != null) return true;
        ArrayList<Node> neighbors = getNeighborsForNode(sourceNode).parallelStream()
                .filter(x -> !visitedNodes.contains(x))
                .collect(Collectors.toCollection(ArrayList::new));
        if (neighbors.isEmpty()) return false;
        else if (neighbors.contains(endNode)) return true;
        visitedNodes.add(sourceNode);
        return neighbors.parallelStream().anyMatch(x -> internalAreConnected(x, endNode, visitedNodes));
    }
}