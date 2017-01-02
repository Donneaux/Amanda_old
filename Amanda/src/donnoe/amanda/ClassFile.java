package donnoe.amanda;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import static donnoe.amanda.Amanda.println;
/**
 *
 * @author joshuadonnoe
 */
public class ClassFile extends Blob {
    public ClassFile(String fileName) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            println(String.format("0x%X%n%3$s.%s", in.readInt(), in.readUnsignedShort(), in.readUnsignedShort()));
            //next is constant pool (inluding null) count as u2
        }
        catch (IOException x) {
            throw new IOError(x);
        }
    }
}