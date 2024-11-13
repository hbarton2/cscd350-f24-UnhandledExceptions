package com.unhandledexceptions.View;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.unhandledexceptions.Controller.BaseController;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

/*
 * This class is used to create a line between two classes in the GUI.
 * The way it works is you drag from the Ranchor (relationship anchor) of a classbox into the other Ranchor of a class box to create a relationship.
 * You can also draw from a Ranchor to a blank space to create a relationship, which will automatically create a new class as well.
 */

public class RelationLine extends Polyline
{
    // indexes of the classboxes' relationship anchors either 0, 1, 2, or 3
    private int i1, i2;
    // classboxes that the relation line connects
    private ClassBox c1, c2;
    // type of relationship (Aggregation, Composition, Generalization, Realization)
    private String type;

    private Polygon shape;
    BaseController baseController;
    AnchorPane anchorPane;
    Button typeButton;
    Button deleteButton;

    // constructor for relationship line
    public RelationLine(BaseController baseController, AnchorPane anchorPane)
    {
        // toBack() moves the relationship line to the back of the canvas so it doesn't cover the classboxes
        toBack();
        this.anchorPane = anchorPane;
        this.baseController = baseController;
        type = "Composition";
        shape = new Polygon();
        anchorPane.getChildren().add(shape);
        shape.toBack();



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
        if (!justView) baseController.RemoveRelationshipListener(c1.getClassName(), c2.getClassName());
        anchorPane.getChildren().remove(shape);
        anchorPane.getChildren().remove(typeButton);
        anchorPane.getChildren().remove(deleteButton);
        anchorPane.getChildren().remove(this);
    }

    public void mouseMoved(MouseEvent event, Scale scaleTransform)
    {
        deleteButton.setVisible(false);

        if (c1 == c2) return;

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
            
            if (distance < 100 && distance < minDistance)
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
        if (c1 == c2) return;

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
            baseController.ChangeRelationshipTypeListener(c1.getClassName(), c2.getClassName(), selectedOption);
        } else {
            System.out.println("Dialog was canceled.");
        }

        Update(scaleTransform);
    }

    // saves the relationships between classes into the model using the controller
    public void Save()
    {
        baseController.AddRelationshipListener(c1.getClassName(), c2.getClassName(), type);
        baseController.getData().getRelationshipItems().get(c1.getClassName() + "_" + c2.getClassName()).setSourceLoc(i1);
        baseController.getData().getRelationshipItems().get(c1.getClassName() + "_" + c2.getClassName()).setDestLoc(i2);
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
                Bounds bounds = c1.getRanchor(i1).localToScene(c1.getRanchor(i1).getBoundsInLocal());
                Bounds bounds2 = c2.getRanchor(i2).localToScene(c2.getRanchor(i2).getBoundsInLocal());
                double startX = bounds.getCenterX() / scaleTransform.getX();
                double startY = (bounds.getCenterY() - 50) / scaleTransform.getY();
                double endX = bounds2.getCenterX() / scaleTransform.getX();
                double endY = (bounds2.getCenterY() - 50) / scaleTransform.getY();
                update(startX, startY, endX, endY);
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
                Bounds bounds = c1.getRanchor(i1).localToScene(c1.getRanchor(i1).getBoundsInLocal());
                double startX = bounds.getCenterX() / scaleTransform.getX();
                double startY = (bounds.getCenterY() - 50) / scaleTransform.getY();
                double endX = event.getSceneX() / scaleTransform.getX();
                double endY = event.getSceneY() / scaleTransform.getY();
                update(startX, startY, endX, endY);
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
    private void update(double startX, double startY, double endX, double endY)
    {
        toBack();
        getPoints().clear();
        shape.getPoints().clear();

        //start
        if (type.equals("Aggregation") || type.equals("Composition"))
        {
            double aimX = c1.getLayoutX() + c1.getWidth() / 2;
            double aimY = c1.getLayoutY() + c1.getHeight() / 2;
            double angle = Math.atan2(aimY - startY, aimX - startX);

            //type shape
            int offset = 20;
            shape.getPoints().clear();

            shape.getPoints().add(startX - (offset) * Math.cos(angle));
            shape.getPoints().add(startY - (offset) * Math.sin(angle));

            shape.getPoints().add(startX + (offset/2) * Math.cos(angle + Math.PI / 2));
            shape.getPoints().add(startY + (offset/2) * Math.sin(angle + Math.PI / 2));

            shape.getPoints().add(startX + (offset) * Math.cos(angle));
            shape.getPoints().add(startY + (offset) * Math.sin(angle));

            shape.getPoints().add(startX + (offset/2) * Math.cos(angle - Math.PI / 2));
            shape.getPoints().add(startY + (offset/2) * Math.sin(angle - Math.PI / 2));

            startX = startX - (offset) * Math.cos(angle);
            startY = startY - (offset) * Math.sin(angle);
        }
        getPoints().add(startX);
        getPoints().add(startY);

        //middle for top anchor
        if (i1 == 0)
        {
            if (endY > startY)
            {
                double shift = c1.getWidth() / 2;
                if (endX < startX)
                    shift = shift * -1;
                getPoints().add(startX + shift);
                getPoints().add(startY);
            }
            getPoints().add(getPoints().get(getPoints().size() - 2));
            getPoints().add(endY);
        }

        //middle for bottom anchor
        if (i1 == 2)
        {
            if (endY < startY)
            {
                double shift = c1.getWidth() / 2;
                if (endX < startX)
                    shift = shift * -1;
                getPoints().add(startX + shift);
                getPoints().add(startY);
            }
            getPoints().add(getPoints().get(getPoints().size() - 2));
            getPoints().add(endY);
        }

        //middle for left anchor
        if (i1 == 3)
        {
            if (endX > startX)
            {
                double shift = c1.getHeight() / 2;
                if (endY < startY)
                    shift = shift * -1;
                getPoints().add(startX);
                getPoints().add(startY + shift);
            }
            getPoints().add(endX);
            getPoints().add(getPoints().get(getPoints().size() - 2));
        }

        //middle for right anchor
        if (i1 == 1)
        {
            if (endX < startX)
            {
                double shift = c1.getHeight() / 2;
                if (endY < startY)
                    shift = shift * -1;
                getPoints().add(startX);
                getPoints().add(startY + shift);
            }
            getPoints().add(endX);
            getPoints().add(getPoints().get(getPoints().size() - 2));
        }

        //end
        getPoints().add(endX);
        getPoints().add(endY);

        if (c1 != c2)
        {
            if (type.equals("Generalization") || type.equals("Realization"))
            {
                int offset = 10;

                double aimX = c2.getLayoutX() + c2.getWidth() / 2;
                double aimY = c2.getLayoutY() + c2.getHeight() / 2;
                double angle = Math.atan2(aimY - endY, aimX - endX);

                endX = (endX + 5 * Math.cos(angle));
                endY = (endY + 5 * Math.sin(angle));

                getPoints().add(endX);
                getPoints().add(endY);

                //type shape
                shape.getPoints().clear();

                shape.getPoints().add(endX + (offset) * Math.cos(angle + Math.PI / 2));
                shape.getPoints().add(endY + (offset) * Math.sin(angle + Math.PI / 2));

                shape.getPoints().add(endX + (offset) * Math.cos(angle));
                shape.getPoints().add(endY + (offset) * Math.sin(angle));

                shape.getPoints().add(endX + (offset) * Math.cos(angle - Math.PI / 2));
                shape.getPoints().add(endY + (offset) * Math.sin(angle - Math.PI / 2));
            }
            else
            {
                getPoints().add(c2.getLayoutX() + c2.getWidth() / 2);
                getPoints().add(c2.getLayoutY() + c2.getHeight() / 2);
            }
        }

        //misc
        setStrokeWidth(3);
        shape.setStrokeWidth(3);
        shape.setStroke(Color.BLACK);
        shape.setFill(Color.BLACK);
        getStrokeDashArray().clear();
        if (!type.equals("Composition"))
            shape.setFill(Color.TRANSPARENT);
        if (type.equals("Realization"))
            getStrokeDashArray().addAll(10.0, 10.0);
        
    }
    
    // default getters and setters
    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getI1()
    {
        return this.i1;
    }

    public int getI2()
    {
        return this.i2;
    }

    public ClassBox getC1()
    {
        return this.c1;
    }

    public ClassBox getC2()
    {
        return this.c2;
    }

    /*
     * This method sets the start of the relationship line.
     * 
     * @param classBox the classbox that the relationship line starts from
     * @param index the index of the relationship anchor that the relationship line starts from
     */
    public void setStart(ClassBox classBox, int index)
    {
        this.c1 = classBox;
        this.i1 = index;
        this.c2 = c1;
        this.i2 = i1;
    }

    /*
     * This method sets the end of the relationship line.
     * 
     * @param classBox the classbox that the relationship line ends at
     * @param index the index of the relationship anchor that the relationship line ends at
     */
    public void setEnd(ClassBox classBox, int index)
    {
        this.c2 = classBox;
        this.i2 = index;
    }
}
