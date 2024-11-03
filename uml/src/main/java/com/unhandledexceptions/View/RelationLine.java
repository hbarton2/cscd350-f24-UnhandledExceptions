package com.unhandledexceptions.View;

import com.unhandledexceptions.Controller.BaseController;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Scale;

public class RelationLine extends Polyline
{
    //just do it simple for now
    private int i1, i2;
    private ClassBox c1, c2;
    private String type;

    public RelationLine()
    {
        toBack();
        type = "aggregation";
    }

    public void Save(BaseController controller)
    {
        controller.AddRelationshipListener(c1.getClassName(), c2.getClassName(), type);
        controller.PlaceRelationshipListener(c1.getClassName(), c2.getClassName(), i1, i2);
    }

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

    public void setStart(ClassBox classBox, int index)
    {
        this.c1 = classBox;
        this.i1 = index;
        this.c2 = c1;
        this.i2 = i1;
    }

    public void setEnd(ClassBox classBox, int index)
    {
        this.c2 = classBox;
        this.i2 = index;
    }
}
