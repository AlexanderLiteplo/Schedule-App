package model;

import org.json.JSONObject;

// Represents a scheduled block of time
public class ScheduledBlock extends Block {
    private int time;
    private String timeString;

    //MODIFES: this
    //EFFECTS: initializes all fields
    public ScheduledBlock(String name, String timeString, int duration, int time, String category) {
        super(name,duration,category);
        this.time = time;
        this.timeString = timeString;
    }

    public int getTime() {
        return time;
    }

    public String getTimeString() {
        return timeString;
    }

    public String toString() {
        return timeString + " | " + getName();
    }

    //EFFECTS: converts a ScheduledBlock to a json object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("timeString",getTimeString());
        json.put("duration",getDuration());
        json.put("time",getTime());
        json.put("category",getCategoryName());
        return json;
    }
}
