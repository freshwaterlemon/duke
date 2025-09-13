import java.util.Scanner;
import java.util.ArrayList;

public class Pakipaki {
    private static final String BOTNAME = "PakiPaki";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final String USERGUIDEMSG = """
            Task Commands Overview:

                1. Type 'list' to see all your tasks.
                2. Use 'todo <task>' to add a simple task.
                3. Use 'deadline <task> /by <date/time>' to add a task with a deadline.
                4. Use 'event <task> /from <start date/time> /to <end date/time>' to add an event.
                5. Use 'mark <task>' to mark a task as done.
                6. Use 'unmark <task>' to mark a task as not done.
                7. Type 'bye' to exit the chat.
            """;

    // print the horizontal decoration line for seperation
    private static void printHorzLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    // print start message
    private static void startMsg() {
        printHorzLine();
        String message = """
                Hello! I'm %s, your friendly assistant here to help you manage your tasks.

                %s
                """.formatted(BOTNAME, USERGUIDEMSG);
        System.out.println(message);
        printHorzLine();
    }

    // print end message
    private static void endMsg() {
        System.out.println("    Thank you for chatting with me! Until next time!");
        printHorzLine();
    }

    // print items inside task list and display message if the list is empty.
    private static void printTaskList(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            System.out.println(
                    "   Your task list is empty! Add something and I'll keep it ready for you!");
        } else {
            System.out.println("    Here's what's on your task list so far: " + "(" + taskList.size() + " in total)");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println("        " + (i + 1) + ". " + taskList.get(i));
            }
            System.out.println();
        }
    }

    // find if task is inside the tasklist arraylist
    private static Task findTaskByDescription(String description, ArrayList<Task> taskList, boolean doneStatus,
            boolean findFromEnd) {
        // unmark from the end of list as newer task should be unmark first
        int startFrom, endAt, step;

        if (findFromEnd) {
            startFrom = taskList.size() - 1;
            endAt = -1;
            step = -1;
        } else {
            startFrom = 0;
            endAt = taskList.size();
            step = 1;
        }

        for (int i = startFrom; i != endAt; i += step) {
            Task task = taskList.get(i);
            // add in && task.getIsDone() == doneStatus to handle duplicate task
            // user decide to do the same task again after it is done
            // check isDone in task obj, continue if task is found but already mark as done
            if (task.getDescription().equalsIgnoreCase(description) && task.getIsDone() == doneStatus) {
                return task;
            }
        }
        return null;
    }

    // handle marking of task as done
    private static void handleMark(String taskString, ArrayList<Task> taskList) {
        Task task = findTaskByDescription(taskString, taskList, false, false);
        if (task != null) {
            task.markAsDone();
            System.out.println("    Alright! \"" + taskString + "\" mark as done!");
            System.out.println("        " + task + "\n");
        } else {
            System.out.println(taskString + " not found or all matching tasks are already marked as done.\n");
        }
    }

    // handle unmarking of task as undone
    private static void handleUnmark(String taskString, ArrayList<Task> taskList) {
        Task task = findTaskByDescription(taskString, taskList, true, true);
        if (task != null) {
            task.markAsUndone();
            System.out.println("    Alright! \"" + taskString + "\" unmark.");
            System.out.println("        " + task + "\n");
        } else {
            System.out.println("    " + taskString + " not found or all matching tasks are already unmarked.\n");
        }
    }

    // add task object to arraylist of task and display confirmation message
    private static void addTask(Task task, ArrayList<Task> taskList) {
        taskList.add(task);
        System.out.println("    Got it, task added to your task list.");
        System.out.println("        " + task.toString() + "\n");
        System.out.println("    Now you have " + taskList.size() + " tasks in the list.\n");
    }

    // handle to do
    private static void handleToDo(String description, ArrayList<Task> taskList) {
        addTask(new Todo(description), taskList);
    }

    // handle deadline
    private static void handleDeadline(String description, ArrayList<Task> taskList) {
        int findIndex = description.indexOf("/by");
        if (findIndex == -1) {
            System.out.println("    Oops! The deadline detail is missing");
            System.out.println("    Please use the format: deadline <task> /by <date/time>");
            return;
        }
        String deadlineDescription = description.substring(0, findIndex).trim();
        String deadlineTiming = description.substring(findIndex + 3).trim();
        addTask(new Deadline(deadlineDescription, deadlineTiming), taskList);

    }

    // handle event
    private static void handleEvent(String description, ArrayList<Task> taskList) {
        int findIndexFrom = description.indexOf("/from");
        int findIndexTo = description.indexOf("/to");
        // user must include both from and to
        if (findIndexFrom == -1 || findIndexTo == -1 || findIndexFrom > findIndexTo) {
            System.out.println("    Oops! The event detail is missing");
            System.out.println("    Please use the format: event <task> /from <date/time> /to <date/time>");
            return;
        }
        String eventDescription = description.substring(0, findIndexFrom).trim();
        String eventTimingFrom = description.substring(findIndexFrom + 5, findIndexTo).trim();
        String eventTimingTo = description.substring(findIndexTo + 3).trim();
        addTask(new Event(eventDescription, eventTimingFrom, eventTimingTo), taskList);
    }

    // get user input and display them
    public static void handleUserInput(Scanner in) {

        // arraylist of task object to store all task dynamically
        ArrayList<Task> taskList = new ArrayList<Task>();
        // loop for user input
        while (true) {
            try {
                System.out.println();
                String userInput = in.nextLine().trim();

                if (userInput.isEmpty()) {
                    System.out.println("    (Oops! You didn't type anything. Go ahead, give me a task!)\n");
                    continue;
                }

                // get the first word in the sentance
                String command = userInput.split(" ")[0].toLowerCase();
                // get everything after first word if there are more after first word
                String taskString = userInput.length() > command.length() ? userInput.substring(command.length()).trim()
                        : "";

                // Check if command is one that doesn't need arguments
                if (!(command.equals("list") || command.equals("bye"))) {
                    // For all other commands, arguments must not be empty
                    if (taskString.isEmpty()) {
                        System.out.println("    Details after '" + command + "' cannot be empty.");
                        continue;
                    }
                }

                switch (command) {
                    case "list":
                        printTaskList(taskList);
                        break;

                    case "bye":
                        endMsg();
                        return;

                    case "mark":
                        handleMark(taskString, taskList);
                        break;

                    case "unmark":
                        handleUnmark(taskString, taskList);
                        break;

                    case "todo":
                        handleToDo(taskString, taskList);
                        break;

                    case "deadline":
                        handleDeadline(taskString, taskList);
                        break;

                    case "event":
                        handleEvent(taskString, taskList);
                        break;

                    default:
                        String unknownCommandMsg = """
                                    Sorry, I do not quite get that.

                                    %s
                                """.formatted(USERGUIDEMSG);
                        System.out.println(unknownCommandMsg);
                        break;
                }
            } catch (Exception e) {
                System.out.println("    Sorry, something went wrong processing your input. Please try again.");
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        startMsg();
        handleUserInput(in);
        in.close();
    }
}
