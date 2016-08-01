package com.dh.perfectoffer.event.framework.alibaba.fastjson.serializer;

public interface ValueFilter {

    Object process(Object source, String name, Object value);
}
