package battleship;

public class Cell {
    private boolean isShip;
    private boolean isMiss;
    private boolean isHit;

    Cell() {
        this.isHit = false;
        this.isMiss = false;
        this.isShip = false;
    }

    void setHit() {
        isHit = true;
    }

    boolean isHit() {
        return isHit;
    }

    void setMiss() {
        isMiss = true;
    }

    boolean isMiss() {
        return isMiss;
    }

    void setShip() {
        isShip = true;
    }

    boolean isShip() {
        return isShip;
    }


    String show(boolean withFogOfWar) {
        if (isShip && !isHit && !withFogOfWar) {
            return "O";
        } else if (isHit){
            return "X";
        } else if (isMiss) {
            return "M";
        } else{
            return "~";
        }
    }
}
