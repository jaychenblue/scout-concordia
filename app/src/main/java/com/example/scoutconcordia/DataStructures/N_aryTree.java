package com.example.scoutconcordia.DataStructures;

import com.google.android.gms.maps.model.LatLng;

// under the assumption no 2 points will be the same
public class N_aryTree
{
    TreeNode head;
    
    public class TreeNode
    {
        TreeNode parent;
        LinkedList<TreeNode> children;
        LatLng element;
        
        public TreeNode getParent()
        {
            return parent;
        }
    
        public LinkedList<TreeNode> getChildren()
        {
            return children;
        }
    
        public LatLng getElement()
        {
            return element;
        }
    
        public void setParent(TreeNode parent)
        {
            this.parent = parent;
        }
    
        public void setChildren(LinkedList<TreeNode> children)
        {
            this.children = children;
        }
    
        public void setElement(LatLng element)
        {
            this.element = element;
        }
    
        public TreeNode(TreeNode parent, LatLng element)
        {
            this.parent = parent;
            this.element = element;
            children = new LinkedList<TreeNode>(this);
        }
    
        public boolean addToChildren(LatLng pointToAdd)
        {
            if (children == null)
                return false;
            TreeNode addMe = new TreeNode(this, pointToAdd);
            children.add(addMe);
            return true;
        }
        
        public boolean equals(TreeNode n2)
        {
            return (element.equals(n2.element));
        }
    }
    
    public N_aryTree()
    {
        head = new TreeNode(null, null);
    }
    
    public TreeNode getHead()
    {
        return head;
    }
    
    public TreeNode findSpecifiedNode(TreeNode goFromHere, LatLng findThisPoint)
    {
        if (goFromHere == null || findThisPoint == null)
            return null;
        if (goFromHere.element.equals(findThisPoint))
            return goFromHere;
        if (goFromHere.children != null)
        {
            LinkedList.Node current = goFromHere.children.getHead();
            for (int i = 0; i < goFromHere.children.size(); i++)
            {
                if (findSpecifiedNode((TreeNode)current.getEle(), findThisPoint) != null)
                    if (findSpecifiedNode((TreeNode)current.getEle(), findThisPoint).element != null)
                        if (findSpecifiedNode((TreeNode)current.getEle(), findThisPoint).element.equals(findThisPoint))
                            return findSpecifiedNode((TreeNode)current.getEle(), findThisPoint);
                current = current.getNext();
            }
        }
        return null;
    }
}
