package com.example.scoutconcordia;

interface BaseMethods <E>
{
    boolean add(E element);
    void add(int index, E element);
    void clear();
    E remove(int index);
    boolean removeOBJ(Object o);
    String toString();
    int size();
}
