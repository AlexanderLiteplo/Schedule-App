package ui;

import model.Block;
import model.Category;
import model.Schedule;
import model.ScheduledBlock;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

// CITATION: the methods loadSchedule and save schedule, along with their respective
//          implementations were modelled after workroom app class fields from the JsonSerializationDemo.git repository
//          The implementation structure of the panels and frames of this class was modelled
//          after the DrawingPlayer class of the CPSC 210 SimpleDrawingPlayer repository

//Schedule display and editor window
public class ScheduleEditorWindow extends JFrame {
    private Schedule schedule;
    private String fileName;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 800;

    private JPanel scheduleDisplayPanel;

    private String timeString;
    private String name;
    private int duration;
    private String category;
    private int intTime;

    private static final String JSON_STORE = "./data/";
    private JsonWriter jsonWriter;
    private JPanel buttonArea;

    //MODIFIES: this
    //EFFECTS:  initializes fields and graphics for schedule display panel
    public ScheduleEditorWindow(Schedule schedule, String fileName) {
        super("Schedule App");

        this.schedule = schedule;
        this.fileName = fileName;

        initializeScheduleGraphics(false);

        addButtonPane();
        addScheduleDisplayPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    //MODIFIES: this
    //EFFECTS: initializes schedule graphics
    //      if update is true, removes panel to start from scratch
    private void initializeScheduleGraphics(Boolean update) {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        if (update) {
            remove(scheduleDisplayPanel);
        }
    }

    //MODIFIES: this
    //EFFECTS: adds panel to this that displays schedule info
    private void addScheduleDisplayPanel() {
        scheduleDisplayPanel = new JPanel(new GridLayout(0, 1));
        for (Block b : schedule.getBlocks()) {
            JLabel label = new JLabel(b.toString());
            scheduleDisplayPanel.add(label);
            scheduleDisplayPanel.add(new JSeparator());
        }
        add(scheduleDisplayPanel, BorderLayout.CENTER);
    }

    //MODIFIES: this
    //EFFECTS: initializes and button panel of buttons
    private void addButtonPane() {
        buttonArea = new JPanel();
        buttonArea.setLayout(new GridLayout(0, 1));

        addEditButton();

        addSummaryButton();

        addToDoListButton();

        addSaveButton();

        add(buttonArea, BorderLayout.SOUTH);
    }

    //MODIFIES: this
    //EFFECTS: initializes save button and adds it to button panel
    private void addSaveButton() {
        JButton saveButton = new JButton("Save Schedule");
        saveButton.setBorderPainted(true);
        saveButton.setFocusPainted(true);
        saveButton.setContentAreaFilled(true);
        saveButton.addActionListener(new SaveScheduleAction());
        buttonArea.add(saveButton);
    }

    //MODIFIES: this
    //EFFECTS: initializes todolist button and adds it to button panel
    private void addToDoListButton() {
        JButton toDoListButton = new JButton("Make Todo List");
        toDoListButton.setBorderPainted(true);
        toDoListButton.setFocusPainted(true);
        toDoListButton.setContentAreaFilled(true);
        toDoListButton.addActionListener(new ToDoListAction());
        buttonArea.add(toDoListButton);
    }

    //MODIFIES: this
    //EFFECTS: initializes summary button and adds it to button panel
    private void addSummaryButton() {
        JButton categoryDisplayButton = new JButton("View Summary");
        categoryDisplayButton.setBorderPainted(true);
        categoryDisplayButton.setFocusPainted(true);
        categoryDisplayButton.setContentAreaFilled(true);
        categoryDisplayButton.addActionListener(new DisplayCategoriesAction(schedule));
        buttonArea.add(categoryDisplayButton);
    }

    //MODIFIES: this
    //EFFECTS: initializes edit button and adds it to button panel
    private void addEditButton() {
        JButton editButton = new JButton("Edit schedule");
        editButton.setBorderPainted(true);
        editButton.setFocusPainted(true);
        editButton.setContentAreaFilled(true);
        editButton.addActionListener(new EditScheduleAction());

        buttonArea.add(editButton);
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
            jsonWriter = new JsonWriter(JSON_STORE + fileName + ".json");
            jsonWriter.open();
            jsonWriter.write(schedule);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    "Saved schedule to " + JSON_STORE + fileName + ".json");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to write to file: " + JSON_STORE);
        }
    }

    //action for todolist button
    private class ToDoListAction extends AbstractAction {

        //MODIFIES: this
        //EFFECTS: constructs todolist action
        ToDoListAction() {
            super("To-Do List");
        }

        //MODIFIES: this
        //EFFECTS: hides schedule editor window and opens todolist window
        public void actionPerformed(ActionEvent evt) {
            setVisible(false);
            ToDoListWindow toDoListWindow = new ToDoListWindow(schedule);
        }
    }

    //action for save button
    private class SaveScheduleAction extends AbstractAction {

        //MODIFIES:this
        //EFFECTS: constructs save schedule action
        SaveScheduleAction() {
            super("Save Schedule");
        }

        //MODIFIES: this
        //EFFECTS: gets user input for schedule name
        //      saves schedule under name
        public void actionPerformed(ActionEvent evt) {
            fileName = JOptionPane.showInputDialog(null,
                    "Enter Name of Schedule:",
                    "Save Schedule",
                    JOptionPane.QUESTION_MESSAGE);
            saveSchedule();
        }
    }

    //action for edit button
    private class EditScheduleAction extends AbstractAction {

        //MODIFIES: this
        //EFFECTS:  initializes the class with name
        EditScheduleAction() {
            super("Make Schedule");
        }

        //REQUIRES: Time entered must be within previous start and end times entered
        //MODIFIES: this
        //EFFECTS: initializes and adds a scheduled time block to the schedule
        public void actionPerformed(ActionEvent evt) {

            name = JOptionPane.showInputDialog(null,
                    "What is the name of the new event?",
                    "Event Name",
                    JOptionPane.QUESTION_MESSAGE);
            timeString = JOptionPane.showInputDialog(null,
                    "What time does the event start at?",
                    "Start Time",
                    JOptionPane.QUESTION_MESSAGE);
            duration = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "How many 30 minute intervals will your event last for?",
                    "Duration",
                    JOptionPane.QUESTION_MESSAGE));
            category = JOptionPane.showInputDialog(null,
                    "What category of event is this?",
                    "Category",
                    JOptionPane.QUESTION_MESSAGE);
            intTime = convertStringTimeToInt(timeString);

            schedule.addScheduledBlock(new ScheduledBlock(name, timeString, duration, intTime, category));
            initializeScheduleGraphics(true);
        }
    }

    //pop up window to display a categorical breakdown of the schedule
    private static class DisplayCategoriesAction extends AbstractAction {

        private static JDialog jDialog;
        private static Schedule mySchedule;

        //MODIFIES: this
        //EFFECTS: initializes static schedule to be referenced from class schedule
        DisplayCategoriesAction(Schedule schedule) {
            super("Summary");
            this.mySchedule = schedule;
        }

        //MODIFIES: this
        //EFFECTS: when this pressed, displays categorical breakdown to user
        public void actionPerformed(ActionEvent evt) {
            JFrame categoryFrame = new JFrame();
            jDialog = new JDialog(categoryFrame, "Summary", true);
            jDialog.setLayout(new GridLayout(0, 1));
            JPanel upperPanel = initializeUpperPanel();
            JPanel categoryPanel = initializeCategoryPanel();
            addAndDisplayPanels(upperPanel, categoryPanel);
            displayImage();
        }

        //MODIFIES:
        //EFFECTS: initializes upper panel components: Done button,
        //          and JLabel that gives the user instructions
        private JPanel initializeUpperPanel() {
            JPanel upperPanel = new JPanel(new FlowLayout());
            JButton b = new JButton("Done");
            //action listener for close button
            b.addActionListener(e -> DisplayCategoriesAction.jDialog.setVisible(false));
            upperPanel.add(new JLabel("Here is a breakdown of your schedule:"));
            upperPanel.add(b);
            return upperPanel;
        }

        //MODIFIES: this
        //EFFECTS: initializes the display of the categories
        private JPanel initializeCategoryPanel() {
            mySchedule.initializeCategories();
            JPanel categoryPanel = new JPanel(new GridLayout(0, 1));
            for (Category c : mySchedule.getCategories()) {
                categoryPanel.add(new JLabel(c.toString()));
                categoryPanel.add(new JSeparator());
            }
            return categoryPanel;
        }

        //MODIFIES: this
        //EFFECTS: adds and displays panels
        private void addAndDisplayPanels(JPanel upperPanel, JPanel categoryPanel) {
            jDialog.add(upperPanel);
            jDialog.add(categoryPanel);
            jDialog.setSize(400, 300);
            jDialog.setVisible(true);
        }

        //MODIFIES: this
        //EFFECTS: if user has less than 2 hours of available time
        //      display image with negative connotation
        //          else display a relaxing image
        public void displayImage() {
            if (mySchedule.getAvailableTime() < 4) {
                NotEnoughFreeTimeDisplay display = new NotEnoughFreeTimeDisplay();
                JFrame pictureFrame = new JFrame();
                pictureFrame.add(display);
                pictureFrame.setSize(900, 600);
                pictureFrame.setVisible(true);
            } else {
                EnoughFreeTimeDisplay display = new EnoughFreeTimeDisplay();
                JFrame pictureFrame = new JFrame();
                pictureFrame.add(display);
                pictureFrame.setSize(900, 600);
                pictureFrame.setVisible(true);
            }
        }
    }

}
