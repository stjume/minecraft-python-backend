package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.PrintWriter;

public class CommandSetBlock implements Command {
    private PrintWriter writer;
    private int x,y,z;
    private String blockId;

    private boolean valid;

    public CommandSetBlock(PrintWriter writer, String command) {
        this.writer = writer;
        String[] parts = command.split(" ");

        this.valid = true;
        try {
            this.x = Integer.parseInt(parts[1]);
            this.y = Integer.parseInt(parts[2]);
            this.z = Integer.parseInt(parts[3]);
            this.blockId = parts[4].toUpperCase();


        } catch (NumberFormatException e) {
            this.valid = false;
        }
    }

    public void apply() {
        Material material = Material.matchMaterial(blockId);
        if (material == null || !material.isBlock()) {
            writer.println("error invalid_block");
            return;
        }

        Bukkit.getScheduler().runTask(this, () -> {
            World world = Bukkit.getWorlds().getFirst(); // default world
            Location loc = new Location(world, x, y, z);
            loc.getBlock().setType(material);
        });
    }

    public boolean isValid() {
        return this.valid;
    }
}
