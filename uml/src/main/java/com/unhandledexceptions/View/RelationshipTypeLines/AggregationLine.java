package com.unhandledexceptions.View.RelationshipTypeLines;

import com.unhandledexceptions.View.RelationLine;

import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class AggregationLine extends RelationLine {
  // constructor for relationship line with type aggregation
  public AggregationLine() {
    super("Aggregation");
  }

  /*
   * Creates a diamond-shaped polygon to represent an aggregation relationship in a UML diagram.
   * The diamond is defined by four points and is filled with white color and outlined with black color.
   *
   * @return a Polygon object representing the diamond shape.
   */
  private Polygon createDiamond() {
    Polygon diamond = new Polygon();
        diamond.getPoints().addAll(0.0, 0.0, 10.0, 5.0, 0.0, 10.0, -10.0, 5.0);
        diamond.setStroke(Color.BLACK);
        diamond.setFill(Color.WHITE);
        return diamond;
  }
}

