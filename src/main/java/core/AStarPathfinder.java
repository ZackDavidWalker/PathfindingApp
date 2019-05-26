package core;

public class AStarPathfinder extends PathfinderBase
{

    public AStarPathfinder(Graph graph, String startNode, String endNode, EdgeWeightType weightType)
    {
        super(graph);
    }

    @Override
    protected Path internalFindPath(Node startNode, Node endNode)
    {
        return null;
    }
}
