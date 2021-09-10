package model;

//Represent an object with a name and duration
//Created to abstract common methods and fields from Block and Category
public abstract class Timeable {
    private String name;
    private int duration;

    //MODIFES: this
    //EFFECTS: initializes all fields
    public Timeable(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    //increments duration
    //REQUIRES: amount > 0
    //MODIFIES: this
    //EFFECTS: adds amount to duration
    public void incrementTime(int amount) {
        duration += amount;
    }

    //decrements duration
    //REQUIRES: amount <= duration
    //MODIFIES: this
    //EFFECTS: subtracts amount from duration
    public void decrementTime(int amount) {
        duration -= amount;
    }


}
