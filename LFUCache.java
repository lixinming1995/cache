package com.lxm.dp;

import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * @Author lixinming
 * @Date 2022/9/16 17:14
 * @Version 1.0
 */
public class LFUCache {
    //key-value存储
    HashMap<Integer, Integer> valueMap;
    //浏览量
    HashMap<Integer, Integer> keyFreMap;
    //浏览量指向指定key
    HashMap<Integer, LinkedHashSet<Integer>> freHashKeyMap;
    //最小浏览量
    int minFre;
    //容量
    int cap;

    public LFUCache(int capacity){
        this.valueMap = new HashMap<>(capacity);
        this.keyFreMap = new HashMap<>(capacity);
        this.freHashKeyMap = new HashMap<>(capacity);
        this.cap = capacity;
        this.minFre = 0;
    }

    //获取value
    public int get(int key){
        //如果是不存在返回-1
        if(!valueMap.containsKey(key)){
            return -1;
        }
        //如果存在更新缓存浏览量
        updateFre(key);
        return valueMap.get(key);
    }
    //放入缓存
    public void put(int key, int value){
        if(this.cap == 0){
            return;
        }
        //如果存在直接更新
        if(valueMap.containsKey(key)){
            valueMap.put(key,value);
            updateFre(key);
            return;
        }
        //如果不存在
        //如果内存已满先删除内存
        if(valueMap.size() >= this.cap){
            removeCache();
        }
        //内存未满
        //kv存储
        valueMap.put(key,value);
        keyFreMap.put(key,1);
        if(!freHashKeyMap.containsKey(1)){
            //如果不存在
            freHashKeyMap.put(1,new LinkedHashSet<>());
        }
        freHashKeyMap.get(1).add(key);
        this.minFre = 1;

    }
    //删除最小浏览量缓存
    public void removeCache(){
        //查询最小的minfre
        LinkedHashSet<Integer> min = freHashKeyMap.get(minFre);
        Integer next = min.iterator().next();
        min.remove(next);
        //如果最小的min为空
        if(min.isEmpty()){
            //删除最小min
            freHashKeyMap.remove(minFre);
            //这里其实不需要更新最小minfre浏览量,如果调用则是需要新增时,删除后浏览量必定会被更新为1；
        }
        valueMap.remove(next);
        keyFreMap.remove(next);
    }
    //更新浏览量
    public void updateFre(int key){
        Integer fre = keyFreMap.get(key);
        //删除旧的浏览量
        freHashKeyMap.get(fre).remove(key);
        //如果旧浏览量列表为空则删除
        if(freHashKeyMap.get(fre).isEmpty()){
            freHashKeyMap.remove(fre);
            // 如果旧浏览量刚好空了,且刚好等于min则+1
            if (fre == this.minFre) {
                this.minFre++;
            }
        }
        //查询浏览量+1的集合是否为空
        //为空则新建
        //不为空则添加到第一个
        if(!freHashKeyMap.containsKey(fre + 1)){
            freHashKeyMap.put(fre + 1,new LinkedHashSet<>());
        }
        freHashKeyMap.get(fre + 1).add(key);
        //覆盖keyfre
        keyFreMap.put(key, fre + 1);

    }

    public static void main(String[] args) {
        LFUCache lfuCache = new LFUCache(0);

        lfuCache.put(1,1);
        lfuCache.put(2,2);
        System.out.println(lfuCache.get(1));
        lfuCache.put(3,3);
        System.out.println(lfuCache.get(2));
        System.out.println(lfuCache.get(3));
        lfuCache.put(4,4);
        System.out.println(lfuCache.get(1));
        System.out.println(lfuCache.get(2));
        System.out.println(lfuCache.get(3));
    }


}