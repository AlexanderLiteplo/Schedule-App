package model;

//Represents a block of unscheduled time
public class UnscheduledBlock extends Block {

    //MODIFES: this
    //EFFECTS: initializes all fields
    public UnscheduledBlock(String name, int duration, String category) {
        super(name, duration, category);
    }

    public String toString() {
        return getName() + ": " + " duration = " + getDuration() + " category = " + getCategoryName();
    }
}
