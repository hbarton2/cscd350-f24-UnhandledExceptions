package com.unhandledexceptions.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Model.RelationshipItem;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

/*
 * This class is used to create a line between two classes in the GUI.
 * The way it works is you drag from the Ranchor (relationship anchor) of a classbox into the other Ranchor of a class box to create a relationship.
 * You can also draw from a Ranchor to a blank space to create a relationship, which will automatically create a new class as well.
 */

public class RelationLine extends Polyline
{
    //temp
    List<Rectangle> testRects = new ArrayList<>();

    private static final int CELL_SIZE = 10; // Grid resolution
    
    //private Point2D sourceOffset, destOffset; not serializable!!
    private double sourceX, sourceY, destX, destY;
    // classboxes that the relation line connects
    private ClassBox source, dest;
    // type of relationship (Aggregation, Composition, Generalization, Realization)
    private String type;

    BaseController baseController;
    AnchorPane anchorPane;
    Button typeButton;
    Button deleteButton;
    ImageView typeIcon;
    Circle ranchor;
    Rectangle testRect; Line testLine;

    private final Color lightColor = Color.rgb(211, 211, 211);
    private final Color darkColor = Color.rgb(27, 70, 160);

    // constructor for relationship line
    public RelationLine(BaseController baseController, AnchorPane anchorPane)
    {
        // toBack() moves the relationship line to the back of the canvas so it doesn't cover the classboxes
        toBack();
        this.anchorPane = anchorPane;
        this.baseController = baseController;
        type = "Composition";
        typeIcon = new ImageView(new Image("/images/Composition.png"));
        typeIcon.setVisible(false);
        anchorPane.getChildren().add(typeIcon);
        typeIcon.toBack();

        ranchor = new Circle(5);
        ranchor.setFill(Color.BLACK);
        ranchor.setVisible(false);
        anchorPane.getChildren().add(this.ranchor);

        //type button
        typeButton = new Button();
        ImageView typeImage = new ImageView("/images/link-image.png");
        typeImage.setFitHeight(12);
        typeImage.setFitWidth(18);
        typeImage.setStyle("-fx-background-color: transparent;");
        typeButton.setGraphic(typeImage);
        typeButton.getStyleClass().add("transparent-button-delete");
        typeButton.setVisible(false);
        typeButton.setOnAction(event -> typeDialog());
        typeButton.setOnMouseEntered(event -> {
            deleteButton.setVisible(true);
            deleteButton.setLayoutX(typeButton.getLayoutX()+typeButton.getWidth());
            deleteButton.setLayoutY(typeButton.getLayoutY());
        });
        anchorPane.getChildren().add(typeButton);
        //delete button
        deleteButton = new Button();
        ImageView deleteImage = new ImageView("/images/trash-can-icon.png");
        deleteImage.setFitHeight(12);
        deleteImage.setFitWidth(18);
        deleteImage.setStyle("-fx-background-color: transparent;");
        deleteButton.setGraphic(deleteImage);
        deleteButton.getStyleClass().add("transparent-button-delete");
        deleteButton.setVisible(false);
        deleteButton.setOnAction(event -> Remove(false));
        anchorPane.getChildren().add(deleteButton);
    }

    public void Remove(boolean justView)
    {
        if (!justView) baseController.RemoveRelationshipListener(source.getClassName(), dest.getClassName());
        anchorPane.getChildren().remove(typeIcon);
        anchorPane.getChildren().remove(ranchor);
        anchorPane.getChildren().remove(typeButton);
        anchorPane.getChildren().remove(deleteButton);
        anchorPane.getChildren().remove(this);
    }

    public void mouseMoved(MouseEvent event, Scale scaleTransform)
    {
        deleteButton.setVisible(false);

        if (source == dest) return;

        Point2D nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        double distance = 0;

        for (int i = 0; i < getPoints().size() - 2; i += 2)
        {
            double x1, y1, x2, y2;
            x1 = getPoints().get(i);
            y1 = getPoints().get(i+1);
            x2 = getPoints().get(i+2);
            y2 = getPoints().get(i+3);

            Point2D pointOnSegment = getClosestPointOnSegment(event.getSceneX(), event.getSceneY(), x1, y1, x2, y2);
            distance = pointOnSegment.distance(event.getSceneX(), event.getSceneY()-50);
            
            if (distance < 25 && distance < minDistance)
            {
                minDistance = distance;
                nearestPoint = pointOnSegment;
            }
        }

        if (nearestPoint != null)
        {
            typeButton.setLayoutX(nearestPoint.getX() / scaleTransform.getX());
            typeButton.setLayoutY(nearestPoint.getY() / scaleTransform.getY());

            typeButton.setVisible(true);
        }
        else
        {
            typeButton.setVisible(false);
        }

    }

    public Point2D getClosestPointOnSegment(double mouseX, double mouseY, double x1, double y1, double x2, double y2)
    {
        if (Math.abs(x1 - x2) < 1)
        {
            // Vertical segment: x-coordinate is constant
            double clampedY = Math.max(Math.min(y1, y2), Math.min(mouseY, Math.max(y1, y2)));
            return new Point2D(x1, clampedY - 70);
        }
        else if (Math.abs(y1 - y2) < 1)
        {
            // Horizontal segment: y-coordinate is constant
            double clampedX = Math.max(Math.min(x1, x2), Math.min(mouseX, Math.max(x1, x2)));
            return new Point2D(clampedX - 10, y1);
        }

        return new Point2D(0, 0);
    }

    private void typeDialog()
    {
        if (source == dest) return;

        Scale scaleTransform = null;
        for (Transform transform : anchorPane.getTransforms())
        {
            if (transform instanceof Scale)
            {
                scaleTransform = (Scale) transform;
                break;
            }
        }

        String[] types = {"Aggregation", "Composition", "Generalization", "Realization"};
        JComboBox<String> comboBox = new JComboBox<>(types);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select a relationship type:"));
        panel.add(comboBox);

         int result = JOptionPane.showConfirmDialog(null, panel, "Select a type",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String selectedOption = (String) comboBox.getSelectedItem();
            type = selectedOption;
            typeIcon.setImage(new Image("/images/" + type + ".png"));
            baseController.ChangeRelationshipTypeListener(source.getClassName(), dest.getClassName(), selectedOption);
        } else {
            System.out.println("Dialog was canceled.");
        }

        Update(scaleTransform, true, false, false);
    }

    // saves the relationships between classes into the model using the controller
    public void Save()
    {
        String c1Name = source.getClassName().toLowerCase().trim();
        String c2Name = dest.getClassName().toLowerCase().trim();

        baseController.AddRelationshipListener(c1Name, c2Name, type);
        RelationshipItem r = baseController.getData().getRelationshipItems().get(c1Name + "_" + c2Name);
        r.setSourceX(getSourceOffset().getX());
        r.setSourceY(getSourceOffset().getY());
        r.setDestX(getDestOffset().getX());
        r.setDestY(getDestOffset().getY());
    }

    /*
     * Updates the position of the relation line based on the provided scale transformation.
     * This method calculates the new start and end coordinates of the relation line
     * by transforming the bounds of the anchors of the connected components.
     * The update is then scheduled to run on the JavaFX Application Thread.
     *
     * @param scaleTransform the scale transformation to be applied to the coordinates
     */
    public void Update(Scale scaleTransform, boolean pathfinding, boolean darkMode, boolean partyMode)
    {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                double startX = (source.getLayoutX() + getSourceOffset().getX()) / scaleTransform.getX();
                double startY = ((source.getLayoutY() + getSourceOffset().getY())) / scaleTransform.getY();
                double endX = (dest.getLayoutX() + getDestOffset().getX()) / scaleTransform.getX();
                double endY = ((dest.getLayoutY() + getDestOffset().getY())) / scaleTransform.getY();
                update(scaleTransform, startX, startY, endX, endY, pathfinding, darkMode, partyMode);
            }
        });
    }

    /*
     * Updates the position of the relation line based on the provided scale transformation and mouse event.
     * This is for when you drag a relationship line with a mouse to place it.
     *
     * @param scaleTransform the scale transformation to be applied to the coordinates
     * @param event the mouse event containing the new position
     */
    public void Update(Scale scaleTransform, boolean pathfinding, MouseEvent event, boolean darkMode, boolean partyMode)
    {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                double startX = (source.getLayoutX() + getSourceOffset().getX()) / scaleTransform.getX();
                double startY = ((source.getLayoutY() + getSourceOffset().getY())) / scaleTransform.getY();
                double endX = event.getX() / scaleTransform.getX();
                double endY = event.getY() / scaleTransform.getY();
                update(scaleTransform, startX, startY, endX, endY, pathfinding, darkMode, partyMode);
            }
        });
    }

    /*
     * Updates the points of the relation line based on the given start and end coordinates.
     * The method adjusts the points to ensure the line is drawn correctly between two classboxes.
     *
     * @param startX the starting X coordinate of the line
     * @param startY the starting Y coordinate of the line
     * @param endX the ending X coordinate of the line
     * @param endY the ending Y coordinate of the line
     */
    private void update(Scale scaleTransform, double startX, double startY, double endX, double endY, boolean pathfinding, boolean darkMode, boolean partyMode)
    {
        toBack();
        getPoints().clear();

        double preX = startX, firstX = startX;
        double preY = startY, firstY = startY;
        double postX = endX, lastX = endX;
        double postY = endY, lastY = endY;

        // anchorPane.getChildren().remove(testRect);
        // testRect = new Rectangle(sourceBounds.getMinX(), sourceBounds.getMinY(), sourceBounds.getWidth(), sourceBounds.getHeight());
        // testRect.setStroke(Color.RED);
        // testRect.setFill(Color.TRANSPARENT);
        // anchorPane.getChildren().add(testRect);
        // testRect.toBack();

        //get pre-start
        if (getSourceOffset().getX() < 0)
        {
            firstX = startX - 20;
            preX = startX + 10;
            typeIcon.setRotate(0);
        }
        else if (getSourceOffset().getX() > source.getWidth())
        {
            firstX = startX + 20;
            preX = startX - 10;
            typeIcon.setRotate(180);
        }
        else if (getSourceOffset().getY() < 0)
        {
            firstY = startY - 20;
            preY = startY + 10;
            typeIcon.setRotate(90);
        }
        else if (getSourceOffset().getY() > source.getHeight())
        {
            firstY = startY + 20;
            preY = startY - 10;
            typeIcon.setRotate(-90);
        }

        //get post-end
        if (source != dest)
        {
            if (getDestOffset().getX() < 0)
            {
                lastX = endX - 20;
                postX = endX + 10;
                if (type.equals("Generalization") || type.equals("Realization")) typeIcon.setRotate(0);
            }
            else if (getDestOffset().getX() > dest.getWidth())
            {
                lastX = endX + 20;
                postX = endX - 10;
                if (type.equals("Generalization") || type.equals("Realization"))typeIcon.setRotate(180);
            }
            else if (getDestOffset().getY() < 0)
            {
                lastY = endY - 20;
                postY = endY + 10;
                if (type.equals("Generalization") || type.equals("Realization"))typeIcon.setRotate(90);
            }
            else if (getDestOffset().getY() > dest.getHeight())
            {
                lastY = endY + 20;
                postY = endY - 10;
                if (type.equals("Generalization") || type.equals("Realization"))typeIcon.setRotate(-90);
            }
        }

        //set pre-start point
        getPoints().add(preX);
        getPoints().add(preY);

        //set first real point
        getPoints().add(firstX);
        getPoints().add(firstY);

        //middle =====================================================================================

        if (pathfinding) drawPath(firstX, firstY, lastX, lastY);

        //end ========================================================================================
        //get post-end
        if (source != dest)
        {
            //set final 2 points (dragging a classbox)
            getPoints().add(lastX);
            getPoints().add(lastY);
            getPoints().add(postX);
            getPoints().add(postY);
        }
        else
        {
            //set final point (placing the line with the mouse)
            getPoints().add(lastX);
            getPoints().add(lastY);
        }

        //extra parts
        if (type.equals("Aggregation") || type.equals("Composition"))
        {
            typeIcon.setLayoutX(startX - typeIcon.getImage().getWidth() / 2);
            typeIcon.setLayoutY(startY - typeIcon.getImage().getHeight() / 2);
            ranchor.setCenterX(endX);
            ranchor.setCenterY(endY);
        }
        else
        {
            typeIcon.setLayoutX(endX - typeIcon.getImage().getWidth() / 2);
            typeIcon.setLayoutY(endY - typeIcon.getImage().getHeight() / 2);
            ranchor.setCenterX(startX);
            ranchor.setCenterY(startY);
        }

        typeIcon.toBack();
        ranchor.toBack();
        
        //misc "Aggregation", "Composition", "Generalization", "Realization"
        typeIcon.setVisible(true);
        ranchor.setVisible(true);
        setStrokeWidth(3);

        DropShadow effect = effectHelper(darkMode, partyMode);
        source.setEffect(effect);
        dest.setEffect(effect);

        if(partyMode){
            setStroke(effect.getColor());
        }else{
            setStroke(Color.BLACK);
        }
        
        getStrokeDashArray().clear();
        if (type.equals("Realization"))
            getStrokeDashArray().addAll(10.0, 10.0);
        
    }

    //helper function for resizing a bounds
    public static Bounds modBounds(Bounds bounds, double amount)
    {
        return new BoundingBox(bounds.getMinX() - amount, bounds.getMinY() - amount,
                            bounds.getWidth() + (amount * 2), bounds.getHeight() + (amount * 2));
    }

    // default getters and setters
    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
        typeIcon.setImage(new Image("/images/" + type + ".png"));
    }

    public ClassBox getSource()
    {
        return this.source;
    }

    public ClassBox getDest()
    {
        return this.dest;
    }

    public Point2D getSourceOffset()
    {
        Point2D sourceOffset = new Point2D(this.sourceX, this.sourceY);
        return sourceOffset;
    }

    public Point2D getDestOffset()
    {
        Point2D destOffset = new Point2D(this.destX, this.destY);
        return destOffset;
    }

    public void setSourceOffset(Point2D sourceOffset)
    {
        this.sourceX = sourceOffset.getX();
        this.sourceY = sourceOffset.getY();
    }

    public void setDestOffset(Point2D destOffset)
    {
        this.destX = destOffset.getX();
        this.destY = destOffset.getY();
    }

    /*
     * This method sets the start of the relationship line.
     * 
     * @param classBox the classbox that the relationship line starts from
     * @param index the index of the relationship anchor that the relationship line starts from
     */
    public void setStart(ClassBox classBox, Point2D offset)
    {
        this.source = classBox;
        setSourceOffset(offset);
        this.dest = source;
        setDestOffset(offset);
    }

    /*
     * This method sets the end of the relationship line.
     * 
     * @param classBox the classbox that the relationship line ends at
     * @param index the index of the relationship anchor that the relationship line ends at
     */
    public void setEnd(ClassBox classBox, Point2D offset)
    {
        this.dest = classBox;
        setDestOffset(offset);
    }

    //a-star pathfinding
    private void drawPath(double startX, double startY, double endX, double endY)
    {
        // Convert points to grid coordinates
        int startCol = (int) (startX / CELL_SIZE);
        int startRow = (int) (startY / CELL_SIZE);
        int endCol = (int) (endX / CELL_SIZE);
        int endRow = (int) (endY / CELL_SIZE);

        // Perform a-star pathfinding
        List<Node> path = findPath(startCol, startRow, endCol, endRow);

        if (path != null)
        {
            // Convert path to polyline
            for (Node node : path)
                getPoints().addAll((double) node.x * CELL_SIZE, (double) node.y * CELL_SIZE);
        }
    }

    private List<Node> findPath(int startCol, int startRow, int endCol, int endRow)
    {
        //testing
        for (Rectangle rect : testRects)
        {
            anchorPane.getChildren().remove(rect);
            rect = null;
        }
        testRects.clear();

         // Check if start and end are the same
         if (startCol == endCol && startRow == endRow) {
            // Return a path with just the start node (no pathfinding needed)
            Node startNode = getNode(startCol, startRow, null, 0, endCol, endRow, new HashMap<>(), null);
            return Collections.singletonList(startNode); // Return the start node as the path
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.gCost + n.hCost));
        Map<String, Node> allNodes = new HashMap<>();

        Node startNode = getNode(startCol, startRow, null, 0, endCol, endRow, allNodes, null);
        startNode.direction = null;
        Node endNode = getNode(endCol, endRow, null, 0, endCol, endRow, allNodes, null);
        endNode.direction = null;

        openSet.add(startNode);
        Set<Node> closedSet = new HashSet<>();

        while (!openSet.isEmpty())
        {
            Node current = openSet.poll();

            //System.out.println("openSet: " + openSet.size());
            //System.out.println("closedSet: " + closedSet.size());
            if (closedSet.size() > 10000 || openSet.size() > 10000)
                return null;

            // Check if we've reached the destination
            if (current.equals(endNode))
            {
                return reconstructPath(current);
            }
            closedSet.add(current);

            // Check neighbors
            for (int[] dir : new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}})
            {
                int neighborX = current.x + dir[0];
                int neighborY = current.y + dir[1];
                String newDirection = dir[0] == -1 ? "W" : dir[0] == 1 ? "E" : dir[1] == -1 ? "N" : "S";

                // Skip directions that don't match the current direction unless blocked or overshooting
                if (current.direction != null && !current.direction.equals(newDirection)) {
                    // Allow direction change only if blocked or overshooting
                    if (!isBlocked(current.x + dx(current.direction), current.y + dy(current.direction))
                            && !isOvershooting(current, endCol, endRow)) {
                        continue;
                    }
                }

                // Skip blocked or invalid neighbors
                if (isBlocked(neighborX, neighborY)) continue;

                Node neighbor = getNode(neighborX, neighborY, current, current.gCost + 1, endCol, endRow, allNodes, endNode);
                neighbor.direction = newDirection;

                if (closedSet.contains(neighbor)) continue;

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
            }
        }

        return null; // No path found
    }

    private boolean isOvershooting(Node current, double endCol, double endRow) {
        if (current.direction == null) return false;
    
        switch (current.direction) {
            case "N": return current.y <= endRow;
            case "S": return current.y >= endRow;
            case "E": return current.x >= endCol;
            case "W": return current.x <= endCol;
            default: return false;
        }
    }

    private int dx(String direction) {
        switch (direction) {
            case "W": return -1;
            case "E": return 1;
            default: return 0;
        }
    }

    private int dy(String direction) {
        switch (direction) {
            case "N": return -1;
            case "S": return 1;
            default: return 0;
        }
    }

    private Node getNode(int x, int y, Node parent, double gCost, int endCol, int endRow, Map<String, Node> allNodes, Node endNode)
    {
        String key = x + "," + y;
        Node node = allNodes.get(key);
        if (node == null)
        {
            node = new Node(x, y, gCost, Math.abs(endCol - x) + Math.abs(endRow - y), parent, parent != null ? parent.direction : null);
            allNodes.put(key, node);
        }
        if (node == endNode)
        {
            node.parent = parent;
        }

        //testing
        // Rectangle r = new Rectangle((x * CELL_SIZE), (y * CELL_SIZE), CELL_SIZE, CELL_SIZE);
        // r.setStroke(Color.RED);
        // r.setFill(Color.TRANSPARENT);
        // testRects.add(r);
        // anchorPane.getChildren().add(r);

        return node;
    }

    private boolean isBlocked(int col, int row)
    {
        double x = col * CELL_SIZE;
        double y = row * CELL_SIZE;
        for (javafx.scene.Node node : anchorPane.getChildren())
        {
            if (node instanceof ClassBox)
            {
                Bounds bounds = modBounds(node.getBoundsInParent(), 10);
                if (bounds.intersects(x, y, CELL_SIZE, CELL_SIZE))
                {
                    return true;
                }
            }
            else if (node instanceof RelationLine && node != this)
            {
                Rectangle curNode = new Rectangle(x, y, CELL_SIZE, CELL_SIZE);
                RelationLine line = (RelationLine) node;
                
                ObservableList<Double> points = line.getPoints(); // Get the points of the relation line

                // Iterate through the polyline segments
                for (int i = 0; i < points.size() - 2; i += 2)
                {
                    double x1 = points.get(i);
                    double y1 = points.get(i + 1);
                    double x2 = points.get(i + 2);
                    double y2 = points.get(i + 3);

                    // Create a line for the current segment
                    Line segment = new Line(x1, y1, x2, y2);

                    // Check if the rectangle intersects the segment
                    Shape intersection = Shape.intersect(curNode, segment);
                    if (intersection.getBoundsInLocal().getWidth() > 0 || intersection.getBoundsInLocal().getHeight() > 0)
                        return true; // Rectangle is touching the polyline
                }
            }
        }
        return false;
    }

    private List<Node> reconstructPath(Node node)
    {
        List<Node> path = new ArrayList<>();
        while (node != null)
        {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    static class Node
    {
        int x, y;
        double gCost, hCost;
        Node parent;
        String direction;

        Node(int x, int y, double gCost, double hCost, Node parent, String direction)
        {
            this.x = x;
            this.y = y;
            this.gCost = gCost;
            this.hCost = hCost;
            this.parent = parent;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;

            // int ix = (int) x;
            // int iy = (int) y;
            // int inx = (int) node.x;
            // int iny = (int) node.y;

            // return ix == inx && iy == iny;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(x, y);
        }
    }

    public DropShadow effectHelper(boolean darkMode, boolean partyMode){
        DropShadow shadow = new DropShadow();

        Color color = (darkMode) ? darkColor : lightColor;

        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setRadius(10);

        if(partyMode){
            int max = 255;
            int min = 0;

            int r= (int)(Math.random() * (max - min + 1)) + min;
            int g = (int)(Math.random() * (max - min + 1)) + min;
            int b = (int)(Math.random() * (max - min + 1)) + min;

            color = Color.rgb(r, g, b);
        }

        shadow.setColor(color);

        return shadow;
    }
}
