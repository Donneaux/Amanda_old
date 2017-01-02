package donnoe.amanda;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 *
 * @author joshuadonnoe
 */
public class ClassFile extends Blob {
    public ClassFile(String fileName) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            Amanda.INSTANCE.
            println(String.format("0x%X%n%3$s.%s", in.readInt(), in.readUnsignedShort(), in.readUnsignedShort()));
            Map<Integer, BlockingQueue<Future<Constant>>> qs = IntStream.range(1, in.readUnsignedShort()).boxed().collect(Collectors.toMap(i -> i, i -> new ArrayBlockingQueue<>(1)));
//            Map<Integer, Future<Constant>> CONSTANT_FUTURES = qs.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Amanda.INSTANCE().exec.submit(() -> e.getValue().take().get())));
        }
        catch (IOException x) {
            throw new IOError(x);
        }
    }
}