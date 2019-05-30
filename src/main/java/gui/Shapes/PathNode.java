package gui.Shapes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class PathNode extends Circle
{
    private boolean isSelected;
    private String name;
    private Color currentColor;

    public static final double RADIUS = 7.5;
    public static final Color FILL = Color.BLUE;
    public static  final Color OPT_PATH_FILL = Color.LIGHTGREEN;
    public static  final Color OPT_PATH_FILL_FIRST = Color.GREEN;
    public static  final Color OPT_PATH_FILL_LAST = Color.RED;
    public static final Color FILL_MOUSEOVER = Color.SKYBLUE;
    public static final Color STROKE = Color.RED;

    public PathNode(String name, double x, double y)
    {
        this.name = name;
        setRadius(RADIUS);
        super.setFill(FILL);
        currentColor = FILL;
        setStroke(STROKE);
        setTranslateX(x - RADIUS);
        setTranslateY(y - RADIUS);
        deselect();
    }

    public String getName()
    {
        return name;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void select()
    {
        setStrokeWidth(1);
        isSelected = true;
    }

    public void deselect()
    {
        setStrokeWidth(0);
        isSelected = false;
    }

    public void setMainColor(Color color)
    {
        super.setFill(color);
        currentColor = color;
    }

    public Color getCurrentMainColor()
    {
        return currentColor;
    }
}