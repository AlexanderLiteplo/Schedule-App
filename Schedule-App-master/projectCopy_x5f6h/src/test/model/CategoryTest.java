package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Test class for Category
public class CategoryTest {
    Category c;

    @BeforeEach
    void before(){
        c = new Category("A",1);
    }

    @Test
    void incrementTimeTest() {
        c.incrementTime(4);
        assertEquals(5,c.getDuration());
    }

    @Test
    void decrementTimeTest() {
        c.decrementTime(1);
        assertEquals(0,c.getDuration());
    }

    @Test
    void toStringTest() {
        //test odd duration prints fraction
        assertEquals("A: 0.5 hour(s)",c.toString());
        //test even duration prints non fraction
        c.incrementTime(1);
        assertEquals("A: 1 hour(s)",c.toString());
    }

}
