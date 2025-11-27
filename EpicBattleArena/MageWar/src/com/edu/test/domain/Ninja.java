package com.edu.test.domain;

import java.util.Random;

public class Ninja extends Unit {
    private int stealthCharges;
    private boolean isStealth = false;

    public Ninja(String name) {
        super(name, 80 + new Random().nextInt(31), "🥷 Ниндзя"); // HP: 80-110
        this.stealthCharges = 3; // Увеличил количество зарядов
    }

    @Override
    public int getDamage(int dmg) {
        if (isStealth) {
            System.out.println("🌀 " + getName() + " в режиме невидимости и избегает урона!");
            isStealth = false;
            return 0;
        }

        int actualDmg = dmg;
        hp -= actualDmg;
        return actualDmg;
    }

    @Override
    public void attack(Unit unit) {
        applyBuffsAndDebuffs();

        if (stunned) {
            System.out.println("🥷 " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int dmg = 12 + random.nextInt(7); // 12-18 урона
        if (isStealth) {
            dmg *= 2; // Крит урон из невидимости
            isStealth = false;
            System.out.println("🎯 " + getName() + " атакует из тени! Критический урон: " + dmg);
        } else {
            System.out.println("🥷 " + getName() + " атакует сюрикенами! Урон: " + dmg);
        }

        int actualDmg = unit.getDamage(dmg);
    }

    @Override
    public void specialAttack(Unit unit) {
        applyBuffsAndDebuffs();

        if (stunned) {
            System.out.println("🥷 " + getName() + " оглушен и пропускает ход!");
            stunned = false;
            return;
        }

        int skillType = random.nextInt(3);
        switch (skillType) {
            case 0: // Исчезновение
                if (stealthCharges > 0) {
                    isStealth = true;
                    stealthCharges--;
                    System.out.println("🌀 " + getName() + " исчезает в тенях! Следующая атака будет критической | Зарядов: " + stealthCharges);
                } else {
                    System.out.println("🥷 " + getName() + " не осталось зарядов невидимости!");
                    attack(unit);
                }
                break;

            case 1: // Отравленный клинок
                int poisonDmg = 18 + random.nextInt(13); // 18-30 урона
                int actualPoisonDmg = unit.getDamage(poisonDmg);
                unit.setDamageBoost(false); // Снимает баффы у врага
                unit.setDefenseBoost(false);
                System.out.println("☠️ " + getName() + " использует Отравленный клинок! Урон: " + actualPoisonDmg + " | Снимает баффы врага");
                break;

            case 2: // Теневой клон
                setInvisible(true);
                System.out.println("👥 " + getName() + " создает Теневого клона! Уклонение от следующей атаки");
                break;
        }
    }

    @Override
    public void aoeAttack(Unit[] enemies) {
        applyBuffsAndDebuffs();

        System.out.println("🌟 " + getName() + " использует Дождь сюрикенов по врагам на соседних позициях!");

        Position[] adjacentPositions = getAdjacentPositions();
        int targetsHit = 0;

        for (Unit enemy : enemies) {
            if (enemy.getHp() > 0) {
                for (Position pos : adjacentPositions) {
                    if (enemy.getPosition() == pos) {
                        int dmg = 8 + random.nextInt(7); // 8-14 урона
                        int actualDmg = enemy.getDamage(dmg);
                        System.out.println("   💥 По " + enemy.getName() + " на позиции " + getPositionName(pos) + ": " + actualDmg + " урона");
                        targetsHit++;
                        break;
                    }
                }
            }
        }

        if (targetsHit == 0) {
            System.out.println("   💨 Никого не задел!");
        }
    }

    private Position[] getAdjacentPositions() {
        switch (position) {
            case LEFT: return new Position[]{Position.LEFT, Position.CENTER};
            case CENTER: return new Position[]{Position.LEFT, Position.CENTER, Position.RIGHT};
            case RIGHT: return new Position[]{Position.CENTER, Position.RIGHT};
            default: return new Position[]{};
        }
    }

    private String getPositionName(Position pos) {
        switch (pos) {
            case LEFT: return "⬅️ Слева";
            case CENTER: return "⬆️ Центр";
            case RIGHT: return "➡️ Справа";
            default: return "❓ Неизвестно";
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("🌀 Заряды невидимости: " + stealthCharges);
        System.out.println("📚 Способности: Исчезновение, Отравленный клинок, Теневой клон, Дождь сюрикенов");
    }
}