package minesweeper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import minesweeper.source_code.*;

public class PrimaryController implements Initializable{
    private Field mf;
    @FXML
    private GridPane mineFieldGrid = new GridPane();
    @FXML
    private ImageView tile;
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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mf = new Field();
        setImages();
        buildGrid();
    }

    private void buildGrid() {
        MineBox[][] field = mf.getField();
        for (int i = 0; i < mf.getHeight(); i++) {
            for (int j = 0; j < mf.getWidth(); j++) {
                buildMineBox(field[i][j]);
                mineFieldGrid.add(buildMineBox(field[i][j]), i, j);
            }
        }
    }

    private StackPane buildMineBox(MineBox mb) {
        StackPane mineBox = new StackPane();
        tile.setImage(tileImage);
        mineBox.getChildren().add(tile);
        ImageView number = new ImageView();
        switch(mb.getMineNumber()) {
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
            default:
                number.setImage(mine);
                break;
        }
        number.setImage(one);
        mineBox.getChildren().add(number);
        return mineBox;
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

    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
