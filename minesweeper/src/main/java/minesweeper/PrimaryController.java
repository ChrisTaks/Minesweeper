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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mf = new Field();

        buildGrid();
    }

    private void buildGrid() {

    }

    private StackPane buildMineBox() {
        StackPane mineBox = new StackPane();

        Image background = new Image(getClass().getResourceAsStream(""));

        return mineBox;
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
