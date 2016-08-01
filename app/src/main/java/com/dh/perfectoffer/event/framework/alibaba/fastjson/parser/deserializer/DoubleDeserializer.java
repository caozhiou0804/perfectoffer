package com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.deserializer;

import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONLexer;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONToken;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Type;


public class DoubleDeserializer implements ObjectDeserializer {

    public final static DoubleDeserializer instance = new DoubleDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz) {
        return (T) deserialze(parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultExtJSONParser parser) {
        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_INT) {
            String val = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            return (T) Double.valueOf(Double.parseDouble(val));
        }

        if (lexer.token() == JSONToken.LITERAL_FLOAT) {
            String val = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            return (T) Double.valueOf(Double.parseDouble(val));
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        return (T) TypeUtils.castToDouble(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
