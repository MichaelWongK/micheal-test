package com.micheal.demo.temp.structure.map;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/1/5 17:41
 * @Description
 */
public interface Map<K, V> {

    V put(K k, V v);

    V get(K k);

    int size();

    interface Entry<K, V> {
        K getKey();

        V getValue();
    }
}
