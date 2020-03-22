package com.example.scoutconcordia;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DataStructures.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LinkedListTest {
    
    static LinkedList<Integer> l;
    static Integer intTest;
    static Integer[] numbs;

    @BeforeEach
    public void beforeEach() throws Exception {
        numbs = new Integer[]{1, 2, 3};
        intTest = new Integer(1);
        l = new LinkedList<Integer>(intTest, numbs);
    }

    @AfterEach
    public void afterEach() throws Exception {
        numbs = null;
        intTest = null;
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
        
        list = null;
    }
    
    // test getters
    @Test
    public void testGetters() {
        assertEquals(3, l.size());
        assertNotNull(l.getHead());
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
