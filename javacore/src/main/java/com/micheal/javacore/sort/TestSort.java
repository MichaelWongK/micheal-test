package com.micheal.javacore.sort;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/8/5 17:17
 * @Description
 */
@Slf4j
public class TestSort {


    @org.junit.Test
    public void testSort() {
        List<SortDTO> list = ImmutableList.of(
                new SortDTO("300"),
                new SortDTO("50"),
                new SortDTO("200"),
                new SortDTO("220"),
                new SortDTO("7000"),
                new SortDTO("458")
        );

        // 我们先把数组的大小初始化成 list 的大小，保证能够正确执行 toArray
        SortDTO[] array = new SortDTO[list.size()];
        list.toArray(array);

        log.info("排序之前{}", JSON.toJSONString(array));
        Arrays.sort(array, Comparator.comparing(SortDTO::getSortTarget));
        log.info("排序之后{}", JSON.toJSONString(array));

        // sort 方法排序的性能较高，主要原因是 sort 使用了双轴快速排序算法
    }

    @Test
    public void binarySort() {
        List<SortDTO> list = ImmutableList.of(
                new SortDTO("300"),
                new SortDTO("50"),
                new SortDTO("200"),
                new SortDTO("220")
        );

        SortDTO[] array = new SortDTO[list.size()];
        list.toArray(array);

        log.info("搜索之前：{}", JSON.toJSONString(array));
        Arrays.sort(array, Comparator.comparing(SortDTO::getSortTarget));
        log.info("先排序，结果为：{}", JSON.toJSONString(array));
        int index = this.binarySearch(array, new SortDTO("200"),
                Comparator.comparing(SortDTO::getSortTarget));
        if (index < 0) {
            throw new RuntimeException("没有找到 200");
        }
        log.info("搜索结果：{}", JSON.toJSONString(array[index]));
    }

    public static <T> int binarySearch(T[] a, T key, Comparator<? super T> c) {
        return binarySearch0(a, 0, a.length, key, c);
    }

    // a：我们要搜索的数组，fromIndex：从那里开始搜索，默认是0； toIndex：搜索到何时停止，默认是数组大小
    // key：我们需要搜索的值 c：外部比较器
    private static <T> int binarySearch0(T[] a, int fromIndex, int toIndex,
                                         T key, Comparator<? super T> c) {
        // 如果比较器 c 是空的，直接使用 key 的 Comparable.compareTo 方法进行排序
        // 假设 key 类型是 String 类型，String 默认实现了 Comparable 接口，就可以直接使用 compareTo 方法进行排序
        if (c == null) {
            // 这是另外一个方法，使用内部排序器进行比较的方法
            return binarySearch0(a, fromIndex, toIndex, key);
        }
        int low = fromIndex;
        int high = toIndex - 1;
        // 开始位置小于结束位置，就会一直循环搜索
        while (low <= high) {
            // 假设 low =0，high =10，那么 mid 就是 5，所以说二分的意思主要在这里，每次都是计算索引的中间值
            int mid = (low + high) >>> 1;
            T midVal = a[mid];
            // 比较数组中间值和给定的值的大小关系
            int cmp = c.compare(midVal, key);
            // 如果数组中间值小于给定的值，说明我们要找的值在中间值的右边
            if (cmp < 0)
                low = mid + 1;
                // 我们要找的值在中间值的左边
            else if (cmp > 0)
                high = mid - 1;
            else
                // 找到了
                return mid; // key found
        }
        // 返回的值是负数，表示没有找到
        return -(low + 1);  // key not found.
    }

    // Like public version, but without range checks.
    private static int binarySearch0(Object[] a, int fromIndex, int toIndex,
                                     Object key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            @SuppressWarnings("rawtypes")
            Comparable midVal = (Comparable) a[mid];
            @SuppressWarnings("unchecked")
            int cmp = midVal.compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }

    // 找到倒数第 k 个的元素
    @Test
    public void test() {
        findKthLargest(new int[]{4,6,7,2,9,10,12}, 3);
        System.out.println("------------------------");
        findKthLargest2(new int[]{4,6,7,2,9,10,12}, 3);
        System.out.println("------------------------");
        topKFrequent(new int[]{1,1,1,2,2,3}, 2);
    }

    // 快速排序
    private void findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
        }
        System.out.println(nums[nums.length - k]);

    }

    // 小顶堆
    // JAVA中PRIORITYQUEUE详解 https://www.cnblogs.com/Elliott-Su-Faith-change-our-life/p/7472265.html
    private void findKthLargest2(int[] nums, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for (int val: nums) {
            priorityQueue.add(val);
            if (priorityQueue.size() > k) {
                priorityQueue.poll(); // 删除堆顶元素
            }
        }
        System.out.println(priorityQueue.peek());
        System.out.println(priorityQueue);
//        for (int i = 0; i < nums.length; i++) {
//            System.out.println(nums[i]);
//        }
    }

    // 桶排序-出现频率最多的 k 个元素
    private void topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num: nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return map.get(o1) - map.get(o2);
            }
        });

        for (int key: map.keySet()) {
            if (priorityQueue.size() < k) {
                priorityQueue.add(key);
            } else {
                priorityQueue.add(key);
                priorityQueue.remove();
            }
        }
        int[] result = new int[priorityQueue.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = priorityQueue.remove();
        }
//        while (!priorityQueue.isEmpty()) {
//            result.add(priorityQueue.remove());
//        }
//        result.toArray(new Integer[result.size()]);
        System.out.println(result);
    }

    // 按照字符出现次数对字符串排序 （大顶堆）
    @Test
    public void frequencySort() {
        String s = "ctddrfdeeaac";
        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Character> pq = new PriorityQueue<>(new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return map.get(o2) - map.get(o1); // 大顶堆
            }
        });
        pq.addAll(map.keySet());
        StringBuilder str = new StringBuilder();
        while (!pq.isEmpty()) {
            Character c = pq.remove();
            for (int i = 0; i < map.get(c); i++) {
                str.append(c);
            }
        }
        System.out.println(str.toString());
    }

    // 桶排序
    @Test
    public void frequencySort2() {
        String s = "ctddrfdeeaac";
        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        List<Character>[] frequencyBucket = new ArrayList[s.length() + 1];
        for (char c : map.keySet()) {
            int index = map.get(c);
            if (frequencyBucket[index] == null)
                frequencyBucket[index] = new ArrayList<>();
            frequencyBucket[index].add(c);
        }
        StringBuilder str = new StringBuilder();
        for (int i = frequencyBucket.length - 1; i >= 0; i--) {
            if (frequencyBucket[i] != null) {
                for (char c : frequencyBucket[i]) {
                    for (int j=0; j<i; j++) {
                        str.append(c);
                    }
                }
            }
        }
        System.out.println(str.toString());
    }

    @Test
    public void sortColors() {
        int[] nums = new int[]{2,0,2,1,0,1,0};
        int zero = -1, one = 0, two = nums.length;
        while (one < two) {
            if (nums[one] == 0) {
                swap(nums, ++zero, one++);
            } else if (nums[one] == 2) {
                swap(nums, --two, one);
            } else {
                ++one;
            }
        }
        for (int i = 0; i < nums.length; i++) {
            System.out.printf(nums[i] + ",");
        }
    }

    private void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    // 贪心算法
    
}