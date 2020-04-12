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

    @Test
    public void testShortestPath() {
        LatLng ClassH100, HallwayHall1a, HallwayHall1b, HallwayHall1c,
                HallwayHall1d, HallwayHall1e, HallwayHall1f, HallwayHall1g,
                EscalatorHall1, EscalatorHall2,
                HallwayHall2a, HallwayHall2b, HallwayHall2c, HallwayHall2d,
                ClassH224;

        ClassH100 = new LatLng(45.497022,-73.578662);
        HallwayHall1a = new LatLng(45.497013,-73.578742);
        HallwayHall1b = new LatLng(45.497074,-73.578776);
        HallwayHall1c = new LatLng(45.497118,-73.578719);
        HallwayHall1d = new LatLng(45.497171,-73.578662);
        HallwayHall1e = new LatLng(45.497203,-73.578618);
        HallwayHall1f = new LatLng(45.497244,-73.578591);
        HallwayHall1g = new LatLng(45.497295,-73.578553);
        EscalatorHall1 = new LatLng(45.497324,-73.57861);
        EscalatorHall2 = new LatLng(45.497324,-73.57861);
        HallwayHall2a = new LatLng(45.497339,-73.578656);
        HallwayHall2b = new LatLng(45.4973,-73.578736);
        HallwayHall2c = new LatLng(45.497267,-73.57882);
        HallwayHall2d = new LatLng(45.497235,-73.578873);
        ClassH224 = new LatLng(45.4972247,-73.57894);

        N_aryTree testTree = new N_aryTree();

        N_aryTree.TreeNode classNodeH100 = testTree.new TreeNode(null, ClassH100);
        N_aryTree.TreeNode hallwayNodeHall1a = testTree.new TreeNode(classNodeH100, HallwayHall1a);
        N_aryTree.TreeNode hallwayNodeHall1b = testTree.new TreeNode(hallwayNodeHall1a, HallwayHall1b);
        N_aryTree.TreeNode hallwayNodeHall1c = testTree.new TreeNode(hallwayNodeHall1b, HallwayHall1c);
        N_aryTree.TreeNode hallwayNodeHall1d = testTree.new TreeNode(hallwayNodeHall1c, HallwayHall1d);
        N_aryTree.TreeNode hallwayNodeHall1e = testTree.new TreeNode(hallwayNodeHall1d, HallwayHall1e);
        N_aryTree.TreeNode hallwayNodeHall1f = testTree.new TreeNode(hallwayNodeHall1e, HallwayHall1f);
        N_aryTree.TreeNode hallwayNodeHall1g = testTree.new TreeNode(hallwayNodeHall1f, HallwayHall1g);
        N_aryTree.TreeNode escalatorNodeHall1 = testTree.new TreeNode(hallwayNodeHall1g, EscalatorHall1);
        N_aryTree.TreeNode escalatorNodeHall2 = testTree.new TreeNode(escalatorNodeHall1, EscalatorHall1);
        N_aryTree.TreeNode hallwayNodeHall2a = testTree.new TreeNode(escalatorNodeHall2, HallwayHall2a);
        N_aryTree.TreeNode hallwayNodeHall2b = testTree.new TreeNode(hallwayNodeHall1a, HallwayHall2b);
        N_aryTree.TreeNode hallwayNodeHall2c = testTree.new TreeNode(hallwayNodeHall2b, HallwayHall2c);
        N_aryTree.TreeNode hallwayNodeHall2d = testTree.new TreeNode(hallwayNodeHall2c, HallwayHall2d);
        N_aryTree.TreeNode classNodeH224 = testTree.new TreeNode(hallwayNodeHall2d, ClassH224);

        classNodeH100.setElement(ClassH100);
        hallwayNodeHall1a.setElement(HallwayHall1a);
        hallwayNodeHall1b.setElement(HallwayHall1b);
        hallwayNodeHall1c.setElement(HallwayHall1c);
        hallwayNodeHall1d.setElement(HallwayHall1d);
        hallwayNodeHall1e.setElement(HallwayHall1e);
        hallwayNodeHall1f.setElement(HallwayHall1f);
        hallwayNodeHall1g.setElement(HallwayHall1g);
        escalatorNodeHall1.setElement(EscalatorHall1);
        escalatorNodeHall2.setElement(EscalatorHall2);
        hallwayNodeHall2a.setElement(HallwayHall2a);
        hallwayNodeHall2b.setElement(HallwayHall2b);
        hallwayNodeHall2c.setElement(HallwayHall2c);
        hallwayNodeHall2d.setElement(HallwayHall2d);
        classNodeH224.setElement(ClassH224);

        hallwayNodeHall1a.setParent(classNodeH100);
        hallwayNodeHall1b.setParent(hallwayNodeHall1a);
        hallwayNodeHall1c.setParent(hallwayNodeHall1b);
        hallwayNodeHall1d.setParent(hallwayNodeHall1c);
        hallwayNodeHall1e.setParent(hallwayNodeHall1d);
        hallwayNodeHall1f.setParent(hallwayNodeHall1e);
        hallwayNodeHall1g.setParent(hallwayNodeHall1f);
        escalatorNodeHall1.setParent(hallwayNodeHall1g);
        escalatorNodeHall2.setParent(escalatorNodeHall1);
        hallwayNodeHall2a.setParent(escalatorNodeHall2);
        hallwayNodeHall2b.setParent(hallwayNodeHall2a);
        hallwayNodeHall2c.setParent(hallwayNodeHall2b);
        hallwayNodeHall2d.setParent(hallwayNodeHall2c);

        classNodeH100.addToChildren(HallwayHall1a);
        hallwayNodeHall1a.addToChildren(HallwayHall1b);
        hallwayNodeHall1b.addToChildren(HallwayHall1c);
        hallwayNodeHall1c.addToChildren(HallwayHall1d);
        hallwayNodeHall1d.addToChildren(HallwayHall1e);
        hallwayNodeHall1e.addToChildren(HallwayHall1f);
        hallwayNodeHall1f.addToChildren(HallwayHall1g);
        hallwayNodeHall1g.addToChildren(EscalatorHall1);
        escalatorNodeHall1.addToChildren(EscalatorHall2);
        escalatorNodeHall2.addToChildren(HallwayHall2a);
        hallwayNodeHall2a.addToChildren(HallwayHall2b);
        hallwayNodeHall2b.addToChildren(HallwayHall2c);
        hallwayNodeHall2c.addToChildren(HallwayHall2d);

        testTree.setHead(classNodeH100);

        assertEquals(ClassH100, testTree.getHead().getElement());

        assertEquals(ClassH100, testTree.getPath(ClassH100, HallwayHall1b)[0]); // this is supposed to work but fails right now
    }

}
