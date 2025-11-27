package com.edu.test;

import com.edu.test.domain.*;
import com.edu.test.service.Printable;

import java.util.*;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final String[] WARRIOR_NAMES = {"Арагорн", "Боромир", "Гимли", "Геральт", "Лютер", "Тирион", "Драко", "Каэден", "Тор", "Один"};
    private static final String[] MAGE_NAMES = {"Гэндальф", "Мерлин", "Дамблдор", "Йеннифер", "Трисс", "Сирана", "Эльминстер", "Радагаст", "Моргана", "Сараман"};
    private static final String[] HEALER_NAMES = {"Элронд", "Гальадриэль", "Жасмин", "Лиана", "Селина", "Аурения", "Фаэй", "Исильда", "Мико", "Эйлин"};
    private static final String[] SAMURAI_NAMES = {"Кенши", "Такеши", "Хаттори", "Мусаши", "Дзиро", "Рю", "Каге", "Ямато", "Хана", "Кенсин"};
    private static final String[] NINJA_NAMES = {"Хаято", "Сайдзо", "Кайдэн", "Шикамару", "Итачи", "Наруто", "Саске", "Какаши", "Ранмару", "Цунаде"};

    public static void main(String[] args) {
        printWelcomeMessage();
        int gameMode = selectGameMode();
        startBattle(gameMode);
        scanner.close();
    }

    private static void printWelcomeMessage() {
        System.out.println("🎮 Добро пожаловать в EPIC BATTLE ARENA! 🎮");
        System.out.println("===========================================");
        System.out.println("⚔️  Доступные классы:");
        System.out.println("🔮 Маг - Мощные заклинания, контроль, АОЕ атаки");
        System.out.println("💚 Целитель - Исцеление, защита, поддержка союзников");
        System.out.println("⚔️ Воин - Баланс атаки и защиты, броня");
        System.out.println("🗡️ Самурай - Высокий урон, контратаки, система чести");
        System.out.println("🥷 Ниндзя - Невидимость, критические атаки, уклонение");
        System.out.println();
        System.out.println("🔄 Особенности:");
        System.out.println("📍 25% шанс смены позиций у команды каждый раунд (со 2-го раунда)");
        System.out.println("🎯 АОЕ атаки зависят от текущей позиции");
        System.out.println();
    }

    private static int selectGameMode() {
        int mode = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("🎯 Выберите режим игры:");
            System.out.println("1️⃣  - 1 на 1 (Дуэль)");
            System.out.println("2️⃣  - 2 на 2 (Командный бой)");
            System.out.println("3️⃣  - 3 на 3 (Массовая битва)");
            System.out.print("👉 Ваш выбор: ");

            if (scanner.hasNextInt()) {
                mode = scanner.nextInt();
                if (mode >= 1 && mode <= 3) {
                    validInput = true;
                } else {
                    System.out.println("❌ Неверный выбор! Пожалуйста, введите число от 1 до 3.");
                }
            } else {
                System.out.println("❌ Неверный ввод! Пожалуйста, введите число от 1 до 3.");
                scanner.next();
            }
            System.out.println();
        }
        return mode;
    }

    private static void startBattle(int teamSize) {
        System.out.println("⚔️ НАЧИНАЕТСЯ БИТВА " + teamSize + " на " + teamSize + "!");
        System.out.println("=================================");

        Unit[] team1 = createTeam("Команда 1", teamSize);
        Unit[] team2 = createTeam("Команда 2", teamSize);

        System.out.println("\n🎯 НАЧАЛЬНЫЙ СОСТАВ КОМАНД:");
        printTeamInfo(team1, "Команда 1");
        printTeamInfo(team2, "Команда 2");

        waitForContinue();

        System.out.println("\n🎲 НАЧИНАЕМ БИТВУ!");
        battle(team1, team2);

        printBattleResults(team1, team2);
    }

    private static Unit[] createTeam(String teamName, int size) {
        Unit[] team = new Unit[size];
        List<Position> availablePositions = new ArrayList<>(Arrays.asList(Position.values()));
        Collections.shuffle(availablePositions);

        System.out.println("\n" + teamName + ":");
        for (int i = 0; i < size; i++) {
            int classType = random.nextInt(5);
            String name = generateName(classType);
            Position position = availablePositions.get(i % availablePositions.size());

            Unit unit = switch (classType) {
                case 0 -> new Warrior(name);
                case 1 -> new Mage(name);
                case 2 -> new Healer(name);
                case 3 -> new Samurai(name);
                case 4 -> new Ninja(name);
                default -> new Warrior(name);
            };

            unit.setPosition(position);
            team[i] = unit;
            team[i].printInfo();
        }
        return team;
    }

    private static String generateName(int classType) {
        return switch (classType) {
            case 0 -> WARRIOR_NAMES[random.nextInt(WARRIOR_NAMES.length)];
            case 1 -> MAGE_NAMES[random.nextInt(MAGE_NAMES.length)];
            case 2 -> HEALER_NAMES[random.nextInt(HEALER_NAMES.length)];
            case 3 -> SAMURAI_NAMES[random.nextInt(SAMURAI_NAMES.length)];
            case 4 -> NINJA_NAMES[random.nextInt(NINJA_NAMES.length)];
            default -> WARRIOR_NAMES[random.nextInt(WARRIOR_NAMES.length)];
        };
    }

    private static void printTeamInfo(Unit[] team, String teamName) {
        System.out.println("\n" + teamName + ":");
        Arrays.sort(team, Comparator.comparing(Unit::getPosition));
        for (Unit unit : team) {
            unit.printInfo();
        }
    }

    private static void battle(Unit[] team1, Unit[] team2) {
        int round = 1;

        while (isTeamAlive(team1) && isTeamAlive(team2)) {
            System.out.println("\n🎯 РАУНД " + round + "!");
            System.out.println("====================");

            if (round > 1) {
                boolean team1Changes = random.nextDouble() < 0.25;
                boolean team2Changes = random.nextDouble() < 0.25;

                if (team1Changes || team2Changes) {
                    System.out.println("\n🔄 СМЕНА ПОЗИЦИЙ:");
                }

                if (team1Changes) {
                    changePositions(team1, "Команда 1");
                }
                if (team2Changes) {
                    changePositions(team2, "Команда 2");
                }

                if (!team1Changes && !team2Changes) {
                    System.out.println("\n📍 Позиции остаются без изменений");
                }

                waitForContinue();
            }

            System.out.println("\n⭐ ХОД КОМАНДЫ 1:");
            teamTurn(team1, team2);

            if (!isTeamAlive(team2)) break;

            waitForContinue();

            System.out.println("\n⭐ ХОД КОМАНДЫ 2:");
            teamTurn(team2, team1);

            round++;

            if (isTeamAlive(team1) && isTeamAlive(team2)) {
                System.out.println("\n⏸️ Конец раунда " + round);
                waitForContinue();
            }
        }
    }

    private static void changePositions(Unit[] team, String teamName) {
        System.out.println("\n🔄 " + teamName + " меняет позиции:");

        List<Position> positions = new ArrayList<>();
        for (Unit unit : team) {
            if (unit.getHp() > 0) {
                positions.add(unit.getPosition());
            }
        }
        Collections.shuffle(positions);

        int positionIndex = 0;
        for (Unit unit : team) {
            if (unit.getHp() > 0) {
                Position oldPosition = unit.getPosition();
                Position newPosition = positions.get(positionIndex % positions.size());
                unit.setPosition(newPosition);
                System.out.println("   " + unit.getName() + ": " +
                        getPositionName(oldPosition) + " → " + getPositionName(newPosition));
                positionIndex++;
            }
        }
    }

    private static void teamTurn(Unit[] attackingTeam, Unit[] defendingTeam) {
        Arrays.sort(attackingTeam, Comparator.comparing(Unit::getPosition));

        for (Unit attacker : attackingTeam) {
            if (attacker.getHp() > 0) {
                Unit target = findAliveTarget(defendingTeam, attacker.getPosition());
                if (target != null) {
                    printTurnInfo(attacker, target);

                    double attackType = random.nextDouble();
                    if (attackType < 0.25) {
                        attacker.aoeAttack(defendingTeam);
                    } else if (attackType < 0.60) {
                        attacker.specialAttack(target);
                    } else {
                        attacker.attack(target);
                    }

                    if (target.getHp() <= 0) {
                        System.out.println("💀 " + target.getName() + " повержен!");
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private static void printTurnInfo(Unit attacker, Unit target) {
        System.out.println("\n🎭 " + attacker.getName() + " (" + attacker.getClass().getSimpleName() +
                ") 🆚 " + target.getName() + " (" + target.getClass().getSimpleName() + ")");
        System.out.println("📍 Позиции: " + getPositionName(attacker.getPosition()) + " → " + getPositionName(target.getPosition()));
    }

    private static String getPositionName(Position position) {
        return switch (position) {
            case LEFT -> "⬅️ Слева";
            case CENTER -> "⬆️ Центр";
            case RIGHT -> "➡️ Справа";
        };
    }

    private static Unit findAliveTarget(Unit[] team, Position preferredPosition) {
        for (Unit unit : team) {
            if (unit.getHp() > 0 && unit.getPosition() == preferredPosition) {
                return unit;
            }
        }
        for (Unit unit : team) {
            if (unit.getHp() > 0) {
                return unit;
            }
        }
        return null;
    }

    private static boolean isTeamAlive(Unit[] team) {
        for (Unit unit : team) {
            if (unit.getHp() > 0) {
                return true;
            }
        }
        return false;
    }

    private static void printBattleResults(Unit[] team1, Unit[] team2) {
        System.out.println("\n🎉 БИТВА ЗАВЕРШЕНА!");
        System.out.println("====================");

        boolean team1Alive = isTeamAlive(team1);
        boolean team2Alive = isTeamAlive(team2);

        if (team1Alive && !team2Alive) {
            System.out.println("🏆 ПОБЕДИЛА КОМАНДА 1!");
            printSurvivors(team1);
        } else if (!team1Alive && team2Alive) {
            System.out.println("🏆 ПОБЕДИЛА КОМАНДА 2!");
            printSurvivors(team2);
        } else {
            System.out.println("🤝 НИЧЬЯ! Обе команды пали в бою!");
        }

        System.out.println("\n📊 ФИНАЛЬНАЯ СТАТИСТИКА:");
        printTeamInfo(team1, "Команда 1");
        printTeamInfo(team2, "Команда 2");
    }

    private static void printSurvivors(Unit[] team) {
        System.out.println("🎖️ Выжившие бойцы:");
        for (Unit unit : team) {
            if (unit.getHp() > 0) {
                System.out.println("   ✅ " + unit.getName() + " (" + unit.getClass().getSimpleName() + ") - " + unit.getHp() + " HP");
            }
        }
    }

    private static void waitForContinue() {
        System.out.print("\n⏳ Продолжить? (да/нет): ");
        String input = scanner.next().toLowerCase();

        if (input.equals("да") || input.equals("yes") || input.equals("y") || input.equals("д")) {
            System.out.println("⏳ Продолжаем через 3 секунды...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("👋 Завершение игры...");
            System.exit(0);
        }
    }
}