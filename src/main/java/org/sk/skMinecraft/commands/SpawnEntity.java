package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.sk.skMinecraft.CentralResourceHandler;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class SpawnEntity extends Command{

    private int x,y,z;
    private EntityType type;

    public SpawnEntity(StringCommand command) {
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
        this.y = result.getPositional(2);
        this.z = result.getPositional(2);

        String typeName = result.getPositional(3);
        this.type = EntityType.valueOf(typeName.toUpperCase());
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
