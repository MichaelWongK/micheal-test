package com.micheal.demo.temp.lock;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/15 23:46
 * @Description
 */
public class MyBatisEnhance {
    private static int size = 1000000;

    private static Map<Object, Object> objectMap = new ConcurrentHashMap<>(size);

    private static MichealLock michealLock;

    @Data
    static class MapperScanner {
        private String mapperLocation;

        public List<Object> scanMapper() {
            List<Object> objects = Lists.newArrayList();
            for (int i = 0; i < size; i++) {
                objects.add(MyBatisEnhance.class);
            }
            return objects;
        }
    }

    public static void put(Object obj) {
        objectMap.put(obj, obj);
    }


    public static void main(String[] args) {
        String mapperLocation = "classpath:mapper/*.xml";
        MapperScanner mapperScanner = new MapperScanner();
        mapperScanner.setMapperLocation(mapperLocation);

        /**
         * 1000个
         */
        List<Object> classes = mapperScanner.scanMapper();
        long start = System.currentTimeMillis();
        classes.parallelStream().forEach(clazz -> put(new Object()));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
