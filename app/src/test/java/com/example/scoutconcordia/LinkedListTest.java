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

        numbs = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        LinkedList<Integer> l2 = new LinkedList<Integer>(intTest, numbs);
        assertEquals("Size: 10\n" +
                        "Entry 0: 1\n" +
                        "Entry 1: 2\n" +
                        "Entry 2: 3\n" +
                        "Entry 3: 4\n" +
                        "Entry 4: 5\n" +
                        "Entry 5: 6\n" +
                        "Entry 6: 7\n" +
                        "Entry 7: 8\n" +
                        "Entry 8: 9\n" +
                        "Entry 9: 10\n",
                        l2.toString());

        numbs = new Integer[]{};
        l2 = new LinkedList<>(intTest, numbs);
        assertEquals("Size: 0\n",
                l2.toString());

        numbs = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
        l2 = new LinkedList<>(intTest, numbs);
        assertEquals("Size: 100\n" + "Entry 0: 1\n" + "Entry 1: 2\n" + "Entry 2: 3\n" +
                        "Entry 3: 4\n" + "Entry 4: 5\n" + "Entry 5: 6\n" + "Entry 6: 7\n" + "Entry 7: 8\n" +
                        "Entry 8: 9\n" + "Entry 9: 10\n" + "Entry 10: 11\n" + "Entry 11: 12\n" + "Entry 12: 13\n" +
                        "Entry 13: 14\n" + "Entry 14: 15\n" + "Entry 15: 16\n" + "Entry 16: 17\n" + "Entry 17: 18\n" +
                        "Entry 18: 19\n" + "Entry 19: 20\n" + "Entry 20: 21\n" + "Entry 21: 22\n" +
                        "Entry 22: 23\n" + "Entry 23: 24\n" + "Entry 24: 25\n" + "Entry 25: 26\n" + "Entry 26: 27\n" +
                        "Entry 27: 28\n" + "Entry 28: 29\n" + "Entry 29: 30\n" + "Entry 30: 31\n" + "Entry 31: 32\n" +
                        "Entry 32: 33\n" + "Entry 33: 34\n" + "Entry 34: 35\n" + "Entry 35: 36\n" + "Entry 36: 37\n" +
                        "Entry 37: 38\n" + "Entry 38: 39\n" + "Entry 39: 40\n" + "Entry 40: 41\n" + "Entry 41: 42\n" +
                        "Entry 42: 43\n" + "Entry 43: 44\n" + "Entry 44: 45\n" + "Entry 45: 46\n" + "Entry 46: 47\n" +
                        "Entry 47: 48\n" + "Entry 48: 49\n" + "Entry 49: 50\n" + "Entry 50: 51\n" + "Entry 51: 52\n" +
                        "Entry 52: 53\n" + "Entry 53: 54\n" + "Entry 54: 55\n" + "Entry 55: 56\n" + "Entry 56: 57\n" +
                        "Entry 57: 58\n" + "Entry 58: 59\n" + "Entry 59: 60\n" + "Entry 60: 61\n" + "Entry 61: 62\n" +
                        "Entry 62: 63\n" + "Entry 63: 64\n" + "Entry 64: 65\n" + "Entry 65: 66\n" + "Entry 66: 67\n" +
                        "Entry 67: 68\n" + "Entry 68: 69\n" + "Entry 69: 70\n" + "Entry 70: 71\n" + "Entry 71: 72\n" +
                        "Entry 72: 73\n" + "Entry 73: 74\n" + "Entry 74: 75\n" + "Entry 75: 76\n" + "Entry 76: 77\n" +
                        "Entry 77: 78\n" + "Entry 78: 79\n" + "Entry 79: 80\n" + "Entry 80: 81\n" + "Entry 81: 82\n" +
                        "Entry 82: 83\n" + "Entry 83: 84\n" + "Entry 84: 85\n" + "Entry 85: 86\n" +
                        "Entry 86: 87\n" + "Entry 87: 88\n" + "Entry 88: 89\n" + "Entry 89: 90\n" + "Entry 90: 91\n" +
                        "Entry 91: 92\n" + "Entry 92: 93\n" + "Entry 93: 94\n" + "Entry 94: 95\n" + "Entry 95: 96\n" +
                        "Entry 96: 97\n" + "Entry 97: 98\n" + "Entry 98: 99\n" + "Entry 99: 100\n",
                l2.toString());
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
