package core;

public class EdgeWeight
{
    private EdgeWeightType type;
    private double value;

    public EdgeWeight(EdgeWeightType type, double value)
    {
        this.type = type;
        this.value = value;
    }

    public EdgeWeightType getType()
    {
        return this.type;
    }

    public double getValue()
    {
        return this.value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }
}
