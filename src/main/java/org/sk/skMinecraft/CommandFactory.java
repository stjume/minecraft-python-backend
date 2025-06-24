package org.sk.skMinecraft;

import org.sk.skMinecraft.commands.*;

import java.util.HashMap;
import java.util.function.Function;

public class CommandFactory {

    private final HashMap<String, Function<String, Command>> commands;

    public CommandFactory() {
        this.commands = new HashMap<>();

        this.commands.put("setBlock", SetBlock::new);
        this.commands.put("getPlayer", GetPlayer::new);
        this.commands.put("postChat", PostChat::new);
        this.commands.put("spawnEntity", SpawnEntity::new);

        this.commands.put("addInv", AddInventory::new);

    }

    public Command build(String command) {
        int firstSpace = command.indexOf(" ");
        String commandName = firstSpace == -1 ? command : command.substring(0, firstSpace);
        return this.commands.get(commandName).apply(command);
    }
}
