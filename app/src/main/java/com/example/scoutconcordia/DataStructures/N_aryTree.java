package com.example.scoutconcordia.DataStructures;

import com.google.android.gms.maps.model.LatLng;

/** Class representing an N_ary tree under the assumption that no 2 points will be the same */
public class N_aryTree
{
    private TreeNode head;

    /** Inner class TreeNode that represents Nodes of the N_ary tree.
     */
    public class TreeNode
    {
        private TreeNode parent;
        private LinkedList<TreeNode> children;
        private LatLng element;

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

        /** Constructor for a TreeNode
         * @param parent The parent of the new tree node
         * @param element the LatLng value of the new tree node
         */
        public TreeNode(TreeNode parent, LatLng element)
        {
            this.parent = parent;
            this.element = element;
            children = new LinkedList<>(this);
        }

        /** Adds a child node to a TreeNode
         * @param pointToAdd the LatLng value of the new child node
         * @return Returns true if the child node was added successfully.
         */
        public boolean addToChildren(LatLng pointToAdd)
        {
            if (children == null)
                return false;
            TreeNode addMe = new TreeNode(this, pointToAdd);
            children.add(addMe);
            return true;
        }

        /** Overrides the equals method.
         * @param n2 An object to compare to
         * @return Returns true if both TreeNode elements are the same.
         */
        @Override public boolean equals(Object n2)
        {
            if (n2 == null)
                return false;
            else if (n2.getClass() != this.getClass())
                return false;
            return (element.equals(((TreeNode)n2).element));
        }
    }

    /** Default constructor for a N_ary Tree */
    public N_aryTree()
    {
        head = new TreeNode(null, null);
    }

    public TreeNode getHead()
    {
        return head;
    }

    public void setHead(TreeNode head) {
        this.head = head;
    }

    /** Finds a node within the N_ary Tree
     * @param goFromHere Starting node
     * @param findThisPoint Destination node
     * @return Returns a node along the path. This is a recursive method.
     */
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
                TreeNode gotten = findSpecifiedNode((TreeNode)current.getEle(), findThisPoint);
                if (gotten != null)
                    if (gotten.element != null)
                        if (gotten.element.equals(findThisPoint))
                            return gotten;
                current = current.getNext();
            }
        }
        return null;
    }

    /** Finds the path from one node to another using the findSpecifiedNode method
     * @param from The starting location
     * @param to The destination
     * @return Returns an array of nodes signaling the path from starting location to destination
     */
    public Object[] getPath(LatLng from, LatLng to)
    {
        if (from  == null || to == null)
            return null;
        TreeNode start = findSpecifiedNode(getHead(), from);
        if (start == null)
            return null;
        TreeNode end = findSpecifiedNode(start, to);
        if (end == null)
            return null;
        LinkedList<LatLng> path = new LinkedList<>(new LatLng(1,1));
        TreeNode current = end;
        while (!(current.equals(start)))
        {
            path.add(0,current.getElement());
            current = current.parent;
        }
        path.add(0,current.getElement());
        return path.toArray();
    }
}
