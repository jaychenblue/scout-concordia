package com.example.scoutconcordia;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BuildingInfoTest {
    
    static BuildingInfo b;
    
    @BeforeAll
    static void beforeAll() throws Exception {
        b = new BuildingInfo("ABC", "123", "0800");
    }
    
    // test getters
    @Test
    public void testGetters() {
        assertEquals("ABC", b.getName());
        assertEquals("123", b.getAddress());
        assertEquals("0800", b.getOpeningTimes());
    }
    
    // test constructor
    @Test
    public void testConstructor() {
        BuildingInfo building = new BuildingInfo("XYZ", "145 St-Laurent", "0700-1900");
        
        assertEquals("XYZ", building.getName());
        assertEquals("145 St-Laurent", building.getAddress());
        assertEquals("0700-1900", building.getOpeningTimes());
        
        building = null;
    }
    
    @AfterAll
    static void afterAll() throws Exception {
        b = null;
        assertNull(b);
    }
    
}
