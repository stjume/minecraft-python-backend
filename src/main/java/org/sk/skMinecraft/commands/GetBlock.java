package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class GetBlock extends Command{

    private int x,y,z;
    private World world;

    public GetBlock(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.DoubleParser,
            ArgumentParser.DoubleParser,
            ArgumentParser.DoubleParser,
            ArgumentParser.StringParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        double x = result.getPositional(0);
        double y = result.getPositional(1);
        double z = result.getPositional(2);
        
        this.x = (int)Math.floor(x);
        this.y = (int)Math.floor(y);
        this.z = (int)Math.floor(z);

        String worldName = result.getPositional(3);
        this.world = Bukkit.getWorld(worldName);
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Block block = this.world.getBlockAt(x,y,z);

            this.writer.println(block.getType().name());
        });
    }
}
