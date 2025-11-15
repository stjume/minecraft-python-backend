package org.sk.skMinecraft.commands;

import org.bukkit.Material;
import org.sk.skMinecraft.SkMinecraft;

public class Validator extends Command {

    private enum Type {
        MATERIAL,
        ENTITY,
    };

    private Type type;
    private String name;

    public Validator(String command) {
        String[] parts = command.split(SkMinecraft.seperator);

        if(parts.length < 3) {
            this.valid = false;
            return;
        }

        this.type = Type.valueOf(parts[1].toUpperCase());
        this.name = parts[2].toUpperCase();
    }

    @Override
    public void apply() {
        switch (this.type) {
            case MATERIAL -> {
                Material mat = Material.getMaterial(this.name);
                if(mat == null) {
                    this.writer.println("No");
                } else {
                    this.writer.println("Yes");
                }
            }
            case ENTITY -> {
                try {
                    this.writer.println("Yes");
                }catch(IllegalArgumentException e) {
                    this.writer.println("No");
                }

            }
        }
    }
}
