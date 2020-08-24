package com.micheal.wang.proxydemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/8/20 17:42
 * @Description
 */
public class Test {


    public static int[] twoSum(int[] numbers, int target) {
        int index1=0, index2=0;
        int[] newElements;
        boolean flag = false;
        for (int i=0; i<numbers.length; i++) {
            index1 = i;
            int right = target -numbers[index1];
            for (int j=i+1; j<numbers.length;j++) {
                index2 = j;
                if (numbers[index2] == right) {
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
        if (target == (numbers[index1] + numbers[index2])) {
            return new int[]{++index1, ++index2};
        }
        return null;
    }

    public static boolean judgeSquareSum(int target) {
        if (target < 0) return false;
        int a = 0,b = (int) Math.sqrt(target);
        while (a<b) {
            int sum = a * a + b * b;
            if (sum == target) {
                return true;
            } else if (sum > target) {
                b--;
            } else {
                a++;
            }
        }
        return false;
    }

    public static final HashSet<Character> elements = new HashSet<>(
            Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'));

    public static String reverseVowels(String str) {
        if (str == null) return null;
        int i = 0, j = str.length() - 1;
        char[] result = str.toCharArray();
        while (i <= j) {
            char ci = result[i];
            char cj= result[j];
            if (!elements.contains(ci)) {
//                result[i++] = ci;
                i++;
            } else if (!elements.contains(cj)) {
                j--;
            } else {
                result[i] = cj;
                result[j] = ci;
                i++;
                j--;
            }
        }
        return new String(result);
    }

    public static boolean validPalindrome(String str) {
        char[] result = str.toCharArray();
        int i = 0, j = str.length() - 1;
        while (i < j) {
            char ci = result[i];
            char cj = result[j];
            if(ci != cj) {
                return isValidPalindrome(result, i + 1, j) || isValidPalindrome(result, i, j - 1);
            }
            i++;
            j--;
        }
        return true;
    }

    public static boolean isValidPalindrome(char[] result, int i, int j) {
        while (i < j) {
            char ci = result[i];
            char cj = result[j];
            if(ci != cj) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int index1 = m -1, index2 = n - 1;
        int num = m + n -1;
        while (index1 >= 0 || index2 >= 0) {
            if (index1 < 0) {
                nums1[num--] = nums2[index2--];
            } else if (index2 < 0) {
                nums1[num--] = nums1[index1--];
            } else if (nums1[index1] > nums2[index2]){
                nums1[num--] = nums1[index1--];
            } else {
                nums1[num--] = nums2[index2--];
            }
        }
        System.out.println(nums1);
    }

    public static String findLongestWord(String s, List<String> d) {
        String longestWord = "";
        for (String target : d) {
            if (longestWord.length() > target.length() || (longestWord.length() == target.length() && longestWord.compareTo(target) < 0)) {
                continue;
            }
            if (isSub(s, target)) {
                longestWord = target;
            }
        }
        return longestWord;
    }

    public static boolean isSub(String s, String target) {
        int i=0, j=0;
        while(i < s.length() && j < target.length()) {
            if (s.charAt(i) == target.charAt(j)) {
                j++;
            }
            i++;
        }
        return j == target.length();
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("ale");
        list.add("apple");
        list.add("monkey");
        list.add("plea");

        System.out.println(findLongestWord("abpcplea", list));

//        int[] num1 = new int[]{1,2,4,6,8,0,0,0};
//        int[] num2 = new int[]{2,3,7};
//        merge(num1, 5, num2, num2.length);
//        System.out.println(num1);
//
//        System.out.println(validPalindrome("cbbcc"));
//        System.out.println(reverseVowels("leetcode"));;

//        System.out.println(twoSum(new int[]{1,2,3,4,4,9,56,90}, 8));;
    }

}
