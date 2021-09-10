package model;

//Represents a half hour block of time in the schedule
public abstract class Block extends Timeable {
    private String categoryName;

    //MODIFES: this
    //EFFECTS: initializes all fields
    public Block(String name, int duration, String categoryName) {
        super(name, duration);
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
