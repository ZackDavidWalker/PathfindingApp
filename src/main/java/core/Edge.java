package core;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class Edge
{
    private List<EdgeWeight> weights;
    private Node connectedNode1;
    private Node connectedNode2;

    public Edge(Node node1, Node node2)
    {
        if (node1 == null || node2 == null) throw new IllegalArgumentException("Nodes cannot be null." +
                "An edge must connect two nodes.");
        connectedNode1 = node1;
        connectedNode2 = node2;
        weights = new ArrayList<>();
    }

    public void addWeight(EdgeWeight weightToAdd) throws InstanceAlreadyExistsException
    {
        if (weights.parallelStream().anyMatch(x -> x.getType().equals(weightToAdd.getType())))
            throw new InstanceAlreadyExistsException("This edge already contains a value for this weight type." +
                    "Use method setWeight to change the weight");
        else
            weights.add(weightToAdd);
    }

    public void setWeight(EdgeWeightType type, double value)
    {
        if (!weights.parallelStream().anyMatch(x -> x.getType().equals(type)))
        {
            EdgeWeight newWeight = new EdgeWeight(type, value);
            try
            {
                addWeight(newWeight);
            }
            catch (InstanceAlreadyExistsException ex)
            {
                ex.printStackTrace();
            }
        }
        else
            weights.parallelStream()
                    .filter(x -> x.getType().equals(type))
                    .findFirst()
                    .get()
                    .setValue(value);
    }

    public Node getPartner(Node sourceNode)
    {
        return connectedNode1.equals(sourceNode) ? connectedNode2 : connectedNode1;
    }

    public boolean connects(Node node1, Node node2)
    {
        return node1.equals(connectedNode1) && node2.equals(connectedNode2) ||
                node1.equals(connectedNode2) && node2.equals(connectedNode1);
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Edge)) return false;
        else
        {
            Edge o = (Edge)other;
            return connectedNode1.getName().equals(o.connectedNode1.getName()) &&
                    connectedNode2.getName().equals(o.connectedNode2.getName());
        }
    }
}
