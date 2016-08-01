package com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.deserializer;

import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONToken;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.ParserConfig;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Type;


public class ThrowableDeserializer extends JavaBeanDeserializer {

    public ThrowableDeserializer(ParserConfig mapping, Class<?> clazz){
        super(mapping, clazz);
    }

    @Override
	@SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz) {
        Object jsonValue = parser.parse();
        return (T) TypeUtils.cast(jsonValue, clazz, parser.getConfig());
    }

    @Override
	public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
