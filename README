# Minecraft Python Backend

A simple minecraft plugin for sending commands to a server

## Protocal

- `setBlock <x> <y> <z> <blockid>` Set a block at a location
  - `<x> <y> <z>` position of the block
  - `<blockid>` the block id as a string i.e. "stone"
- `getBlock <x> <y> <z>` gets information about a block
  - `<x> <y> <z>` position of the block
- `getPlayer <index>` gets information about a player
  - `<index>` the index of the online Player
  - returns: `<index> <name> <x> <y> <z> <rotation>`
- `postChat <message>` posts a message to chat
  - `<message>` is the message which should be posted, everything after the `postChat` is interpreted as the message
  - example: `postChat Hello World my name is Adrian` will post the message "Hello World my name is Adrian" to the chat
- `spawnEntity <x> <y> <z> <entityid>`
  - `<x> <y> <z>` position
  - `<entityid>` a string with entity id full list can be cound here: [Entities](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html)
  - `returns`: the uuid with which the entity can be identified
- `chatCommand <command>` Runs chat command in console
  - `<command>` is the command, everything after the `chatCommand` is interpreted as the command. It should not contain the /
  - example: `chatCommand say Hello from plugin!` Will run the command `/say Hello from plugin!`
- `addInv <playerIndex> <materialId> ?name? ?slot? !unbreakable!`
  - `<playerIndex>` the index of the player
  - `<materialId>` the material 
  - `<amount>` The amount that should be added
  - `?name?` the custom name of the stack in the inventory
  - `?slot?` The slot in which the item should be added
  - `!unbreakable!` Controls wether or not the item is unbreakable or not
- `batch ;|;<command>;|;<command>` Runs all commands in a batch, the `;|;` acts as the seperator. Please make sure there are no spaces around the seperator except before the first one.
  - `example`: `batch ;|;addInv 0 wooden_pickaxe 1 unbreakable;|;postChat Hello World this is message` This would give the player wooden pickaxe and post the message "Hello World this is a message" to the chat  
  - Make special note of the space before the first `;|;` this is important because otherwise the `batch` command can not be recognized
  - a seperator at the end is not necessary but does not affect the outcome, the seperator after the `batch` command is necessary
### Optional Arguments

Optional arguments are arguments that are strictly necessary but offer additional options.
They always have the form `<argument>:<value>` where `<argument>` is the name of the argument defined by the command and `<value>` is the value.
Optional arguments are marked as `?arg?`
Important usage guides for optional arguments are:

- There **must not** be a space anywhere in this configuration. If the value needs to contain a space please replace it with the sequence `|&s&|`, this will be interpreted as a space 
- They must come **after** any positional arguments

### Flags

Flagas are arguments that toggle an effekt, the are markt by  `!flag!`.
