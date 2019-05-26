package core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class DijkstraPathfinderTest
{
    private Graph simpleGraph, complexGraph;
    private IPathfinder pathfinder;

    @Before
    public void setup()
    {
        simpleGraph = getSimpleGraph();
        complexGraph = getComplexGraph();
    }

    @Test
    public void TestSimpleGraph()
    {
        pathfinder = new DijkstraPathfinder(simpleGraph);
        Path path = pathfinder.findPath("n1", "n3", EdgeWeightType.Distance);
        Assert.assertEquals("n1 -> n4 -> n3", path.toString());
    }

    @Test
    public void TestComplexGraph()
    {
        pathfinder = new DijkstraPathfinder(complexGraph);
        Path path = pathfinder.findPath("n1", "n6", EdgeWeightType.Distance);
        Assert.assertEquals("n1 -> n7 -> n8 -> n6", path.toString());
    }

    @Test
    public void TestNotConnectedNodes()
    {
        pathfinder = new DijkstraPathfinder(simpleGraph);
        Assert.assertNull(pathfinder.findPath("n1", "n6", EdgeWeightType.Distance));
    }

    @Test
    public void TestClosedNode()
    {
        pathfinder = new DijkstraPathfinder(simpleGraph);
        simpleGraph.getAllNodes().stream()
                .filter(x -> x.getName().equals("n4"))
                .findFirst()
                .get()
                .setOpen(false);
        Path path = pathfinder.findPath("n1", "n3", EdgeWeightType.Distance);
        Assert.assertEquals("n1 -> n2 -> n3", path.toString());
    }

    @Test
    public void TestStartNodeClosed()
    {
        pathfinder = new DijkstraPathfinder(simpleGraph);
        simpleGraph.getAllNodes().stream()
                .filter(x -> x.getName().equals("n1"))
                .findFirst()
                .get()
                .setOpen(false);
        Path path = pathfinder.findPath("n1", "n3", EdgeWeightType.Distance);
        Assert.assertNull(path);
    }

    @Test
    public void TestEndNodeClosed()
    {
        pathfinder = new DijkstraPathfinder(simpleGraph);
        simpleGraph.getAllNodes().stream()
                .filter(x -> x.getName().equals("n3"))
                .findFirst()
                .get()
                .setOpen(false);
        Path path = pathfinder.findPath("n1", "n3", EdgeWeightType.Distance);
        Assert.assertNull(path);
    }

    private Graph getSimpleGraph()
    {
        Graph g = new Graph();
        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Node n3 = new Node("n3");
        Node n4 = new Node("n4");
        Node n5 = new Node("n5");
        Node n6 = new Node("n6");
        Arrays.asList(n1, n2, n3, n4, n5, n6).forEach(g::addNode);
        g.connectNodes(n1, n2, EdgeWeightType.Distance, 2);
        g.connectNodes(n1, n4, EdgeWeightType.Distance, 2);
        g.connectNodes(n1, n5, EdgeWeightType.Distance, 1);
        g.connectNodes(n2, n3, EdgeWeightType.Distance, 3);
        g.connectNodes(n3, n4, EdgeWeightType.Distance, 2);

        return g;
    }

    private Graph getComplexGraph()
    {
        Graph g = new Graph();
        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Node n3 = new Node("n3");
        Node n4 = new Node("n4");
        Node n5 = new Node("n5");
        Node n6 = new Node("n6");
        Node n7 = new Node("n7");
        Node n8 = new Node("n8");
        Arrays.asList(n1, n2, n3, n4, n5, n6, n7, n8).forEach(g::addNode);

        g.connectNodes(n1, n2, EdgeWeightType.Distance, 4);
        g.connectNodes(n1, n7, EdgeWeightType.Distance, 4);
        g.connectNodes(n2, n3, EdgeWeightType.Distance, 10);
        g.connectNodes(n2, n7, EdgeWeightType.Distance, 3);
        g.connectNodes(n3, n4, EdgeWeightType.Distance, 2);
        g.connectNodes(n4, n5, EdgeWeightType.Distance, 2);
        g.connectNodes(n5, n6, EdgeWeightType.Distance, 5);
        g.connectNodes(n5, n7, EdgeWeightType.Distance, 7);
        g.connectNodes(n6, n8, EdgeWeightType.Distance, 6);
        g.connectNodes(n7, n8, EdgeWeightType.Distance, 3);

        return g;
    }
}
