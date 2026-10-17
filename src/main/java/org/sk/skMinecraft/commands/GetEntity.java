package org.sk.skMinecraft.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class GetEntity extends Command {

    private UUID target;

    public GetEntity(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.StringParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }
        
        this.target = UUID.fromString(result.getPositional(0));
    }

    public static String informationString(String sep, UUID uuid, Entity ent) {
        if(ent == null) {
            return SkMinecraft.joinWithSeperatorArg(sep,"", "", "", "", "", "", "", "", "");    
        }

        String name = ent.getCustomName();
        Location loc = ent.getLocation();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        String world = loc.getWorld().getName();
        double health = 0;
        boolean ai = false;
        if(ent instanceof LivingEntity lent) {
            health = lent.getHealth();
            ai = lent.hasAI();
        }
        String type = ent.getType().toString();

        String result = SkMinecraft.joinWithSeperatorArg(sep, uuid, type, name, x, y, z, world, health, ai);    
        return result;
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Entity ent = Bukkit.getEntity(this.target);

            if (ent == null) {
                this.writer.println("");
                return;
            }

            String output = GetEntity.informationString(SkMinecraft.seperator, this.target, ent);
            this.writer.println(output);
        });
    }
}
