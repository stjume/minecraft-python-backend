package org.sk.skMinecraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;


public class ArgumentParser {

    public static Function<String, Object> StringParser = (arg) -> arg;
    public static Function<String, Object> IntParser = Integer::parseInt;
    public static Function<String, Object> DoubleParser = Double::parseDouble;

    public static class ArgumentResult {
        Object value;

        public ArgumentResult(Object value) {
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        public <T> T get() {
            return (T)this.value;
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

        // public Object get() {return this.value;}
    }


    private record Argument(Function<String, Object> parser, Object defaultValue) {}
    public record ParseResult(ArrayList<ArgumentResult> positional, HashMap<String, ArgumentResult> optionals, boolean valid) {
        public <T> T getPositional(int index) {
            return positional.get(index).get();
        }

        public <T> T getOptional(String name) {
            return optionals.get(name).get();
        }

        public boolean isValid() {
            return valid;
        }

        public boolean getFlag(String name) {
            return optionals.get(name).asBool();
        }
        
        public boolean isSet(String name) {
            return !optionals.get(name).isNull();
        }
    } 

    private final ArrayList<Function<String, Object>> positionalArguments;
    private final HashMap<String, Argument> optionalArguments;
    private final HashSet<String> flags;
    
    public ArgumentParser(){
        this.positionalArguments = new ArrayList<>();
        this.optionalArguments = new HashMap<>();
        this.flags = new HashSet<>();
    }

    @SafeVarargs
    public final void addPositionalArguments(Function<String, Object> ...parsers) {
        for(Function<String, Object> parser : parsers) {
            this.positionalArguments.add(parser);
        }
    }

    public void addOptionalArgument(String name, Function<String, Object> parser, Object defaultValue) {
        this.optionalArguments.put(name, new Argument(parser, defaultValue));
    }

    public void addOptionalArgument(String name, Function<String, Object> parser) {
        this.addOptionalArgument(name, parser, null);
    }

    public void addFlag(String name){
        this.flags.add(name);
    }

    public ParseResult parse(String[] parts) {
        if(parts.length < positionalArguments.size()) {
            return new ParseResult(null, null, false);
        }
        
        ArrayList<ArgumentResult> positionals = new ArrayList<>();
        for(int i = 0;i < positionalArguments.size();i++) {
            try {
                String part = parts[i];
                Function<String, Object> parser = positionalArguments.get(i);
                Object parsedResult = parser.apply(part);

                positionals.add(new ArgumentResult(parsedResult));
            } catch(Exception e) {
                return new ParseResult(null, null, false);
            } 
        }
        
        String[] optionalParts = Arrays
            .stream(parts, positionalArguments.size(), parts.length)
            .toArray(String[]::new);
        
        HashMap<String, ArgumentResult> optionals = new HashMap<>();
        for (String part : optionalParts) {
            System.out.println(part);
            String[] arg = part.split(":");
            String arg_name = arg[0];

            if(this.flags.contains(arg_name)) {
                optionals.put(arg_name, new ArgumentResult(true));
                continue;
            }

            if (arg.length != 2 || !optionalArguments.containsKey(arg_name)) {
                continue;
            }

            Object value = optionalArguments.get(arg_name).defaultValue;
            try {
                value = optionalArguments.get(arg_name).parser.apply(arg[1]);
            } catch (Exception ignored) {
                return new ParseResult(null, null, false);
            }

            optionals.put(arg_name, new ArgumentResult(value));
        }

        for(String key: this.optionalArguments.keySet()) {
            if(optionals.containsKey(key)) continue;

            optionals.put(key, new ArgumentResult(this.optionalArguments.get(key).defaultValue));
        }

        for(String key : this.flags) {
            if(optionals.containsKey(key)) continue;

            optionals.put(key, new ArgumentResult(false));
        }

        return new ParseResult(positionals, optionals, true);
    }
}
