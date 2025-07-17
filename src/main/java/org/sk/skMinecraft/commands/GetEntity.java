package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.ExecutionException;

public class GetEntity extends Command {

    private String target;

    public GetEntity(String command) {
        String[] parts = command.split(" ");

        if(parts.length < 2) {
            this.valid = false;
            return;
        }

        this.target = parts[1];
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

            if (ent == null) {
                return;
            }

            String name = ent.getCustomName();
            Location loc = ent.getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            double health = 0;
            if(ent instanceof LivingEntity lent) {
                health = lent.getHealth();
            }
            this.writer.println(this.target + " " + name + " " + x+ " " + y + " " + z + " " + health);
        });
    }
}
