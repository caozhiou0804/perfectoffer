package com.dh.perfectoffer.event.framework.alibaba.fastjson.serializer;

public interface NameFilter {

    String process(Object source, String name, Object value);
}
