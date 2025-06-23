package org.sk.skMinecraft;

import org.sk.skMinecraft.commands.Command;
import org.sk.skMinecraft.commands.CommandGetPlayer;
import org.sk.skMinecraft.commands.CommandSetBlock;

import java.io.PrintWriter;

public class CommandFactory {

    private PrintWriter writer;

    public CommandFactory(PrintWriter writer) {
        this.writer = writer;
    }

    public Command build(String command) {
        Command result = null;
        if (command.startsWith("setBlock")) {
            result = new CommandSetBlock(this.writer, command);
        } else if (command.startsWith("getPlayer")) {
            result = new CommandGetPlayer(this.writer, command);
        } else if(command.startsWith("chat")) {

        }

        return result;
    }
}
