package com.micheal.demo.temp.structure.map;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/1/5 17:53
 * @Description
 */
public class HashMap<K, V> implements Map<K,V> {

    // 数组
    private Entry<K, V> table[] = null;

    public HashMap() {
        table = new Entry[16];
    }

    int size = 0;

    /**
     * 通过 key hash
     * index数组下标 当前数组对应的对象 Entry
     * 判断当前这个对象是否为空 如果为空的
     * 是直接可以存储数据的，如果不为空
     * 属于hash冲突 所以要用链表
     * 然后返回这个存储结果
     * @param k
     * @param v
     * @return
     */
    @Override
    public V put(K k, V v) {
        int index = hash(k);
        Entry<K, V> entry = table[index];
        if (entry == null) {
            table[index] = new Entry<>(k, v, index, null);
            size++;
        } else {
            table[index] = new Entry<>(k, v, index, entry);
        }

        return (V) table[index].getValue();
    }

    private int hash(K k) {
        int i = k.hashCode() % 16;
        return i >= 0 ? i : -i;
    }

    /**
     * 通过key进行hash
     * 找到数组下标index
     * 找到entry对象 是否为空
     * 如果为空 直接返回 没有找到
     * 如果不为空 判断k和当前对象的key是否想等
     * 如果不想等 不是它 判断next值是否为空
     * 不为空 比较key值 知道想等 返回value 或者返回null
     * @param k
     * @return
     */
    @Override
    public V get(K k) {
        if (size == 0) {
            return null;
        }
        int index = hash(k);
        Entry<K, V> entry = findValue(table[index], k);

        return entry == null ? null : (V) entry.getValue();
    }

    private Entry<K, V> findValue(Entry<K,V> entry, K k) {
        if (k.equals(entry.getKey()) || k == entry.getKey()) {
            return entry;
        } else {
            if (entry.next != null) {
                return findValue(entry.next, k);
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    class Entry<K, V> implements Map.Entry {

        K k;
        V v;
        int hash;
        Entry<K, V> next;

        public Entry(K k, V v, int hash, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public Object getKey() {
            return k;
        }

        @Override
        public Object getValue() {
            return v;
        }
    }
}
