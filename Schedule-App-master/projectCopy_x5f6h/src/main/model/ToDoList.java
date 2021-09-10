package model;

import java.util.ArrayList;

// Represents a ToDoList of Tasks
public class ToDoList extends TimeableList {
    //    private ArrayList<Block> blocks;
    private int durationSum;

    //MODIFIES: this
    //EFFECTS: durationSum set to 0 because list should be empty
    //other fields initialized
    public ToDoList(int availableTime) {
        this.durationSum = 0;
        setAvailableTime(availableTime);
        blocks = new ArrayList<>();
    }

    public UnscheduledBlock get(int i) {
        return (UnscheduledBlock) blocks.get(i);
    }

    public int size() {
        return blocks.size();
    }

    public int getDurationSum() {
        return durationSum;
    }

    //REQUIRES: duration < availableTime
    //MODIFIES: this
    //EFFECTS: adds a task to todolist and updates durationSum and availableTime
    public void addTask(Block task) {
        blocks.add(task);
        durationSum += task.getDuration();
        setAvailableTime(getAvailableTime() - task.getDuration());
    }

    //REQUIRES: duration < availableTime
    //MODIFIES: this
    //EFFECTS: adds a task to todolist and updates durationSum and availableTime
    public void addTask(String name, int duration, String category) {
        addTask(new UnscheduledBlock(name, duration, category));
    }

    //MODIFIES: this
    //EFFECTS: looks through tasks for matching name
    //if found return true, remove task and availableTime and durationSum
    public boolean removeTask(String name) {
        String matchName;
        UnscheduledBlock match;
        for (int i = 0; i < blocks.size(); i++) {
            match = (UnscheduledBlock) blocks.get(i);
            matchName = match.getName();
            if (name.equalsIgnoreCase(matchName)) {
                blocks.remove(i);
                setAvailableTime(getAvailableTime() + match.getDuration());
                durationSum -= match.getDuration();
                return true;
            }
        }
        return false;
    }

    //EFFECTS: looks through tasks for task with matching name
    // if found returns task otherwise returns empty task
    public UnscheduledBlock getTask(String name) {
        String matchName;
        UnscheduledBlock match;
        for (int i = 0; i < blocks.size(); i++) {
            match = (UnscheduledBlock) blocks.get(i);
            matchName = match.getName();
            if (name.equalsIgnoreCase(matchName)) {
                return match;
            }
        }
        return new UnscheduledBlock("", 0, "");
    }

    //MODIFIES: this
    //EFFECTS: if task found, decrement task
    // update availableTime and durationSum
    // if amount is greater than task duration delete task
    public void decrementTask(String name, int amount) {
        UnscheduledBlock task = getTask(name);
        int prevDuration = task.getDuration();
        //check if task null
        if (task.getName().equalsIgnoreCase(name)) {
            //check if amount decremented too high
            if (prevDuration < amount) {
                removeTask(name);
                return;
            }
            task.decrementTime(amount);
            setAvailableTime(getAvailableTime() + amount);
            durationSum -= amount;
        }
    }


    //REQUIRES: amount < availableTime
    //EFFECTS: this
    // increments task by amount
    // update availableTime and durationSum
    public void incrementTask(String name, int amount) {
        UnscheduledBlock task = getTask(name);
        task.incrementTime(amount);
        setAvailableTime(getAvailableTime() - amount);
        durationSum += amount;
    }
}
