package org.sk.skMinecraft.commands;

public interface Command {
    boolean isValid();
    void apply();
}
