package gui.Shapes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PathEdge extends Line
{
    public static final Color COLOR = Color.SKYBLUE;
    public static final Color OPT_PATH_COLOR = Color.LIGHTGREEN;
    private final double WIDTH = 4;
    private PathNode startNode;
    private PathNode endNode;

    public PathEdge()
    {
        setFill(COLOR);
        setStroke(COLOR);
        setStrokeWidth(WIDTH);
    }

    public void setStartNode(PathNode startNode)
    {
        this.startNode = startNode;
        DoubleProperty startX = new SimpleDoubleProperty(startNode.getTranslateX());
        DoubleProperty startY = new SimpleDoubleProperty(startNode.getTranslateY());
        startXProperty().bind(startX);
        startYProperty().bind(startY);
    }

    public void setEndNode(PathNode endNode)
    {
        this.endNode = endNode;
        DoubleProperty endX = new SimpleDoubleProperty(endNode.getTranslateX());
        DoubleProperty endY = new SimpleDoubleProperty(endNode.getTranslateY());
        endXProperty().bind(endX);
        endYProperty().bind(endY);
        translate();
    }

    public PathNode getStartNode()
    {
        return startNode;
    }

    public PathNode getEndNode()
    {
        return endNode;
    }

    public double getLength()
    {
        return Math.sqrt(Math.pow(endNode.getTranslateX() - startNode.getTranslateX(), 2) +
                Math.pow(endNode.getTranslateY() - startNode.getTranslateY(), 2));
    }

    private void translate()
    {
        double startX = startNode.getTranslateX() + PathNode.RADIUS/2;
        double startY = startNode.getTranslateY() + PathNode.RADIUS/2;
        double endX = endNode.getTranslateX() + PathNode.RADIUS/2;
        double endY = endNode.getTranslateY() + PathNode.RADIUS/2;

        if (startX <= endX)
            setTranslateX(startX);
        else
            setTranslateX(endX);
        if (startY >= endY)
            setTranslateY(endY);
        else
            setTranslateY(startY);

    }
}
