package model;


//represents the Category of a scheduledBlock in schedule
public class Category extends Timeable {

    //Modifies: this
    //Effects: initializes the fields of the category
    public Category(String name, int duration) {
        super(name, duration);
    }

    //EFFECTS: formats string showing its name and duration in hours
    //if duration is multiple of 2 it divides by 2
    //otherwise divide duration by 2 and add .5 to string
    public String toString() {
        if (getDuration() % 2 == 0) {
            return getName() + ": " + getDuration() / 2 + " hour(s)";
        } else {
            return getName() + ": " + getDuration() / 2 + ".5 hour(s)";
        }
    }
}
