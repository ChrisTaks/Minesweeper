package minesweeper.source_code;

public class MineBox {
    private int index;
    private int w;
    private int h;
    private boolean isMine;
    private int mineNumber;
    private boolean isCovered;
    private boolean isFlagged;
    private boolean isDoubleClicked;
    
    public MineBox(int n, int w, int h) {
        setIsFlagged(false);
        setIndex(n);
        setW(w);
        setH(h);
        setIsCovered(true);
        setIsDoubleClicked(false);
    }

    private void setIndex(int n) {
        this.index = n;
    }

    public int getIndex() {
        return this.index;
    }

    private void setW(int w) {
        this.w = w;
    }

    private void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return this.w;
    }

    public int getH() {
        return this.h;
    }

    public void setIsCovered(boolean bool) {
        if (!isFlagged) {
            this.isCovered = bool;
        }
    }

    public boolean getIsCovered() {
        return this.isCovered;
    }

    public void setIsFlagged(boolean bool) {
        this.isFlagged = bool;
    }

    public boolean getIsFlagged() {
        return this.isFlagged;
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
        // if (isMine) {
        //     return 9;    
        // }
        return this.mineNumber;
    }

    public void setIsDoubleClicked(boolean bool) {
        this.isDoubleClicked = bool;
    }

    public boolean getIsDoubleClicked() {
        return this.isDoubleClicked;
    }

    public String toString() {
        if (isMine) {
            return "[X]";
        }
        return String.valueOf(index);
        //return "["+mineNumber+"]";
    }
}
