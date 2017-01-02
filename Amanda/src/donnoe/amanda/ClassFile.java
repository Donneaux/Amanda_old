package donnoe.amanda;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.*;
import static donnoe.amanda.Amanda.INSTANCE;
import static donnoe.util.Functions.ExceptionalFunction;
/**
 *
 * @author joshuadonnoe
 */
public final class ClassFile extends Blob {

    public ClassFile(String fileName) {
        try {
            in = new DataInputStream(new FileInputStream(fileName));
        } catch (IOException x) {
            throw new IOError(x);
        }
        INSTANCE.println(String.format("0x%X%n%3$s.%s", readInt(), readUnsignedShort(), readUnsignedShort()));
        Map<Integer, BlockingQueue<Future<Constant>>> qs = IntStream.range(1, readUnsignedShort()).boxed().collect(Collectors.toMap(i -> i, i -> new ArrayBlockingQueue<>(1)));
//        Map<Integer, Future<Constant>> CONSTANT_FUTURES = qs.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Amanda.INSTANCE().exec.submit(() -> e.getValue().take().get())));
    }

    private DataInputStream in;
    
    public int readInt() {
        return read(DataInputStream::readInt);
    }
    
    public int readUnsignedShort() {
        return read(DataInputStream::readUnsignedShort);
    }
    
    private <T> T read(ExceptionalFunction<DataInputStream, T, IOException> f) {
        try {
            return f.apply(in);
        } catch (IOException x) {
            throw new IOError(x);
        }
    }
}
