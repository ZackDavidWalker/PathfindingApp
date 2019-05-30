package gui;

import core.*;
import gui.Shapes.PathEdge;
import gui.Shapes.PathNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
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
    @FXML
    private Text infoText;
    @FXML
    private Text currentCoordsText;

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
        infoText.setText(node.getName() + " -> ");
    }

    private void endLineAt(PathNode node, PathEdge edge)
    {
        if (!node.equals(edge.getStartNode()))
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
            infoText.setText("");
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to connect nodes");
            alert.setContentText("You cannot connect node to itself.");
        }
    }

    private void setNode(MouseEvent mouseEvent)
    {
        PathNode node = new PathNode(getNextName(), mouseEvent.getX(), mouseEvent.getY());
        StackPane.setAlignment(node, Pos.TOP_LEFT);
        graphPane.getChildren().add(node);
        node.setOnMouseClicked(this::onNodeClicked);
        node.setOnMouseEntered(x ->
        {
            node.setFill(PathNode.FILL_MOUSEOVER);
            if (!edgeDrawingActive)
                infoText.setText("Node: " + node.getName());
            else
                infoText.setText(infoText.getText() + node.getName());
        });
        node.setOnMouseExited(x ->
        {
            node.setFill(node.getCurrentMainColor());
            if (!edgeDrawingActive)
                infoText.setText("");
            else if (infoText.getText().contains(node.getName()) && infoText.getText().lastIndexOf(node.getName()) > 0)
                infoText.setText(infoText.getText().replace(" " + node.getName(), " "));
        });
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
        edges.forEach(x ->
        {
            x.setStroke(PathEdge.COLOR);
            x.stopAnimate();
        });
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
                        PathEdge edge = edges.stream()
                                .filter(x -> x.getStartNode().getName().equals(finalPrev.getName()) &&
                                        x.getEndNode().getName().equals(finalCurr.getName()) ||
                                        x.getStartNode().getName().equals(finalCurr.getName()) &&
                                                x.getEndNode().getName().equals(finalPrev.getName()))
                                .findFirst()
                                .get();
                        edge.setStroke(PathEdge.OPT_PATH_COLOR);
                        boolean reverse = edge.getStartNode().getName().equals(curr.getName());
                        edge.animate(reverse);
                    }

                    prev = curr;
                }
            }
            else
            {
                nodes.stream().filter(x -> x.getName().equals(startNodeName)).findFirst().get().setMainColor(PathNode.OPT_PATH_FILL_FIRST);
                nodes.stream().filter(x -> x.getName().equals(endNodeName)).findFirst().get().setMainColor(PathNode.OPT_PATH_FILL_LAST);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Failed to find path");
                alert.setContentText(startNodeName + " and " + endNodeName + " are not connected");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to find path");
            if (startNode == null)
                alert.setContentText("The node '" + startNodeName + "' does not exist.");
            else if (endNode == null)
                alert.setContentText("The node '" + endNodeName + "' does not exist.");
            alert.showAndWait();
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

    public void onMouseMoved(MouseEvent mouseEvent)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        double currentX = mouseEvent.getX();
        double currentY = mouseEvent.getY();
        currentCoordsText.setText("X: " + df.format(currentX) + "; Y: " + df.format(currentY));
    }
}