package com.unhandledexceptions.Controller;

import com.unhandledexceptions.View.ClassBox;

public interface ClassBoxEventHandler {
    /*
     * This interface is used to handle events that occur on the class boxes. It is
     * implemented by the mainDiagramController class.
     * It is also used by buttons on the main window to pass information to the
     * controller.
     */

    void onClassBoxClicked(ClassBox classBox);

    void onAddClassClicked();

    void onClassNameClicked(String oldName, String newName, ClassBox classBox);

}
