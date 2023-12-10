package minesweeper.source_code;

public class MineBox {
    private int index;
    private boolean isMine;
    private int mineNumber;
    
    public MineBox(int n) {
        setIndex(n);
    }

    private void setIndex(int n) {
        this.index = n;
    }

    public int getIndex() {
        return this.index;
    }

    public void setMine(Boolean m) {
        this.isMine = m;
    }

    public boolean getIsMine() {
        return this.isMine;
    }

    public void setMineNumber(int n) {
        this.mineNumber = n;
    }

    public int getMineNumber() {
        if (isMine) {
            return 9;    
        }
        return this.mineNumber;
    }

    public String toString() {
        if (isMine) {
            return "[X]";
        }
        //return String.valueOf(index);
        return "["+mineNumber+"]";
    }
}
