package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft;

import java.util.Arrays;
import java.util.HashMap;

public class SetPlayerPos extends Command {

    private int x;
    private int y;
    private int z;
    private int rot;
    private boolean setRot;
    private int playerIndex;

    public SetPlayerPos(String command ){
        String[] parts = command.split(SkMinecraft.seperator);

        if(parts.length < 5) {
            this.valid = false;
            return;
        }

        try {
            this.playerIndex = Integer.parseInt(parts[1]);
            this.x = Integer.parseInt(parts[2]);
            this.y = Integer.parseInt(parts[3]);
            this.z = Integer.parseInt(parts[4]);
        } catch (Exception e) {
            this.valid = false;
        }

        ArgumentParser parser = new ArgumentParser();

        parser.addArgument("rot", Integer::parseInt);

        HashMap<String,ArgumentParser.ArgumentResult> result = parser.parse(Arrays.copyOfRange(parts,5, parts.length));

        this.setRot = !result.get("rot").isNull();
        if(this.setRot) {
            this.rot = result.get("rot").asInt();
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
            Location loc = new Location(player.getWorld(), this.x, this.y, this.z);
            if(!this.setRot) {
                loc.setYaw(player.getLocation().getYaw());
            }else {
                loc.setYaw(this.rot);
            }
            player.teleport(loc);
        });

    }
}
