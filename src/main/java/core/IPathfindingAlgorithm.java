package core;

public interface IPathfindingAlgorithm
{
    Path findPath(Node startNode, Node endNode, EdgeWeightType weightType);
}
