package com.edu.test.domain;

import com.edu.test.service.Printable;
import java.util.Random;

public abstract class Unit implements Printable {
    private String name;
    protected int hp;
    protected int maxHp;
    protected boolean stunned = false;
    protected boolean invisible = false;
    protected boolean damageBoost = false;
    protected boolean defenseBoost = false;
    protected Position position;
    protected Random random = new Random();
    protected String className;

    public Unit(String name, int hp, String className) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.className = className;
        this.position = Position.values()[random.nextInt(3)];
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isStunned() {
        return stunned;
    }

    public void setStunned(boolean stunned) {
        this.stunned = stunned;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean hasDamageBoost() {
        return damageBoost;
    }

    public void setDamageBoost(boolean damageBoost) {
        this.damageBoost = damageBoost;
    }

    public boolean hasDefenseBoost() {
        return defenseBoost;
    }

    public void setDefenseBoost(boolean defenseBoost) {
        this.defenseBoost = defenseBoost;
    }

    public void heal(int amount) {
        hp = Math.min(hp + amount, maxHp);
    }

    public void clearBuffs() {
        damageBoost = false;
        defenseBoost = false;
    }

    public abstract int getDamage(int dmg);
    public abstract void attack(Unit unit);
    public abstract void specialAttack(Unit unit);
    public abstract void aoeAttack(Unit[] enemies);

    @Override
    public void printInfo() {
        System.out.println();
        System.out.println("===============");
        System.out.println("👤 Имя: " + name);
        System.out.println("🎯 Класс: " + className);
        System.out.println("❤️ HP: " + hp + "/" + maxHp);
        System.out.println("📍 Позиция: " + getPositionName());

        // Статусы
        StringBuilder status = new StringBuilder("📊 Статусы: ");
        if (stunned) status.append("🔹Оглушен ");
        if (invisible) status.append("🔹Невидим ");
        if (damageBoost) status.append("🔹Усиление урона ");
        if (defenseBoost) status.append("🔹Усиление защиты ");
        System.out.println(status.toString());
    }

    private String getPositionName() {
        switch (position) {
            case LEFT: return "⬅️ Слева";
            case CENTER: return "⬆️ Центр";
            case RIGHT: return "➡️ Справа";
            default: return "❓ Неизвестно";
        }
    }

    protected void applyBuffsAndDebuffs() {
        // Сбрасываем баффы в начале хода (кроме невидимости и оглушения)
        damageBoost = false;
        defenseBoost = false;
    }
}