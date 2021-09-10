package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//schedule app user interface
// CITATION: the methods loadSchedule and save schedule, along with their respective
//implementations were modelled after workroom app class fields from the JsonSerializationDemo.git repository
public class ConsoleScheduleApp {
    private String endTime;
    private Schedule mySchedule;
    private String timeString;
    private String name;
    private int duration;
    private String category;
    private int intTime;
    private String isDone;
    private Scanner in;
    private Scanner test;
    private ToDoList toDoList;

    private static final String JSON_STORE = "./data/";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //MODIFIES: this
    //EFFECTS: initializes fields json writer and reader and isDone
    // runs app
    public ConsoleScheduleApp() {
        isDone = "n";
        runSchedule();
    }

    //REQUIRES: user to adhere to entering time in correct form
    //MODIFIES: this
    //EFFECTS: runs the entire ui of the schedule app
    public void runSchedule() {
        System.out.println("Hello and welcome, I am the Auto-Scheduler!");
        in = new Scanner(System.in);
        test = new Scanner(System.in);
        String quit = "";
        loadOrMakeSchedule();
        do {
            editSchedule();
            toDoList = new ToDoList(mySchedule.getAvailableTime());
            makeToDoList();
            mergeScheduleAndToDoList();
            mySchedule.initializeCategories();
            displaySchedule();
            displayCategories();
            quit = saveOrEndSchedule();
        } while (quit.equalsIgnoreCase("n"));
    }

    //MODIFIES: this
    //EFFECTS: loads schedule from saved copy or creates a new schedule
    private void loadOrMakeSchedule() {
        String load = getString("Would you like to: \n1: Load a schedule from a file?\n2: Create a new schedule?");
        if (load.equalsIgnoreCase("1")) {
            loadSchedule(getString("Enter the name of the schedule you would like to load:"));
            endTime = mySchedule.makeTimeString(mySchedule.getEnd());
            displaySchedule();
            mySchedule.initializeCategories();
            displayCategories();
            System.out.println();
            isDone = getString("Would you like to add any appointments or scheduled events to this? (Y/N)");
        } else {
            System.out.println("All times must be entered in the form xx:xx (military time).");
            getStartAndEnd();
            System.out.println("Do you have any appointments or scheduled events for today? (Y/N)");
            isDone = getString("For example: eating breakfast, attending a lecture, attending a meeting.");
        }
    }

    //MODIFIES: this
    //EFFECTS: creates a schedule with start and end time from user
    private void getStartAndEnd() {
        String temp = "";
        temp = getString("What time does your day start?");
        int start = convertStringTimeToInt(temp);
        endTime = getString("What time does your day end?");
        int end = convertStringTimeToInt(endTime);
        mySchedule = new Schedule(start, end);
    }


    //REQUIRES: Time entered must be within previous start and end times entered
    //MODIFIES: this
    //EFFECTS: initializes and adds all scheduled time blocks to the schedule until user enters N
    public void editSchedule() {
        while (!isDone.equalsIgnoreCase("n")) {
            System.out.println("Okay!");
            name = getString("What is the name of the event?");
            timeString = getString("What time does the event start at?");
            duration = getInt("How many 30 minute intervals of time will your event last for?");
            category = getString("What type or category of event is this?");
            intTime = convertStringTimeToInt(timeString);
            ArrayList<ScheduledBlock> busyBlocks = mySchedule.checkBusy(intTime, duration);
            Boolean overWrite = false;
            while (busyBlocks.size() > 0 && !overWrite) {
                overWrite = warnBusy(busyBlocks);
                if (!overWrite) {
                    timeString = getString("Okay. Please enter a different time:");
                    intTime = convertStringTimeToInt(timeString);
                    busyBlocks = mySchedule.checkBusy(intTime, duration);
                }
            }
            mySchedule.addScheduledBlock(new ScheduledBlock(name, timeString, duration, intTime, category));
            displaySchedule();
            System.out.println("Here is your schedule so far!");
            isDone = getString("Would you like me to add another appointment or event to your schedule? (Y/N)");
        }
    }

    //EFFECTS: prints message that schedule full
    //returns user decision of user to overwrite a scheduled event or not
    private Boolean warnBusy(ArrayList<ScheduledBlock> busyBlocks) {
        System.out.println("Warning! you have already scheduled something for this time period:");
        System.out.println(busyBlocks.toString());
        System.out.println("You have two options: overwrite this or choose a new time for " + name + ".");
        String overWrite = getString("Would you like to overwrite this? (Y/N)");
        if (overWrite.equalsIgnoreCase("y")) {
            System.out.println("Okay I will replace:");
            System.out.println(busyBlocks.toString());
            System.out.println();
            return true;
        } else {
            return false;
        }
    }

    //MODIFIES: this
    //EFFECTS: adds tasks to todolist until user enters N
    //also ensures there is enough room in todolist to add task
    //allows user to edit list
    private void makeToDoList() {
        isDone = getString("Do you have any tasks you would like me to add to your to-do list for you? (Y/N)");
        while (!isDone.equalsIgnoreCase("n")) {
            int available = toDoList.getAvailableTime();
            if (available > 0) {
                System.out.println("Okay!");
               System.out.println("You now have " + available + " available 30 minute time blocks in your schedule.");
                name = getString("What is the name of your task?");
                duration = getInt("How many 30 minute intervals would you like to allocate to this task?");
                category = getString("What type or category of task is this?");
                while (duration - available > 0) {
                    duration = fullToDoList(available);
                }
                System.out.println("Task added.");
                toDoList.addTask(name, duration, category);
            } else {
                System.out.println("I'm sorry, you have run out of available 30 minute time blocks.");
            }
            editToDoList();
            isDone = getString("Would you like me to add another task for you? (Y/N)");
        }
    }

    //EFFECTS: Warns user that todolist is full and gets new duration
    private int fullToDoList(int available) {
        System.out.println("Warning! Your schedule is out of space:");
        System.out.println("You only have " + available + " 30 minute intervals left that are available.");
        return getInt("Enter a number of intervals less than or equal to: " + available);
    }

    //MODIFIES: (implicitly) this
    //EFFECTS: displays list, asks user if they would like to edit
    //calls edit task, shows updated list, and asks user if they would like to edit again
    private void editToDoList() {
        System.out.println("Here is your current task list:");
        displayToDoList();
        String wantsEdit = getString("Would you like to edit this list? (Y/N)");
        while (wantsEdit.equalsIgnoreCase("y")) {
            editTask();
            System.out.println("Great! Here is your updated task list:");
            displayToDoList();
            wantsEdit = getString("Would you like to edit another task on your list? (Y/N)");
        }
    }

    //MODIFIES: this
    //EFFECTS: user enters task they would like to edit
    //if user enters wrong name, they are notified and re-prompted
    //user enters type type of edit they would like
    //edit is preformed on task
    private void editTask() {
        String taskName = getString("Enter the name of the task you would like to edit:");
        UnscheduledBlock editBlock = toDoList.getTask(taskName);
        while (editBlock.getDuration() == 0) {
            taskName = getString("Task not found. Enter new name:");
            editBlock = toDoList.getTask(taskName);
        }
        System.out.println("What would you like to do with this task?");
        int input = getInt("Enter 1 to decrement time, 2 to increment time, 3 to remove, 4 to rename, 5 to Exit:");
        switch (input) {
            case 1:
              toDoList.decrementTask(taskName, getInt("Enter the number of 30 minute intervals to be decremented: "));
                break;
            case 2:
                incrementTimeOfTask(taskName);
                break;
            case 3:
                toDoList.removeTask(taskName);
                break;
            case 4:
                editBlock.setName(getString("Enter the new name:"));
                break;
        }
    }

    //MODIFIES: this
    //EFFECTS: gets UI for amount to increment task by
    // ensures there is enough available time
    // increments task duration
    public void incrementTimeOfTask(String taskName) {
        int time;
        int availableTime = toDoList.getAvailableTime();
        System.out.println("Enter number of 30 minute intervals to be incremented:");
        time = getInt("Amount must be less than or equal to: " + availableTime);
        while (time > availableTime) {
            System.out.println("Not enough time:");
            time = getInt("Amount must be less than or equal to " + availableTime + ".");
        }
        toDoList.incrementTask(taskName, time);
    }

    //EFFECTS: prints todolist
    public void displayToDoList() {
        for (Block b : toDoList.getBlocks()) {
            System.out.println("\t" + b.toString());
        }
    }

    //MODIFIES: this
    //EFFECTS: adds all tasks from todolist to schedule
    public void mergeScheduleAndToDoList() {
        for (Block task : toDoList.getBlocks()) {
            mySchedule.addTask(task);
        }
    }

    //EFFECTS: Prints list categories
    private void displayCategories() {
        mySchedule.getCategories();
        System.out.println();
        System.out.println("Categorical Breakdown:");
        for (Category c : mySchedule.getCategories()) {
            System.out.println("\t" + c.toString());
        }
    }

    //EFFECTS: prints and formats the output of the newly formed schedule
    //branch formats the schedule such that consecutive tasks are not printed
    private void displaySchedule() {
        boolean isNewName;
        String tempName = "";
        System.out.println();
        System.out.println("DAY SCHEDULE");
        for (Block sb : mySchedule.getBlocks()) {
            isNewName = !tempName.equals(sb.getName());
            if (isNewName) {
                System.out.println("-----------------------");
                System.out.println(sb.toString());
                tempName = sb.getName();
            } else {
                System.out.println(((ScheduledBlock) sb).getTimeString());
            }
        }
        System.out.println("-----------------------");
        System.out.println(endTime + " End");
    }

    //EFFECTS: prints message to user then gets int from user
    private int getInt(String message) {
        System.out.println(message);
        return test.nextInt();
    }

    //EFFECTS: prints message to user then gets string from user
    private String getString(String message) {
        System.out.println(message);
        return in.nextLine();
    }


    //Requires: string of format 00:00 that is a 30 minute multiple
    //Effects: returns an int of 0-48, corresponding to the half hour of the day
    //of the time entered.
    public int convertStringTimeToInt(String timeString) {
        int time = 0;
        time += Integer.parseInt(timeString.substring(0, 2)) * 2;
        if (timeString.startsWith("30", 3)) {
            time++;
        }
        return time;
    }

    //EFFECTS: saves schedule to file
    private void saveSchedule() {
        try {
            String fname = getString("Enter the name of your schedule.");
            jsonWriter = new JsonWriter(JSON_STORE + fname + ".json");
            jsonWriter.open();
            jsonWriter.write(mySchedule);
            jsonWriter.close();
            System.out.println("Saved schedule to " + JSON_STORE + fname + ".json");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads schedule from file
    private void loadSchedule(String fname) {
        try {
            jsonReader = new JsonReader(JSON_STORE + fname + ".json");
            mySchedule = jsonReader.read();
            System.out.println("Loaded schedule from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //EFFECTS: save schedule based on user decision
    //returns user decision to end program or not.
    public String saveOrEndSchedule() {
        String save = getString("Would you like to save this schedule? (Y/N)");
        if (save.equalsIgnoreCase("y")) {
            saveSchedule();
        }
        String quit = getString("Would you like to quit the application? (Y/N)");
        if (!quit.equalsIgnoreCase("y")) {
            isDone = getString("Would you like to schedule another event or appointment? (Y/N)");
        }
        return quit;
    }
}
