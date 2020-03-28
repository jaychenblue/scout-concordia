package com.example.scoutconcordia;

import com.example.scoutconcordia.MapInfoClasses.ShuttleInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShuttleInfoTest {

    ShuttleInfo shuttleInfo;

    @BeforeEach
    public void beforeEach() throws Exception {
        shuttleInfo = new ShuttleInfo();
    }

    @AfterEach
    public void afterEach() throws Exception {
        shuttleInfo = null;
    }

    // based on the implementation choices, the methods being tested only work with the current time
    // as a result, the test results change and cannot be predicted -- we can only test for null values

    @Test
    public void testGetEstimatedRouteTimeFromSGW() {
        assertNotNull(shuttleInfo.getEstimatedRouteTimeFromSGW());
    }

    @Test
    public void testGetEstimatedRouteTimeFromLoyola() {
        assertNotNull(shuttleInfo.getEstimatedRouteTimeFromLoyola());
    }

}
