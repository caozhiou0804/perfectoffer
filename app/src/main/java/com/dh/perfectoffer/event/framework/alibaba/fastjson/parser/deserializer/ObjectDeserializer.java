package com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.deserializer;

import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.DefaultExtJSONParser;

import java.lang.reflect.Type;


public interface ObjectDeserializer {
    <T> T deserialze(DefaultExtJSONParser parser, Type type);
    
    int getFastMatchToken();
}
