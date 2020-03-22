package DataStructures;

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
        for (int i = 0; i < size; i++)
            nodes[i] = null;
    }

    public void reset()
    {
        nodes = null;
        numberOfNodes = 0;
    }

    private int getID(LatLng ele)
    {
        int id = -1;
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != null)
                if (nodes[i].equals(ele))
                    id = i;
        }
        return id;
    }

    // returns true if element can be inserted false otherwise
    public boolean insertVertex(LatLng element)
    {
        int placeMe = -1;
        for (int i = 0; i < nodes.length; i++)
        {
            if (placeMe == -1)
                if (nodes[i] == null)
                    placeMe = i;
        }
        if (placeMe == -1)
            return false;
        nodes[placeMe] = new Node(element, placeMe);
        numberOfNodes++;
        return true;
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
    
    public boolean replace(LatLng oldCoordinate, LatLng newCoordinate)
    {
        int id = getID(oldCoordinate);
        if (id < 0)
            return false;
        nodes[id].element = newCoordinate;
        return true;
    }
    
    // returns -1 if the elements can't be found in the nodes of the graph, 0 if one of the elements couldn't be removed / wasn't in the adjacency list and 1 if it was successful
    public int removeEdge(LatLng ele1, LatLng ele2)
    {
        int id1 = getID(ele1), id2 = getID(ele2);
        if (id1 < 0 || id2 < 0)
            return -1;
        // get the indexes of the obj's in the linked list
        int indexOfEle1inListofEle2 = nodes[id2].adjacencyList.indexOf(nodes[id1]);
        int indexOfEle2inListofEle1 = nodes[id1].adjacencyList.indexOf(nodes[id2]);
        if (indexOfEle1inListofEle2 < 0 || indexOfEle2inListofEle1 < 0)
            return 0;
        nodes[id1].adjacencyList.removeOBJ(nodes[id2]);
        nodes[id2].adjacencyList.removeOBJ(nodes[id1]);
        return 1;
    }
    
    // returns -1 if element isn't in the graph, 0 if the element to be removed wasn't in one of it's own adjacent entries / system error and 1 if successful
    public int removeVertex(LatLng removeMe)
    {
        int id = getID(removeMe);
        if (id < 0)
            return -1;
        Object[] adjacentVerticies = nodes[id].adjacencyList.toArray();
        for(int i = 0; i < adjacentVerticies.length; i++)
        {
            if (removeEdge(nodes[id].element, ((Node)adjacentVerticies[i]).element) != 1)
                return 0;
        }
        nodes[id] = null;
        numberOfNodes--;
        return 1;
    }
    
    public LatLng[] incidentVerticies(LatLng coordinate)
    {
        int id = getID(coordinate);
        Object[] gotten = nodes[id].adjacencyList.toArray();
        LatLng[] returnMe = new LatLng[gotten.length];
        for (int i = 0; i < gotten.length; i++)
        {
            returnMe[i] = ((Node)gotten[i]).element;
        }
        return returnMe;
    }

    // returns an array of all the elements in the graph
    public LatLng[] vertices()
    {
        int getMe = -1, placeMe = 0;
        LatLng[] returnMe = new LatLng[numberOfNodes];
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != null)
            {
                returnMe[placeMe] = new LatLng(nodes[i].getElement().latitude, nodes[i].getElement().longitude);
                placeMe++;
            }
        }
        return returnMe;
    }
}
