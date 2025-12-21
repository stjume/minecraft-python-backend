package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

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
    String typeName;
    String name;
    String text;
    BarColor color;
    float value;
    BarStyle style;

    public EditBossBar(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.StringParser,
            ArgumentParser.StringParser
        );
            
        parser.addOptionalArgument("text", ArgumentParser.StringParser, "");
        parser.addOptionalArgument("color", ArgumentParser.StringParser, "PURPLE");
        parser.addOptionalArgument("value", Float::parseFloat, 0.0f);
        parser.addOptionalArgument("style", ArgumentParser.StringParser, "SOLID");

        ParseResult result = parser.parse(command.arguments());

        this.typeName = result.getPositional(0);
        this.type = typeFromText(this.typeName);
        this.name = result.getPositional(01);

        this.text = result.getOptional("text");

        String colorName = result.getOptional("color");
        this.color = BarColor.valueOf(colorName.toUpperCase());

        this.value = result.getOptional("value");

        String styleName = result.getOptional("style");
        this.style = BarStyle.valueOf(styleName.toUpperCase());
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
                case EditType.UNKOWN -> System.out.println("BossBar: Unkown edit type" + this.typeName);
            }

        });
    }
}
