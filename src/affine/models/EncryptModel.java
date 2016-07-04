package affine.models;

import java.util.ArrayList;
import java.util.Scanner;

import static affine.models.BaseModel.is_char;

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
                    char dummy = ('A' <= cache && cache <= 'Z') ? 'A' : 'a'; // Assume only char type
                    encrypted_i = cache - dummy;
                    encrypted_i = (keyA * indexes[encrypted_i] + keyB) % 26 + dummy;
                }
                lineCache += (char) (encrypted_i);
            }
            texts.add(lineCache);
        }
        return texts;
    }
}
