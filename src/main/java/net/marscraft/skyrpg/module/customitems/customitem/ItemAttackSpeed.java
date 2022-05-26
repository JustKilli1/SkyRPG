package net.marscraft.skyrpg.module.customitems.customitem;

public enum ItemAttackSpeed {

    SUPERSLOW(1, 2.5),
    SLOW(2, 2),
    NORMAL(3, 1.5),
    FAST(4, 1),
    SUPERFAST(5, 0.5)
    ;

    //AttackCooldown between 2 Attacks in seconds
    private int id;
    private double attackCooldown;

    ItemAttackSpeed(int id, double attackCooldown) {
        this.id = id;
        this.attackCooldown = attackCooldown;
    }

    public double getAttackCooldown() {
        return attackCooldown;
    }

    public int getId() {
        return id;
    }
}
