package org.sk.skMinecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.sk.skMinecraft.commands.Command;
import org.sk.skMinecraft.data.ChatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public final class SkMinecraft extends JavaPlugin implements Listener {

    private Thread tcpListenerThread;
    private ServerSocket serverSocket;

    public static String seperator = "𝇉";

    public static String joinWithSeperator(Object... args) {
        String result = "";

        for(int i = 0;i < args.length;i++){

            Object arg_i = args[i];
            if (arg_i == null){
                arg_i = "null";
            }

            result += arg_i.toString();
            if(i < args.length - 1) {
                result += SkMinecraft.seperator;
            }
        }  

        return result;
    } 

    @Override
    public void onEnable() {
        CentralResourceHandler.init();
        getServer().getPluginManager().registerEvents(this, this);
        startTcpListener();
    }

    @Override
    public void onDisable() {
        stopTcpListener();
    }

    private void startTcpListener() {
        tcpListenerThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(12345); // Use your desired port
                getLogger().info("TCP server started on port 12345");

                while (!serverSocket.isClosed()) {
                    Socket client = serverSocket.accept();
                    getLogger().info("Client connected: " + client.getRemoteSocketAddress());

                    // Handle the client in a new thread
                    new Thread(() -> handleClient(client)).start();
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    getLogger().severe("TCP server error: " + e.getMessage());
                }
            }
        });
        tcpListenerThread.start();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        ChatMessage message = new ChatMessage(event.getPlayer(), event.getMessage());
        CentralResourceHandler.addChatMessage(message);
    }

    private void stopTcpListener() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (tcpListenerThread != null && tcpListenerThread.isAlive()) {
                tcpListenerThread.interrupt();
            }
            getLogger().info("TCP server stopped.");
        } catch (IOException e) {
            getLogger().severe("Failed to close TCP server: " + e.getMessage());
        }
    }

    private void handleClient(Socket client) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
        ) {
            CommandFactory commandFactory = new CommandFactory();

            String line;
            while ((line = reader.readLine()) != null) {
                final String command = line.trim();
                if (line.isEmpty()) continue;

                getLogger().info("Received: " + command);

                Command commandObject = commandFactory.build(command);

                if(commandObject == null) continue;

                if(!commandObject.isValid()) {
                    System.out.println("Invalid");
                    continue;
                }

                commandObject.setParameters(writer, this);

                commandObject.apply();
            }

            client.close();
        } catch (IOException e) {
            getLogger().severe("TCP client error: " + e.getMessage());
        }
    }
 }
