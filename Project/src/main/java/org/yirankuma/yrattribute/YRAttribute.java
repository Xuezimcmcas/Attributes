package org.yirankuma.yrattribute;

import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.yirankuma.yrattribute.Attributes.Attribute;
import org.yirankuma.yrattribute.Attributes.EventAttribute;
import org.yirankuma.yrattribute.Attributes.GlobalAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import static org.apache.commons.io.FileUtils.readFileToString;

public class YRAttribute extends JavaPlugin {
    // 定义存储脚本的 Map
    private Map<String, Attribute> scriptMap = new HashMap<>();

    // 获取 scripts 文件夹路径
    private File scriptsFolder;

    private GlobalAttributes globalAttributes;

    //整个全局变量方便调插件本体
    private static YRAttribute plugin;

    public static YRAttribute getPlugin() {
        return plugin;
    }


    @Override
    public void onEnable() {
        plugin = this;

        // 初始化脚本内容
        initializeScripts();
        String scriptPath = this.getDataFolder() + File.separator + "scripts" + File.separator + "attributes.js";
        GlobalAttributes.loadAttributesFromScript(scriptPath);
        getPlugin().getLogger().info(ChatColor.GREEN + "属____性插♂件插进去了");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info(ChatColor.RED + "属____性插♂件拔出来了");
    }


    public void initializeScripts() {
        // 遍历 scripts 文件夹
        scriptsFolder = new File(this.getDataFolder().getPath() + "/scripts"); // 替换为实际的文件夹路径
        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdirs();
        }
        File[] files = scriptsFolder.listFiles();
        if (files != null) {
            for (File file : scriptsFolder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".js")) {
                    String script = null;
                    try {
                        script = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

                        ScriptEngineManager engineManager = new ScriptEngineManager();
                        ScriptEngine engine = engineManager.getEngineByName("js");
                        // 执行JavaScript脚本...
                        engine.eval(script);
                        // 从 JavaScript 引擎获取 attributeName 变量的值
                        String attributeName = (String) engine.get("attributeName");
                        String attributeType = (String) engine.get("attributeType");
                        if (attributeType.equals("EventAttribute")) {
                            // 将脚本添加到 scriptMap 中，使用 attributeName 作为键
                            EventAttribute eventAttribute = new EventAttribute(script);
                            scriptMap.put(attributeName, eventAttribute);
                            getLogger().info("[YRAttribute]事件驱动型属性" + attributeName + "加载完毕");
                        }

                    } catch (IOException | ScriptException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}