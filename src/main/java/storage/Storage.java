package storage;

import parser.Parser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import task.Task;

public class Storage {
    private String filepath;

    public Storage(String filepath) {
        this.filepath = filepath;
    }

    // file loading and storage
    public void handleStorage(ArrayList<Task> taskList) {
        File f = new File(filepath);
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

    // write to storage file
    public void writeStorageFile(File f, String textToAdd) {
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(textToAdd);
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    // save task list to storage txt file
    public void saveTasksToStorage(ArrayList<Task> taskList) {
        File f = new File(filepath);
        StringBuilder sb = new StringBuilder();
        for (Task task : taskList) {
            sb.append(task.toStorageString()).append(System.lineSeparator());
        }
        writeStorageFile(f, sb.toString());
    }

    // read from tasks.txt in storage and add them to tasklist
    public void readStorageFile(File f, ArrayList<Task> taskList) throws IOException {
        try (Scanner s = new Scanner(f)) { // close scanner after done
            while (s.hasNext()) {
                String line = s.nextLine().trim();
                // Task task = createTaskFromLine(line);
                // taskList.add(task);
                try {
                    Task task = Parser.parseTaskLine(line);
                    taskList.add(task);
                } catch (IOException e) {
                    System.out.println("Corrupted line skipping: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Storage file appears corrupted: " + e.getMessage());
            handleCorruptedFile(f, taskList);
        }
    }

    // handle corrupted file
    private static void handleCorruptedFile(File f, ArrayList<Task> taskList) {
        File archiveFile = new File(f.getParent(), "tasksArchive.txt");

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
