package com.micheal.javacore.sort;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/8/18 16:10
 * @Description
 */
@Slf4j
public class TreeMapDemo {

    @Data
    class DTO implements Comparable<DTO> {

        private Integer id;

        public DTO(Integer id) {
            this.id = id;
        }

        @Override
        public int compareTo(DTO o) {
            // 默认从小到大排序
            return id - o.getId();
        }
    }

    @Test
    public void testTwoComparable() {
        // 第一种排序，从小到大排序，实现 Comparable 的 compareTo 方法进行排序
        List<DTO> list = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            list.add(new DTO(i));
        }
        Collections.sort(list);
        log.info(JSON.toJSONString(list));

        // 第二种排序，从大到小排序，利用外部排序器 Comparator 进行排序
        Comparator comparator = (Comparator<DTO>) (o1, o2) -> o2.getId() - o1.getId();
        List<DTO> list2 = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            list2.add(new DTO(i));
        }
        list2.add(new DTO(10));
        list2.add(new DTO(8));
        list2.add(new DTO(9));
        list2.add(new DTO(7));
        Collections.sort(list2,comparator);
        log.info(JSON.toJSONString(list2));
    }
}
