package com.edu.test.domain;

import java.util.Random;

public class Samurai extends Unit {
    private int honorPoints;
    private boolean counterStance = false;

    public Samurai(String name) {
        super(name, 100 + new Random().nextInt(41), "🗡️ Самурай"); // HP: 100-140
        this.honorPoints = 0;
    }

    @Override
    public int getDamage(int dmg) {
        if (counterStance) {
            System.out.println("🎌 " + getName() + " парирует атаку и контратакует!");
            counterStance = false;
            // Контратака наносит 50% от полученного урона
            return dmg / 2;
        }

        int actualDmg = dmg;
        hp -= actualDmg;
        honorPoints++; // Получает честь при получении урона
        return actualDmg;
    }

    @Override
    public void attack(Unit unit) {
        applyBuffsAndDebuffs();
        counterStance = false;

        if (stunned) {
            System.out.println("🗡️ " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int dmg = 16 + random.nextInt(9); // 16-24 урона
        if (hasDamageBoost()) {
            dmg = (int)(dmg * 1.5);
        }
        int actualDmg = unit.getDamage(dmg);
        System.out.println("🗡️ " + getName() + " атакует катаной! Урон: " + actualDmg);
        honorPoints++;
    }

    @Override
    public void specialAttack(Unit unit) {
        applyBuffsAndDebuffs();
        counterStance = false;

        if (stunned) {
            System.out.println("🗡️ " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int skillType = random.nextInt(3);
        switch (skillType) {
            case 0: // Быстрая атака
                int fastDmg = 22 + random.nextInt(11); // 22-32 урона
                int actualFastDmg = unit.getDamage(fastDmg);
                System.out.println("💨 " + getName() + " использует Быструю атаку! Урон: " + actualFastDmg);
                honorPoints += 2;
                break;

            case 1: // Стойка контратаки
                counterStance = true;
                System.out.println("🎌 " + getName() + " принимает стойку контратаки! Следующая атака будет парирована с контратакой");
                break;

            case 2: // Финальный удар
                if (honorPoints >= 3) {
                    int finalDmg = 35 + random.nextInt(16); // 35-50 урона
                    int actualFinalDmg = unit.getDamage(finalDmg);
                    honorPoints -= 3;
                    System.out.println("⚡ " + getName() + " использует Финальный удар! Урон: " + actualFinalDmg + " | Честь: -3");
                } else {
                    System.out.println("🗡️ " + getName() + " не хватает чести для Финального удара! (нужно 3, есть " + honorPoints + ")");
                    attack(unit);
                }
                break;
        }
    }

    @Override
    public void aoeAttack(Unit[] enemies) {
        applyBuffsAndDebuffs();
        counterStance = false;

        if (honorPoints < 2) {
            System.out.println("🗡️ " + getName() + " не хватает чести для Вихревой атаки! (нужно 2)");
            return;
        }

        System.out.println("🌪️ " + getName() + " использует Вихревую атаку катаной по всем на своей позиции!");
        honorPoints -= 2;

        for (Unit enemy : enemies) {
            if (enemy.getHp() > 0 && enemy.getPosition() == this.position) {
                int dmg = 14 + random.nextInt(9); // 14-22 урона
                int actualDmg = enemy.getDamage(dmg);
                System.out.println("   💥 По " + enemy.getName() + ": " + actualDmg + " урона");
            }
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("🎗️ Честь: " + honorPoints + " (получает за урон и атаки)");
        System.out.println("📚 Способности: Быстрая атака, Стойка контратаки, Финальный удар, Вихревая атака");
    }
}