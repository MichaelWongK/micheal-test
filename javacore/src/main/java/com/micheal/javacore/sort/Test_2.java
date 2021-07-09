package com.micheal.javacore.sort;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/3/17 15:24
 * @Description
 * 670. 最大交换
 * 给定一个非负整数，你至多可以交换一次数字中的任意两位。返回你能得到的最大值。
 *
 * 示例 1 :
 *
 * 输入: 2736
 * 输出: 7236
 * 解释: 交换数字2和数字7。
 * 示例 2 :
 *
 * 输入: 9973
 * 输出: 9973
 * 解释: 不需要交换。
 * 注意:
 *
 * 给定数字的范围是 [0, 108]
 */
public class Test_2 {

    public static int maximumSwap(int num) {
        int length = String.valueOf(num).length();
        String maxString = "0";
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length ; j++) {
                char[] charArray = String.valueOf(num).toCharArray();
                char temp = charArray[i];
                charArray[i] = charArray[j];
                charArray[j] = temp;
                if (Integer.valueOf(String.valueOf(charArray)) > Integer.valueOf(maxString)) {
                    maxString = String.valueOf(charArray);
                }

            }
        }
        return  Integer.valueOf(maxString);
    }

    public static void main(String[] args) {
        System.out.println(maximumSwap(2736));;
    }
}
