package org.sk.skMinecraft.commands;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.PrintWriter;

public abstract class Command {

    protected PrintWriter writer;
    protected boolean valid;
    protected JavaPlugin plugin;

    public Command() {
        this.valid = true;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setParameters(PrintWriter writer, JavaPlugin plugin) {
        this.writer = writer;
        this.plugin = plugin;
    }

    public abstract void apply();

}
