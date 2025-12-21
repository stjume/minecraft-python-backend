package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class SpawnBossBar extends Command {

    private String name;
    private String text;

    public SpawnBossBar(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.StringParser,
            ArgumentParser.StringParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        this.name = result.getPositional(0);
        this.text = result.getPositional(1);
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            NamespacedKey key = new NamespacedKey(this.plugin, name);
            KeyedBossBar bossBar = Bukkit.getBossBar(key);
            if (bossBar != null) {
                bossBar.removeAll();
            }

            bossBar = Bukkit.createBossBar(
                    key,
                    text,
                    BarColor.PURPLE,
                    BarStyle.SOLID);

            for (Player player : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(player);
            }

            bossBar.setVisible(true);
        });
    }
}
