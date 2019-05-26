package core;

import java.util.ArrayList;

public class Node
{
    private String name;
    private ArrayList<Edge> edges;
    private boolean isOpen;

    public Node(String name)
    {
        edges = new ArrayList<>();
        this.name = name;
        isOpen = true;
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

    public boolean isOpen()
    {
        return isOpen;
    }

    public void setOpen(boolean isOpen)
    {
        this.isOpen = isOpen;
    }

    public String toString()
    {
        return name;
    }
}
