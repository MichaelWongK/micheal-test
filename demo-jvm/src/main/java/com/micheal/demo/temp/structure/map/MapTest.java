package com.micheal.demo.temp.structure.map;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/1/6 15:25
 * @Description
 */
public class MapTest {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put("micheal" + i, "micheal牛批" + i);

        }
            System.out.println(map.get("micheal"));
    }
}
