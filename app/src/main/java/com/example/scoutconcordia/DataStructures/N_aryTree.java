package com.example.scoutconcordia.DataStructures;

import com.google.android.gms.maps.model.LatLng;

public class N_aryTree
{
    Node head;
    
    public class Node
    {
        public Node getParent()
        {
            return parent;
        }
    
        public LinkedList<Node> getChildren()
        {
            return children;
        }
    
        public LatLng getElement()
        {
            return element;
        }
    
        Node parent;
    
        public void setParent(Node parent)
        {
            this.parent = parent;
        }
    
        public void setChildren(LinkedList<Node> children)
        {
            this.children = children;
        }
    
        public void setElement(LatLng element)
        {
            this.element = element;
        }
    
        LinkedList<Node> children;
        LatLng element;
    
        public Node(Node parent, LatLng element)
        {
            this.parent = parent;
            this.element = element;
            children = new LinkedList<Node>(this);
        }
    
        public boolean addToChildren(LatLng pointToAdd)
        {
            if (children == null)
                return false;
            Node addMe = new Node(this, pointToAdd);
            children.add(addMe);
            return true;
        }
    }
    
    
}
