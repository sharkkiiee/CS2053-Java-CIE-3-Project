import java.util.*;

class Player {
    private Room currentRoom;
    private List<Item> inventory = new ArrayList<>();
    private int maxHealth;
    private int health;

    public Player(Room start, int maxHealth) {
        this.currentRoom = start; this.maxHealth = maxHealth; this.health = maxHealth;
    }

    public Room getCurrentRoom() { return currentRoom; }
    public void moveTo(Room r) { currentRoom = r; }

    public void take(Item i) { inventory.add(i); }
    public void drop(Item i) { inventory.remove(i); }
    public List<Item> getInventory() { return inventory; }

    public int getHealth() { return health; }
    public void heal(int amount) { health = Math.min(maxHealth, health + amount); }
    public void damage(int amount) { health = Math.max(0, health - amount); }
    public boolean isAlive() { return health > 0; }

    public Weapon getEquippedWeapon() {
        for (Item it : inventory) if (it instanceof Weapon) return (Weapon) it;
        return null;
    }

    public Potion findPotion() {
        for (Item it : inventory) if (it instanceof Potion) return (Potion) it;
        return null;
    }

    public Item findByName(String name) {
        for (Item it : inventory) if (it.getName().equalsIgnoreCase(name)) return it;
        return null;
    }

    public boolean hasKey(String keyName) {
        for (Item it : inventory) if (it instanceof Key && it.getName().equalsIgnoreCase(keyName)) return true;
        return false;
    }
}
