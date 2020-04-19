package com.example.scoutconcordia.DataStructures;

/** Class for implementing a LinkedList data structure. */
public class LinkedList <E>
{
    private Node head;
    private Node tail;
    private int size;
    private E classType;

    /** Inner class representing a Node of the LinkedList */
    public class Node
    {
        private E element;
        private LinkedList.Node next;
        private LinkedList.Node prev;

        /** Default constructor for a Node
         * Sets element, next, and prev to null.
         */
        public Node()
        {
            element = null;
            next = null;
            prev = null;
            size++;
        }

        /** Parameterized constructor for a Node
         * @param element The value of the node
         * @param after The following Node in the LinkedList
         * @param previous The previous Node in the LinkedList
         */
        public Node(E element, LinkedList.Node after, LinkedList.Node previous)
        {
            if (element != null && element.getClass() == classType.getClass())
                this.element = (E)element;
            else
                element = null;
            next = after;
            prev = previous;
            size++;
        }

        private void setNext(Node nexty) {next = nexty;}
        private void setPrev(Node prevy) {prev = prevy;}
        public Node getNext() {return next;}
        public Node getPrev() {return prev;}
        public E getEle() {return element;}
        public String toString() {return element.toString();}
    }

    /** Default constructor for the LinkedList
     * @param classType the class type fo the LinkedList
     */
    public LinkedList(E classType)
    {
        head = null;
        tail = null;
        size = 0;
        this.classType = (E)classType;
    }

    /** Instantiate a Linked List with a pre-existing array of elements.
     * @param classType the class type of the LinkedList
     * @param elements is an array of elements that you wish to use for the LinkedList
     */
    public LinkedList(E classType, E[] elements)
    {
        this(classType);
        for (int i = 0; i < elements.length; i++)
            if (elements[i] != null)
                addToTail(elements[i]);
    }

    /** Adds an element to the the head of the LinkedList
     * @param element is the element that you wish to add
     * @return Returns true if successful
     */
    private boolean addToHead(E element)
    {
        Node oldhead = head;
        head = new Node(element, oldhead, null);
        if (oldhead != null)
            oldhead.setPrev(head);
        if (tail == null)
            tail = head;
        oldhead = null;
        return true;
    }

    /** Adds an element to the tail of the LinkedList
     * @param element is the element that you wish to add
     * @return Returns true if successful
     */
    private boolean addToTail(E element)
    {
        Node oldtail = tail;
        tail = new Node(element, null, tail);
        if (oldtail != null)
            oldtail.setNext(tail);
        if (head == null)
            head = tail;
        oldtail = null;
        return true;
    }

    /** Adds an element at the specified index of the LinkedList
     * @param index is the index in the LinkedList to add the element
     * @param element is the element that you wish to add
     * @return Returns true if successful.
     */
    private boolean addToIndex(int index, E element)
    {
        if (index > size)
            return false;
        if (index == size)
            return addToTail(element);
        if (index == 0)
            return addToHead(element);
        
        // Not base case
        Node toBeNext = getNodeFromIndex(index);
        Node toBeBefore = null;
        if (toBeNext != null)
            toBeBefore = toBeNext.getPrev();
        Node newOne = new Node(element, toBeNext, toBeBefore);
        if (toBeBefore != null)
        {
            toBeBefore.setNext(newOne);
        }
        if (toBeNext != null)
        {
            toBeNext.setPrev(newOne);
        }
        toBeBefore = null;
        toBeNext = null;
        newOne = null;
        return true;
    }

    /** Searches a specified index in the LinkedList for an element
     * @param index is the index in the LinkedList at which to find an element
     * @return Returns the value of the element at the specified index in the LinkedList
     */
    private Node getNodeFromIndex(int index)
    {
        Node cycler = null;
        if (index >= 0 && index < size)
        {
            cycler = head;
            for (int i = 0; i < index; i++)
                cycler = cycler.next;
        }
        return cycler;
    }

    /** Removes the node at the specified index in the LinkedList
     * @param index is the index in the LinkedList at which you want to remove a node
     * @return Returns the element that has been removed from the LinkedList
     */
    private E removeNode(int index)
    {
        E returnMe = null;
        if (index == 0)
        {
            if (head == null)
                return null;
            returnMe = head.getEle();
            head = head.getNext();
            if (head != null)
                head.setPrev(null);
            size--;
            return returnMe;
        }
        if (index >= size || index < 0)
            return null;
        Node toBeRemoved = getNodeFromIndex(index);
        Node before = null;
        Node after = null;
        if (toBeRemoved != null)
        {
            before = toBeRemoved.getPrev();
            after = toBeRemoved.getNext();
            returnMe = (E)toBeRemoved.getEle();
            toBeRemoved.element = null;
            toBeRemoved.setPrev(null);
            toBeRemoved.setNext(null);
        }
        toBeRemoved = null;
        if (before != null)
            before.setNext(after);
        if (after != null)
            after.setPrev(before);
        before = null;
        after = null;
        size--;
        return returnMe;
    }
    
    private E remove(int index){return removeNode(index);}

    /** Removes an object from the LinkedList
     * @param s is the object that you wish to remove from the LinkedList
     * @return Returns true if the object was in the LinkedList and has been removed successfully.
     * Otherwise, returns false.
     */
    private boolean remove(Object s)
    {
        Node cycler = head;
        for (int i = 0; i < size; i++)
        {
            if (cycler.getEle().equals(s))
            {
                remove(i);
                return true;
            }
            cycler = cycler.getNext();
        }
        return false;
    }

    /** Clears the LinkedList of all elements */
    public void clear()
    {
        for (int i = size - 1; i > -1; i--)
            remove(i);
    }

    /** Override the toString method
     * @return Returns a custom formatted output for the LinkedList to display.
     */
    @Override public String toString()
    {
        int sizeDigits = 0, multiplier = 1, entryLength = 0, numLength = 0;
        do
        {
            multiplier *= 10;
            sizeDigits++;
        } while ((size / (multiplier) > 0));

        if (head != null)
            entryLength = (head.toString()).length();
        else
            entryLength = 0;

        // Calculate numLength / number of num characters
        if (size == 0)
            numLength = 1;
        else if (size < 10)
            numLength = size;
        else if (sizeDigits == 2)
        {
            int first = size/10;
            int second = size - first*10;
            numLength = 10;
            numLength += (first - 1) * 10 * 2;
            numLength += (second + 1) * 1 * 2;// never gotta worry bout first digit < 0 since above currentOpen
        }
        else
        {
            numLength = 10;
            multiplier /= 10;       // Back at 1 * max digit/ = ^10, ex: if 300 at 100
            for (int i = 2; i < sizeDigits; i++)
                numLength += 9 * 10 * i;
            numLength += (size - multiplier + 1) * sizeDigits; // 1 for if 10, 0 still a displayed char
        }

        StringBuilder printme = new StringBuilder(8+sizeDigits+((entryLength + 11)*size) + numLength);
        printme.append("Size: ");
        printme.append(size);
        printme.append("\n");                       // 8

        // Entries
        // printme.append("Entry 1: " + entries.toString() + "\n")      // Not truely 11
        // Fixed and called numLength, added to stringBuilder initial size
        Node cycler = head;
        if (cycler != null)
            for (int i = 0; i < size; i++)
            {
                printme.append("Entry ");
                printme.append(i);
                printme.append(": ");
                printme.append(cycler.toString());
                printme.append("\n");
                cycler = cycler.getNext();
            }
        return printme.toString();
    }

    /** Method for checking if the LinkedList contains a certain object
     * @param given id the object to search for in the LinkedList
     * @return Returns true if the object is found in the LinkedList. Otherwise, returns false.
     */
    public boolean contains(Object given)
    {
        Node cycler = head;
        for (int i = 0; i < size; i++)
        {
            if (cycler.getEle().equals(given))
            {
                return true;
            }
            cycler = cycler.getNext();
        }
        return false;
    }

    /** Searches for the index of an object in the LinkedList
     * @param given An object to search for
     * @return Returns -1 if not found, and otherwise returns the index of the object.
     */
    public int indexOf(Object given)
    {
        Node cycler = head;
        for (int i = 0; i < size; i++)
        {
            if (cycler.getEle().equals(given))
            {
                return i;
            }
            cycler = cycler.getNext();
        }
        return -1;
    }

    /** Converts the LinkedList into a Java array.
     * @return Returns an Object array containing all the Nodes of the LinkedList
     */
    public Object[] toArray()
    {
        Object[] returnMe = new Object[size];
        Node cycler = head;
        for (int i = 0; i < size; i++)
        {
            returnMe[i] = cycler.element;
            cycler = cycler.getNext();
        }
        return returnMe;
    }

    public boolean removeOBJ(Object o)
    {
        if (o == null)
            return false;
        if (o.getClass() != classType.getClass())
            return false;
        return remove(o);
    }

    public int size() {return size;}
    public boolean add(E element) {return addToTail(element);}
    public void add(int index, E element){addToIndex(index, element);}
    public Node getHead() {return head;}
}