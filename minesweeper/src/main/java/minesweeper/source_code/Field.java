package minesweeper.source_code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Field {
    private MineBox[][] field;
    private final int WIDTH = 30; //OG 30
    private final int HEIGHT = 16; //OG 16
    private final int MINES = 99;  //OG 99
    private int fieldTotal = 0;


    public Field(ArrayList<int[]> fcb) {
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
        int minesLeft = MINES;
        ArrayList<Boolean> tempField = new ArrayList<Boolean>();
        ArrayList<Integer> mineList = new ArrayList<Integer>();
        for (int i = 0; i < fieldTotal; i++) {
            tempField.add(false);
        }
        //System.out.println("fieldTotal: "+fieldTotal);
        mineList.add(rand.nextInt(fieldTotal));
        int mine = 0;
        for (int i = 0; i < minesLeft; i++) {
            //mine = fieldTotal+1;
            boolean mineDuplicate = true;
            while (mineDuplicate) {
                mineDuplicate = false;
                mine = rand.nextInt(fieldTotal);
                //System.out.println("rolled mine: "+mine);
                for (int j = 0; j < mineList.size(); j++) {
                    if (mine == mineList.get(j)) {
                        mineDuplicate = true;
                        //System.out.println("DUPE FOUND");
                    }
                }
                for (int j = 0; j < fcb.size(); j++) {
                    //System.out.println("FCB: "+(((fcb.get(j)[1]) * WIDTH) + (fcb.get(j)[0]))+" Mine: "+mine);
                    if (mine == ((fcb.get(j)[0] * WIDTH) + fcb.get(j)[1])) {
                        mineDuplicate = true;
                       // System.out.println("Mine equals first click box!");
                    }
                }
                //System.out.println("");
            }
            mineList.add(mine);
            //System.out.println("MINE SET: "+mine);
            tempField.set(mine, true);
            //minesLeft--;
        } 
        //System.out.println("TempFIeld size: "+tempField.size());  

        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                //System.out.println(tempField.get(WIDTH * i + j));
                if (tempField.get((WIDTH * h) + w)) {
                    //System.out.println("index "+HEIGHT * j + i+" is a mine");
                    field[h][w].setMine(true);
                }
            }
        }
        // Test print
        for (int j = 0; j < fcb.size(); j++) {
            System.out.println("FCB: "+(((fcb.get(j)[0]) * WIDTH) + (fcb.get(j)[1])));
        }
    }

    private void setMineNumber() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
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
