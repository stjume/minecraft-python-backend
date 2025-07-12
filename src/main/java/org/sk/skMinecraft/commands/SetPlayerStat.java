package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class SetPlayerStat extends Command {

    private enum Type {
        MAX_HEALTH,
        HEALTH,
        FOOD_LEVEL,
        SATURATION
    }

    private Type type;
    private int playerIndex;
    private double value;

    public SetPlayerStat(String command){
        String[] parts = command.split(" ");

        if(parts.length < 4) {
            this.valid = false;
            return;
        }

        this.type = Type.valueOf(parts[1].toUpperCase());
        try {
            this.playerIndex = Integer.parseInt(parts[2]);
            this.value = Double.parseDouble(parts[3]);
        }catch (Exception ignored) {
            this.valid = false;
        }
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            if(playerIndex < 0 || playerIndex >= players.length) {
                writer.println("error invalid_index");
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

            }
        });
    }
}
