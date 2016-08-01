package com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.deserializer;

import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ArrayListTypeDeserializer implements ObjectDeserializer {

    private Type itemType;

    public ArrayListTypeDeserializer(Type type){
        this.itemType = type;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultExtJSONParser parser, Type type) {
        ArrayList list = new ArrayList();

        parser.parseArray(itemType, list);

        return (T) list;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

}
