package com.micheal.javacore.sort;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/3/17 13:49
 * @Description
 */
public class Test_1 {

    public static String intToRoman(int num) {


        Integer[] nums = new Integer[] {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

        Map<Integer, String> map = new HashMap<>();
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

        StringBuffer result = new StringBuffer();
        for (int i=0; i < nums.length; i++) {
            while (num >= nums[i]) {
                num -= nums[i];
                result.append(map.get(nums[i]));
            }

        }
        return result + "";
    }

    public static void main(String[] args) {
        System.out.println(intToRoman(1456));
    }
}
