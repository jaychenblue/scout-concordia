package com.example.scoutconcordia;

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

    @Test
    public void getNameTest() {
        String output = b.getName();

        assertEquals("ABC", output);
    }

    @Test
    public void getAddressTest() {
        String output = b.getAddress();

        assertEquals("123", output);
    }

    @Test
    public void getOpeningTimesTest() {
        String output = b.getOpeningTimes();

        assertEquals("0800", output);
    }

    @After
    public void after() throws Exception {
        b = null;
        assertNull(b);
    }

}
