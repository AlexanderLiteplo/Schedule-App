package ui;

import model.Schedule;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

// CITATION: the methods loadSchedule and save schedule, along with their respective
//implementations were modelled after workroom app class fields from the JsonSerializationDemo.git repository

//Window giving option to create new or load old schedule
public class WelcomeWindow extends JFrame {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 180;

    private Schedule schedule;
    private String name;

    private static final String JSON_STORE = "./data/";
    private JsonReader jsonReader;

    //MODIFIES: this
    //EFFECTS: initializes fields and graphics of frame
    public WelcomeWindow() {
        super("Schedule App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeGraphics();

    }

    //MODIFIES: this
    //EFFECTS: initializes frame graphics
    private void initializeGraphics() {
        setLayout(new GridLayout(0, 1));
        setSize(new Dimension(WIDTH, HEIGHT));
        addComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: adds buttons and labels to panel
    private void addComponents() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(0, 1));
        textPanel.setSize(new Dimension(0, 0));
        add(textPanel);
        JLabel welcome = new JLabel("Welcome to the Schedule App!");
        welcome.setFont(new Font("Verdana", Font.PLAIN, 20));
        welcome.setHorizontalAlignment(0);
        textPanel.add(welcome);
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2));
        buttonPanel.add(new JButton(new MakeScheduleAction()));
        buttonPanel.add(new JButton(new LoadScheduleAction()));
        add(buttonPanel);
    }

    //REQUIRES: user to enter time between 00:00 24:00 in military form
    //MODIFIES: this
    //EFFECTS: initializes a new schedule and displays schedule
    public void makeNewSchedule(String start, String end) {
        schedule = new Schedule(timeToInt(start), timeToInt(end));
        launchScheduleDisplayWindow();
    }

    //Requires: string of format 00:00 that is a 30 minute multiple
    //Effects: returns an int of 0-48, corresponding to the half hour of the day
    //of the time entered.
    private int timeToInt(String timeString) {
        int time = 0;
        time += Integer.parseInt(timeString.substring(0, 2)) * 2;
        if (timeString.startsWith("30", 3)) {
            time++;
        }
        return time;
    }

    // MODIFIES: this
    // EFFECTS: loads schedule from file and displays it to user
    //      if schedule not found, beep and restart
    public void loadSchedule(String fileName) {
        try {
            jsonReader = new JsonReader(JSON_STORE + fileName + ".json");
            schedule = jsonReader.read();
            launchScheduleDisplayWindow();
        } catch (IOException e) {
            Toolkit.getDefaultToolkit().beep();
            new WelcomeWindow();
        }
    }

    //MODIFIES: this
    //EFFECTS: closes this and launches schedule display
    public void launchScheduleDisplayWindow() {
        ScheduleEditorWindow scheduleEditor = new ScheduleEditorWindow(schedule, name);
    }

    //MODIFIES: this
    //EFFECTS: closes this
    public void closeWindow() {
        setVisible(false);
    }

    //CITATION: the following class and its implementation was modelled after the
    //          addCodeAction class from the CPSC 210 AlarmSystem repository
    //pop up to get user input for making new schedule
    private class MakeScheduleAction extends AbstractAction {

        //MODIFIES: this
        //EFFECTS: constructs class
        MakeScheduleAction() {
            super("Make Schedule");
        }

        //REQUIRES: users to adhere to input format
        //MODIFIES: this
        //EFFECTS: gets user input and makes a new schedule
        public void actionPerformed(ActionEvent evt) {
            closeWindow();
            name = JOptionPane.showInputDialog(null,
                    "Enter the Name of Your Schedule",
                    "Schedule Name",
                    JOptionPane.QUESTION_MESSAGE);
            String start = JOptionPane.showInputDialog(null,
                    "Enter Start Time (Military Form: ##:##)",
                    "Start Time",
                    JOptionPane.QUESTION_MESSAGE);
            String end = JOptionPane.showInputDialog(null,
                    "Enter End Time (Military Form ##:##)",
                    "End Time",
                    JOptionPane.QUESTION_MESSAGE);

            makeNewSchedule(start, end);
        }
    }

    //CITATION: the following class and its implementation was modelled after the
    //          addCodeAction class from the CPSC 210 AlarmSystem repository

    //action for load schedule button
    private class LoadScheduleAction extends AbstractAction {

        //MODIFIES: this
        //EFFECTS: initializes class name
        LoadScheduleAction() {
            super("Load Schedule");
        }

        //MODIFIES: this
        //EFFECTS: closes welcome window and gets user input for the name of
        //      the schedule they would like to load
        public void actionPerformed(ActionEvent evt) {
            closeWindow();
            String name = JOptionPane.showInputDialog(null,
                    "Enter Name of Schedule:",
                    "Schedule Name",
                    JOptionPane.QUESTION_MESSAGE);
            loadSchedule(name);
        }
    }
}
