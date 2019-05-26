package core;

public interface IPathfinder
{
    Path findPath(String startNode, String endNode, EdgeWeightType weightType);
}
