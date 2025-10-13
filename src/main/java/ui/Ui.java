package ui;

import java.util.ArrayList;
import task.Task;

public class Ui {
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
            """; // add delete command later

    // print the horizontal decoration line for seperation
    private void printHorzLine() {
        printMessage(HORIZONTAL_LINE);
    }

    // print start message
    public void startMsg() {
        printHorzLine();
        String message = """
                Hello! I'm %s, your friendly assistant here to help you manage your tasks.

                %s
                """.formatted(BOTNAME, USERGUIDEMSG);
        printMessage(message);
        printHorzLine();
    }

    // print end message
    public void endMsg() {
        printMessage(("Thank you for chatting with me! Until next time!").indent(4));
        printHorzLine();
    }

    // print items inside task list and display message if the list is empty.
    public void printTaskList(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            printMessage(("Your task list is empty! Add something and I'll keep it ready for you!").indent(4));
        } else {
            printMessage(
                    ("Here's what's on your task list so far: " + "(" + taskList.size() + " in total)").indent(4));
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println((((i + 1) + ". " + taskList.get(i)).indent(8)));
            }
            printLine();
        }
    }

    public String getUserGuideMsg() {
        return USERGUIDEMSG;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printLine() {
        System.out.println();
    }
}
