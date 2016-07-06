package affine.main;

import affine.models.BaseModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

import static affine.main.Main.mainStage;

public class Controller {
    @FXML
    private TextField pathKey;
    @FXML
    private TextField pathTxt;
    private File keyFile, textFile;
    private FileChooser fileChooser = new FileChooser();

    @FXML
    private void browseKey() {
        fileChooser.setTitle("Open Key File");
        fileChooser.getExtensionFilters().remove(0, fileChooser.getExtensionFilters().size());
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Affine Cipher Key", "*.txt"));
        keyFile = fileChooser.showOpenDialog(mainStage);
        if (keyFile != null) {
            pathKey.setText(keyFile.getPath());
        }
    }

    @FXML
    private void browseTxt() {
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().remove(0, fileChooser.getExtensionFilters().size());
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text File", "*.txt"));
        textFile = fileChooser.showOpenDialog(mainStage);
        pathTxt.setText(textFile.getPath());
    }

    @FXML
    private void encrypt() {
        BaseModel.operate(pathKey.getText(), pathTxt.getText(), BaseModel.OPMODE.ENCRYPT);
    }

    @FXML
    private void decrypt() {
        BaseModel.operate(pathKey.getText(), pathTxt.getText(), BaseModel.OPMODE.DECRYPT);
    }
}
