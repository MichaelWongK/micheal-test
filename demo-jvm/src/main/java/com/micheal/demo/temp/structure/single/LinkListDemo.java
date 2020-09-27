package com.micheal.demo.temp.structure.single;

import org.springframework.util.Assert;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/14 20:37
 * @Description 单向链表demo
 */
class NodeSingle {
    private String data;
    private NodeSingle next;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public NodeSingle getNext() {
        return next;
    }

    public void setNext(NodeSingle next) {
        this.next = next;
    }
}

public class LinkListDemo {
    // 必须要有一个根节点
    public static NodeSingle root = new NodeSingle();
    // 插入节点
    public static void insert(NodeSingle nodeSingle) {
        // 先找到最后一个尾节点，然后将尾节点的next指针指向新的结点
        // 如何找到最后一个节点
        NodeSingle current = root;
        NodeSingle pNodeSingle;
        while ((pNodeSingle = current.getNext()) != null) {
            current = pNodeSingle;
        }
        current.setNext(nodeSingle);

    }
    // 遍历结点
    public static void getAll() {
        NodeSingle current = root;
        NodeSingle pNodeSingle;
        while ((pNodeSingle = current.getNext()) != null) {
            current = pNodeSingle;
            System.out.println(pNodeSingle.getData());
        }
//        System.out.println(current.getData());
    }

    // 删除
    public static void delete(String str) {
        NodeSingle current = root;
        NodeSingle pNodeSingle;
        while ((pNodeSingle = current.getNext()) != null) {
            if (pNodeSingle.getData().equals(str)) {
                current.setNext(pNodeSingle.getNext());
                pNodeSingle.setNext(null);
                return;
            }
            current = pNodeSingle;
//            System.out.println(pNode.getData());
        }
    }

    // 修改
    public static void modify(String originStr, String destStr) {
        Assert.notNull(originStr, "原始数据不能为null");
        NodeSingle current = root;
        NodeSingle pNodeSingle;
        while ((pNodeSingle = current.getNext()) != null) {
            if (originStr.equals(pNodeSingle.getData())) {
                pNodeSingle.setData(destStr);
                return;
            }
            current = pNodeSingle;
//            System.out.println(pNode.getData());
        }
    }

    public static void main(String[] args) {
        NodeSingle nodeSingle1 = new NodeSingle();
        NodeSingle nodeSingle2 = new NodeSingle();
        NodeSingle nodeSingle3 = new NodeSingle();
        NodeSingle nodeSingle4 = new NodeSingle();
        nodeSingle1.setData("1");
        nodeSingle2.setData("2");
        nodeSingle3.setData("3");
        nodeSingle4.setData("4");
        insert(nodeSingle1);
        insert(nodeSingle2);
        insert(nodeSingle3);
        insert(nodeSingle4);
        delete("2");
        modify(null, "5");
        getAll();
    }
}
