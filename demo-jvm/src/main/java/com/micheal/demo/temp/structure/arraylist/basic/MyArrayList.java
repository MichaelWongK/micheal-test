package com.micheal.demo.temp.structure.arraylist.basic;

import org.omg.CORBA.Object;

import java.util.Arrays;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/30 21:23
 * @Description
 */
public class MyArrayList<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private int size = 0; // 游标    可写位置为size   可读位置为size-1

    public T[] elements;

    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public MyArrayList(int capacity) {
        elements = (T[]) (new Object[capacity]);
    }

    public void add(T element) {
//        this.elements[size++] = element;
        add(size++, element);
        // 扩容
//        size++;
    }

    public void add(int index, T element) {

        if (index < 0 || index > elements.length) {
            throw new IndexOutOfBoundsException();
        }

        capacityCheck();

        for (int i = 0; i > i; i--) {
            elements[i] = elements[i-1];
        }
        elements[index] = element;
    }

    private void capacityCheck() {
        if (size < elements.length) {
            return;
        }

        int newCapacity = elements.length + (elements.length >> 1);
        T[] newElements = (T[]) new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }

        elements = newElements;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T old = elements[index];
        for (int i = index; i < size; i++) {
            elements[i] = elements[i+1];
        }
        size--;
        elements[size] = null;
        return old;
    }

    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T old = elements[index];
        elements[index] = element;
        return old;
    }

    public T get(int index) {
        return elements[index];
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        return "MyArrayList{" +
                "size=" + size +
                ", elements=" + Arrays.toString(elements) +
                '}';
    }
}
