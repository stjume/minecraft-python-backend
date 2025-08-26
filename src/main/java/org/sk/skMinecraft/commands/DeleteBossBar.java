package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.KeyedBossBar;

public class DeleteBossBar extends Command {

    private String name;

    public DeleteBossBar(String command) {
        String[] parts = command.split(" ");

        if(parts.length != 2) {
            this.valid = false;
            return;
        }

        this.name = parts[1];
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            NamespacedKey key = new NamespacedKey(this.plugin, this.name);

            KeyedBossBar bossBar = Bukkit.getBossBar(key);

            if(bossBar != null) {
               bossBar.removeAll();
            }
        });
    }
}
