package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Tests for classes: Block, Schedule, ScheduledBlock, Timeable, UnscheduledBock
public class ScheduleTest {
    private Schedule schedule1;
    private Schedule schedule2;
    private Schedule schedule3;
    ScheduledBlock coding;
    ScheduledBlock physics;
    ScheduledBlock math;
    ScheduledBlock breakfast;
    UnscheduledBlock mathHomework;
    UnscheduledBlock pianoPractice;
    UnscheduledBlock guitarPractice;

    @BeforeEach
    void runBefore() {
        schedule1 = new Schedule(0, 48);
        schedule3 = new Schedule(16, 40);
        coding = new ScheduledBlock("Coding", "13:00", 6, 26, "Homework");
        physics = new ScheduledBlock("Physics Lecture", "12:00", 1, 24, "Lecture");
        math = new ScheduledBlock("Math Lecture", "09:00", 2, 18, "Lecture");
        breakfast = new ScheduledBlock("Breakfast", "08:00", 2, 16, "Eating");
        mathHomework = new UnscheduledBlock("Math Homework", 2, "Homework");
        pianoPractice = new UnscheduledBlock("Piano Practice", 1, "Music");
        guitarPractice = new UnscheduledBlock("Guitar Practice", 1, "Music");
    }

    @Test
    void numHalfHourBlocksTest() {
        schedule1 = new Schedule(16, 35);
        assertEquals(35 - 16, schedule1.getNumHalfHourBlocks());
        schedule2 = new Schedule(0, 0);
        assertEquals(0, schedule2.getNumHalfHourBlocks());
    }

    @Test
    void initializeScheduleTest() {
        schedule1 = new Schedule(16, 40);
        assertEquals(40, schedule3.getEnd());
        //is schedule correct size
        assertEquals(40 - 16, schedule1.getBlocks().size());
        //check name
        assertEquals("Free Time", schedule1.getBlocks().get(2).getName());
        //check string time half hour
        assertEquals("08:30", ((ScheduledBlock) schedule1.getBlocks().get(1)).getTimeString());
        //check string time hour
        assertEquals("09:00", ((ScheduledBlock) schedule1.getBlocks().get(2)).getTimeString());
        //check for correct int time
        assertEquals(16, ((ScheduledBlock) schedule1.getBlocks().get(0)).getTime());
        //test if category added and incremented properly
        schedule1.initializeCategories();
        assertEquals(24, schedule1.getCategories().get(0).getDuration());
        assertEquals("Free Time", schedule1.getCategories().get(0).getName());
        assertEquals(1, schedule1.getCategories().size());
    }

    @Test
    void addScheduledBlockNoConflictTest() {
        //test if ScheduledBlock of duration 1 is added
        schedule3.addScheduledBlock(physics);
        //test if available time decremented properly
        assertEquals(24 - physics.getDuration(), schedule3.getAvailableTime());
        //test block name added to correct position
        assertEquals(physics.getName(), schedule3.getBlocks().get(8).getName());
        //test adding a ScheduledBlock of duration > 1
        schedule3.addScheduledBlock(coding);
        //Only checking if name matches because constructor has been
        //implicitly tested elsewhere
        for (int i = 10; i < 16; i++) {
            assertEquals(coding.getName(), schedule3.getBlocks().get(i).getName());
        }
    }

    @Test
    void addScheduledBlockConflictTest() {
        schedule3.addScheduledBlock(physics);
        schedule3.addScheduledBlock(new ScheduledBlock("Overwrite", "12:00", 1, 24, "new"));
        //test overwrite
        assertEquals("Overwrite", schedule3.getBlocks().get(8).getName());
        //test availableTime not decremented extra
        assertEquals(23, schedule3.getAvailableTime());
    }

    @Test
    void addScheduledBlockFreeTimeTest() {
        physics.setName("Free Time");
        schedule3.addScheduledBlock(physics);
        //check availableTime unchanged
        assertEquals(24, schedule3.getAvailableTime());
    }

    @Test
    void checkBusyTest() {
        //test if list of correct blocks are returned
        schedule3.addScheduledBlock(physics);
        ArrayList<ScheduledBlock> busyBlocks = schedule3.checkBusy(physics.getTime(), physics.getDuration());
        //test name
        assertEquals("Physics Lecture", busyBlocks.get(0).getName());
        //test size
        assertEquals(1, busyBlocks.size());
        //test multiple busyBLocks
        schedule3.addScheduledBlock(coding);
        busyBlocks = schedule3.checkBusy(coding.getTime(), coding.getDuration());
        //check names
        for (ScheduledBlock sb : busyBlocks) {
            assertEquals("Coding", sb.getName());
        }
        //test correct size
        assertEquals(6, busyBlocks.size());
        //test when only some blocks are busy
        busyBlocks = schedule3.checkBusy(coding.getTime() - 1, coding.getDuration() + 1);
        assertEquals(6, busyBlocks.size());
        //test empty list returned when no interference
        busyBlocks = schedule1.checkBusy(0, 4);
        assertEquals(0, busyBlocks.size());
    }


    @Test
    void addNewCategoryTest() {
        //test new category added
        schedule3.addScheduledBlock(physics);
        schedule3.initializeCategories();
        assertEquals("Lecture", schedule3.getCategories().get(1).getName());
        assertEquals(1, schedule3.getCategories().get(1).getDuration());
        //test free time duration decremented
        assertEquals(23, schedule3.getCategories().get(0).getDuration());
        //test adding another category
        schedule3.addScheduledBlock(coding);
        schedule3.getCategories().clear();
        schedule3.initializeCategories();
        assertEquals(3, schedule3.getCategories().size());
        //test correct duration
        assertEquals(6, schedule3.getCategories().get(2).getDuration());
    }

    @Test
    void addToExistingCategoryTest() {
        //test if adding multiple blocks of same category
        //only creates one new category
        schedule3.addScheduledBlock(math);
        schedule3.addScheduledBlock(physics);
        schedule3.initializeCategories();
        assertEquals(2, schedule3.getCategories().size());
        assertEquals("Lecture", schedule3.getCategories().get(1).getName());
        //test duration incremented correctly
        assertEquals(3, schedule3.getCategories().get(1).getDuration());
    }

    @Test
    void addTaskEmptyScheduleTest() {
        //test adding task to empty (Free Time) schedule
        schedule3.addTask(pianoPractice);
        assertEquals("Piano Practice", schedule3.getBlocks().get(0).getName());
        assertEquals(pianoPractice.getDuration(), schedule3.getBlocks().get(0).getDuration());
        //test new category added
        schedule3.initializeCategories();
        assertEquals(2, schedule3.getCategories().size());
    }

    @Test
    void addTooManyTasksTest() {
        //test schedule doesn't overload
        for (int i = 0; i < 100; i++) {
            schedule3.addTask(mathHomework);
        }
        assertEquals(24, schedule3.getBlocks().size());
    }

    @Test
    void addTaskNonEmptyScheduleTest() {
        //test added task does not overwrite existing block
        schedule3.addScheduledBlock(breakfast);
        schedule3.addTask(mathHomework);
        //test breakfast unchanged
        assertEquals("Breakfast", schedule3.getBlocks().get(0).getName());
        assertEquals("Breakfast", schedule3.getBlocks().get(1).getName());
        //test mathHomework added
        assertEquals("Math Homework", schedule3.getBlocks().get(2).getName());
        assertEquals("Math Homework", schedule3.getBlocks().get(3).getName());
    }

    @Test
    void scheduledBlockToStringTest() {
        assertEquals("08:00 | Breakfast", breakfast.toString());
    }

    @Test
    void unscheduledBlockTostringTest() {
        String s = "Math Homework:  duration = 2 category = Homework";
        assertEquals(s, mathHomework.toString());
    }

    @Test
    void decrementTimeTest() {
        mathHomework.decrementTime(1);
        assertEquals(1, mathHomework.getDuration());
    }

    @Test
    void scheduleToJsonTest() {
        JSONObject json = schedule3.scheduleToJson();
        assertEquals(16, json.getNumber("start"));
        assertEquals(40, json.getNumber("end"));

        schedule3.addScheduledBlock(physics);
        json = schedule3.scheduleToJson();
        JSONArray test = json.getJSONArray("scheduledBlocks");
        List<Object> scheduleCopy = test.toList();
        assertEquals(24, scheduleCopy.size());

//        System.out.println(newList);
//        assertEquals();
    }


}
