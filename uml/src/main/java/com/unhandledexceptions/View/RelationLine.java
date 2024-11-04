package com.unhandledexceptions.View;

import com.unhandledexceptions.Controller.BaseController;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
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

    // constructor for relationship line without type
    public RelationLine()
    {
        // toBack() moves the relationship line to the back of the canvas so it doesn't cover the classboxes
        toBack();
        // TODO: change this to take in the 4 types, not just hardcode the type
        type = null;
    }

    // overloaded constructor for adding a type to a relationship
    public RelationLine(String type) {
        this.type = type;
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
        double startX = bounds.getMinX() / scaleTransform.getX();
        double startY = (bounds.getMinY() - 25) / scaleTransform.getY();
        double endX = bounds2.getMinX() / scaleTransform.getX();
        double endY = (bounds2.getMinY() - 25) / scaleTransform.getY();

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
        double startX = bounds.getMinX() / scaleTransform.getX();
        double startY = (bounds.getMinY() - 25) / scaleTransform.getY();
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

        //start
        getPoints().add(c1.getLayoutX() + c1.getWidth() / 2);
        getPoints().add(c1.getLayoutY() + c1.getHeight() / 2);
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
            getPoints().add(c2.getLayoutX() + c2.getWidth() / 2);
            getPoints().add(c2.getLayoutY() + c2.getHeight() / 2);
        }
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
