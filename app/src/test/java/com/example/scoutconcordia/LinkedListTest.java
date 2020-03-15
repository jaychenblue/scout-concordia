package com.example.scoutconcordia;

import java.lang.Integer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
public class LinkedListTest {
    
    static LinkedList<Integer> l;
    static Integer[] numbs;
    
    @BeforeAll
    static void beforeAll() throws Exception {
        numbs = new Integer[]{1, 2, 3};
        Integer intTest = new Integer(1);
        l = new LinkedList<Integer>(intTest, numbs);
    }
    
    @AfterAll
    static void afterAll() throws Exception {
        l = null;
        assertNull(l);
    }
    
    // test constructors
    @Test
    @Order(1)
    public void testStandardConstructor() {
        LinkedList<Integer> list = new LinkedList<Integer>(1);
        
        assertNull(list.getHead());
        assertEquals(0, list.size());
        
        list = null;
    }
    
    @Test
    @Order(2)
    public void testCustomConstructor() {
        Integer[] arr = new Integer[]{5, 7, 9, 11, 13000000, -999999};
        LinkedList<Integer> list = new LinkedList<Integer>(1, arr);
        
        assertNotNull(list);
        assertEquals(6, list.size());
        
        list = null;
    }
    
    // test getters
    @Test
    @Order(4)
    public void testGetters() {
        assertEquals(3, l.size());
        assertNotNull(l.getHead());
        assertTrue(l.add(77));
    }
    
    // test clear
    @Test
    @Order(6)
    public void testClear() {
        l.clear();
        assertEquals(0, l.size());
        assertNotNull(l);
    }
    
    // test toString
    @Test
    @Order(3)
    public void testToString() {
        assertEquals("Size: 3\n" +
                     "Entry 0: 1\n" +
                     "Entry 1: 2\n" +
                     "Entry 2: 3\n",
                     l.toString());
    }
    
    // test remove object
    @Test
    @Order(5)
    public void testRemoveOBJ() {
        assertTrue(l.removeOBJ(2));
    }
    
}
