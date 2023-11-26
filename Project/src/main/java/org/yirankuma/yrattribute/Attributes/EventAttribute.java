package org.yirankuma.yrattribute.Attributes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.yirankuma.yrattribute.ScriptEngine.AttributeScriptEngine;
import org.yirankuma.yrattribute.YRAttribute;

import javax.script.*;
import java.util.Arrays;

public class EventAttribute extends Attribute implements Listener {
    private String script;
    private String eventName;
    private ScriptEngine engine;

    public EventAttribute(String script) {
        this.script = script;
        this.engine = AttributeScriptEngine.getEngine(getAttrName(), script);
        try {
            // 执行脚本以初始化上下文
            engine.eval(script);
        } catch (ScriptException e) {
            // 处理脚本执行错误，例如记录日志或发送通知
            YRAttribute.getPlugin().getLogger().warning("[YRAttribute]执行事件触发型属性" + getAttrName() + "的初始化脚本失败");
        }

        Object eventNameObj = engine.get("eventName");
        if (eventNameObj instanceof String) {
            this.eventName = (String) eventNameObj;
        } else {
            this.eventName = "";
            // 处理变量类型不匹配的情况，例如记录日志或发送通知
            YRAttribute.getPlugin().getLogger().warning("[YRAttribute]事件触发型属性" + getAttrName() + "中的eventName变量类型不是String");
        }

        registerEvent();
    }

    private void registerEvent() {
        Plugin plugin = YRAttribute.getPlugin();
        if (plugin != null) {
            PluginManager pluginManager = plugin.getServer().getPluginManager();
            Class<?> eventClass = null;
            try {
                eventClass = Class.forName(eventName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            pluginManager.registerEvent((Class<? extends Event>) eventClass, this, EventPriority.NORMAL, (l, event) -> {
                try {
                    engine.put("event", event);
                    // 在引擎中执行事件处理代码
                    engine.eval("onEvent(event);");
                } catch (ScriptException e) {
                    e.printStackTrace();
                    // 处理脚本执行错误，例如记录日志或发送通知
                    YRAttribute.getPlugin().getLogger().warning("[YRAttribute]执行事件触发型属性" + getAttrName() + "的事件处理代码失败");
                }
            }, plugin);
        } else {
            YRAttribute.getPlugin().getLogger().warning("[YRAttribute]无法注册事件触发型属性" + getAttrName() + "，插件未初始化");
        }
    }
}