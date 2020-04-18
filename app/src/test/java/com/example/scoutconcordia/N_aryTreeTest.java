package com.example.scoutconcordia;

import com.example.scoutconcordia.DataStructures.LinkedList;
import com.example.scoutconcordia.DataStructures.N_aryTree;
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

    LatLng coordinate1, coordinate2, coordinate3;
    LatLng escalatorHall1, hallwayHall1a, hallwayHall1b, classH100;
    LatLng escalatorHall2, hallwayHall2a, hallwayHall2b, classH224;
    N_aryTree tree;
    N_aryTree.TreeNode node1, node2, node3;
    LinkedList<N_aryTree.TreeNode> l;

    @BeforeEach
    public void beforeEach() throws Exception {
        coordinate1 = new LatLng(45.494619, -73.577376); // SGW
        coordinate2 = new LatLng(45.458423, -73.640460); // Loyola
        coordinate3 = new LatLng(45.496827, -73.578849); // Hall building

        tree = new N_aryTree();
        node1 = tree.new TreeNode(null, coordinate1);
        node2 = tree.new TreeNode(node1, coordinate2);
        node3 = tree.new TreeNode(node1, coordinate3);
    }

    @AfterEach
    public void afterEach() throws Exception {
        coordinate1 = null;
        coordinate2 = null;
        coordinate3 = null;
        tree = null;
        node1 = null;
        node2 = null;
        node3 = null;
    }

    // === tests for nested TreeNode class ===

    @Test
    public void testGetters() {
        node2.setParent(node1);
        assertEquals(node1, node2.getParent());

        l = new LinkedList<N_aryTree.TreeNode>(node3);
        node2.setChildren(l);
        assertEquals(l, node2.getChildren());

        node2.setElement(coordinate1);
        assertEquals(coordinate1, node2.getElement());

        l = null;
    }

    @Test
    public void testAddToChildren() {
        assertTrue(node1.addToChildren(coordinate3));
    }

    @Test
    public void testEquals() {
        // node 1 and node 2/3 have different elements
        assertFalse(node1.equals(node2));
        assertFalse(node1.equals(node3));

        // node 2 and node 3 have different elements
        assertFalse(node2.equals(node3));
    }

    // ... end of tests for nested TreeNode class ...

    @Test
    public void testGetHead() {
        assertNotNull(tree.getHead());
    }

    @Test
    public void testFindSpecifiedNode() {
        assertNull(tree.findSpecifiedNode(null, null));

        assertNotNull(tree.findSpecifiedNode(node1, coordinate1)); // should find itself
        assertEquals(node1, tree.findSpecifiedNode(node1, coordinate1)); // should find itself

        assertNull(tree.findSpecifiedNode(node1, coordinate2)); // node 1 has no children

        node1.setElement(coordinate1);
        assertEquals(coordinate1, node1.getElement());

        node2.setElement(coordinate2);
        assertEquals(coordinate2, node2.getElement());

        node2.setParent(node1);
        assertEquals(node1, node2.getParent());

        l = new LinkedList<N_aryTree.TreeNode>(node2);
        node1.setChildren(l);
        assertEquals(l, node1.getChildren());

        node1.addToChildren(coordinate2);

        assertNotNull(tree.findSpecifiedNode(node1, coordinate2));

        l = null;
    }

    @Test
    public void testGetPath() {
        assertNull(tree.getPath(null, null));

        node1.setElement(coordinate1);
        assertEquals(coordinate1, node1.getElement());

        node2.setElement(coordinate2);
        assertEquals(coordinate2, node2.getElement());

        node2.setParent(node1);
        assertEquals(node1, node2.getParent());

        node1.addToChildren(coordinate2);

        tree.setHead(node1);

        assertEquals(coordinate1, tree.getHead().getElement());

        assertNotNull(tree.getPath(coordinate1, coordinate2));

        assertEquals(coordinate1, tree.getPath(coordinate1, coordinate2)[0]);
        assertEquals(coordinate2, tree.getPath(coordinate1, coordinate2)[1]);
    }
}
