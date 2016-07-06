package affine.models;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Scanner;

import static affine.models.BaseModel.is_char;
import static affine.models.BaseModel.showErrorDialog;

/**
 * Created by ebolo on 6/29/16.
 */
public class EncryptModel {
    public static ArrayList<String> encrypt(int keyA, int keyB, int[] indexes, Scanner buffer) {
        ArrayList<String> texts = new ArrayList<>();
        String line;
        while (buffer.hasNextLine()) {
            line = buffer.nextLine();
            String lineCache = "";
            for (char cache : line.toCharArray()) {
                int encrypted_i = cache;
                if (is_char(cache)) {
                    if ('a' <= cache && cache <= 'z')
                        cache -= ('a' - 'A');
                    //char dummy = ('A' <= cache && cache <= 'Z') ? 'A' : 'a'; // Assume only char type
                    encrypted_i = cache - 'A';
                    encrypted_i = (keyA * indexes[encrypted_i] + keyB) % 26 + 'A';
                    lineCache += (char) (encrypted_i);
                } else if (cache != ' ') {
                    showErrorDialog(Alert.AlertType.ERROR, "Plain text error",
                            "The plain text must not contain numbers or special characters!");
                    return null;
                }
            }
            texts.add(lineCache);
        }
        return texts;
    }
}
