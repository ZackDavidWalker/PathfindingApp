package core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node
{
    private String name;
    private ArrayList<Edge> edges;

    public Node(String name)
    {
        edges = new ArrayList<>();
        this.name = name;
    }

    public void connectTo(Node node)
    {
        if (node != null && !isDirectlyConnectedTo(node))
        {
            Edge edge = new Edge(this, node);
            edges.add(edge);
            node.connectTo(this);
        }
    }

    public ArrayList<Node> getNeighbors()
    {
        ArrayList<Node> neighbors = new ArrayList<>();
        edges.parallelStream()
                .forEach(x -> neighbors.add(x.getPartner(this)));
        return neighbors;
    }

    public boolean isConnectedTo(Node node)
    {
        return internalIsConnectedTo(node, new ArrayList<>());
    }

    private boolean internalIsConnectedTo(Node node, ArrayList<Node> visitedNodes)
    {
        ArrayList<Node> neighbors = getNeighbors().stream()
                .filter(x -> !visitedNodes.contains(x))
                .collect(Collectors.toCollection(ArrayList::new));
        if (neighbors.isEmpty()) return false;
        else if (neighbors.contains(node)) return true;
        else
        {
            visitedNodes.add(this);
            return neighbors.parallelStream().anyMatch(x -> x.internalIsConnectedTo(node, visitedNodes));
        }
    }

    public String getName()
    {
        return name;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Node)) return false;
        return ((Node)other).name.equals(this.name);
    }

    public String toString()
    {
        return name;
    }

    private boolean isDirectlyConnectedTo(Node node)
    {
        return getNeighbors().contains(node);
    }
}
