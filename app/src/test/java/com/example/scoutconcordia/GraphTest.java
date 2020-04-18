package com.example.scoutconcordia;

import com.example.scoutconcordia.DataStructures.Graph;
import com.google.android.gms.maps.model.LatLng;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.internal.http1.Http1Codec;
import okhttp3.internal.http2.Http2Codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTest {

    Graph g;
    LatLng coordinate1, coordinate2;

    @BeforeEach
    public void beforeEach() throws Exception {
        g = new Graph(3);
        coordinate1 = new LatLng(45.494619, -73.577376); // SGW
        coordinate2 = new LatLng(45.458423, -73.640460); // Loyola
    }

    @AfterEach
    public void afterEach() throws Exception {
        g = null;
        coordinate1 = null;
        coordinate2 = null;
    }

    @Test
    public void testReset() {
        g.insertVertex(coordinate1, 1, "H-907");
        g.insertVertex(coordinate2, 1, "CC-110");
        assertNotNull(g.nodes());

        g.reset();
        assertNull(g.nodes());
    }

    // inserting elements

    @Test
    public void testInsertVertex() {
        assertTrue(g.insertVertex(coordinate1, 1, "H-907"));
        assertTrue(g.insertVertex(coordinate2, 1, "CC-110"));
        assertTrue(g.insertVertex(coordinate1, 1, "H-820"));
        assertFalse(g.insertVertex(coordinate2, 1, "CC-210")); // should be false because exceeds graph size of 3
    }

    @Test
    public void testInsertEdge() {
        assertEquals(-1, g.insertEdge(coordinate1, coordinate2)); // -1 if point not in graph

        g.insertVertex(coordinate1, 1, "H-907");
        g.insertVertex(coordinate2, 1, "CC-110");

        assertEquals(1, g.insertEdge(coordinate1, coordinate2)); // 1 if successful

        assertEquals(0, g.insertEdge(coordinate1, coordinate2)); // 0 if edge already exists
    }

    @Test
    public void testAreAdjacent() {
        assertFalse(g.areAdjacent(coordinate1, coordinate2));

        g.insertVertex(coordinate1, 1, "H-907");

        assertFalse(g.areAdjacent(coordinate1, coordinate2));

        g.insertVertex(coordinate2, 1, "CC-110");
        g.insertEdge(coordinate1, coordinate2);

        assertTrue(g.areAdjacent(coordinate1, coordinate2)); // now that both points are connected, should be true
    }

    // removing elements

    @Test
    public void testReplace() {
        assertFalse(g.replace(coordinate1, coordinate2)); // shouldn't work because nothing is in the graph yet

        g.insertVertex(coordinate1, 1, "H-907");

        assertTrue(g.replace(coordinate1, coordinate2));
    }

    @Test
    public void testRemoveEdge() {
        assertEquals(-1, g.removeEdge(coordinate1, coordinate2)); // nothing in the graph yet

        g.insertVertex(coordinate1, 1, "H-907");
        g.insertVertex(coordinate2, 1, "CC-110");
        assertEquals(0, g.removeEdge(coordinate1, coordinate2)); // nodes aren't connected yet

        g.insertEdge(coordinate1, coordinate2);
        assertEquals(1, g.removeEdge(coordinate1, coordinate2));
    }

    @Test
    public void testRemoveVertex() {
        assertEquals(-1, g.removeVertex(coordinate1));

        g.insertVertex(coordinate1, 1, "H-907");

        assertEquals(1, g.removeVertex(coordinate1));
    }

    @Test
    public void testIncidentVerticies() {
        g.insertVertex(coordinate1, 1, "H-907");

        assertNotNull(g.incidentVerticies(coordinate1));
    }

    // checking all elements and shortest path

    @Test
    public void testVertices() {
        g.insertVertex(coordinate1, 1, "H-907");

        assertNotNull(g.vertices());
    }

    @Test
    public void testNodes() {
        g.insertVertex(coordinate1, 1, "H-907");
        g.insertVertex(coordinate2, 1, "CC-110");
        g.insertVertex(coordinate2, 1, "CC-117");

        assertNotNull(g.nodes());
        assertEquals(coordinate1, g.nodes()[0].getElement());
        assertEquals(coordinate2, g.nodes()[1].getElement());
        assertEquals(coordinate2, g.nodes()[2].getElement());
    }

    @Test
    public void testSearchByClassName() {
        g.insertVertex(coordinate1, 1, "H-907");
        g.insertVertex(coordinate2, 1, "CC-110");

        assertEquals(coordinate1, g.searchByClassName("H-907"));
        assertEquals(coordinate2, g.searchByClassName("CC-110"));
    }

    @Test
    public void testBreathFirstSearch() {
        assertNull(g.breathFirstSearch(coordinate1, coordinate2));

        g.insertVertex(coordinate1, 1, "H-907");
        g.insertVertex(coordinate2, 1, "CC-110");
        g.insertEdge(coordinate1, coordinate2);

        assertNotNull(g.breathFirstSearch(coordinate1, coordinate2));
    }

    @Test
    public void testShortestPath() {
        LatLng ClassH100, HallwayHall1a, HallwayHall1b, HallwayHall1c,
                HallwayHall1d, HallwayHall1e, HallwayHall1f, HallwayHall1g,
                EscalatorHall1, EscalatorHall2,
                HallwayHall2a, HallwayHall2b, HallwayHall2c, HallwayHall2d,
                ClassH224;

        LatLng HallwayHall1e2, HallwayHall1f2, HallwayHall1g2, HallwayHall1h2,
                HallwayHall1i2, HallwayHall1j2, HallwayHall1k2;

        LatLng HallwayHall2a2, HallwayHall2b2, HallwayHall2c2, HallwayHall2d2,
                HallwayHall2e2, HallwayHall2f2;

        // this should be the shortest path
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

        // coordinates for alternate path on the first floor
        // starts with HallwayHall1d
        HallwayHall1e2 = new LatLng(45.4972,-73.57871);
        HallwayHall1f2 = new LatLng(45.497212,-73.578739);
        HallwayHall1g2 = new LatLng(45.497245,-73.578755);
        HallwayHall1h2 = new LatLng(45.497264,-73.578733);
        HallwayHall1i2 = new LatLng(45.497291,-73.578708);
        HallwayHall1j2 = new LatLng(45.497323,-73.578677);
        HallwayHall1k2 = new LatLng(45.497348,-73.578654);
        // ends at EscalatorHall1

        // coordinates for alternate path on the second floor
        // starts at EscalatorHall2
        HallwayHall2a2 = new LatLng(45.497292,-73.578604);
        HallwayHall2b2 = new LatLng(45.497252,-73.57864);
        HallwayHall2c2 = new LatLng(45.49723,-73.578632);
        HallwayHall2d2 = new LatLng(45.497205,-73.578663);
        HallwayHall2e2 = new LatLng(45.497167,-73.578697);
        HallwayHall2f2 = new LatLng(45.497192,-73.578756);
        // then goes to HallwayHall2d

        Graph testGraph = new Graph(30);

        // should return null because no path exists yet
        assertNull(testGraph.breathFirstSearch(ClassH100, ClassH224));

        assertTrue(testGraph.insertVertex(ClassH100, 0, "H-100"));
        assertTrue(testGraph.insertVertex(HallwayHall1a, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1b, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1c, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1d, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1e, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1f, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1g, 1, ""));
        assertTrue(testGraph.insertVertex(EscalatorHall1, 1, ""));
        assertTrue(testGraph.insertVertex(EscalatorHall2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2a, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2b, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2c, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2d, 1, ""));
        assertTrue(testGraph.insertVertex(ClassH224, 0, "H-224"));

        assertTrue(testGraph.insertVertex(HallwayHall1e2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1f2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1g2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1h2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1i2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1j2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall1k2, 1, ""));

        assertTrue(testGraph.insertVertex(HallwayHall2a2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2b2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2c2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2d2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2e2, 1, ""));
        assertTrue(testGraph.insertVertex(HallwayHall2f2, 1, ""));

        assertEquals(1, testGraph.insertEdge(ClassH100, HallwayHall1a));
        assertEquals(1, testGraph.insertEdge(HallwayHall1a, HallwayHall1b));
        assertEquals(1, testGraph.insertEdge(HallwayHall1b, HallwayHall1c));
        assertEquals(1, testGraph.insertEdge(HallwayHall1c, HallwayHall1d));
        assertEquals(1, testGraph.insertEdge(HallwayHall1d, HallwayHall1e));
        assertEquals(1,  testGraph.insertEdge(HallwayHall1e, HallwayHall1f));
        assertEquals(1, testGraph.insertEdge(HallwayHall1f, HallwayHall1g));
        assertEquals(1, testGraph.insertEdge(HallwayHall1g, EscalatorHall1));
        assertEquals(1, testGraph.insertEdge(EscalatorHall1, EscalatorHall2));
        assertEquals(1, testGraph.insertEdge(EscalatorHall2, HallwayHall2a));
        assertEquals(1, testGraph.insertEdge(HallwayHall2a, HallwayHall2b));
        assertEquals(1, testGraph.insertEdge(HallwayHall2b, HallwayHall2c));
        assertEquals(1, testGraph.insertEdge(HallwayHall2c, HallwayHall2d));
        assertEquals(1, testGraph.insertEdge(HallwayHall2d, ClassH224));

        // floor 1 alternate path
        assertEquals(1, testGraph.insertEdge(HallwayHall1d, HallwayHall1e2));
        assertEquals(1, testGraph.insertEdge(HallwayHall1e2, HallwayHall1f2));
        assertEquals(1, testGraph.insertEdge(HallwayHall1f2, HallwayHall1g2));
        assertEquals(1, testGraph.insertEdge(HallwayHall1g2, HallwayHall1h2));
        assertEquals(1, testGraph.insertEdge(HallwayHall1h2, HallwayHall1i2));
        assertEquals(1, testGraph.insertEdge(HallwayHall1i2, HallwayHall1j2));
        assertEquals(1, testGraph.insertEdge(HallwayHall1j2, HallwayHall1k2));
        assertEquals(1, testGraph.insertEdge(HallwayHall1k2, EscalatorHall1));

        // floor 2 alternate path
        assertEquals(1, testGraph.insertEdge(EscalatorHall2, HallwayHall2a2));
        assertEquals(1, testGraph.insertEdge(HallwayHall2a2, HallwayHall2b2));
        assertEquals(1, testGraph.insertEdge(HallwayHall2b2, HallwayHall2c2));
        assertEquals(1, testGraph.insertEdge(HallwayHall2c2, HallwayHall2d2));
        assertEquals(1, testGraph.insertEdge(HallwayHall2d2, HallwayHall2e2));
        assertEquals(1, testGraph.insertEdge(HallwayHall2e2, HallwayHall2f2));
        assertEquals(1, testGraph.insertEdge(HallwayHall2f2, HallwayHall2d));

        // check that the alternate path on floor 1 exists
        Object[] alternatePathFloor1 = testGraph.breathFirstSearch(ClassH100, HallwayHall1k2);
        assertNotNull(alternatePathFloor1);
        assertEquals(ClassH100, alternatePathFloor1[0]);
        assertEquals(HallwayHall1k2, alternatePathFloor1[alternatePathFloor1.length-1]);

        // check that the alternate path on floor 2 exists
        Object[] alternatePathFloor2 = testGraph.breathFirstSearch(EscalatorHall1, HallwayHall2f2);
        assertNotNull(alternatePathFloor2);
        assertEquals(EscalatorHall1, alternatePathFloor2[0]);
        assertEquals(HallwayHall2f2, alternatePathFloor2[alternatePathFloor2.length-1]);

        // should now return a path in the form of an array
        Object[] path = testGraph.breathFirstSearch(ClassH100, ClassH224);
        assertNotNull(path);

        // checking that the path returned is actually the shortest path
        assertEquals(ClassH100, path[0]);
        assertEquals(HallwayHall1a, path[1]);
        assertEquals(HallwayHall1b, path[2]);
        assertEquals(HallwayHall1c, path[3]);
        assertEquals(HallwayHall1d, path[4]);
        assertEquals(HallwayHall1e, path[5]);
        assertEquals(HallwayHall1f, path[6]);
        assertEquals(HallwayHall1g, path[7]);
        assertEquals(EscalatorHall1, path[8]);
        assertEquals(HallwayHall2a, path[9]);
        assertEquals(HallwayHall2b, path[10]);
        assertEquals(HallwayHall2c, path[11]);
        assertEquals(HallwayHall2d, path[12]);
        assertEquals(ClassH224, path[13]);

        testGraph = null;
    }

    // testing inner Node class

    @Test
    public void testNodeGetters() {
        Graph.Node node1 = g.new Node(coordinate1, 0, 1, "H-907");
        assertEquals(1, node1.getType());
        assertEquals(coordinate1, node1.getElement());
        assertEquals("H-907", node1.getRoom());

        node1 = null;
    }

    @Test
    public void testNodeEquals() {
        Graph.Node node1 = g.new Node(coordinate1, 0, 1, "H-907");
        Graph.Node node2 = g.new Node(coordinate1, 0, 1, "H-907");
        Graph.Node node3 = null;
        LatLng node4 = new LatLng(45.494619, -73.577376);
        String node5 = "random string";

        assertEquals(false, node1.equals(node3)); //check if the comparing node is null
        assertEquals(true, node1.equals(node2)); // check if the node.element is the same
        assertEquals(true, node1.equals(node4));  //check if the LatLng is the same
        assertEquals(false, node1.equals(node5)); //should return false as it is comparing to a garbage value

        node1 = null;
        node2 = null;
        node3 = null;
        node4 = null;
        node5 = null;
    }

}
