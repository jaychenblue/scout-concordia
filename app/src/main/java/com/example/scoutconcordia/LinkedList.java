package com.example.scoutconcordia;

import java.io.Serializable;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

public class LinkedList <E> implements BaseMethods<E>
{
    private Node head;
    private Node tail;
    private int size;
    private E classType;
    private static final File destination = new File("Temp.txt");
    
    private class Node
    {
        private E element;
        private LinkedList.Node next;
        private LinkedList.Node prev;
        
        Node()
        {
            element = null;
            next = null;
            prev = null;
            size++;
        }
        
        Node(E element, LinkedList.Node after, LinkedList.Node previous)
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
        private Node getNext() {return next;}
        private Node getPrev() {return prev;}
        private E getEle() {return element;}
        public String toString() {return element.toString();}
    }
    
    public LinkedList(E classType)
    {
        head = null;
        tail = null;
        size = 0;
        this.classType = (E)classType;
    }
    
    public LinkedList(E classType, E[] elements)
    {
        this(classType);
        for (int i = 0; i < elements.length; i++)
            if (elements[i] != null)
                addToTail(elements[i]);
    }

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
        Node toBeBefore = toBeNext.getPrev();
        Node newOne = new Node(element, toBeNext, toBeBefore);
        toBeBefore.setNext(newOne);
        toBeNext.setPrev(newOne);
        toBeBefore = null;
        toBeNext = null;
        newOne = null;
        return true;
    }
    
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
        Node before = toBeRemoved.getPrev();
        Node after = toBeRemoved.getNext();
        returnMe = (E)toBeRemoved.getEle();
        toBeRemoved.element = null;
        toBeRemoved.setPrev(null);
        toBeRemoved.setNext(null);
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
    
    private boolean remove(Serializable s)
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
    
    public void clear()
    {
        for (int i = size - 1; i > -1; i--)
            remove(i);
    }
    
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



    public Object clone() {
        throw new UnsupportedOperationException("This class does not support that method.");
    }

    public boolean addAll(int i, Collection given) {
        throw new UnsupportedOperationException("This class does not support that method.");
    }
    public boolean addAll(Collection given) {
        throw new UnsupportedOperationException("This class does not support that method.");
    }

    public boolean contains(Object given) {
        throw new UnsupportedOperationException("This class does not support that method.");
    }

    public int indexOf(Object given) {
        throw new UnsupportedOperationException("This class does not support that method.");
    }

    public int lastIndexOf(Object given) {
        throw new UnsupportedOperationException("This class does not support that method.");
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException("This class does not support that method.");
    }
    public Object[] toArray(Object[] a) {
        throw new UnsupportedOperationException("This class does not support that method.");
    }

    public void trimToSize() {
        throw new UnsupportedOperationException("This class does not support that method.");
    }
    

    /*
    private Serializable deepClone(Serializable o)
    {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        E returnMe = null;
        try
        {
            oos = new ObjectOutputStream(new FileOutputStream(destination));
            oos.writeObject(o);
            oos.flush();
            ois = new ObjectInputStream(new FileInputStream(destination));
            returnMe = (E)ois.readObject();
            try
            {
                oos.close();
                ois.close();
            }
            catch (IOException ioe2)
            {
                System.out.println("There was an issue in closing one of the binary streams");
            }
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class read isn't located anywhere that registers with the written OBJ");
        }
        catch (FileNotFoundException fnf)
        {
            System.out.println("Temporary FIle was moved while serializing and de-serializing OBJs");
        }
        catch (IOException ioe)
        {
            System.out.println("An issue occurred while opening the binary files");
        }
        finally
        {
            return returnMe;
        }
    }*/
    
    public boolean removeOBJ(Object o)
    {
        if (o == null)
            return false;
        if (o.getClass() != classType.getClass())
            return false;
        return remove((Serializable)o);
    }
    
    public int size() {return size;}
    public boolean add(E element) {return addToTail(element);}
    public void add(int index, E element){addToIndex(index, element);}
    public E remove(int index){return removeNode(index);}
}