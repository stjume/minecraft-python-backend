package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class GetInventory extends Command {

    private int playerIndex;

    public GetInventory(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.IntParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        this.playerIndex = result.getPositional(0);
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

                ItemMeta meta = stack.getItemMeta();

                if(i != 0) {
                    result.append(SkMinecraft.seperator);
                }

                assert meta != null;
                result.append(i).append(":");
                result.append(stack.getType().name());
                result.append(";").append(meta.getDisplayName());
                result.append(":").append(stack.getAmount());
                
            }

            this.writer.println(result.toString().trim());
        });
    }
}
