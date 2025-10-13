package tasklist;

import java.util.ArrayList;

import exception.NeruneruneException;
import parser.Parser;
import storage.Storage;
import task.Task;
import ui.Ui;

public class TaskList {
    private final ArrayList<Task> taskList;
    private final Storage storage;
    private final Ui ui;

    public TaskList(Storage storage, Ui ui) {
        this.storage = storage;
        this.ui = ui;
        this.taskList = new ArrayList<>();
    }

    // load tasks from storage
    public void loadTasks() {
        storage.handleStorage(taskList);
    }

    // save the current task list
    public void saveTasks() {
        storage.saveTasksToStorage(getTaskList());
    }

    // print tasklist
    public void listTasks() {
        ui.printTaskList(taskList);
    }

    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    // add task object to arraylist of task and display confirmation message
    public void addTask(Task task) {
        taskList.add(task);
        ui.printMessage(("Got it, task added to your task list.").indent(4));
        ui.printMessage((task.toString() + "\n").indent(8));
        ui.printMessage(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));
    }

    // handle marking of task as done
    public void markTask(String taskString) {
        try {
            Task task = findTaskByDescription(taskString, false, false);
            task.markAsDone();
            ui.printMessage(("Alright! \"" + taskString + "\" mark as done!").indent(4));
            ui.printMessage((task + "\n").indent(8));
        } catch (NeruneruneException e) {
            ui.printMessage((e.getMessage() + "\n").indent(4));
        }
    }

    // handle unmarking of task as undone
    public void unmarkTask(String taskString) {
        try {
            Task task = findTaskByDescription(taskString, true, true);
            task.markAsUndone();
            ui.printMessage(("Alright! \"" + taskString + "\" unmark.").indent(4));
            ui.printMessage((task + "\n").indent(8));
        } catch (NeruneruneException e) {
            ui.printMessage((e.getMessage() + "\n").indent(4));
        }
    }

    // delete task object from arraylist of task and display confirmation message
    public void deleteTask(String description) throws NeruneruneException {
        try {
            Task task = findTaskByDescription(description);
            taskList.remove(task);
            ui.printMessage(("Got it, task removed from your task list.").indent(4));
            ui.printMessage((task.toString() + "\n").indent(8));
            ui.printMessage(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));

        } catch (NeruneruneException e) {
            ui.printMessage(e.getMessage());
        }
    }

    // find if task is inside the tasklist arraylist
    private Task findTaskByDescription(String description, boolean doneStatus, boolean findFromEnd)
            throws NeruneruneException {
        // unmark from the end of list as newer task should be unmark first

        // find from end if true
        int startFrom = findFromEnd ? taskList.size() - 1 : 0;
        int endAt = findFromEnd ? -1 : taskList.size();
        int step = findFromEnd ? -1 : 1;

        for (int i = startFrom; i != endAt; i += step) {
            Task task = taskList.get(i);
            // user decide to do the same task again after it is done
            // check isDone in task obj, continue if task is found but already mark as done
            if (task.getDescription().equalsIgnoreCase(description) && task.getIsDone() == doneStatus) {
                return task;
            }
        }
        // return null;
        throw new NeruneruneException(
                "Task with description \"" + description + "\" not found.");

    }

    /*
     * find if task is inside the tasklist arraylist using
     * description or by using index
     */
    private Task findTaskByDescription(String description) throws NeruneruneException {
        try {
            int taskIndex = Integer.parseInt(description) - 1;
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                throw new NeruneruneException("No task number: " + (taskIndex + 1));
            }
            return taskList.get(taskIndex);
        } catch (NumberFormatException e) {
            // no number provided, fall back to description search
        }

        for (Task task : taskList) {
            if (task.getDescription().equalsIgnoreCase(description)) {
                return task;
            }
        }

        throw new NeruneruneException("Task with description \"" + description + "\" not found.");
    }

    // handle to do
    public void addTodo(String description) {
        try {
            Task todoTask = Parser.parseTodo(description);
            addTask(todoTask);
        } catch (Exception e) {
            ui.printMessage(("Error parsing todo: " + e.getMessage()).indent(4));
        }
    }

    // handle deadline
    public void addDeadline(String description) {
        try {
            Task deadlineTask = Parser.parseDeadline(description);
            addTask(deadlineTask);
        } catch (Exception e) {
            ui.printMessage(("Error parsing deadline: " + e.getMessage()).indent(4));
            ui.printMessage(("Please use the format: deadline <task> /by <date/time>").indent(4));
        }
    }

    // handle event
    public void addEvent(String description) {
        try {
            Task eventTask = Parser.parseEvent(description);
            addTask(eventTask);
        } catch (Exception e) {
            ui.printMessage(("Error parsing event: " + e.getMessage()).indent(4));
            ui.printMessage(("Please use the format: event <task> /from <date/time> /to <date/time>").indent(4));
        }
    }

}
