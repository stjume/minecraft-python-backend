package org.sk.skMinecraft;

import org.sk.skMinecraft.commands.*;

import java.util.HashMap;
import java.util.function.Function;

public class CommandFactory {

    private final HashMap<String, Function<String, Command>> commands;

    public CommandFactory() {
        this.commands = new HashMap<>();

        this.commands.put("setBlock", SetBlock::new);
        this.commands.put("getBlock", GetBlock::new);

        this.commands.put("getPlayer", GetPlayer::new);
        this.commands.put("setPlayerStat", SetPlayerStat::new);
        this.commands.put("setPlayerVelocity", SetPlayerVelocity::new);

        this.commands.put("postChat", PostChat::new);
        this.commands.put("chatCommand", ChatCommand::new);

        this.commands.put("spawnEntity", SpawnEntity::new);
        this.commands.put("editEntity", EditEntity::new);
        this.commands.put("getEntity", GetEntity::new);

        this.commands.put("addInv", AddInventory::new);
        this.commands.put("getInv", GetInventory::new);

        this.commands.put("batch", Batching::new);

        this.commands.put("spawnBossBar", SpawnBossBar::new);
        this.commands.put("editBossBar", EditBossBar::new);
        this.commands.put("deleteBossBar", DeleteBossBar::new);

        this.commands.put("pollChat", PollChat::new);

        this.commands.put("setPlayerPos", SetPlayerPos::new);

        this.commands.put("validate", Validator::new);
    }

    public Command build(String command) {
        int firstSpace = command.indexOf(" ");
        String commandName = firstSpace == -1 ? command : command.substring(0, firstSpace);
        return this.commands.get(commandName).apply(command);
    }
}
