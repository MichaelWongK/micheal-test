package com.micheal.javacore.sort;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        if(index<0){
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
            Comparable midVal = (Comparable)a[mid];
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
}
