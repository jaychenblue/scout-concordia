package com.example.scoutconcordia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

import java.lang.Integer;

public class LinkedListTest {

    LinkedList<Integer> l;

    @Before
    public void before() throws Exception {
        Integer number = new Integer(1);

        l = new LinkedList<Integer>(number);
    }

    @After
    public void after() throws Exception {
        l = null;
        assertNull(l);
    }

    // test constructors
    @Test
    public void testStandardConstructor() {

    }

    @Test
    public void testCustomConstructor() {

    }

    // test getters
    @Test
    public void testGetters() {

    }

    // test adding stuff
    @Test
    public void testAddToHead() {

    }

    @Test
    public void testAddToTail() {

    }

    @Test
    public void testAddToIndex() {

    }

    // test nodes
    @Test
    public void testGetNodeFromIndex() {

    }

    @Test
    public void testRemoveNode() {

    }

    // test remove
    @Test
    public void testRemove() {

    }

    // test clear
    @Test
    public void testClear() {

    }

    // test toString
    @Test
    public void testToString() {

    }

    // test unsupported operations
    @Test
    public void testUnsupportedOperations() {

    }

    // test remove object
    @Test
    public void testRemoveOBJ() {

    }

}
