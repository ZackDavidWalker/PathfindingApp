package core;

import java.util.ArrayList;

public class Node
{
    private String name;
    private ArrayList<Edge> edges;

    public Node(String name)
    {
        edges = new ArrayList<>();
        this.name = name;
    }

    public void addEdge(Edge edge)
    {
        if (!edges.contains(edge))
            edges.add(edge);
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
}
