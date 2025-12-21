package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.KeyedBossBar;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class DeleteBossBar extends Command {

    private String name;

    public DeleteBossBar(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.StringParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        this.name = result.getPositional(0);
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
