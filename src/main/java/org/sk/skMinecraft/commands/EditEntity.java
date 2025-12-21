package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;
import org.sk.skMinecraft.data.Position;

public class EditEntity extends Command {
    private String target;

    private boolean setAi;
    private boolean ai;

    private boolean setPosition;
    private int x,y,z;
    private World world;

    private String name;

    private boolean setHealth;
    private double health;

    public EditEntity(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.StringParser
        );

        parser.addOptionalArgument("ai", Boolean::parseBoolean);
        parser.addOptionalArgument("position", arg -> Position.fromString(arg, ";"));
        parser.addOptionalArgument("name", ArgumentParser.StringParser);
        parser.addOptionalArgument("health", Double::parseDouble);

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        this.target = result.getPositional(0);

        this.setAi = result.isSet("ai");
        if(this.setAi) {
            this.ai = result.getFlag("ai");
        }

        this.setPosition = result.isSet("position");
        if(this.setPosition) {
            Position position = result.getOptional("position");
            this.x = position.x;
            this.y = position.y;
            this.z = position.z;
            this.world = position.world;
        }

        this.name = result.getOptional("name");

        this.setHealth = result.isSet("health");
        if(this.setHealth) {
            this.health = result.getOptional("health");
        }
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Entity ent = null;
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getUniqueId().toString().equals(this.target)) {
                        ent = entity;
                        break;
                    }
                }
                if (ent != null) break;
            }

            if(ent == null) {
                return;
            }

            if (ent instanceof LivingEntity living) {
                if(this.setAi) {
                    living.setAI(this.ai);
                }
                if(this.setHealth) {
                    living.setHealth(this.health);
                }
            }

            if(this.setPosition) {
                Location loc = ent.getLocation();
                loc.setX(this.x);
                loc.setY(this.y);
                loc.setZ(this.z);
                loc.setWorld(this.world);
                ent.teleport(loc);
            }

            if(this.name != null) {
                ent.setCustomName(this.name);
            }
        });
    }
}
