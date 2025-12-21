package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class SetPlayerStat extends Command {

    private enum Type {
        MAX_HEALTH,
        HEALTH,
        FOOD_LEVEL,
        SATURATION,
        XP_LEVEL,
        XP_PROGRESS
    }

    private Type type;
    private int playerIndex;
    private double value;

    public SetPlayerStat(StringCommand command){
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.StringParser,
            ArgumentParser.IntParser,
            ArgumentParser.IntParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        String typeName = result.getPositional(0);
        this.type = Type.valueOf(typeName.toUpperCase());

        this.playerIndex = result.getPositional(1);
        this.value = result.getPositional(2);
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            if(playerIndex < 0 || playerIndex >= players.length) {
                return;
            }

            Player target = players[this.playerIndex];

            switch (this.type) {
                case MAX_HEALTH -> {
                    AttributeInstance maxHealthAttr = target.getAttribute(Attribute.MAX_HEALTH);
                    if (maxHealthAttr != null) {
                        maxHealthAttr.setBaseValue(this.value); // Example: double the normal max health
                    }
                }
                case HEALTH -> target.setHealth(this.value);
                case FOOD_LEVEL -> target.setFoodLevel((int)this.value);
                case SATURATION -> target.setSaturation((int)this.value);
                case XP_LEVEL -> target.setLevel((int)this.value);
                case XP_PROGRESS -> target.setExp((float)this.value);
            }
        });
    }
}
