package org.yirankuma.yrattribute.Attributes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerAttributePool {
    private Player player;
    private Map<String, Attribute> attributes = new HashMap<>();

    public PlayerAttributePool(Player player) {
        this.player = player;
        initializeAttributes();
    }

    private void initializeAttributes() {
        // 检查玩家的装备和手上拿的东西
        checkAndAddAttributesFromInventory();

    }

    private void checkAndAddAttributesFromInventory() {
        // 检查手持物品
        checkAndAddAttributeFromItem(player.getInventory().getItemInMainHand());
        checkAndAddAttributeFromItem(player.getInventory().getItemInOffHand());

        // 检查装备
        for (ItemStack item : player.getInventory().getArmorContents()) {
            checkAndAddAttributeFromItem(item);
        }
    }

    private void checkAndAddAttributeFromItem(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lores = item.getItemMeta().getLore();
            for (String lore : lores) {
                Attribute attribute = GlobalAttributes.getAttribute(lore);
                if (attribute != null) {
                    attributes.put(lore, attribute);
                }
            }
        }
    }
//下面的方法可根据实际用途考量，我给你写好框架，需要用就用:(
    // 获取特定属性值
    public Object getAttributeValue(String attributeName) {
        Attribute attribute = attributes.get(attributeName);
        return attribute != null ? attribute.getValue() : null;
    }

    // 添加或更新属性
    public void addOrUpdateAttribute(String attributeName, Attribute attribute) {
        attributes.put(attributeName, attribute);
    }

    // 移除属性
    public void removeAttribute(String attributeName) {
        attributes.remove(attributeName);
    }

    // 检查玩家是否有特定属性
    public boolean hasAttribute(String attributeName) {
        return attributes.containsKey(attributeName);
    }
}