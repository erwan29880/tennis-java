package front.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;

import org.junit.Test;

import front.Trigo;

public class TrigoTest {

    Trigo t;

    public TrigoTest() {
        t = new Trigo(500, 600);
    }

    @Test 
    public void testRand() {
        boolean check = t.testRand();
        assertEquals(true, check);
    }

    @Test 
    public void testDenominateur() {
        assertNotEquals(t.testDenominateur(), 0);
    }

    @Test 
    public void testModifiyAngle() {
        t.testModifiyAngle().forEach(e -> {
            assertEquals(true, e);
        });
    }

    @Test 
    public void testDirections() {
        boolean[] checks = t.testDirections();
        for (int i = 0 ; i < 4 ; i++) {
            assertEquals(true, checks[i]);
        }
    }

}
