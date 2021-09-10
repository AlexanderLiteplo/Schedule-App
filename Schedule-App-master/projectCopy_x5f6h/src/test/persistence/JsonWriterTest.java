package persistence;

import model.Schedule;
import model.ScheduledBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//CITATION: this test class is a copied and modified version
//of the JsonWriterTest class from the JsonSerializationDem.git repository
public class JsonWriterTest {

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
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptySchedule.json");
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptySchedule.json");
            schedule = reader.read();
            assertEquals(40, schedule.getEnd());
            assertEquals(24, schedule.getAvailableTime());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Schedule schedule = new Schedule(16,40);
            schedule.addScheduledBlock(physics);
            schedule.addScheduledBlock(coding);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSchedule.json");
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralSchedule.json");
            Schedule scheduleCopy = reader.read();
            assertEquals(40, scheduleCopy.getEnd());

            assertEquals(24-7,schedule.getAvailableTime());
            for (int i = 10; i < 16; i++) {
                assertEquals(coding.getName(), scheduleCopy.getBlocks().get(i).getName());
            }

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
