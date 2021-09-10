package ui;

import model.Block;
import model.Schedule;
import model.ToDoList;
import model.UnscheduledBlock;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//          The implementation structure of the panels and frame of this class was modelled
//          after the DrawingPlayer class of the CPSC 210 SimpleDrawingPlayer repository


//TodoList window, for scheduling tasks to schedule
public class ToDoListWindow extends JFrame implements ListSelectionListener {
    private Schedule schedule;
    private ToDoList toDoList;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    private static final String addString = "Add Task";
    private static final String removeString = "Remove Selected Task";


    private JButton removeButton;
    private JButton addButton;
    private JTextField taskNameField;
    private AddListener addListener;
    private JPanel inputPane;

    private JTextField taskCategoryNameField;
    private JTextField taskDurationField;

    private String category;
    private String taskName;
    private int duration;

    private JScrollPane listScrollPane;
    private JList list;
    private DefaultListModel listModel;
    private JButton mergeButton;


    //MODIFIES: this
    //EFFECTS: Launches todolist window, and initializes fields and graphics
    public ToDoListWindow(Schedule schedule) {
        super("Todo List");
        this.schedule = schedule;
        setLayout(new BorderLayout());
        listModel = new DefaultListModel();
        setMinimumSize(new Dimension(WIDTH, HEIGHT));

        initializeFields();
        initializeGraphics();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    //MODIFIES: this
    //EFFECTS: initializes todolist based on schedule
    private void initializeFields() {
        //Create the list and put it in a scroll pane.
        initializeList();
        toDoList = new ToDoList(schedule.getAvailableTime());
    }

    //EFFECTS: calls methods to add scroll pane and input panel
    private void initializeGraphics() {
        //Create a panel that uses BoxLayout.
        addScrollPane();
        initializeInteractionPanel();
    }

    //MODIFIES: this
    //EFFECTS: add scroll pane to frame
    private void addScrollPane() {
        listScrollPane = new JScrollPane(list);
        add(listScrollPane);
    }

    //MODIFIES: this
    //EFFECTS: initializes a JList to display the todolist
    private void initializeList() {
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
    }

    //MODIFIES: this
    //EFFECTS: initializes and adds interaction pane to the frame
    private void initializeInteractionPanel() {
        inputPane = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        inputPane.setLayout(gbl);

        initializeButtons();

        initializeTextFields();

        initializeRemoveGraphics(gbc);

        initializeTextFieldGraphics(gbc);


        initializeAddMergeGraphics(gbc);

        inputPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(inputPane, BorderLayout.SOUTH);
    }

    //MODIFIES: this
    //EFFECTS: adds remove button to pane
    private void initializeRemoveGraphics(GridBagConstraints gbc) {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPane.add(removeButton, gbc);
        gbc.gridwidth = 1;
    }

    //MODIFIES: this
    //EFFECTS: adds text fields to pane
    private void initializeTextFieldGraphics(GridBagConstraints gbc) {
        initializeTaskFieldGraphics(gbc, 1, "Task Name:", taskNameField);

        initializeTaskFieldGraphics(gbc, 2, "Task Category:", taskCategoryNameField);

        initializeTaskFieldGraphics(gbc, 3, "Duration (# of 30 Minute Intervals):", taskDurationField);
    }

    //MODIFIES: this
    //EFFECTS: adds a task field to pane
    private void initializeTaskFieldGraphics(GridBagConstraints gbc, int i, String s, JTextField taskNameField) {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = i;
        inputPane.add(new JLabel(s), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = i;
        inputPane.add(taskNameField, gbc);
    }

    //MODIFIES: this
    //EFFECTS: adds merge button and add button to pane
    private void initializeAddMergeGraphics(GridBagConstraints gbc) {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        inputPane.add(addButton, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPane.add(mergeButton, gbc);
    }

    //MODIFIES: this
    //EFFECTS: initializes text fields for a task's name, category, and duration
    private void initializeTextFields() {
        taskNameField = new JTextField(10);
        taskNameField.addActionListener(addListener);
        taskNameField.getDocument().addDocumentListener(addListener);


        taskCategoryNameField = new JTextField(10);
        taskCategoryNameField.addActionListener(addListener);
        taskCategoryNameField.getDocument().addDocumentListener(addListener);

        taskDurationField = new JTextField(10);
        taskDurationField.addActionListener(addListener);
        taskDurationField.getDocument().addDocumentListener(addListener);
    }

    //MODIFIES: this
    //EFFECTS: initializes remove button, add button and merge button
    private void initializeButtons() {
        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new DeleteListener());

        addButton = new JButton(addString);
        addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        mergeButton = new JButton("Schedule ToDoList Tasks");
        mergeButton.addActionListener(new MergeAction());
    }


    //MODIFIES: this
    //EFFECTS: if list item selected, remove button enabled
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                removeButton.setEnabled(false);
            } else {
                removeButton.setEnabled(true);
            }
        }
    }

    //delete button listener
    class DeleteListener implements ActionListener {

        //MODIFIES:this
        //EFFECTS: Deletes selected task from todolist
        public void actionPerformed(ActionEvent e) {
            Toolkit.getDefaultToolkit().beep();
            int index = list.getSelectedIndex();
            String taskName = (String) listModel.get(index);
            taskName = taskName.substring(0, taskName.indexOf(":"));
            listModel.remove(index);
            toDoList.removeTask(taskName);
        }
    }

    //listener for add button
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        //MODIFIES: this
        //EFFECTS: initializes button field
        public AddListener(JButton button) {
            this.button = button;
        }

        //MODIFIES: this
        //EFFECTS: adds new task to todolist
        public void actionPerformed(ActionEvent e) {
            taskName = taskNameField.getText();
            category = taskCategoryNameField.getText();
            duration = Integer.parseInt(taskDurationField.getText());
            UnscheduledBlock task = new UnscheduledBlock(taskName, duration, category);
            String taskString = task.toString();

            toDoList.addTask(taskName, duration, category);

            listModel.addElement(taskString);
            taskNameField.setText("");
            taskCategoryNameField.setText("");
            taskDurationField.setText("");

        }

        //MODIFIES: this
        //EFFECTS: enables button
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //EFFECTS: calls handleEmptyTextField
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //MODIFIES: this
        //EFFECTS: enables button if text not empty
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        //MODIFIES: this
        //EFFECTS: allows button to be pressed
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        //MODIFIES: this
        //EFFECTS: if text field empt return true, beep, and disable button
        //         else return false
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                Toolkit.getDefaultToolkit().beep();
                return true;
            }
            return false;
        }
    }

    //action for merge schedule and todolist button
    private class MergeAction extends AbstractAction {

        MergeAction() {
            super("Merge");
        }

        //MODIFIES: this
        //EFFECTS: merge the schedule and todolist
        //          pass schedule to a new editor window
        //          close todolist window
        public void actionPerformed(ActionEvent evt) {
            mergeScheduleAndToDoList();
            setVisible(false);
            ScheduleEditorWindow scheduleEditorWindow = new ScheduleEditorWindow(schedule, null);
        }
    }

    //MODIFIES: this
    //EFFECTS: adds all tasks from todolist to schedule
    public void mergeScheduleAndToDoList() {
        for (Block task : toDoList.getBlocks()) {
            schedule.addTask(task);
        }
    }

}
