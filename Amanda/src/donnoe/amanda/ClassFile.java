package donnoe.amanda;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;

/**
 *
 * @author joshuadonnoe
 */
public class ClassFile extends Blob {
    public ClassFile(String fileName) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
        }
        catch (IOException x) {
            throw new IOError(x);
        }
    }
}