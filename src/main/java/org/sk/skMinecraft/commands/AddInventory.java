package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sk.skMinecraft.SkMinecraft;

import java.util.Arrays;
import java.util.HashMap;

public class AddInventory extends Command{

    private int playerIndex;
    private Material material;
    private int amount;

    private String name;
    private int slot;
    private boolean unbreakable;

    public AddInventory(String command) {
        String[] parts = command.split(SkMinecraft.seperator);

        if(parts.length < 4) {
            this.valid = false;
            return;
        }

        try {
            this.playerIndex = Integer.parseInt(parts[1]);
            this.amount = Integer.parseInt(parts[3]);
        } catch(NumberFormatException e){
            this.valid = false;
            return;
        }

        if(playerIndex < 0 || playerIndex >= Bukkit.getOnlinePlayers().toArray(new Player[0]).length) {
            this.valid = false;
            return;
        }

        String blockId = parts[2].toUpperCase();
        this.material = Material.matchMaterial(blockId);

        if(this.material == null) {
            this.valid = false;
            return;
        }

        ArgumentParser parser = new ArgumentParser();

        parser.addArgument("name", ArgumentParser.StringParser, "");
        parser.addArgument("slot", Integer::parseInt, -1);

        parser.addFlag("unbreakable");

        HashMap<String,ArgumentParser.ArgumentResult> result = parser.parse(Arrays.copyOfRange(parts,4, parts.length));

        this.name = result.get("name").asString();
        this.slot = result.get("slot").asInt();
        this.unbreakable = result.get("unbreakable").asBool();
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            Player target = players[playerIndex];

            Inventory inv = target.getInventory();

            ItemStack stack = new ItemStack(material, this.amount);
            ItemMeta meta = stack.getItemMeta();

            if(meta != null){
                if(!this.name.isEmpty()) {
                    meta.setDisplayName(this.name);
                }

                meta.setUnbreakable(this.unbreakable);
            }

            stack.setItemMeta(meta);

            if(this.slot != -1) {
                inv.setItem(this.slot, stack);
            } else {
                inv.addItem(stack);
            }
        });
    }
}
