package affine.models;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import static affine.models.DecryptModel.decrypt;
import static affine.models.EncryptModel.encrypt;

/**
 * Created by ebolo on 6/29/16.
 */
public class BaseModel {
    public enum OPMODE {ENCRYPT, DECRYPT};

    public static void operate(String fileKey, String fileTxt, OPMODE opmode) {
        int[] indexes = new int[26]; int a = 1, b = 0;
        Scanner buffer;
        boolean op_sus = true;

        try { //Read key data
            buffer = new Scanner(Paths.get(fileKey)); int i;
            String line = buffer.nextLine(); String cache = "";
            if (is_char(line.charAt(0))) {
                a = 1; b = 0;
            } else {
                for (i = 0; i < line.length(); i++) {
                    if ('0' <= line.charAt(i) && line.charAt(i) <= '9')
                        cache += line.charAt(i);
                    else break;
                }
                a = Integer.parseInt(cache);

                cache = "";
                i++;
                for (; i < line.length(); i++) {
                    if ('0' <= line.charAt(i) && line.charAt(i) <= '9')
                        cache += line.charAt(i);
                    else break;
                }
                b = Integer.parseInt(cache);
                line = buffer.nextLine(); //Skip through alphabet line
            }

            line = buffer.nextLine();
            cache = ""; int index = 0;
            for (i = 0; i < line.length(); i++) {
                if ('0' <= line.charAt(i) && line.charAt(i) <= '9')
                    cache += line.charAt(i);
                else {
                    indexes[index] = Integer.parseInt(cache);
                    if (indexes[index] < 0 || indexes[index] > 25) {
                        op_sus = false;
                        showErrorDialog(Alert.AlertType.ERROR, "Key File Error!",
                                "Indexes are not valid! Please check again!");
                        break;
                    }
                    index++;
                    cache = "";
                }
            }
            indexes[index] = Integer.parseInt(cache);
            if (indexes[index] < 0 || indexes[index] > 25) {
                op_sus = false;
                showErrorDialog(Alert.AlertType.ERROR, "Key File Error!",
                        "Indexes are not valid! Please check again!");
            }
        } catch (Exception e) {
            op_sus = false;
            String errorMess = (fileKey.length() <= 0)?
                    "Missing path for Key file!" :
                    "The Key file path is invalid:\n" + fileKey;
            showErrorDialog(Alert.AlertType.ERROR, "Key File Not Found!", errorMess);
        }

        if (op_sus) {
            String savePath;

            if ((a % 2 == 0 || a % 13 == 0) && (b < 0 || b > 25) && (a < 0 || a > 25)) {
                op_sus = false;
                String errorMess = "This Key file is not valid for encrypting/decrypting! Please check again!";
                errorMess += "\nPath: " + fileKey + "\nKey A value = " + a + "\nKey B value = " + b;
                showErrorDialog(Alert.AlertType.ERROR, "Key File Error!", errorMess);
            } else try {
                savePath = fileTxt.substring(0, fileTxt.length() - 4);
                savePath += (opmode == OPMODE.ENCRYPT) ? "_encrypted.txt" : "_decrypted.txt";
                buffer = new Scanner(Paths.get(fileTxt));
                ArrayList<String> texts;
                if (opmode == OPMODE.ENCRYPT)
                    texts = encrypt(a, b, indexes, buffer);
                else
                    texts = decrypt(a, b, indexes, buffer);
                if (texts != null)
                    Files.write(Paths.get(savePath), texts, Charset.forName("UTF-8"));
                else op_sus = false;
            } catch (Exception e) {
                op_sus = false;
                String errorMess = (fileTxt.length() <= 0) ?
                        "Missing path for Text file!" :
                        "The Text file path is invalid:\n" + fileTxt;
                showErrorDialog(Alert.AlertType.ERROR, "Text File Not Found!", errorMess);
            }
        }

        if (op_sus) {
            String errorMess = "File:\n" + fileTxt;
            errorMess += (opmode == OPMODE.ENCRYPT)? "\nEncrypted: " : "\nDecrypted: ";
            errorMess += '\n' + fileTxt.substring(0, fileTxt.length() - 4);
            errorMess += (opmode == OPMODE.ENCRYPT)? "_encrypted.txt" : "_decrypted.txt";
            showErrorDialog(Alert.AlertType.INFORMATION, "Successful!", errorMess);
        } else {

        }
    }

    public static boolean is_char(char c) {
        return (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z'));
    }

    public static int calcAI(int a) {
        int i;
        for (i = 0; i < 26; i++) {
            if ((a * i) % 26 == 1)
                break;
        }
        return i;
    }

    public static void showErrorDialog(Alert.AlertType alertType, String title, String errorMess) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(errorMess);
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.showAndWait();
    }
}
