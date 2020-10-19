package battleship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Field {
    private final Cell[][] battleField = new Cell[10][10];
    private final Map<String, List<String>> shipLocations = new HashMap<>(5);

    Field() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                battleField[i][j] = new Cell();
            }
        }
    }

    void putLocation(String shipType, List<String> locations) {
        shipLocations.put(shipType, locations);
    }

    Set<Map.Entry<String, List<String>>> getShipLocationsEntrySet() {
        return shipLocations.entrySet();
    }

    void removeLocation(String key) {
        shipLocations.remove(key);
    }

    boolean isEmpty() {
        return shipLocations.isEmpty();
    }

    void drawField(boolean withFogOfWar) {
        int colCounter = 1;
        char rowCounter = 'A';
        System.out.println();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j != 0) {
                    System.out.print(colCounter++ + " ");
                } else if (j == 0 && i != 0) {
                    System.out.print(rowCounter++ + " ");
                } else if (i == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(battleField[i - 1][j - 1].show(withFogOfWar) + " ");
                }
            }
            System.out.println();
        }
    }

    Cell getCell(int row, int col) {
        return battleField[row][col];
    }
}
