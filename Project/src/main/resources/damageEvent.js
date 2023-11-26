var attributeName = "伤害事件";
var attributeType = "EventAttribute";
var eventName = "org.bukkit.event.entity.EntityDamageEvent";

function onEvent(event){
    //获取攻击者（和EntityDamageByEntityEvent里参数获取方法一致
    var victim = event.getEntity()
    print(victim.getType())
    if (event instanceof org.bukkit.event.entity.EntityDamageByEntityEvent) {
        var damager = event.getDamager()
        if (isPlayer(damager)) {
            // 如果是玩家类型，进行相应的操作
            // 例如：发送消息给玩家
            damager.sendMessage("你是玩家类型！");
            //设置伤害（这边设置前可以计算各种公式参数什么的
            event.setDamage(114514)
        } else if(isMonster(damager)) {
            // 如果是怪物类型
            event.setCancelled(true)

        }
    }
}