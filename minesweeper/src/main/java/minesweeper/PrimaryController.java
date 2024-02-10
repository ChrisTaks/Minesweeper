package minesweeper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import minesweeper.source_code.*;

public class PrimaryController implements Initializable{
    private Field mf;
    private MineBox[][] field;
    private ArrayList<int[]> firstClickBoxes = new ArrayList<int[]>();
    private int width = 30; // og 30 - expert mode
    private int height = 16; // og 16 - expert mode
    private int mines = 99; // og 99 - expert mode
    private ObservableList<StackPane> OLTiles = FXCollections.observableArrayList();
    @FXML
    private GridPane gameGrid = new GridPane();
    @FXML
    private GridPane mineFieldGrid = new GridPane();
    @FXML
    private Image tileImage;
    @FXML
    private Image one;
    @FXML
    private Image two;
    @FXML
    private Image three;
    @FXML
    private Image four;
    @FXML
    private Image five;
    @FXML
    private Image six;
    @FXML
    private Image seven;
    @FXML
    private Image eight;
    @FXML
    private Image mine;
    @FXML
    private Image cover;
    @FXML
    private Image flag;
    @FXML
    private Image hPipe;
    @FXML
    private Image vPipe;
    @FXML
    private Image topLeftPipe;
    @FXML
    private Image topRightPipe;
    @FXML
    private Image middleLeftPipe;
    @FXML
    private Image middleRightPipe;
    @FXML
    private Image bottomLeftPipe;
    @FXML
    private Image bottomRightPipe;
    @FXML
    private Pane mainPane;
    @FXML
    private Button newGame;
    @FXML
    private Pane gameOverPane;
    @FXML
    private TextField heightBox;
    @FXML
    private TextField widthBox;

    // TODO: make the mine you chose on a loss different looking (original has red background)
    // TODO: get timer working
    // TODO: get game button picture/game face working (maybe cat faces)
    // TODO: get number of mines working
    // TODO: make resize field + number of mines dynamic (user enterable, next to mine number probaby)
    // TODO: get double click working

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImages();
        buildNewGame();
        newGame = new Button();
        newGame.setText("New Game");
        heightBox = new TextField();
        heightBox.setPrefWidth(40);
        widthBox = new TextField();
        widthBox.setPrefWidth(40);
        mainPane.getChildren().add(gameGrid);
        mainPane.getChildren().add(newGame);
        mainPane.getChildren().add(heightBox);
        mainPane.getChildren().add(mineFieldGrid);
        mainPane.setBackground(Background.EMPTY);
        mineFieldGrid.relocate(19, 98); // (19*2)+(30*2)
        gameGrid.relocate(0, 0);
        heightBox.relocate(90,0);
        newGame.setOnAction(event -> {
            setNewGame();
        });

        // build the game field
        //very top row
        gameGrid.add(new ImageView(topLeftPipe), 0, 0);
        gameGrid.add(new ImageView(topRightPipe), width+1 , 0);
        for (int w = 1; w < width+1; w++) {
            // very top row
            gameGrid.add(new ImageView(hPipe), w, 0);
            // second row
            gameGrid.add(new ImageView(hPipe), w, 3);
            // bottom row
            gameGrid.add(new ImageView(hPipe), w, height+4);
        }
        // vertical pipes around the console box
        for (int h = 1; h < 3; h++) {
            gameGrid.add(new ImageView(vPipe), 0, h);
            gameGrid.add(new ImageView(vPipe), width+1, h);
        }
        // console box bottom corner pipes
        gameGrid.add(new ImageView(middleLeftPipe), 0, 3);
        gameGrid.add(new ImageView(middleRightPipe), width+1, 3);

        // vertical pipes around the mineFieldGrid
        for (int h = 0; h < height; h++) {
            gameGrid.add(new ImageView(vPipe), 0, h+4);
            gameGrid.add(new ImageView(vPipe), width+1, h+4);
        }
        // bottom corners
        gameGrid.add(new ImageView(bottomLeftPipe), 0, height+4);
        gameGrid.add(new ImageView(bottomRightPipe), width+1, height+4);
    }

    private void buildNewGame() {
        mainPane.getChildren().remove(gameOverPane);
        buildFacade();
    }
    
    private void buildMineGrid() {
        OLTiles.clear();
        mf = new Field(firstClickBoxes);
        field = mf.getField();
        mf.printField();
        for (int h = 0; h < mf.getHeight(); h++) {
            for (int w = 0; w < mf.getWidth(); w++) {
                OLTiles.add(buildMineBox(field[h][w]));
            }
        }
    }

    private void drawMineGrid() {
        mineFieldGrid.getChildren().clear();
        for (int h = 0; h < mf.getHeight(); h++) {
            for (int w = 0; w < mf.getWidth(); w++) {
                mineFieldGrid.add(buildMineBox(field[h][w]), w, h);
            }
        }
    }

    private StackPane buildMineBox(MineBox mb) {
        StackPane mineBox = new StackPane();
        ImageView tile = new ImageView(tileImage);
        mineBox.getChildren().add(tile);
        ImageView number = new ImageView();
        switch(mb.getMineNumber()) {
            case 0:
                number.setImage(tileImage);
                break;
            case 1:
                number.setImage(one);
                break;
            case 2:
                number.setImage(two);
                break;
            case 3:
                number.setImage(three);
                break;
            case 4:
                number.setImage(four);
                break;
            case 5:
                number.setImage(five);
                break;
            case 6:
                number.setImage(six);
                break;
            case 7:
                number.setImage(seven);
                break;
            case 8:
                number.setImage(eight);
                break;
            case 9:
                number.setImage(mine);
                break;
        }
        mineBox.getChildren().add(number);
        ImageView tileCover = new ImageView(cover);
        if (mb.getIsCovered()) {
            mineBox.getChildren().add(tileCover);
        }
        ImageView flagCover = new ImageView(flag);
        if (mb.getIsFlagged()) {
            mineBox.getChildren().add(flagCover);
        }
        ImageView doubleClickCover = new ImageView(tileImage);
        if (mb.getIsDoubleClicked() && !mb.getIsFlagged() && mb.getIsCovered()) {
            mineBox.getChildren().add(doubleClickCover);
        }
        ImageView clickCover = new ImageView(tileImage);
        mineBox.setOnMousePressed(event -> {

            // left click
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("left click");
                if (mb.getIsCovered() && !mb.getIsFlagged()) {
                    mineBox.getChildren().add(clickCover);
                }
            }

            // middle click
            if (event.getButton() == MouseButton.MIDDLE) {
                System.out.println("middle click");
                if (!mb.getIsCovered()) {
                    mb.setIsDoubleClicked(true);
                    ArrayList<MineBox> boxes = mf.getSurroundingTiles(mb);
                    for (MineBox box : boxes) {
                        box.setIsDoubleClicked(true);
                    }
                    drawMineGrid();
                }
            }

            // right click
            if (event.getButton() == MouseButton.SECONDARY) {
                System.out.println("right click");
                if (mb.getIsCovered()) {
                    if (mb.getIsFlagged()) {
                        mineBox.getChildren().remove(flagCover);
                        mb.setIsFlagged(false);
                    } else {
                        mineBox.getChildren().add(flagCover);
                        mb.setIsFlagged(true);
                    }
                }
            }
        });

        mineBox.setOnMouseReleased(event -> {
            // left click release
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("leftclick release");
                if (!mb.getIsFlagged() && mb.getIsCovered()) {
                    mineBox.getChildren().remove(clickCover);
                    mineBox.getChildren().remove(tileCover);
                    mb.setIsCovered(false);
                    if (mb.getIsMine()) {
                        // GAME OVER
                        setGameOver();
                    }
                }
                if (mb.getMineNumber() == 0) {
                    // uncover tiles
                    mf.unCoverEmptyBoxes(mb);
                    drawMineGrid();
                }
                checkWinStatus();
            }

            // middle click release
            if (event.getButton() == MouseButton.MIDDLE) {
                System.out.println("middle click release");
                if (!mb.getIsCovered()) {
                    int flaggedBoxes = 0;
                    mb.setIsDoubleClicked(false);
                    ArrayList<MineBox> boxes = mf.getSurroundingTiles(mb);
                    for (MineBox box : boxes) {
                        if (box.getIsFlagged()) {
                            ++flaggedBoxes;
                        }
                        box.setIsDoubleClicked(false);
                    }
                    if (flaggedBoxes == mb.getMineNumber() && mb.getMineNumber() != 0) {
                        for (MineBox box : boxes) {
                            if (!box.getIsFlagged() && box.getIsCovered()) {
                                box.setIsCovered(false);
                                if (box.getIsMine()) {
                                    // GAME OVER
                                    setGameOver();
                                }
                            }
                            if (box.getMineNumber() == 0) {
                                // uncover tiles
                                mf.unCoverEmptyBoxes(box);
                            }
                        }
                    }
                    drawMineGrid();
                    checkWinStatus();
                }
            }
        });

        return mineBox;
    }

    private void setGameOver() {
        mf.unCoverAllMines();
        drawMineGrid();
        gameOverPane = new Pane();
        mainPane.getChildren().add(gameOverPane);
        gameOverPane.setLayoutX(0);
        gameOverPane.setLayoutY(30);
        gameOverPane.setPrefWidth(940);
        gameOverPane.setPrefHeight(620);

    }

    private void buildFacade() {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                mineFieldGrid.add(buildFacadeCoverBox(h, w), w, h);
            }
        }
    }

    private ImageView buildFacadeCoverBox(int h, int w) {
        ImageView fcb = new ImageView(cover);
        fcb.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                fcb.setImage(tileImage);
            }
        });
        fcb.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                firstClickBoxes = getSurroundingTileIndexes(w, h);
                mineFieldGrid.getChildren().remove(fcb);
                buildMineGrid();
                mf.unCoverEmptyBoxes(mf.getMineBox(h, w));
                drawMineGrid();
            }
        });
        return fcb;
    }

    private ArrayList<int[]> getSurroundingTileIndexes(int w, int h) {
        System.out.println("H: "+h);
        System.out.println("W: "+w);
        ArrayList<int[]> boxes = new ArrayList<int[]>();
        int topRow = h-1;
        int bottomRow = h+1;
        int left = w-1;
        int right = w+1;
        int[] click = {h, w};
        boxes.add(click);
        if (topRow >= 0) {
            int[] one = {topRow, w};
            boxes.add(one);
            if (left >= 0) {
                int[] zero = {topRow, left};
                boxes.add(zero);
            }
            if (right < width) {
                int[] two = {topRow, right};
                boxes.add(two);
            }
        }
        // bottow rom
        if (bottomRow < height) {
            int[] five = {bottomRow, w};
            boxes.add(five);
            if (left >= 0) {
                int[] six = {bottomRow, left};
                boxes.add(six);
            }
            if (right < width) {
                int[] four = {bottomRow, right};
                boxes.add(four);
            }
                    
        }
        // middle row
        if (left >= 0) {
            int[] seven = {h, left};
            boxes.add(seven);
        }
        if (right < width) {
            int[] three = {h, right};
            boxes.add(three);
        }
        return boxes;

    }

    private void setImages() {
        tileImage = new Image(getClass().getResourceAsStream("/images/tile.png"));
        one = new Image(getClass().getResourceAsStream("/images/1.png"));
        two = new Image(getClass().getResourceAsStream("/images/2.png"));
        three = new Image(getClass().getResourceAsStream("/images/3.png"));
        four = new Image(getClass().getResourceAsStream("/images/4.png"));
        five = new Image(getClass().getResourceAsStream("/images/5.png"));
        six = new Image(getClass().getResourceAsStream("/images/6.png"));
        seven = new Image(getClass().getResourceAsStream("/images/7.png"));
        eight = new Image(getClass().getResourceAsStream("/images/8.png"));
        mine = new Image(getClass().getResourceAsStream("/images/mine.png"));
        cover = new Image(getClass().getResourceAsStream("/images/cover.png"));
        flag = new Image(getClass().getResourceAsStream("/images/flag.png"));
        //border
        vPipe = new Image(getClass().getResourceAsStream("/images/v_pipe.png"));
        hPipe = new Image(getClass().getResourceAsStream("/images/h_pipe.png"));
        topLeftPipe = new Image(getClass().getResourceAsStream("/images/top_left_pipe.png"));
        topRightPipe = new Image(getClass().getResourceAsStream("/images/top_right_pipe.png"));
        middleLeftPipe = new Image(getClass().getResourceAsStream("/images/middle_left_pipe.png"));
        middleRightPipe = new Image(getClass().getResourceAsStream("/images/middle_right_pipe.png"));
        bottomLeftPipe = new Image(getClass().getResourceAsStream("/images/bottom_left_pipe.png"));
        bottomRightPipe = new Image(getClass().getResourceAsStream("/images/bottom_right_pipe.png"));


    }

    private void setNewGame() {
        buildNewGame();
    }

    private void checkWinStatus() {
        if (mf.getIsFieldClear()) {
                    // yay you won
                    setGameOver();
                }
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
