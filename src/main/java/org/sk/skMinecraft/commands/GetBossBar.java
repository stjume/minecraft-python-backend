package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.KeyedBossBar;
import org.sk.skMinecraft.SkMinecraft;

public class GetBossBar extends Command {

    String name;

    public GetBossBar(String command) {
        String[] parts = command.split(SkMinecraft.seperator);
        if(parts.length != 2) {
            this.valid = false;
            return;
        }

        this.name = parts[1];
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            NamespacedKey key = new NamespacedKey(this.plugin, name);

            KeyedBossBar bossbar = Bukkit.getBossBar(key);
            if(bossbar == null) {
                this.writer.println("");
                return;
            }

            
        });
    }
    
    
}
