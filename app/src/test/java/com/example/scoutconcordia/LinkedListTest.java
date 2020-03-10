package com.example.scoutconcordia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.Integer;

public class LinkedListTest {

    LinkedList<Integer> l;
    Integer[] numbs;

    @Before
    public void before() throws Exception {
        numbs = new Integer[]{1, 2, 3};

        l = new LinkedList<Integer>(1, numbs);
    }

    @After
    public void after() throws Exception {
        numbs = null;
        l = null;
        assertNull(l);
    }

    // test constructors
    @Test
    public void testStandardConstructor() {
        LinkedList<Integer> list = new LinkedList<Integer>(1);

        assertNull(list.getHead());
        assertEquals(0, list.size());

        list = null;
    }

    @Test
    public void testCustomConstructor() {
        Integer[] arr = new Integer[]{5, 7, 9, 11, 13000000, -999999};
        LinkedList<Integer> list = new LinkedList<Integer>(1, arr);

        assertNotNull(list);
        assertEquals(6, list.size());
    }

    // test getters
    @Test
    public void testGetters() {
        assertEquals(3, l.size());
        assertNotNull(l.getHead());
        assertNotNull(l.remove(1));
        assertTrue(l.add(77));
    }

    // test clear
    @Test
    public void testClear() {
        l.clear();
        assertEquals(0, l.size());
        assertNotNull(l);
    }

    // test toString
    @Test
    public void testToString() {
        assertEquals("Size: 3\n" +
                "Entry 0: 1\n" +
                "Entry 1: 2\n" +
                "Entry 2: 3\n",
                l.toString());
    }

    // test remove object
    @Test
    public void testRemoveOBJ() {
        assertTrue(l.removeOBJ(2));
    }

}
