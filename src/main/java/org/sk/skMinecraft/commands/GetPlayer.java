package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class GetPlayer extends Command {

    private int playerIndex;

    public GetPlayer(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.IntParser
        );

        ParseResult arguments = parser.parse(command.arguments());

        if(!arguments.isValid()) {
            this.valid = false;
            return;
        }

        this.playerIndex = arguments.getPositional(0);
    }

    @Override
    public void apply() {
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        if(playerIndex < 0 || playerIndex >= players.length) {
            writer.println("error invalid_index");
            return;
        }

        Player target = players[playerIndex];
        // String name = target.getDisplayName();
        String name = target.getName();
        String looking_at = "AIR";
        try {
            looking_at = target.getTargetBlock(null, 100).getType().name();
        }catch(Exception e){}
        
        Location loc = target.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        String world_name = SkMinecraft.convertWorldName(loc.getWorld().getName());
        int rotation = (int)loc.getYaw();
        boolean sneak = target.isSneaking();
        double maxhealth = target.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
        double health = target.getHealth();
        double hunger = target.getFoodLevel();
        double saturation = target.getSaturation();
        double xp_level = target.getLevel();
        double xp_progress = target.getExp();

        String result = SkMinecraft.joinWithSeperator(
            playerIndex,
            name,
            x,
            y,
            z,
            world_name,
            rotation,
            looking_at,
            sneak,
            maxhealth,
            health,
            hunger,
            saturation,
            xp_level,
            xp_progress
        );

        writer.println(result);
    }
}
