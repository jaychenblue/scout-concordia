package com.example.scoutconcordia;

import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BuildingInfoTest {

    BuildingInfo b;

    @Before
    public void before() throws Exception {
        b = new BuildingInfo("ABC", "123", "0800");
    }

    // test getters
    @Test
    public void testGetters() {
        assertEquals("ABC", b.getName());
        assertEquals("123", b.getAddress());
        //assertEquals("smiling.png", b.getIconName());
        //assertEquals("(0,0)", b.getCoordinates());
        assertEquals("0800", b.getOpeningTimes());
    }

    // test toString method
//    @Test
//    public void testToString() {
//        assertEquals("Name: ABC\n" +
//                "Address: 123\n" +
//                "IconName: smiling.png\n" +
//                "OpeningTimes: 0800\n" +
//                "Coordinates:(0,0)\n" +
//                "Center:",
//                b.toString());
//    }

    // test constructor
    @Test
    public void testConstructor() {
        BuildingInfo building = new BuildingInfo("XYZ", "145 St-Laurent", "0700-1900");

        assertEquals("XYZ", building.getName());
        assertEquals("145 St-Laurent", building.getAddress());
        assertEquals("0700-1900", building.getOpeningTimes());

        building = null;
    }

    // testing the linked list
//    @Test
//    public void testObtainBuildings() {
//
//    }

    @After
    public void after() throws Exception {
        b = null;
        assertNull(b);
    }

}
