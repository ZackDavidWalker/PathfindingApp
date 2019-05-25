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

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        nodePath.stream()
                .forEach(x -> sb.append(x.getName() + " -> "));
        return sb.toString();
    }
}
