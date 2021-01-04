package com.micheal.demo.temp.structure.arraylist.basic;

import com.micheal.demo.po.User;

import java.util.ArrayList;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/30 22:30
 * @Description
 */
public class MyArrayListTest {

    public static void main2(String[] args) {
        MyArrayList myArrayList = new MyArrayList();
        myArrayList.add(1);
        myArrayList.add(2);
        myArrayList.add(3);
        myArrayList.add(4);
//        myArrayList.add(2, 19);
//        myArrayList.remove(3);
        myArrayList.add(0, 16);
        System.out.println(myArrayList.get(0));
    }

    public static void main(String[] args) {
//        main2(new String[]{});
        MyArrayList<User> myArrayList = new MyArrayList<User>();
        myArrayList.add(new User("micheal"));
        myArrayList.add(new User("kkk"));
        myArrayList.add(new User("jjjjjj"));
        myArrayList.add(new User("aaa"));
        myArrayList.add(new User("2223"));
    }
}
