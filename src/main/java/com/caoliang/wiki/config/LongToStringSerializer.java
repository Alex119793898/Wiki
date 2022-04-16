package com.caoliang.wiki.config;


import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class LongToStringSerializer implements ObjectSerializer {
    public static final LongToStringSerializer instance = new LongToStringSerializer();

    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object o1, Type type, int i) throws IOException {
        SerializeWriter out = jsonSerializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        if(object instanceof Long){
            String strVal = object.toString();
            out.writeString(strVal);
        }
    }
}
