package core;

import java.util.LinkedList;

public class Path
{
    private LinkedList<Node> nodePath;

    public Path()
    {
        nodePath = new LinkedList<>();
    }

    public void addNode(Node node)
    {
        nodePath.add(node);
    }

    public void addAtStart(Node node)
    {
        nodePath.addFirst(node);
    }

    public String toString()
    {
        String delimiter = " -> ";
        StringBuilder sb = new StringBuilder();
        nodePath.forEach(x -> sb.append(x.getName()).append(delimiter));
        sb.delete(sb.lastIndexOf(delimiter), sb.length());
        return sb.toString();
    }
}
