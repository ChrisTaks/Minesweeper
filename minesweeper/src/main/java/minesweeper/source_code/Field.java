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


    public Field() {
        setField();
        populateMines();
        setMineNumber();
    }

    private void setField() {
        this.field = new MineBox[HEIGHT][WIDTH];

        // populate field
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                field[i][j] = new MineBox(fieldTotal);
                fieldTotal++;
            }
        }
    }

    private void populateMines() {
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
            }
            mineList.add(mine);
            System.out.println("MINE SET: "+mine);
            tempField.set(mine, true);
            //minesLeft--;
        } 
        //System.out.println("TempFIeld size: "+tempField.size());  

        //WIDTH * i + j;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                //System.out.println(tempField.get(WIDTH * i + j));
                if (tempField.get((HEIGHT * j) + i)) {
                    //System.out.println("index "+HEIGHT * j + i+" is a mine");
                    field[i][j].setMine(true);
                }
            }
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


    private void printField() {
        int totalMines = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(field[i][j]);
                if (field[i][j].getIsMine()) {
                    totalMines++;
                }
            }
            System.out.println();
        }
        System.out.println("\nTotalMines: "+totalMines);
    }

    public static void main(String[] args) {
        Field field = new Field();
        field.printField();
    } 


}
