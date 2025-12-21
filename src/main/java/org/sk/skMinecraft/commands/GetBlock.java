package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class GetBlock extends Command{

    private int x,y,z;

    public GetBlock(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.IntParser,
            ArgumentParser.IntParser,
            ArgumentParser.IntParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        this.x = result.getPositional(0);
        this.y = result.getPositional(1);
        this.z = result.getPositional(2);
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
