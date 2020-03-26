package com.example.scoutconcordia.DataStructures;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import static android.location.Location.distanceBetween;


// Assumption is no two points are equivalent
public class Graph
{
    private int numberOfNodes;
    private Node[] nodes;
    private N_aryTree breathFirstSearchResults;

    public class Node
    {
        private int id;
        private LatLng element;
        private LinkedList<Node> adjacencyList;
        private boolean traversed;
        private int type;  // 0 is a class node, 1 is a hall node.
        private String room; //this is the corresponding room name for the node

        public Node(LatLng element, int id, int type, String room)
        {
            this.element = element;
            this.id = id;
            this.type = type;
            this.room = room;
            adjacencyList = new LinkedList<Node>(this);
        }

        private boolean equals(Node n1)
        {
            return this.element.equals(n1.element);
        }

        private boolean equals(LatLng ele)
        {
            return this.element.equals(ele);
        }

        private int getId() { return id; }
        private void setId(int id) { this.id = id; }
        public int getType() { return type; }
        public LatLng getElement() { return element; }
        public String getRoom() {return room;}
        private void setElement(LatLng element) { this.element = element; }
        private boolean isTraversed() { return traversed; }
        private void setTraversed(boolean traversed) { this.traversed = traversed; }
    }

    public Graph(int size)
    {
        numberOfNodes = 0;
        nodes = new Node[size];
        for (int i = 0; i < size; i++)
            nodes[i] = null;
        breathFirstSearchResults = new N_aryTree();
    }

    // resets the graph to start from fresh
    public void reset()
    {
        nodes = null;
        numberOfNodes = 0;
        breathFirstSearchResults = null;
    }

    // check the placement in the array of the given point / node
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
    // type indicates whether the node is a class room node or a hall node
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

    // replaces a coordinate in the graph with a new one
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

    // returns any vertices next to the coordinate
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

    // returns an array of all the nodes in the graph
    public Node[] nodes()
    {
        return nodes;
    }

    public LatLng searchByClassName(String className)
    {
        String currentRoom;
        for (int i = 0; i < nodes.length; i++)
        {
            currentRoom = nodes[i].getRoom();
            if (currentRoom.equals(className))  //we have found the room
            {
                return nodes[i].getElement();
            }
        }
        return null; //if we don't get a match we return null
    }

    // returns an array of points corresponding to the shortest path from --> to
    public Object[] breathFirstSearch(LatLng from, LatLng to)
    {
        // resets the results for each search
        if (breathFirstSearchResults.getHead() != null)
            breathFirstSearchResults = new N_aryTree();
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != null)
            {
                if (nodes[i].getType() > 0) // not a class node
                    nodes[i].setTraversed(false);
                else
                    nodes[i].setTraversed(true);
            }
        }
        // Both points exist
        int id1 = getID(from);
        int id2 = getID(to);
        if (id1 < 0 || id2 < 0)
            return null;
        // Start by adding the from point to tree
        LinkedList<LatLng> currentPointsToCycle = new LinkedList<LatLng>(new LatLng(0,0));
        LinkedList<LatLng> newCurrentPointsToCycle = new LinkedList<LatLng>(new LatLng(0,0));
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
            newCurrentPointsToCycle = new LinkedList<LatLng>(new LatLng(0, 0));
            LinkedList.Node treeNodeChild = currentTreeNode.getChildren().getHead();
            for (int i = 0; i < currentTreeNode.getChildren().size(); i++)
            {
                treeNodeChild = treeNodeChild.getNext();
            }
        }
        return null;
    }

    // reads from a node file to add nodes to a graph
    public static Graph addNodesToGraph(String[] contents)
    {
        int currentPos = 0;
        String currentLine = null;
        String floorName = null;
        Graph returnMe = null;
        LatLng currentCoordinate = null;
        String roomNumber = null;
        String[] lineString = null;
        int posOfHalfway = 0;
        double x_coordinate = 0, y_coordinate = 0;
        int nodeType = 0;  //0 is for a classroom node, 1 is for a hall node
        int nmbClassNodes = 0;
        int nmbHallNodes = 0;

        LinkedList<LatLng> coordinatesToInsert = new LinkedList<LatLng>(new LatLng(0,0));
        LinkedList<String> namesToInsert = new LinkedList<String>("");

        try
        {
            for (int i = 0; i < contents.length; i++)
            {
                currentLine = contents[i];
                Log.println(Log.WARN, "printing", currentLine);
                currentPos = currentLine.indexOf("Name of Image: ");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected a name but didn't find one");
                floorName = (currentLine.substring(currentPos + 13));

                i++; i++;
                currentLine = contents[i];
                currentPos = currentLine.indexOf("classrooms:");
                i++;
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
                currentLine = contents[i];
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
            for (int i = 0; i < coordinatesToInsert.size(); i++)
            {
                if (i == 0)
                {
                    current = coordinatesToInsert.getHead();
                    currentClass = namesToInsert.getHead();
                }
                if (current != null)
                    if (i < nmbClassNodes) // i < nmbClassNodes the condition causing the error
                    {
                        returnMe.insertVertex((LatLng)current.getEle(), 0, (String)currentClass.getEle()); //insert a class node
                    } else if (i > nmbClassNodes && i < nmbClassNodes + nmbHallNodes) {
                        returnMe.insertVertex((LatLng)current.getEle(), 1, (String)currentClass.getEle()); //insert a hall node
                    } else {
                        returnMe.insertVertex((LatLng)current.getEle(), 2, (String)currentClass.getEle()); //insert a hall node
                    }
                currentClass = currentClass.getNext();
                current = current.getNext();
            }
        }
        return returnMe;
    }

    private static LatLng readClassCoordinate(String currentLine)
    {
        String[] lineString = currentLine.split(":");
        String floorName = lineString[0].trim(); // ex: H-801

        int currentPos = lineString[1].indexOf("{");
        int posOfHalfway = lineString[1].indexOf(",");
        double x_coordinate = Double.parseDouble(lineString[1].substring(currentPos+1,posOfHalfway));
        double y_coordinate = Double.parseDouble(lineString[1].substring(posOfHalfway+2, lineString[1].length()-2));
        LatLng currentCoordinate = new LatLng(x_coordinate, y_coordinate);
        return currentCoordinate;
    }

    private static String readClassName(String currentLine)
    {
        String[] lineString = currentLine.split(":");
        String floorName = lineString[0].trim(); // ex: H-801

        return floorName;
    }

    private static LatLng readHallCoordinate(String currentLine)
    {
        int currentPos = currentLine.indexOf("{");
        int posOfHalfway = currentLine.indexOf(",");
        double x_coordinate = Double.parseDouble(currentLine.substring(currentPos+1,posOfHalfway));
        double y_coordinate = Double.parseDouble(currentLine.substring(posOfHalfway+2, currentLine.length()-2));
        LatLng currentCoordinate = new LatLng(x_coordinate, y_coordinate);
        return currentCoordinate;
    }

    public void addAdjacentNodes()
    {
        // we want to insert an edge between all of the nodes that are adjacent. i.e the nodes that are closest to eachother
        float distance = 0;
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

    // calculates the distance between 2 nodes in meters
    private float calculateNodeDistance(LatLng node1, LatLng node2)
    {
        float[] distance = new float[1];
        distanceBetween(node1.latitude, node1.longitude, node2.latitude, node2.longitude, distance);

        return distance[0];
    }
}
