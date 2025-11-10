import java.util.*;

class Room {
    private String name;
    private String description;
    private Map<String, Room> exits = new HashMap<>();
    private List<Item> items = new ArrayList<>();
    private Enemy enemy = null;
    private boolean locked = false;
    private String keyNameRequired = null;

    public Room(String name, String description) {
        this.name = name; this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setExit(String dir, Room room) { exits.put(dir, room); }
    public Room getExit(String dir) { return exits.get(dir); }
    public Set<String> getExitDirections() { return exits.keySet(); }

    public void addItem(Item i) { items.add(i); }
    public List<Item> getItems() { return items; }

    public void setEnemy(Enemy e) { this.enemy = e; }
    public Enemy getEnemy() { return enemy; }

    public void setLocked(String keyName) { locked = true; keyNameRequired = keyName; }
    public boolean isLocked() { return locked; }

    public boolean unlock(String keyName) {
        if (!locked) return true;
        if (keyNameRequired != null && keyNameRequired.equalsIgnoreCase(keyName)) {
            locked = false;
            keyNameRequired = null;
            return true;
        }
        return false;
    }
}
