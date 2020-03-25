package com.example.scoutconcordia;

import com.google.android.gms.maps.model.LatLng;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class N_aryTreeTest {

    LatLng coordinate1, coordinate2;

    @BeforeEach
    public void beforeEach() throws Exception {
        coordinate1 = new LatLng(45.494619, -73.577376); // SGW
        coordinate2 = new LatLng(45.458423, -73.640460); // Loyola
    }

    @AfterEach
    public void afterEach() throws Exception {
        coordinate1 = null;
        coordinate2 = null;
    }

    // nested TreeNode class

    @Test
    public void testGetters() {

    }

    @Test
    public void testAddToChildren() {

    }

    @Test
    public void testEquals() {

    }

    // ---

    @Test
    public void testGetHead() {

    }

    @Test
    public void testFindSpecifiedNode() {

    }

    @Test
    public void testGetPath() {

    }

}
