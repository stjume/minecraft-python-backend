package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.sk.skMinecraft.CentralResourceHandler;
import org.sk.skMinecraft.SkMinecraft;

public class SpawnEntity extends Command{

    private int x,y,z;
    private EntityType type;

    public SpawnEntity(String command) {
        String[] parts = command.split(SkMinecraft.seperator);

        if(parts.length != 5) {
            this.valid = false;
            return;
        }

        try {
            this.x = Integer.parseInt(parts[1]);
            this.y = Integer.parseInt(parts[2]);
            this.z = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            this.valid = false;
        }

        this.type = EntityType.valueOf(parts[4].toUpperCase());
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            World world = Bukkit.getWorlds().getFirst(); // default world
            Location loc = new Location(world, x, y, z);
            Entity entity = world.spawnEntity(loc, this.type);
            CentralResourceHandler.addEntity(entity.getUniqueId().toString(), entity);

            String uuid = entity.getUniqueId().toString();
            String output = GetEntity.informationString(uuid, entity);
            writer.println(output);
        });
    }
}
