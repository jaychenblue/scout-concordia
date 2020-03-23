package com.example.scoutconcordia.DataStructures;

import com.google.android.gms.maps.model.LatLng;

public class N_aryTree
{
    TreeNode head;
    
    public class TreeNode
    {
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
    
        TreeNode parent;
    
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
    
        LinkedList<TreeNode> children;
        LatLng element;
    
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
    }
    
    
}
