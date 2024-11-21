package com.unhandledexceptions.View;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.unhandledexceptions.Controller.BaseController;

import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

/*
 * This class is used to create a line between two classes in the GUI.
 * The way it works is you drag from the Ranchor (relationship anchor) of a classbox into the other Ranchor of a class box to create a relationship.
 * You can also draw from a Ranchor to a blank space to create a relationship, which will automatically create a new class as well.
 */

public class RelationLine extends Polyline
{
    private Point2D sourceOffset, destOffset;
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
        ranchor.toBack();

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

    public void mouseMoved(MouseEvent event)
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
            distance = pointOnSegment.distance(event.getSceneX(), event.getSceneY());
            
            if (distance < 100 && distance < minDistance)
            {
                minDistance = distance;
                nearestPoint = pointOnSegment;
            }
        }

        if (nearestPoint != null)
        {
            typeButton.setLayoutX(nearestPoint.getX());
            typeButton.setLayoutY(nearestPoint.getY());

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
            return new Point2D(x1, clampedY - 35);
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

        Update(scaleTransform);
    }

    // saves the relationships between classes into the model using the controller
    public void Save()
    {
        String c1Name = source.getClassName().toLowerCase().trim();
        String c2Name = dest.getClassName().toLowerCase().trim();

        baseController.AddRelationshipListener(c1Name, c2Name, type);
        baseController.getData().getRelationshipItems().get(c1Name + "_" + c2Name).setSourceLoc(sourceOffset);
        baseController.getData().getRelationshipItems().get(c1Name + "_" + c2Name).setDestLoc(destOffset);
    }

    /*
     * Updates the position of the relation line based on the provided scale transformation.
     * This method calculates the new start and end coordinates of the relation line
     * by transforming the bounds of the anchors of the connected components.
     * The update is then scheduled to run on the JavaFX Application Thread.
     *
     * @param scaleTransform the scale transformation to be applied to the coordinates
     */
    public void Update(Scale scaleTransform)
    {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                double startX = (source.getLayoutX() + sourceOffset.getX()) / scaleTransform.getX();
                double startY = ((source.getLayoutY() + sourceOffset.getY())) / scaleTransform.getY();
                double endX = (dest.getLayoutX() + destOffset.getX()) / scaleTransform.getX();
                double endY = ((dest.getLayoutY() + destOffset.getY())) / scaleTransform.getY();
                update(scaleTransform, startX, startY, endX, endY);
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
    public void Update(Scale scaleTransform, MouseEvent event)
    {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                double startX = (source.getLayoutX() + sourceOffset.getX()) / scaleTransform.getX();
                double startY = ((source.getLayoutY() + sourceOffset.getY())) / scaleTransform.getY();
                double endX = event.getX() / scaleTransform.getX();
                double endY = event.getY() / scaleTransform.getY();
                update(scaleTransform, startX, startY, endX, endY);
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
    private void update(Scale scaleTransform, double startX, double startY, double endX, double endY)
    {
        toBack();
        getPoints().clear();

        double preX = startX, firstX = startX;
        double preY = startY, firstY = startY;
        double postX = endX, lastX = endX;
        double postY = endY, lastY = endY;
        Bounds sourceBounds = modBounds(source.getBoundsInParent(), 35);
        Bounds destBounds = modBounds(dest.getBoundsInParent(), 35);

        // Rectangle rect = new Rectangle(sourceBounds.getMinX(), sourceBounds.getMinY(), sourceBounds.getWidth(), sourceBounds.getHeight());
        // rect.setStroke(Color.RED);
        // rect.setFill(Color.TRANSPARENT);
        // anchorPane.getChildren().add(rect);
        // rect.toBack();

        //get pre-start
        if (sourceOffset.getX() < 0)
        {
            firstX = startX - 20;
            preX = startX + 10;
            typeIcon.setRotate(0);
        }
        else if (sourceOffset.getX() > source.getWidth())
        {
            firstX = startX + 20;
            preX = startX - 10;
            typeIcon.setRotate(180);
        }
        else if (sourceOffset.getY() < 0)
        {
            firstY = startY - 20;
            preY = startY + 10;
            typeIcon.setRotate(90);
        }
        else if (sourceOffset.getY() > source.getHeight())
        {
            firstY = startY + 20;
            preY = startY - 10;
            typeIcon.setRotate(-90);
        }

        //get post-end
        if (source != dest)
        {
            if (destOffset.getX() < 0)
            {
                lastX = endX - 20;
                postX = endX + 10;
                if (type.equals("Generalization") || type.equals("Realization")) typeIcon.setRotate(0);
            }
            else if (destOffset.getX() > dest.getWidth())
            {
                lastX = endX + 20;
                postX = endX - 10;
                if (type.equals("Generalization") || type.equals("Realization"))typeIcon.setRotate(180);
            }
            else if (destOffset.getY() < 0)
            {
                lastY = endY - 20;
                postY = endY + 10;
                if (type.equals("Generalization") || type.equals("Realization"))typeIcon.setRotate(90);
            }
            else if (destOffset.getY() > dest.getHeight())
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
        //get source intersection points, if any
        Point2D[] sourcePath = getPathAroundBounds(
            new Point2D(firstX, firstY), new Point2D(lastX, lastY), sourceBounds);
        
        //TODO: weird patchest on left and right that dont trigger
        //route around source classbox
        if (sourcePath != null)
        {
            for (Point2D point : sourcePath)
            {
                if (point == null) continue;
                getPoints().add(point.getX());
                getPoints().add(point.getY());
            }
        }

        if (source != dest)
        {
            //TODO: dest is checking from first to last but it should check from whatever just happened to last
            //get dest intersection points, if any
            Point2D[] destPoints = getPathAroundBounds(
                new Point2D(firstX, firstY), new Point2D(lastX, lastY), destBounds);

            //route around dest classbox
            if (destPoints != null)
            {
                for (Point2D point : destPoints)
                {
                    if (point == null) continue;
                    getPoints().add(point.getX());
                    getPoints().add(point.getY());
                }
            }
        }

        //end =====================================================================================
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
        
        //misc "Aggregation", "Composition", "Generalization", "Realization"
        typeIcon.setVisible(true);
        ranchor.setVisible(true);
        setStrokeWidth(3);
        getStrokeDashArray().clear();
        if (type.equals("Realization"))
            getStrokeDashArray().addAll(10.0, 10.0);
        
    }

    //helper function for resizing a bounds
    private Bounds modBounds(Bounds bounds, double amount)
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
        return this.sourceOffset;
    }

    public Point2D getDestOffset()
    {
        return this.destOffset;
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
        this.sourceOffset = offset;
        this.dest = source;
        this.destOffset = offset;
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
        this.destOffset = offset;
    }

    /*
     * this method returns both points a line intersects a rectangle (if it does)
     * or null if it doesn't intersect
     */
    public static Point2D[] getPathAroundBounds(Point2D lineStart, Point2D lineEnd, Bounds bounds)
     {
        Point2D[] hits = new Point2D[2]; // At most 2 intersection points

        // Rectangle edges as line segments
        Point2D topLeft = new Point2D(bounds.getMinX(), bounds.getMinY());
        Point2D topRight = new Point2D(bounds.getMaxX(), bounds.getMinY());
        Point2D bottomLeft = new Point2D(bounds.getMinX(), bounds.getMaxY());
        Point2D bottomRight = new Point2D(bounds.getMaxX(), bounds.getMaxY());
        boolean top = false, left = false, right = false, bottom = false;

        // Check intersection with each edge
        int count = 0;
        if (count < 2 && (hits[count] = lineSegmentIntersection(lineStart, lineEnd, topLeft, topRight)) != null) { count++; top = true; }
        if (count < 2 && (hits[count] = lineSegmentIntersection(lineStart, lineEnd, topRight, bottomRight)) != null) { count++; right = true; }
        if (count < 2 && (hits[count] = lineSegmentIntersection(lineStart, lineEnd, bottomRight, bottomLeft)) != null) { count++; bottom = true; }
        if (count < 2 && (hits[count] = lineSegmentIntersection(lineStart, lineEnd, bottomLeft, topLeft)) != null) { count++; left = true; }

        //result
        Point2D[] result = new Point2D[2];

        //handle only 1 edge hit
        if (hits[1] == null)
        {
            // if (top) { result[0] = topLeft; result[1] = bottomLeft; }
            // if (left) { result[0] = topLeft; result[1] = topRight; }
            // if (right) { result[0] = bottomRight; result[1] = bottomLeft; }
            // if (bottom) { result[0] = bottomRight; result[1] = topRight; }

            return result;
        }

        //make sure first intersection is actually first
        if (hits[0].distance(lineStart) > hits[1].distance(lineStart))
        {
            Point2D swap = hits[0];
            hits[0] = hits[1];
            hits[1] = swap;
        }

        //intersection though parallel sides
        if (top && bottom) 
        {
            double leftDistance = (hits[0].getX() - topLeft.getX()) + (hits[1].getX() - topLeft.getX());
            double rightDistance = (topRight.getX() - hits[0].getX()) + (topRight.getX() - hits[1].getX());
            if (leftDistance < rightDistance)
            {
                result[0] = new Point2D(topLeft.getX(), hits[0].getY());
                result[1] = new Point2D(topLeft.getX(), hits[1].getY());
            }
            else
            {
                result[0] = new Point2D(topRight.getX(), hits[0].getY());
                result[1] = new Point2D(topRight.getX(), hits[1].getY());
            }

            return result;
        }
        if (left && right) 
        {
            double topDistance = (hits[0].getY() - topLeft.getY()) + (hits[1].getY() - topLeft.getY());
            double bottomDistance = (bottomLeft.getY() - hits[0].getY()) + (bottomLeft.getY() - hits[1].getY());
            if (topDistance < bottomDistance)
            {
                result[0] = new Point2D(hits[0].getX(), topLeft.getY());
                result[1] = new Point2D(hits[1].getX(), topLeft.getY());
            }
            else
            {
                result[0] = new Point2D(hits[0].getX(), bottomLeft.getY());
                result[1] = new Point2D(hits[1].getX(), bottomLeft.getY());
            }

            return result;
        }

        //intersection though connected sides
        if (top && left) { result[0] = topLeft; }
        if (top && right) { result[0] = topRight; }
        if (bottom && left) { result[0] = bottomLeft; }
        if (bottom && right) { result[0] = bottomRight; }
        
        return result;
    }

    /*
     * helper for findLineRectangleIntersections
     * returns the point where a line intersects another, if it does
     * null if it doesn't
     */
    private static Point2D lineSegmentIntersection(Point2D p1, Point2D p2, Point2D p3, Point2D p4)
    {
        // Line (p1, p2) and line (p3, p4)
        double a1 = p2.getY() - p1.getY();
        double b1 = p1.getX() - p2.getX();
        double c1 = a1 * p1.getX() + b1 * p1.getY();

        double a2 = p4.getY() - p3.getY();
        double b2 = p3.getX() - p4.getX();
        double c2 = a2 * p3.getX() + b2 * p3.getY();

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) 
        {// Lines are parallel
            return null;
        }

        // Intersection point
        double x = (b2 * c1 - b1 * c2) / determinant;
        double y = (a1 * c2 - a2 * c1) / determinant;

        Point2D intersection = new Point2D(x, y);

        // Check if the intersection is within both line segments
        if (isBetween(p1, p2, intersection) && isBetween(p3, p4, intersection)) 
        {
            return intersection;
        }

        return null;
    }

    /*
     * helper for lineSegmentIntersection
     * returns true if point is between the line formed by start and end
     */
    private static boolean isBetween(Point2D start, Point2D end, Point2D point)
    {
        double crossProduct = (point.getX() - start.getX()) * (end.getY() - start.getY()) -
                              (point.getY() - start.getY()) * (end.getX() - start.getX());
        if (Math.abs(crossProduct) > 1e-6) return false; // Not collinear

        double dotProduct = (point.getX() - start.getX()) * (end.getX() - start.getX()) +
                            (point.getY() - start.getY()) * (end.getY() - start.getY());
        if (dotProduct < 0) return false;

        double squaredLengthBA = (end.getX() - start.getX()) * (end.getX() - start.getX()) +
                                 (end.getY() - start.getY()) * (end.getY() - start.getY());
        return dotProduct <= squaredLengthBA;
    }
}
