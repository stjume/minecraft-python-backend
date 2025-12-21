package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class SetBlock extends Command {
    private int x,y,z;
    private World world;
    private Material material;

    public SetBlock(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.IntParser,
            ArgumentParser.IntParser,
            ArgumentParser.IntParser,
            ArgumentParser.StringParser,
            ArgumentParser.StringParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        this.x = result.getPositional(0);
        this.y = result.getPositional(1);
        this.z = result.getPositional(2);

        String worldName = result.getPositional(3);
        this.world = Bukkit.getWorld(worldName);

        String blockId = result.getPositional(4);
        this.material = Material.matchMaterial(blockId);

        if (this.material == null || !this.material.isBlock()) {
            this.valid = false;
            return;
        }
    }

    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Location loc = new Location(this.world, x, y, z);
            loc.getBlock().setType(this.material);
        });
    }
}
