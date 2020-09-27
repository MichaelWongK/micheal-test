package com.micheal.demo.temp.structure.single;

import org.springframework.util.Assert;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/14 20:37
 * @Description 单向链表demo
 */
class Node {
    private String data;
    private Node next;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}

public class DoubleLinkListDemo {
    // 必须要有一个根节点
    public static Node first;
    // 插入节点
    public static void insert(Node node) {
        Node current = null;
        // 先找到最后一个尾节点，然后将尾节点的next指针指向新的结点
        // 如何找到最后一个节点
        // 插入的时候，需要做什么？判断first也就是根节点是否为空，
        // 如果为空，是不是就把当前节点作为头节点
        if (first == null) {
            first = node;
            return;
        }
        current = first;
        Node pNode;
        while ((pNode = current.getNext()) != null) {
            current = pNode;
        }
        current.setNext(node);

    }
    // 遍历结点
    public static void getAll() {
        Assert.notNull(first, "当前链表为空");
        Node current = first;
        Node pNode;
        while ((pNode = current.getNext()) != null) {
            System.out.println(current.getData());
            current = pNode;
//            System.out.println(pNode.getData());
        }
        System.out.println(current.getData());
    }

    // 删除
//    public static void delete(String str) {
//        Node current = root;
//        Node pNode;
//        while ((pNode = current.getNext()) != null) {
//            if (pNode.getData().equals(str)) {
//                current.setNext(pNode.getNext());
//                pNode.setNext(null);
//                return;
//            }
//            current = pNode;
////            System.out.println(pNode.getData());
//        }
//    }
//
//    // 修改
//    public static void modify(String originStr, String destStr) {
//        Assert.notNull(originStr, "原始数据不能为null");
//        Node current = root;
//        Node pNode;
//        while ((pNode = current.getNext()) != null) {
//            if (originStr.equals(pNode.getData())) {
//                pNode.setData(destStr);
//                return;
//            }
//            current = pNode;
////            System.out.println(pNode.getData());
//        }
//    }

    public static void main(String[] args) {
        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        Node node4 = new Node();
        node1.setData("1");
        node2.setData("2");
        node3.setData("3");
        node4.setData("4");
        insert(node1);
        insert(node2);
        insert(node3);
        insert(node4);
//        delete("2");
//        modify(null, "5");
        getAll();
    }
}
