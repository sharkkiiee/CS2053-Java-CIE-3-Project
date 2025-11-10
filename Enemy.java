class Enemy {
    private String name;
    private int health;
    private int attack;

    public Enemy(String name, int health, int attack) {
        this.name = name; this.health = health; this.attack = attack;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getAttack() { return attack; }

    public void takeDamage(int d) { health = Math.max(0, health - d); }
    public boolean isAlive() { return health > 0; }
}
