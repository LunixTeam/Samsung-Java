package com.edu.test.domain;

import java.util.Random;

public class Warrior extends Unit {
    private int armor;
    private int maxArmor;

    public Warrior(String name) {
        super(name, 120 + new Random().nextInt(41), "⚔️ Воин"); // HP: 120-160
        this.armor = 6 + new Random().nextInt(5); // Броня: 6-10
        this.maxArmor = this.armor;
    }

    @Override
    public int getDamage(int dmg) {
        int actualDmg = dmg;

        if (hasDefenseBoost()) {
            actualDmg /= 2;
        }

        if (armor > 0) {
            actualDmg = Math.max(1, actualDmg / 2);
            armor--;
        }

        hp -= actualDmg;
        return actualDmg;
    }

    @Override
    public void attack(Unit unit) {
        applyBuffsAndDebuffs();

        if (stunned) {
            System.out.println("⚔️ " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int dmg = 14 + random.nextInt(7); // 14-20 урона
        int actualDmg = unit.getDamage(dmg);
        System.out.println("⚔️ " + getName() + " атакует мечом! Урон: " + actualDmg);
    }

    @Override
    public void specialAttack(Unit unit) {
        applyBuffsAndDebuffs();

        if (stunned) {
            System.out.println("⚔️ " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int skillType = random.nextInt(3);
        switch (skillType) {
            case 0: // Мощный удар
                int heavyDmg = 28 + random.nextInt(13); // 28-40 урона
                int actualHeavyDmg = unit.getDamage(heavyDmg);
                System.out.println("💥 " + getName() + " использует Мощный удар! Урон: " + actualHeavyDmg);
                break;

            case 1: // Боевой клич
                setDamageBoost(true);
                System.out.println("📢 " + getName() + " издает Боевой клич! Урон усилен на следующий ход");
                break;

            case 2: // Восстановление брони
                int restoredArmor = 2 + random.nextInt(3); // 2-4 брони
                armor = Math.min(armor + restoredArmor, maxArmor);
                System.out.println("🔧 " + getName() + " восстанавливает броню! +" + restoredArmor + " брони");
                break;
        }
    }

    @Override
    public void aoeAttack(Unit[] enemies) {
        applyBuffsAndDebuffs();

        System.out.println("🔄 " + getName() + " использует Круговую атаку мечом!");

        for (Unit enemy : enemies) {
            if (enemy.getHp() > 0 && enemy.getPosition() == this.position) {
                int dmg = 10 + random.nextInt(6); // 10-15 урона
                int actualDmg = enemy.getDamage(dmg);
                System.out.println("   💥 По " + enemy.getName() + " на позиции " + getPositionName() + ": " + actualDmg + " урона");
            }
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("🛡️ Броня: " + armor + "/" + maxArmor);
        System.out.println("📚 Способности: Мощный удар, Боевой клич, Восстановление брони, Круговая атака");
    }

    private String getPositionName() {
        switch (position) {
            case LEFT: return "⬅️ Слева";
            case CENTER: return "⬆️ Центр";
            case RIGHT: return "➡️ Справа";
            default: return "❓ Неизвестно";
        }
    }
}