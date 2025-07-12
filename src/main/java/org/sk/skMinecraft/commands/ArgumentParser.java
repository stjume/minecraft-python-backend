package org.sk.skMinecraft.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;


public class ArgumentParser {

    public static Function<String, Object> StringParser = (arg) -> arg;

    public static String spaceEscape = "|&s&|";

    public static class ArgumentResult {
        Object value;

        public ArgumentResult(Object value) {
            this.value = value;
        }

        public int asInt() {
            return (int)value;
        }

        public String asString() {
            return (String)value;
        }

        public boolean asBool() {
            return (boolean)value;
        }

        public boolean isNull() {
            return value == null;
        }

        public float asFloat() {
            return (Float)value;
        }

        public Object get() {return this.value;}
    }

    private record argument(Function<String, Object> parser, Object defaultValue) {}
    private final HashMap<String, argument> arguments;
    private final HashSet<String> flags;

    public ArgumentParser(){
        this.arguments = new HashMap<>();
        this.flags = new HashSet<>();
    }

    public void addArgument(String name, Function<String, Object> parser, Object defaultValue) {
        this.arguments.put(name, new argument(parser, defaultValue));
    }

    public void addArgument(String name, Function<String, Object> parser) {
        this.addArgument(name, parser, null);
    }

    public void addFlag(String name){
        this.flags.add(name);
    }

    public HashMap<String, ArgumentResult> parse(String[] parts) {
        HashMap<String, ArgumentResult> result = new HashMap<>();

        for (String part : parts) {
            System.out.println(part);
            String[] arg = part.split(":");
            String arg_name = arg[0];

            if(this.flags.contains(arg_name)) {
                result.put(arg_name, new ArgumentResult(true));
                continue;
            }

            arg[1] = arg[1].replace(spaceEscape, " ");

            if (arg.length != 2 || !arguments.containsKey(arg_name)) {
                continue;
            }

            Object value = arguments.get(arg_name).defaultValue;
            try {
                value = arguments.get(arg_name).parser.apply(arg[1]);
            } catch (Exception ignored) {}

            result.put(arg_name, new ArgumentResult(value));
        }

        for(String key: this.arguments.keySet()) {
            if(result.containsKey(key)) continue;

            result.put(key, new ArgumentResult(this.arguments.get(key).defaultValue));
        }

        for(String key : this.flags) {
            if(result.containsKey(key)) continue;

            result.put(key, new ArgumentResult(false));
        }

        return result;
    }
}
