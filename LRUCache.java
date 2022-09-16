package com.lxm.dp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author lixinming
 * @Date 2022/9/15 15:07
 * @Version 1.0
 */
class LRUCache {

    int cap;
    LinkedHashMap<Integer,Integer> cache = new LinkedHashMap();
    //key --- value对应数组下标
    HashMap<Integer,Integer> map = new HashMap();
    //初始化
    public LRUCache(int capacity) {
        this.cap = capacity;
    }

    public int get(int key) {
        //如果没有返回-1
        if(!cache.containsKey(key)){
            return -1;
        }
        //如果有,将Key的浏览量+1,并更新缓存中的位置
        makeRecently(key);
        //返回数据
        return cache.get(key);
    }

    public void put(int key, int value) {
        //如果存在，更新缓存中浏览量及位置
        if(cache.containsKey(key)){
            cache.put(key,value);
            makeRecently(key);
            return;
        }
        //如果不存在,判断容量如果容量不足，删除初始位置
        if(cache.size() >= this.cap){
            Integer next = cache.keySet().iterator().next();
            cache.remove(next);
        }
        //放入缓存
        cache.put(key,value);
    }

    public void makeRecently(int key){
        //获取当前数据
        int value = cache.get(key);
        //比较Key的浏览量应该放在那个位置
        cache.remove(key);
        //删除之前的缓存,重新插入
        cache.put(key,value);
    }
}



