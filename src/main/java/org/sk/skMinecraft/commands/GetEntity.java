package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.sk.skMinecraft.CentralResourceHandler;

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

    public static String informationString(String uuid, Entity ent) {
        String name = ent.getCustomName();
        Location loc = ent.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        double health = 0;
        boolean ai = false;
        if(ent instanceof LivingEntity lent) {
            health = lent.getHealth();
            ai = lent.hasAI();
        }
        String type = ent.getType().toString();
        return uuid + " " + type + " " + name + " " + x + " " + y + " " + z + " " + health + " " + ai;
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Entity ent = CentralResourceHandler.getEntity(this.target);

            if (ent == null) {
                this.writer.println("");
                return;
            }

            String output = GetEntity.informationString(this.target, ent);
            this.writer.println(output);
        });
    }
}
