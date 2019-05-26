package core;

public abstract class PathfinderBase implements IPathfinder
{
    protected Graph graph;
    protected Node startNode;
    protected Node endNode;
    protected EdgeWeightType weightType;

    public PathfinderBase(Graph graph)
    {
        this.graph = graph;
    }

    public void setWeightType(EdgeWeightType weightType)
    {
        this.weightType = weightType;
    }

    @Override
    public final Path findPath(String startNode, String endNode, EdgeWeightType weightType)
    {
        this.startNode = graph.getNode(startNode);
        this.endNode = graph.getNode(endNode);
        if (!graph.areConnected(this.startNode, this.endNode))
            return null;
        if (this.startNode == null || this.endNode == null)
            throw new IllegalArgumentException("Given start- or end-node does not exist within the given graph");
        if (graph == null || weightType == null)
            throw new IllegalStateException("Graph or weight type is null. Cannot proceed");
        setWeightType(weightType);
        return internalFindPath(this.startNode, this.endNode);
    }

    protected abstract Path internalFindPath(Node startNode, Node endNode);
}
