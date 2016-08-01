package com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.deserializer;

import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSONException;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONScanner;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONToken;

import java.lang.reflect.Type;


public class DateDeserializer implements ObjectDeserializer {

    public final static DateDeserializer instance = new DateDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz) {
        Object val = parser.parse();

        if (val == null) {
            return null;
        }

        if (val instanceof java.util.Date) {
            return (T) val;
        } else if (val instanceof Number) {
            return (T) new java.util.Date(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            
            JSONScanner dateLexer = new JSONScanner(strVal);
            if (dateLexer.scanISO8601DateIfMatch()) {
                return (T) dateLexer.getCalendar().getTime();
            }

            long longVal = Long.parseLong(strVal);
            return (T) new java.util.Date(longVal);
        }
        
        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
