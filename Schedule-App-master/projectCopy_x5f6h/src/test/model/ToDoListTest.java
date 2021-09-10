package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Test class for ToDoList
public class ToDoListTest {
    ToDoList list;
    ToDoList list2;
    UnscheduledBlock mathHomework;
    UnscheduledBlock pianoPractice;
    UnscheduledBlock guitarPractice;

    @BeforeEach
    void before(){
        list = new ToDoList(10);
        list2 = new ToDoList(10);
        list2.addTask("Math Homework", 2, "Homework");
        list2.addTask("Piano Practice", 1, "Music");
        mathHomework = new UnscheduledBlock("Math Homework", 2, "Homework");
        pianoPractice = new UnscheduledBlock("Piano Practice", 1, "Music");
        guitarPractice = new UnscheduledBlock("Guitar Practice", 8, "Music");
    }

    @Test
    void addTaskTest() {
        list.addTask("A",3,"cA");
        //test length
        assertEquals(1,list.size());
        //test correctly added
        assertEquals("A",list.get(0).getName());
        //test duration sum incremented
        assertEquals(3,list.getDurationSum());
        //test availabletime decremented
        assertEquals(7,list.getAvailableTime());
    }

    @Test
    void removeTaskTest() {
        //test not on list
        assertFalse(list2.removeTask("NotOnList"));
        //test removing something from list
        assertTrue(list2.removeTask("Math Homework"));
        assertEquals(1,list2.size());
        //test avalabletime changed properly
        assertEquals(9,list2.getAvailableTime());
        //test duration sum changed properly
        assertEquals(1,list2.getDurationSum());
    }

    @Test
    void getTaskTest() {
        //test on list
        assertEquals("Math Homework",list2.getTask("Math Homework").getName());
        //test another
        assertEquals("Piano Practice",list2.getTask("Piano Practice").getName());
        //test not on list
        assertEquals("",list2.getTask("pizza party").getName());
    }

    @Test
    void testDecrementTask(){
        //test nothing happens when match not found
        list2.decrementTask("Bananasss",4);
        assertEquals(2,list2.size());
        //math homework unchanged
        assertEquals(2,list2.getBlocks().get(0).getDuration());
        //piano practice unchanged
        assertEquals(1,list2.getBlocks().get(1).getDuration());

        //test duration properly decremented
        list2.decrementTask("Math Homework",1);
        assertEquals(1,list2.getTask("Math Homework").getDuration());
        //test availabletime
        assertEquals(8,list2.getAvailableTime());
        //test sum duration
        assertEquals(2,list2.getDurationSum());
    }
    @Test
    void decrementTaskTooMuchTest() {
        //test decrementing item more than needed
        list2.decrementTask("Math Homework",3);
        //test item deleted
        assertEquals(1,list2.size());
        //test available time inc
        assertEquals(9,list2.getAvailableTime());
        //test sum duration dec
        assertEquals(1,list2.getDurationSum());
    }

    @Test
    void incrementTaskTest() {
        //test item incremented properly
        list2.incrementTask("Math Homework",3);
        assertEquals(5,list2.getTask("Math Homework").getDuration());
        //test availabletime
        assertEquals(4,list2.getAvailableTime());
        //test sum duration
        assertEquals(6,list2.getDurationSum());
    }


}
