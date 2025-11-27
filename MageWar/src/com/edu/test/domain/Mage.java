package com.edu.test.domain;

import java.util.Random;

public class Mage extends Unit {
    private int mp;
    private int maxMp;

    public Mage(String name, int hp, int mp) {
        super(name, hp, "🔮 Маг");
        this.mp = mp;
        this.maxMp = mp;
    }

    public Mage(String name) {
        super(name, 70 + new Random().nextInt(31), "🔮 Маг"); // HP: 70-100
        this.mp = 50 + new Random().nextInt(26); // MP: 50-75
        this.maxMp = this.mp;
    }

    public int getMp() {
        return mp;
    }

    // Восстановление маны в начале хода
    @Override
    public void applyBuffsAndDebuffs() {
        super.applyBuffsAndDebuffs();
        // Маги восстанавливают 5-10 маны каждый ход
        int manaRegen = 5 + random.nextInt(6);
        mp = Math.min(mp + manaRegen, maxMp);
    }

    @Override
    public int getDamage(int dmg) {
        if (isInvisible()) {
            System.out.println("🌀 " + getName() + " невидим и избегает урона!");
            return 0;
        }

        int actualDmg = hasDefenseBoost() ? dmg / 2 : dmg;
        hp -= actualDmg;
        return actualDmg;
    }

    @Override
    public void attack(Unit unit) {
        applyBuffsAndDebuffs();

        if (stunned) {
            System.out.println("🔮 " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int dmg = 10 + random.nextInt(6); // 10-15 урона
        if (mp >= 5) {
            int actualDmg = unit.getDamage(dmg);
            mp -= 5;
            System.out.println("🔮 " + getName() + " атакует магией! Урон: " + actualDmg + " | Мана: -5");
        } else {
            int actualDmg = unit.getDamage(dmg / 2);
            System.out.println("🔮 " + getName() + " атакует посохом! Урон: " + actualDmg + " (не хватает маны)");
        }
    }

    @Override
    public void specialAttack(Unit unit) {
        applyBuffsAndDebuffs();

        if (stunned) {
            System.out.println("🔮 " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        if (mp < 20) {
            System.out.println("🔮 " + getName() + " не хватает маны для специальной атаки!");
            attack(unit);
            return;
        }

        int spellType = random.nextInt(4);
        switch (spellType) {
            case 0: // Огненный шар
                int fireDmg = 25 + random.nextInt(11); // 25-35 урона
                int actualFireDmg = unit.getDamage(fireDmg);
                mp -= 20;
                System.out.println("🔥 " + getName() + " использует Огненный шар! Урон: " + actualFireDmg + " | Мана: -20");
                break;

            case 1: // Ледяная стрела
                int iceDmg = 18 + random.nextInt(8); // 18-25 урона
                int actualIceDmg = unit.getDamage(iceDmg);
                unit.setStunned(true);
                mp -= 25;
                System.out.println("❄️ " + getName() + " использует Ледяную стрелу! Урон: " + actualIceDmg + " | Враг оглушен! | Мана: -25");
                break;

            case 2: // Магический щит
                setDefenseBoost(true);
                mp -= 15;
                System.out.println("🛡️ " + getName() + " использует Магический щит! Защита усилена на следующий ход | Мана: -15");
                break;

            case 3: // Энергетический всплеск
                int energyDmg = 15 + random.nextInt(6); // 15-20 урона
                int actualEnergyDmg = unit.getDamage(energyDmg);
                // Восстанавливает часть маны при успешной атаке
                int manaGain = 10;
                mp = Math.min(mp + manaGain, maxMp);
                mp -= 12;
                System.out.println("⚡ " + getName() + " использует Энергетический всплеск! Урон: " + actualEnergyDmg + " | +" + manaGain + " маны | Мана: -12");
                break;
        }
    }

    @Override
    public void aoeAttack(Unit[] enemies) {
        applyBuffsAndDebuffs();

        if (mp < 35) {
            System.out.println("🔮 " + getName() + " не хватает маны для АОЕ атаки!");
            return;
        }

        System.out.println("🌪️ " + getName() + " вызывает Магическую бурю по всем врагам!");
        mp -= 35;

        for (Unit enemy : enemies) {
            if (enemy.getHp() > 0) {
                int dmg = 12 + random.nextInt(9); // 12-20 урона
                int actualDmg = enemy.getDamage(dmg);
                System.out.println("   💥 По " + enemy.getName() + ": " + actualDmg + " урона");
            }
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("🔵 Мана: " + mp + "/" + maxMp + " (+5-10 за ход)");
        System.out.println("📚 Заклинания: Огненный шар, Ледяная стрела, Магический щит, Энергетический всплеск");
    }
}