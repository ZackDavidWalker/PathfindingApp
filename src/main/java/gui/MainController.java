package gui;

import core.*;
import gui.Shapes.PathEdge;
import gui.Shapes.PathNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MainController
{
    private Graph model;
    private ArrayList<PathNode> nodes;
    private ArrayList<PathEdge> edges;
    private ArrayList<Integer> usedInts;
    private boolean edgeDrawingActive;
    private PathEdge activeEdge;

    @FXML
    private StackPane graphPane;
    @FXML
    private BorderPane root;
    @FXML
    private ToggleButton nodeToggle;
    @FXML
    private ToggleButton edgeToggle;
    @FXML
    private TextField startNodeName;
    @FXML
    private TextField endNodeName;

    public MainController()
    {
        model = new Graph();
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        usedInts = new ArrayList<>();
    }

    public void initialize()
    {
        nodeToggle.setSelected(true);
    }

    public void setShape(MouseEvent mouseEvent)
    {
        if (nodeToggle.isSelected())
            setNode(mouseEvent);
        else if (edgeToggle.isSelected())
            setLine(mouseEvent);
    }

    private void setLine(MouseEvent mouseEvent)
    {
        Object sender = mouseEvent.getSource();
        if (sender instanceof PathNode)
        {
            PathNode node = (PathNode) sender;
            if (!edgeDrawingActive)
                startLineAt(node);
            else
                endLineAt(node, activeEdge);

            mouseEvent.consume();
        }
    }

    private void startLineAt(PathNode node)
    {
        PathEdge edge = new PathEdge();
        edge.setStartNode(node);
        activeEdge = edge;
        edgeDrawingActive = true;
    }

    private void endLineAt(PathNode node, PathEdge edge)
    {
        edge.setEndNode(node);
        StackPane.setAlignment(edge, Pos.TOP_LEFT);
        graphPane.getChildren().add(edge);
        edge.toBack();
        activeEdge = null;
        edgeDrawingActive = false;
        model.connectNodes(model.getNode(edge.getStartNode().getName()),
                model.getNode(edge.getEndNode().getName()),
                EdgeWeightType.Distance,
                edge.getLength());
        edges.add(edge);
    }

    private void setNode(MouseEvent mouseEvent)
    {
        PathNode node = new PathNode(getNextName(), mouseEvent.getX(), mouseEvent.getY());
        StackPane.setAlignment(node, Pos.TOP_LEFT);
        graphPane.getChildren().add(node);
        node.setOnMouseClicked(this::onNodeClicked);
        node.setOnMouseEntered(x -> node.setFill(PathNode.FILL_MOUSEOVER));
        node.setOnMouseExited(x -> node.setFill(node.getCurrentMainColor()));
        nodes.add(node);
        model.addNode(new Node(node.getName()));
    }

    private void onNodeClicked(MouseEvent event)
    {
        Object sender = event.getSource();
        if (sender instanceof PathNode)
        {
            PathNode node = (PathNode) sender;
            if (nodeToggle.isSelected())
            {
                if (node.isSelected())
                    node.deselect();
                else
                {
                    nodes.forEach(PathNode::deselect);
                    node.select();
                }
                event.consume();
            }
            else if (edgeToggle.isSelected())
            {
                setLine(event);
            }
        }
    }

    private String getNextName()
    {
        String prefix = "x";
        int nextInt = 0;
        if (!usedInts.isEmpty())
        {
            for (int i : usedInts)
            {
                if (!usedInts.contains(i + 1))
                    nextInt = i + 1;
            }
        }

        usedInts.add(nextInt);
        prefix += Integer.toString(nextInt);
        return prefix;
    }

    public void findPath(ActionEvent actionEvent)
    {
        nodes.forEach(x -> x.setMainColor(PathNode.FILL));
        edges.forEach(x -> x.setStroke(PathEdge.COLOR));
        String startNodeName = this.startNodeName.getText();
        String endNodeName = this.endNodeName.getText();
        Node startNode = model.getNode(startNodeName);
        Node endNode = model.getNode(endNodeName);
        if (startNode != null && endNode != null)
        {
            IPathfinder pathfinder = new DijkstraPathfinder(model);
            Path path = pathfinder.findPath(startNodeName, endNodeName, EdgeWeightType.Distance);
            if (path != null)
            {
                LinkedList<Node> nodePath = path.getNodePath();
                Iterator<Node> i = nodePath.iterator();
                Node prev = null;
                Node curr = null;
                while (i.hasNext())
                {
                    curr = i.next();
                    Node finalCurr = curr;
                    PathNode pathNode = nodes.stream()
                            .filter(x -> x.getName().equals(finalCurr.getName()))
                            .findFirst()
                            .get();
                    if (prev == null)
                        pathNode.setMainColor(PathNode.OPT_PATH_FILL_FIRST);
                    else if (!i.hasNext())
                        pathNode.setMainColor(PathNode.OPT_PATH_FILL_LAST);
                    else
                        pathNode.setMainColor(PathNode.OPT_PATH_FILL);

                    if (prev != null)
                    {
                        Node finalPrev = prev;
                        edges.stream()
                                .filter(x -> x.getStartNode().getName().equals(finalPrev.getName()) &&
                                        x.getEndNode().getName().equals(finalCurr.getName()) ||
                                        x.getStartNode().getName().equals(finalCurr.getName()) &&
                                        x.getEndNode().getName().equals(finalPrev.getName()))
                                .findFirst()
                                .get()
                                .setStroke(PathEdge.OPT_PATH_COLOR);
                    }

                    prev = curr;
                }
            }
        }
    }

    public void clear(ActionEvent actionEvent)
    {
        graphPane.getChildren().clear();
        nodes.clear();
        edges.clear();
        usedInts.clear();
        model.clear();
    }
}