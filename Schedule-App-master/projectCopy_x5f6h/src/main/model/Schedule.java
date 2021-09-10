package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//represents a schedule of half hour time blocks and list/record of categories
// with info pertaining to the scheduled activities
public class Schedule extends TimeableList {
    private int start;
    private int end;
    private int numHalfHourBlocks;
    //    private ArrayList<ScheduledBlock> blocks;
    private ArrayList<Category> categories;

    //REQUIRES: start < end and both must be in range [0,48]
    //MODIFIES: this
    //EFFECTS: Initializes all fields based on start and end time
    public Schedule(int start, int end) {
        this.start = start;
        this.end = end;
        this.numHalfHourBlocks = end - start;
        setAvailableTime(end - start);
        blocks = new ArrayList<>();
        categories = new ArrayList<>();
        initializeSchedule();
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getNumHalfHourBlocks() {
        return numHalfHourBlocks;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }


    //initializes a schedule of free time slots
    //REQUIRES:
    //MODIFIES: this
    //EFFECTS: initializes schedule of Scheduled blocks
    //with name Free Time, correct time String, duration of 1, time of start + i
    // and category Free Time
    public void initializeSchedule() {
        for (int i = 0; i < getNumHalfHourBlocks(); i++) {
            int time = getStart() + i;
            ScheduledBlock block = new ScheduledBlock("Free Time", makeTimeString(time), 1, time, "Free Time");
            blocks.add(block);
        }
    }

    //converts integer representation of time of a ScheduledBlock into a String
    //REQUIRES: int 0-48 inclusive
    //EFFECTS: returns a string in format hours:minutes
    // corresponding to the halfHourMultiple entered
    public String makeTimeString(int halfHourMultiple) {
        String timeString = "";
        if (halfHourMultiple % 2 == 0) {
            timeString = halfHourMultiple / 2 + ":00";
        } else {
            timeString = halfHourMultiple / 2 + ":30";
        }
        if (halfHourMultiple < 20) {
            timeString = "0" + timeString;
        }
        return timeString;
    }

    //EFFECTS: Returns list of ScheduledBlocks that interfere with
    // with time and duration
    //blocks that interfere are non - Free Time blocks
    public ArrayList<ScheduledBlock> checkBusy(int time, int duration) {
        ArrayList<ScheduledBlock> interferingTimes = new ArrayList<>(0);
        int blockStartArrPos = time - getStart();
        for (int i = blockStartArrPos; i < blockStartArrPos + duration; i++) {
            if (!blocks.get(i).getName().equals("Free Time")) {
                interferingTimes.add((ScheduledBlock) blocks.get(i));
            }
        }
        return interferingTimes;
    }

    //REQUIRES: ScheduledBlock time within the range of startTime and endTime
    // startTime + scheduledBlock duration < end time
    //MODIFIES: this
    //EFFECTS: updates category info
    // adds the ScheduledBlock to its positions in schedule
    public void addScheduledBlock(ScheduledBlock scheduledBlock) {
        int blockStartArrPos = scheduledBlock.getTime() - getStart();
        int duration = scheduledBlock.getDuration();
        for (int i = blockStartArrPos; i < blockStartArrPos + duration; i++) {
            if (blocks.get(i).getName().equalsIgnoreCase("Free Time")) {
                if (!scheduledBlock.getName().equalsIgnoreCase("Free Time")) {
                    setAvailableTime(getAvailableTime() - 1);
                }
            }
            blocks.get(i).setName(scheduledBlock.getName());
            blocks.get(i).setCategoryName(scheduledBlock.getCategoryName());
        }
    }

    //Schedules task in free time position
    //MODIFIES: this
    //EFFECTS: adds task to schedule in place of free time
    // scheduledBlock fields are set to task equivalent task fields
    // for duration times or until no Free Time blocks left
    public void addTask(Block task) {
        int duration = task.getDuration();
        //if block in schedule is free time
        //replace with task for duration times
        for (int i = 0; i < numHalfHourBlocks && duration > 0; i++) {
            if (blocks.get(i).getName().equals("Free Time")) {
                blocks.get(i).setName(task.getName());
                blocks.get(i).setCategoryName(task.getCategoryName());
                duration--;
                setAvailableTime(getAvailableTime() - 1);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: adds category of free time in 0th position
    //for each block in schedule, adds category information to categories
    public void initializeCategories() {
        categories.clear();
        categories.add(new Category("Free Time", 0));
        for (Block block : blocks) {
            addCategory(block);
        }
    }


    //Adds new Category to category list or increments existing category
    //MODIFIES: this
    //EFFECTS: if category exists in list then increments its duration
    //otherwise adds a new category to the list and increments its duration
    public void addCategory(Block block) {
        int duration = block.getDuration();
        String categoryName = block.getCategoryName();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equalsIgnoreCase(categoryName)) {
                categories.get(i).incrementTime(duration);
                return;
            }
        }
        categories.add(new Category(categoryName, duration));
    }


    //citation: modelled after thingiesToJson method from JsonSerializationDemo.git
    //EFFECTS: converts ScheduledBlocks into JSON array
    private JSONArray scheduledBlocksToJson() {
        JSONArray arr = new JSONArray();
        for (Block scheduledBlock : blocks) {
            arr.put(((ScheduledBlock) scheduledBlock).toJson());
        }
        return arr;
    }

    //EFFECTS: converts Schedule to JSON object
    public JSONObject scheduleToJson() {
        JSONObject json = new JSONObject();
        json.put("start", start);
        json.put("end", end);
        json.put("scheduledBlocks", scheduledBlocksToJson());
        return json;
    }

}
