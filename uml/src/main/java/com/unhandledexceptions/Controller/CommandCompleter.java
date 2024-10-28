package com.unhandledexceptions.Controller;

import jline.console.completer.Completer;
import java.util.List;

public class CommandCompleter implements Completer {

  // List of commands to auto complete
  private List<String> commands;

  // Constructor for command completer
  public CommandCompleter(List<String> commands) {

    // checks to see if commands is null
    if (commands == null) {
      throw new IllegalArgumentException("commands cannot be null");
    }

    // assigns the list of commands to auto complete
    this.commands = commands;
  }

  // We need to have this method because complete is abstract in Completer
  /*
   * Complete adds all commands as a candidate for tab auto completion if the buffer is null,
   * AKA the user hasn't typed anything into the command line.
   * If the buffer isn't null, we see if a command starts with what's in the buffer and if it matches,
   * we add the command to the candidates list.
   * If the candidate list is empty, return -1, else return 0.
   */
  @Override
  public int complete(String buffer, int cursor, List<CharSequence> candidates) {
    if (buffer == null) {
        candidates.addAll(commands);
    } else {
        for (String command : commands) {
            if (command.startsWith(buffer)) {
                candidates.add(command);
            }
        }
    }
    return candidates.isEmpty() ? -1 : 0;
}
}
