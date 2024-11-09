package com.unhandledexceptions.View;

import javafx.scene.layout.VBox;

public interface ClassBoxBuilderInterface {
    void withFieldPane();     // Each Builder and implement a different type of FieldPane
    void withMethodPane();    // Each Builder and implement a different type of MethodPane
    VBox setup();                        // Required setup method, sets up the classbox's orientation
    ClassBox build();                    // Builds and returns the ClassBox
}
