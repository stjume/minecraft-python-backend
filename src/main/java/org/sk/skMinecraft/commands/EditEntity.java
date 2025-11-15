package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EditEntity extends Command {

    private String target;

    private boolean setAi;
    private boolean ai;

    private boolean setPosition;
    private int x,y,z;

    private String name;

    private boolean setHealth;
    private double health;

    public EditEntity(String command) {
        String[] parts = command.split(" ");

        if(parts.length < 2) {
            this.valid = false;
            return;
        }

        this.target = parts[1];

        ArgumentParser parser = new ArgumentParser();

        parser.addArgument("ai", Boolean::parseBoolean);
        parser.addArgument("position", arg-> Arrays.stream(arg.split(";")).map(Integer::parseInt).collect(Collectors.toList()));
        parser.addArgument("name", ArgumentParser.StringParser);
        parser.addArgument("health", Double::parseDouble);

        HashMap<String,ArgumentParser.ArgumentResult> result = parser.parse(Arrays.copyOfRange(parts,2, parts.length));

        this.setAi = !result.get("ai").isNull();
        if(this.setAi) {
            this.ai = result.get("ai").asBool();
        }

        this.setPosition = !result.get("position").isNull();
        if(this.setPosition) {
            @SuppressWarnings("unchecked")
            List<Integer> positions = (List<Integer>)result.get("position").get();;
            this.x = positions.get(0);
            this.y = positions.get(1);
            this.z = positions.get(2);
        }

        this.name = result.get("name").asString();

        this.setHealth = !result.get("health").isNull();
        if(this.setHealth) {
            this.health = result.get("health").asInt();
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
                ent.teleport(loc);
            }

            if(this.name != null) {
                ent.setCustomName(this.name);
            }
        });
    }
}
