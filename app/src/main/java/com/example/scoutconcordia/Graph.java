package com.example.scoutconcordia;

import com.google.android.gms.maps.model.LatLng;

// Assumption is no two points are equivalent
public class Graph
{
    private int numberOfNodes;
    private Node[] nodes;

    private class Node
    {
        private int id;
        private LatLng element;
        private LinkedList<Node> adjacencyList;

        public Node(LatLng element, int id)
        {
            this.element = element;
            this.id = id;
            adjacencyList = new LinkedList<Node>(this);
        }

        public boolean equals(Node n1)
        {
            return this.element.equals(n1.element);
        }

        public boolean equals(LatLng ele)
        {
            return this.element.equals(ele);
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public LatLng getElement() { return element; }
        public void setElement(LatLng element) { this.element = element; }
    }

    public Graph(int size)
    {
        numberOfNodes = 0;
        nodes = new Node[size];
    }

    public void reset()
    {
        nodes = null;
        numberOfNodes = 0;
    }

    private int getID(LatLng ele)
    {
        int id = -1;
        for (int i = 0; i < numberOfNodes; i++)
        {
            if (nodes[i].equals(ele))
                id = i;
        }
        return id;
    }

    public void insertVertex(LatLng element)
    {
        nodes[numberOfNodes] = new Node(element, numberOfNodes);
        numberOfNodes++;
    }

    // returns 1 if successful, 0 if the edge already exists, -1 if point not in graph
    public int insertEdge(LatLng ele1, LatLng ele2)
    {
        int id1 = getID(ele1), id2 = getID(ele2);
        if (id1 < 0 || id2 < 0)
            return -1;
        if (nodes[id1].adjacencyList.contains(nodes[id2]))
            return 0;
        nodes[id1].adjacencyList.add(nodes[id2]);
        nodes[id2].adjacencyList.add(nodes[id1]);
        return 1;
    }

    // returns true if adjacent, false if not
    public boolean areAdjacent(LatLng ele1, LatLng ele2)
    {
        int id1 = getID(ele1), id2 = getID(ele2);
        if (id1 < 0 || id2 < 0)
            return false;
        if (nodes[id1].adjacencyList.contains(nodes[id2]))
                return true;
        return false;
    }

    // returns an array of all the elements in the graph
    public LatLng[] vertices()
    {
        LatLng[] returnMe = new LatLng[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++)
        {
            returnMe[i] = new LatLng(nodes[i].getElement().latitude, nodes[i].getElement().longitude);
        }
        return returnMe;
    }
}
