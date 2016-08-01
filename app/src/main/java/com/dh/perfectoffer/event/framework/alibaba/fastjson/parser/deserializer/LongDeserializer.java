package com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.deserializer;

import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONLexer;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONToken;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Type;


public class LongDeserializer implements ObjectDeserializer {

    public final static LongDeserializer instance = new LongDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz) {
        return (T) deserialze(parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultExtJSONParser parser) {
        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_INT) {
            long longValue = lexer.longValue();
            lexer.nextToken(JSONToken.COMMA);
            return (T) Long.valueOf(longValue);
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        return (T) TypeUtils.castToLong(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
