package com.unhandledexceptions.View;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.unhandledexceptions.Controller.BaseController;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Scale;

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
    
    // constructor for relationship line
    public RelationLine(BaseController baseController, AnchorPane anchorPane)
    {
        // toBack() moves the relationship line to the back of the canvas so it doesn't cover the classboxes
        toBack();
        this.baseController = baseController;
        type = "Composition";
        shape = new Polygon();
        anchorPane.getChildren().add(shape);
        shape.toBack();
        setOnMouseClicked(event -> mouseClicked(event));
    }

    public void Remove(AnchorPane anchorPane)
    {
        anchorPane.getChildren().remove(shape);
        anchorPane.getChildren().remove(this);
    }

    private void mouseClicked(MouseEvent event)
    {
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
        } else {
            System.out.println("Dialog was canceled.");
        }

        Save(baseController);
        Update(new Scale());
    }

    // saves the relationships between classes into the model using the controller
    public void Save(BaseController controller)
    {
        controller.AddRelationshipListener(c1.getClassName(), c2.getClassName(), type);
        controller.PlaceRelationshipListener(c1.getClassName(), c2.getClassName(), i1, i2);
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
        Bounds bounds = c1.getRanchor(i1).localToScene(c1.getRanchor(i1).getBoundsInLocal());
        Bounds bounds2 = c2.getRanchor(i2).localToScene(c2.getRanchor(i2).getBoundsInLocal());
        double startX = bounds.getCenterX() / scaleTransform.getX();
        double startY = (bounds.getCenterY() - 25) / scaleTransform.getY();
        double endX = bounds2.getCenterX() / scaleTransform.getX();
        double endY = (bounds2.getCenterY() - 25) / scaleTransform.getY();

        Platform.runLater(new Runnable() {
            @Override public void run() {
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
        Bounds bounds = c1.getRanchor(i1).localToScene(c1.getRanchor(i1).getBoundsInLocal());
        double startX = bounds.getCenterX() / scaleTransform.getX();
        double startY = (bounds.getCenterY() - 25) / scaleTransform.getY();
        double endX = event.getSceneX() / scaleTransform.getX();
        double endY = event.getSceneY() / scaleTransform.getY();

        Platform.runLater(new Runnable() {
            @Override public void run() {
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
        setStrokeWidth(2);
        shape.setStrokeWidth(2);
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
