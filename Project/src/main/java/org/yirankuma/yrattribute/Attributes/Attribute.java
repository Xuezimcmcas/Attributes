package org.yirankuma.yrattribute.Attributes;

import java.util.Map;
import java.util.HashMap;

//属性类 基类
public class Attribute {
    // 属性名字
    private String attrName = "属性名称";
    // 属性类型
    private String attrType = "属性类型";
    // 记录属性变量
    private Map<String, Object> values = new HashMap<>();

    // 获取属性名字
    public String getAttrName() {
        return attrName;
    }

    //获取属性类型
    public String getAttrType() {
        return attrType;
    }

    //获取记录的变量
    public Object getValue() {
        return values.getOrDefault(getAttrName(), "未知");
    }

    //设置记录变量
    public void setValue(String name, Object value) {
        values.put(name, value);
    }
}