package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

public class GetBlock extends Command{

    private int x,y,z;

    public GetBlock(String command) {
        String[] parts = command.split(" ");
        try {
            this.x = Integer.parseInt(parts[1]);
            this.y = Integer.parseInt(parts[2]);
            this.z = Integer.parseInt(parts[3]);
        } catch(Exception ignored){}
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            World world = Bukkit.getWorlds().getFirst();
            Block block = world.getBlockAt(x,y,z);

            this.writer.println(block.getType().name());
        });
    }
}
