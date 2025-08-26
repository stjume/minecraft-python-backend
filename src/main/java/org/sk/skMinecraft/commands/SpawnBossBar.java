package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;

public class SpawnBossBar extends Command {

    private String name;
    private String text;

    public SpawnBossBar(String command) {
        String[] parts = command.split(" ");

        this.name = "";
        this.text = "";
        if(parts.length < 2) {

            this.valid = false;
            return;
        }

        name = parts[1];
        text = parts[2];
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            NamespacedKey key = new NamespacedKey(this.plugin, name);
            KeyedBossBar bossBar = Bukkit.getBossBar(key);
            if(bossBar != null) {
                bossBar.removeAll();
            }

            bossBar = Bukkit.createBossBar(
                    key,
                    text,
                    BarColor.PURPLE,
                    BarStyle.SOLID
            );

            for (Player player : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(player);
            }

            bossBar.setVisible(true);
        });
    }
}
