package org.sk.skMinecraft.commands;

import org.sk.skMinecraft.CommandFactory;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Batching extends Command {

    private final String[] commands;

    public Batching(StringCommand command) {
        String[] parts = command.arguments()[0].split(Pattern.quote(";|;"));

        this.commands = Arrays.copyOfRange(parts, 1, parts.length);
    }

    @Override
    public void apply() {
        CommandFactory factory = new CommandFactory();

        for(String commandStr : commands) {
            StringCommand commandObj = SkMinecraft.splitCommand(commandStr);
            Command command = factory.build(commandObj);

            if(!command.isValid()) continue;

            command.setParameters(this.writer, this.plugin);

            command.apply();
        }
    }
}
