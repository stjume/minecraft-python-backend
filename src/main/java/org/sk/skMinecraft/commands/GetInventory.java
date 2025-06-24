package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class GetInventory extends Command {

    private int playerIndex;

    public GetInventory(String command) {
        String[] parts = command.split(" ");

        if(parts.length < 2) {
            this.valid = false;
            return;
        }

        try {
            this.playerIndex = Integer.parseInt(parts[1]);
        } catch (Exception ignored) {}
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            if(playerIndex < 0 || playerIndex >= players.length) {
                writer.println("error invalid_index");
                return;
            }

            Player target = players[playerIndex];
            Inventory inv = target.getInventory();

            StringBuilder result = new StringBuilder();

            for(int i = 0;i < inv.getSize();i++) {
                ItemStack stack = inv.getItem(i);
                if(stack == null) continue;

                result.append(i).append(":").append(stack.getType().name()).append(":").append(stack.getAmount());

                result.append(" ");
            }

            this.writer.println(result.toString().trim());
        });
    }
}
