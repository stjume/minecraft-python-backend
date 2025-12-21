# Minecraft Python Backend

A simple minecraft plugin for sending commands to a server.

`The plugin was developed with spigot version 1.21.5`.

## Building

This project can be build using maven, just run the mvn command in the directory and it will build the `target/sk-mincraft-1.0.jar`:

```bash
mvn
```

## Usage

- This is a plugin intended to be used with a spigot server, just put the jar into the plugin folder.
- Then you can connect to the plugin via tcp on port `12345`.
- Send each command as a message over tcp, the message has to end with a newline.
- Each command is a commandname followed by a number of arguments. For explanation of the argument types see: [Arguments](#argument-types)

## Protocol Glossary

### General Notes

- The symbols around arguments: `<>` `??` `!!` indicate the type of the argument. [Type](#argument-types)
- A detailed explanation of each command can be found further down [Details](#protocol-details).
- Throughout the commands there are references to indices of online players, it is important to note that currently the index is completly based on the order the players joined in and will change if the players disconnect and rejoin. Currently there is no way to identify the players unqiuely based on a id.
  - You can of course still differantiate the players based on their names

#### Dimensions

- Currently the following dimensions are available
  - `world`: the normal overworld
  - `world_nether`: The Nether
  - ` world_the_end`: The End

### Player

- `getPlayer <index:int>` gets information about a player. [Details](#command-getplayer)
- `setPlayerPos <playerindex:int> <x:int> <y:int> <z:int> <dimension> ?rotation:int?` Set the player position [Details](#command-setplayerpos)
- `setPlayerStat <type:string> <playerIndex:int> <value:double>` Set a player stat [Details](#command-setplayerstat)
- `showTitle <playerIndex:int> <title:String> <subtitle:String> <fadeIn:int> <stay:int> <fadeOut:int>` Show a Title to players [Details](#command-showtitle)

#### Inventory

- `addInv <playerIndex:int> <materialId:String> ?name:String? ?slot:int? !unbreakable!` Add an Item to the Inventor [Details](#command-addinv)
- `getInv <playerIndex:int>` Returns the inventory of a player [Details](#command-getinv)

## Block

- `setBlock <x:int> <y:int> <z:int> <dimension:String> <blockid:String>` Set a block at a location [Deailts](#command-setblock)
- `getBlock <x:int> <y:int> <z:int> <dimension:String>` gets information about a block [Details](#command-getblock)

## Chat

- `postChat <message:string>` posts a message to chat [Details](#command-postchat)
- `chatCommand <command:String>` Runs chat command in console [Details](#command-chatcommand)
- `pollChat` [Details](#command-pollchat)

### Entity

- `spawnEntity <x:int> <y:int> <z:int> <dimension:String> <entityid:String>` [Details](#command-spawnentity)
- `editEntity <target:String> ?name:String? ?position:x;y;z? ?ai:boolean?` [Details](#command-editentity)
- `getEntity <target:String>` [Details](#command-getentity)

### Boss Bar

- `spawnBossBar <name> <text>` [Details](#command-spawnbossbar)
- `editBossBar <command:string> <name:string> ?text:string? ?color:string? ?value:float?` [Details](#command-editbossbar)
- `deleteBossBar <name:string>` [Details](#command-deletebossbar)

### Misc

- `batch ;|;<command:String>;|;<command:String>` Runs all commands in a batch, the `;|;` acts as the seperator. Please make sure there are no spaces around the seperator except before the first one. [Details](#command-batch)

## Argument Types

### Positional Arguments

Positional arguments are always the first arguments in argument list. They are marked as `<arg>`. They only consist of the value.

### Optional Arguments

Optional arguments are arguments that are strictly necessary but offer additional options.
They always have the form `<argument>:<value>` where `<argument>` is the name of the argument defined by the command and `<value>` is the value.
Optional arguments are marked as `?arg?`

Important usage guides for optional arguments are:

- There **must not** be a space anywhere in this configuration. If the value needs to contain a space please replace it with the sequence `|&s&|`, this will be interpreted as a space
- They must come **after** any positional arguments

### Flags

Flags are arguments that toggle an effect, the send command just needs to containe the name of the flag. They are marked by `!flag!`.

- They too must come **after** any positional arguments

### General notes

- Optional arguments and flags can be in an arbitrary order

## Protocol Details

- In each example the first line is the command you sent, the second line is a possible return. An empty return indicates that the server send nothing back.

### Command: getPlayer

- `getPlayer <index:int>` gets information about a player

#### Args

- `index` the index of the online Player

#### Returns

- `<index:int> <name:String> <x:int> <y:int> <z:int> <dimension:String> <rotation:int> <looking_at_block:String> <sneak:boolean> <max_health:double> <health:double> <food_level:double> <saturation: double> <xp_level:int> <xp_progress:double>`
  - `<index>` the index of the player
  - `<name>` the name of the player
  - `<x>, <y>, <z>` `<dimension>` The tile position of the player (see [dimensions](#dimensions))
  - `<rotation>` The minecraft rotation of the player.
  - `<looking_at_block>` The block type the player is looking at
  - `<sneak>` depending of wether or not the player is sneaking `true` or `false`
  - `<max_healt>` the max health the player can have
  - `<health>` the current health of the player usually between 0 and 20
  - `<food_level>` the current food level of the player always between 0 and 20
  - `<saturation>` The players saturation level
  - `<xp_level>` The level number of the player
  - `<xp_progress>` The current progress of the player along the progress bar, will be between 0 and 1

#### Notes

- If the player is not looking at anything the `looking_at_block` will be AIR
- The rotation will be between -180 and 180

#### Example

```bash
getPlayer 0
0 ThisRyan 39 83 72 17 STONE false
```

### Command: setPlayerStat

- `setPlayerStat <type:string> <playerIndex:int> <value:double>`

#### Args

- `type` one of `MAX_HEALTH`, `HEALTH`, `FOOD_LEVEL`, `SATURATION`, `XP_LEVEL`, `XP_PROGRESS`
- `playerIndex` the index of the online player
- `value`: the value the respective stat should be set to

### Example

```bash
setPlayerStat MAX_HEALTH 0 10

```

### Command: setPlayerVelocity

- `setPlayerVelocity <type:String> <playerIndex:int> <value:double>`

#### Args

- `type` one of `UP`, `DOWN`, `BACK`,`LOOKING`
  - `UP` is an impulse straight up
  - `Down` is an impulse straight down
  - `BACK` is an impulse away from the players looking direction
  - `LOOKING` is an impulse in the direction the player is looking
- `playerIndex` the index of the online player
- `value` the strength of the impulse. The strenght of a normal player jump is 0.5

#### Example

```bash
setPlayerVelocity UP 0 1

```

### Command: showTitle

- `showTitle <playerIndex:int> <title:String> <subtitle:String> <fadeIn:int> <stay:int> <fadeOut:int>`

#### Args

- `playerIndex` Index of a player, if the index is smaller then 0 the title is shown to all players
- `title` The title to show
- `subtitle` The subtitle to show
- `fadeIn` The time it takes for the title to fade in, in ticks
- `stay` The time it takes for the title to stay, in ticks
- `fadeOut` The time it takes for the title to fade out, in ticks

#### Example

```bash
showTitle𝇉-1𝇉Das ist ein Title𝇉Mit einem subtitle𝇉200𝇉200𝇉200

```

### Command: addInv

- `addInv <playerIndex:int> <materialId:String> ?name:String? ?slot:int? !unbreakable!`

#### Args

- `<playerIndex>` the index of the player
- `<materialId>` the material
- `<amount>` The amount that should be added
- `?name?` the custom name of the stack in the inventory
- `?slot?` The slot in which the item should be added
- `!unbreakable!` Controls wether or not the item is unbreakable or not

#### Example

```bash
addInv 0 wooden_pickaxe 1 unbreakable name:Adrians|&s&|Pickaxe slot:2

```

### Command: getInv

- `getInv <playerIndex:int>` Returns the inventory of a player

#### Args

- `<playerIndex>` Index of the player to query

#### Returns

A series of items of the following structure: `<index:int>:<materialname:String>;<displayname:String>:<amount:int>`, these are separated by spaces. Notably only inventory slots with content will be sent everything else can be assumed to be empty.

#### Example

```bash
getInv 0
0:LILY_OF_THE_VALLEY:1 4:STONE_PRESSURE_PLATE:1 7:SCULK_SHRIEKER:1 11:REDSTONE:13 25:DISPENSER:1 29:TARGET:1
```

### Command: setPlayerPos

- `setPlayerPos <playerindex:int> <x:int> <y:int> <z:int> <dimension:String> ?rotation:int?`

#### Args

- `playerIndex` the index of the online player
- `<x:>, <y>, <z> <dimension>` the new position of the player (see [dimensions](#dimensions))
- `?rotation?` optionaly the direction the player should be looking in

### Command: setBlock

- `setBlock <x:int> <y:int> <z:int> <dimension:String> <blockid:String>` Set a block at a location

#### Args

- `<x> <y> <z>` position of the block
- `<dimension>` see [dimensions](#dimensions)
- `<blockid>` the block id as a string i.e. "stone"

#### Example

```bash
setBlock 10 10 10 stone

```

### Command: getBlock

- `getBlock <x:int> <y:int> <z:int> <dimension:String>` gets information about a block

#### Args

- `<x> <y> <z>:int int int` position of the block
- `<dimension>` see [dimensions](#dimensions)

#### Example

```bash
getBlock 10 10 10
DEEPSLATE
```

### Command: postChat

- `postChat <message:string>` posts a message to chat

#### Args

- `<message>` is the message which should be posted, everything after the `postChat` is interpreted as the message

#### Example

```bash
postChat Hello World my name is Adrian

```

### Command: chatCommand

- `chatCommand <command:String>` Runs chat command in console

#### Args

- `<command>` is the command, everything after the `chatCommand` is interpreted as the command. It should not contain the /

#### Example

```bash
chatCommand say Hello from plugin!

```

### Command: pollChat

- `pollChat`

#### Return

- A list of block of `<playername>:<message>` seperated by `𝇉`

#### Example

```bash
pollChat
Player1:Hallo𝇉Player1:A second message𝇉Player2:Last message
```

### Command: spawnEntity

- `spawnEntity <x:int> <y:int> <z:int> <dimension:String> <entityid:String>` Spawns an entity

#### Args

- `<x> <y> <z>` position
- `<dimension>` see [dimensions](#dimensions) 
- `<entityid>` a string with entity id full list can be cound here: [Entities](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html)

#### Returns

- the uuid with which the entity can be identified

#### Example

```bash
spawnEntity 10 10 10 Zombie`
ff568527-7c0c-4536-aed2-ef77429d61b8
```

### Command: editEntity

- `editEntity <target:String> ?name:String? ?position:x;y;z? ?ai:boolean?` Edits a existing entity which was previously spawned by the `spawnEntity` command

#### Args

- `target` is the uuid returned by `spawnEntity`
- `name` if is set the entites custom name is changed
- `position`if  is set the entities position will be set to the given x,y,z coordinates
- `ai` sets the ai of the entity, if its turned off the entity will not move.

#### Example

```bash
editEntity ff568527-7c0c-4536-aed2-ef77429d61b8 name:Zomb position:10;10;200 ai:false

```

### Command: getEntity

- `getEntity <target:String>`

#### Args

- `<target>` needs to be the entity id returned by spawn Entity

#### Returns

- `<target:String> <name:string> <x:int> <y:int> <z:int> <dimension:String> <health:double>`

- `<dimensions>` see [dimensions](#dimensions)

#### Example

```bash
getEntity ff568527-7c0c-4536-aed2-ef77429d61b8
ff568527-7c0c-4536-aed2-ef77429d61b8 Zomb 10 10 200 5
```

### Command: spawnBossBar

- `spawnBossBar <name> <text>`

#### Args

- `<name>: the name which can later be used to change the boss bar`
- `<text>`: the text which is above the boss bar.(Currenlty a single word)

#### Example

```bash
spawnBossBar myuniquename Text

```

### Command: editBossBar

- `editBossBar <command:string> <name:string> ?text:string? ?color:string? ?value:float?`

#### Args

- `<command> Which edit command should be executed, possible values are`
  - `text : Edit the text which is display, the ?text? argument needs to be set`
  - `color: Edit the color of the bossBar ?color? argument needs to be set`
  - `value: Edit the value of the bossbar ?value? argument needs to be set`
  - `style: Edit the style of the bossbar ?style? argument need to bet set`

#### Example

```bash
editBossBar text myuniquename text:Hello

```

### Command: deleteBossBar

- `deleteBossBar <name:string>` deletes the boss bar

#### Args

- `<name>` the name of the boss bar

#### Example

```bash
deleteBossBar myuniquename

```

### Command: batch

!!This command is currently not supported!!

- `batch ;|;<command:String>;|;<command:String>` Runs all commands in a batch, the `;|;` acts as the seperator. Please make sure there are no spaces around the seperator except before the first one.

#### Args

- The commands to be batched. Each command can be a normal command, **spaces do not need to be replaced**.

#### Notes

- Make special note of the space before the first `;|;` this is important because otherwise the `batch` command can not be recognized
- a seperator at the end is not necessary but does not affect the outcome, the seperator after the `batch` command is necessary
- If you batch multiple commands which receive input the output comes line-by-line in the order of the commands in the batching command

#### Examples

- This example gives the player wooden pickaxe and post the message "Hello World this is a message" to the chat

```bash
batch ;|;addInv 0 wooden_pickaxe 1 unbreakable;|;postChat Hello World this is message`

```
