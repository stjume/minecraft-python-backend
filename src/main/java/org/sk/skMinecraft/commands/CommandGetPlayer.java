package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.PrintWriter;

public class CommandGetPlayer implements Command{

    private PrintWriter writer;

    private boolean valid;
    private int playerIndex;

    public CommandGetPlayer(PrintWriter writer, String command) {
        this.writer = writer;
        String[] parts = command.split(" ");

        if(parts.length != 2) {
            this.valid = false;
            return;
        }

        try {
            this.playerIndex = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            this.valid = false;
        }
    }

    @Override
    public boolean isValid() {
        return this.valid;
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
        Location loc = target.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        int rotation = (int)loc.getYaw();
        writer.println(playerIndex + " " + name + " " + x + " " + y + " " + z + " " + rotation);
    }
}
