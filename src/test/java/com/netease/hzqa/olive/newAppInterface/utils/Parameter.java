package com.netease.hzqa.olive.newAppInterface.utils;
/**
 * 
 * @Description 
 * @author hzzhengyinyan
 * @date 2015年7月23日 下午2:06:10
 */
public class Parameter {
	private String name;
	private Object value;

	public Parameter(String name, Object value){
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
