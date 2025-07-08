package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;

import java.util.Arrays;
import java.util.HashMap;

public class EditBossBar extends Command {

    private enum EditType {
        TEXT,
        COLOR,
        VALUE,
        STYLE,
        UNKOWN
    };

    private EditType typeFromText(String command) {
        if(command.equals("text")) {
            return EditType.TEXT;
        }
        if(command.equals("color")) {
            return EditType.COLOR;
        }
        if(command.equals("value")) {
            return EditType.VALUE;
        }
        if(command.equals("style")) {
            return EditType.STYLE;
        }
        return EditType.UNKOWN;
    }

    EditType type;
    String name;
    String text;
    BarColor color;
    float value;
    BarStyle style;

    public EditBossBar(String command) {
        String[] parts = command.split(" ");

        if(parts.length < 3) {
            this.valid = false;
            return;
        }

        type = typeFromText(parts[1]);
        this.name = parts[2];

        ArgumentParser parser = new ArgumentParser();

        parser.addArgument("text", ArgumentParser.StringParser, "");
        parser.addArgument("color", ArgumentParser.StringParser, "PURPLE");
        parser.addArgument("value", Float::parseFloat, 0.0f);
        parser.addArgument("style", ArgumentParser.StringParser, "SOLID");

        HashMap<String,ArgumentParser.ArgumentResult> result = parser.parse(Arrays.copyOfRange(parts,3, parts.length));

        this.text = result.get("text").asString();
        this.color = BarColor.valueOf(result.get("color").asString().toUpperCase());
        this.value = result.get("value").asFloat();
        this.style = BarStyle.valueOf(result.get("style").asString().toUpperCase());
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            NamespacedKey key = new NamespacedKey(this.plugin, name);

            KeyedBossBar bossbar = Bukkit.getBossBar(key);
            if(bossbar == null) return;
            switch (this.type) {
                case EditType.TEXT -> bossbar.setTitle(this.text);
                case EditType.COLOR -> bossbar.setColor(this.color);
                case EditType.VALUE -> bossbar.setProgress(this.value);
                case EditType.STYLE -> bossbar.setStyle(this.style);
            }

        });
    }
}
