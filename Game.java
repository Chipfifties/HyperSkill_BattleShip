package battleship;

import java.io.IOException;
import java.util.*;

public class Game {
    private static final Scanner sc = new Scanner(System.in);
    private final Field[] battleField = new Field[]{new Field(), new Field()};
    private final Map<String, Integer> shipTypes = new LinkedHashMap<>(5);
    private static final int[] startCoordinates = new int[2];
    private static final int[] endCoordinates = new int[2];
    private static final int[] shotCoordinates = new int[2];
    private static String alignment;

    Game() {
        shipTypes.put("Aircraft Carrier", 4);
        shipTypes.put("BattleShip", 3);
        shipTypes.put("Submarine", 2);
        shipTypes.put("Cruiser", 2);
        shipTypes.put("Destroyer", 1);
    }

    void run() {
        for (int i = 0; i < 2; i++) {
            System.out.println("Player " + (i + 1) + ", place your ships on the game field");
            battleField[i].drawField(false);
            for (Map.Entry<String, Integer> entry : shipTypes.entrySet()) {
                System.out.printf("\nEnter the coordinates of the %s (%d cells):\n", entry.getKey(), entry.getValue() + 1);
                placeShip(entry.getKey(), entry.getValue(), battleField[i]);
            }
            System.out.println("Press Enter and pass the move to another player");
            String pass = sc.nextLine();
            clearScreen();
        }

        while (true) {
            for (int i = 0; i < 2; i++) {
                int enemyField = i == 0 ? 1 : 0;
                battleField[enemyField].drawField(true);
                System.out.println("---------------------");
                battleField[i].drawField(false);

                System.out.println("Player " + (i + 1) + ", it's your turn:");

                while (true) {
                    if(isValidShotCoordinates(sc.nextLine().toUpperCase())){
                        break;
                    }
                }

                if (battleField[enemyField].getCell(shotCoordinates[0], shotCoordinates[1]).isShip()) {

                    battleField[enemyField].getCell(shotCoordinates[0], shotCoordinates[1]).setHit();

                    if (shipSank(shotCoordinates[0], shotCoordinates[1], battleField[enemyField])) {
                        if (battleField[enemyField].isEmpty()) {
                            System.out.println("\nYou sank the last ship. You won. Congratulations!");
                            return;
                        }
                        System.out.println("\nYou sank a ship!");
                    } else {
                        System.out.println("\nYou hit a ship!");
                    }
                } else {
                    battleField[enemyField].getCell(shotCoordinates[0], shotCoordinates[1]).setMiss();
                    System.out.println("\nYou missed!");
                }
                nextTurn();
            }
        }
    }

    private void nextTurn() {
        System.out.println("Press Enter and pass the move to another player");
        String pass = sc.nextLine();
        clearScreen();
    }

    private boolean shipSank(int x, int y, Field field) {
        for (Map.Entry<String, List<String>> entry : field.getShipLocationsEntrySet()) {
            if (entry.getValue().contains(x + " " + y)) { ;
                int hitCounter = 0;
                for (String value : entry.getValue()) {
                    int row = Integer.parseInt(value.substring(0, 1));
                    int col = Integer.parseInt(value.substring(2));
                    if (field.getCell(row, col).isHit()) {
                        hitCounter++;
                    }
                }
                if (hitCounter == shipTypes.get(entry.getKey()) + 1) {
                    field.removeLocation(entry.getKey()); // remove sunken ship from the map
                    return true;
                }
            }
        }
        return false;
    }

    boolean isValidShotCoordinates(String shotCoords) {
        if (shotCoords.matches("[A-J](10|[0-9])")) {
            shotCoordinates[0] = shotCoords.charAt(0) - 65;
            shotCoordinates[1] = Integer.parseInt(shotCoords.substring(1)) - 1;
            return true;
        } else {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }
    }

    void placeShip(String shipType, int shipLength, Field field) {
        while (true) {
            System.out.println();
            String coordinates = sc.nextLine().toUpperCase();
            if (!processCoordinates(coordinates, shipLength, shipType, field)) {
                continue;
            }

            List<String> location = new ArrayList<>(); // List of ship's coordinates

            switch (alignment) {
                case "horizontal":
                    int shipStart = Math.min(startCoordinates[1], endCoordinates[1]);
                    int shipEnd = Math.max(startCoordinates[1], endCoordinates[1]);

                    for (int i = shipStart; i <= shipEnd; i++) {
                        field.getCell(startCoordinates[0], i).setShip();
                        location.add(startCoordinates[0] + " " + i);
                    }
                    break;
                case "vertical":
                    shipStart = Math.min(startCoordinates[0], endCoordinates[0]);
                    shipEnd = Math.max(startCoordinates[0], endCoordinates[0]);

                    for (int i = shipStart; i <= shipEnd; i++) {
                        field.getCell(i, startCoordinates[1]).setShip();
                        location.add(i + " " + startCoordinates[1]);
                    }
                    break;
            }

            field.putLocation(shipType, location);
            field.drawField(false);
            return;
        }
    }

    boolean processCoordinates(String coordinates, int shipLength, String shipType, Field field) {
        if (!coordinates.matches("[A-J](10|[0-9]) [A-J](10|[0-9])")) {
            System.out.println("Error! Wrong coordinates! Try again:");
            return false;
        }

        String[] coords = coordinates.split(" ");
        startCoordinates[0] = coords[0].charAt(0) - 65;
        startCoordinates[1] = Integer.parseInt(coords[0].substring(1)) - 1;
        endCoordinates[0] = coords[1].charAt(0) - 65;
        endCoordinates[1] = Integer.parseInt(coords[1].substring(1)) - 1;

        if (startCoordinates[0] == endCoordinates[0]) {
            alignment = "horizontal";
            if (Math.abs(startCoordinates[1] - endCoordinates[1]) == shipLength) {
                return checkNeighbours(field);
            } else {
                System.out.println("Error! Wrong length of the " + shipType + "! Try again:");
            }
        } else if (startCoordinates[1] == endCoordinates[1]) {
            alignment = "vertical";
            if (Math.abs(startCoordinates[0] - endCoordinates[0]) == shipLength) {
                return checkNeighbours(field);
            } else {
                System.out.println("Error! Wrong length of the " + shipType + "! Try again:");
            }
        } else {
            System.out.println("Error! Wrong length of the " + shipType + "! Try again:");
        }

        return false;
    }

    boolean checkNeighbours(Field field) {
        switch (alignment) {
            case "horizontal":
                int shipStart = Math.min(startCoordinates[1], endCoordinates[1]) - 1;
                int shipEnd = Math.max(startCoordinates[1], endCoordinates[1]) + 1;

                for (int i = startCoordinates[0] - 1; i <= startCoordinates[0] + 1; i++) {
                    for (int j = shipStart; j <= shipEnd; j++) {
                        try {
                            if (field.getCell(i, j).isShip()) {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                return false;
                            }
                        } catch (IndexOutOfBoundsException ignored) {
                        }
                    }
                }
                break;
            case "vertical":
                shipStart = Math.min(startCoordinates[0], endCoordinates[0]) - 1;
                shipEnd = Math.max(startCoordinates[0], endCoordinates[0]) + 1;

                for (int i = shipStart; i <= shipEnd; i++) {
                    for (int j = startCoordinates[1] - 1; j <= startCoordinates[1] + 1; j++) {
                        try {
                            if (field.getCell(i, j).isShip()) {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                return false;
                            }
                        } catch (IndexOutOfBoundsException ignored) {
                        }
                    }
                }
                break;
        }
        return true;
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
