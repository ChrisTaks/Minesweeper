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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import minesweeper.source_code.*;

public class PrimaryController implements Initializable{
    private Field mf;
    private MineBox[][] field;
    private ArrayList<int[]> firstClickBoxes = new ArrayList<int[]>();
    private int width = 30;
    private int height = 16;
    private boolean leftClick = false;
    private boolean rightClick = false;
    private boolean doubleClick = false;
    private ObservableList<StackPane> OLTiles = FXCollections.observableArrayList();
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
    private Pane mainPane;
    @FXML
    private Button newGame;
    @FXML
    private Pane gameOverPane;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImages();
        buildNewGame();
        newGame = new Button();
        newGame.setText("New Game");
        mainPane.getChildren().add(newGame);
        mineFieldGrid.relocate(0, 30);
        newGame.setOnAction(event -> {
            setNewGame();
        });
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
        if (mb.getIsDoubleClicked() && !mb.getIsFlagged()) {
            mineBox.getChildren().add(doubleClickCover);
        }
        ImageView clickCover = new ImageView(tileImage);
        mineBox.setOnMousePressed(event -> {
            // both buttons pressed
            if (leftClick && event.getButton() == MouseButton.SECONDARY) {
                System.out.println("BOTH BUTTONS CLICKED");
                doubleClick = true;
                ArrayList<MineBox> boxes = mf.getSurroundingTiles(mb);
                for (MineBox box : boxes) {
                    box.setIsDoubleClicked(true);
                }
                drawMineGrid();
                leftClick = false;
            }

            if (event.getButton() == MouseButton.PRIMARY) {
                leftClick = true;
                if (mb.getIsCovered() && !mb.getIsFlagged()) {
                    mineBox.getChildren().add(clickCover);
                }
            }
            if (!doubleClick && event.getButton() == MouseButton.SECONDARY) {
                System.out.println("DOUBLE CLICK RELEASE 2");
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
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("leftclick release");
                leftClick = false;
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
            }
            if (doubleClick && event.getButton() == MouseButton.SECONDARY) {
                System.out.println("DOUBLE CLICK RELEASE");
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
    }

    private void setNewGame() {
        buildNewGame();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
