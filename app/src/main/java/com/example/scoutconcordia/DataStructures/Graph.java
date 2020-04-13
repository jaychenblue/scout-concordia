package com.example.scoutconcordia.DataStructures;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.util.InputMismatchException;
import static android.location.Location.distanceBetween;

/** Class representing a Graph.*/
public class Graph
{
    private int numberOfNodes;
    private Node[] nodes;
    private N_aryTree breathFirstSearchResults;
    /** Represents the name of the floor that the Graph is being used for
     */
    public String id;

    /** Inner class representing a Node of the Graph. */
    public class Node
    {
        private int id;
        private LatLng element;
        private LinkedList<Node> adjacencyList;
        private boolean traversed;
        private int type;  // 0 is a class node, 1 is a hall node.
        private String room; //this is the corresponding room name for the node

        /**Creates a Node with a specified location
         * @param element The latitude and longitude coordinate of the node
         * @param id Index of the node in the Node array from the Graph.
         * @param type Integer representing the type of node. 0 = class node. 1 is a hallway node.
         * @param room The corresponding room name for the node.
         */
        public Node(LatLng element, int id, int type, String room)
        {
            this.element = element;
            this.id = id;
            this.type = type;
            this.room = room;
            adjacencyList = new LinkedList<Node>(this);
        }

        @Override public boolean equals(Object n1)
        {
            if (n1 == null)
                return false;
            else if (n1.getClass() == Node.class)
                return this.element.equals(((Node)n1).element);
            else if (n1.getClass() == LatLng.class)
                return this.element.equals(n1);
            return false;
        }

        private int getId() { return id; }
        private void setId(int id) { this.id = id; }
        private void setElement(LatLng element) { this.element = element; }
        private boolean isTraversed() { return traversed; }
        private void setTraversed(boolean traversed) { this.traversed = traversed; }
        public int getType() { return type; }
        public LatLng getElement() { return element; }
        public String getRoom() {return room;}
    }

    /** Creates a Graph with a specified size.
     * @param size The size of the graph to be created.
     */
    public Graph(int size)
    {
        numberOfNodes = 0;
        nodes = new Node[size];
        for (int i = 0; i < size; i++)
            nodes[i] = null;
        breathFirstSearchResults = new N_aryTree();
    }

    /** Resets the graph to start from fresh.
     */
    public void reset()
    {
        nodes = null;
        numberOfNodes = 0;
        breathFirstSearchResults = null;
    }

    /**
     * Check the placement in the array of the given point / node
     * @param ele latitude and longitude coordinate of the node to check
     * @return Returns an integer representing the index of the node in the array of nodes.
     */
    private int getID(LatLng ele)
    {
        int id = -1;
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != null)
                if (nodes[i].getElement().equals(ele))
                    id = i;
        }
        return id;
    }

    /** Inserts a new node into the Graph
     * @param element The latitude and longitude coordinate of the node
     * @param type Integer representing the type of node. 0 = class node. 1 is a hallway node.
     * @param room The corresponding room name for the node.
     * @return A Boolean signaling if the operation was successful.
     */
    public boolean insertVertex(LatLng element, int type, String room)
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
        nodes[placeMe] = new Node(element, placeMe, type, room);
        numberOfNodes++;
        return true;
    }

    /** Inserts an edge between 2 vertices (nodes) in the graph.
     * @param ele1 The latitude and longitude coordinate of the 1st node.
     * @param ele2 The latitude and longitude coordinate of the 2nd node.
     * @return An integer signaling if the operation was successful. -1 means 1 or both of the
     * nodes are not in the graph. 0 means that these 2 vertices are already adjacent. 1 means that
     * the operation was successful and the vertices are now adjacent.
     */
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

    /** Checks to see if 2 nodes are adjacent to each other.
     * @param ele1 The latitude and longitude coordinate of the 1st node.
     * @param ele2 The latitude and longitude coordinate of the 2nd node.
     * @return A boolean signaling if the 2 nodes are adjacent to each other.
     */
    public boolean areAdjacent(LatLng ele1, LatLng ele2)
    {
        int id1 = getID(ele1), id2 = getID(ele2);
        if (id1 < 0 || id2 < 0)
            return false;
        return nodes[id1].adjacencyList.contains(nodes[id2]);
    }

    /** Replaces a coordinate in the graph with a new one
     * @param oldCoordinate The latitude and longitude coordinate of the old node.
     * @param newCoordinate The latitude and longitude coordinate of the new node.
     * @return A boolean signaling if the operation was successful.
     */
    public boolean replace(LatLng oldCoordinate, LatLng newCoordinate)
    {
        int id = getID(oldCoordinate);
        if (id < 0)
            return false;
        nodes[id].element = newCoordinate;
        return true;
    }

    // returns -1 if the elements can't be found in the nodes of the graph, 0 if one of the elements couldn't be removed / wasn't in the adjacency list and 1 if it was successful

    /** Removes an edge between 2 vertices (nodes)
     * @param ele1 The latitude and longitude coordinate of the 1st node.
     * @param ele2 The latitude and longitude coordinate of the 2nd node.
     * @return An integer signaling if the operation was successful. -1 means 1 or both of the
     * nodes are not in the graph. 0 if one of the elements couldn't be removed / wasn't in the
     * adjacency list. 1 if the operation was successful and the edge has been removed.
     */
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

    /** Removes a vertex from the Graph.
     * @param removeMe The latitude and longitude coordinate of the node to be removed from the Graph.
     * @return An integer signaling if the operation was successful. -1 if the node is not in the graph.
     *  0 if the element to be removed wasn't in one of it's own adjacent entries. 1 if successful.
     */
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

    /** Returns any vertices next to a specified coordinate.
     * @param coordinate The latitude and longitude coordinate of the node to be used.
     * @return A LatLng array containing all coordinates that are incident to the original node.
     */
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

    /**
     * @return Returns an array of all the elements in the graph.
     */
    public LatLng[] vertices()
    {
        int placeMe = 0;
        LatLng[] returnMe = new LatLng[numberOfNodes];
        for (Node node : nodes)
        {
            if (node != null)
            {
                returnMe[placeMe] = new LatLng(node.getElement().latitude, node.getElement().longitude);
                placeMe++;
            }
        }
        return returnMe;
    }

    /**
     * @return Returns an array of all the nodes in the graph.
     */
    public Node[] nodes()
    {
        return nodes;
    }

    /** Searches the array of nodes for a specific class node using the name of the class.
     * @param className The name of the classroom to find. ex: "H-110".
     * @return Returns the LatLng coordinate of the classroom. If there is no match, returns null.
     */
    public LatLng searchByClassName(String className)
    {
        String currentRoom;
        for (Node node : nodes)
        {
            currentRoom = node.getRoom();
            if (currentRoom.equals(className))  // we have found the room
            {
                return node.getElement();
            }
        }
        return null; //if we don't get a match we return null
    }

    /** Breath first search algorithm implemented to search for a path between 2 nodes
     * @param from Latitude and longitude coordinate of the start node.
     * @param to  Latitude and longitude coordinate of the destination node.
     * @return Returns an array of points corresponding to the shortest path from start to destination.
     */
    public Object[] breathFirstSearch(LatLng from, LatLng to)
    {
        // resets the results for each search
        if (breathFirstSearchResults.getHead() != null)
            breathFirstSearchResults = new N_aryTree();
        for (Node node: nodes) {
            if (node != null)
            {
                if (node.getType() > 0) // not a class node
                    node.setTraversed(false);
                else
                    node.setTraversed(true);
            }
        }
        // Both points exist
        int id1 = getID(from);
        int id2 = getID(to);
        if (id1 < 0 || id2 < 0)
            return null;
        // Start by adding the from point to tree
        LinkedList<LatLng> currentPointsToCycle = new LinkedList<>(new LatLng(0,0));
        LinkedList<LatLng> newCurrentPointsToCycle = new LinkedList<>(new LatLng(0,0));
        N_aryTree.TreeNode currentTreeNode = breathFirstSearchResults.getHead();
        currentTreeNode.setElement(from);
        LinkedList.Node currentPoint = null;
        // Starting Point Case
        if (id1 == id2)
        {
            return breathFirstSearchResults.getPath(from, to);
        }
        // First Breath
        currentPointsToCycle.add(nodes[id1].getElement());
        nodes[id1].setTraversed(true);
        nodes[id2].setTraversed(false);
        while (currentPointsToCycle.size() != 0)
        {
            currentPoint = currentPointsToCycle.getHead();
            currentTreeNode = breathFirstSearchResults.findSpecifiedNode(breathFirstSearchResults.getHead(), (LatLng) currentPoint.getEle());
            for (int i = 0; i < currentPointsToCycle.size(); i++)
            {
                id1 = getID((LatLng) currentPoint.getEle());
                if (id1 >= 0)
                {
                    currentTreeNode = breathFirstSearchResults.findSpecifiedNode(breathFirstSearchResults.getHead(), (LatLng) currentPoint.getEle());
                    LinkedList.Node currentAdjacentNode = nodes[id1].adjacencyList.getHead(); // LinkList of Graph Nodes
                    while (((Node) currentAdjacentNode.getEle()).isTraversed() && currentAdjacentNode.getNext() != null)
                    {
                        currentAdjacentNode = currentAdjacentNode.getNext();
                    }
                    for (int j = 0; j < nodes[id1].adjacencyList.size(); j++)
                    {
                        if (currentAdjacentNode != null)
                        {
                            if (((Node) currentAdjacentNode.getEle()).getElement().equals(to)) // we found the point in this breath
                            {
                                currentTreeNode.addToChildren(((Node) currentAdjacentNode.getEle()).getElement());
                                return breathFirstSearchResults.getPath(from, to);
                            }
                            if (!(((Node) currentAdjacentNode.getEle()).isTraversed()))        // Adding New element to the breath
                            {
                                currentTreeNode.addToChildren(((Node) currentAdjacentNode.getEle()).getElement());
                                ((Node) currentAdjacentNode.getEle()).setTraversed(true);
                                newCurrentPointsToCycle.add(((Node) currentAdjacentNode.getEle()).getElement());
                            }
                            while (((Node) currentAdjacentNode.getEle()).isTraversed() && currentAdjacentNode.getNext() != null)
                            {
                                currentAdjacentNode = currentAdjacentNode.getNext();
                            }
                            if (((Node) currentAdjacentNode.getEle()).isTraversed())
                                currentAdjacentNode = currentAdjacentNode.getNext();
                        }
                    }
                    if (currentPoint != null)
                    {
                        currentPoint = currentPoint.getNext();
                    }
                }
            }
            currentPointsToCycle = newCurrentPointsToCycle;
            newCurrentPointsToCycle = new LinkedList<>(new LatLng(0, 0));
            LinkedList.Node treeNodeChild = currentTreeNode.getChildren().getHead();
            for (int i = 0; i < currentTreeNode.getChildren().size(); i++)
            {
                treeNodeChild = treeNodeChild.getNext();
            }
        }
        return null;
    }

    /** Reads from a node file to add the nodes to the graph.
     * @param contents A String array representing the contents of the file.
     * @return Returns a graph using all of the coordinates that were listed in the file.
     */
    public static Graph addNodesToGraph(String[] contents)
    {
        int currentPos;
        String currentLine;
        String floorName = null;
        Graph returnMe;
        int nmbClassNodes = 0;
        int nmbHallNodes = 0;

        LinkedList<LatLng> coordinatesToInsert = new LinkedList<>(new LatLng(0,0));
        LinkedList<String> namesToInsert = new LinkedList<>("");

        try
        {
            for (int i = 0; i < contents.length; i++)
            {
                currentLine = contents[i];
                Log.println(Log.WARN, "printing", currentLine);
                currentPos = currentLine.indexOf("Name of Image: ");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected a name but didn't find one");
                floorName = (currentLine.substring(currentPos + 15));

                i++; i++; i++;
                currentLine = contents[i];

                while (currentLine.charAt(currentLine.length() - 1) != '}')
                {
                    coordinatesToInsert.add(readClassCoordinate(currentLine));
                    namesToInsert.add(readClassName(currentLine));
                    nmbClassNodes += 1;
                    i++;
                    currentLine = contents[i];
                }
                // this is for the last classroom coordinate
                coordinatesToInsert.add(readClassCoordinate(currentLine));
                namesToInsert.add(readClassName(currentLine));
                nmbClassNodes += 1;

                // read the file searching for hallway
                i++; i++;
                currentLine = contents[i];
                while (currentLine.charAt(currentLine.length() - 1) != '}')
                {
                    coordinatesToInsert.add(readHallCoordinate(currentLine));
                    namesToInsert.add("HALLWAY");
                    nmbHallNodes += 1;
                    i ++;
                    currentLine = contents[i];
                }
                coordinatesToInsert.add( readHallCoordinate(currentLine));
                namesToInsert.add("HALLWAY");
                nmbHallNodes += 1;

                //read the file searching for the escalator node
                i++; i++;
                currentLine = contents[i];
                coordinatesToInsert.add(readHallCoordinate(currentLine));
                namesToInsert.add("ESCALATOR");

                // read the file searching for the elevator node
                i++; i++;
                currentLine = contents[i];
                coordinatesToInsert.add(readHallCoordinate(currentLine));
                namesToInsert.add("ELEVATOR");

                i++;
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        finally
        {
            LinkedList.Node current = null;
            LinkedList.Node currentClass = null;
            returnMe = new Graph(coordinatesToInsert.size());
            returnMe.id = floorName;
            for (int i = 0; i < coordinatesToInsert.size(); i++)
            {
                if (i == 0)
                {
                    current = coordinatesToInsert.getHead();
                    currentClass = namesToInsert.getHead();
                }
                if (current != null)
                {
                    if (i < nmbClassNodes) // i < nmbClassNodes the condition causing the error
                    {
                        returnMe.insertVertex((LatLng) current.getEle(), 0, (String) currentClass.getEle()); //insert a class node
                    }
                    else if (i > nmbClassNodes && i < nmbClassNodes + nmbHallNodes)
                    {
                        returnMe.insertVertex((LatLng) current.getEle(), 1, (String) currentClass.getEle()); //insert a hall node
                    }
                    else
                    {
                        returnMe.insertVertex((LatLng) current.getEle(), 2, (String) currentClass.getEle()); //insert a hall node
                    }
                    current = current.getNext();
                }
                if (currentClass != null)
                    currentClass = currentClass.getNext();
            }
        }
        return returnMe;
    }

    /** Helper method for reading a classroom coordinate from the input file.
     * @param currentLine String representing the current line of text that is being read.
     * @return Returns a latitude and longitude coordinate representing the location of a classroom node.
     */
    private static LatLng readClassCoordinate(String currentLine)
    {
        String[] lineString = currentLine.split(":");
        int currentPos = lineString[1].indexOf("{");
        int posOfHalfway = lineString[1].indexOf(",");
        double x_coordinate = Double.parseDouble(lineString[1].substring(currentPos+1,posOfHalfway));
        double y_coordinate = Double.parseDouble(lineString[1].substring(posOfHalfway+2, lineString[1].length()-2));
        return new LatLng(x_coordinate, y_coordinate); //return currentCoordinate
    }

    /** Helper method for reading a classroom name from the input file.
     * @param currentLine String representing the current line of text that is being read.
     * @return Returns a String representing the name of the classroom.
     */
    private static String readClassName(String currentLine)
    {
        String[] lineString = currentLine.split(":");
        return lineString[0].trim(); // return floor name
    }

    /**
     * Helper method for reading a hallway coordinate from the input file.
     * @param currentLine String representing the current line of text that is being read.
     * @return Returns a latitude and longitude coordinate representing the location of the hallway node.
     */
    private static LatLng readHallCoordinate(String currentLine)
    {
        int currentPos = currentLine.indexOf("{");
        int posOfHalfway = currentLine.indexOf(",");
        double x_coordinate = Double.parseDouble(currentLine.substring(currentPos+1,posOfHalfway));
        double y_coordinate = Double.parseDouble(currentLine.substring(posOfHalfway+2, currentLine.length()-2));
        return new LatLng(x_coordinate, y_coordinate);
    }

    /** Method for adding edges between all nodes that are within a certain distance from eachother.
     */
    public void addAdjacentNodes()
    {
        // we want to insert an edge between all of the nodes that are adjacent. i.e the nodes that are closest to eachother
        float distance;
        for (int i = 0; i < nodes.length; i++)
        {
            Node currentNode = nodes[i];
            for (int j = 0; j < nodes.length; j++)
            {
                distance = calculateNodeDistance(currentNode.getElement(), nodes[j].getElement());
                if (j != i && distance < 8.5)
                    insertEdge(currentNode.getElement(), nodes[j].getElement());
            }
        }
    }

    /** Helper method for calculating the distance between 2 nodes.
     * @param node1 latitude and longitude coordinate of the 1st node.
     * @param node2 latitude and longitude coordinate of the 2nd node.
     * @return Returns a float representing the distance between the 2 nodes.
     */
    private float calculateNodeDistance(LatLng node1, LatLng node2)
    {
        float[] distance = new float[1];
        distanceBetween(node1.latitude, node1.longitude, node2.latitude, node2.longitude, distance);

        return distance[0];
    }
}
