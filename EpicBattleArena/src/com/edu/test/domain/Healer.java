package com.edu.test.domain;

import java.util.Random;

public class Healer extends Unit {
    private int mp;
    private int maxMp;

    public Healer(String name) {
        super(name, 80 + new Random().nextInt(31), "💚 Целитель"); // HP: 80-110
        this.mp = 60 + new Random().nextInt(26); // MP: 60-85
        this.maxMp = this.mp;
    }

    // Восстановление маны в начале хода
    @Override
    public void applyBuffsAndDebuffs() {
        super.applyBuffsAndDebuffs();
        // Целители восстанавливают 8-13 маны каждый ход
        int manaRegen = 8 + random.nextInt(6);
        mp = Math.min(mp + manaRegen, maxMp);
    }

    @Override
    public int getDamage(int dmg) {
        int actualDmg = hasDefenseBoost() ? dmg / 2 : dmg;
        hp -= actualDmg;
        return actualDmg;
    }

    @Override
    public void attack(Unit unit) {
        applyBuffsAndDebuffs();

        if (stunned) {
            System.out.println("💚 " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int dmg = 6 + random.nextInt(5); // 6-10 урона
        int actualDmg = unit.getDamage(dmg);
        System.out.println("💚 " + getName() + " атакует посохом! Урон: " + actualDmg);
    }

    @Override
    public void specialAttack(Unit unit) {
        applyBuffsAndDebuffs();

        if (stunned) {
            System.out.println("💚 " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int spellType = random.nextInt(3);
        switch (spellType) {
            case 0: // Исцеление союзника
                if (mp >= 18) {
                    int healAmount = 30 + random.nextInt(21); // 30-50 лечения
                    unit.heal(healAmount);
                    mp -= 18;
                    System.out.println("🌟 " + getName() + " исцеляет " + unit.getName() + " на " + healAmount + " HP | Мана: -18");
                } else {
                    attack(unit);
                }
                break;

            case 1: // Божественный щит
                if (mp >= 20) {
                    setDefenseBoost(true);
                    mp -= 20;
                    System.out.println("✨ " + getName() + " создает Божественный щит! Полная защита на следующий ход | Мана: -20");
                } else {
                    attack(unit);
                }
                break;

            case 2: // Очищение + лечение
                if (mp >= 15) {
                    setStunned(false);
                    setInvisible(false);
                    int selfHeal = 15 + random.nextInt(11); // 15-25 лечения
                    heal(selfHeal);
                    mp -= 15;
                    System.out.println("💫 " + getName() + " использует Очищение! Снимает негативные эффекты + " + selfHeal + " HP | Мана: -15");
                } else {
                    attack(unit);
                }
                break;
        }
    }

    @Override
    public void aoeAttack(Unit[] enemies) {
        applyBuffsAndDebuffs();

        if (mp < 30) {
            System.out.println("💚 " + getName() + " не хватает маны для массового исцеления!");
            return;
        }

        System.out.println("🙏 " + getName() + " использует Массовое исцеление на всех союзников!");
        mp -= 30;

        for (Unit ally : enemies) { // В этом контексте enemies - союзники
            if (ally.getHp() > 0) {
                int healAmount = 20 + random.nextInt(16); // 20-35 лечения
                ally.heal(healAmount);
                System.out.println("   💚 " + ally.getName() + " получает +" + healAmount + " HP");
            }
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("🔵 Мана: " + mp + "/" + maxMp + " (+8-13 за ход)");
        System.out.println("📚 Заклинания: Исцеление, Божественный щит, Очищение, Массовое исцеление");
    }
}