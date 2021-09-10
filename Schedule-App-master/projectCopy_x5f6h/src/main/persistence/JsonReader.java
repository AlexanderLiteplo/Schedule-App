package persistence;

import model.Schedule;
import model.ScheduledBlock;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

//Represents a Json reader object
//Citation: this class is a copied and modified version of
//the JsonReader class from the JsonSerializationDemo.git repository
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Schedule read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSchedule(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses schedule from JSON object and returns it
    private Schedule parseSchedule(JSONObject jsonObject) {
        int start = jsonObject.getInt("start");
        int end = jsonObject.getInt("end");
        Schedule s = new Schedule(start, end);
        createSchedule(s, jsonObject);
        return s;
    }

    // MODIFIES: Schedule
    // EFFECTS: parses ScheduledBlocks from JSON object and adds them to schedule
    private void createSchedule(Schedule schedule, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("scheduledBlocks");
        for (Object json : jsonArray) {
            JSONObject nextScheduledBlock = (JSONObject) json;
            addScheduledBlock(schedule, nextScheduledBlock);
        }
    }

    // MODIFIES: schedule
    // EFFECTS: parses ScheduledBlock from JSON object and adds it to schedule
    private void addScheduledBlock(Schedule schedule, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String timeString = jsonObject.getString("timeString");
        int duration = jsonObject.getInt("duration");
        int time = jsonObject.getInt("time");
        String category = jsonObject.getString("category");
        ScheduledBlock scheduledBlock = new ScheduledBlock(name, timeString, duration, time, category);
        schedule.addScheduledBlock(scheduledBlock);
    }
}
