class Potion extends Item {
    private int healAmount;
    public Potion(String name, String description, int healAmount) {
        super(name, description);
        this.healAmount = healAmount;
    }
    public int getHealAmount() { return healAmount; }
}
