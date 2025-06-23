package org.sk.skMinecraft;

import org.sk.skMinecraft.commands.Command;
import org.sk.skMinecraft.commands.CommandGetPlayer;
import org.sk.skMinecraft.commands.CommandPostChat;
import org.sk.skMinecraft.commands.CommandSetBlock;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.function.Function;

public class CommandFactory {

    private PrintWriter writer;

    private HashMap<String, Function<String, Command>> commands;

    public CommandFactory(PrintWriter writer) {
        this.writer = writer;

        this.commands = new HashMap<>();

        this.commands.put("setBlock", CommandSetBlock::new);
        this.commands.put("getPlayer", CommandGetPlayer::new);
        this.commands.put("postChat", CommandPostChat::new);

    }

    public Command build(String command) {
        int firstSpace = command.indexOf(" ");
        String commandName = firstSpace == -1 ? command : command.substring(0, firstSpace);
        return this.commands.get(commandName).apply(command);
    }
}
