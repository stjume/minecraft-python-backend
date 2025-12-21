package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class SetPlayerPos extends Command {

    private int x;
    private int y;
    private int z;
    private World world;
    private int rot;
    private boolean setRot;
    private int playerIndex;

    public SetPlayerPos(StringCommand command ){
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.IntParser,
            ArgumentParser.IntParser,  
            ArgumentParser.IntParser, 
            ArgumentParser.IntParser,
            ArgumentParser.StringParser
        );

        parser.addOptionalArgument("rotation", Integer::parseInt);

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        this.playerIndex = result.getPositional(0);
        this.x = result.getPositional(1);
        this.y = result.getPositional(2);
        this.z = result.getPositional(3);
        String worldName = result.getPositional(4);
        this.world = Bukkit.getWorld(worldName);

        this.setRot = result.isSet("rotation");
        if(this.setRot) {
            this.rot = result.getOptional("rotation");
        }
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            if(playerIndex < 0 || playerIndex >= players.length) {
                writer.println("error invalid_index");
                return;
            }

            Player player = players[playerIndex];
            Location loc = new Location(this.world, this.x, this.y, this.z);
            if(!this.setRot) {
                loc.setYaw(player.getLocation().getYaw());
            }else {
                loc.setYaw(this.rot);
            }
            player.teleport(loc);
        });

    }
}
