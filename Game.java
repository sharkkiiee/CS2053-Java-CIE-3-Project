import java.util.*;

public class Game {
    private Scanner scanner = new Scanner(System.in);
    private Player player;
    private boolean running = true;
    private boolean treasureTaken = false;

    public static void main(String[] args) {
        new Game().start();
    }

    private void start() {
        // Build world
        Room dungeon = new Room("Dungeon", "A cold dim room with stone walls.");
        Room hallway = new Room("Hallway", "A narrow hallway with torches.");
        Room armory = new Room("Armory", "Old weapon racks, some intact.");
        Room dragonLair = new Room("Dragon Lair", "A cavernous room with piles of gold.");
        Room exitRoom = new Room("Exit", "A heavy door leading to freedom.");

        dungeon.setExit("north", hallway);
        hallway.setExit("south", dungeon);
        hallway.setExit("east", armory);
        hallway.setExit("north", dragonLair);
        armory.setExit("west", hallway);
        dragonLair.setExit("south", hallway);
        dragonLair.setExit("east", exitRoom);
        exitRoom.setExit("west", dragonLair);

        // Lock exitRoom and require 'GoldenKey'
        exitRoom.setLocked("GoldenKey");

        // Items
        armory.addItem(new Weapon("Short Sword", "A small but sharp sword.", 6));
        hallway.addItem(new Potion("Small Potion", "Heals 10 HP.", 10));
        dragonLair.addItem(new Key("GoldenKey", "A heavy key made of gold."));
        dragonLair.addItem(new Item("Treasure","A pile of glittering treasure.") {}); // anonymous Item for treasure

        // Enemy
        Enemy dragon = new Enemy("Young Dragon", 25, 7);
        dragonLair.setEnemy(dragon);

        // Player
        player = new Player(dungeon, 30);

        printIntro();

        while (running && player.isAlive()) {
            Room r = player.getCurrentRoom();
            System.out.println("\n---");
            System.out.println("You are in: " + r.getName());
            System.out.println(r.getDescription());
            if (!r.getItems().isEmpty()) {
                System.out.println("You see:");
                for (Item it : r.getItems()) System.out.println("- " + it.getName() + ": " + it.getDescription());
            }
            if (r.getEnemy() != null && r.getEnemy().isAlive()) {
                System.out.println("An enemy is here: " + r.getEnemy().getName() + " (HP: " + r.getEnemy().getHealth() + ")");
            }
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            handleCommand(input);
            // check endings
            if (!player.isAlive()) {
                System.out.println("You have died. Game Over.");
                running = false;
            }
            if (treasureTaken) {
                // if treasure taken and player reaches exit unlocked -> victory
                if (player.getCurrentRoom().getName().equals("Exit") && !player.getCurrentRoom().isLocked()) {
                    System.out.println("You step through the exit with treasure. You win!");
                    running = false;
                }
            }
        }
        if (!player.isAlive()) System.out.println("Ending: Death.");
        else if (!running) System.out.println("Thanks for playing!");
    }

    private void printIntro() {
        System.out.println("Welcome to the Adventure Game!");
        System.out.println("Commands: go [dir], look, take [item], inventory, attack, use [item], unlock [item], help, quit");
        System.out.println("Objective examples: find treasure, escape through the exit, survive encounters.");
    }

    private void handleCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String arg = parts.length > 1 ? parts[1] : "";

        switch (cmd) {
            case "go": commandGo(arg); break;
            case "look": /* repetition allowed */ break;
            case "take": commandTake(arg); break;
            case "inventory": commandInventory(); break;
            case "attack": commandAttack(); break;
            case "use": commandUse(arg); break;
            case "unlock": commandUnlock(arg); break;
            case "help": printIntro(); break;
            case "quit": running = false; break;
            default: System.out.println("Unknown command. Type 'help'.");
        }
    }

    private void commandGo(String dir) {
        if (dir.isEmpty()) { System.out.println("Go where?"); return; }
        Room current = player.getCurrentRoom();
        Room next = current.getExit(dir);
        if (next == null) { System.out.println("You can't go that way."); return; }
        if (next.isLocked()) {
            System.out.println("The way is locked. Maybe a key is required.");
            return;
        }
        player.moveTo(next);
        // enemy auto-attack on enter if present
        Enemy e = next.getEnemy();
        if (e != null && e.isAlive()) {
            System.out.println("The " + e.getName() + " notices you!");
            // enemy does a free attack
            int at = e.getAttack();
            System.out.println("It hits you for " + at + " damage!");
            player.damage(at);
            System.out.println("Your HP: " + player.getHealth());
            if (!player.isAlive()) return;
        }
    }

    private void commandTake(String arg) {
        Room r = player.getCurrentRoom();
        if (arg.isEmpty()) {
            if (r.getItems().isEmpty()) { System.out.println("Nothing to take here."); return; }
            Item it = r.getItems().remove(0);
            player.take(it);
            System.out.println("You picked up: " + it.getName());
            if (it.getName().equalsIgnoreCase("Treasure")) treasureTaken = true;
            return;
        }
        // take by name
        Item found = null;
        for (Item it : r.getItems()) {
            if (it.getName().equalsIgnoreCase(arg)) { found = it; break; }
        }
        if (found == null) { System.out.println("No such item here."); return; }
        r.getItems().remove(found);
        player.take(found);
        System.out.println("You picked up: " + found.getName());
        if (found.getName().equalsIgnoreCase("Treasure")) treasureTaken = true;
    }

    private void commandInventory() {
        List<Item> inv = player.getInventory();
        if (inv.isEmpty()) { System.out.println("Inventory empty."); return; }
        System.out.println("Inventory:");
        for (Item it : inv) {
            System.out.println("- " + it.getName() + " : " + it.getDescription());
        }
        System.out.println("HP: " + player.getHealth());
    }

    private void commandAttack() {
        Room r = player.getCurrentRoom();
        Enemy e = r.getEnemy();
        if (e == null || !e.isAlive()) { System.out.println("Nothing to attack here."); return; }

        Weapon w = player.getEquippedWeapon();
        int playerDamage = (w == null) ? 2 : w.getDamage();
        System.out.println("You attack with " + (w == null ? "your fists" : w.getName()) + " for " + playerDamage + " damage.");
        e.takeDamage(playerDamage);

        if (!e.isAlive()) {
            System.out.println("You defeated the " + e.getName() + "!");
            // drop nothing special (could drop items)
            return;
        }

        // enemy retaliates
        int enemyAtk = e.getAttack();
        System.out.println(e.getName() + " strikes back for " + enemyAtk + " damage!");
        player.damage(enemyAtk);
        System.out.println("Your HP: " + player.getHealth());
    }

    private void commandUse(String arg) {
        if (arg.isEmpty()) { System.out.println("Use what? (e.g. use Small Potion)"); return; }
        Item it = player.findByName(arg);
        if (it == null) { System.out.println("You don't have that item."); return; }
        if (it instanceof Potion) {
            Potion p = (Potion) it;
            player.heal(p.getHealAmount());
            player.drop(it);
            System.out.println("You used " + p.getName() + ". HP: " + player.getHealth());
        } else {
            System.out.println("You can't use that right now.");
        }
    }

    private void commandUnlock(String arg) {
        if (arg.isEmpty()) { System.out.println("Unlock what? (try a key name)"); return; }
        Room current = player.getCurrentRoom();
        boolean unlockedAny = false;
        for (String dir : current.getExitDirections()) {
            Room neighbor = current.getExit(dir);
            if (neighbor != null && neighbor.isLocked()) {
                if (player.hasKey(arg)) {
                    boolean ok = neighbor.unlock(arg);
                    if (ok) {
                        System.out.println("You unlocked the door to the " + neighbor.getName() + " using " + arg + ".");
                        unlockedAny = true;
                    } else {
                        System.out.println("That key doesn't fit the lock.");
                    }
                } else {
                    System.out.println("You don't have the key named: " + arg);
                }
            }
        }
        if (!unlockedAny) System.out.println("No locked doors matched that key here.");
    }
}
