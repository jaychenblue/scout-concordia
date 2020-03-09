package com.example.scoutconcordia;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuildingInfoTest {

    @Test
    public void getNameTest() {
        BuildingInfo b = new BuildingInfo("ABC", "123", "0800");

        String output = b.getName();

        assertEquals("ABC", output);
    }

}
