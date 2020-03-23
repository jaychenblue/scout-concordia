package com.example.scoutconcordia.DataStructures;

import com.google.android.gms.maps.model.LatLng;

public class N_aryTree
{
    Node head;
    
    private class Node
    {
        Node parent;
        LinkedList<Node> children;
        LatLng element;
        
        Node()
        {
            parent = null;
            element = null;
            children = null;
        }
        
        Node(Node parent, LatLng element)
        {
            this.parent = parent;
            this.element = element;
            children = new LinkedList<Node>(head);
        }
        
        boolean addToChildren(LatLng pointToAdd)
        {
            if (children == null)
                return false;
            Node addMe = new Node(this, pointToAdd);
            children.add(addMe);
            return true;
        }
    }
}
