package core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DijkstraPathfinder extends PathfinderBase
{
    private ArrayList<EvaluatedNode> unsettledNodes, settledNodes;
    private ArrayList<EvaluatedNode> allNodes;
    private HashMap<Node, EvaluatedNode> nodeMap;

    public DijkstraPathfinder(Graph graph)
    {
        super(graph);
    }

    private void initialize()
    {
        nodeMap = new HashMap<>();
        allNodes = new ArrayList<>();
        unsettledNodes = new ArrayList<>();
        settledNodes = new ArrayList<>();
        graph.getAllNodes().stream().filter(Node::isOpen).forEach(x ->
        {
            EvaluatedNode evalNode = new EvaluatedNode(x);
            nodeMap.put(x, evalNode);
            allNodes.add(new EvaluatedNode(x));
        });
        for (EvaluatedNode node : allNodes)
        {
            if (node != null && node.innerNode.equals(startNode))
            {
                node.setDistance(0);
                unsettledNodes.add(node);
            }
        }
    }

    @Override
    protected Path internalFindPath(Node startNode, Node endNode)
    {
        initialize();
        while(!unsettledNodes.isEmpty())
        {
            EvaluatedNode node = unsettledNodes.stream().min(Comparator.comparingDouble(x -> x.distance)).get();
            ArrayList<Node> neighbors = graph.getNeighborsForNode(node.innerNode);
            neighbors.removeIf(x -> settledNodes
                    .parallelStream()
                    .anyMatch(sn -> sn.innerNode.equals(x)));
            for (Node x : neighbors)
            {
                double edgeWeight = graph.getEdgeBetween(node.innerNode, x).getWeight(weightType).getValue();
                double evaluatedDistance = node.getDistance() + edgeWeight;
                EvaluatedNode mappedNode = nodeMap.get(x);
                if (mappedNode.getDistance() > evaluatedDistance)
                {
                    mappedNode.setDistance(evaluatedDistance);
                    mappedNode.setPredecessorNode(node.innerNode);
                    unsettledNodes.add(mappedNode);
                }
            }

            unsettledNodes.remove(node);
            settledNodes.add(node);
        }

        EvaluatedNode evalNode = nodeMap.get(endNode);
        Path path = new Path();
        path.addNode(evalNode.innerNode);
        while (evalNode.getPredecessorNode() != null)
        {
            EvaluatedNode predecessor = nodeMap.get(evalNode.getPredecessorNode());
            path.addAtStart(predecessor.innerNode);
            evalNode = predecessor;
        }

        return path;
    }

    private class EvaluatedNode
    {
        private Node innerNode;
        private Node predecessorNode;
        private double distance;

        private EvaluatedNode(Node innerNode)
        {
            this.innerNode = innerNode;
            predecessorNode = null;
            distance = Double.MAX_VALUE;
        }

        private double getDistance()
        {
            return distance;
        }

        private void setDistance(double distance)
        {
            this.distance = distance;
        }

        private Node getPredecessorNode()
        {
            return predecessorNode;
        }

        private void setPredecessorNode(Node predecessorNode)
        {
            this.predecessorNode = predecessorNode;
        }
    }
}
