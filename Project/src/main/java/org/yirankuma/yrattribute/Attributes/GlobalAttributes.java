package org.yirankuma.yrattribute.Attributes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GlobalAttributes {
    private static final Map<String, Attribute> attributes = new HashMap<>();

    public static void registerAttribute(String name, Attribute attribute) {
        attributes.put(name, attribute);
    }

    public static Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    // 加从脚本或文件加载属性的方法
    public static void loadAttributesFromScript(String scriptPath) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        try {
            // 执行jio本
            engine.eval(new FileReader(scriptPath));

            Object result = engine.eval("getAttributes()");

            if (result instanceof Map<?, ?>) {
                Map<?, ?> attributesMap = (Map<?, ?>) result;
                for (Map.Entry<?, ?> entry : attributesMap.entrySet()) {
                    if (entry.getKey() instanceof String && entry.getValue() instanceof Attribute) {
                        registerAttribute((String) entry.getKey(), (Attribute) entry.getValue());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "无法找到属性脚本文件: " + scriptPath, e);
        } catch (ScriptException e) {
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "脚本执行错误: " + scriptPath, e);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "加载属性脚本时发生未知错误: " + scriptPath, e);
        }
    }
}