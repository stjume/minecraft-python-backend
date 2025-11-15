package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.sk.skMinecraft.SkMinecraft;

public class SetPlayerVelocity extends Command {

    private enum Type {
        UP,
        DOWN,
        BACK,
        LOOKING
    };

    private Type type;
    private int playerIndex;
    private double strength;

    public SetPlayerVelocity(String command) {
        String[] parts = command.split(SkMinecraft.seperator);

        if(parts.length < 4) {
            this.valid = false;
            return;
        }
        try {
            this.type = Type.valueOf(parts[1]);
            this.playerIndex = Integer.parseInt(parts[2]);
            this.strength = Double.parseDouble(parts[3]);
        }catch (Exception e) {
            this.valid = false;
        }
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
                case UP -> target.setVelocity(new Vector(0, 1, 0).multiply(this.strength));
                case DOWN -> target.setVelocity(new Vector(0, -1, 0).multiply(this.strength));
                case BACK -> {
                    Vector backDir = target.getLocation().getDirection();
                    backDir.setY(0); // remove vertical component
                    if (backDir.lengthSquared() == 0) {
                        backDir = new Vector(0, 0, -1); // fallback just in case
                    }
                    target.setVelocity(backDir.normalize().multiply(-this.strength));
                }
                case LOOKING -> target.setVelocity(target.getLocation().getDirection().normalize().multiply(this.strength));
            }
        });
    }
}
