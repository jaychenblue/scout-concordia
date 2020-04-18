package com.example.scoutconcordia;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.scoutconcordia.DataStructures.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LinkedListTest
{
    
    static LinkedList<Integer> l;
    static Integer intTest;
    static Integer[] numbs;

    @BeforeEach
    public void beforeEach() throws Exception
    {
        numbs = new Integer[]{1, 2, 3};
        intTest = new Integer(1);
        l = new LinkedList<Integer>(intTest, numbs);
    }

    @AfterEach
    public void afterEach() throws Exception
    {
        numbs = null;
        intTest = null;
        l = null;
        assertNull(l);
    }
    
    // test constructors
    @Test
    public void testStandardConstructor()
    {
        LinkedList<Integer> list = new LinkedList<Integer>(1);

        assertNull(list.getHead());
        assertEquals(0, list.size());
        
        list = null;
    }
    
    @Test
    public void testCustomConstructor()
    {
        Integer[] arr = new Integer[]{5, 7, 9, 11, 13000000, -999999};
        LinkedList<Integer> list = new LinkedList<Integer>(1, arr);
        
        assertNotNull(list);
        assertEquals(6, list.size());
        
        list = null;
    }
    
    // test getters
    @Test
    public void testGetters()
    {
        assertEquals(3, l.size());
        assertNotNull(l.getHead());
        assertTrue(l.add(77));
    }
    
    // test clear
    @Test
    public void testClear()
    {
        l.clear();
        assertEquals(0, l.size());
        assertNotNull(l);
    }
    
    // test toString
    @Test
    public void testToString()
    {
        assertEquals("Size: 3\n" +
                     "Entry 0: 1\n" +
                     "Entry 1: 2\n" +
                     "Entry 2: 3\n",
                     l.toString());
    }

    @Test
    public void testContains()
    {
        assertTrue(l.contains(1));
        assertTrue(l.contains(2));
        assertTrue(l.contains(3));

        assertFalse(l.contains(0));
    }

    @Test
    public void testIndexOf()
    {
        assertEquals(0, l.indexOf(1));
        assertEquals(1, l.indexOf(2));
        assertEquals(2, l.indexOf(3));

        assertEquals(-1, l.indexOf(0));
    }

    @Test
    public void testToArray()
    {
        assertNotNull(l.toArray());
        assertEquals(1, l.toArray()[0]);
        assertEquals(2, l.toArray()[1]);
        assertEquals(3, l.toArray()[2]);
    }
    
    // test remove object
    @Test
    public void testRemoveOBJ()
    {
        assertTrue(l.removeOBJ(2));
    }

    // testing inner Node class
    @Test
    public void testNodeGetters()
    {
        LinkedList.Node node1 = l.new Node();
        LinkedList.Node node2 = l.new Node();
        LinkedList.Node node3 = l.new Node(3, node1, node2); // node1 is after, node2 is before

        assertEquals(3, node3.getEle());
        assertEquals(node1, node3.getNext());
        assertEquals(node2, node3.getPrev());

        node1 = null;
        node2 = null;
        node3 = null;
    }

    @Test
    public void testNodeToString()
    {
        LinkedList.Node node1 = l.new Node();
        LinkedList.Node node2 = l.new Node();
        LinkedList.Node node3 = l.new Node(3, node1, node2);

        assertEquals("3", node3.toString());

        node1 = null;
        node2 = null;
        node3 = null;
    }
    
}
