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

    // 贪心算法 https://leetcode-cn.com/problems/assign-cookies/description/
//    @Test
    public void findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int gi = 0, si = 0;
        while (gi < g.length && si < s.length) {
            if (g[gi] <= s[si]) {
                gi++;
            }
            si++;
        }
    }

    /**
     * 在每次选择中，区间的结尾最为重要，选择的区间结尾越小，留给后面的区间的空间越大，
     * 那么后面能够选择的区间个数也就越大。
     *
     * 按区间的结尾进行排序，每次选择结尾最小，并且和前一个区间不重叠的区间。
     */
    @Test
    public void eraseOverlapIntervals() {
        int[][] intervals = new int[][] {{1,2}, {2,3}, {3,4}, {1,3} };
        if (intervals.length == 0) {
            return;
        }
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[1]));
        int cnt = 1;
        int end = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < end) {
                continue;
            }
            end = intervals[i][1];
            cnt++;
        }
        System.out.println(intervals.length - cnt);
        return ;
    }

    /**
     * 投飞镖刺破气球
     *
     * 题目描述：气球在一个水平数轴上摆放，可以重叠，飞镖垂直投向坐标轴，使得路径上的气球都被刺破。
     * 求解最小的投飞镖次数使所有气球都被刺破。
     * 也是计算不重叠的区间个数，不过和 Non-overlapping Intervals 的区别在于，
     * [1, 2] 和 [2, 3] 在本题中算是重叠区间。
     */
    @Test
    public void findMinArrowShots() {
        int[][] points = new int[][]{{7,0}, {4,4}, {7,1}, {5,0}, {6,1}, {5,2}};
        Arrays.sort(points, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] - o2[1];
            }
        });
        int cnt = 1, end = points[0][1];
        for (int i = 1; i < points.length; i++) {
            if (points[i][0] <= end) {
                continue;
            }
            cnt++;
            end = points[i][1];
        }
        System.out.println(cnt);
    }

    /**
     * Input:
     * [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
     *
     * Output:
     * [[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]]
     * 题目描述：一个学生用两个分量 (h, k) 描述，h 表示身高，
     * k 表示排在前面的有 k 个学生的身高比他高或者和他一样高。
     *
     * 为了使插入操作不影响后续的操作，身高较高的学生应该先做插入操作，
     * 否则身高较小的学生原先正确插入的第 k 个位置可能会变成第 k+1 个位置。
     *
     * 身高 h 降序、个数 k 值升序，然后将某个学生插入队列的第 k 个位置中。
     */
    @Test
    public void reconstructQueue() {
        int[][] people = new int[][]{{7,0}, {4,4}, {7,1}, {5,0}, {6,1}, {5,2}};
        Arrays.sort(people, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0];
            }
        });
        List<int[]> queue = new ArrayList<>();
        for (int[] p: people) {
            queue.add(p[1], p);
        }
        System.out.println(queue.toArray(new int[queue.size()][]));
    }

    /**
     * 买卖股票最大的收益
     * 一次股票交易包含买入和卖出，只进行一次交易，求最大收益。
     *
     * 只要记录前面的最小价格，将这个最小价格作为买入价格，然后将当前的价格作为售出价格，查看当前收益是不是最大收益。
     */
    @Test
    public void maxProfit() {
        int[] prices = new int[]{7,1,5,3,6,4};
        if (prices.length == 0)
            return;
        int min = prices[0], max = 0;
        for (int i = 1; i < prices.length; i++) {
            if (min > prices[i]) {
                min = prices[i];
            } else {
                max = Math.max(max, prices[i] - min);
            }
        }
        System.out.println(max);
    }

    /**
     * 买卖股票的最大收益 II
     * 可以进行多次交易，多次交易之间不能交叉进行，可以进行多次交易。
     *
     * 对于 [a, b, c, d]，如果有 a <= b <= c <= d ，那么最大收益为 d - a。
     * 而 d - a = (d - c) + (c - b) + (b - a) ，
     * 因此当访问到一个 prices[i] 且 prices[i] - prices[i-1] > 0，
     * 那么就把 prices[i] - prices[i-1] 添加到收益中。
     */
    @Test
    public void  maxProfit2() {
        int[] prices = new int[] {7,1,5,3,6,4};
        int profit = 0;
        for (int i = prices.length-1; i > 0; i--) {
            if (prices[i] > prices[i-1]) {
                profit += prices[i] - prices[i-1];
            }
        }
        System.out.println(profit);
    }

    /**
     * 种花问题
     * 假设你有一个很长的花坛，一部分地块种植了花，另一部分却没有。可是，花卉不能种植在相邻的地块上，
     * 它们会争夺水源，两者都会死去。
     *
     * 给定一个花坛（表示为一个数组包含0和1，其中0表示没种植花，1表示种植了花），和一个数 n 。
     * 能否在不打破种植规则的情况下种入 n 朵花？能则返回True，不能则返回False。
     *
     */
    @Test
    public void canPlaceFlower() {
        int[] flowerbed = new int[]{1,0,0,0,1,0,0};
        int n = 2;
        int cnt = 0;
        for (int i = 0; i < flowerbed.length && cnt < n ; i++) {
            if (flowerbed[i] == 1)
                continue;
            int prev = i == 0 ? 0 : flowerbed[i-1];
            int next = i == flowerbed.length - 1 ? 0: flowerbed[i+1];
            if (prev == 0 && next == 0) {
                cnt++;
                flowerbed[i] = 1;
            }
        }
        System.out.println(cnt>=n);
    }

    /**
     * 在出现 nums[i] < nums[i - 1] 时，需要考虑的是应该修改数组的哪个数，
     * 使得本次修改能使 i 之前的数组成为非递减数组，并且 不影响后续的操作 。
     * 优先考虑令 nums[i - 1] = nums[i]，因为如果修改 nums[i] = nums[i - 1] 的话，
     * 那么 nums[i] 这个数会变大，就有可能比 nums[i + 1] 大，从而影响了后续操作。
     * 还有一个比较特别的情况就是 nums[i] < nums[i - 2]，
     * 修改 nums[i - 1] = nums[i] 不能使数组成为非递减数组，只能修改 nums[i] = nums[i - 1]。
     */
    @Test
    public void checkPossibility() {
        int[] nums = new int[]{3,4,2,3};
        int cnt = 0;
        for (int i=1; i < nums.length && cnt < 2; i++) {
            if (nums[i] >= nums[i-1])
                continue;
            cnt++;
            if (i - 2 >= 0 && nums[i - 2] > nums[i]) {
                nums[i] = nums[i-1];
            } else {
                nums[i-1] = nums[i];
            }
        }
        System.out.println(cnt<=1);
    }

    public static void main(String[] args) {
        int[][] people = new int[][]{{7,0}, {4,4}, {7,1}, {5,0}, {6,1}, {5,2}};
        Arrays.sort(people, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0];
            }
        });
        List<int[]> queue = new ArrayList<>();
        for (int[] p: people) {
            queue.add(p[1], p);
        }

    }

}