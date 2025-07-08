package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetPlayerPos extends Command {

    private int x;
    private int y;
    private int z;
    private int rot;
    private int playerIndex;

    public SetPlayerPos(String command ){
        String[] parts = command.split(" ");

        if(parts.length < 6) {
            this.valid = false;
            return;
        }

        try {
            this.playerIndex = Integer.parseInt(parts[1]);
            this.x = Integer.parseInt(parts[2]);
            this.y = Integer.parseInt(parts[3]);
            this.z = Integer.parseInt(parts[4]);
            this.rot = Integer.parseInt(parts[5]);
        } catch (Exception e) {
            this.valid = false;
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
            loc.setYaw(this.rot);
            player.teleport(loc);
        });

    }
}
