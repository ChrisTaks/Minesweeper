package minesweeper.source_code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Field {
    private MineBox[][] field;
    private int WIDTH = 30; //OG 30 - default for expert
    private int HEIGHT = 16; //OG 16 - default for expert
    private int MINES = 99;  //OG 99 - default for expert
    private int fieldTotal = 0;


    public Field(int h, int w, int m, ArrayList<int[]> fcb) {
        setHeight(h);
        setWidth(w);
        setMines(m);
        setField();
        populateMines(fcb);
        setMineNumber();
    }

    private void setField() {
        this.field = new MineBox[HEIGHT][WIDTH];

        // populate field
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                field[i][j] = new MineBox(fieldTotal, j, i);
                fieldTotal++;
            }
        }
    }

    public MineBox[][] getField() {
        return this.field;
    }

    private void populateMines(ArrayList<int[]> fcb) {
        Random rand = new Random();

        ArrayList<Integer> eligibleMines = new ArrayList<Integer>();
        for (int i = 0; i < fieldTotal; i++) {
            eligibleMines.add(i);
            // System.out.println("i: "+i);
        }
        for (int i = 0; i < fcb.size(); i++) {
            for (int j = 0; j < eligibleMines.size(); j++) {
                if ((fcb.get(i)[0] * WIDTH) + fcb.get(i)[1] == eligibleMines.get(j)) {
                    eligibleMines.remove(j);
                }
            }
        }
        // Note: eligibleMine indexes are not equal to their values!
        int mine = 0;
        for (int i = 0; i < MINES; i++) {
            if (eligibleMines.size() > 0) {
                mine = rand.nextInt(eligibleMines.size());
                System.out.println("eligibleMine size: "+eligibleMines.size());
                for (int h = 0; h < HEIGHT; h++) {
                    for (int w = 0; w < WIDTH; w++) {
                        if (eligibleMines.get(mine) == WIDTH * h + w) {
                            field[h][w].setMine(true);
                        }
                    }
                }
                eligibleMines.remove(mine);
            } else {
                for (int j = 1; j < fcb.size(); j++) {
                    if (!field[fcb.get(j)[0]][fcb.get(j)[1]].getIsMine()) {
                            field[fcb.get(j)[0]][fcb.get(j)[1]].setMine(true);
                            break;
                    }
                }
            }
        }
        // // Test print
        // for (int j = 0; j < fcb.size(); j++) {
        //     System.out.println("FCB: "+(((fcb.get(j)[0]) * WIDTH) + (fcb.get(j)[1])));
        // }
    }

    private void setMineNumber() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (field[i][j].getIsMine()) {
                    field[i][j].setMineNumber(9);
                } else {
                    boolean[] mines = new boolean[8];
                    Arrays.fill(mines, false);
                    int topRow = i-1;
                    int bottomRow = i+1;
                    int left = j-1;
                    int right = j+1;
                    // top row
                    if (topRow >= 0) {
                        mines[1] = field[topRow][j].getIsMine();
                        if (left >= 0) {
                            mines[0] = field[topRow][left].getIsMine();
                        }
                        if (right < WIDTH) {
                            mines[2] = field[topRow][right].getIsMine();
                        }
                    }
                    // bottow rom
                    if (bottomRow < HEIGHT) {
                        mines[5] = field[bottomRow][j].getIsMine();
                        if (left >= 0) {
                            mines[6] = field[bottomRow][left].getIsMine();
                        }
                        if (right < WIDTH) {
                            mines[4] = field[bottomRow][right].getIsMine();
                        }
                        
                    }
                    // middle row
                    if (left >= 0) {
                        mines[7] = field[i][left].getIsMine();
                    }
                    if (right < WIDTH) {
                        mines[3] = field[i][right].getIsMine();
                    }

                    int mineNumber = 0;
                    for (int k = 0; k < 8; k++) {
                        if (mines[k]) {
                            mineNumber++;
                        }
                    }
                    field[i][j].setMineNumber(mineNumber);
                }
            }
        }
    }

    public ArrayList<MineBox> getSurroundingTiles(MineBox mb) {
        ArrayList<MineBox> boxes = new ArrayList<MineBox>();
        int w = mb.getW();
        int h = mb.getH();
        int topRow = h-1;
        int bottomRow = h+1;
        int left = w-1;
        int right = w+1;

        if (topRow >= 0) {
            boxes.add(field[topRow][w]);
            if (left >= 0) {
                boxes.add(field[topRow][left]);
            }
            if (right < WIDTH) {
                boxes.add(field[topRow][right]);
            }
        }
        // bottow rom
        if (bottomRow < HEIGHT) {
            boxes.add(field[bottomRow][w]);
            if (left >= 0) {
                 boxes.add(field[bottomRow][left]);
            }
            if (right < WIDTH) {
                 boxes.add(field[bottomRow][right]);
            }
                    
        }
        // middle row
        if (left >= 0) {
            boxes.add(field[h][left]);
        }
        if (right < WIDTH) {
            boxes.add(field[h][right]);
        }
        return boxes;
    }

    public void unCoverEmptyBoxes(MineBox origin) {
        origin.setIsCovered(false);
        ArrayList<MineBox> surroundingBoxes = getSurroundingTiles(origin);
        for (MineBox mb : surroundingBoxes) {
            if (mb.getIsFlagged()) {
                return;
            }
            if (mb.getMineNumber() == 0 && !mb.getIsMine() && mb.getIsCovered()) {
                mb.setIsCovered(false);
                unCoverEmptyBoxes(mb);
            }
            if (!(mb.getMineNumber() == 0) && !mb.getIsMine()) {
                mb.setIsCovered(false);
            }
        }
    }

    public void unCoverAllMines() {
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                if(field[h][w].getIsMine()) {
                    field[h][w].setIsCovered(false);
                }
            }
        }
    }

    public MineBox getMineBox(int h, int w) {
        return field[h][w];
    }


    public void printField() {
        int totalMines = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if(field[i][j].getIndex() < 10 && !field[i][j].getIsMine()) {
                    System.out.print("00"+field[i][j]+" ");
                } else if(field[i][j].getIndex() < 100 && !field[i][j].getIsMine()) {
                    System.out.print("0"+field[i][j]+" ");
                } else {
                    System.out.print(field[i][j]+" ");
                }
                if (field[i][j].getIsMine()) {
                    totalMines++;
                }
            }
            System.out.println();
        }
        System.out.println("\nTotalMines: "+totalMines);
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    public int getMines() {
        return this.MINES;
    }

    public void setWidth(int w) {
        if (w < 10) {
            w = 10;
        }
        this.WIDTH = w;
    }

    public void setHeight(int h) {
        if (h < 10) {
            h = 10;
        }
        this.HEIGHT = h;
    }

    public void setMines(int m) {
        if (m < 1) {
            m = 1;
        }
        if (m > HEIGHT * WIDTH) {
            m = HEIGHT * WIDTH - 1;
        }
        this.MINES = m;
    }

    public boolean getIsFieldClear() {
        int uncovered = 0;
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                if (!field[h][w].getIsCovered()) {
                    uncovered++;
                }
            }
        }
        if (uncovered == WIDTH * HEIGHT - MINES) {
            return true;
        }
        return false;
    }

    // public static void main(String[] args) {
    //     Field field = new Field();
    //     field.printField();
    // } 
}
