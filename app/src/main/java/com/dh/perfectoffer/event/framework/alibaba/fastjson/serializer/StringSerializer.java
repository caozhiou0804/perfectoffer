/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dh.perfectoffer.event.framework.alibaba.fastjson.serializer;

import java.io.IOException;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class StringSerializer implements ObjectSerializer {

    public static StringSerializer instance = new StringSerializer();

    public void write(JSONSerializer serializer, Object object) throws IOException {
        write(serializer, (String) object);
    }

    public void write(JSONSerializer serializer, String value) {
        SerializeWriter out = serializer.getWriter();

        if (value == null) {
            if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty)) {
                out.writeString("");
            } else {
                out.writeNull();
            }
            return;
        }

        out.writeString(value);
    }
}
