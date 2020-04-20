package com.example.scoutconcordia;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.scoutconcordia.DataStructures.LinkedList;
import com.example.scoutconcordia.MapInfoClasses.BuildingInfo;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BuildingInfoTest
{
    
    static BuildingInfo b;
    
    @BeforeAll
    static void beforeAll() throws Exception
    {
        b = new BuildingInfo("ABC", "123", "0800");
    }
    
    // test getters
    @Test
    public void testGetters()
    {
        assertEquals("ABC", b.getName());
        assertEquals("123", b.getAddress());
        assertEquals("0800", b.getOpeningTimes());
    }
    
    // test constructor
    @Test
    public void testConstructor()
    {
        BuildingInfo building = new BuildingInfo("XYZ", "145 St-Laurent", "0700-1900");
        
        assertEquals("XYZ", building.getName());
        assertEquals("145 St-Laurent", building.getAddress());
        assertEquals("0700-1900", building.getOpeningTimes());
        
        building = null;
    }

    @Test
    public void testToString()
    {
        LinkedList<LatLng> newCoordinates;
        newCoordinates = new LinkedList<>(new LatLng(0,0));
        BuildingInfo b2 = new BuildingInfo();
        b2.setName("HALL");
        b2.setAddress("ABC");
        b2.setOpeningTimes("1200");
        b2.setCoordinates(newCoordinates);

        assertEquals("Name: HALL\n" +
                        "Address: ABC\n" +
                        "IconName: smiling.png\n" +
                        "OpeningTimes: 1200\n" +
                        "Coordinates: Size: 0\n\n" +
                        "Center: null", b2.toString());
    }

    @Test
    public void testGetCenter()
    {
        b.setCenter(new LatLng(0, 0));
        assertEquals(new LatLng(0,0), b.getCenter());
    }

    @Test
    public void testGetIconName()
    {
        b.setIconName("smiley.png");
        assertEquals("smiley.png", b.getIconName());
    }

    @Test
    public void testGetCoordinates()
    {
        LinkedList<LatLng> newCoordinates;
        newCoordinates = new LinkedList<>(new LatLng(0,0));
        b.setCoordinates(newCoordinates);

        assertEquals(newCoordinates , b.getCoordinates());
    }
    
    @AfterAll
    static void afterAll() throws Exception
    {
        b = null;
        assertNull(b);
    }
    
}
