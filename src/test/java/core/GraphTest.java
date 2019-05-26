package core;

import org.junit.*;

public class GraphTest
{
    private Node n1, n2, n3, n4, n5, n6;
    private Graph g;

    @Before
    public void setUp()
    {
        g = new Graph();
        n1 = new Node("1");
        n2 = new Node("2");
        n3 = new Node("3");
        n4 = new Node("4");
        n5 = new Node("5");
        n6 = new Node("6");
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.addNode(n6);

        g.connectNodes(n1, n2);
        g.connectNodes(n1, n4);
        g.connectNodes(n1, n6);
        g.connectNodes(n2, n3);
        g.connectNodes(n3, n4);

        g.setEdgeWeightBetween(n1, n2, EdgeWeightType.Distance, 3);
        g.setEdgeWeightBetween(n1, n4, EdgeWeightType.Distance, 2);
        g.setEdgeWeightBetween(n1, n6, EdgeWeightType.Distance, 1);
        g.setEdgeWeightBetween(n2, n3, EdgeWeightType.Distance, 7);
        g.setEdgeWeightBetween(n3, n4, EdgeWeightType.Distance, 2);
    }

    @Test
    public void TestGetNeighbors()
    {
        Assert.assertEquals(3, g.getNeighborsForNode(n1).size());
        Assert.assertEquals(2, g.getNeighborsForNode(n2).size());
        Assert.assertEquals(2, g.getNeighborsForNode(n3).size());
        Assert.assertEquals(2, g.getNeighborsForNode(n4).size());
        Assert.assertEquals(0, g.getNeighborsForNode(n5).size());
        Assert.assertEquals(1, g.getNeighborsForNode(n6).size());
    }

    @Test
    public void TestDirectConnections()
    {
        Assert.assertTrue(g.areConnected(n1, n2));
        Assert.assertTrue(g.areConnected(n1, n4));
        Assert.assertTrue(g.areConnected(n1, n6));
        Assert.assertTrue(g.areConnected(n2, n1));
        Assert.assertTrue(g.areConnected(n2, n3));
        Assert.assertTrue(g.areConnected(n3, n2));
        Assert.assertTrue(g.areConnected(n3, n4));
        Assert.assertTrue(g.areConnected(n4, n1));
        Assert.assertTrue(g.areConnected(n4, n3));
    }

    @Test
    public void TestIndirectConnections()
    {
        Assert.assertTrue(g.areConnected(n1, n3));
        Assert.assertTrue(g.areConnected(n2, n4));
        Assert.assertTrue(g.areConnected(n2, n6));
        Assert.assertTrue(g.areConnected(n3, n1));
        Assert.assertTrue(g.areConnected(n3, n6));
        Assert.assertTrue(g.areConnected(n4, n2));
        Assert.assertTrue(g.areConnected(n4, n6));
    }

    @Test
    public void TestN5Connections()
    {
        Assert.assertFalse(g.areConnected(n1, n5));
        Assert.assertFalse(g.areConnected(n2, n5));
        Assert.assertFalse(g.areConnected(n3, n5));
        Assert.assertFalse(g.areConnected(n4, n5));
        Assert.assertFalse(g.areConnected(n6, n5));
    }

    @Test
    public void TestGetEdges()
    {
        Assert.assertTrue(g.getEdgeBetween(n1, n2).equals(g.getEdgeBetween(n2, n1)));
        Assert.assertTrue(g.getEdgeBetween(n2, n3).equals(g.getEdgeBetween(n3, n2)));
        Assert.assertTrue(g.getEdgeBetween(n3, n4).equals(g.getEdgeBetween(n4, n3)));
        Assert.assertTrue(g.getEdgeBetween(n4, n1).equals(g.getEdgeBetween(n1, n4)));
        Assert.assertTrue(g.getEdgeBetween(n1, n6).equals(g.getEdgeBetween(n6, n1)));

        Assert.assertTrue(g.getEdgeBetween(n1, n5) == null);
        Assert.assertTrue(g.getEdgeBetween(n2, n5) == null);
        Assert.assertTrue(g.getEdgeBetween(n3, n5) == null);
        Assert.assertTrue(g.getEdgeBetween(n4, n5) == null);
        Assert.assertTrue(g.getEdgeBetween(n6, n5) == null);
    }

    @Test
    public void TestEdgeWeight()
    {
        EdgeWeightType weightType = EdgeWeightType.Distance;
        Assert.assertEquals(3.0, g.getEdgeBetween(n1, n2).getWeight(weightType).getValue(), 0.0);
        Assert.assertEquals(2.0, g.getEdgeBetween(n1, n4).getWeight(weightType).getValue(), 0.0);
        Assert.assertEquals(1.0, g.getEdgeBetween(n1, n6).getWeight(weightType).getValue(), 0.0);
        Assert.assertEquals(7.0, g.getEdgeBetween(n2, n3).getWeight(weightType).getValue(), 0.0);
        Assert.assertEquals(2.0, g.getEdgeBetween(n3, n4).getWeight(weightType).getValue(), 0.0);
    }
}
