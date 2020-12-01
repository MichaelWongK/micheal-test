package com.micheal.demo.temp.structure.tree;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/1 10:29
 * @Description
 * 输入 1,A,0;2,B,1;3,C,2;4,D,2
 * 构建一棵树，参数分别是 id, name, pid
 */
public class TreeDemo {

    private static List<Dept> depts;

    /**
     * 切割字符串，并且构建部门的java bean保存起来
     * @param dataString  控制台输入的字符串
     */
    public static void createDepts(String dataString) {
        String[] deptDataArray = dataString.split(";");
        if (deptDataArray.length < 3) {
            System.out.println("data error");
            return;
        }
        depts = new ArrayList<>(deptDataArray.length);
        for (String string : deptDataArray) {
            String[] metaData = string.split(",");
            Dept dept = new Dept();
            dept.setId(Integer.valueOf(metaData[0]));
            dept.setName(metaData[1]);
            dept.setPid(Integer.valueOf(metaData[2]));
            depts.add(dept);
        }
    }

    /**
     * 创建一棵树
     */
    public static Dept createTree() {
        if (depts == null) {
            System.out.println("请先创建部门");
            return null;
        }

        Dept root = null;
        // 第一步找到根节点
        for (Iterator<Dept> iterator = depts.iterator(); iterator.hasNext();) {
            Dept dept = iterator.next();
            if (dept.getPid() == 0) {
                root = dept;
                iterator.remove();
            }
        }

        if (root == null) {
            System.out.println("找不到根节点");
            return null;
        }

        // 找到根节点以下的节点
        recuCreate(root, depts);
        return root;
    }

    public static void recuCreate(Dept dept, List<Dept> depts) {
        for (Iterator<Dept> iterator = depts.iterator(); iterator.hasNext();) {
            Dept deptTemp = iterator.next();
            if (deptTemp.getPid() == dept.getId()) {
                dept.getSubDepts().add(deptTemp);
                iterator.remove();
                recuCreate(deptTemp, depts);
            }
        }

    }

    /**
     * 遍历
     */
    public static void traverseTree(Dept dept) {
        System.out.println(dept);
        if (dept.getSubDepts() != null) {
            for (Dept subDept : dept.getSubDepts()) {
                traverseTree(subDept);
            }
        }
    }

    @Data
    @ToString
    static class Dept {
        private Integer id;
        private String name;
        private Integer pid;
        private List<Dept> subDepts = new ArrayList<>();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();
        createDepts(nextLine);
        Dept dept = createTree();
        scanner.close();
        System.out.println(depts);
        System.out.println("*********************");
        System.out.println(dept);
        System.out.println("*********************");
        traverseTree(dept);
    }
}
