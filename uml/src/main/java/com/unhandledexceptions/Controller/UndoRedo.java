package com.unhandledexceptions.Controller;

import java.util.ArrayDeque;
import java.util.Deque;

public class UndoRedo
{
    private final int maxSize = 100;
    private final Deque<String> undo;
    private final Deque<String> redo;

    public UndoRedo()
    {
        this.undo = new ArrayDeque<>(maxSize);
        this.redo = new ArrayDeque<>(maxSize);
    }

    //every command in BaseController needs to add the string here
    public void Add(String command)
    {
        if (undo.size() == maxSize)
            undo.removeFirst();
        
        undo.addLast(command);
    }

    public void Undo()
    {
        //String lastCommand = undo.getLast();

        //make a string command that undoes what the lastCommand did
        //and send it to CLI.CommandParsing.
        //Example: lastCommand = addclass c1
        //String undoCommand = removeclass c1
        //CLI.CommandParsing(undoCommand);
    }

    public void Redo()
    {
        //??
    }
}