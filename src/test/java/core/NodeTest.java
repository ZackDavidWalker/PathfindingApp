package core;

import org.junit.*;

public class NodeTest
{
    Node n1, n2, n3, n4, n5;

    @Before
    public void setUp()
    {
        n1 = new Node("1");
        n2 = new Node("2");
        n3 = new Node("3");
        n4 = new Node("4");
        n5 = new Node("5");

        n1.connectTo(n2);
        n2.connectTo(n3);
        n3.connectTo(n4);
        n4.connectTo(n1);
    }

    @Test
    public void TestDirectConnections()
    {
        Assert.assertTrue(n1.isConnectedTo(n2));
        Assert.assertTrue(n2.isConnectedTo(n3));
        Assert.assertTrue(n3.isConnectedTo(n4));
        Assert.assertTrue(n4.isConnectedTo(n1));
    }

    @Test
    public void TestIndirectConnections()
    {
        Assert.assertTrue(n1.isConnectedTo(n3));
        Assert.assertTrue(n2.isConnectedTo(n4));
    }

    @Test
    public void TestN5Connections()
    {
        Assert.assertFalse(n1.isConnectedTo(n5));
        Assert.assertFalse(n2.isConnectedTo(n5));
        Assert.assertFalse(n3.isConnectedTo(n5));
        Assert.assertFalse(n4.isConnectedTo(n5));
    }
}
