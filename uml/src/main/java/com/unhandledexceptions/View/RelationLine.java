package com.unhandledexceptions.View;

import javafx.geometry.Bounds;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class RelationLine extends Line
{
    //just do it simple for now
    private Rectangle r1;
    private Rectangle r2;

    public void Update()
    {
        Bounds rbounds = r1.localToScene(r1.getBoundsInLocal());
        setStartX(rbounds.getMinX());
        setStartY(rbounds.getMinY()-25);
        rbounds = r2.localToScene(r2.getBoundsInLocal());
        setEndX(rbounds.getMinX());
        setEndY(rbounds.getMinY()-25);
    }

    public Rectangle getR1()
    {
        return this.r1;
    }

    public void setR1(Rectangle r1)
    {
        this.r1 = r1;
        Bounds rbounds = r1.localToScene(r1.getBoundsInLocal());
        setStartX(rbounds.getMinX());
        setStartY(rbounds.getMinY()-25);
    }

    public Rectangle getR2()
    {
        return this.r2;
    }

    public void setR2(Rectangle r2)
    {
        this.r2 = r2;
        Bounds rbounds = r2.localToScene(r2.getBoundsInLocal());
        setEndX(rbounds.getMinX());
        setEndY(rbounds.getMinY()-25);
    }
}
