package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;


public class AddInventory extends Command{

    private int playerIndex;
    private Material material;
    private int amount;

    private String name;
    private int slot;
    private boolean unbreakable;

    public AddInventory(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.IntParser,
            ArgumentParser.StringParser,
            ArgumentParser.IntParser
        );

        parser.addOptionalArgument("name", ArgumentParser.StringParser, "");
        parser.addOptionalArgument("slot", Integer::parseInt, -1);

        parser.addFlag("unbreakable");

        ParseResult result = parser.parse(command.arguments());

        if(!this.isValid()) {
            this.valid = false;
            return;
        }

        this.playerIndex = result.getPositional(0);

        String blockId = result.getPositional(1);
        this.material = Material.matchMaterial(blockId.toUpperCase());

        this.amount = result.getPositional(2);

        this.name = result.getOptional("name");
        this.slot = result.getOptional("slot");
        this.unbreakable = result.getOptional("unbreakable");
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
