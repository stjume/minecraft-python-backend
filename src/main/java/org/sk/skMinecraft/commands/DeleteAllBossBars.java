package org.sk.skMinecraft.commands;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.boss.KeyedBossBar;
import org.sk.skMinecraft.SkMinecraft.StringCommand;

public class DeleteAllBossBars extends Command {

    public DeleteAllBossBars(StringCommand command) {}

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Iterator<KeyedBossBar> bossBars =Bukkit.getBossBars();

            while(bossBars.hasNext()) {
                KeyedBossBar bossBar = bossBars.next();

                bossBar.removeAll();
            }
        });
    }
    
}
