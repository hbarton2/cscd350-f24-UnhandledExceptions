package com.unhandledexceptions.Controller;

import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.Data.Memento;

import java.util.Stack;
/*
 * This is the Caretaker class for the Memento design pattern. It is responsible for storing the memento objects.
 * It is also responsible for restoring the state of the Data object.
 */
public class Caretaker {
    // Data is a field because our caretaker will only manage one Data object throughout the program
    // Because of this, tight coupling between Data and Caretaker seems okay.
    // undoStack and redoStack are stacks that store the memento objects
    private Data data;
    private Stack<Memento> undoStack;
    private Stack<Memento> redoStack;

    // Constructor
    public Caretaker(Data data) {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.data = data;
    }

    // Save the current state of the Data object
    public void saveState() {
        undoStack.push(this.data.createMemento());
        redoStack.clear(); // Clear redo stack whenever a new state is saved
    }

    // undo the last action
    // if the undo stack is empty we do nothing since there isn't a state we can undo
    public void undo() {
        if (!undoStack.isEmpty()) {
            // move the "undo" to the "redo"
            redoStack.push(this.data.createMemento());
            // restore the state of the Data object from the redo
            this.data.restoreFromMemento(undoStack.pop());
        }
    }

    // redo the last undo
    public void redo() {
        if (!redoStack.isEmpty()) {
            // move the "redo" to the "undo"
            undoStack.push(this.data.createMemento());
            // restore the state of the Data object from the redo
            this.data.restoreFromMemento(redoStack.pop());
        }
    }

    // getters for the stacks
    // ONLY for testing, I don't see a reason we need to call these outside of unit tests
    public Stack<Memento> getUndoStack() {
        return this.undoStack;
    }

    public Stack<Memento> getRedoStack() {
        return this.redoStack;
    }
}
