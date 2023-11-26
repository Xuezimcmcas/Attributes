package org.yirankuma.yrattribute.ScriptEngine;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.yirankuma.yrattribute.YRAttribute;

import javax.script.*;
import java.util.function.Function;

public class AttributeScriptEngine {

    public static ScriptEngine getEngine(String attrName, String script) {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("js");

        try {
            // 在引擎中执行脚本，并将Bindings对象传递给eval方法
            engine.eval(script);

            // 添加自定义函数
            addCustomFunctions(engine);

        } catch (ScriptException e) {
            e.printStackTrace();
            // 处理脚本执行错误，例如记录日志或发送通知
            YRAttribute.getPlugin().getLogger().warning("§a[§fYRAttribute§a]§f执行事件触发型属性§e" + attrName + "§f失败");
        }
        return engine;
    }

    private static void addCustomFunctions(ScriptEngine engine) {
        // 获取引擎的Bindings对象，用于添加自定义函数
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);

        // 快捷方法
        //是否为玩家
        Function<Object, Boolean> isPlayerFunction = obj -> obj instanceof org.bukkit.entity.Player;
        bindings.put("isPlayer", isPlayerFunction);
        //是否为怪物
        Function<Object, Boolean> isMonsterFunction = obj -> obj instanceof org.bukkit.entity.Monster;
        bindings.put("isMonster", isMonsterFunction);
    }
}