package core;

import java.util.ArrayList;

public class GraphManager
{
    private ArrayList<Graph> graphs;
    private IPathfinder pathfinder;

    public GraphManager()
    {
        graphs = new ArrayList<>();
    }

    public void setPathfinder(IPathfinder pathfinder)
    {
        this.pathfinder = pathfinder;
    }

    public Path findPath(String startNode, String endNode, EdgeWeightType weightType)
    {
        if (pathfinder == null)
            throw new IllegalStateException("Pathfinder is not set.");
        return pathfinder.findPath(startNode, endNode, weightType);
    }
}
