package com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.deserializer;


import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.JSONToken;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.parser.ParserConfig;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.util.FieldInfo;

public class DefaultFieldDeserializer extends FieldDeserializer {

    private ObjectDeserializer fieldValueDeserilizer;

    public DefaultFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultExtJSONParser parser, Object object) {
        if (fieldValueDeserilizer == null) {
            fieldValueDeserilizer = parser.getConfig().getDeserializer(fieldInfo);
        }

        Object value = fieldValueDeserilizer.deserialze(parser, getFieldType());
        setValue(object, value);
    }

    @Override
	public int getFastMatchToken() {
        if (fieldValueDeserilizer != null) {
            return fieldValueDeserilizer.getFastMatchToken();    
        }
        
        return JSONToken.LITERAL_INT;
    }
}
