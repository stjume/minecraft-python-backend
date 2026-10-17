package org.sk.skMinecraft.commands;

import java.rmi.server.UID;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class GetPlayer extends Command {

    private int playerIndex;

    public GetPlayer(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
                ArgumentParser.IntParser);

        ParseResult arguments = parser.parse(command.arguments());

        if (!arguments.isValid()) {
            this.valid = false;
            return;
        }

        this.playerIndex = arguments.getPositional(0);
    }

    public static String convertPlayerToString(Player target, int playerIndex) {
        // String name = target.getDisplayName();
        String name = target.getName();
        String looking_at = "AIR";
        int block_x = 0, block_y = 0, block_z = 0;
        UUID looking_at_entity_id = UUID.randomUUID();
        try {
            Block looking_at_block = target.getTargetBlock(null, 200);
            looking_at = looking_at_block.getType().name();
            block_x = (int) looking_at_block.getLocation().getX();
            block_y = (int) looking_at_block.getLocation().getY();
            block_z = (int) looking_at_block.getLocation().getZ();

            Location eye = target.getEyeLocation();
            RayTraceResult result = target.getWorld().rayTraceEntities(eye, eye.getDirection(), 200,
                    e -> !e.equals(target));
            if (result != null) {
                looking_at_entity_id = result.getHitEntity().getUniqueId();
            }

        } catch (Exception e) {
            System.out.println("THere was an expection:");
            System.out.println(e.getMessage());
        }

        Location loc = target.getLocation();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        String world_name = loc.getWorld().getName();
        int rotation = (int) loc.getYaw();
        boolean sneak = target.isSneaking();
        double maxhealth = target.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
        double health = target.getHealth();
        double hunger = target.getFoodLevel();
        double saturation = target.getSaturation();
        double xp_level = target.getLevel();
        double xp_progress = target.getExp();
        PlayerInventory inventory = target.getInventory();

        int heldIndex = inventory.getHeldItemSlot();
        String heldItemString = Integer.toString(heldIndex) + ":;:";
        heldItemBlock: {
            ItemStack heldItemStack = inventory.getItem(heldIndex);
            if (heldItemStack == null)
                break heldItemBlock;

            ItemMeta meta = heldItemStack.getItemMeta();
            heldItemString = heldIndex + ":" + heldItemStack.getType().name() + ";" + meta.getDisplayName() + ":"
                    + heldItemStack.getAmount();
        }

        String looking_at_block_output = looking_at + ":" + block_x + ":" + block_y + ":" + block_z;
        String lookint_at_entity_output = GetEntity.informationString(SkMinecraft.secondLevelSeperator, looking_at_entity_id, Bukkit.getEntity(looking_at_entity_id));

        return SkMinecraft.joinWithSeperator(
                playerIndex,
                name,
                x,
                y,
                z,
                world_name,
                rotation,
                looking_at_block_output,
                lookint_at_entity_output,
                sneak,
                maxhealth,
                health,
                hunger,
                saturation,
                xp_level,
                xp_progress,
                heldItemString);
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            if (playerIndex < 0 || playerIndex >= players.length) {
                writer.println("error invalid_index");
                return;
            }

            Player target = players[playerIndex];

            String result = GetPlayer.convertPlayerToString(target, playerIndex);

            writer.println(result);
        });
    }
}
