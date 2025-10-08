import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Pakipaki {
    private static final String DEFAULT_STORAGE_FILEPATH = "data/tasks.txt";
    private static final String STORAGE_ARCHIVE_FILENAME = "tasksArchive.txt";
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
        System.out.println(("Thank you for chatting with me! Until next time!").indent(4));
        printHorzLine();
    }

    // print items inside task list and display message if the list is empty.
    private static void printTaskList(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            System.out.println(("Your task list is empty! Add something and I'll keep it ready for you!").indent(4));
        } else {
            System.out.println(
                    ("Here's what's on your task list so far: " + "(" + taskList.size() + " in total)").indent(4));
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println((((i + 1) + ". " + taskList.get(i)).indent(8)));
            }
            System.out.println();
        }
    }

    // find if task is inside the tasklist arraylist
    private static Task findTaskByDescription(String description, ArrayList<Task> taskList, boolean doneStatus,
            boolean findFromEnd) throws PakipakiException {
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
        // return null;
        throw new PakipakiException(
                "Task with description \"" + description + "\" not found.");

    }

    /*
     * find if task is inside the tasklist arraylist using
     * description or by using index
     */
    private static Task findTaskByDescription(String description, ArrayList<Task> taskList) throws PakipakiException {
        try {
            int taskIndex = Integer.parseInt(description) - 1;
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                throw new PakipakiException("No task number: " + (taskIndex + 1));
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

        throw new PakipakiException("Task with description \"" + description + "\" not found.");
    }

    // handle marking of task as done
    private static void handleMark(String taskString, ArrayList<Task> taskList) {
        try {
            Task task = findTaskByDescription(taskString, taskList, false, false);
            task.markAsDone();
            System.out.println(("Alright! \"" + taskString + "\" mark as done!").indent(4));
            System.out.println((task + "\n").indent(8));
            saveTasksToStorage(taskList); // mark and save new tasklist to tasks.txt
        } catch (PakipakiException e) {
            System.out.println((e.getMessage() + "\n").indent(4));
        }
    }

    // handle unmarking of task as undone
    private static void handleUnmark(String taskString, ArrayList<Task> taskList) {
        try {
            Task task = findTaskByDescription(taskString, taskList, true, true);
            task.markAsUndone();
            System.out.println(("Alright! \"" + taskString + "\" unmark.").indent(4));
            System.out.println((task + "\n").indent(8));
            saveTasksToStorage(taskList); // unmark and save new tasklist to tasks.txt
        } catch (PakipakiException e) {
            System.out.println((e.getMessage() + "\n").indent(4));
        }
    }

    // add task object to arraylist of task and display confirmation message
    private static void addTask(Task task, ArrayList<Task> taskList) {
        taskList.add(task);
        System.out.println(("Got it, task added to your task list.").indent(4));
        System.out.println((task.toString() + "\n").indent(8));
        System.out.println(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));

        saveTasksToStorage(taskList); // add and save new tasklist to tasks.txt
    }

    // handle to do
    private static void handleToDo(String description, ArrayList<Task> taskList) {
        addTask(new Todo(description), taskList);
    }

    // handle deadline
    private static void handleDeadline(String description, ArrayList<Task> taskList) {
        int findIndex = description.indexOf("/by");
        if (findIndex == -1) {
            System.out.println(("Oops! The deadline detail is missing").indent(4));
            System.out.println(("Please use the format: deadline <task> /by <date/time>").indent(4));
            return;
        }
        String deadlineDescription = description.substring(0, findIndex).trim();
        String deadlineTiming = description.substring(findIndex + 3).trim();
        // String formattedDeadlineDescription = deadlineDescription + " (by: " +
        // deadlineTiming + ")";
        // addTask(new Deadline(formattedDeadlineDescription), taskList);
        addTask(new Deadline(deadlineDescription, deadlineTiming), taskList);

    }

    // handle event
    private static void handleEvent(String description, ArrayList<Task> taskList) {
        int findIndexFrom = description.indexOf("/from");
        int findIndexTo = description.indexOf("/to");
        // user must include both from and to
        if (findIndexFrom == -1 || findIndexTo == -1 || findIndexFrom > findIndexTo) {
            System.out.println(("Oops! The event detail is missing").indent(4));
            System.out.println(("Please use the format: event <task> /from <date/time> /to <date/time>").indent(4));
            return;
        }
        String eventDescription = description.substring(0, findIndexFrom).trim();
        String eventTimingFrom = description.substring(findIndexFrom + 5, findIndexTo).trim();
        String eventTimingTo = description.substring(findIndexTo + 3).trim();
        // String formattedEventDescription = eventDescription + " (from: " +
        // eventTimingFrom + " to: " + eventTimingTo
        // + ")";
        // addTask(new Event(formattedEventDescription), taskList);
        addTask(new Event(eventDescription, eventTimingFrom, eventTimingTo), taskList);
    }

    // delete task object from arraylist of task and display confirmation message
    private static void deleteTask(Task task, ArrayList<Task> taskList) {
        taskList.remove(task);
        System.out.println(("Got it, task removed from your task list.").indent(4));
        System.out.println((task.toString() + "\n").indent(8));
        System.out.println(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));

        saveTasksToStorage(taskList); // delete and save new tasklist to tasks.txt
    }

    // handle delete
    private static void handleDelete(String description, ArrayList<Task> taskList) throws PakipakiException {
        Task task = findTaskByDescription(description, taskList);
        deleteTask(task, taskList);
    }

    // check if details after valid command is missing
    private static void validateCommandDetails(String command, String taskString) throws PakipakiException {
        if (taskString.isEmpty()) {
            throw new PakipakiException("Details after '" + command + "' cannot be empty.");
        }
    }

    // file loading and storage
    public static void handleStorage(ArrayList<Task> taskList) {
        File f = new File(DEFAULT_STORAGE_FILEPATH);
        try {
            if (f.exists()) {
                System.out.println("Storage file found. saved tasks loaded");
                readStorageFile(f, taskList);
            } else {
                createStorageFile(f);
            }
        } catch (IOException e) {
            System.err.println("IO error during storage handling: " + e.getMessage());
        }
    }

    // create file
    public static void createStorageFile(File f) {
        try {
            f.getParentFile().mkdirs();
            f.createNewFile();
            System.out.println("Created new storage file: " + f.getName());
        } catch (IOException e) {
            System.out.println("An error occurred, file not created.");
        }
    }

    public static void writeStorageFile(File f, String textToAdd) {
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(textToAdd);
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    private static void saveTasksToStorage(ArrayList<Task> taskList) {
        File f = new File(DEFAULT_STORAGE_FILEPATH);
        StringBuilder sb = new StringBuilder();
        for (Task task : taskList) {
            sb.append(task.toStorageString()).append("\n");
        }
        writeStorageFile(f, sb.toString());
    }

    // read from tasks.txt in storage and add them to tasklist
    public static void readStorageFile(File f, ArrayList<Task> taskList) throws IOException {
        try (Scanner s = new Scanner(f)) { // close scanner after done
            while (s.hasNext()) {

                String line = s.nextLine().trim();
                // split lines
                String[] parts = line.split(" \\| "); // split by '|'

                // check
                if (parts.length < 3) {
                    throw new IOException("Corrupted line: " + line);
                }

                String taskType = parts[0];
                boolean isDone = parts[1].equals("1"); // true if 1 -> mark with X
                String description = parts[2];

                Task task;

                switch (taskType) {
                    case "T": // todo
                        if (parts.length > 3) {
                            throw new IOException("Corrupted todo line: " + line);
                        }
                        task = new Todo(description, isDone);
                        break;
                    case "D": // deadline
                        if (parts.length < 4) {
                            throw new IOException("Corrupted deadline line: " + line);
                        }
                        String by = parts[3];
                        task = new Deadline(description, by, isDone);
                        break;
                    case "E": // event
                        if (parts.length < 5) {
                            throw new IOException("Corrupted event line: " + line);
                        }
                        String from = parts[3];
                        String to = parts[4];
                        task = new Event(description, from, to, isDone);
                        break;
                    default:
                        throw new IOException("Unknown task type in line: " + line);
                }
                taskList.add(task);

            }
        } catch (IOException e) {
            System.out.println("Storage file appears corrupted: " + e.getMessage());
            File archiveFile = new File(f.getParent(), STORAGE_ARCHIVE_FILENAME);

            // rename corrupted file
            boolean renamed = f.renameTo(archiveFile);
            if (renamed) {
                System.out.println("Renamed corrupted storage to: " + archiveFile.getName());
            } else {
                System.out.println("Failed to rename corrupted storage file.");
                if (f.delete()) {
                    System.out.println("Deleted corrupted storage file.");
                } else {
                    System.out.println("Failed to delete corrupted storage file.");
                }
            }

            createStorageFile(f); // create new empty file with original file name

            taskList.clear(); // clear the task list as loading failed

        }
    }

    // get user input and display them
    public static void handleUserInput(Scanner in) {

        ArrayList<Task> taskList = new ArrayList<Task>(); // arraylist of task object to store all task dynamically
        handleStorage(taskList); // load tasklist from storage if availble

        // loop for user input
        while (true) {
            try {

                System.out.println();
                String userInput = in.nextLine().trim();

                if (userInput.isEmpty()) {
                    System.out.println(("Oops! You didn't type anything. Go ahead, give me a task!\n").indent(4));
                    continue;
                }

                // get the first word in the sentance
                String command = userInput.split(" ")[0].toLowerCase();
                // get everything after first word if there are more after first word
                String taskString = userInput.length() > command.length() ? userInput.substring(command.length()).trim()
                        : "";

                switch (command) {
                    case "list":
                        printTaskList(taskList);
                        break;

                    case "bye":
                        endMsg();
                        return;

                    case "mark":
                        validateCommandDetails(command, taskString);
                        handleMark(taskString, taskList);
                        break;

                    case "unmark":
                        validateCommandDetails(command, taskString);
                        handleUnmark(taskString, taskList);
                        break;

                    case "todo":
                        validateCommandDetails(command, taskString);
                        handleToDo(taskString, taskList);
                        break;

                    case "deadline":
                        validateCommandDetails(command, taskString);
                        handleDeadline(taskString, taskList);
                        break;

                    case "event":
                        validateCommandDetails(command, taskString);
                        handleEvent(taskString, taskList);
                        break;

                    case "delete":
                        validateCommandDetails(command, taskString);
                        handleDelete(taskString, taskList);
                        break;

                    default:
                        String unknownCommandMsg = """
                                Sorry, I do not quite get that.

                                    %s
                                """.formatted(USERGUIDEMSG);
                        throw new PakipakiException(unknownCommandMsg);
                }
            } catch (PakipakiException e) {
                System.out.println((e.getMessage()).indent(4));
            } catch (Exception e) {
                System.out.println(("Something went wrong!: " + e.getMessage()).indent(4));
            }
        }
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) { // close scanner
            startMsg();
            handleUserInput(in);
        }
    }
}