package model;


import java.util.ArrayList;

//represents a list of timeable objects
public abstract class TimeableList {

    private int availableTime;
    protected ArrayList<Block> blocks;

    public abstract void addTask(Block task);

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public int getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(int availableTime) {
        this.availableTime = availableTime;
    }

}
