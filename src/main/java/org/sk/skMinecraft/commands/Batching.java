package org.sk.skMinecraft.commands;

import org.sk.skMinecraft.CommandFactory;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Batching extends Command {

    private final String[] commands;

    public Batching(String command) {
        String[] parts = command.split(Pattern.quote(";|;"));

        this.commands = Arrays.copyOfRange(parts, 1, parts.length);
    }

    @Override
    public void apply() {
        CommandFactory factory = new CommandFactory();

        for(String command_str : commands) {
            Command command = factory.build(command_str);

            if(!command.isValid()) continue;

            command.setParameters(this.writer, this.plugin);

            command.apply();
        }
    }
}
