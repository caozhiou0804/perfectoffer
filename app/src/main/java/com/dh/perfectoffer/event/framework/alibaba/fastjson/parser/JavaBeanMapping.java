package com.dh.perfectoffer.event.framework.alibaba.fastjson.parser;

@Deprecated
public class JavaBeanMapping extends ParserConfig {
	private final static JavaBeanMapping instance = new JavaBeanMapping();

	public static JavaBeanMapping getGlobalInstance() {
		return instance;
	}
}
