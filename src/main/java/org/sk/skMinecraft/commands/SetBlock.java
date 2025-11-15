package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.sk.skMinecraft.SkMinecraft;

public class SetBlock extends Command {
    private int x,y,z;
    private Material material;

    public SetBlock(String command) {
        String[] parts = command.split(SkMinecraft.seperator);

        this.valid = true;
        try {
            this.x = Integer.parseInt(parts[1]);
            this.y = Integer.parseInt(parts[2]);
            this.z = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            this.valid = false;
        }

        String blockId = parts[4].toUpperCase();
        this.material = Material.matchMaterial(blockId);

        if (this.material == null || !this.material.isBlock()) {
            this.valid = false;
        }
    }

    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            World world = Bukkit.getWorlds().getFirst(); // default world
            Location loc = new Location(world, x, y, z);
            loc.getBlock().setType(this.material);
        });
    }
}
