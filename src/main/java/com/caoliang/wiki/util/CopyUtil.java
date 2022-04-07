package com.caoliang.wiki.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CopyUtil {

    /*单体复制*/
    public static <T> T copy(Object source, Class<T> clazz){
        if(source == null){
            return null;
        }
        T obj = null;

        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BeanUtils.copyProperties(source,obj);
        return obj;
    }

    /*列表复制*/
    public static <T> List<T> copyList(List source, Class<T> clazz){
        ArrayList<T> targetList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(source)){
            for (Object o : source) {
                T copy = copy(o, clazz);
                targetList.add(copy);
            }
        }
        return targetList;
    }
}
