package com.example.scoutconcordia.DataStructures;

import android.location.Location;
import android.renderscript.ScriptGroup;
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

        public Node(LatLng element, int id)
        {
            this.element = element;
            this.id = id;
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
        private LatLng getElement() { return element; }
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
    
    // returns an array of points corresponding to the shortest path from --> to
    public Object[] breathFirstSearch(LatLng from, LatLng to)
    {
        // resets the results for each search
        if (breathFirstSearchResults.getHead() != null)
            breathFirstSearchResults = new N_aryTree();
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != null)
                nodes[i].setTraversed(false);
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
    public static Graph addNodesToGraph(InputStream readFromMe)
    {
        int currentPos = 0;
        Scanner reader = null;
        String currentLine = null;
        String floorName = null;
        Graph returnMe = null;
        LatLng currentCoordinate = null;
        String roomNumber = null;
        String[] lineString = null;
        int posOfHalfway = 0;
        double x_coordinate = 0, y_coordinate = 0;

        LinkedList<LatLng> coordinatesToInsert = new LinkedList<LatLng>(new LatLng(0,0));
        try
        {
            reader = new Scanner(readFromMe);
            while(reader.hasNext())
            {
                currentLine = reader.nextLine();
                Log.println(Log.WARN, "printing", currentLine);
                currentPos = currentLine.indexOf("Name of Image: ");
                if (currentPos < 0)
                    throw new InputMismatchException("Expected a name but didn't find one");
                floorName = (currentLine.substring(currentPos + 13));

                reader.nextLine();  // reads the {
                reader.nextLine();

                currentLine = reader.nextLine(); // starts reading the classrooms

                while (currentLine.charAt(currentLine.length() - 1) != '}')
                {
                    coordinatesToInsert.add(readClassCoordinate(currentLine));
                    currentLine = reader.nextLine();
                }
                // this is for the last classroom coordinate
                coordinatesToInsert.add(readClassCoordinate(currentLine));
                currentLine = reader.nextLine();

                // read the file searching for hallway
                //currentPos = currentLine.indexOf("hallway:");
                currentLine = reader.nextLine();
                while (currentLine.charAt(currentLine.length() - 1) != '}')
                {
                    coordinatesToInsert.add(readHallCoordinate(currentLine));
                    currentLine = reader.nextLine();
                }
                coordinatesToInsert.add( readHallCoordinate(currentLine));
                currentLine = reader.nextLine();
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        finally
        {
            LinkedList.Node current = null;
            returnMe = new Graph(coordinatesToInsert.size());
            for (int i = 0; i < coordinatesToInsert.size(); i++)
            {
                if (i == 0)
                    current = coordinatesToInsert.getHead();
                if (current != null)
                    returnMe.insertVertex((LatLng)current.getEle());
                current = current.getNext();
            }
        }
        return returnMe;
    }

    public static LatLng readClassCoordinate(String currentLine)
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

    public static LatLng readHallCoordinate(String currentLine)
    {
        int currentPos = currentLine.indexOf("{");
        int posOfHalfway = currentLine.indexOf(",");
        double x_coordinate = Double.parseDouble(currentLine.substring(currentPos+1,posOfHalfway));
        double y_coordinate = Double.parseDouble(currentLine.substring(posOfHalfway+2, currentLine.length()-2));
        LatLng currentCoordinate = new LatLng(x_coordinate, y_coordinate);
        return currentCoordinate;
    }

    public void addAjacentNodes()
    {
        // we want to insert an edge between all of the nodes that are adjacent. i.e the nodes that are closest to eachother
        // if a node is closest to 2 nodes then it will have 2 nodes in its adjacency list.

        // we want each node to have 2 closest nodes. They will be stored in an array [closest_node, second_closest_node]

        float smallestDistance = 0;
        float secondSmallestDistance = 0;
        float distance = 0;
        Node[] closestNodes = new Node[2]; // create an array of length 1
        int nmbClosestNodes = 2;

        for (Node node: this.nodes)
        {
            smallestDistance = 1000000;
            secondSmallestDistance = 1000000;
            distance = 1000000;
            LatLng myCoordinate = node.element;

            for (Node otherNode: this.nodes)
            {
                if (otherNode != node)  // we only want to get the distance if the nodes are not equal
                {
                    distance = calculateNodeDistance(myCoordinate, otherNode.element);

                    if (distance < smallestDistance)
                    {
                        closestNodes[1] = closestNodes[0];
                        closestNodes[0] = otherNode;
                        secondSmallestDistance = smallestDistance;
                        smallestDistance = distance;
                    } else if (distance > smallestDistance && distance < secondSmallestDistance)
                    {
                        closestNodes[1] = otherNode;
                        secondSmallestDistance = distance;
                    }
                }
            }
            for (Node closestNode : closestNodes)
            {
                this.insertEdge(node.element, closestNode.element);
            }
        }
    }

    public static Node[] addNode(Node arr[], Node x)
    {
        Node newarr[] = new Node[arr.length + 1];

        for (int i = 0; i < arr.length; i++)
        {
            newarr[i] = arr[i];
        }
        newarr[arr.length] = x;
        return newarr;
    }

    // calculates the distance between 2 nodes in meters
    public float calculateNodeDistance(LatLng node1, LatLng node2)
    {
        float[] distance = new float[1];
        distanceBetween(node1.latitude, node1.longitude, node2.latitude, node2.longitude, distance);

        return distance[0];
    }
}
