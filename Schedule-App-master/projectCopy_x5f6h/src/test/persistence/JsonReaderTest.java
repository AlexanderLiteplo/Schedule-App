package persistence;


import model.Schedule;
import model.ScheduledBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


//CITATION: this test class is a copied and modified version
//of the JsonReaderTest class from the JsonSerializationDem.git repository
public class JsonReaderTest {

    private Schedule schedule;
    ScheduledBlock coding;
    ScheduledBlock physics;
    ScheduledBlock math;
    ScheduledBlock breakfast;

    @BeforeEach
    void runBefore() {
        schedule = new Schedule(16, 40);
        coding = new ScheduledBlock("Coding", "13:00", 6, 26, "Homework");
        physics = new ScheduledBlock("Physics Lecture", "12:00", 1, 24, "Lecture");
        math = new ScheduledBlock("Math Lecture", "09:00", 2, 18, "Lecture");
        breakfast = new ScheduledBlock("Breakfast", "08:00", 2, 16, "Eating");
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Schedule schedule = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptySchedule.json");
        try {
            Schedule scheduleCopy = reader.read();
            assertEquals(40, scheduleCopy.getEnd());
            assertEquals(24, scheduleCopy.getAvailableTime());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSchedule.json");
        try {
            Schedule scheduleCopy = reader.read();
            assertEquals(40, scheduleCopy.getEnd());
            assertEquals(24-7,scheduleCopy.getAvailableTime());
            for (int i = 10; i < 16; i++) {
                assertEquals(coding.getName(), scheduleCopy.getBlocks().get(i).getName());
            }
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
